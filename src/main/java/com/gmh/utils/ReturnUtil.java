package com.gmh.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.gmh.enums.ReturnCodeEnum;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * @ClassName ReturnUtil
 * @Description 返回数据工具类
 * @Author 18380
 * @Date 2019/10/29 23:43
 * @Version 1.0
 **/
public class ReturnUtil {

    private static PropertyFilter filter;
    private static SerializeConfig serializeConfig;

    public static String error() {
        return toJSON(ReturnCodeEnum.ERROR, ReturnCodeEnum.ERROR.getMsg(), null);
    }

    public static String error(ReturnCodeEnum returnCodeEnum) {
        return toJSON(returnCodeEnum, returnCodeEnum.getMsg(), null);
    }

    public static String error(String message) {
        return toJSON(ReturnCodeEnum.DATA_ERROR, message, null);
    }

    public static String error(ReturnCodeEnum returnCodeEnum,String message) {
        return toJSON(returnCodeEnum, message, null);
    }

    private static PropertyFilter getPropertyFilter() {
        if (Objects.isNull(filter)) {
            filter =
                    (source, name, value) ->
                            !StringUtils.equals(name, "cancel") && !StringUtils.equals(name,
                                    "createTime");
        }
        return filter;
    }

    private static SerializeConfig getSerializeConfig() {
        if (Objects.isNull(serializeConfig)) {
            serializeConfig = new SerializeConfig();
            serializeConfig.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd"));
            serializeConfig.put(Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd " +
                    "HH:mm:ss"));
        }
        return serializeConfig;
    }

    public static String success() {
        return toJSON(ReturnCodeEnum.SUCCESS, ReturnCodeEnum.SUCCESS.getMsg(), null);
    }

    public static String success(ReturnCodeEnum returnCodeEnum) {
        return toJSON(returnCodeEnum, returnCodeEnum.getMsg(), null);
    }

    public static String success(ReturnCodeEnum returnCodeEnum,String message) {
        return toJSON(returnCodeEnum, message, null);
    }

    public static String success(Object data) {
        return toJSON(ReturnCodeEnum.SUCCESS, ReturnCodeEnum.SUCCESS.getMsg(), data);
    }

    /**
     * 成功返回
     *
     * @param message 返回的信息
     * @param data    返回的数据
     * @return
     */
    public static String success(String message, Object data) {
        return toJSON(ReturnCodeEnum.SUCCESS, message, data);
    }

    public static String toJSON(ReturnCodeEnum returnCodeEnum, String message, Object data) {

        StringBuilder json = new StringBuilder("{\"code\":" + returnCodeEnum.getCode());
        if (StringUtils.isNoneEmpty(message)) {
            json.append(",\"msg\":\"" + message + "\"");
        }
        if (data != null) {
            json.append(
                    ",\"data\":"
                            + JSON.toJSONString(
                            data,
                            getSerializeConfig(),
                            SerializerFeature.WriteNullBooleanAsFalse,
                            SerializerFeature.WriteNullStringAsEmpty));
        }
        return json.toString() + "}";
    }
}
