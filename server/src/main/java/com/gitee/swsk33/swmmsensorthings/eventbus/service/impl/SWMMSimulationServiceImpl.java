package com.gitee.swsk33.swmmsensorthings.eventbus.service.impl;

import com.gitee.swsk33.swmmsensorthings.eventbus.model.Result;
import com.gitee.swsk33.swmmsensorthings.eventbus.service.SWMMSimulationService;
import io.github.swsk33.swmmjava.SWMM;
import io.github.swsk33.swmmjava.model.RainGage;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static io.github.swsk33.swmmjava.param.ObjectTypeCode.GAGE;

@Slf4j
@Service
public class SWMMSimulationServiceImpl implements SWMMSimulationService {

	@Autowired
	private SWMM swmm;

	@Override
	public Result<Void> stepRun() {
		double elapsedTime = swmm.step();
		if (elapsedTime == 0) {
			return Result.resultFailed("模拟已结束！");
		}
		return Result.resultSuccess("已完成一次步长模拟！");
	}

	@Override
	public Result<Void> stepRun(String gageName, double rainfall) {
		VisualObject gage = swmm.getObject(GAGE, gageName);
		if (gage == null) {
			return Result.resultFailed(String.format("ID为%s的雨量计不存在！", gageName));
		}
		RainGage patchGage = new RainGage();
		patchGage.setIndex(gage.getIndex());
		patchGage.setRainfall(rainfall);
		try {
			swmm.setValue(patchGage);
		} catch (Exception e) {
			return Result.resultFailed("设定雨量计降水属性失败！");
		}
		return stepRun();
	}

}