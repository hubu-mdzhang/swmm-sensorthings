package com.gitee.swsk33.swmmsensorthings.eventbus.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 关于API-Processes服务相关配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "com.gitee.swsk33.sensor-things-eventbus.api-processes")
public class ProcessProperties {

	/**
	 * 配置Process JSON文件列表
	 * <ul>
	 *     <li>本地文件路径以<code>file:</code>开头</li>
	 *     <li>ClassPath文件路径以<code>classpath:</code>开头</li>
	 * </ul>
	 */
	private String[] processList;

}