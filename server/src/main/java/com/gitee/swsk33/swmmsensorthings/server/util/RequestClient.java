package com.gitee.swsk33.swmmsensorthings.server.util;

import com.gitee.swsk33.swmmsensorthings.server.param.HttpMethod;
import com.gitee.swsk33.swmmsensorthings.server.property.SensorThingsServerProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 调用OkHttp发起请求的客户端实用类
 */
@Slf4j
@Component
public class RequestClient {

	/**
	 * Okhttp客户端对象
	 */
	private OkHttpClient client;

	/**
	 * 请求网址前缀
	 */
	private String urlPrefix;

	@Autowired
	private SensorThingsServerProperties properties;

	@PostConstruct
	private void initialize() {
		// 初始化一些属性
		client = new OkHttpClient();
		urlPrefix = String.format("http://%s:%d/v1.1", properties.getHost(), properties.getPort());
		log.info("HTTP请求客户端已完成初始化！");
	}

	/**
	 * 发送一个HTTP请求
	 *
	 * @param path   请求路径，以/开头
	 * @param method 请求方法类型，参考{@link HttpMethod}中常量枚举
	 * @param body   请求体，无请求体传入null
	 * @return 响应对象
	 */
	public Response sendRequest(String path, String method, byte[] body) throws IOException {
		// 检查path
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		// 构建请求体
		RequestBody requestBody = null;
		if (body != null) {
			requestBody = RequestBody.create(body);
		}
		// 构建请求
		Request request = new Request.Builder()
				.url(urlPrefix + path)
				.method(method, requestBody)
				.build();
		// 发起请求
		return client.newCall(request).execute();
	}

	/**
	 * 发起GET请求
	 *
	 * @param path 请求路径，以/开头
	 * @return 响应对象
	 */
	public Response get(String path) throws IOException {
		return sendRequest(path, HttpMethod.GET, null);
	}

	/**
	 * 发起POST请求
	 *
	 * @param path 请求路径，以/开头
	 * @param body 请求体
	 * @return 响应对象
	 */
	public Response post(String path, byte[] body) throws IOException {
		return sendRequest(path, HttpMethod.POST, body);
	}

	/**
	 * 发起PATCH请求
	 *
	 * @param path 请求路径，以/开头
	 * @param body 请求体
	 * @return 响应对象
	 */
	public Response patch(String path, byte[] body) throws IOException {
		return sendRequest(path, HttpMethod.PATCH, body);
	}

	/**
	 * 发起DELETE请求
	 *
	 * @param path 请求路径，以/开头
	 * @return 响应对象
	 */
	public Response delete(String path) throws IOException {
		return sendRequest(path, HttpMethod.DELETE, null);
	}

}