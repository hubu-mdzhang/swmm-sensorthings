package com.gitee.swsk33.swmmsensorthings.eventbus.config;

import com.alibaba.fastjson2.JSON;
import com.gitee.swsk33.swmmsensorthings.eventbus.deserializer.LocalDateTimeReader;
import com.gitee.swsk33.swmmsensorthings.eventbus.serializer.LocalDateTimeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * JSON配置
 */
@Slf4j
@Component
public class JSONConfig implements InitializingBean {

	@Autowired
	private LocalDateTimeReader localDateTimeReader;

	@Autowired
	private LocalDateTimeWriter localDateTimeWriter;

	@Override
	public void afterPropertiesSet() {
		// 注册时间类型序列化和反序列化器
		JSON.register(LocalDateTime.class, localDateTimeReader);
		JSON.register(LocalDateTime.class, localDateTimeWriter);
		log.info("已完成JSON序列化与反序列化配置！");
	}

}