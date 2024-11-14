package com.gitee.swsk33.swmmsensorthings.server.client;

import com.alibaba.fastjson2.JSON;
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
 * 通用抽象SensorThings API请求客户端
 *
 * @param <T> 该客户端操作的SensorThings对象具体类型
 */
@Slf4j
@Component
public abstract class BaseSensorThingsClient<T extends SensorThingsObject> {

	@Autowired
	private RequestClient requestClient;

	/**
	 * 添加一个SensorThings对象
	 *
	 * @param object 添加的SensorThings对象
	 * @return 是否添加成功
	 */
	protected boolean add(T object) {
		try (Response response = requestClient.post(getTypePrefix(object.getClass()), JSON.toJSONBytes(object))) {
			if (response.code() == CREATED) {
				return true;
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
	protected boolean remove(Object id, Class<T> type) {
		try (Response response = requestClient.delete(String.format("%s(%s)", getTypePrefix(type), id))) {
			if (response.code() == OK) {
				return true;
			}
		} catch (Exception e) {
			log.error("移除{}时出现错误！", type.getSimpleName());
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 获取一个SensorThings对象
	 *
	 * @param id   要获取的SensorThings对象的id
	 * @param type 要获取的SensorThings对象具体类型
	 * @return 获取的SensorThings对象，不存在返回null
	 */
	protected T get(Object id, Class<T> type) {
		try (Response response = requestClient.get(String.format("%s(%s)", getTypePrefix(type), id))) {
			if (response.code() == OK) {
				ResponseBody body = response.body();
				if (body != null) {
					return JSON.parseObject(body.bytes(), type);
				}
			}
		} catch (Exception e) {
			log.error("获取{}时出现错误！", type.getSimpleName());
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
	protected List<T> getAll(Class<T> type) {
		try (Response response = requestClient.get(getTypePrefix(type))) {
			if (response.code() == OK) {
				ResponseBody body = response.body();
				if (body != null) {
					JSONObject resultObject = JSON.parseObject(body.bytes());
					return JSON.parseArray(resultObject.getJSONArray("value").toJSONBBytes(), type);
				}
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
	protected boolean existByName(String name, Class<T> type) {
		try (Response response = requestClient.get(String.format("%s?$filter=name eq '%s'", getTypePrefix(type), name))) {
			if (response.code() == OK) {
				ResponseBody body = response.body();
				if (body != null) {
					JSONObject resultObject = JSON.parseObject(body.bytes());
					if (!resultObject.getJSONArray("value").isEmpty()) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			log.error("查询{}存在时出现错误！", type.getSimpleName());
			log.error(e.getMessage());
		}
		return false;
	}

}