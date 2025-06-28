package com.gitee.swsk33.swmmsensorthings.eventbus.model.process.data;

import com.gitee.swsk33.swmmsensorthings.eventbus.annotation.AllowedValue;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Execute;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Format;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.constant.TransmissionMode;
import lombok.Data;

/**
 * API-Processes中，表示执行参数对象{@link Execute}的输出对象格式
 */
@Data
public class OutputFormat {

	/**
	 * 格式描述
	 */
	private Format format;

	/**
	 * 输出值的传递格式，即引用传递还是值传递
	 */
	@AllowedValue({TransmissionMode.VALUE, TransmissionMode.REFERENCE})
	private String transmissionMode = TransmissionMode.VALUE;

}