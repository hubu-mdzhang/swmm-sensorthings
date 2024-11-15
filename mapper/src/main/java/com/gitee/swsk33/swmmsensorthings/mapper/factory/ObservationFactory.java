package com.gitee.swsk33.swmmsensorthings.mapper.factory;

import com.gitee.swsk33.swmmsensorthings.mapper.factory.impl.FeatureOfInterestFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.util.PropertyReadUtils;
import com.gitee.swsk33.swmmsensorthings.model.Datastream;
import com.gitee.swsk33.swmmsensorthings.model.FeatureOfInterest;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import io.github.swsk33.swmmjava.model.Subcatchment;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * 创建观测数据的简单工厂
 */
@Slf4j
public class ObservationFactory {

	/**
	 * 根据传入的SWMM可视对象，读取并创建指定属性的观测值为观测对象，此外会同时创建对应的Datastream对象并关联<br>
	 * 如果object类型为Subcatchment，则还会创建FeatureOfInterest并关联
	 *
	 * @param object SWMM可视对象
	 * @param name   计算属性名称，可通过{@link PropertyReadUtils}的<code>getComputedPropertyNames</code>方法获取一个对象的可视对象名称
	 * @param time   观测记录时间
	 * @return 观测记录对象，出现错误返回null
	 */
	public static Observation createObservation(VisualObject object, String name, LocalDateTime time) {
		// 如果是子汇水区域类型，则同时创建数据流与兴趣要素
		if (Subcatchment.class.isAssignableFrom(object.getClass())) {
			return createObservation(object, name, DatastreamFactory.createDatastream(object, name), (FeatureOfInterest) FeatureOfInterestFactory.getInstance().createObject(object), time);
		}
		// 否则，仅创建数据流并关联
		return createObservation(object, name, DatastreamFactory.createDatastream(object, name), time);
	}

	/**
	 * 根据传入的SWMM可视对象，读取并创建指定属性的观测值为观测对象，并关联已有数据流
	 *
	 * @param object SWMM可视对象
	 * @param name   计算属性名称，可通过{@link PropertyReadUtils}的<code>getComputedPropertyNames</code>方法获取一个对象的可视对象名称
	 * @param stream 这条观测记录所属的数据流对象
	 * @param time   观测记录时间
	 * @return 观测记录对象，出现错误返回null
	 */
	public static Observation createObservation(VisualObject object, String name, Datastream stream, LocalDateTime time) {
		// 读取观测值
		Object result;
		try {
			result = PropertyReadUtils.getComputedPropertyValue(object, name);
		} catch (Exception e) {
			log.error("读取计算属性：{}出现错误！", name);
			log.error(e.getMessage());
			return null;
		}
		// 创建观测值对象
		Observation observation = new Observation();
		observation.setPhenomenonTime(time);
		observation.setResultTime(time);
		observation.setResult(result);
		// 关联Datastream时，如果id不为空，则仅传递id字段
		if (stream.getId() != null) {
			Datastream streamId = new Datastream();
			streamId.setId(stream.getId());
			stream = streamId;
		}
		observation.setDatastream(stream);
		return observation;
	}

	/**
	 * 根据传入的SWMM可视对象，读取并创建指定属性的观测值为观测对象
	 *
	 * @param object  SWMM可视对象
	 * @param name    计算属性名称，可通过{@link PropertyReadUtils}的<code>getComputedPropertyNames</code>方法获取一个对象的可视对象名称
	 * @param stream  这条观测记录所属的数据流对象
	 * @param feature 关联的兴趣要素
	 * @param time    观测记录时间
	 * @return 观测记录对象，出现错误返回null
	 */
	public static Observation createObservation(VisualObject object, String name, Datastream stream, FeatureOfInterest feature, LocalDateTime time) {
		Observation observation = createObservation(object, name, stream, time);
		if (observation != null) {
			observation.setFeatureOfInterest(feature);
		}
		return observation;
	}

}