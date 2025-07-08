package com.gitee.swsk33.swmmsensorthings.eventbus.api;

import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Job;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.response.JobList;
import com.gitee.swsk33.swmmsensorthings.eventbus.property.CoreProperties;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
public class JobAPI {

	@Resource(name = "jobList")
	private Map<String, Job> jobList;

	@Autowired
	private CoreProperties coreProperties;

	@GetMapping("")
	public JobList getJobs() {
		List<Job> jobs = new ArrayList<>(jobList.values());
		return new JobList(jobs, String.format("http://%s:%d/processes", coreProperties.getAdvertiseHost(), coreProperties.getAdvertisePort()));
	}

	@GetMapping("/{jobId}")
	public Job getJob(@PathVariable("jobId") String jobId) {
		return jobList.get(jobId);
	}

}