package com.gitee.swsk33.swmmsensorthings.eventbus.subscriber.http;

import com.gitee.swsk33.swmmsensorthings.eventbus.model.ObservationPool;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import io.github.swsk33.swmmjava.SWMM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 用于接收观测数据、并输入到SWMM模型的HTTP接收器，一个模型实例（的全部传感器）对应一个该接收器
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HttpObservationReceiver {

	/**
	 * 当前订阅者对应的数据缓存池
	 */
	private final ObservationPool dataPool;

	/**
	 * 构造函数
	 *
	 * @param swmm 对应的SWMM模型实例
	 */
	public HttpObservationReceiver(SWMM swmm) {
		this.dataPool = new ObservationPool(swmm);
	}

	/**
	 * 传入接收数据
	 *
	 * @param data 观测数据
	 */
	public void receiveData(Observation data) {
		log.info("HTTP接收到：{}", data);
		// 传递到数据缓存池
		this.dataPool.addData(data);
		// 调用缓存池，在时间步长数据集齐时驱动模型运行
		this.dataPool.runModel();
	}

}