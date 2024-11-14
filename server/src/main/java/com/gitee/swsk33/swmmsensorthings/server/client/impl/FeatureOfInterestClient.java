package com.gitee.swsk33.swmmsensorthings.server.client.impl;

import com.gitee.swsk33.swmmsensorthings.model.FeatureOfInterest;
import com.gitee.swsk33.swmmsensorthings.server.client.BaseSensorThingsClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用于请求FeatureOfInterest对象的具体客户端
 */
@Component
public class FeatureOfInterestClient extends BaseSensorThingsClient<FeatureOfInterest> {

	/**
	 * 查询时展开的属性
	 */
	private static final String[] EXPAND_PROPERTIES = new String[]{"Observations"};

	public boolean add(FeatureOfInterest object) {
		return super.add(object);
	}

	public boolean remove(Object id) {
		return super.remove(id, FeatureOfInterest.class);
	}

	public FeatureOfInterest getById(Object id) {
		return super.getById(id, FeatureOfInterest.class, EXPAND_PROPERTIES);
	}

	public FeatureOfInterest getByName(String name) {
		return super.getByName(name, FeatureOfInterest.class, EXPAND_PROPERTIES);
	}

	public List<FeatureOfInterest> getAll() {
		return super.getAll(FeatureOfInterest.class);
	}

	public boolean existByName(String name) {
		return super.existByName(name, FeatureOfInterest.class);
	}

}