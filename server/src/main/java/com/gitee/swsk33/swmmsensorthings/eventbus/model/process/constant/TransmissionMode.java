package com.gitee.swsk33.swmmsensorthings.eventbus.model.process.constant;

/**
 * API-Processes中，变量/参数传递的方式常量
 */
public class TransmissionMode {

	/**
	 * 直接传递值<br>
	 * 比如直接传递一个数值、字符串等
	 */
	public static final String VALUE = "value";

	/**
	 * 传递引用<br>
	 * 比如传递文件下载的URL地址等
	 */
	public static final String REFERENCE = "reference";

}