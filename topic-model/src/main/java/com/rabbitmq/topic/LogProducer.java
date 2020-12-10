package com.rabbitmq.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author zhengbing
 * @date 2020/12/10
 * @email mydreambing@126.com
 */
public class LogProducer {

    public static final String EXCHANGE_LOG = "exchange_log_topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂实例
        ConnectionFactory factory = new ConnectionFactory();
        // 配置连接信息
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("guest");

        factory.setPassword("guest");
        factory.setHandshakeTimeout(9999999);
        // 创建连接实例
        Connection connection = factory.newConnection();

        // 创建通道实例
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_LOG, BuiltinExchangeType.TOPIC);
        for (int i = 0; i < 5; i++) {
            String message = "error message";
            channel.basicPublish(EXCHANGE_LOG, "log.rabbit.error", null, message.getBytes(UTF_8));
            System.out.println("[X] 发送消息：" + message);
        }

        for (int i = 0; i < 5; i++) {
            String message = "info message";
            channel.basicPublish(EXCHANGE_LOG, "log.rabbit.info", null, message.getBytes(UTF_8));
            System.out.println("[X] 发送消息：" + message);
        }

        for (int i = 0; i < 5; i++) {
            String message = "warning message";
            channel.basicPublish(EXCHANGE_LOG, "log.rabbit.warning", null, message.getBytes(UTF_8));
            System.out.println("[X] 发送消息：" + message);
        }

        for (int i = 0; i < 5; i++) {
            String message = "all message";
            channel.basicPublish(EXCHANGE_LOG, "log.rabbit.warning.info.error", null, message.getBytes(UTF_8));
            System.out.println("[X] 发送消息：" + message);
        }

        channel.close();
        connection.close();


    }
}
