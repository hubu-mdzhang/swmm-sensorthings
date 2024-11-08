package com.gitee.swsk33.swmmsensorthings.mapper.strategy;

import com.gitee.swsk33.swmmsensorthings.model.Sensor;
import io.github.swsk33.swmmjava.model.VisualObject;

import java.util.List;

/**
 * 从SWMM对象创建传感器或者虚拟传感器的抽象策略
 */
public interface SensorCreateStrategy {

	/**
	 * 从SWMM对象创建Sensor对象，其中：
	 * <ul>
	 *     <li>RainGage将创建为一个单独的具体传感器</li>
	 *     <li>其余对象的计算属性将创建为一个虚拟传感器</li>
	 * </ul>
	 *
	 * @param object 原始SWMM对象
	 * @return 构造后的Sensor传感器列表
	 */
	List<Sensor> createSensors(VisualObject object);

}