package com.gitee.swsk33.swmmsensorthings.eventbus.context;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 记录读取方式延迟的列表
 */
@Component
public class ReadDelayList {

	/**
	 * 记录每一个模拟步长执行时，使用同步方法读取一组计算结果的时间
	 */
	public final List<LocalDateTime> syncReadTimeList = new ArrayList<>();

	/**
	 * 记录每一个模拟步长执行时，使用异步反应式流读取一组计算结果的时间
	 */
	public final List<LocalDateTime> reactiveReadTimeList = new ArrayList<>();

}