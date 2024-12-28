package com.gitee.swsk33.swmmsensorthings.eventbus.config;

import com.gitee.swsk33.swmmsensorthings.eventbus.property.SWMMInputProperty;
import com.gitee.swsk33.swmmsensorthings.eventbus.subscriber.reactor.SensorThingsSubscriber;
import io.github.swsk33.swmmjava.SWMM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.swsk33.swmmjava.param.ObjectTypeCode.*;

/**
 * 配置SWMM操作对象的配置类
 */
@Slf4j
@Configuration("SWMMConfig")
public class SWMMConfig {

	@Autowired
	private SWMMInputProperty inputProperty;

	@Autowired
	private BeanFactory beanFactory;

	@Bean
	public SWMM configSWMM() {
		// 创建SWMM引擎对象
		SWMM swmm = new SWMM(inputProperty.getInputFile(), inputProperty.getReportFile(), inputProperty.getOutputFile());
		// 订阅所有数据
		// swmm.subscribe(GAGE, beanFactory.getBean(SensorThingsSubscriber.class));
		swmm.subscribe(SUB_CATCHMENT, beanFactory.getBean(SensorThingsSubscriber.class));
		swmm.subscribe(LINK, beanFactory.getBean(SensorThingsSubscriber.class));
		swmm.subscribe(NODE, beanFactory.getBean(SensorThingsSubscriber.class));
		// 启动！
		boolean started = swmm.start();
		if (!started) {
			log.error("SWMM引擎启动失败！");
		} else {
			log.info("------- SWMM，启动！ -------");
		}
		return swmm;
	}

}