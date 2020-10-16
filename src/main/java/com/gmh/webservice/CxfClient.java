package com.gmh.webservice;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;


/**
 * Created by sky on 2018/2/27.
 */
public class CxfClient {
    //webservice接口地址
    private static String address = "http://localhost:8080/self/services/webservice/test?wsdl";

    //测试
    public static void main(String[] args) {
//        test1();
        test2();
    }

    /**
     * 方式1:使用代理类工厂,需要拿到对方的接口
     */
//    public static void test1() {
//        try {
//            // 代理工厂
//            JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
//            // 设置代理地址
//            jaxWsProxyFactoryBean.setAddress(address);
//            //添加用户名密码拦截器
//            jaxWsProxyFactoryBean.getOutInterceptors().add(new LoginInterceptor("root","admin"));;
//            // 设置接口类型
//            jaxWsProxyFactoryBean.setServiceClass(AppService.class);
//            // 创建一个代理接口实现
//            AppService cs = (AppService) jaxWsProxyFactoryBean.create();
//            // 数据准备
//            String LineId = "1";
//            // 调用代理接口的方法调用并返回结果
//            User result = (User)cs.getUser(LineId);
//            System.out.println("==============返回结果:" + result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 动态调用方式
     */
    public static void test2() {
        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(address);
        // 需要密码的情况需要加上用户名和密码
         client.getOutInterceptors().add(new LoginInterceptor("root","admin"));
        Object[] objects = new Object[0];
        try {
            // invoke("方法名",参数1,参数2,参数3....);
            System.out.println("======client"+client);
            objects = client.invoke("getUserName", "gmh");
            System.out.println("返回数据:" + objects[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
