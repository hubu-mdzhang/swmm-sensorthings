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

	public boolean add(ObservedProperty object) {
		return super.add(object);
	}

	public boolean remove(Object id) {
		return super.remove(id, ObservedProperty.class);
	}

	public ObservedProperty get(Object id) {
		return super.get(id, ObservedProperty.class);
	}

	public List<ObservedProperty> getAll() {
		return super.getAll(ObservedProperty.class);
	}

	public boolean existByName(String name) {
		return super.existByName(name, ObservedProperty.class);
	}

}