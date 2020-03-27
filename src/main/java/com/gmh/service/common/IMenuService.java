package com.gmh.service.common;

import com.gmh.entity.common.Menu;
import com.gmh.service.IBaseService;
import com.gmh.vo.TreeVO;

import java.util.List;

/**
 * @project: self-springboot
 * @author: GMH
 * @create: 2019-01-23 10:29
 * @description: 系统菜单
 */
public interface IMenuService extends IBaseService<Menu> {

    /**
     * 管理界面的tree
     *
     * @return
     */
    List<TreeVO> getTree();

    List<Menu> queryByUserId(Long id);

}
