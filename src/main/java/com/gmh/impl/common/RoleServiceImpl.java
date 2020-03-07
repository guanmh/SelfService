package com.gmh.impl.common;

import com.gmh.entity.common.Role;
import com.gmh.impl.BaseServiceImpl;
import com.gmh.mapper.BaseMapper;
import com.gmh.mapper.common.RoleMapper;
import com.gmh.service.common.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @project: crm-springboot
 * @author: TYX
 * @create: 2019-01-25 16:36
 * @description: 职位、角色
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements IRoleService {

  @Autowired
  private RoleMapper roleMapper;
  /**
   * 获取所有没有标记为删除状态的数据
   *
   * @return
   */
  @Override
  public List<Role> getAllNoCancel() {
    return roleMapper.getAllNoCancel();
  }

  /**
   * 子类实现各自的mapper
   *
   * @return
   */
  @Override
  protected BaseMapper getMapper() {
    return roleMapper;
  }
}
