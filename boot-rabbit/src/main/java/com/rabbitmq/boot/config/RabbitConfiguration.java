package com.rabbitmq.boot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMq 配置类
 *
 * @author zhengbing
 * @date 2020/12/11
 * @email mydreambing@126.com
 */
@Configuration
public class RabbitConfiguration {

    public static final String EXCHANGE_TOPIC_INFO = "exchange_topic_info";
    public static final String QUEUE_SMS = "queue_sms";
    public static final String QUEUE_EMAIL = "queue_email";

    /**
     * 配置交换机
     *
     * @return Exchange
     */
    @Bean(EXCHANGE_TOPIC_INFO)
    public Exchange exchangeTopicInfo() {
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPIC_INFO).durable(true).build();
    }

    /**
     * 声明 短信队列
     *
     * @return Queue
     */
    @Bean(QUEUE_SMS)
    public Queue smsQueue() {
        return new Queue(QUEUE_SMS);
    }

    /**
     * 声明 邮件队列
     *
     * @return Queue
     */
    @Bean(QUEUE_EMAIL)
    public Queue emailQueue() {
        return new Queue(QUEUE_EMAIL);
    }

    @Bean
    public Binding bindQueueSms(@Qualifier(QUEUE_SMS) Queue queue, @Qualifier(EXCHANGE_TOPIC_INFO) Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("info.#.sms.#")
                .noargs();
    }

    @Bean
    public Binding bindQueueEmail(@Qualifier(QUEUE_EMAIL) Queue queue, @Qualifier(EXCHANGE_TOPIC_INFO) Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("info.#.email.#")
                .noargs();
    }

}
