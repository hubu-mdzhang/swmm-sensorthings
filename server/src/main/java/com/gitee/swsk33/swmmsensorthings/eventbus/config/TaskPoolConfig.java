package com.gitee.swsk33.swmmsensorthings.eventbus.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步任务线程池配置
 */
@Slf4j
@Configuration
public class TaskPoolConfig {

	@Bean("processTaskPool")
	public ExecutorService taskPool() {
		log.info("API-Processes任务线程池已完成配置！");
		return Executors.newCachedThreadPool();
	}

}