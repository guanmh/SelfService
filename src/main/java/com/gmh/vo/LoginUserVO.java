package com.gmh.vo;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Objects;

/**
 * @project: crm-springboot
 * @author: TYX
 * @create: 2019-01-23 14:59
 * @description: 登陆用户VO
 */
@Data
public class LoginUserVO extends User {

  /** 登陆用户ID */
  private Long id;
  /** 是否是admin用户 */
  private Boolean isAdmin;
  /** 登陆用户名称 */
  private String name;
  private com.gmh.entity.common.User user;

  public LoginUserVO(
      Long id,
      String name,
      String username,
      String password,
      boolean isAdmin,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, Objects.nonNull(id), true, true, true, authorities);
    setId(id);
    com.gmh.entity.common.User user = new com.gmh.entity.common.User();
    user.setId(getId());
    setUser(user);
    setName(name);
    setIsAdmin(isAdmin);
  }
}
