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

	public boolean add(Datastream object) {
		return super.add(object);
	}

	public boolean remove(Object id) {
		return super.remove(id, Datastream.class);
	}

	public Datastream get(Object id) {
		return super.get(id, Datastream.class);
	}

	public List<Datastream> getAll() {
		return super.getAll(Datastream.class);
	}

	public boolean existByName(String name) {
		return super.existByName(name, Datastream.class);
	}

}