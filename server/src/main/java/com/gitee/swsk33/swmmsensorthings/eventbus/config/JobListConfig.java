package com.gitee.swsk33.swmmsensorthings.eventbus.config;

import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局任务列表配置
 */
@Slf4j
@Configuration
public class JobListConfig {

	@Bean("jobList")
	public Map<String, Job> jobList() {
		log.info("已初始化API-Processes全局任务列表！");
		return new HashMap<>();
	}

}