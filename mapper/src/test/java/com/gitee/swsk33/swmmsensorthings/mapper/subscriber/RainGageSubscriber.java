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
public class RainGageSubscriber extends BaseSubscriber<VisualObjectEvent> {

	public void hookOnSubscribe(Subscription subscription) {
		log.info("开始订阅雨量计数据！");
		request(1);
	}

	public void hookOnNext(VisualObjectEvent event) {
		// 转换为SensorThings对象
		List<Observation> gageObservations = SensorDataStrategyContext.doMapObservation(event.getData(), event.getComputedTime());
		List<ObservedProperty> gageProperties = SensorDataStrategyContext.doMapObservedProperty(event.getData());
		// 构建为数据流并输出
		if (gageProperties != null && gageObservations != null) {
			for (int i = 0; i < gageProperties.size(); i++) {
				Observation observation = gageObservations.get(i);
				Datastream datastream = Datastream.create(gageObservations.get(i), gageProperties.get(i));
				System.out.println(JSON.toJSONString(observation));
				System.out.println(JSON.toJSONString(datastream));
			}
		}
		// 继续订阅
		request(1);
	}

}