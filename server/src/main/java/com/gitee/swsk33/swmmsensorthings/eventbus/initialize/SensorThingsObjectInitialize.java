package com.gitee.swsk33.swmmsensorthings.eventbus.initialize;

import com.alibaba.fastjson2.JSON;
import com.gitee.swsk33.swmmsensorthings.eventbus.deserializer.LocalDateTimeReader;
import com.gitee.swsk33.swmmsensorthings.eventbus.serializer.LocalDateTimeWriter;
import com.gitee.swsk33.swmmsensorthings.eventbus.template.SensorThingsInitializeTemplate;
import io.github.swsk33.swmmjava.SWMM;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用于从SWMM读取全部可视对象，并初始化到SensorThings服务器的类
 */
@Slf4j
@Component
@DependsOn("SWMMConfig")
public class SensorThingsObjectInitialize implements InitializingBean {

	@Autowired
	private SWMM swmm;

	@Autowired
	private SensorThingsInitializeTemplate initializeTemplate;

	@Autowired
	private LocalDateTimeReader localDateTimeReader;

	@Autowired
	private LocalDateTimeWriter localDateTimeWriter;

	@Override
	public void afterPropertiesSet() {
		log.info("------- 开始初始化SensorThings服务对象 -------");
		// 获取全部SWMM对象
		List<VisualObject> objects = swmm.getAllObjects();
		// 遍历初始化
		for (VisualObject object : objects) {
			initializeTemplate.execute(object);
		}
		// 注册时间类型序列化和反序列化器
		JSON.register(LocalDateTime.class, localDateTimeReader);
		JSON.register(LocalDateTime.class, localDateTimeWriter);
		log.info("全部SWMM对象已注册至SensorThings API服务器！");
	}

}