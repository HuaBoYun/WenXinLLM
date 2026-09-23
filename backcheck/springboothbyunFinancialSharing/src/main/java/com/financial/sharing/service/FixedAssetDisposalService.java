package com.financial.sharing.service;

import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.FixedAssetDisposalQueryParam;
import com.financial.sharing.vo.param.FixedAssetDisposalSaveParam;

import java.util.List;
import java.util.Map;

/**
 * 固定资产处置服务接口
 * @author system
 * @since 2026-01-23
 */
public interface FixedAssetDisposalService {

    /**
     * 分页查询资产处置列表
     * @param param 查询参数
     * @return 处置列表
     */
    MyJsonBean<PageResult> getDisposalList(FixedAssetDisposalQueryParam param);

    /**
     * 根据ID查询资产处置详情
     * @param disposalId 处置ID
     * @param tenantId 租户ID
     * @return 处置详情
     */
    MyJsonBean getDisposalById(String disposalId, Long tenantId);

    /**
     * 保存或更新资产处置
     * @param param 保存参数
     * @return 处置ID
     */
    MyJsonBean saveOrUpdateDisposal(FixedAssetDisposalSaveParam param);

    /**
     * 删除资产处置
     * @param disposalId 处置ID
     * @param tenantId 租户ID
     * @return 删除结果
     */
    MyJsonBean deleteDisposal(String disposalId, Long tenantId);

    /**
     * 批量删除资产处置
     * @param disposalIds 处置ID列表
     * @param tenantId 租户ID
     * @return 删除结果
     */
    MyJsonBean batchDeleteDisposal(List<String> disposalIds, Long tenantId);

    /**
     * 审批资产处置
     * @param disposalId 处置ID
     * @param approved 是否通过
     * @param comment 审批意见
     * @param operatorId 操作人ID
     * @param tenantId 租户ID
     * @return 审批结果
     */
    MyJsonBean approveDisposal(String disposalId, Boolean approved, String comment, String operatorId, Long tenantId);

    /**
     * 批量审批资产处置
     * @param disposalIds 处置ID列表
     * @param approved 是否通过
     * @param comment 审批意见
     * @param operatorId 操作人ID
     * @param tenantId 租户ID
     * @return 审批结果
     */
    MyJsonBean batchApproveDisposal(List<String> disposalIds, Boolean approved, String comment, String operatorId, Long tenantId);

    /**
     * 执行资产处置
     * @param disposalId 处置ID
     * @param operatorId 操作人ID
     * @param tenantId 租户ID
     * @return 执行结果
     */
    MyJsonBean executeDisposal(String disposalId, String operatorId, Long tenantId);

    /**
     * 获取处置统计数据
     * @param tenantId 租户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据
     */
    MyJsonBean<Map<String, Object>> getDisposalStats(Long tenantId, String startDate, String endDate);

    /**
     * 导出处置列表
     * @param param 查询参数
     * @return 导出文件路径
     */
    MyJsonBean exportDisposalList(FixedAssetDisposalQueryParam param);

    /**
     * 生成处置报告
     * @param tenantId 租户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 报告文件路径
     */
    MyJsonBean generateDisposalReport(Long tenantId, String startDate, String endDate);
}

