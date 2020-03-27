package com.gmh.impl.common;

import com.gmh.entity.common.Function;
import com.gmh.impl.BaseServiceImpl;
import com.gmh.mapper.BaseMapper;
import com.gmh.mapper.common.FunctionMapper;
import com.gmh.service.common.IFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @project: self-springboot
 * @author: GMH
 * @create: 2019-01-23 10:54
 * @description: 菜单功能
 */
@Service
public class FunctionServiceImpl extends BaseServiceImpl<Function> implements IFunctionService {

  @Autowired
  private FunctionMapper functionMapper;

  /**
   * 获取所有没有标记为删除状态的数据
   *
   * @return
   */
  @Override
  public List<Function> getAllNoCancel() {
    return functionMapper.queryList(null);
  }

  @Override
  public List pageMapper(Map<String, Object> params) {
    return super.pageMapper(params);
  }

  @Override
  public List<Function> queryBuUserId(Long id) {
    return functionMapper.queryList(id);
  }

  @Override
  protected BaseMapper getMapper() {
    return functionMapper;
  }
}
