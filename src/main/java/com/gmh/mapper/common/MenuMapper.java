package com.gmh.mapper.common;

import com.gmh.entity.common.Menu;
import com.gmh.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/** @author GMH */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

  /** 删除关联角色关系 */
  @Delete("DELETE FROM c_role_function WHERE role_id = #{roleId}")
  void deleteRoleFunction(Long roleId);

  /** 删除关联角色菜单关系 */
  @Delete("DELETE FROM c_role_menu WHERE role_id = #{roleId}")
  void deleteRoleMenu(Long roleId);


  @Select("SELECT menu_id FROM c_role_menu WHERE role_id=#{rid}")
  List<String> ids(Long rid);

  /**
   * 保存
   *
   * @param menu
   */
  @Insert(
      "INSERT INTO c_menu(name,icon,order_asc,code,parent_id) VALUES(#{name}, #{icon}, #{order}, #{code}, #{parentMenu.id})")
  int insertParnet(Menu menu);

  /**
   * 保存关系
   *
   * @param roleId
   * @param functionId
   */
  @Insert("INSERT INTO c_role_function ( role_id, function_id ) VALUES ( #{roleId}, #{menuId} )")
  void insertRoleFunction(@Param("roleId") Long roleId, @Param("menuId") Long functionId);

  /**
   * 保存关系
   *
   * @param roleId
   * @param menuId
   */
  @Insert("INSERT INTO c_role_menu ( role_id, menu_id ) VALUES ( #{roleId}, #{menuId} )")
  void insertRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

  /**
   * 管理界面的树形菜单
   *
   * @return
   */
  @Select(
      "SELECT m.id AS mid, m.`name` AS mname, m.parent_id AS mpid, c.id AS cid, c.`name` AS cname, c.menu_id AS cmid FROM c_menu AS m LEFT JOIN c_function AS c ON c.menu_id = m.id AND c.is_cancel = 0 WHERE m.is_cancel = 0 ORDER BY m.order_asc ASC")
  List<Map<String, Object>> tree();

  /**
   * 查询菜单，包括父级ID
   *
   *
   * @return
   */
  List<Menu> getMenus(@Param("userId") Long userId);
}
