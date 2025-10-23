package com.gitee.swsk33.swmmsensorthings.eventbus.stats;

import com.gitee.swsk33.swmmsensorthings.eventbus.context.ReadDelayList;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.ReadCursor;
import com.gitee.swsk33.swmmsensorthings.eventbus.subscriber.DataSubscriber;
import com.gitee.swsk33.swmmsensorthings.eventbus.util.DataUtils;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import io.github.swsk33.swmmjava.SWMM;
import io.github.swsk33.swmmjava.SWMMNative;
import io.github.swsk33.swmmjava.model.RainGage;
import io.github.swsk33.swmmjava.param.GagePropertyCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.swsk33.swmmjava.param.ObjectTypeCode.LINK;
import static io.github.swsk33.swmmjava.param.ObjectTypeCode.NODE;

/**
 * 面向对象数据读取方式延迟对比
 */
@Slf4j
@SpringBootTest
public class ReadDelayStatsTests {

	/**
	 * 测试步长数
	 */
	private static final int STEP_COUNT = 60;

	@Autowired
	private ReadDelayList readDelayList;

	@Autowired
	private DataUtils dataUtils;

	/**
	 * 执行步长模拟时，每个步长执行后同步读取SWMM模型的指定个数的计算结果对象，并记录耗时
	 *
	 * @param swmm          模型（jni直接调用对象，需要已经open和start）
	 * @param readDataCount 每个步长读取的计算结果对象数量
	 */
	private void syncReadData(SWMMNative swmm, int readDataCount) {
		// 初始化读取参数枚举列表
		List<ReadCursor> readCursors = new ArrayList<>();
		Map<Integer, Integer> typeCountMap = new HashMap<>();
		typeCountMap.put(NODE, swmm.getCount(NODE));
		typeCountMap.put(LINK, swmm.getCount(LINK));
		for (Map.Entry<Integer, List<Integer>> entry : ReadCursor.READ_PROPERTIES.entrySet()) {
			int typeCount = typeCountMap.get(entry.getKey());
			entry.getValue().forEach(propertyCode -> {
				for (int i = 0; i < typeCount; i++) {
					readCursors.add(new ReadCursor(i, propertyCode));
				}
			});
		}
		// 逐个步长运行
		for (int i = 0; i < STEP_COUNT; i++) {
			// 运行一步长
			swmm.step(new double[1]);
			// 记录开始时间
			LocalDateTime start = LocalDateTime.now();
			// 开始读取
			int currentCount = 0;
			int cursorIndex = 0;
			boolean stepReadDone = false;
			while (!stepReadDone) {
				for (; cursorIndex < readCursors.size() && !stepReadDone; cursorIndex++) {
					ReadCursor cursor = readCursors.get(cursorIndex);
					swmm.getValue(cursor.getProperty(), cursor.getIndex());
					currentCount++;
					stepReadDone = currentCount >= readDataCount;
				}
				// 若全部属性读取了一遍仍然未读取完成，则继续读取
				if (!stepReadDone) {
					cursorIndex = 0;
				}
			}
			// 记录结束时间
			LocalDateTime end = LocalDateTime.now();
			readDelayList.syncReadTimeList.add(Duration.between(start, end).toNanos());
		}
	}

	/**
	 * 执行步长模拟时，每个步长执行后通过异步响应式流读取对应数量的计算结果，并记录耗时
	 *
	 * @param swmm          模型实例
	 * @param readDataCount 每个步长读取的计算结果对象数量
	 */
	private void reactiveReadData(SWMM swmm, int readDataCount) {
		// 每个步长的读取个数计数器
		AtomicInteger readCount = new AtomicInteger(0);
		// 订阅全部
		swmm.subscribe(NODE, new DataSubscriber(readCount));
		swmm.subscribe(LINK, new DataSubscriber(readCount));
		// 逐个步长运行
		for (int i = 0; i < STEP_COUNT; i++) {
			// 运行一个步长
			DataSubscriber.isStats = true;
			Thread.startVirtualThread(swmm::step);
			// 等待直到读取到指定个数数据
			while (readCount.get() < readDataCount) {
				// ...
			}
			// 记录结束时间
			DataSubscriber.isStats = false;
			LocalDateTime end = LocalDateTime.now();
			readDelayList.reactiveReadTimeList.add(Duration.between(DataSubscriber.firstTime, end).toNanos());
			// 重置部分计数变量
			readCount.set(0);
			DataSubscriber.firstTime = null;
		}
	}

	@Test
	@DisplayName("测试读取延迟")
	void testReadDelay() throws Exception {
		ReadCursor.init();
		// 创建数据
		LocalDateTime start = LocalDateTime.of(2025, 7, 1, 12, 0, 0);
		int stepLengthSecond = 60;
		int readDataCount = 30;
		List<Observation> dataList = dataUtils.generateObservations(1, start, stepLengthSecond, STEP_COUNT);
		// 2. 执行一轮测试
		String inputFile = "test-data/input.inp";
		log.info("正在测试同步读取...");
		SWMMNative swmmNative = new SWMMNative();
		swmmNative.open(inputFile, inputFile + ".rpt", inputFile + ".out");
		swmmNative.start(0);
		for (Observation observation : dataList) {
			swmmNative.setValue(GagePropertyCode.RAINFALL, 0, (Double) observation.getResult());
			syncReadData(swmmNative, readDataCount);
		}
		swmmNative.close();
		log.info("正在测试异步读取...");
		SWMM swmm = new SWMM(inputFile);
		for (Observation observation : dataList) {
			RainGage patch = new RainGage();
			patch.setIndex(0);
			patch.setRainfall((Double) observation.getResult());
			swmm.setValue(patch);
			reactiveReadData(swmm, readDataCount);
		}
		Thread.sleep(3000);
		Map<String, List<Long>> result = new HashMap<>();
		result.put("sync", readDelayList.syncReadTimeList);
		result.put("reactive", readDelayList.reactiveReadTimeList);
//		dataUtils.writeCSV(String.format("test-output/read-delay-%d.csv", readDataCount), result);
	}

}