package com.gmh.webservice;

import javax.jws.WebService;
import java.io.UnsupportedEncodingException;

//name暴露的服务名称, targetNamespace:命名空间,设置为接口的包名倒写(默认是本类包名倒写). endpointInterface接口地址
@WebService(name = "test" ,targetNamespace ="http://webservice.gmh.com/" ,endpointInterface = "com.gmh.webservice.AppService")
public class AppServiceImpl implements AppService {


    @Override
    public String getUserName(String id) throws UnsupportedEncodingException {
        System.out.println("==========================="+id);
        return "明哥";
    }
    @Override
    public String getUserName2(String id) throws UnsupportedEncodingException {
        System.out.println("==========================="+id);
        return "明哥";
    }
    @Override
    public String getUser(String id)throws UnsupportedEncodingException  {
        System.out.println("==========================="+id);
        return "明哥";
    }

}