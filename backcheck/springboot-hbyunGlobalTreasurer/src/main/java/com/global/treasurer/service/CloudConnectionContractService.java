package com.global.treasurer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblCloudConnectionContract;

import java.util.List;
import java.util.Map;

/**
 * 云连接合同服务接口
 *
 * @author 华博云开发团队
 * @since 2025-01-27
 */
public interface CloudConnectionContractService extends IService<TblCloudConnectionContract> {

    /**
     * 分页查询云连接合同
     *
     * @param params 查询参数
     * @return 分页结果
     */
    Map<String, Object> getCloudContractPage(Map<String, Object> params);

    /**
     * 根据ID查询合同
     *
     * @param contractId 合同ID
     * @return 合同信息
     */
    TblCloudConnectionContract getCloudContractById(Long contractId);

    /**
     * 创建合同
     *
     * @param contract 合同信息
     * @return 影响行数
     */
    int createCloudContract(TblCloudConnectionContract contract);

    /**
     * 更新合同
     *
     * @param contract 合同信息
     * @return 影响行数
     */
    int updateCloudContract(TblCloudConnectionContract contract);

    /**
     * 删除合同
     *
     * @param contractIds 合同ID列表
     * @return 影响行数
     */
    int deleteCloudContract(List<Long> contractIds);

    /**
     * 获取合同统计数据
     *
     * @param orgId 组织ID
     * @return 统计数据
     */
    Map<String, Object> getCloudContractStatistics(Long orgId);

    /**
     * 获取即将到期的合同
     *
     * @param days 天数
     * @return 合同列表
     */
    List<TblCloudConnectionContract> getExpiringContracts(Integer days);

    /**
     * 启用/禁用合同
     *
     * @param contractIds 合同ID列表
     * @param enabled 是否启用
     * @return 影响行数
     */
    int toggleEnabled(List<Long> contractIds, Integer enabled);

    /**
     * 变更合同状态
     *
     * @param contractId 合同ID
     * @param status 状态
     * @param reason 原因
     * @return 影响行数
     */
    int changeContractStatus(Long contractId, String status, String reason);

    /**
     * 获取续约提醒列表
     *
     * @return 需要续约的合同列表
     */
    List<TblCloudConnectionContract> getRenewalAlerts();

    /**
     * 导出合同列表
     *
     * @param params 查询参数
     * @return 合同列表
     */
    List<TblCloudConnectionContract> exportContracts(Map<String, Object> params);
}
