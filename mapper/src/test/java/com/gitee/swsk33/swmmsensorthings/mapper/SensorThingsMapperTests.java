package com.gitee.swsk33.swmmsensorthings.mapper;

import com.alibaba.fastjson2.JSON;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.SensorThingsObjectFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.builder.ObjectFactoryBuilder;
import com.gitee.swsk33.swmmsensorthings.mapper.subscriber.NodeSubscriber;
import com.gitee.swsk33.swmmsensorthings.mapper.subscriber.RainGageSubscriber;
import io.github.swsk33.swmmjava.SWMM;
import io.github.swsk33.swmmjava.model.VisualObject;
import io.github.swsk33.swmmjava.param.ObjectTypeCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 测试SensorThings对象映射
 */
@Slf4j
public class SensorThingsMapperTests {

	private final SWMM swmm;

	public SensorThingsMapperTests() {
		swmm = new SWMM("input-data/demo.inp");
		swmm.start();
	}

	@Test
	@DisplayName("测试对象基本映射")
	void testObjectMapping() {
		// 雨量计
		List<VisualObject> gages = swmm.getObjectList(ObjectTypeCode.GAGE);
		// 转换为Sensor
		SensorThingsObjectFactory sensorFactory = ObjectFactoryBuilder.getObjectFactory(gages.getFirst());
		assert sensorFactory != null;
		log.info("传感器列表：");
		gages.stream().map(sensorFactory::createObject).forEach(item -> {
			System.out.println(JSON.toJSONString(item));
		});
		// 子汇水区域
		List<VisualObject> catchments = swmm.getObjectList(ObjectTypeCode.SUB_CATCHMENT);
		// 转换为FeatureOfInterest
		SensorThingsObjectFactory catchmentFactory = ObjectFactoryBuilder.getObjectFactory(catchments.getFirst());
		assert catchmentFactory != null;
		log.info("兴趣要素列表：");
		catchments.stream().map(catchmentFactory::createObject).forEach(item -> {
			System.out.println(JSON.toJSONString(item));
		});
		// 链接
		List<VisualObject> links = swmm.getObjectList(ObjectTypeCode.LINK);
		SensorThingsObjectFactory linkFactory = ObjectFactoryBuilder.getObjectFactory(links.getFirst());
		assert linkFactory != null;
		log.info("物品列表：");
		links.stream().map(linkFactory::createObject).forEach(item -> {
			System.out.println(JSON.toJSONString(item));
		});
		// 节点
		List<VisualObject> nodes = swmm.getObjectList(ObjectTypeCode.NODE);
		SensorThingsObjectFactory nodeFactory = ObjectFactoryBuilder.getObjectFactory(nodes.getFirst());
		assert nodeFactory != null;
		nodes.stream().map(nodeFactory::createObject).forEach(item -> {
			System.out.println(JSON.toJSONString(item));
		});
	}

	@Test
	@DisplayName("测试数据流订阅映射")
	void testDataMapping() {
		swmm.subscribe(ObjectTypeCode.GAGE, new RainGageSubscriber());
		swmm.subscribe(ObjectTypeCode.NODE, new NodeSubscriber());
		while (swmm.step() != 0) {
			// ...
		}
	}

}