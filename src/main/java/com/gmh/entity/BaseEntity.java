package com.gmh.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
/**
 * @project: self-springboot
 * @author: GMH
 * @create: 2019-01-21 15:47
 * @description: 基础实体类
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class BaseEntity implements Serializable {

  private Boolean cancel = false;

  private Date createTime;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
