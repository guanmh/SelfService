package com.gmh.vo;

import lombok.Data;

import java.util.*;

/**
 * @project: self
 * @author: GMH
 * @create: 2019-01-23 11:19
 * @description: 树形数据结构
 */
@Data
public class TreeVO {

  /** 下级菜单 */
  private List<TreeVO> children;
  /** 数据 */
  private Map<String, Object> data;
  /** 树形结构唯一标识 */
  private String id;
  /** 名称 */
  private String label;
  /** 上级CODE */
  private String parentId;
  /** 下拉框选择的值 */
  private String value;

  public TreeVO() {}

  public TreeVO(String id, String label, String parentId) {
    this.id = id;
    this.label = label;
    this.parentId = parentId;
  }

  public void addChild(TreeVO tree) {
    if (Objects.isNull(getChildren())) {
      setChildren(new ArrayList<>());
    }
    getChildren().add(tree);
  }

  public void putData(String key, Object value) {
    if (Objects.isNull(data)) {
      data = new HashMap<>();
    }
    data.put(key, value);
  }
}
