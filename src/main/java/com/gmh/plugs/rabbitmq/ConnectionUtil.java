package com.gmh.plugs.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author GMH
 * @title: ConnectionUtil
 * @projectName Self
 * @description: rabbitmq 连接类
 * @date 2020/3/3 23:01
 */
public class ConnectionUtil {
    public static Connection getConnection() throws Exception{
        //定义链接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务地址
        factory.setHost("localhost");
        //设置端口
        factory.setPort(5672);
        //设置用户名密码vhost:testhost(自己建的权限分组)
        factory.setVirtualHost("testhost");
        factory.setUsername("admin");
        factory.setPassword("admin");
        //通过工程获取链接
        Connection connection = factory.newConnection();
        return connection;
    }
}
