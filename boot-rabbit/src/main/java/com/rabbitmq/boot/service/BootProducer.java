package com.rabbitmq.boot.service;

import com.rabbitmq.boot.config.RabbitConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhengbing
 * @date 2020/12/11
 * @email mydreambing@126.com
 */
@Component
public class BootProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send2Topic() {

        for (int i = 0; i < 5; i++) {
            String message = "springboot so cool to send message :" + i;
            rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE_TOPIC_INFO, "info.sms.email", message);
        }
    }

}
