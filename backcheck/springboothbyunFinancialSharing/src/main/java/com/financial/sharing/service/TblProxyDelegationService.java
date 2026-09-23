package com.financial.sharing.service;

import java.util.List;
import java.util.Map;

/**
 * 代理委托服务接口
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
public interface TblProxyDelegationService {

    /**
     * 查询代理委托列表（分页）
     *
     * @param param 查询参数
     * @return 分页结果
     */
    Object getList(Object param);

    /**
     * 保存或更新代理委托
     *
     * @param param 代理委托数据
     * @return 保存结果
     */
    Object saveOrUpdate(Object param);

    /**
     * 删除代理委托
     *
     * @param delegationId 委托ID
     * @return 删除结果
     */
    Object delete(String delegationId);

    /**
     * 获取代理委托详情
     *
     * @param delegationId 委托ID
     * @return 委托详情
     */
    Object getDetail(String delegationId);

    /**
     * 更新代理委托状态
     *
     * @param delegationId 委托ID
     * @param status 状态
     * @return 更新结果
     */
    Object updateStatus(String delegationId, Integer status);

    /**
     * 获取代理委托权限
     *
     * @param delegationId 委托ID
     * @return 权限列表
     */
    Object getPermissions(String delegationId);

    /**
     * 保存代理委托权限
     *
     * @param delegationId 委托ID
     * @param permissions 权限数据
     * @return 保存结果
     */
    Object savePermissions(String delegationId, Object permissions);

    /**
     * 获取代理委托历史记录
     *
     * @param delegationId 委托ID
     * @return 历史记录列表
     */
    Object getHistory(String delegationId);

    /**
     * 批量删除代理委托
     *
     * @param delegationIds 委托ID列表
     * @return 删除结果
     */
    Object batchDelete(List<String> delegationIds);

    /**
     * 激活代理委托
     *
     * @param delegationId 委托ID
     * @return 激活结果
     */
    Object activate(String delegationId);

    /**
     * 停用代理委托
     *
     * @param delegationId 委托ID
     * @return 停用结果
     */
    Object deactivate(String delegationId);

    /**
     * 暂停代理委托
     *
     * @param delegationId 委托ID
     * @param suspendInfo 暂停信息
     * @return 暂停结果
     */
    Object suspend(String delegationId, Object suspendInfo);

    /**
     * 恢复代理委托
     *
     * @param delegationId 委托ID
     * @return 恢复结果
     */
    Object resume(String delegationId);

    /**
     * 获取我的代理委托
     *
     * @param userId 用户ID
     * @param delegationType 委托类型
     * @return 我的代理委托列表
     */
    Object getMyDelegations(String userId, String delegationType);

    /**
     * 获取代理委托统计
     *
     * @param delegationId 委托ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据
     */
    Object getStatistics(String delegationId, String startDate, String endDate);
}

