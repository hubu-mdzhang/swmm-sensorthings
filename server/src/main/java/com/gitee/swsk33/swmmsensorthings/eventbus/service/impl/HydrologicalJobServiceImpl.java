package com.gitee.swsk33.swmmsensorthings.eventbus.service.impl;

import com.gitee.swsk33.swmmsensorthings.eventbus.model.Result;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Job;
import com.gitee.swsk33.swmmsensorthings.eventbus.property.CoreProperties;
import com.gitee.swsk33.swmmsensorthings.eventbus.service.HydrologicalJobService;
import com.gitee.swsk33.swmmsensorthings.eventbus.task.SimulationTask;
import io.github.swsk33.fileliftspringbootstarter.property.FileSystemProperties;
import io.github.swsk33.swmmjava.SWMM;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
public class HydrologicalJobServiceImpl implements HydrologicalJobService {

	@Autowired
	private BeanFactory beanFactory;

	@Autowired
	private FileSystemProperties fileSystemProperties;

	@Autowired
	private CoreProperties coreProperties;

	@Resource(name = "processTaskPool")
	private ExecutorService taskPool;

	@Override
	public Result<Job> createJob(String inputFile) {
		// 创建水文模型
		SWMM swmm = new SWMM(fileSystemProperties.getSaveFolder() + File.separator + inputFile);
		// 创建异步任务
		SimulationTask task = beanFactory.getBean(SimulationTask.class, inputFile);
		taskPool.execute(task);
		return Result.resultSuccess("水文模型创建成功！", task.getJob());
	}

}