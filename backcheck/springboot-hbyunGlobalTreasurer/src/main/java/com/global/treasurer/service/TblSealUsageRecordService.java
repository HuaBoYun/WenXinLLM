package com.global.treasurer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblSealUsageRecord;

import java.util.List;
import java.util.Map;

/**
 * 印鉴使用记录Service接口
 * 基于 01_create_seal_usage_record.sql 表结构
 *
 * @author 华博云开发团队
 * @since 2024-12-23
 */
public interface TblSealUsageRecordService extends IService<TblSealUsageRecord> {

    /**
     * 分页查询印鉴使用记录
     *
     * @param page 页码
     * @param limit 每页数量
     * @param recordNumber 记录编号
     * @param sealCode 印鉴编码
     * @param sealName 印鉴名称
     * @param operatorName 使用人员姓名
     * @param businessType 业务类型
     * @param usageStatus 使用状态
     * @return 分页结果
     */
    IPage<TblSealUsageRecord> getSealUsageRecordPage(Integer page, Integer limit,
                                                     String recordNumber,
                                                     String sealCode, String sealName,
                                                     String operatorName, String businessType,
                                                     String usageStatus);

    /**
     * 根据ID查询记录
     */
    TblSealUsageRecord getByRecordId(Long id);

    /**
     * 新增印鉴使用记录
     */
    boolean saveRecord(TblSealUsageRecord record);

    /**
     * 更新印鉴使用记录
     */
    boolean updateRecord(TblSealUsageRecord record);

    /**
     * 删除印鉴使用记录
     */
    boolean deleteByRecordId(Long id);

    /**
     * 批量删除
     */
    boolean deleteBatchByIds(List<Long> ids);

    /**
     * 获取统计信息
     */
    Map<String, Object> getStatistics();

    /**
     * 条件查询
     */
    List<TblSealUsageRecord> queryByCondition(Map<String, Object> params);

    /**
     * 审批记录
     */
    boolean approveRecord(Long id, String approver, String status);
}
