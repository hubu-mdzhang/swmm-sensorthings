package com.gitee.swsk33.swmmsensorthings.server.cache;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import com.gitee.swsk33.swmmsensorthings.model.*;
import com.gitee.swsk33.swmmsensorthings.server.client.SensorThingsObjectClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.gitee.swsk33.swmmsensorthings.server.param.SensorThingsExpandProperty.getExpandProperty;

/**
 * 通用操作SensorThings对象缓存的类
 */
@Component
public class SensorThingsObjectCache {

	/**
	 * 存放不同的SensorThings类型对应的缓存池的哈希表
	 */
	private final Map<Class<? extends SensorThingsObject>, Cache<String, SensorThingsObject>> CACHE_POOL_MAP = new HashMap<>();

	@Autowired
	private SensorThingsObjectClient client;

	@PostConstruct
	private void initialize() {
		// 初始化缓存池
		CACHE_POOL_MAP.put(Datastream.class, CacheUtil.newLRUCache(300));
		CACHE_POOL_MAP.put(FeatureOfInterest.class, CacheUtil.newLRUCache(300));
		CACHE_POOL_MAP.put(Observation.class, CacheUtil.newLRUCache(300));
		CACHE_POOL_MAP.put(ObservedProperty.class, CacheUtil.newLRUCache(300));
		CACHE_POOL_MAP.put(Sensor.class, CacheUtil.newLRUCache(300));
		CACHE_POOL_MAP.put(Thing.class, CacheUtil.newLRUCache(300));
	}

	/**
	 * 根据名称获取对应类型的SensorThings对象
	 *
	 * @param name 对象名称
	 * @param type 对象具体类型
	 * @param <T>  泛型类型
	 * @return 获取的对应SensorThings对象
	 */
	public <T extends SensorThingsObject> T getByName(String name, Class<T> type) {
		// 先从缓存获取
		Cache<String, SensorThingsObject> cache = CACHE_POOL_MAP.get(type);
		SensorThingsObject object = cache.get(name);
		// 缓存不存在则请求
		if (object == null) {
			object = client.getByName(name, type, getExpandProperty(type));
			// 如果还是不存在则失败
			if (object == null) {
				return null;
			}
			// 存入缓存
			cache.put(name, object);
		}
		return type.cast(object);
	}

}