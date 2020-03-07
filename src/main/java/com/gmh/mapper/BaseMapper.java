package com.gmh.mapper;

import com.gmh.entity.BaseEntity;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.base.BaseDeleteMapper;
import tk.mybatis.mapper.common.base.BaseInsertMapper;
import tk.mybatis.mapper.common.base.BaseSelectMapper;
import tk.mybatis.mapper.common.base.BaseUpdateMapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 通用mapper父类
 *
 * @author GMH
 */
public interface BaseMapper<T extends BaseEntity>
    extends BaseSelectMapper<T>, BaseInsertMapper<T>, BaseUpdateMapper<T>, BaseDeleteMapper<T> {

  /**
   * 检测ID在不在数据库中
   *
   * @param tableName
   * @param ids
   * @return
   */
  @Select(
      "<script>"
          + "SELECT id ,count(id) c from ${tableName} where id in"
          + "<foreach item='id' index='index' collection='ids' open='(' separator=',' close=')'>"
          + "#{id}"
          + "</foreach>"
          + "AND is_cancel = 0 GROUP BY id"
          + "</script>")
  List<Map<String, Long>> checkIdInDatabase(
          @Param("tableName") String tableName, @Param("ids") Long... ids);

  @Delete(
      "<script>DELETE from ${tableName } where id in"
          + "<foreach item='id' index='index' collection='ids' open='(' separator=',' close=')'>"
          + "#{id}"
          + "</foreach>"
          + "</script>")
  Integer deleteIds(@Param("tableName") String tableName, @Param("ids") Collection<Long> ids);

  /**
   * 根据己方ID删除中间表数据
   *
   * @param tableName
   * @param meField 己方ID字段名
   * @param meId 己方ID
   */
  @Delete("DELETE FROM ${tableName} where ${meField} = #{meId}")
  void deleteIntermediate(
          @Param("tableName") String tableName,
          @Param("meField") String meField,
          @Param("meId") Long meId);

  @Select(
      "<script>"
          + "SELECT id FROM ${tableName} where"
          + " ${column}"
          + "<choose>"
          + "<when test='operator == \"LIKE\"'>"
          + " LIKE concat('%',concat(#{propertyValue},'%'))"
          + "</when>"
          + "<otherwise> = #{propertyValue}</otherwise>"
          + "</choose>"
          + "<if test='!haveCancel'>"
          + " AND is_cancel = 0"
          + "</if>"
          + "</script>")
  List<Long> getIds(
          @Param(value = "tableName") String tableName,
          @Param(value = "column") String column,
          @Param(value = "propertyValue") Object propertyValue,
          @Param(value = "haveCancel") boolean haveCancel,
          @Param(value = "operator") String operator);

  /**
   * 获取insert的ID
   *
   * @return
   */
  @Select("SELECT LAST_INSERT_ID()")
  List<Long> getInsertId();

  /**
   * 根据当前对象获取数据库中相关属性是否重复
   *
   * @param tableName
   * @param column
   * @param id
   * @param propertyValue
   * @param haveCancel 是否包含删除状态
   * @param operator 比较方式
   * @return
   */
  @Select(
      "<script>"
          + "SELECT count(id) FROM ${tableName} where"
          + " ${column}"
          + "<choose>"
          + "<when test='operator == \"LIKE\"'>"
          + " LIKE concat('%',concat(#{propertyValue},'%'))"
          + "</when>"
          + "<otherwise> = #{propertyValue}</otherwise>"
          + "</choose>"
          + "<if test='!haveCancel'>"
          + " AND is_cancel = 0"
          + "</if>"
          + "<if test='id!=null'>"
          + " AND id != #{id}"
          + "</if>"
          + "</script>")
  Integer getRepeat(
          @Param(value = "tableName") String tableName,
          @Param(value = "column") String column,
          @Param(value = "id") Long id,
          @Param(value = "propertyValue") Object propertyValue,
          @Param(value = "haveCancel") boolean haveCancel,
          @Param(value = "operator") String operator);

  /**
   * 根据己方ID删除中间表数据
   *
   * @param tableName
   * @param meField 己方ID字段名
   * @param associatedField 对方的字段名
   * @param meId 己方ID
   * @param associatedId 对方ID
   */
  @Insert(
      "INSERT INTO ${tableName} (${meField}, ${associatedField}) VALUES (#{meId}, #{associatedId})")
  void insertIntermediate(
          @Param("tableName") String tableName,
          @Param("meField") String meField,
          @Param("associatedField") String associatedField,
          @Param("meId") Long meId,
          @Param("associatedId") Long associatedId);

  @Update("UPDATE ${tableName} set is_cancel = 1 where id = #{id}")
  void remove(@Param("tableName") String tableName, @Param("id") Long id);

  @Update("UPDATE ${tableName} SET ${field} = #{value} WHERE id = #{id}")
  void updateById(
          @Param("tableName") String tableName,
          @Param("id") Long id,
          @Param("field") String field,
          @Param("value") Object value);

  @Update(
      "<script>"
          + "UPDATE ${tableName} SET ${field}"
          + " = #{value}"
          + " WHERE id in "
          + "<foreach close=\")\" collection=\"ids\" item=\"id\" open=\"(\" separator=\",\">"
          + "#{id}"
          + "</foreach>"
          + "</script>")
  void updateByIds(
          @Param("tableName") String tableName,
          @Param("ids") List<Long> ids,
          @Param("field") String field,
          @Param("value") Object value);

  @Update(
      "<script>"
          + "UPDATE ${tableName} SET "
          + "<foreach item=\"value\" index=\"key\" collection=\"mapData.entrySet()\" separator=\",\">"
          + "${key} = #{value}"
          + "</foreach>"
          + "WHERE id = #{id}"
          + "</script>")
  void updateDateById(
          @Param("tableName") String tableName,
          @Param("id") Long id,
          @Param("saveData") Map<String, Object> saveData);
}
