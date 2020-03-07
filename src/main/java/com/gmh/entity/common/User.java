package com.gmh.entity.common;

import com.gmh.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
　* @description:系统用户类
　* @param
　* @return
　* @author GMH
　* @date 2019/11/6 23:50
　*/
@Data
@ApiModel(value = "用户实体")
public class User extends BaseEntity {

    @ApiModelProperty(value = "电子邮件")
    private String email;

    @ApiModelProperty(value = "登陆账户")
    private String loginName;

    @ApiModelProperty(value = "登陆密码")
    private String loginPassword;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "角色")
    @ManyToMany
    @JoinTable(
            name = "sys_user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    public User() {
    }

    public User(String loginName) {
        this.loginName = loginName;
    }

    public User(Long userId) {
        setId(userId);
    }

    public void addRole(Role role) {
        if (Objects.isNull(getRoles())) {
            setRoles(new ArrayList<>());
        }
        getRoles().add(role);
    }
}
