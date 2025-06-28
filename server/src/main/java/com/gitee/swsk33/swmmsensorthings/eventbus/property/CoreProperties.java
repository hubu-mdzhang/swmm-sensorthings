package com.gitee.swsk33.swmmsensorthings.eventbus.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 关于事件总线服务端核心配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "com.gitee.swsk33.sensor-things-eventbus.core")
public class CoreProperties {

	/**
	 * 服务器本身的广播地址，请配置为外网地址，例如：127.0.0.1
	 */
	private String advertiseHost;

	/**
	 * 服务器本身的广播端口，请配置为外网访问端口，例如：5354
	 */
	private int advertisePort;

}