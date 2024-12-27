package com.gitee.swsk33.swmmsensorthings.eventbus.subscriber.mqtt;

import com.gitee.swsk33.swmmsensorthings.eventbus.service.SWMMSimulationService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RainGageSubscriber implements IMqttMessageListener {

	@Autowired
	private SWMMSimulationService SWMMSimulationService;

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {

	}

}