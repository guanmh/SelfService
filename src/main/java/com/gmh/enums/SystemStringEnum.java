package com.gmh.enums;

import lombok.Getter;

/**
 * @ClassName SystemStringEnum
 * @Description
 * @Author 18380
 * @Date 2019/10/30 0:30
 * @Version 1.0
 **/
public class SystemStringEnum {

    public class RedisValueEnum {

        /**
         * 方法缓存
         */
        static final String METHOD_KEY = "Method:";
        /**
         * 登陆人信息
         */
        public static final String LOGIN_USER_VALUE = METHOD_KEY + "UserController:loginUser";
        /**
         * 所有菜单
         */
        public static final String ALL_MENU_VALUE = METHOD_KEY + "MenuController:getMenu";
        /**
         * 获取csrf
         */
        public static final String CSRF_VALUE = METHOD_KEY + "LoginController:getCsrf";
        /**
         * 获取角色编码对应人员
         */
        public static final String ROLE_USER_VALUE = METHOD_KEY + "UserController:selectUser";
    }
    /**
     * 1、全公司 2、当前公司 3、只能看数据归属人是自己的
     */
    @Getter
    public enum RolePowerEnum {
        ADMIN("系统管理员", "ADMIN", 1);

        private String code;
        private String name;
        private int power;

        RolePowerEnum(String name, String code, int power) {
            this.name = name;
            this.code = code;
            this.power = power;
        }
    }
}
