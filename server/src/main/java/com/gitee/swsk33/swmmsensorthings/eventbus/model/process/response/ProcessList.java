package com.gitee.swsk33.swmmsensorthings.eventbus.model.process.response;

import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Link;
import lombok.Data;

import java.util.List;

/**
 * API-Processes的获取进程列表响应
 */
@Data
public class ProcessList {

	/**
	 * 全部进程列表
	 */
	private List<Process> processes;

	/**
	 * 全部链接对象列表
	 */
	private List<Link> links;

}