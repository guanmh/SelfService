package com.gmh.entity.common;

import com.gmh.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
　* @description: 功能实体
　* @param
　* @return
　* @author GMH
　* @date 2019/11/6 23:51
　*/
@Data
@ApiModel(value = "功能实体")
public class Function extends BaseEntity {

    @ApiModelProperty(value = "功能CODE")
    private String code;

    @ManyToOne
    @ApiModelProperty(value = "所属菜单ID")
    private Menu menu;

    @ApiModelProperty(value = "功能名称")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "sys_role_function",
            joinColumns = {@JoinColumn(name = "function_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;
}
