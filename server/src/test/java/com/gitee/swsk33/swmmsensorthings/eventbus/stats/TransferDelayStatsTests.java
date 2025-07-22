package com.gitee.swsk33.swmmsensorthings.eventbus.stats;

import com.gitee.swsk33.swmmsensorthings.eventbus.service.HydrologicalJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 用于比较HTTP和MQTT传输方式延迟的统计测试类型
 */
@SpringBootTest
public class TransferDelayStatsTests {

	@Autowired
	private HydrologicalJobService hydrologicalJobService;

}