package com.gitee.swsk33.swmmsensorthings.eventbus.api;

import com.gitee.swsk33.swmmsensorthings.eventbus.model.Result;
import com.gitee.swsk33.swmmsensorthings.eventbus.subscriber.DataReceiverContext;
import com.gitee.swsk33.swmmsensorthings.eventbus.subscriber.http.HttpObservationReceiver;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 使用HTTP协议输入观测数据驱动水文模型运行的API
 */
@RestController
@RequestMapping("/api/http-receiver")
public class HttpInputAPI {

	@Autowired
	private DataReceiverContext receiverContext;

	/**
	 * 输入观测数据驱动对应模拟任务的水文模型运行
	 *
	 * @param taskId 任务id
	 * @param data   SensorThings API的Observation对象，要求其中的datastream属性不能为空，且datastream必须包含id和name属性（且不为空）
	 * @return 结果
	 */
	@PostMapping("/input/{taskId}")
	public Result<Void> inputObservation(@PathVariable("taskId") String taskId, @RequestBody Observation data) {
		HttpObservationReceiver receiver = receiverContext.getHttpReceiver(taskId);
		if (receiver == null) {
			return Result.resultFailed("没有对应的任务！");
		}
		receiver.receiveData(data);
		return Result.resultSuccess("输入数据成功！");
	}

}