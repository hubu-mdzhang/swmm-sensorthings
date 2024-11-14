package com.gitee.swsk33.swmmsensorthings.server.client.impl;

import com.gitee.swsk33.swmmsensorthings.model.Observation;
import com.gitee.swsk33.swmmsensorthings.server.client.BaseSensorThingsClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用于请求Observation对象的具体客户端
 */
@Component
public class ObservationClient extends BaseSensorThingsClient<Observation> {

	/**
	 * 查询时展开的属性
	 */
	private static final String[] EXPAND_PROPERTIES = new String[]{"Datastream", "FeatureOfInterest"};

	public boolean add(Observation object) {
		return super.add(object);
	}

	public boolean remove(Object id) {
		return super.remove(id, Observation.class);
	}

	public Observation getById(Object id) {
		return super.getById(id, Observation.class, EXPAND_PROPERTIES);
	}

	public Observation getByName(String name) {
		return super.getByName(name, Observation.class, EXPAND_PROPERTIES);
	}

	public List<Observation> getAll() {
		return super.getAll(Observation.class);
	}

	public boolean existByName(String name) {
		return super.existByName(name, Observation.class);
	}

}