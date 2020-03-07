package com.gmh.plugs.activemq;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

/**
 * @author GMH
 * @title: MessageUtil
 * @projectName Self
 * @description: activemq工具类
 * @date 2019/12/28 20:49
 */
@Slf4j
public class MessageUtil {

    //定义ActiveMQ的链接地址
    public static final String ACTIVEMQ_URL = "tcp://127.0.0.1:61616";
    //定义发送消息的队列名称
    public static final String QUEUE_NAME = "MyMessage";
}
