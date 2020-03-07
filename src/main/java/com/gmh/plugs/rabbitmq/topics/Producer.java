package com.gmh.plugs.rabbitmq.topics;

import com.gmh.plugs.rabbitmq.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GMH
 * @title: Producer
 * @projectName Self
 * @description: 生产者(路由模式)
 * @date 2020/3/3 23:28
 */
@Slf4j
public class Producer {

    //定义交换机名称
    private final static String EXCHANGE_NAME = "test_exchange_topic";

    public static void main(String[] args) throws Exception {
        //获取链接
        Connection connection = ConnectionUtil.getConnection();
        //从链接中创建通道
        Channel channel = connection.createChannel();
        /**
         * 声明exchange交换机
         */
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        //消息内容
        String infoMsg = "hello topic";
        /**
         * exchange：交换机名称，如果设置为空字符串，则消息会被发送到RabbitMQ默认的交换器中。
         * routingKey：指定路由键，交换器根据路由键将消息存储到相应的队列之中
         * BasicProperties：消息的基本属性，例如路由头等
         * body：msg字节
         */
        channel.basicPublish(EXCHANGE_NAME,"2.topic.1",null,infoMsg.getBytes());
        log.info("发送消息: " + infoMsg);
        //关闭通道和链接
        channel.close();
        connection.close();
    }
}
