package com.gmh.plugs.activemq;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

/**
 * @author GMH
 * @title: test
 * @projectName Self
 * @description:
 * @date 2019/12/28 21:09
 */
public class test {

    @Ignore
    @Test
    public void test1(){
        MessageProducer producer = new MessageProducer();
        producer.sendMessage();
        //生产者发送消息
        MessageConsumer consumerA = new MessageConsumer("A");
        MessageConsumer consumerB = new MessageConsumer("B");
//        consumerA.receiveMessage();
//        consumerB.receiveMessage();
//        new Thread(consumerA).start();
//        new Thread(consumerB).start();
    }
}
