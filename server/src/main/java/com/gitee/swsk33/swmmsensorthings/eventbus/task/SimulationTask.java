package com.gitee.swsk33.swmmsensorthings.eventbus.task;

import cn.hutool.core.util.IdUtil;
import com.gitee.swsk33.swmmsensorthings.eventbus.client.SensorThingsObjectClient;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Job;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.constant.JobStatus;
import com.gitee.swsk33.swmmsensorthings.eventbus.param.SensorThingsExpandProperty;
import com.gitee.swsk33.swmmsensorthings.eventbus.subscriber.mqtt.RainGageSubscriber;
import com.gitee.swsk33.swmmsensorthings.eventbus.subscriber.reactor.SensorThingsSubscriber;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.DatastreamFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.impl.FeatureOfInterestFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.util.NameUtils;
import com.gitee.swsk33.swmmsensorthings.mapper.util.PropertyReadUtils;
import com.gitee.swsk33.swmmsensorthings.model.Datastream;
import com.gitee.swsk33.swmmsensorthings.model.FeatureOfInterest;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import com.gitee.swsk33.swmmsensorthings.model.Sensor;
import io.github.swsk33.swmmjava.SWMM;
import io.github.swsk33.swmmjava.model.Subcatchment;
import io.github.swsk33.swmmjava.model.VisualObject;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.github.swsk33.swmmjava.param.ObjectTypeCode.*;

/**
 * 水文模型异步模拟任务对象
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SimulationTask implements Runnable, InitializingBean {

	/**
	 * 任务id
	 */
	@Getter
	private final String id;

	/**
	 * 模拟执行任务的状态<br>
	 * 符合OGC API-Processes的任务状态描述
	 */
	@Getter
	private final Job job;

	/**
	 * 正在执行的水文模型实例
	 */
	private final SWMM swmm;

	/**
	 * 存放当前模型订阅的全部主题列表，一个主题对应一个雨量计对象
	 */
	private final List<String> subscribedTopics;

	@Autowired
	private MqttClient mqttClient;

	@Autowired
	private SensorThingsObjectClient client;

	@Autowired
	private BeanFactory beanFactory;

	@Resource(name = "jobList")
	private Map<String, Job> jobList;

	/**
	 * 构造函数
	 *
	 * @param inputFile SWMM模型的输入文件路径
	 */
	public SimulationTask(String inputFile) {
		// 随机id
		this.id = IdUtil.simpleUUID();
		// 初始化水文模型
		this.swmm = new SWMM(inputFile);
		this.swmm.start();
		// 初始化订阅者列表
		this.subscribedTopics = new ArrayList<>();
		// 初始化Job对象
		this.job = new Job();
		job.setJobID(this.id);
		job.setStatus(JobStatus.ACCEPTED);
		job.setProcessID("swmm");
		job.setStarted(LocalDateTime.now());
		job.setProgress(0);
	}

	@Override
	public void afterPropertiesSet() {
		// 存入列表
		jobList.put(job.getJobID(), job);
	}

	/**
	 * 水文模型创建初始化操作
	 */
	private void initialize() throws Exception {
		// 1. 获取全部SWMM对象并转换为对应的SensorThings API对象注册
		List<VisualObject> objects = swmm.getAllObjects();
		// 遍历初始化
		for (VisualObject object : objects) {
			// 若对象对应的传感器（实体或者虚拟）存在，说明该对象对应的全部SensorThings对象存在，不进行该对象初始化
			if (client.existByName(object.getId(), Sensor.class)) {
				log.warn("对象{}所对应的SensorThings对象已存在，跳过其转换映射与初始化步骤！", object.getId());
				continue;
			}
			// 如果是子汇水区域，则转换成兴趣要素并发送到服务器
			if (Subcatchment.class.isAssignableFrom(object.getClass())) {
				FeatureOfInterest featureOfInterest = (FeatureOfInterest) FeatureOfInterestFactory.getInstance().createObject(object);
				if (!client.add(featureOfInterest)) {
					log.error("添加兴趣要素{}时出错！", featureOfInterest.getName());
				}
			}
			// 转换成Datastream，将会同时创建Thing（包含Location信息）、Sensor和ObservedProperty并设定好关联
			List<Datastream> datastreamList = DatastreamFactory.createDatastreamList(object, swmm.getSystem().getFlowUnits());
			// 发送请求
			for (Datastream datastream : datastreamList) {
				if (!client.add(datastream)) {
					log.error("添加数据流{}时出错！", datastream.getName());
				}
			}
		}
		// 2. 创建Reactor订阅者，订阅全部水文计算数据，并记录到SensorThings API服务器
		this.swmm.subscribe(SUB_CATCHMENT, beanFactory.getBean(SensorThingsSubscriber.class));
		this.swmm.subscribe(LINK, beanFactory.getBean(SensorThingsSubscriber.class));
		this.swmm.subscribe(NODE, beanFactory.getBean(SensorThingsSubscriber.class));
		// 3. 创建订阅者订阅接收雨量计数据，订阅者会调用数据缓存对象，完成数据驱动、时间步长对齐以及水文模拟操作
		// 订阅对应其中降水数据
		List<VisualObject> gages = swmm.getObjectList(GAGE);
		// 查询对应传感器观测属性的数据流
		List<Datastream> datastreams = new ArrayList<>();
		for (VisualObject gage : gages) {
			Set<String> properties = PropertyReadUtils.getComputedPropertyNames(gage);
			// 查找每个属性对应的数据流
			for (String property : properties) {
				Datastream datastream = client.getByName(NameUtils.generateObservedPropertyName(gage, property), Datastream.class, SensorThingsExpandProperty.getExpandProperty(Datastream.class));
				if (datastream != null) {
					datastreams.add(datastream);
				}
			}
		}
		// 订阅全部雨量计相关的数据流
		for (Datastream datastream : datastreams) {
			String topic = String.format("v1.1/Datastreams(%d)/Observations?$expand=%s", (int) datastream.getId(), String.join(",", SensorThingsExpandProperty.getExpandProperty(Observation.class)));
			mqttClient.subscribe(topic, beanFactory.getBean(RainGageSubscriber.class, swmm));
			subscribedTopics.add(topic);
			log.info("已订阅观测数据主题：{}", topic);
		}
	}

	/**
	 * 模拟完成后的资源回收操作
	 */
	private void dispose() throws Exception {
		// 1. 停止模型
		this.swmm.close();
		// 2. 取消订阅全部相关传感器
		for (String topic : subscribedTopics) {
			mqttClient.unsubscribe(topic);
		}
	}

	/**
	 * 水文模型完整模拟操作，包括：
	 * <ol>
	 *     <li>首先初始化，包括水文模型文件读取、转换为SensorThings API对象，并订阅其中对应名称的雨量计MQTT数据</li>
	 *     <li>然后基于订阅数据驱动水文模型运行，运行过程中会将模拟产生数据转换成SensorThings API并发布</li>
	 *     <li>模拟完成，停止模型，取消订阅等资源回收</li>
	 * </ol>
	 */
	@Override
	public void run() {
		try {
			// 初始化模型
			initialize();
			this.job.setStatus(JobStatus.RUNNING);
			while (true) {
				if (swmm.isComplete()) {
					// 释放资源
					log.warn("水文模拟结束！");
					dispose();
					// 更新状态
					this.job.setFinished(LocalDateTime.now());
					this.job.setProgress(100);
					this.job.setStatus(JobStatus.SUCCESSFUL);
					break;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}