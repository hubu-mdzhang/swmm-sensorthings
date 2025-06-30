package com.gitee.swsk33.swmmsensorthings.eventbus.subscriber.reactor;

import com.gitee.swsk33.swmmsensorthings.eventbus.cache.SensorThingsObjectCache;
import com.gitee.swsk33.swmmsensorthings.eventbus.client.SensorThingsObjectClient;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.ObservationFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.util.NameUtils;
import com.gitee.swsk33.swmmsensorthings.mapper.util.PropertyReadUtils;
import com.gitee.swsk33.swmmsensorthings.model.Datastream;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import io.github.swsk33.swmmjava.model.event.VisualObjectEvent;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.BaseSubscriber;

import java.util.Set;

/**
 * 订阅SWMM计算对象，并发布到SensorThings服务器的通用订阅者
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SensorThingsSubscriber extends BaseSubscriber<VisualObjectEvent> {

	@Autowired
	private SensorThingsObjectClient client;

	@Autowired
	private SensorThingsObjectCache cache;

	@Override
	public void hookOnSubscribe(Subscription subscription) {
		request(1);
	}

	@Override
	public void hookOnNext(VisualObjectEvent event) {
		try {
			// 获取全部观测属性名称
			Set<String> propertyName = PropertyReadUtils.getComputedPropertyNames(event.getData());
			// 遍历观测属性
			for (String property : propertyName) {
				// 获取该属性数据流对象
				Datastream datastream = cache.getByName(NameUtils.generateObservedPropertyName(event.getData(), property), Datastream.class);
				if (datastream == null) {
					log.error("名为{}的数据流不存在！", property);
					continue;
				}
				// 创建观测对象
				Observation observation = ObservationFactory.createObservation(event.getData(), property, datastream, event.getComputedTime());
				// 发布观测值
				client.add(observation);
			}
		} catch (Exception e) {
			log.error("解析订阅的{}对象计算属性并发布观测值时出错！", event.getData().getClass().getSimpleName());
			e.printStackTrace();
		} finally {
			request(1);
		}
	}

}