package com.gitee.swsk33.swmmsensorthings.eventbus.api;

import com.gitee.swsk33.swmmsensorthings.eventbus.model.Result;
import io.github.swsk33.swmmjava.SWMM;
import io.github.swsk33.swmmjava.model.SWMMSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/swmm-system")
public class SWMMSystemAPI {

	@Autowired
	private SWMM swmm;

	/**
	 * 获取当前SWMM水文系统的状态
	 *
	 * @return 水文系统对象
	 */
	@GetMapping("/get")
	public Result<SWMMSystem> getSystem() {
		return Result.resultSuccess("获取水文系统状态完成！", swmm.getSystem());
	}

}