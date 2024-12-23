package com.gitee.swsk33.swmmsensorthings.eventbus.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 关于SWMM输入的相关配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "com.gitee.swsk33.swmm")
public class SWMMInputProperty {

	/**
	 * 输入文件（inp文件）路径
	 */
	private String inputFile;

	/**
	 * 报告文件（rpt文件）路径
	 */
	private String reportFile;

	/**
	 * 输出文件（out文件）路径，可使用空字符串表示不输出为out
	 */
	private String outputFile = "";

}