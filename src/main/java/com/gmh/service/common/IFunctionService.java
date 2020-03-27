package com.gmh.service.common;

import com.gmh.entity.common.Function;
import com.gmh.service.IBaseService;

import java.util.List;

/**
 * 菜单功能
 *
 * @author GMH
 */
public interface IFunctionService extends IBaseService<Function> {

    List<Function> queryBuUserId(Long id);

}
