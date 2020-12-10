package com.rabbitmq.pubsub;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author zhengbing
 * @date 2020/12/10
 * @email mydreambing@126.com
 */
public class Producer {

    private static final String EXCHANGE_NAME = "exchange_pubsub";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setHandshakeTimeout(99999);

            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

            for (int i = 0; i < 5; i++) {
                String message = "pubsub message test";
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("[X] send message:" + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != channel) {
                channel.close();
            }
            if (null != connection) {
                connection.close();
            }
        }
    }
}
