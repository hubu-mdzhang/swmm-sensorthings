package com.gitee.swsk33.swmmsensorthings.eventbus.model.process;

import com.gitee.swsk33.swmmsensorthings.eventbus.annotation.AllowedValue;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.constant.ResponseType;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.data.BoundBox;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.data.ComplexData;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.data.OutputFormat;
import lombok.Data;

import java.util.Map;

/**
 * API-Processes中执行任务的提交对象，包含执行进程时的输入值、输出参数列表等
 */
@Data
public class Execute {

	/**
	 * 输入参数值
	 * <ul>
	 *     <li>键：输入参数的参数名称</li>
	 *     <li>值：输入参数的值，支持三大类数据类型：
	 *     	<ul>
	 * 			<li>基本类型（LiteralInput）：包括数值、字符串、布尔值、数组和四至范围{@link BoundBox}对象</li>
	 * 			<li>复杂类型（ComplexInput）：即{@link ComplexData}对象，其中包含数据格式描述与数据值</li>
	 * 			<li>引用链接（Link）：即{@link Link}对象，表示对数据资源的引用地址</li>
	 *     	</ul>
	 *     	同理，查询任务结果时，得到的结果键值对中的值也是上述三大类对象
	 *     </li>
	 * </ul>
	 */
	private Map<String, Object> inputs;

	/**
	 * 输出参数列表
	 * <ul>
	 *     <li>键：输出参数名称</li>
	 *     <li>值：输出参数格式{@link OutputFormat}对象</li>
	 * </ul>
	 */
	private Map<String, OutputFormat> outputs;

	/**
	 * 响应数据类型，见{@link ResponseType}中常量
	 */
	@AllowedValue({ResponseType.RAW, ResponseType.DOCUMENT})
	private String response = ResponseType.RAW;

	/**
	 * 回调通知订阅者对象
	 */
	private Subscriber subscriber;

}