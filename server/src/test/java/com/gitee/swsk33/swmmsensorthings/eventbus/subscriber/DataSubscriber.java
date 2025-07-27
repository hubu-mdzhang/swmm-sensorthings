package com.gitee.swsk33.swmmsensorthings.eventbus.subscriber;

import com.gitee.swsk33.swmmsensorthings.eventbus.model.ReadCursor;
import io.github.swsk33.swmmjava.model.Link;
import io.github.swsk33.swmmjava.model.Node;
import io.github.swsk33.swmmjava.model.VisualObject;
import io.github.swsk33.swmmjava.model.event.VisualObjectEvent;
import reactor.core.publisher.BaseSubscriber;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.swsk33.swmmjava.param.ObjectTypeCode.NODE;
import static javax.swing.TransferHandler.LINK;

/**
 * 反应式数据读取测试订阅者
 */
public class DataSubscriber extends BaseSubscriber<VisualObjectEvent> {

	/**
	 * 标记当前是否正在统计过程中
	 */
	public static volatile boolean isStats = false;

	/**
	 * 每个步长中，接收到第一个数据时的时间
	 */
	public static volatile LocalDateTime firstTime;

	/**
	 * 用于统计每个步长接收读取到的数据个数
	 */
	private final AtomicInteger readCount;

	/**
	 * 若第一个数据时间为空，则设定为当前时间
	 */
	private static void setFirstTime() {
		if (!isStats) {
			return;
		}
		if (firstTime == null) {
			synchronized (DataSubscriber.class) {
				if (firstTime == null) {
					firstTime = LocalDateTime.now();
				}
			}
		}
	}

	/**
	 * 构造函数
	 *
	 * @param readCount 计数器
	 */
	public DataSubscriber(AtomicInteger readCount) {
		this.readCount = readCount;
	}

	@Override
	public void hookOnNext(VisualObjectEvent data) {
		setFirstTime();
		// 根据类型统计个数
		Class<? extends VisualObject> eventType = data.getData().getClass();
		if (Link.class.isAssignableFrom(eventType)) {
			readCount.addAndGet(ReadCursor.TYPE_PROPERTY_COUNT.get(LINK));
		} else if (Node.class.isAssignableFrom(eventType)) {
			readCount.addAndGet(ReadCursor.TYPE_PROPERTY_COUNT.get(NODE));
		}
	}

}