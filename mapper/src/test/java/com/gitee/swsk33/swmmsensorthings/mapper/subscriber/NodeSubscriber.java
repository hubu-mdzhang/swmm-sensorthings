package com.gitee.swsk33.swmmsensorthings.mapper.subscriber;

import com.alibaba.fastjson2.JSON;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.context.SensorDataStrategyContext;
import com.gitee.swsk33.swmmsensorthings.model.Datastream;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import com.gitee.swsk33.swmmsensorthings.model.ObservedProperty;
import io.github.swsk33.swmmjava.model.event.VisualObjectEvent;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

import java.util.List;

/**
 * 雨量计数据订阅者
 */
@Slf4j
public class NodeSubscriber extends BaseSubscriber<VisualObjectEvent> {

	public void hookOnSubscribe(Subscription subscription) {
		log.info("开始订阅节点类型数据！");
		request(1);
	}

	public void hookOnNext(VisualObjectEvent event) {
		// 转换为SensorThings对象
		List<Observation> nodeObservations = SensorDataStrategyContext.doMapObservation(event.getData(), event.getComputedTime());
		List<ObservedProperty> nodeProperties = SensorDataStrategyContext.doMapObservedProperty(event.getData());
		// 构建为数据流并输出
		if (nodeProperties != null && nodeObservations != null) {
			for (int i = 0; i < nodeProperties.size(); i++) {
				Observation observation = nodeObservations.get(i);
				Datastream datastream = Datastream.create(nodeObservations.get(i), nodeProperties.get(i));
				System.out.println(JSON.toJSONString(observation));
				System.out.println(JSON.toJSONString(datastream));
			}
		}
		// 继续订阅
		request(1);
	}

}