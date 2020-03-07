package com.gmh.service;

import com.github.pagehelper.PageInfo;
import com.gmh.entity.BaseEntity;

import java.util.List;
import java.util.Map;

/**
 * @project: crm-springboot
 * @author: GMH
 * @create: 2019-01-24 14:19
 * @description: 基础业务类接口
 */
public interface IBaseService<T extends BaseEntity> {

  /**
   * 获取所有没有标记为删除状态的数据
   *
   * @return
   */
  List<T> getAllNoCancel();

  /**
   * 通用详情查询
   *
   * @param entityId 数据ID
   * @return
   */
  T get(Long entityId);

  void remove(Long id);

  /**
   * 保存数据
   *
   * @return
   */
  Long save(T t);

  /**
   * 通用分页
   *
   * @param params 过滤条件
   * @param pageNum 开始条数
   * @param pageSize 总条数
   * @return
   */
  PageInfo page(Map<String, Object> params, int pageNum, int pageSize);

  /**
   * 根据当前对象获取数据库中相关属性是否重复
   *
   * @param tableName 表格名称
   * @param propertyName 属性名称
   * @param propertyValue 属性值
   * @param haveCancel 是否包含删除状态
   * @param operator 比较方式
   * @return
   */
  public int getRepeat(
          String tableName,
          String propertyName,
          Object propertyValue,
          Long id,
          boolean haveCancel,
          String operator);

}
