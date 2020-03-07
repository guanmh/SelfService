package com.gmh.plugs.rabbitmq.work;

import com.gmh.plugs.rabbitmq.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GMH
 * @title: Producer
 * @projectName Self
 * @description: 生产者(work模式)
 * @date 2020/3/3 23:28
 */
@Slf4j
public class Producer {

    //定义队列名称
    private final static String QUEUE_NAME = "test_msg";

    public static void main(String[] args) throws Exception {
        //获取链接
        Connection connection = ConnectionUtil.getConnection();
        //从链接中创建通道
        Channel channel = connection.createChannel();
        /**
         * 声明队列
         * 参数1(queue):队列名称
         * 参数2(durable):是否持久化, 队列的声明默认是存放到内存中的，如果rabbitmq重启会丢失，如果想重启之后还存在就要使队列持久化，
         * 保存到Erlang自带的Mnesia数据库中，当rabbitmq重启之后会读取该数据库
         * 参数3(exclusive):排他队列
         * 如果一个队列被声明为排他队列，该队列仅对首次声明它的连接可见，并在连接断开时自动删除。这里需要注意三点：
         * 其一，排他队列是基于连接可见的，同一连接的不同信道是可以同时访问同一个连接创建的排他队列的。
         * 其二，“首次”，如果一个连接已经声明了一个排他队列，其他连接是不允许建立同名的排他队列的，这个与普通队列不同。
         * 其三，即使该队列是持久化的，一旦连接关闭或者客户端退出，该排他队列都会被自动删除的。
         * 这种队列适用于只限于一个客户端发送读取消息的应用场景。
         * 参数4(autoDelete):自动删除，true为自动删除，删除的前提是至少有一个消费者连接这个队列，之后所有与这个队列连接的消费者都断开时都会自动删除（并不是当连接此队列的所有客户端都断开时自动删除）
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        /**
         * exchange：交换器名称，如果设置为空字符串，则消息会被发送到RabbitMQ默认的交换器中。
         * routingKey：指定路由键，交换器根据路由键将消息存储到相应的队列之中
         * BasicProperties：消息的基本属性，例如路由头等
         * body：msg字节
         */
        for (int i = 0; i < 100; i++){
            //消息内容
            String msg = "hello rabbitmq"+i;
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
            log.info("发送消息: " + msg);
        }
        //关闭通道和链接
        channel.close();
        connection.close();
    }
}
