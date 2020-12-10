package com.rabbitmq.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author zhengbing
 * @date 2020/12/9
 * @email mydreambing@126.com
 */
public class Producer {

    public static final String QUEUE_HELLO = "helloworld";

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
        try (Connection connection = factory.newConnection();

             // 创建通道实例
             Channel channel = connection.createChannel()) {
            // 声明队列
            channel.queueDeclare(QUEUE_HELLO, false, false, false, null);

            String message = "hello world!!!";
            channel.basicPublish("", QUEUE_HELLO, null, message.getBytes(UTF_8));
            System.out.println("[X] 发送消息：" + message);
        }

    }
}
