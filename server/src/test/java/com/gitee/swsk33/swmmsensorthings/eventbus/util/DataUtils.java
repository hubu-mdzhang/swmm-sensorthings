package com.gitee.swsk33.swmmsensorthings.eventbus.util;

import cn.hutool.core.io.FileUtil;
import com.gitee.swsk33.swmmsensorthings.eventbus.client.SensorThingsObjectClient;
import com.gitee.swsk33.swmmsensorthings.model.Datastream;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import io.github.swsk33.swmmjava.model.TimeStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 数据实用类
 */
@Slf4j
@Component
public class DataUtils {

	@Autowired
	private SensorThingsObjectClient sensorThingsClient;

	/**
	 * 生成观测属u就
	 *
	 * @param datastreamId 数据流id
	 * @param start        起始时间
	 * @param stepLength   时间步长长度（秒）
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
		stream.setResultTime(null);
		stream.setPhenomenonTime(null);
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

	/**
	 * 计算两个时间列表之间的延迟
	 *
	 * @param startList 记录开始时间的列表
	 * @param endList   记录结束时间的列表
	 * @return 两个时间列表的延迟列表，单位ns
	 */
	public List<Long> computeDelay(List<LocalDateTime> startList, List<LocalDateTime> endList) {
		int size = startList.size();
		List<Long> delayList = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			LocalDateTime start = startList.get(i);
			LocalDateTime end = endList.get(i);
			delayList.add(Duration.between(start, end).toNanos());
		}
		return delayList;
	}

	/**
	 * 输出为CSV文件
	 *
	 * @param path    文件路径
	 * @param content 文件内容
	 */
	public void writeCSV(String path, Map<String, List<Long>> content) {
		// 对应顺序
		List<String> headers = new ArrayList<>();
		List<List<Long>> data = new ArrayList<>();
		for (Map.Entry<String, List<Long>> entry : content.entrySet()) {
			headers.add(entry.getKey());
			data.add(entry.getValue());
		}
		// 全部行
		List<String> lines = new ArrayList<>();
		lines.add(String.join(",", headers));
		int dataSize = data.getFirst().size();
		for (int i = 0; i < dataSize; i++) {
			List<String> row = new ArrayList<>();
			for (int j = 0; j < headers.size(); j++) {
				row.add(String.valueOf(data.get(j).get(i)));
			}
			lines.add(String.join(",", row));
		}
		// 写入
		path = Paths.get(path).toAbsolutePath().toString();
		if (!Files.exists(Paths.get(path).getParent())) {
			FileUtil.mkParentDirs(path);
		}
		FileUtil.writeLines(lines, path, StandardCharsets.UTF_8);
		log.info("已写入文件：{}", path);
	}

}