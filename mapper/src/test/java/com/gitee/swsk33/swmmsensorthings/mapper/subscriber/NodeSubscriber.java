package com.gitee.swsk33.swmmsensorthings.mapper.subscriber;

import io.github.swsk33.swmmjava.model.event.VisualObjectEvent;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

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
		// 继续订阅
		request(1);
	}

}