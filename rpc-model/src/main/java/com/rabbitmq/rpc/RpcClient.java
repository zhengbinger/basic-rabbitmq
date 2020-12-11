package com.rabbitmq.rpc;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * @author zhengbing
 * @date 2020/12/10
 * @email mydreambing@126.com
 */
public class RpcClient {

    /**
     * 计算斐波列其数列的第n项
     *
     * @param n
     * @return
     * @throws Exception
     */
    private static int fib(int n) throws Exception {
        if (n < 0) {
            throw new Exception("参数错误，n必须大于等于0");
        }
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return fib(n - 1) + fib(n - 2);
    }

    private static final String QUEUE_RPC = "queue_rpc";

    public static void main(String[] args) {
        String message = "china is good";
        final String correlation_id = UUID.randomUUID().toString();

        // 创建 ConnectionFactory 实例，并配置 Broker
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5762);
        factory.setUsername("guest");
        factory.setPassword("guest");

        // 通过连接工厂获得 Connection
        Connection connection = null;
        Channel channel = null;
        try {


            connection = factory.newConnection();

            // 通过 Connection 创建 连接通道  Channel

            channel = connection.createChannel();

            // 创建临时队列，并获得队列名称
            String tempQueueName = channel.queueDeclare().getQueue();

            // 声明队列
            channel.queueDeclare(QUEUE_RPC, false, false, false, null);

            // 生成发送消息的属性
            AMQP.BasicProperties properties = new AMQP.BasicProperties()
                    .builder()
                    .correlationId(correlation_id)
                    // 设置指定回调队列
                    .replyTo(tempQueueName)
                    .build();

            // 设置发送消息的队列
            channel.basicPublish("", QUEUE_RPC, properties, message.getBytes(StandardCharsets.UTF_8));

            // 阻塞队列用于存放回调结果
            final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

            // 定义消息的回退方法
            channel.basicConsume(tempQueueName, true, new DefaultConsumer(channel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    if (properties.getCorrelationId().equals(correlation_id)) {
                        response.offer(new String(body, StandardCharsets.UTF_8));
                    }
                }
            });

            // 获取回调结果
            String result = response.take();
            System.out.println("[X] RPC 回调结果：" + result);
            System.out.println("[X] 发送消息" + message);

            System.out.println(fib(10));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != channel) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (null != connection) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
