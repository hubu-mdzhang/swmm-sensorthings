package com.gitee.swsk33.swmmsensorthings.server.serializer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 自定义LocalDateTime序列化
 */
@Component
public class LocalDateTimeWriter implements ObjectWriter<LocalDateTime> {

	@Override
	public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
		if (object == null) {
			jsonWriter.writeNull();
			return;
		}
		LocalDateTime localDateTime = (LocalDateTime) object;
		DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		String formattedDate = localDateTime.atOffset(ZoneOffset.ofHours(8)).format(formatter);
		jsonWriter.writeString(formattedDate);
	}

}