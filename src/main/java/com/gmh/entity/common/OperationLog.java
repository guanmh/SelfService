package com.gmh.entity.common;

import com.gmh.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @project: crm-springboot
 * @author: GMH
 * @create: 2019-01-28 13:30
 * @description: 操作日志
 */
@Data
@ApiModel(value = "日志实体")
public class OperationLog extends BaseEntity {

    @ApiModelProperty(value = "业务对象")
    private String business;

    @ApiModelProperty(value = "日志内容")
    private String content;

    @ApiModelProperty(value = "操作人IP地址")
    private String ip;

    @ApiModelProperty(value = "操作类型")
    private String type;

    @ApiModelProperty(value = "操作人ID")
    private Long userId;
}
