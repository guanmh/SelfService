package com.gmh.mapper.common;

import com.gmh.entity.common.Organization;
import com.gmh.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @project: Self-springboot
 * @author: GMH
 * @create: 2019-01-24 16:35
 * @description: 组织
 */
@Repository
public interface OrganizationMapper extends BaseMapper<Organization> {

  /**
   * 没有删除的组织信息
   *
   * @return
   */
  List<Organization> getAllNoCancel();
  /**
   *@Author gmh
   *@Description 根据父id查询子节点
   *@Date 11:23 2019/6/11 0011
   *@Param [pid]
   *@Return java.util.List<Organization>
   **/
  List<Organization> getOrgByParentId(Map<String, Object> param);
}
