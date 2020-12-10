package com.rabbitmq.pubsub;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author zhengbing
 * @date 2020/12/10
 * @email mydreambing@126.com
 */
public class Consumer {

    private static final String QUEUE_NAME = "PUBSUB";
    private static final String EXCHANGE_NAME = "exchange_pubsub";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHandshakeTimeout(99999);
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

        DeliverCallback deliver =
                (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    System.out.println("[X] RECV MSG:" + message);
                };
        channel.basicConsume(QUEUE_NAME, deliver, comsumerTag -> {
        });
    }
}
