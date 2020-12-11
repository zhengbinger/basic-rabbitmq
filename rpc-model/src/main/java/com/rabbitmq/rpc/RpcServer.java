package com.rabbitmq.rpc;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author zhengbing
 * @date 2020/12/10
 * @email mydreambing@126.com
 */
public class RpcServer {

    private static final String QUEUE_RPC = "queue_rpc";

    public static void execute() throws IOException, TimeoutException {
        // 创建 ConnectionFactory 实例，并配置 Broker
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5762);
        factory.setUsername("guest");
        factory.setPassword("guest");

        // 通过连接工厂获得 Connection
        Connection connection = null;
        try {
            connection = factory.newConnection();

            // 通过 Connection 创建 连接通道  Channel
            final Channel channel = connection.createChannel();

            // 声明队列
            channel.queueDeclare(QUEUE_RPC, false, false, false, null);

            System.out.println("等待RPC 请求中...");
            final Consumer consumer = new DefaultConsumer(channel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    AMQP.BasicProperties prop = new AMQP.BasicProperties()
                            .builder()
                            .correlationId(properties.getCorrelationId())
                            .build();
                    // 生成返回
                    String response = generateResponse(body);
                    // 回复消息
                    channel.basicPublish("", properties.getReplyTo(), prop, response.getBytes(StandardCharsets.UTF_8));

                    // 对消息进行应答
                    channel.basicAck(envelope.getDeliveryTag(), false);
                    // 唤醒正在消费的所有线程
                    synchronized (this) {
                        this.notify();
                    }
                }
            };


            // 消费消息
            channel.basicConsume(QUEUE_RPC, false, consumer);
            // 在收到消息前，本线程进入等待状态
            while (true) {
                synchronized (consumer) {
                    try {
                        consumer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != connection) {
                connection.close();
            }
        }
    }

    public static String generateResponse(byte[] body) {
        System.out.println("[X] RPC Server 接收到的请求：" + new String(body));
        try {
            Thread.sleep(1000 * 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "响应结果：" + new String(body) + "-" + System.currentTimeMillis();
    }
}
