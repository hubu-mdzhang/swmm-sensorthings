package com.gitee.swsk33.swmmsensorthings.eventbus.util;

import com.gitee.swsk33.swmmsensorthings.eventbus.client.SensorThingsObjectClient;
import com.gitee.swsk33.swmmsensorthings.model.Datastream;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import io.github.swsk33.swmmjava.model.TimeStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 数据实用类
 */
@Slf4j
@SpringBootTest
public class DataUtils {

	@Autowired
	private SensorThingsObjectClient sensorThingsClient;

	/**
	 * 生成观测属u就
	 *
	 * @param datastreamId 数据流id
	 * @param start        起始时间
	 * @param stepLength   时间步长长度
	 * @param count        生成数据个数
	 * @return 生成的数据
	 */
	public List<Observation> generateObservations(int datastreamId, LocalDateTime start, int stepLength, int count) {
		// 查数据流
		Datastream stream = sensorThingsClient.getById(datastreamId, Datastream.class, null);
		if (stream == null) {
			log.error("没有id为{}的数据流！", datastreamId);
			return null;
		}
		// 创建时间步
		TimeStep currentStep = new TimeStep(start, stepLength);
		// 生成数据
		List<Observation> observations = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < count; i++) {
			Observation data = new Observation();
			data.setResult(random.nextDouble(0.1, 2.2));
			data.setResultTime(currentStep.getStart());
			data.setPhenomenonTime(currentStep.getStart());
			data.setDatastream(stream);
			observations.add(data);
			currentStep = currentStep.nextStep(stepLength);
		}
		return observations;
	}

}
