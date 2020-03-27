package com.gmh.mapper.common;

import com.gmh.entity.common.Function;
import com.gmh.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单功能
 *
 * @author GMH
 */
@Repository
public interface FunctionMapper extends BaseMapper<Function> {

    @Select("SELECT function_id FROM c_role_function WHERE role_id=#{rid}")
    List<String> ids(Long rid);

  /** 保存 */
  @Insert("INSERT INTO c_function(name,code,menu_id) VALUES(#{name}, #{code}, #{menu.id})")
  int insertMenu(Function function);


  @Select("select count(*) from c_function where menu_id = #{menu.id} and code = #{code}")
  int validate(Function function);

    /**
     * 查询权限
     *
     *
     * @return
     */
    List<Function> queryList(@Param("userId") Long userId);
}
