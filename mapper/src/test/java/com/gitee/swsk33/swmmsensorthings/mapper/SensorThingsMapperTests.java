package com.gitee.swsk33.swmmsensorthings.mapper;

import com.alibaba.fastjson2.JSON;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.SensorDataFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.SensorThingsObjectFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.builder.ObjectFactoryBuilder;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import io.github.swsk33.swmmjava.SWMM;
import io.github.swsk33.swmmjava.model.VisualObject;
import io.github.swsk33.swmmjava.model.event.VisualObjectEvent;
import io.github.swsk33.swmmjava.param.ObjectTypeCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

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
		log.info("具体传感器列表：");
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
		// 虚拟传感器
		log.info("虚拟传感器列表：");
		catchments.stream().map(sensorFactory::createObject).forEach(item -> {
			System.out.println(JSON.toJSONString(item));
		});
		links.stream().map(sensorFactory::createObject).forEach(item -> {
			System.out.println(JSON.toJSONString(item));
		});
		nodes.stream().map(sensorFactory::createObject).forEach(item -> {
			System.out.println(JSON.toJSONString(item));
		});
	}

	@Test
	@DisplayName("测试雨量计数据流订阅映射")
	void testRainGageMapping() {
		swmm.subscribe(ObjectTypeCode.GAGE, new BaseSubscriber<VisualObjectEvent>() {
			@Override
			protected void hookOnSubscribe(Subscription subscription) {
				log.info("开始订阅雨量计类型数据！");
				request(1);
			}

			@Override
			protected void hookOnNext(VisualObjectEvent event) {
				List<Observation> observations = SensorDataFactory.createObservations(event.getData(), event.getComputedTime());
				observations.forEach(observation -> {
					System.out.println(JSON.toJSONString(observation));
				});
				// 继续订阅
				request(1);
			}
		});
		while (swmm.step() != 0) {
			// ...
		}
	}

	@Test
	@DisplayName("测试子汇水区域数据流订阅映射")
	void testSubcatchmentDataMapping() {
		swmm.subscribe(ObjectTypeCode.SUB_CATCHMENT, new BaseSubscriber<>() {
			@Override
			protected void hookOnSubscribe(Subscription subscription) {
				log.info("开始订阅子汇水区域类型数据！");
				request(1);
			}

			@Override
			protected void hookOnNext(VisualObjectEvent event) {
				List<Observation> observations = SensorDataFactory.createObservations(event.getData(), event.getComputedTime());
				observations.forEach(observation -> {
					System.out.println(JSON.toJSONString(observation));
				});
				// 继续订阅
				request(1);
			}
		});
		while (swmm.step() != 0) {
			// ...
		}
	}

	@Test
	@DisplayName("测试节点类型数据流订阅映射")
	void testNodeDataMapping() {
		swmm.subscribe(ObjectTypeCode.NODE, new BaseSubscriber<>() {
			@Override
			protected void hookOnSubscribe(Subscription subscription) {
				log.info("开始订阅节点类型数据！");
				request(1);
			}

			@Override
			protected void hookOnNext(VisualObjectEvent event) {
				List<Observation> observations = SensorDataFactory.createObservations(event.getData(), event.getComputedTime());
				observations.forEach(observation -> {
					System.out.println(JSON.toJSONString(observation));
				});
				// 继续订阅
				request(1);
			}
		});
		while (swmm.step() != 0) {
			// ...
		}
	}

	@Test
	@DisplayName("测试链接类型数据流订阅映射")
	void testLinkDataMapping() {
		swmm.subscribe(ObjectTypeCode.LINK, new BaseSubscriber<>() {
			@Override
			protected void hookOnSubscribe(Subscription subscription) {
				log.info("开始链接类型数据！");
				request(1);
			}

			@Override
			protected void hookOnNext(VisualObjectEvent event) {
				List<Observation> observations = SensorDataFactory.createObservations(event.getData(), event.getComputedTime());
				observations.forEach(observation -> {
					System.out.println(JSON.toJSONString(observation));
				});
				// 继续订阅
				request(1);
			}
		});
		while (swmm.step() != 0) {
			// ...
		}
	}

}