package com.gitee.swsk33.swmmsensorthings.eventbus.service;

import com.gitee.swsk33.swmmsensorthings.eventbus.model.Result;

/**
 * 管理水文模型模拟任务的服务类
 */
public interface HydrologicalJobService {

	/**
	 * 创建一个水文模型运行实例
	 *
	 * @param inputFile 输入的水文模型inp文件
	 * @return SWMM输出out文件链接
	 */
	Result<String> createJob(String inputFile) throws Exception;

}