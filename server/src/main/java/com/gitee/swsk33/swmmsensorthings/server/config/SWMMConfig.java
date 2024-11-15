package com.gitee.swsk33.swmmsensorthings.server.config;

import com.gitee.swsk33.swmmsensorthings.server.property.SWMMInputProperty;
import io.github.swsk33.swmmjava.SWMM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置SWMM操作对象的配置类
 */
@Slf4j
@Configuration("SWMMConfig")
public class SWMMConfig {

	@Autowired
	private SWMMInputProperty inputProperty;

	@Bean
	public SWMM configSWMM() {
		SWMM swmm = new SWMM(inputProperty.getInputFile(), inputProperty.getReportFile(), inputProperty.getOutputFile());
		boolean started = swmm.start();
		if (!started) {
			log.error("SWMM引擎启动失败！");
		} else {
			log.info("------- SWMM，启动！ -------");
		}
		return swmm;
	}

}