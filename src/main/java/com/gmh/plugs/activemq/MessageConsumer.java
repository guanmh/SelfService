package com.gmh.plugs.activemq;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author GMH
 * @title: MessageConsumer
 * @projectName Self
 * @description: 消费者
 * @date 2019/12/28 20:38
 */
@Slf4j
public class MessageConsumer extends MessageUtil implements Runnable{

    private String name;
    private javax.jms.MessageConsumer consumer;
    private Connection connection = null;
    private Session session;

    public MessageConsumer(String name){
        this.name = name;
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(MessageUtil.ACTIVEMQ_URL);
            connection = connectionFactory.createConnection();
            connection.start();
            //设置第一个参数为true，事务类型数据（应该可以进一步确保不重复消费）
            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            //设置每次处理的最大消息数为1
            Destination destination = session.createQueue(MessageUtil.QUEUE_NAME + "?consumer.prefetchSize=1");
            consumer = session.createConsumer(destination);
        } catch (JMSException e) {
            e.printStackTrace();
            log.info("创建消费者失败!");
        }
    }

    /**
     * @param
     * @description: 接收消息
     * @return
     * @author GMH
     * @date 2019/12/28 21:26
     */
    public String receiveMessage(){
        String msg = "";
        //创建消费者
        try {
            //创建消费的监听
            consumer.setMessageListener(message -> {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("消费者"+this.name+"获取消息：" + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
            msg = "消费者获取消息失败";
        }
        return msg;
    }

    @Override
    public void run() {
        while(true){
            this.receiveMessage();
            //线程随机等待0-10s的时间
            long waitTime = (long)(Math.random()*1000);
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
