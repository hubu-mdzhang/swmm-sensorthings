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

	/**
	 * 查询时展开的属性
	 */
	private static final String[] EXPAND_PROPERTIES = new String[]{"Locations", "Datastreams"};

	public boolean add(Thing object) {
		return super.add(object);
	}

	public boolean remove(Object id) {
		return super.remove(id, Thing.class);
	}

	public Thing getById(Object id) {
		return super.getById(id, Thing.class, EXPAND_PROPERTIES);
	}

	public Thing getByName(String name) {
		return super.getByName(name, Thing.class, EXPAND_PROPERTIES);
	}

	public List<Thing> getAll() {
		return super.getAll(Thing.class);
	}

	public boolean existByName(String name) {
		return super.existByName(name, Thing.class);
	}

}