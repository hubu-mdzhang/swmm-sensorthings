package com.gitee.swsk33.swmmsensorthings.eventbus.model.process.data;

import com.gitee.swsk33.swmmsensorthings.eventbus.annotation.AllowedValue;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.constant.CRSLink;
import lombok.Data;

/**
 * 表示一个四至范围的矩形框对象
 */
@Data
public class BoundBox {

	/**
	 * 四至范围经纬度坐标，通常是4个坐标，分别是：最小经度、最小纬度、最大经度、最大纬度
	 */
	private double[] bbox;

	/**
	 * 坐标系规范，必须是{@link CRSLink}中的常量
	 */
	@AllowedValue({CRSLink.CRS84, CRSLink.CRS84_H})
	private String crs = CRSLink.CRS84;

}