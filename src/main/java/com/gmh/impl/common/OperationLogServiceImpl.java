package com.gmh.impl.common;

import com.gmh.entity.common.OperationLog;
import com.gmh.impl.BaseServiceImpl;
import com.gmh.mapper.BaseMapper;
import com.gmh.mapper.common.OperationLogMapper;
import com.gmh.service.common.IOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @project: self
 * @author: GMH
 * @create: 2019-01-28 13:40
 * @description: 日志操作
 */
@Service
public class OperationLogServiceImpl extends BaseServiceImpl<OperationLog>
    implements IOperationLogService {

  @Autowired
  private OperationLogMapper logMapper;

  /**
   * 子类实现各自的mapper
   *
   * @return
   */
  @Override
  protected BaseMapper getMapper() {
    return logMapper;
  }

  @Override
  public OperationLog get(Long entityId) {
    return null;
  }

  @Override
  public void remove(Long id) {

  }
}
