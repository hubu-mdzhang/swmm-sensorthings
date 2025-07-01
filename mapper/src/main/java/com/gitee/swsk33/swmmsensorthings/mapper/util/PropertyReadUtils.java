package com.gitee.swsk33.swmmsensorthings.mapper.util;

import com.alibaba.fastjson2.JSONObject;
import io.github.swsk33.swmmjava.annotation.ComputedProperty;
import io.github.swsk33.swmmjava.annotation.IntrinsicProperty;
import io.github.swsk33.swmmjava.model.VisualObject;
import io.github.swsk33.swmmjava.util.ReflectUtils;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * 用于读取SWMM可视对象的属性信息以及值的实用类
 */
public class PropertyReadUtils {

	/**
	 * 根据注解，读取SWMM可视对象的固有属性
	 *
	 * @param object 要读取的可视对象
	 * @return 包含固有属性的键值对的JSON对象
	 */
	public static JSONObject readIntrinsicProperties(VisualObject object) throws Exception {
		Field[] fields = ReflectUtils.getAllFields(object.getClass());
		JSONObject result = new JSONObject();
		for (Field field : fields) {
			field.setAccessible(true);
			IntrinsicProperty annotation = field.getAnnotation(IntrinsicProperty.class);
			if (annotation != null) {
				result.put(field.getName(), field.get(object));
			}
		}
		return result;
	}

	/**
	 * 根据注解，读取SWMM可视对象的计算属性
	 *
	 * @param object 要读取的可视对象
	 * @return 包含计算属性的键值对的JSON对象
	 */
	public static JSONObject readComputedProperties(VisualObject object) throws Exception {
		Field[] fields = ReflectUtils.getAllFields(object.getClass());
		JSONObject result = new JSONObject();
		for (Field field : fields) {
			field.setAccessible(true);
			ComputedProperty annotation = field.getAnnotation(ComputedProperty.class);
			if (annotation != null) {
				result.put(field.getName(), field.get(object));
			}
		}
		return result;
	}

	/**
	 * 根据注解，获取SWMM可视对象的计算属性的名称列表
	 *
	 * @param object 要读取的可视对象
	 * @return 该可视对象全部的计算属性名称列表
	 */
	public static Set<String> getComputedPropertyNames(VisualObject object) throws Exception {
		return readComputedProperties(object).keySet();
	}

	/**
	 * 根据注解，获取SWMM可视对象的指定名称的计算属性的值
	 *
	 * @param object 要读取的可视对象
	 * @param name   计算属性名称
	 * @return 该可视对象对应名称的计算属性的值
	 */
	public static Object getComputedPropertyValue(VisualObject object, String name) throws Exception {
		return readComputedProperties(object).get(name);
	}

}