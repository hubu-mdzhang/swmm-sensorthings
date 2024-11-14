package com.gitee.swsk33.swmmsensorthings.server.client.impl;

import com.gitee.swsk33.swmmsensorthings.model.Thing;
import com.gitee.swsk33.swmmsensorthings.server.client.BaseSensorThingsClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用于请求Thing对象的具体客户端
 */
@Component
public class ThingClient extends BaseSensorThingsClient<Thing> {

	public boolean add(Thing object) {
		return super.add(object);
	}

	public boolean remove(Object id) {
		return super.remove(id, Thing.class);
	}

	public Thing get(Object id) {
		return super.get(id, Thing.class);
	}

	public List<Thing> getAll() {
		return super.getAll(Thing.class);
	}

	public boolean existByName(String name) {
		return super.existByName(name, Thing.class);
	}

}