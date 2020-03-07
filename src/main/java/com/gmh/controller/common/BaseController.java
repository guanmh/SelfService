package com.gmh.controller.common;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.gmh.entity.BaseEntity;
import com.gmh.entity.common.OperationLog;
import com.gmh.enums.LogTypeEnum;
import com.gmh.enums.ReturnCodeEnum;
import com.gmh.service.IBaseService;
import com.gmh.service.common.IOperationLogService;
import com.gmh.utils.*;
import com.gmh.vo.BaseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author GMH
 * @title: BaseController
 * @projectName Self
 * @description: 基础控制器
 * @date 2019/11/23 23:30
 */
public abstract class BaseController<T extends BaseEntity> {

    /** 日志操作名称 */
    private String logName;

    @Autowired
    private IOperationLogService logService;
    private Class<T> tClass;

    public BaseController() {
        tClass =
                (Class<T>)
                        ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Api log = getClass().getAnnotation(Api.class);
        if (log != null) {
            logName = log.tags()[0];
        }
    }

    @DeleteMapping("delete/{id}")
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "数据ID", paramType = "path", required = true)
    })
    public String delete(@ApiIgnore HttpServletRequest request, @PathVariable(name = "id") Long id) {
        T baseEntity = (T) getBasicService().get(id);
        getBasicService().remove(id);
        // 记录日志
        insertLog(request, baseEntity, null, LogTypeEnum.DELETE);
        return ReturnUtil.success();
    }

    /** 详情 */
    @GetMapping("get/{entityId}")
    @ApiOperation(value = "详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "entityId", value = "数据ID", paramType = "path", required = true)
    })
    public String get(@PathVariable(value = "entityId") Long entityId) {
        T t = (T) getBasicService().get(entityId);
        return ReturnUtil.success(formatterGetData(getEntityToJSONObject(t)));
    }

    /**
     * 通用分页查询
     *
     * @param params
     * @param pageSize
     * @return
     */
    @GetMapping(value = "page")
    @ApiOperation(value = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页，从0开始", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页显示最大行数", required = true),
            @ApiImplicitParam(name = "params", value = "其他查询参数")
    })
    public String page(
            @ApiIgnore HttpServletRequest request,
            @RequestParam Map<String, Object> params,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        long timeMillis = System.currentTimeMillis();
        PageInfo pageInfo = getBasicService().page(setPageFilter(request, params), page, pageSize);
        JSONObject data = new JSONObject();
        if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
            data.put("list", formatterPageData(pageInfo.getList()));
        }
        data.put("total", pageInfo.getTotal());
        String returnJSON = ReturnUtil.success(data);
        return returnJSON;
    }

    /**
     * 保存
     *
     * @param t
     * @return
     */
    @PostMapping("save")
    @ApiOperation(value = "保存")
    public String save(
            @ApiIgnore HttpServletRequest request,
            @ApiIgnore CsrfToken token,
            @RequestParam Map<String, Object> params,
            @ApiIgnore @Valid T t,
            BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return ReturnUtil.error(bindingResult.getFieldError().getDefaultMessage());
        }
        // 数据库中的数据
        T database = null;
        if (CacheUtils.repeatSubmit(token)) {
            return ReturnUtil.error(ReturnCodeEnum.REPEATED_SUBMIT);
        }
        // 本次保存到数据库中的数据
        T saveData;
        // 是否新增
        boolean isInsert = Objects.isNull(t.getId());
        if (!isInsert) {
            /** 如果有ID，则从数据库中取出 */
            database = (T) getBasicService().get(t.getId());
        }
        // 设置其他值到本次保存数据中
        saveData = setSubmitData(request, params, t, database);
        // 保存本次数据
        Long entityId = getBasicService().save(saveData);
        saveAfter(saveData);
        // 记录日志
        insertLog(request, database, saveData, isInsert ? LogTypeEnum.INSERT : LogTypeEnum.UPDATE);
        return ReturnUtil.success(entityId);
    }

    /**
     * 根据当前对象获取数据库中相关属性是否重复
     *
     * @param propertyName 属性名称
     * @param propertyValue
     * @return
     */
    protected int getRepeat(
            String tableName, String propertyName, Object propertyValue) {
        return getBasicService().getRepeat(tableName, propertyName, propertyValue,null,true, "=");
    }

    /**
     * 保存业务实体类（可由实现类重写）
     *
     * @param request
     * @param database
     * @return
     */
    protected T setSubmitData(
            HttpServletRequest request, Map<String, Object> params, T t, T database){
        return t;
    }

    /** 保存之后 */
    public void saveAfter(T T) {}

    /** 设置分页查询参数 */
    private Map<String, Object> setPageFilter(
            HttpServletRequest request, Map<String, Object> params) {
        return params;
    }

    /** 格式化详情数据（由业务控制器实现） */
    public Object formatterGetData(JSONObject entity) {
        return entity;
    }

    /**
     * 格式化分页查询数据（由业务控制器实现）
     *
     * @param pageData
     * @return
     */
    protected List<?> formatterPageData(List<?> pageData) {
        return pageData;
    }

    /**
     * 将查询的对象转换为JSONObject
     *
     * @param t
     * @return
     */
    protected JSONObject getEntityToJSONObject(T t) {
        return (JSONObject) JSONObject.toJSON(t);
    }

    /**
    　* @description: 保存日志
    　* @param
    　* @return
    　* @author GMH
    　* @date 2019/11/23 23:40
    　*/
    private void insertLog(HttpServletRequest request, T database, T saveData, LogTypeEnum logType) {
        OperationLog log = LogUtil.createOperationLog(request, logType, logName);
        try {
            switch (logType) {
                case INSERT:
                    setAddLog(log, saveData);
                    break;
                case UPDATE:
                    setUpdateLog(log, database, saveData);
                    break;
                case DELETE:
                    setDeleteLog(log, database);
                    break;
            }
            logService.save(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置日志的操作功能和操作内容
     *
     * @param log 日志对象
     * @param t 数据库添加的数据
     */
    private void setAddLog(OperationLog log, T t) {
        log.setContent(JSONObject.toJSONString(t));
    }

    /**
     * 设置日志的操作功能和操作内容
     *
     * @param log 日志对象
     * @param t 数据库添加的数据
     */
    private void setDeleteLog(OperationLog log, T t) {
        log.setContent(JSONObject.toJSONString(t));
    }

    /**
     * 设置日志的操作功能和操作内容
     *
     * @param log 日志对象
     * @param before 修改之前的数据
     * @param after 修改之后的数据
     */
    private void setUpdateLog(OperationLog log, T before, T after) {
        log.setContent(JSONObject.toJSONString(before) + "->" + JSONObject.toJSONString(after));
    }

    /**
     * 控制器主要业务接口（用于：分页查询）
     *
     * @return
     */
    protected abstract IBaseService getBasicService();
}
