package com.gitee.swsk33.swmmsensorthings.eventbus.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import com.alibaba.fastjson2.JSON;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Link;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Process;
import com.gitee.swsk33.swmmsensorthings.eventbus.property.CoreProperties;
import com.gitee.swsk33.swmmsensorthings.eventbus.property.ProcessProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * API-Processes算子配置
 */
@Slf4j
@Configuration
public class ProcessConfig {

	@Bean("processMap")
	public Map<String, Process> processMap(ProcessProperties processProperties, CoreProperties coreProperties) {
		if (processProperties.getProcessList() == null || processProperties.getProcessList().length == 0) {
			log.warn("未配置任何API-Processes进程！");
			return null;
		}
		// 读取解析Process描述文件
		final String FILE_PREFIX = "file:";
		final String CLASSPATH_PREFIX = "classpath:";
		Map<String, Process> processMap = new HashMap<>();
		for (String path : processProperties.getProcessList()) {
			// 本地文件系统读取
			if (path.startsWith(FILE_PREFIX)) {
				String processFile = path.substring(FILE_PREFIX.length());
				byte[] content = FileUtil.readBytes(processFile);
				Process process = JSON.parseObject(content, Process.class);
				processMap.put(process.getId(), process);
			}
			// classpath读取
			if (path.startsWith(CLASSPATH_PREFIX)) {
				String processFile = path.substring(CLASSPATH_PREFIX.length());
				ClassPathResource resource = new ClassPathResource(processFile);
				try (InputStream stream = resource.getStream()) {
					Process process = JSON.parseObject(stream, Process.class);
					processMap.put(process.getId(), process);
				} catch (Exception e) {
					log.error("读取{}发生错误！", path);
					log.error(e.getMessage());
				}
			}
		}
		// 最后，给每个Process添加一个执行URL
		processMap.forEach((id, process) -> {
			if (process.getLinks() == null || process.getLinks().length == 0) {
				Link link = new Link();
				link.setTitle("Execute endpoint");
				link.setRel("http://www.opengis.net/def/rel/ogc/1.0/execute");
				link.setHref(String.format("http://%s:%d/processes/%s/execution", coreProperties.getAdvertiseHost(), coreProperties.getAdvertisePort(), id));
				process.setLinks(new Link[]{link});
			}
		});
		log.info("已加载{}个API-Processes进程！", processMap.size());
		return processMap;
	}

}