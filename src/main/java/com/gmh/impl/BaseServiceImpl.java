package com.gmh.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gmh.entity.BaseEntity;
import com.gmh.mapper.BaseMapper;
import com.gmh.service.IBaseService;
import com.gmh.utils.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @project: self-springboot
 * @author: GMH
 * @create: 2019-01-22 16:59
 * @description: 通用功能
 */
public abstract class BaseServiceImpl<T extends BaseEntity> implements IBaseService<T> {

  /** 用于查询没有删除的状态过滤条件 */
  private BaseEntity noCancel;

  private Class<T> tClass;

  public BaseServiceImpl() {
    tClass =
            (Class<T>)
                    ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  @Override
  public List<T> getAllNoCancel() {
    return getMapper().select(getNoCancel());
  }

  private BaseEntity getNoCancel() {
    if (Objects.isNull(noCancel)) {
      try {
        noCancel = tClass.newInstance();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return noCancel;
  }

  @Override
  public T get(Long entityId) {
    return null;
  }

  @Override
  public void remove(Long id) {

  }

  @Override
  public Long save(T t) {
    t = saveBeforeData(t);
    if (Objects.isNull(t.getId())) {
      insertData(t);
    } else {
      updateData(t);
    }
    saveAfterData(t);
    return t.getId();
  }

  /**
   * 保存之前的业务方法，可以重写
   *
   * @param t
   * @return
   */
  protected T saveBeforeData(T t) {
    return t;
  }

  /**
   * 新增的业务方法，可以重写
   *
   * @param t
   */
  protected void insertData(T t) {
    getMapper().insert(t);
    setId(t);
  }

  /**
   * 更新数据的业务方法，可以重写
   *
   * @param t
   */
  protected void updateData(T t) {
    getMapper().updateByPrimaryKey(t);
  }

  /**
   * 保存之后
   *
   * @param t
   */
  protected void saveAfterData(T t) {}

  /**
   * 将insert的ID保存到对象中
   *
   * @param o
   * @param <V>
   */
  public <V extends BaseEntity> void setId(V o) {
    if (Objects.isNull(o.getId())) {
      o.setId(insertId());
    }
  }

  /**
   * 获取插入的ID
   *
   * @return
   */
  private Long insertId() {
    return CollectionUtils.get(batchInsertId(), 0, Long.class);
  }

  /**
   * 获取插入的ID集合
   *
   * @return
   */
  private List<Long> batchInsertId() {
    return getMapper().getInsertId();
  }

  @Override
  public PageInfo page(Map<String, Object> params, int pageNum, int pageSize) {
    return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> pageMapper(params));
  }

  /**
   * 分页查询，按照需求可以重写
   *
   * @param params
   * @return
   */
  public List pageMapper(Map<String, Object> params) {
    return getMapper().selectAll();
  }

  /**
   * 根据当前对象获取数据库中相关属性是否重复
   *
   * @param propertyName 属性名称
   * @param propertyValue 属性值
   * @param haveCancel 是否包含删除状态
   * @param operator 比较方式
   * @return
   */
  @Override
  public int getRepeat(
          String  tableName,
          String propertyName,
          Object propertyValue,
          Long id,
          boolean haveCancel,
          String operator) {
    Integer repeat =
            getMapper().getRepeat(tableName, propertyName, id, propertyValue, haveCancel, operator);
    return Objects.isNull(repeat) ? 0 : repeat;
  }

  /**
   * 子类实现各自的mapper
   *
   * @return
   */
  protected abstract BaseMapper getMapper();

}
