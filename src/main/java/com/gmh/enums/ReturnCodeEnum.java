package com.gmh.enums;

/**
 * @ClassName ReturnUtil
 * @Description 返回数据编码工具类
 * @Author 18380
 * @Date 2019/10/29 23:43
 * @Version 1.0
 **/
public enum ReturnCodeEnum {
    //返回code类型

    SUCCESS(200,"成功"),
    LOGIN_SUCCESS(201,"登陆成功"),

    ERROR(-1,"系统错误"),
    LOGIN_ERROR(100,"用户名或秘密错误"),
    REPEATED_SUBMIT(101,"请勿重复提交"),
    DATA_ERROR(102,"数据异常"),
    NO_PERMISSION(103,"没有权限");

    private int code;
    private String msg;

    ReturnCodeEnum(int code,String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
