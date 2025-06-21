package com.qianbing.rabbitmq.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianbing.common.entity.UsersEntity;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SendEmailListener {

    @Autowired
    JavaMailSender javaMailSender;

    //TODO 常量
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "email.welcome.queue",durable = "true"),
            exchange = @Exchange(name = "email.exchange", type = ExchangeTypes.DIRECT),
            key = {"email.welcome"}
    ))
    public void emailWelComeQueue(Message message, Channel channel) throws IOException {
        try{
            // 1. 手动反序列化 UsersEntity
            byte[] body = message.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            UsersEntity usersEntity = objectMapper.readValue(body, UsersEntity.class);
            sendMessage(usersEntity);
            //收到ack
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),  false);
        }catch (Exception e){
            // 消费失败，nack 或 reject
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }

    }


    /**
     * 发送邮件
     * @param usersEntity
     */
    private void sendMessage(UsersEntity usersEntity){
        //建立邮件消息
        SimpleMailMessage mainMessage = new SimpleMailMessage();
        //发送者
        //TODO 常量
        mainMessage.setFrom("1532498760@qq.com");
        //接收者
        mainMessage.setTo(usersEntity.getUserEmail());
        //发送的标题
        //TODO 常量
        mainMessage.setSubject("欢迎QB博客系统");
        //发送的内容
        //TODO 常量
        mainMessage.setText("注册成功!");
        javaMailSender.send(mainMessage);
    }

}
