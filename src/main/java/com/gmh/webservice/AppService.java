package com.gmh.webservice;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.io.UnsupportedEncodingException;

@WebService
public interface AppService {


    @WebMethod
    String getUserName(@WebParam(name = "id") String id) throws UnsupportedEncodingException;
    String getUserName2(String id) throws UnsupportedEncodingException;
    @WebMethod
    String getUser(String id) throws UnsupportedEncodingException;
}