package com.gmh.impl.common;

import com.gmh.entity.common.Menu;
import com.gmh.impl.BaseServiceImpl;
import com.gmh.mapper.BaseMapper;
import com.gmh.mapper.common.FunctionMapper;
import com.gmh.mapper.common.MenuMapper;
import com.gmh.service.common.IFunctionService;
import com.gmh.service.common.IMenuService;
import com.gmh.utils.CollectionUtils;
import com.gmh.vo.TreeVO;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @project: crm-springboot
 * @author: TYX
 * @create: 2019-01-23 10:32
 * @description: 系统菜单
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements IMenuService {

  private static final Logger LOG = LoggerFactory.getLogger(MenuServiceImpl.class);
  private static final String FIST_FUNCTION_ID = "F";
  private static final String FIST_MENU_ID = "M";
  @Autowired
  private FunctionMapper functionMapper;
  @Autowired
  private IFunctionService functionService;
  @Autowired
  private MenuMapper menuMapper;

  /**
   * 获取所有没有标记为删除状态的数据
   *
   * @return
   */
  @Override
  public List<Menu> getAllNoCancel() {
    return menuMapper.getMenus();
  }

  @Override
  public List<TreeVO> getTree() {
    List<Map<String, Object>> result = menuMapper.tree();
    List<TreeVO> root = null;
    if (CollectionUtils.isNotEmpty(result)) {
      root = new ArrayList<>();
      /** 查询字段：mid菜单ID、mname菜单名称、mpid菜单父级ID cid操作id、cname操作名称、cmid操作关联菜单id */
      /** map转为treevo */
      List<TreeVO> treeVOS =
              result.stream()
                      .map(
                              rs -> {
                                TreeVO menu = new TreeVO();
                                TreeVO fn = null;
                                menu.setId(FIST_MENU_ID + MapUtils.getLong(rs, "mid"));
                                menu.setLabel(MapUtils.getString(rs, "mname"));
                                Long mpid = MapUtils.getLong(rs, "mpid");
                                if (Objects.nonNull(mpid)) {
                                  menu.setParentId(FIST_MENU_ID + mpid);
                                }
                                Long cid = MapUtils.getLong(rs, "cid");
                                if (Objects.nonNull(cid)) {
                                  fn = new TreeVO();
                                  fn.setId(FIST_FUNCTION_ID + cid);
                                  fn.setLabel(MapUtils.getString(rs, "cname"));
                                  fn.setParentId(FIST_MENU_ID + MapUtils.getLong(rs, "cmid"));
                                }
                                return new TreeVO[] {menu, fn};
                              })
                      .flatMap(t -> Arrays.stream(t))
                      .filter(Objects::nonNull)
                      .filter(CollectionUtils.distinctByKey(TreeVO::getId))
                      .collect(Collectors.toList());
      /** 合并上下级关系 */
      for (int i = 0, size = CollectionUtils.size(treeVOS); i < size; i++) {
        TreeVO treeVO = treeVOS.get(i);
        String parentId = treeVO.getParentId();
        if (StringUtils.isNoneEmpty(parentId)) {
          Optional<TreeVO> find =
                  treeVOS.stream().filter(p -> StringUtils.equals(p.getId(), parentId)).findFirst();
          // 找到父级
          if (find.isPresent()) {
            find.get().addChild(treeVO);
          }
        } else {
          // 必须有顶级菜单
          root.add(treeVO);
        }
      }
    }
    return root;
  }

  @Override
  protected BaseMapper getMapper() {
    return menuMapper;
  }

}
