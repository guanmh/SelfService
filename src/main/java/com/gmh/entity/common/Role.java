package com.gmh.entity.common;

import com.gmh.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
　* @description: 角色实体
　* @param
　* @return
　* @author GMH
　* @date 2019/11/6 23:50
　*/
@Data
@ApiModel(value = "角色实体")
public class Role extends BaseEntity {
    @ApiModelProperty(value = "角色功能权限")
    @ManyToMany
    @JoinTable(
            name = "sys_role_function",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "function_id", referencedColumnName = "id")})
    private List<Function> functions;

    @ApiModelProperty(value = "角色菜单")
    @ManyToMany
    @JoinTable(
            name = "sys_role_menu",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")})
    private List<Menu> menus;

    @NotBlank(message = "角色名称不能为空")
    @ApiModelProperty(value = "角色名称")
    private String name;
    @ManyToOne
    @JoinColumn(name = "organization_id", columnDefinition = "bigint(20) COMMENT '角色所属组织ID'")
    private Organization organization;

    @NotBlank(message = "权限编码不能为空")
    @ApiModelProperty(value = "角色权限编码")
    private String power;

    @ApiModelProperty(value = "用户")
    @ManyToMany
    @JoinTable(
            name = "sys_user_role",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private List<User> users;

    public Role() {
    }

    public Role(Long id) {
        setId(id);
    }

    public Role(String power) {
        this.power = power;
    }
}
