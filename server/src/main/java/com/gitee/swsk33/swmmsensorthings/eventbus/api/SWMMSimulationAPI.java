package com.gitee.swsk33.swmmsensorthings.eventbus.api;

import com.gitee.swsk33.swmmsensorthings.eventbus.model.Result;
import com.gitee.swsk33.swmmsensorthings.eventbus.service.SWMMSimulationService;
import com.gitee.swsk33.swmmsensorthings.mapper.util.NameUtils;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/swmm-simulation")
public class SWMMSimulationAPI {

	@Autowired
	private SWMMSimulationService simulationService;

	/**
	 * 仅执行一次步长模拟
	 */
	@GetMapping("/step-only")
	public Result<Void> stepRun() {
		return simulationService.stepRun();
	}

	/**
	 * 输入观测数据并进行一次步长模拟
	 *
	 * @param observation 观测数据，需要包含Datastream属性，以及Datastream的name
	 */
	@PostMapping("/input-step")
	public Result<Void> stepRun(@RequestBody Observation observation) {
		// 获取传感器名称
		String sensorName = NameUtils.getObservationSensorName(observation.getDatastream().getName());
		return simulationService.stepRun(sensorName, (Double) observation.getResult());
	}

}