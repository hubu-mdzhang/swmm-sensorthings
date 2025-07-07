package com.gitee.swsk33.swmmsensorthings.eventbus.api;

import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Execute;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Job;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Process;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.response.ProcessList;
import com.gitee.swsk33.swmmsensorthings.eventbus.property.CoreProperties;
import com.gitee.swsk33.swmmsensorthings.eventbus.service.HydrologicalJobService;
import io.github.swsk33.fileliftspringbootstarter.property.FileSystemProperties;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 符合API-Processes的接口
 */
@RestController
@RequestMapping("/processes")
public class ProcessesAPI {

	@Resource(name = "processMap")
	private Map<String, Process> processes;

	@Autowired
	private HydrologicalJobService hydrologicalJobService;

	@Autowired
	private FileSystemProperties fileSystemProperties;

	@Autowired
	private CoreProperties coreProperties;

	@GetMapping("")
	public ProcessList getProcesses() {
		List<Process> processList = new ArrayList<>();
		for (Process item : processes.values()) {
			Process process = item.clone();
			process.setInputs(null);
			process.setOutputs(null);
			processList.add(process);
		}
		return new ProcessList(processList, String.format("http://%s:%d/processes", coreProperties.getAdvertiseHost(), coreProperties.getAdvertisePort()));
	}

	@GetMapping("/{processId}")
	public Process getProcess(@PathVariable("processId") String processId) {
		if (!processes.containsKey(processId)) {
			return null;
		}
		return processes.get(processId);
	}

	@PostMapping("/{processId}/execution")
	public Job execute(@PathVariable("processId") String processId, @RequestBody Execute execute) {
		if (processId.equals("swmm")) {
			// 提取参数
			String inputFile = String.format("%s/%s", fileSystemProperties.getSaveFolder(), execute.getInputs().get("inputFile").toString());
			// 调用服务
			return hydrologicalJobService.createJob(inputFile).getData();
		}
		return null;
	}

}