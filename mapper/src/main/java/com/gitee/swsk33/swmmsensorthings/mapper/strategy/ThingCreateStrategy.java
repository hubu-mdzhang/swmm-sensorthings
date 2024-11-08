package com.gitee.swsk33.swmmsensorthings.mapper.strategy;

import com.gitee.swsk33.swmmsensorthings.model.Thing;
import io.github.swsk33.swmmjava.model.VisualObject;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * 从SWMM对象创建实体的抽象策略
 */
public interface ThingCreateStrategy {

	/**
	 * 创建几何图形的工厂
	 */
	GeometryFactory geometryFactory = new GeometryFactory();

	/**
	 * 从SWMM对象创建一个实体
	 *
	 * @param object SWMM可视对象
	 * @return 返回创建的实体
	 */
	Thing createThing(VisualObject object);

}