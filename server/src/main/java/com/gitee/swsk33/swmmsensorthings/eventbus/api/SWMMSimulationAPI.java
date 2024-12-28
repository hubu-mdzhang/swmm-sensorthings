package com.gitee.swsk33.swmmsensorthings.eventbus.api;

import com.gitee.swsk33.swmmsensorthings.eventbus.model.Result;
import com.gitee.swsk33.swmmsensorthings.eventbus.service.SWMMSimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}