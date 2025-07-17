package com.qianbing.rabbitmq.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class articleListener {


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "article.delay.queue", durable = "true"),
            exchange = @Exchange(name = "article.delay.direct", delayed = "true"),
            key = "article.delay.key"
    ))
   public void articleDelayQuque(Message message, Channel channel) throws IOException {
        try{
            //TODO 需要优化
            String json = new String(message.getBody(), StandardCharsets.UTF_8);
            Map<String, Object> map = new ObjectMapper().readValue(json, Map.class);
            Long articleId = ((Number) map.get("articleId")).longValue();
            //TODO 远程更新状态为已发布
            articleService.markAsPublished(articleId);
            //收到ack
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),  false);
        }catch (Exception e){
            // 消费失败，nack 或 reject
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
}
