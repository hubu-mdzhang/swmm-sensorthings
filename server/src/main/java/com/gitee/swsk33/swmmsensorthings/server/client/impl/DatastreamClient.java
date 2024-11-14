package com.gitee.swsk33.swmmsensorthings.server.client.impl;

import com.gitee.swsk33.swmmsensorthings.model.Datastream;
import com.gitee.swsk33.swmmsensorthings.server.client.BaseSensorThingsClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用于请求Datastream对象的具体客户端
 */
@Component
public class DatastreamClient extends BaseSensorThingsClient<Datastream> {

	/**
	 * 查询时展开的属性
	 */
	private static final String[] EXPAND_PROPERTIES = new String[]{"ObservedProperty", "Sensor", "Thing", "Observations"};

	public boolean add(Datastream object) {
		return super.add(object);
	}

	public boolean remove(Object id) {
		return super.remove(id, Datastream.class);
	}

	public Datastream getById(Object id) {
		return super.getById(id, Datastream.class, EXPAND_PROPERTIES);
	}

	public Datastream getByName(String name) {
		return super.getByName(name, Datastream.class, EXPAND_PROPERTIES);
	}

	public List<Datastream> getAll() {
		return super.getAll(Datastream.class);
	}

	public boolean existByName(String name) {
		return super.existByName(name, Datastream.class);
	}

}