package com.gmh.enums;

/**
 * @author GMH
 * @title: CacheEnum
 * @projectName Self
 * @description:
 * @date 2019/11/70:36
 */
public enum CacheEnum {

    DICTIONARY_AREA("区域字典编码", "AREA"),

    CHANCE_TYPE("商机状态", "CHANCE_TYPE"),

    DEGREE("学历", "DEGREE"),

    CERTIFICATE_TYPE("证件类型", "CERTIFICATE_TYPE"),

    NATIONAL("民族", "NATIONAL"),

    KINSHIP("亲属关系", "KINSHIP"),

    BANK_TYPE("银行类型", "BANK_TYPE"),

    VACATION_TYPE("节日类型", "VACATION_TYPE"),

    LOGIN_USER_JSON("登陆人", "LOGIN_USER_JSON");

    private String code;
    private String name;

    CacheEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
