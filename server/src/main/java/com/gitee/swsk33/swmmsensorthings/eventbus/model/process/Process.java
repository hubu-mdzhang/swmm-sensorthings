package com.gitee.swsk33.swmmsensorthings.eventbus.model.process;

import com.gitee.swsk33.swmmsensorthings.eventbus.annotation.AllowedValue;
import com.gitee.swsk33.swmmsensorthings.eventbus.annotation.Required;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.constant.JobControl;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.constant.TransmissionMode;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.param.InputDescription;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.param.OutputDescription;
import lombok.Data;

import java.util.Map;

/**
 * API-Processes中任务描述
 */
@Data
public class Process {

	/**
	 * 进程id
	 */
	@Required
	private String id;

	/**
	 * 进程版本号
	 */
	@Required
	private String version;

	/**
	 * 进程标题/显示名称
	 */
	private String title;

	/**
	 * 进程描述
	 */
	private String description;

	/**
	 * 所包含的元数据
	 */
	private Metadata[] metadata;

	/**
	 * 进程所关联的其它内容链接
	 */
	private Link[] links;

	/**
	 * 进程关键字
	 */
	private String[] keywords;

	/**
	 * 支持接收的任务控制方式列表，见{@link JobControl}中常量
	 */
	@AllowedValue({JobControl.SYNC, JobControl.ASYNC, JobControl.DISMISS})
	private String[] jobControlOptions;

	/**
	 * 进程输出结果值的传递形式，键{@link TransmissionMode}中常量
	 */
	@AllowedValue({TransmissionMode.VALUE, TransmissionMode.REFERENCE})
	private String[] outputTransmission;

	/**
	 * 进程的输入参数描述
	 * <ul>
	 *     <li>键：参数名称，也是调用时指定的参数名</li>
	 *     <li>值：参数的描述对象</li>
	 * </ul>
	 */
	private Map<String, InputDescription> inputs;

	/**
	 * 进程的输出参数描述
	 * <ul>
	 *     <li>键：参数名称，也是接收结果时指定的结果参数名</li>
	 *     <li>值：参数的描述对象</li>
	 * </ul>
	 */
	private Map<String, OutputDescription> outputs;

}