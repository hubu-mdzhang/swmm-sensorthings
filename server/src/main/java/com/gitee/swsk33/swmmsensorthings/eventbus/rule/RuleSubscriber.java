package com.gitee.swsk33.swmmsensorthings.eventbus.rule;

import com.gitee.swsk33.swmmsensorthings.model.Actuator;
import io.github.swsk33.swmmjava.model.event.VisualObjectEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.BaseSubscriber;

/**
 * 基于Reactor订阅者的规则订阅触发器，用于监听模型指定的水文设施并实现反馈控制
 */
@Data
@AllArgsConstructor
public class RuleSubscriber extends BaseSubscriber<VisualObjectEvent> {

	/**
	 * 任务规则
	 */
	private TaskRule rule;

	/**
	 * 执行任务的设备
	 */
	private Actuator actuator;

	/**
	 * 订阅到数据触发事件
	 *
	 * @param data 订阅到的数据
	 */
	public void hookOnNext(VisualObjectEvent data) {
		if (rule.satisfied(data.getData())) {
			rule.execute(data.getData(), actuator);
		}
	}

}