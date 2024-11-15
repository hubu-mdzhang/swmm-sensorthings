package com.gitee.swsk33.swmmsensorthings.server.initialize;

import com.gitee.swsk33.swmmsensorthings.server.template.SensorThingsInitializeTemplate;
import io.github.swsk33.swmmjava.SWMM;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

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

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("------- 开始初始化SensorThings服务对象 -------");
		// 获取全部SWMM对象
		List<VisualObject> objects = swmm.getAllObjects();
		// 遍历初始化
		for (VisualObject object : objects) {
			initializeTemplate.execute(object);
		}
		log.info("全部SWMM对象已注册至SensorThings API服务器！");
	}

}