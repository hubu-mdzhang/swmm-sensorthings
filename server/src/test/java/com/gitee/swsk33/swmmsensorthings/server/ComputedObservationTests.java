package com.gitee.swsk33.swmmsensorthings.server;

import com.gitee.swsk33.swmmsensorthings.server.model.Result;
import com.gitee.swsk33.swmmsensorthings.server.service.HydrologicalSimulationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 用于模拟SWMM运行、并将模拟数据转换成观测值发送给SensorThings服务器的测试类
 */
@Slf4j
@SpringBootTest
class ComputedObservationTests {

	@Autowired
	private HydrologicalSimulationService simulationService;

	@Test
	@DisplayName("测试模拟与计算观测值发送")
	void testSimulationAndObservationPost() {
		Result<Void> result;
		while ((result = simulationService.stepRun()).isSuccess()) {
			log.info(result.getMessage());
		}
		log.warn(result.getMessage());
	}

	@Test
	@DisplayName("测试输入降水数据，计算观测并发送")
	void testInputSimulationAndObservationPost() {
		Result<Void> result;
		while ((result = simulationService.stepRun("Gage1", 10)).isSuccess()) {
			log.info(result.getMessage());
		}
		log.warn(result.getMessage());
	}

}