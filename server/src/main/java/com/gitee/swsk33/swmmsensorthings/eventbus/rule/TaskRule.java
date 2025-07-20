package com.gitee.swsk33.swmmsensorthings.eventbus.rule;

import com.gitee.swsk33.swmmsensorthings.model.Actuator;
import io.github.swsk33.swmmjava.model.VisualObject;

/**
 * 基于SensorThings API模型的规则接口<br>
 * 即：满足条件 -> 调用Actuator执行Task
 */
public interface TaskRule {

	/**
	 * 根据传入数据，判断是否满足条件
	 *
	 * @param data 传入数据
	 * @return 数据满足条件时返回true
	 */
	boolean satisfied(VisualObject data);

	/**
	 * 执行规则，该方法仅执行规则触发任务，不进行条件判断
	 *
	 * @param data     触发规则的数据
	 * @param actuator 执行任务的SensorThings API执行器
	 */
	void execute(VisualObject data, Actuator actuator);

}