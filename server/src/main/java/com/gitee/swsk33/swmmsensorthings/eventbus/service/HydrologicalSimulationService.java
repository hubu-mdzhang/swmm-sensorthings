package com.gitee.swsk33.swmmsensorthings.eventbus.service;

import com.gitee.swsk33.swmmsensorthings.eventbus.model.Result;
import org.springframework.stereotype.Service;

/**
 * 调用SWMM水文模型进行模拟的服务
 */
@Service
public interface HydrologicalSimulationService {

	/**
	 * 模拟运行一个步长，并将全部计算属性结果发布到SensorThings API服务器
	 */
	Result<Void> stepRun();

	/**
	 * 设定当前步长的降水强度，然后模拟一个步长，并将全部计算属性结果发布到SensorThings API服务器
	 *
	 * @param gageName 设定降水强度的雨量计ID
	 * @param rainfall 当前步长的降水强度
	 */
	Result<Void> stepRun(String gageName, double rainfall);

}