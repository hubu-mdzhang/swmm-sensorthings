package com.gitee.swsk33.swmmsensorthings.eventbus.model.process.response;

import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Job;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Link;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * API-Processes的获取全部任务列表响应
 */
@Data
public class JobList {

	/**
	 * 全部任务列表
	 */
	private List<Job> jobs;

	/**
	 * 相关链接对象列表
	 */
	private List<Link> links;

	/**
	 * 构造函数
	 *
	 * @param jobs 全部任务列表
	 * @param url  获取全部任务的API完整地址
	 */
	public JobList(List<Job> jobs, String url) {
		this.jobs = jobs;
		this.links = new ArrayList<>();
		Link link = new Link();
		link.setType("application/json");
		link.setRel("self");
		link.setHref(url);
		this.links.add(link);
	}

}