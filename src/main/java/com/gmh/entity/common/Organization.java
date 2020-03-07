package com.gmh.entity.common;

import com.gmh.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
　* @description:组织
　* @param
　* @return
　* @author GMH
　* @date 2019/11/6 23:56
　*/
@Data
@ApiModel(value = "组织")
public class Organization extends BaseEntity {

    @ApiModelProperty(value = "下级组织")
    @OneToMany(mappedBy = "parent")
    private List<Organization> childes;

    @NotBlank(message = "组织名称不能为空")
    @ApiModelProperty(value = "组织名称")
    private String name;

    @ManyToOne
    @ApiModelProperty(value = "所属上级")
    private Organization parent;

    @OneToMany(mappedBy = "organization")
    private List<Role> roles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
