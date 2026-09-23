package com.financial.sharing.service;

import com.financial.sharing.entity.TblExpenseParameter;
import com.financial.sharing.entity.TblExpenseParameterOption;
import com.financial.sharing.entity.TblExpenseParameterPermission;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 费用参数服务接口
 *
 * @author Financial Sharing System
 * @since 2025-01-30
 */
public interface TblExpenseParameterService {

    /**
     * 分页查询费用参数
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean<PageResult<TblExpenseParameter>> getList(Object param);

    /**
     * 根据ID查询参数详情
     *
     * @param parameterId 参数ID
     * @return 参数详情
     */
    MyJsonBean<TblExpenseParameter> getById(String parameterId);

    /**
     * 保存或更新费用参数
     *
     * @param param 保存参数
     * @return 操作结果
     */
    MyJsonBean saveOrUpdate(Object param);

    /**
     * 删除费用参数
     *
     * @param parameterId 参数ID
     * @return 操作结果
     */
    MyJsonBean delete(String parameterId);

    /**
     * 更新参数状态
     *
     * @param parameterId 参数ID
     * @param isEnabled 是否启用
     * @return 操作结果
     */
    MyJsonBean updateStatus(String parameterId, Integer isEnabled);

    /**
     * 获取参数选项
     *
     * @param parameterId 参数ID
     * @return 选项列表
     */
    MyJsonBean<List<TblExpenseParameterOption>> getOptions(String parameterId);

    /**
     * 获取参数权限
     *
     * @param parameterId 参数ID
     * @return 权限列表
     */
    MyJsonBean<List<TblExpenseParameterPermission>> getPermissions(String parameterId);

    /**
     * 保存参数选项
     *
     * @param parameterId 参数ID
     * @param options 选项列表
     * @return 操作结果
     */
    MyJsonBean saveOptions(String parameterId, List<TblExpenseParameterOption> options);

    /**
     * 保存参数权限
     *
     * @param parameterId 参数ID
     * @param permissions 权限列表
     * @return 操作结果
     */
    MyJsonBean savePermissions(String parameterId, List<TblExpenseParameterPermission> permissions);
}
