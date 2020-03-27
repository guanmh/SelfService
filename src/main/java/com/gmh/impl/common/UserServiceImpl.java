package com.gmh.impl.common;

import com.gmh.entity.common.User;
import com.gmh.impl.BaseServiceImpl;
import com.gmh.mapper.BaseMapper;
import com.gmh.mapper.common.UserMapper;
import com.gmh.service.common.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @project: self-springboot
 * @author: GMH
 * @create: 2019-01-24 09:38
 * @description: 系统登录用户
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService {

  @Value("${self-params.user.default-password}")
  private String defaultPassword;

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private UserMapper userMapper;



  /**
   * 根据账号查询
   *
   * @param accountNames
   * @return
   */
  @Override
  public List<User> queryByAccountName(List<String> accountNames) {
    return userMapper.queryByAccountName(accountNames);
  }

  @Override
  public void addUser(User user) {
    //加密密码
    user.setLoginPassword(passwordEncoder.encode(user.getLoginPassword()));
    userMapper.insert(user);
  }

  @Override
  protected BaseMapper getMapper() {
    return userMapper;
  }

}
