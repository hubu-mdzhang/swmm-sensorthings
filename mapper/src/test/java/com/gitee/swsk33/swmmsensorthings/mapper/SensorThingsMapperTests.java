package com.gitee.swsk33.swmmsensorthings.mapper;

import com.alibaba.fastjson2.JSON;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.ObservationFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.impl.FeatureOfInterestFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.impl.SensorFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.impl.ThingFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.util.PropertyReadUtils;
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
import java.util.Set;

import static io.github.swsk33.swmmjava.param.ObjectTypeCode.*;

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

	/**
	 * 订阅对应类型数据，并转换为观测值
	 *
	 * @param type 类型常量
	 */
	private void subscribeAndPrint(int type) {
		swmm.subscribe(type, new BaseSubscriber<>() {
			@Override
			protected void hookOnSubscribe(Subscription subscription) {
				request(1);
			}

			@Override
			protected void hookOnNext(VisualObjectEvent event) {
				// 转换成观测值
				try {
					Set<String> propertyNames = PropertyReadUtils.getComputedPropertyNames(event.getData());
					for (String propertyName : propertyNames) {
						System.out.println(propertyName + ": " + JSON.toJSONString(ObservationFactory.createObservation(event.getData(), propertyName, event.getComputedTime())));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 继续订阅
				request(1);
			}
		});
		while (swmm.step() != 0) {
			// ...
		}
	}

	@Test
	@DisplayName("测试对象基本映射")
	void testObjectMapping() {
		// 雨量计
		List<VisualObject> gages = swmm.getObjectList(GAGE);
		// 转换为Sensor
		log.info("具体传感器列表：");
		gages.stream().map(SensorFactory.getInstance()::createObject).forEach(item -> {
			System.out.println(JSON.toJSONString(item));
		});
		// 子汇水区域
		List<VisualObject> catchments = swmm.getObjectList(ObjectTypeCode.SUB_CATCHMENT);
		// 转换为FeatureOfInterest
		log.info("兴趣要素列表：");
		catchments.stream().map(FeatureOfInterestFactory.getInstance()::createObject).forEach(item -> {
			System.out.println(JSON.toJSONString(item));
		});
		// 链接
		List<VisualObject> links = swmm.getObjectList(ObjectTypeCode.LINK);
		log.info("物品列表：");
		links.stream().map(ThingFactory.getInstance()::createObject).forEach(item -> {
			System.out.println(JSON.toJSONString(item));
		});
		// 节点
		List<VisualObject> nodes = swmm.getObjectList(ObjectTypeCode.NODE);
		nodes.stream().map(ThingFactory.getInstance()::createObject).forEach(item -> {
			System.out.println(JSON.toJSONString(item));
		});
		// 虚拟传感器
		log.info("虚拟传感器列表：");
		catchments.stream().map(SensorFactory.getInstance()::createObject).forEach(item -> {
			System.out.println(JSON.toJSONString(item));
		});
		links.stream().map(SensorFactory.getInstance()::createObject).forEach(item -> {
			System.out.println(JSON.toJSONString(item));
		});
		nodes.stream().map(SensorFactory.getInstance()::createObject).forEach(item -> {
			System.out.println(JSON.toJSONString(item));
		});
	}

	@Test
	@DisplayName("测试雨量计数据流订阅映射")
	void testRainGageMapping() {
		log.info("开始订阅雨量计类型数据！");
		subscribeAndPrint(GAGE);
	}

	@Test
	@DisplayName("测试子汇水区域数据流订阅映射")
	void testSubcatchmentDataMapping() {
		log.info("开始订阅子汇水区域类型数据！");
		subscribeAndPrint(SUB_CATCHMENT);
	}

	@Test
	@DisplayName("测试节点类型数据流订阅映射")
	void testNodeDataMapping() {
		log.info("开始订阅节点类型数据！");
		subscribeAndPrint(NODE);
	}

	@Test
	@DisplayName("测试链接类型数据流订阅映射")
	void testLinkDataMapping() {
		log.info("开始链接类型数据！");
		subscribeAndPrint(LINK);
	}

}