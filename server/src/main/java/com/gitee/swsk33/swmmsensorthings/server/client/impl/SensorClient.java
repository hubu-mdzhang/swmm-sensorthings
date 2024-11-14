package com.gitee.swsk33.swmmsensorthings.server.client.impl;

import com.gitee.swsk33.swmmsensorthings.model.Sensor;
import com.gitee.swsk33.swmmsensorthings.server.client.BaseSensorThingsClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用于请求Sensor对象的具体客户端
 */
@Component
public class SensorClient extends BaseSensorThingsClient<Sensor> {

	public boolean add(Sensor object) {
		return super.add(object);
	}

	public boolean remove(Object id) {
		return super.remove(id, Sensor.class);
	}

	public Sensor get(Object id) {
		return super.get(id, Sensor.class);
	}

	public List<Sensor> getAll() {
		return super.getAll(Sensor.class);
	}

	public boolean existByName(String name) {
		return super.existByName(name, Sensor.class);
	}

}