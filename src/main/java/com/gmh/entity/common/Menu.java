package com.gmh.entity.common;

import com.gmh.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
　* @description:系统菜单
　* @param
　* @return
　* @author GMH
　* @date 2019/11/6 23:53
　*/
@Data
@ApiModel(value = "系统菜单")
public class Menu extends BaseEntity {

    @OneToMany(mappedBy = "parentMenu")
    @ApiModelProperty(value = "子菜单")
    private List<Menu> childMenu;

    @ApiModelProperty(value = "菜单CODE")
    @NotBlank(message = "菜单唯一编码不能为空")
    private String code;

    @OneToMany(mappedBy = "menu")
    @ApiModelProperty(value = "功能权限")
    private List<Function> functions;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "菜单名称")
    @NotBlank(message = "菜单名称不能为空")
    private String name;

    @ApiModelProperty(value = "菜单序号")
    private Integer order;

    @ManyToOne
    @ApiModelProperty(value = "所属菜单")
    private Menu parentMenu;

    @ManyToMany
    @JoinTable(
            name = "sys_role_menu",
            joinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    public Menu() {
    }

    public Menu(Long id){
        this.setId(id);
    }

    public Menu(String name, Integer order, String code, Menu parentMenu) {
        this.name = name;
        this.order = order;
        this.code = code;
        this.parentMenu = parentMenu;
        this.parentMenu.addMenu(this);
    }

    public void addFunction(Function function) {
        if (Objects.isNull(functions)) {
            setFunctions(new ArrayList<>());
        }
        getFunctions().add(function);
    }

    public List<Menu> getChildMenu() {
        return childMenu;
    }

    public void setChildMenu(List<Menu> childMenu) {
        this.childMenu = childMenu;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    private void addMenu(Menu menu) {
        if (Objects.isNull(getChildMenu())) {
            setChildMenu(new ArrayList<>());
        }
        getChildMenu().add(menu);
    }
}
