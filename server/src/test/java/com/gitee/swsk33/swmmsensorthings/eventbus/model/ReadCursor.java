package com.gitee.swsk33.swmmsensorthings.eventbus.model;

import io.github.swsk33.swmmjava.param.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.swsk33.swmmjava.param.LinkPropertyCode.*;
import static io.github.swsk33.swmmjava.param.NodePropertyCode.*;
import static io.github.swsk33.swmmjava.param.ObjectTypeCode.LINK;
import static io.github.swsk33.swmmjava.param.ObjectTypeCode.NODE;

/**
 * 表示读取水文模型的某类对象的某个属性的参数类型
 */
@Data
@AllArgsConstructor
public class ReadCursor {

	/**
	 * 读取的类型列表
	 */
	public static final List<Integer> READ_TYPES = new ArrayList<>();

	/**
	 * 读取的属性列表
	 * - 键：类型
	 * - 值：对应的类型属性列表
	 */
	public static final Map<Integer, List<Integer>> READ_PROPERTIES = new HashMap<>();

	/**
	 * 不同类型对应的属性个数
	 */
	public static final Map<Integer, Integer> TYPE_PROPERTY_COUNT = new HashMap<>();

	/**
	 * 当前正在读取的水文对象下标
	 */
	private int index;

	/**
	 * 当前正在读取的水文对象属性代码参考：
	 * <ul>
	 *   <li>雨量计属性：{@link GagePropertyCode}</li>
	 *   <li>子汇水区域属性：{@link SubcatchmentPropertyCode}</li>
	 *   <li>节点属性：{@link NodePropertyCode}</li>
	 *   <li>连接对象属性：{@link LinkPropertyCode}</li>
	 *   <li>系统属性：{@link SystemPropertyCode}</li>
	 *   </ul>
	 */
	private int property;

	/**
	 * 初始化常量列表
	 */
	public static void init() {
		// 初始化读取类型列表
		READ_TYPES.addAll(List.of(NODE, LINK));
		// 初始化读取属性列表
		READ_PROPERTIES.put(NODE, List.of(WATER_DEPTH, HEAD, VOLUME, LAT_FLOW, IN_FLOW, OVERFLOW));
		READ_PROPERTIES.put(LINK, List.of(FULL_FLOW, FULL_DEPTH, FULL_FLOW, VELOCITY, DEPTH, TOP_WIDTH));
		// 初始化数量列表
		TYPE_PROPERTY_COUNT.put(NODE, ReadCursor.READ_PROPERTIES.get(NODE).size());
		TYPE_PROPERTY_COUNT.put(LINK, ReadCursor.READ_PROPERTIES.get(LINK).size());
	}

}