package com.gitee.swsk33.swmmsensorthings.eventbus;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.gitee.swsk33.swmmsensorthings.eventbus.util.DataUtils;
import com.gitee.swsk33.swmmsensorthings.eventbus.util.DoubleValueUtils;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import io.github.swsk33.swmmjava.SWMM;
import io.github.swsk33.swmmjava.model.Link;
import io.github.swsk33.swmmjava.model.Node;
import io.github.swsk33.swmmjava.model.RainGage;
import io.github.swsk33.swmmjava.model.Subcatchment;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import static io.github.swsk33.swmmjava.param.ObjectTypeCode.*;

/**
 * 单独测试SWMM模拟
 */
@Slf4j
@SpringBootTest
public class SWMMSimulationTests implements InitializingBean {

	@Autowired
	private DataUtils dataUtils;

	private SWMM swmm;

	@Override
	public void afterPropertiesSet() {
		this.swmm = new SWMM("test-data/input.inp");
		this.swmm.start();
	}

	@Test
	@DisplayName("测试步长执行")
	void testStepRun() throws Exception {
		Random random = new Random();
		log.info("时间步长：{}s", this.swmm.getSystem().getCurrentRoutingStep());
		log.info("===================================");
		while (!this.swmm.isComplete()) {
			log.info("当前模拟时间：{}", this.swmm.getSystem().getCurrentDate());
			// 随机模拟降水数据
			RainGage gage = new RainGage();
			gage.setIndex(0);
			gage.setRainfall(random.nextDouble(0.2, 2.0));
			this.swmm.setValue(gage);
			this.swmm.step();
			log.info("降水强度：{}", gage.getRainfall());
			log.info("S2径流：{}", ((Subcatchment) this.swmm.getObject(SUB_CATCHMENT, "S2")).getRunoff());
			log.info("===================================");
		}
	}

	@Test
	@DisplayName("测试干湿步长")
	void testTimeStepLength() throws Exception {
		// 生成数据
		List<Double> rainfalls = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			rainfalls.add(0.0);
		}
		Random random = new Random();
		for (int i = 0; i < 30; i++) {
			rainfalls.add(random.nextDouble(0.3, 2.5));
		}
		for (double rainfall : rainfalls) {
			RainGage gage = new RainGage();
			gage.setIndex(0);
			gage.setRainfall(rainfall);
			this.swmm.setValue(gage);
			log.info("当前模拟时间：{}，时间步长长度：{}s，实际时间步长：{}", this.swmm.getSystem().getCurrentDate(), this.swmm.getSystem().getCurrentRoutingStep(), this.swmm.getSystem().getCurrentStep());
			this.swmm.step();
			log.info("当前降水强度：{}", gage.getRainfall());
			log.info("当前S2径流：{}", ((Subcatchment) this.swmm.getObject(SUB_CATCHMENT, "S2")).getRunoff());
			log.info("====================================");
			if (this.swmm.isComplete()) {
				log.warn("模拟结束！");
				break;
			}
		}
	}

	@Test
	@DisplayName("测试运行模型任务")
	void testRunModelTask() throws Exception {
		List<Observation> dataList = JSON.parseArray(FileUtil.readBytes(Paths.get("input-data/rainfall.json").toAbsolutePath()), Observation.class);
		// 平均值数据
		Map<String, List<Double>> totalResults = new HashMap<>();
		// 区域S18径流
		totalResults.put("S18", new ArrayList<>());
		// 连接处11003测流入水量
		totalResults.put("11003", new ArrayList<>());
		// 出水口point41流入水量
		totalResults.put("point41", new ArrayList<>());
		// 管道15当前水量
		totalResults.put("15", new ArrayList<>());
		for (int i = 0; i < dataList.size() - 1; i++) {
			Observation data = dataList.get(i);
			LocalDateTime end = dataList.get(i + 1).getPhenomenonTime();
			LocalDateTime simulationTime = this.swmm.getSystem().getCurrentDate();
			// 记录当前步长所有数据
			Map<String, List<Double>> stepResults = new HashMap<>();
			// 区域S18径流
			stepResults.put("S18", new ArrayList<>());
			// 连接处11003测流入水量
			stepResults.put("11003", new ArrayList<>());
			// 出水口point41流入水量
			stepResults.put("point41", new ArrayList<>());
			// 管道15当前水量
			stepResults.put("15", new ArrayList<>());
			while (simulationTime.isBefore(end)) {
				// 随机模拟降水数据
				RainGage gage = new RainGage();
				gage.setIndex(0);
				gage.setRainfall(DoubleValueUtils.parseDouble(data.getResult()));
				this.swmm.setValue(gage);
				this.swmm.step();
				simulationTime = this.swmm.getSystem().getCurrentDate();
				log.info("当前时间：{}", simulationTime);
				log.info("降水：{}", gage.getRainfall());
				double s18Rainfall = ((Subcatchment) this.swmm.getObject(SUB_CATCHMENT, "S18")).getRunoff();
				stepResults.get("S18").add(s18Rainfall);
				log.info("S18径流：{}", s18Rainfall);
				double j11003LInf = ((Node) this.swmm.getObject(NODE, "11003")).getLaterInflow();
				stepResults.get("11003").add(j11003LInf);
				log.info("J11003侧面水量：{}", j11003LInf);
				double p41Inf = ((Node) this.swmm.getObject(NODE, "point41")).getTotalInflow();
				stepResults.get("point41").add(p41Inf);
				log.info("point41流入水量：{}", p41Inf);
				double l15cf = ((Link) this.swmm.getObject(LINK, "15")).getCurrentFlow();
				stepResults.get("15").add(l15cf);
				log.info("L15当前流量：{}", l15cf);
				log.info("==================================");
			}
			// 取平均值
			totalResults.get("S18").add(stepResults.get("S18").stream().mapToDouble(Double::doubleValue).average().getAsDouble());
			totalResults.get("11003").add(stepResults.get("11003").stream().mapToDouble(Double::doubleValue).average().getAsDouble());
			totalResults.get("point41").add(stepResults.get("point41").stream().mapToDouble(Double::doubleValue).average().getAsDouble());
			totalResults.get("15").add(stepResults.get("15").stream().mapToDouble(Double::doubleValue).average().getAsDouble());
			for (String key : stepResults.keySet()) {
				stepResults.get(key).clear();
			}
		}
		dataUtils.writeCSV("test-output/result.csv", totalResults);
	}

}