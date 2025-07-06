package com.gitee.swsk33.swmmsensorthings.eventbus.model;

import com.gitee.swsk33.swmmsensorthings.eventbus.util.DoubleValueUtils;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import io.github.swsk33.swmmjava.SWMM;
import io.github.swsk33.swmmjava.model.RainGage;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import static com.gitee.swsk33.swmmsensorthings.mapper.util.NameUtils.getObservationSensorName;
import static io.github.swsk33.swmmjava.param.ObjectTypeCode.GAGE;

/**
 * 数据缓存池，用于接收传感器数据并对齐每个时间步长<br>
 * 一个水文模型实例（的多个传感器）对应一个该缓存池对象
 */
@Slf4j
public class ObservationPool {

	/**
	 * 水文模型对象
	 */
	private final SWMM swmm;

	/**
	 * 当前模型包含的传感器个数
	 */
	private final int sensorCount;

	/**
	 * 缓存接收到的观测数据的线程安全队列<br>
	 * 根据观测时间排列，从队头到队尾的时间是从旧到新
	 */
	private final PriorityBlockingQueue<Observation> dataCacheQueue;

	/**
	 * 当前时间步长及其数据
	 */
	private final TimeStepData currentStepData;

	/**
	 * 构造函数
	 *
	 * @param swmm 输入SWMM对象
	 */
	public ObservationPool(SWMM swmm) {
		// 初始化水文模型
		this.swmm = swmm;
		// 初始化传感器数量
		this.sensorCount = this.swmm.getObjectList(GAGE).size();
		// 初始化数据队列
		this.dataCacheQueue = new PriorityBlockingQueue<>(100, Comparator.comparing(Observation::getPhenomenonTime));
		// 获取当前时间步长并初始化当前时间步长数据
		this.currentStepData = new TimeStepData(this.swmm.getSystem().getCurrentStep());
	}

	/**
	 * 更新当前步长的观测数据
	 */
	private void updateStepData() {
		// 查看队头数据是否位于当前时间步长
		Observation headData = this.dataCacheQueue.peek();
		if (headData == null) {
			return;
		}
		// 首先循环丢弃旧观测数据
		while (headData != null && this.currentStepData.afterDataTime(headData)) {
			log.warn("数据流：{}的数据：{}早于当前时间步长，将会丢弃...", headData.getDatastream().getId(), headData.getId());
			this.dataCacheQueue.poll();
			headData = this.dataCacheQueue.peek();
		}
		// 然后循环收集位于当前时间步长的数据
		while (headData != null && this.currentStepData.stepContains(headData)) {
			this.currentStepData.addData(headData);
			this.dataCacheQueue.poll();
			headData = this.dataCacheQueue.peek();
		}
	}

	/**
	 * 检查当前时间步长数据是否已集齐
	 *
	 * @return 当前时间步长内是否已集齐全部传感器数据
	 */
	public boolean checkCurrentDataAvailable() {
		return this.currentStepData.getDataCount() >= this.sensorCount;
	}

	/**
	 * 将当前时间步长数据输入到水文模型并驱动水文模型运行一次
	 */
	public void doSimulation() {
		// 判断是否结束
		if (this.swmm.isComplete()) {
			log.error("水文模拟已结束！");
			return;
		}
		// 取出数据
		List<Observation> dataList = new ArrayList<>(this.currentStepData.getDataMap().values());
		// 解析数据
		for (Observation data : dataList) {
			// 解析传感器与属性名
			String sensor = getObservationSensorName(data.getDatastream().getName());
			// 输入水文模型
			try {
				// 获取雨量计对象
				VisualObject gage = this.swmm.getObject(GAGE, sensor);
				if (gage == null) {
					log.error("找不到传感器：{}", sensor);
					return;
				}
				// 输入数据
				RainGage patch = new RainGage();
				patch.setIndex(gage.getIndex());
				patch.setRainfall(DoubleValueUtils.parseDouble(data.getResult()));
				this.swmm.setValue(patch);
			} catch (Exception e) {
				log.error("设定数据到水文模型失败！");
				log.error(e.getMessage());
			}
		}
		// 执行一个步长
		this.swmm.step();
		log.info("已输入并执行完成一个时间步长：{}", this.currentStepData.getTimeStep());
		// 将当前时间步长数据置为下一步长并置空数据
		this.currentStepData.setTimeStep(this.swmm.getSystem().getCurrentStep());
		this.currentStepData.getDataMap().clear();
	}

	/**
	 * 添加一个观测数据到缓存队列
	 *
	 * @param data 观测数据
	 */
	public void addData(Observation data) {
		this.dataCacheQueue.add(data);
	}

	/**
	 * 完成整个时间步长数据对齐，并输入水文模型运行一个步长，该方法用于串联上述所有步长方法，主要是：
	 * <ol>
	 *     <li>触发队列检查，位于当前时间步长的数据从队列取出，存入当前时间步长数据列表中</li>
	 *     <li>若当前时间步长已集齐，或者最新数据时间领先了当前1个时间步长，则驱动水文模型运行</li>
	 * </ol>
	 * 整个流程确保线程安全
	 */
	public synchronized void runModel() {
		// 收集当前时间步的数据
		updateStepData();
		if (checkCurrentDataAvailable() || this.dataCacheQueue.size() / this.sensorCount > 1) {
			doSimulation();
		}
	}

}