package com.gmh.mapper.common;

import com.gmh.entity.common.Role;
import com.gmh.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @project: Self-springboot
 * @author: GMH
 * @create: 2019-01-24 16:36
 * @description: 角色
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

  /**
   * 没有删除的组织信息
   *
   * @return
   */
  List<Role> getAllNoCancel();

  /**
   * 根据用户ID查询角色
   *
   * @param entityId
   * @return
   */
  @Select(
      "SELECT c_role.`name`, c_role.id FROM c_user INNER JOIN c_user_role ON c_user_role.user_id = c_user.id INNER JOIN c_role ON c_user_role.role_id = c_role.id WHERE c_role.is_cancel = 0 AND c_user.id = #{userId}")
  List<Role> getByUserId(@Param("userId") Long entityId);

  /**
   * 含有组织的角色保存
   *
   * @param role
   */
  @Insert("INSERT INTO c_role(name,organization_id) VALUES(#{name}, #{organization.id})")
  void insertOrganization(Role role);

  @Insert("INSERT INTO c_role (name,power,organization_id ) VALUES( #{name},#{power}, #{organization.id} ) ")
  int insert(Role role);

  @Insert("UPDATE c_role  SET name = #{name},power = #{power},organization_id=#{organization.id} WHERE  id = #{id} ")
  int updateByPrimaryKey(Role role);

  @Override
  Role selectOne(Role role);
}
