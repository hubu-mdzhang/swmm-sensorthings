package com.gitee.swsk33.swmmsensorthings.eventbus.model.process.response;

import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Link;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Process;
import lombok.Data;

import java.util.ArrayList;
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

	/**
	 * 构造函数
	 *
	 * @param processes 进程列表
	 * @param url       获取全部进程API的完整地址
	 */
	public ProcessList(List<Process> processes, String url) {
		this.processes = processes;
		this.links = new ArrayList<>();
		Link link = new Link();
		link.setType("application/json");
		link.setRel("self");
		link.setHref(url);
		this.links.add(link);
	}

}