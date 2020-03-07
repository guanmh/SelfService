package com.gmh.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @project: self
 * @author: GMH
 * @create: 2019-04-04 09:59
 * @description: 基础VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseVO {

  @ApiModelProperty(value = "数据ID（新增数据时为null，更新时必填）")
  private Long id;
}
