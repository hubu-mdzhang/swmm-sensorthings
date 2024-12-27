package com.gitee.swsk33.swmmsensorthings.eventbus.deserializer;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

/**
 * 自定义OffsetDateTime的反序列化器
 */
@Component
public class OffsetDateTimeReader implements ObjectReader<OffsetDateTime> {

	@Override
	public OffsetDateTime readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
		if (jsonReader.nextIfNull()) {
			return null;
		}
		// 解析时间
		String timeString = jsonReader.readString();
		if (!timeString.contains("/")) {
			return ZonedDateTime.parse(timeString).toOffsetDateTime();
		}
		return ZonedDateTime.parse(timeString.split("/")[0]).toOffsetDateTime();
	}

}