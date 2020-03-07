package com.gmh.plugs.activemq;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author GMH
 * @title: MessageProducer
 * @projectName Self
 * @description: 生产者-用于消息的创建类
 * @date 2019/12/28 20:16
 */
@Slf4j
public class MessageProducer extends MessageUtil{

    private Connection connection = null;
    private javax.jms.MessageProducer producer;
    private Session session;

    public MessageProducer(){
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(MessageUtil.ACTIVEMQ_URL);
        //创建从工厂连接中得到的对象
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            //false：参数表示为非事务类型；AUTO_ACKNOWLEDGE：消息自动确认
            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(MessageUtil.QUEUE_NAME);
            producer = session.createProducer(destination);
        } catch (JMSException e) {
            e.printStackTrace();
            log.info("生产者创建失败");
        }
    }

    /**
    　* @description:发送消息
    　* @param
    　* @return
    　* @author GMH
    　* @date 2019/12/28 20:20
    　*/
    public String sendMessage(){
        String msg = "";
        try {
            //创建模拟100个消息
            for (int i = 1; i <= 10; i++) {
                TextMessage message = session.createTextMessage("我发送的message: " + i);
                //发送消息
                producer.send(message);
                //打印消息
                System.out.println("我现在发的消息是：" + message.getText());
            }
            msg = "生产者发送消息成功";
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
            msg = "生产者发送消息失败";
        }
        return msg;
    }
}
