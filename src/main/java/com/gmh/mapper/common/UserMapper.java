package com.gmh.mapper.common;

import com.gmh.entity.common.User;
import com.gmh.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @project: self-springboot
 * @author: GMH
 * @create: 2019-01-21 15:04
 * @description:登录用户
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

  /**
   * 绑定角色
   *
   * @param userId
   * @param roleId
   */
  @Insert("INSERT INTO c_user_role(user_id,role_id) VALUES(#{userId}, #{roleId})")
  void bindRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

  /**
   * 商机来源可选人员
   *
   * @param companyId
   * @return
   */
  @Select(
      "SELECT c_user.id, c_user.`name` FROM c_role INNER JOIN c_user_role ON c_user_role.role_id = c_role.id INNER JOIN c_user ON c_user_role.user_id = c_user.id WHERE c_role.is_cancel = 0 AND c_user.is_cancel = 0 AND c_role.power = 'AFTER_SALES' AND c_user.company_id = #{companyId}")
  List<Map<String, Object>> chanceSourceUsers(Long companyId);

  /**
   * 根据用户ID删除角色中间表
   *
   * @param userId
   */
  @Delete("DELETE FROM c_user_role WHERE c_user_role.user_id = #{userId}")
  void deleteRoleByUserId(@Param("userId") Long userId);

  User get(@Param("id") Long id);

  @Override
  int insert(User user);

  /**
   * 获取登陆人相关
   *
   * @param name
   * @return
   */
  User loginUser(String name);

  /**
   * 分页查询
   *
   * @param params
   * @return
   */
  List<Map<String, Object>> pageUser(Map<String, Object> params);

  /**
   * 根据账号查询
   *
   * @param accountNames
   * @return
   */
  List<User> queryByAccountName(@Param("accountNames") List<String> accountNames);

  /**
   * 根据角色编码查询
   *
   * @param roleCode
   * @param companyId
   * @return
   */
  @Select(
      "SELECT DISTINCT c_user.id, c_user.`name`, c_user.phone FROM c_user INNER JOIN c_user_role ON c_user_role.user_id = c_user.id INNER JOIN c_role ON c_user_role.role_id = c_role.id WHERE c_user.is_cancel = 0 AND c_role.is_cancel = 0 AND c_role.power = #{roleCode} AND c_user.company_id = #{companyId} ORDER BY c_user.`name` ASC")
  List<Map<String, Object>> queryByRoleCode(
          @Param("roleCode") String roleCode, @Param("companyId") Long companyId);

  List<Long> queryByRoleCodes(@Param("codes") String[] codes, @Param("companyId") Long companyId);

  @Select(
      "SELECT ur.user_id,u.name FROM c_role AS r "
          + "LEFT JOIN c_user_role AS ur ON ur.role_id = r.id LEFT JOIN c_user AS u ON ur.user_id = u.id "
          + "WHERE r.is_cancel = 0 AND r.id = 4")
  List<Map<String, Object>> querySale();

  @Override
  int updateByPrimaryKey(User user);

  /** @Author gmh @Description @Date 10:02 2019/6/10 0010 @Param [userId, userPwd] @Return int */
  @Update("update c_user set login_password =#{userPwd} where id =#{userId}")
  int updateUserPwd(@Param("userId") long userId, @Param("userPwd") String userPwd);
}
