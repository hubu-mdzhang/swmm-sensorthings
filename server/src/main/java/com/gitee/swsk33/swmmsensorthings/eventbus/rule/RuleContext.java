package com.gitee.swsk33.swmmsensorthings.eventbus.rule;

import com.gitee.swsk33.swmmsensorthings.model.Actuator;
import io.github.swsk33.swmmjava.SWMM;
import io.github.swsk33.swmmjava.model.VisualObjectIdentifier;
import io.github.swsk33.swmmjava.param.ObjectTypeCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 规则中心，一个SWMM模型对应一个规则中心实例
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RuleContext {

	/**
	 * 存放监听的水文设施对象对应的规则触发器的列表
	 */
	private static final Map<VisualObjectIdentifier, RuleSubscriber> RULE_LIST = new HashMap<>();

	/**
	 * 对应的水文模型实例
	 */
	private final SWMM swmm;

	/**
	 * 构造函数
	 *
	 * @param swmm 设定水文模型实例
	 */
	public RuleContext(SWMM swmm) {
		this.swmm = swmm;
	}

	/**
	 * 添加一个水文设施监听并实现特定条件反馈控制
	 *
	 * @param id       监听设施id
	 * @param type     监听设施类型，参考{@link ObjectTypeCode}常量
	 * @param rule     自定义规则
	 * @param actuator 控制的执行器设施
	 */
	public void addRule(String id, int type, TaskRule rule, Actuator actuator) {
		// 订阅水文对象
		RuleSubscriber subscriber = new RuleSubscriber(rule, actuator);
		this.swmm.subscribe(id, type, subscriber);
		// 加入列表
		RULE_LIST.put(new VisualObjectIdentifier(id, type), new RuleSubscriber(rule, actuator));
		log.info("已为水文对象：id={} type={}创建自定义反馈控制规则！", id, type);
	}

}