package com.gitee.swsk33.swmmsensorthings.eventbus.model;

import com.gitee.swsk33.swmmsensorthings.model.Observation;
import io.github.swsk33.swmmjava.model.TimeStep;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 表示一个时间步长内、所有传感器的观测数据<br>
 * 在SWMM中，假设同一时间步长内，一个传感器有且只有一个观测数据
 */
@Data
public class TimeStepData {

	/**
	 * 当前时间步长
	 */
	private TimeStep timeStep;

	/**
	 * 当前时间步长内，全部传感器对应的观测数据<br>
	 * key：当前数据所属数据流id<br>
	 * value：当前数据本身
	 */
	private Map<Object, Observation> dataMap = new HashMap<>();

	/**
	 * 构造函数
	 *
	 * @param timeStep 设定当前时间步长
	 */
	public TimeStepData(TimeStep timeStep) {
		this.timeStep = timeStep;
	}

	/**
	 * 添加一个观测数据到当前时间步长数据列表
	 *
	 * @param data 观测数据
	 */
	public void addData(Observation data) {
		dataMap.put(data.getDatastream().getId(), data);
	}

	/**
	 * 判断传入数据是否位于当前时间步长内
	 *
	 * @param data 传入数据
	 * @return 是否位于当前时间步长
	 */
	public boolean stepContains(Observation data) {
		return timeStep.contains(data.getPhenomenonTime());
	}

	/**
	 * 判断当前时间步长是否位于传入数据之后（传入数据是否是旧的数据）
	 *
	 * @param data 传入数据
	 * @return 当前时间步长是否在传入数据的时间之后
	 */
	public boolean afterDataTime(Observation data) {
		return timeStep.getStart().isAfter(data.getPhenomenonTime());
	}

	/**
	 * 获取当前数据个数
	 *
	 * @return 数据个数
	 */
	public int getDataCount() {
		return dataMap.size();
	}

}