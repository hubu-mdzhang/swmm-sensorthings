package com.gitee.swsk33.swmmsensorthings.mapper.strategy.impl;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.SensorDataStrategy;
import com.gitee.swsk33.swmmsensorthings.mapper.util.PropertyReadUtils;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import com.gitee.swsk33.swmmsensorthings.model.ObservedProperty;
import io.github.swsk33.swmmjava.model.Subcatchment;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 将子汇水区域转换成SensorThings观测数据对象的具体策略
 */
@Slf4j
public class SubcatchmentMapStrategy implements SensorDataStrategy {

	@Override
	public List<Observation> mapObservation(VisualObject object, LocalDateTime time) {
		List<Observation> observations = new ArrayList<>();
		// 计算属性数据
		try {
			JSONObject computedProperties = PropertyReadUtils.readComputedProperties(object);
			for (String key : computedProperties.keySet()) {
				Observation observation = new Observation();
				observation.setName("The observation record of subcatchment " + object.getId());
				observation.setResultTime(time);
				observation.setPhenomenonTime(time);
				observation.setResult(computedProperties.get(key));
				observations.add(observation);
			}
		} catch (Exception e) {
			log.error("读取计算属性时出现错误！");
			log.error(e.getMessage());
		}
		return observations;
	}

	@Override
	public List<ObservedProperty> mapObservedProperty(VisualObject object) {
		List<ObservedProperty> observations = new ArrayList<>();
		// 计算属性数据
		try {
			JSONObject computedProperties = PropertyReadUtils.readComputedProperties(object);
			for (String key : computedProperties.keySet()) {
				ObservedProperty observation = new ObservedProperty();
				observation.setName(key);
				observation.setDefinition(Subcatchment.class.getName());
				observation.setDescription("The " + key + " property for Subcatchment.");
				observations.add(observation);
			}
		} catch (Exception e) {
			log.error("读取计算属性出现错误！");
			log.error(e.getMessage());
		}
		return observations;
	}

}