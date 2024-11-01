package com.gitee.swsk33.swmmsensorthings.mapper.factory;

import com.gitee.swsk33.swmmsensorthings.model.SensorThingsObject;
import io.github.swsk33.swmmjava.model.VisualObject;

/**
 * SensorThings对象构造工厂
 */
public interface SensorThingsObjectFactory {

	/**
	 * 将SWMM可视对象构建成对应的SensorThings对象
	 *
	 * @param object SWMM的可视对象
	 * @return 转换后的SensorThings对象
	 */
	SensorThingsObject createObject(VisualObject object);

}