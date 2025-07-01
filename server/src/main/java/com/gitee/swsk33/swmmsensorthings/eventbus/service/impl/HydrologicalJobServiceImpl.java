package com.gitee.swsk33.swmmsensorthings.eventbus.service.impl;

import cn.hutool.core.io.file.FileNameUtil;
import com.gitee.swsk33.swmmsensorthings.eventbus.client.SensorThingsObjectClient;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.Result;
import com.gitee.swsk33.swmmsensorthings.eventbus.param.SensorThingsExpandProperty;
import com.gitee.swsk33.swmmsensorthings.eventbus.property.CoreProperties;
import com.gitee.swsk33.swmmsensorthings.eventbus.service.HydrologicalJobService;
import com.gitee.swsk33.swmmsensorthings.eventbus.subscriber.mqtt.RainGageSubscriber;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.DatastreamFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.impl.FeatureOfInterestFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.util.NameUtils;
import com.gitee.swsk33.swmmsensorthings.mapper.util.PropertyReadUtils;
import com.gitee.swsk33.swmmsensorthings.model.Datastream;
import com.gitee.swsk33.swmmsensorthings.model.FeatureOfInterest;
import com.gitee.swsk33.swmmsensorthings.model.Sensor;
import io.github.swsk33.fileliftspringbootstarter.property.FileSystemProperties;
import io.github.swsk33.swmmjava.SWMM;
import io.github.swsk33.swmmjava.model.Subcatchment;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.github.swsk33.swmmjava.param.ObjectTypeCode.GAGE;

@Slf4j
@Service
public class HydrologicalJobServiceImpl implements HydrologicalJobService {

	@Autowired
	private BeanFactory beanFactory;

	@Autowired
	private SensorThingsObjectClient client;

	@Autowired
	private MqttClient mqttClient;

	@Autowired
	private FileSystemProperties fileSystemProperties;

	@Autowired
	private CoreProperties coreProperties;

	/**
	 * 将一个SWMM模型包含的全部对象转换为SensorThings API表达并注册到服务器
	 *
	 * @param swmm SWMM模型实例
	 */
	private void createSWMMSensorThingsObject(SWMM swmm) {
		// 获取全部SWMM对象
		List<VisualObject> objects = swmm.getAllObjects();
		// 遍历初始化
		for (VisualObject object : objects) {
			// 若对象对应的传感器（实体或者虚拟）存在，说明该对象对应的全部SensorThings对象存在，不进行该对象初始化
			if (client.existByName(object.getId(), Sensor.class)) {
				log.warn("对象{}所对应的SensorThings对象已存在，跳过其转换映射与初始化步骤！", object.getId());
				return;
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
	}

	@Override
	public Result<String> createJob(String inputFile) throws Exception {
		// 创建水文模型
		SWMM swmm = new SWMM(fileSystemProperties.getSaveFolder() + File.separator + inputFile);
		// 转换并注册SensorThings API对象
		createSWMMSensorThingsObject(swmm);
		// 创建对应的雨量计订阅者，接收降水数据并输入模型
		RainGageSubscriber subscriber = beanFactory.getBean(RainGageSubscriber.class);
		subscriber.setSwmm(swmm);
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
		swmm.start();

		return Result.resultSuccess("水文模型创建成功！", String.format("http://%s:%d/api/file/get/%s", coreProperties.getAdvertiseHost(), coreProperties.getAdvertisePort(), FileNameUtil.mainName(inputFile) + ".out"));
	}

}