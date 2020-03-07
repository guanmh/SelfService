package com.gmh.service.common;

import com.gmh.entity.common.User;
import com.gmh.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * @project: crm-springboot
 * @author: TYX
 * @create: 2019-01-24 09:37
 * @description: 系统登录用户接口
 */
public interface IUserService extends IBaseService<User> {

  /**
   * 根据账号查询
   *
   * @param accountNames
   * @return
   */
  List<User> queryByAccountName(List<String> accountNames);
}
