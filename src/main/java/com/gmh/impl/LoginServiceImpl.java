package com.gmh.impl;

import com.gmh.entity.common.Menu;
import com.gmh.entity.common.Role;
import com.gmh.entity.common.Function;
import com.gmh.enums.SystemStringEnum;
import com.gmh.mapper.common.UserMapper;
import com.gmh.service.ILoginService;
import com.gmh.service.common.IFunctionService;
import com.gmh.service.common.IMenuService;
import com.gmh.service.common.IRoleService;
import com.gmh.utils.CollectionUtils;
import com.gmh.vo.LoginUserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @project: self-springboot
 * @author: GMH
 * @create: 2019-01-21 18:03
 * @description: 登录相关
 */
@Slf4j
@Service
public class LoginServiceImpl implements ILoginService {

  @Value("${self-params.user.adminAccountName}")
  private String adminAccountName;

  @Value("${self-params.user.adminId}")
  private long adminId;

  @Value("${self-params.user.adminName}")
  private String adminName;

  @Value("${self-params.user.adminPassword}")
  private String adminPassword;

  @Lazy
  @Autowired(required = false)
  private PasswordEncoder passwordEncoder;

  @Autowired private IRoleService roleService;
  @Autowired private UserMapper userMapper;
  @Autowired private IFunctionService functionService;
  @Autowired private IMenuService menuService;

  @Override
  public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
    if (adminAccountName.equals(loginName)) {
      List<SimpleGrantedAuthority> authorities =
              getSimpleGrantedAuthorities(
                      roleService.getAllNoCancel(),
                      functionService.getAllNoCancel(),
                      menuService.getAllNoCancel(),
                      SystemStringEnum.RolePowerEnum.ADMIN.getCode());
      return new LoginUserVO(
              adminId,
              adminName,
              adminAccountName,
              passwordEncoder.encode(adminPassword),
              true,
              authorities);
    } else {
//      User user = userMapper.loginUser(loginName);
//      if (Objects.isNull(user)) {
//        throw new UsernameNotFoundException("用户名或密码错误！");
//      }
//      return getUserDetails(user);
      return null;
    }
  }
  private List<SimpleGrantedAuthority> getSimpleGrantedAuthorities(
          List<Role> roles, List<Function> functions, List<Menu> menus, String... codes) {
    List<SimpleGrantedAuthority> authorities =
            new ArrayList(
                    CollectionUtils.size(roles)
                            + CollectionUtils.size(functions)
                            + CollectionUtils.size(menus)
                            + CollectionUtils.size(codes));
    if (CollectionUtils.isNotEmpty(codes)) {
      for (String code : codes) {
        authorities.add(new SimpleGrantedAuthority(code));
      }
    }
    if (CollectionUtils.isNotEmpty(roles)) {
      roles.forEach(
              r -> {
                if (StringUtils.isNotEmpty(r.getPower())) {
                  authorities.add(new SimpleGrantedAuthority(r.getPower()));
                }
              });
    }
    if (CollectionUtils.isNotEmpty(functions)) {
      functions.forEach(
              r -> {
                if (StringUtils.isNotEmpty(r.getCode())) {
                  Menu menu = r.getMenu();
                  if (Objects.nonNull(menu)) {
                    authorities.add(new SimpleGrantedAuthority(menu.getCode() + "_" + r.getCode()));
                  } else {
                    authorities.add(new SimpleGrantedAuthority(r.getCode()));
                  }
                }
              });
    }
    if (CollectionUtils.isNotEmpty(menus)) {
      menus.forEach(
              r -> {
                if (StringUtils.isNotEmpty(r.getCode())) {
                  authorities.add(new SimpleGrantedAuthority(r.getCode()));
                }
              });
    }
    return authorities;
  }
}
