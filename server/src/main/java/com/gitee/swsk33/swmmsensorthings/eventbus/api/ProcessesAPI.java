package com.gitee.swsk33.swmmsensorthings.eventbus.api;

import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Process;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 符合API-Processes的接口
 */
@RestController
public class ProcessesAPI {

	@Resource(name = "processMap")
	private Map<String, Process> processes;

	@GetMapping("/processes")
	public List<Process> getProcesses() {
		return null;
	}

	@GetMapping("/processes/{processId}")
	public Process getProcess(@PathVariable("processId") String processId) {
		if (!processes.containsKey(processId)) {
			return null;
		}
		return processes.get(processId);
	}

}