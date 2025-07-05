package com.gitee.swsk33.swmmsensorthings.eventbus;

import io.github.swsk33.swmmjava.SWMM;
import io.github.swsk33.swmmjava.model.RainGage;
import io.github.swsk33.swmmjava.model.Subcatchment;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.github.swsk33.swmmjava.param.ObjectTypeCode.SUB_CATCHMENT;

/**
 * 单独测试SWMM模拟
 */
@Slf4j
@SpringBootTest
public class SWMMSimulation implements InitializingBean {

	private SWMM swmm;

	@Override
	public void afterPropertiesSet() {
		this.swmm = new SWMM("input-data/d42a78e9ca2342d69e3963cd85d47eec.inp");
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

}