package com.financial.sharing.service;

import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.vo.param.FixedAssetDepreciationQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 固定资产折旧服务接口
 * @author system
 * @since 2026-01-22
 */
public interface FixedAssetDepreciationService {

    /**
     * 获取折旧统计数据
     * @param tenantId 租户ID
     * @param period 期间
     * @return 统计数据
     */
    MyJsonBean<Map<String, Object>> getDepreciationStats(Long tenantId, String period);

    /**
     * 分页查询折旧明细列表
     * @param param 查询参数
     * @return 折旧明细列表
     */
    MyJsonBean getDepreciationDetailList(FixedAssetDepreciationQueryParam param);

    /**
     * 计提折旧
     * @param period 期间
     * @param categoryId 资产类别ID（可选）
     * @param calculateType 计提方式
     * @param tenantId 租户ID
     * @param operatorId 操作人ID
     * @return 计提结果
     */
    MyJsonBean calculateDepreciation(String period, String categoryId, String calculateType, 
                                     Long tenantId, String operatorId);

    /**
     * 预览折旧
     * @param period 期间
     * @param categoryId 资产类别ID（可选）
     * @param tenantId 租户ID
     * @return 预览数据
     */
    MyJsonBean previewDepreciation(String period, String categoryId, Long tenantId);

    /**
     * 调整折旧
     * @param depreciationId 折旧ID
     * @param adjustAmount 调整金额
     * @param adjustReason 调整原因
     * @param operatorId 操作人ID
     * @return 调整结果
     */
    MyJsonBean adjustDepreciation(String depreciationId, java.math.BigDecimal adjustAmount, 
                                  String adjustReason, String operatorId);

    /**
     * 获取折旧趋势数据
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @param tenantId 租户ID
     * @return 趋势数据
     */
    MyJsonBean<List<Map<String, Object>>> getDepreciationTrend(String startPeriod, String endPeriod, Long tenantId);

    /**
     * 获取资产类别折旧分布
     * @param period 期间
     * @param tenantId 租户ID
     * @return 分布数据
     */
    MyJsonBean<List<Map<String, Object>>> getCategoryDistribution(String period, Long tenantId);

    /**
     * 根据ID查询折旧详情
     * @param depreciationId 折旧ID
     * @return 折旧详情
     */
    MyJsonBean getDepreciationById(String depreciationId);

    /**
     * 删除折旧记录
     * @param depreciationId 折旧ID
     * @return 删除结果
     */
    MyJsonBean deleteDepreciation(String depreciationId);
}

