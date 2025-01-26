package com.gitee.swsk33.swmmsensorthings.eventbus.serializer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 自定义LocalDateTime的序列化器
 */
@Component
public class LocalDateTimeWriter implements ObjectWriter<LocalDateTime> {

	@Override
	public void write(JSONWriter jsonWriter, Object data, Object fieldName, Type type, long feature) {
		if (data == null) {
			jsonWriter.writeNull();
			return;
		}
		// 转换为实际对象
		LocalDateTime time = (LocalDateTime) data;
		// 写入JSON
		jsonWriter.writeString(time.atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT));
	}

}