package com.gitee.swsk33.swmmsensorthings.server.template;

import com.gitee.swsk33.swmmsensorthings.mapper.factory.DatastreamFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.impl.FeatureOfInterestFactory;
import com.gitee.swsk33.swmmsensorthings.model.Datastream;
import com.gitee.swsk33.swmmsensorthings.model.FeatureOfInterest;
import com.gitee.swsk33.swmmsensorthings.model.Sensor;
import com.gitee.swsk33.swmmsensorthings.server.client.SensorThingsObjectClient;
import io.github.swsk33.swmmjava.model.Subcatchment;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用于初始化SensorThings对象的具体模板方法
 */
@Slf4j
@Component
public class SensorThingsInitializeTemplate {

	@Autowired
	private SensorThingsObjectClient client;

	/**
	 * 执行全部步骤
	 *
	 * @param object 输入SWMM可视对象
	 */
	public void execute(VisualObject object) {
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
		List<Datastream> datastreamList = DatastreamFactory.createDatastreamList(object);
		// 发送请求
		for (Datastream datastream : datastreamList) {
			if (!client.add(datastream)) {
				log.error("添加数据流{}时出错！", datastream.getName());
			}
		}
	}

}