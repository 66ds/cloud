package com.qianbing.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 重试耗尽后，将失败消息投递到指定的交换机
 */
@Configuration
@ConditionalOnProperty(name="spring.rabbitmq.enabled",value = "true")
public class ErrorMessageConfig {

    @Bean
    public DirectExchange errorMessageExchange() {
        return new DirectExchange("error.message.exchange");
    }

    @Bean
    public Queue errorMessageQueue() {
        return new Queue("error.message.queue");
    }

    @Bean
    public Binding errorMessageBinding(Queue errorMessageQueue, DirectExchange errorMessageExchange) {
        return BindingBuilder.bind(errorMessageQueue).to(errorMessageExchange).with("error.message.routing.key");
    }

    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(rabbitTemplate, "error.message.exchange", "error.message.routing.key");
    }
}
