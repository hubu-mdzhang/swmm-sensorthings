package com.gitee.swsk33.swmmsensorthings.server.client;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.model.SensorThingsObject;
import com.gitee.swsk33.swmmsensorthings.server.util.RequestClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gitee.swsk33.swmmsensorthings.server.param.HttpStatusCode.CREATED;
import static com.gitee.swsk33.swmmsensorthings.server.param.HttpStatusCode.OK;
import static com.gitee.swsk33.swmmsensorthings.server.param.SensorThingsPrefix.getTypePrefix;

/**
 * 通用SensorThings API请求客户端
 */
@Slf4j
@Component
public class SensorThingsObjectClient {

	@Autowired
	private RequestClient requestClient;

	/**
	 * 添加一个SensorThings对象
	 *
	 * @param object 添加的SensorThings对象
	 * @return 是否添加成功
	 */
	public <T extends SensorThingsObject> boolean add(T object) {
		try (Response response = requestClient.post(getTypePrefix(object.getClass()), JSON.toJSONBytes(object))) {
			if (response.code() == CREATED) {
				return true;
			}
			if (response.body() != null) {
				log.error("添加{}失败！状态码：{}，响应消息：{}", object.getClass().getSimpleName(), response.code(), response.body().string());
			}
		} catch (Exception e) {
			log.error("增加{}时出现错误！", object.getClass().getSimpleName());
			log.error(e.getMessage());
		}

		return false;
	}

	/**
	 * 移除一个SensorThings对象
	 *
	 * @param id   SensorThings对象的主键id
	 * @param type 要移除的SensorThings对象具体类型
	 * @return 是否移除成功
	 */
	public <T extends SensorThingsObject> boolean remove(Object id, Class<T> type) {
		try (Response response = requestClient.delete(String.format("%s(%s)", getTypePrefix(type), id))) {
			if (response.code() == OK) {
				return true;
			}
			if (response.body() != null) {
				log.error("移除{}失败！状态码：{}，响应消息：{}", type.getSimpleName(), response.code(), response.body().string());
			}
		} catch (Exception e) {
			log.error("移除{}时出现错误！", type.getSimpleName());
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 根据id，获取一个SensorThings对象
	 *
	 * @param id      要获取的SensorThings对象的id
	 * @param type    要获取的SensorThings对象具体类型
	 * @param expands 要展开的对象属性，不展开任何属性传入null
	 * @return 获取的SensorThings对象，不存在返回null
	 */
	public <T extends SensorThingsObject> T getById(Object id, Class<T> type, String[] expands) {
		String path = String.format("%s(%s)", getTypePrefix(type), id);
		if (expands != null && expands.length > 0) {
			path += "?$expand=" + String.join(",", expands);
		}
		try (Response response = requestClient.get(path)) {
			if (response.code() == OK) {
				ResponseBody body = response.body();
				if (body != null) {
					return JSON.parseObject(body.bytes(), type);
				}
			}
			if (response.body() != null) {
				log.error("获取{}失败！状态码：{}，响应消息：{}", type.getSimpleName(), response.code(), response.body().string());
			}
		} catch (Exception e) {
			log.error("根据id获取{}时出现错误！", type.getSimpleName());
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 根据名称，获取一个SensorThings对象
	 *
	 * @param name    要获取的对象名称
	 * @param type    要获取的对象类型
	 * @param expands 要展开的对象属性，不展开任何属性传入null
	 * @return 对应的对象，不存在返回null
	 */
	public <T extends SensorThingsObject> T getByName(String name, Class<T> type, String[] expands) {
		String path = String.format("%s?$filter=name eq '%s'", getTypePrefix(type), name);
		if (expands != null && expands.length > 0) {
			path += "&$expand=" + String.join(",", expands);
		}
		try (Response response = requestClient.get(path)) {
			if (response.code() == OK) {
				ResponseBody body = response.body();
				if (body != null) {
					JSONArray resultArray = JSON.parseObject(body.bytes()).getJSONArray("value");
					if (!resultArray.isEmpty()) {
						return resultArray.getJSONObject(0).toJavaObject(type);
					}
				}
			}
			if (response.body() != null && response.code() != OK) {
				log.error("根据名称查询{}失败！状态码：{}，响应消息：{}", type.getSimpleName(), response.code(), response.body().string());
			}
		} catch (Exception e) {
			log.error("根据名称查询{}时出现错误！", type.getSimpleName());
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 获取全部SensorThings对象
	 *
	 * @param type 要获取的SensorThings对象具体类型
	 * @return 获取的SensorThings对象列表，不存在返回空列表
	 */
	public <T extends SensorThingsObject> List<T> getAll(Class<T> type) {
		try (Response response = requestClient.get(getTypePrefix(type))) {
			if (response.code() == OK) {
				ResponseBody body = response.body();
				if (body != null) {
					JSONObject resultObject = JSON.parseObject(body.bytes());
					return resultObject.getJSONArray("value").toJavaList(type);
				}
			}
			if (response.body() != null) {
				log.error("获取全部{}失败！状态码：{}，响应消息：{}", type.getSimpleName(), response.code(), response.body().string());
			}
		} catch (Exception e) {
			log.error("获取全部{}时出现错误！", type.getSimpleName());
			log.error(e.getMessage());
		}
		return List.of();
	}

	/**
	 * 根据SensorThings的名称，判断是否存在
	 *
	 * @param name 传入要判断的名称
	 * @param type 要查询的SensorThings对象具体类型
	 * @return 是否存在
	 */
	public <T extends SensorThingsObject> boolean existByName(String name, Class<T> type) {
		return getByName(name, type, null) != null;
	}

}