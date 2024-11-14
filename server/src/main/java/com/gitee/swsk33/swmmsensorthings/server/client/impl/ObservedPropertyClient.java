package com.gitee.swsk33.swmmsensorthings.server.client.impl;

import com.gitee.swsk33.swmmsensorthings.model.ObservedProperty;
import com.gitee.swsk33.swmmsensorthings.server.client.BaseSensorThingsClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用于请求ObservedProperty对象的具体客户端
 */
@Component
public class ObservedPropertyClient extends BaseSensorThingsClient<ObservedProperty> {

	/**
	 * 查询时展开的属性
	 */
	private static final String[] EXPAND_PROPERTIES = new String[]{"Datastreams"};

	public boolean add(ObservedProperty object) {
		return super.add(object);
	}

	public boolean remove(Object id) {
		return super.remove(id, ObservedProperty.class);
	}

	public ObservedProperty getById(Object id) {
		return super.getById(id, ObservedProperty.class, EXPAND_PROPERTIES);
	}

	public ObservedProperty getByName(String name) {
		return super.getByName(name, ObservedProperty.class, EXPAND_PROPERTIES);
	}

	public List<ObservedProperty> getAll() {
		return super.getAll(ObservedProperty.class);
	}

	public boolean existByName(String name) {
		return super.existByName(name, ObservedProperty.class);
	}

}