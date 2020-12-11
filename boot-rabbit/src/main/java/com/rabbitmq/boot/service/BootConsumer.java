package com.rabbitmq.boot.service;

import com.rabbitmq.boot.config.RabbitConfiguration;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zhengbing
 * @date 2020/12/11
 * @email mydreambing@126.com
 */
@Component
public class BootConsumer {

    @RabbitListener(queues = {RabbitConfiguration.QUEUE_SMS})
    public void receiveSms(String msg, Message message, Channel channel) {
        System.out.println("[X] RECV SMS MSG:" + msg);
    }

    @RabbitListener(queues = {RabbitConfiguration.QUEUE_EMAIL})
    public void receiveEmail(String msg, Message message, Channel channel) {
        System.out.println("[X] RECV EMAIL MSG:" + msg);
    }
}
