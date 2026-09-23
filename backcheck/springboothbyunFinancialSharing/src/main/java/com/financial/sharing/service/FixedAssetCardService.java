package com.financial.sharing.service;

import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.FixedAssetCardQueryParam;
import com.financial.sharing.vo.param.FixedAssetCardSaveParam;

import java.util.List;
import java.util.Map;

/**
 * 固定资产卡片服务接口
 * @author system
 * @since 2026-01-21
 */
public interface FixedAssetCardService {

    /**
     * 分页查询资产卡片列表
     */
    MyJsonBean<PageResult> getAssetCardList(FixedAssetCardQueryParam param);

    /**
     * 根据ID查询资产卡片详情
     */
    MyJsonBean getAssetCardById(String assetId);

    /**
     * 保存或更新资产卡片
     */
    MyJsonBean saveOrUpdateAssetCard(FixedAssetCardSaveParam param);

    /**
     * 删除资产卡片
     */
    MyJsonBean deleteAssetCard(String assetId);

    /**
     * 批量删除资产卡片
     */
    MyJsonBean batchDeleteAssetCard(List<String> assetIds);

    /**
     * 批量更新资产状态
     */
    MyJsonBean batchUpdateStatus(List<String> assetIds, String status);

    /**
     * 查询资产汇总信息
     */
    MyJsonBean<Map<String, Object>> getAssetSummary(Long tenantId);

    /**
     * 按类别统计资产
     */
    MyJsonBean<List<Map<String, Object>>> getAssetByCategory(Long tenantId);

    /**
     * 检查资产编码是否存在
     */
    MyJsonBean checkAssetCodeExists(String assetCode, String assetId, Long tenantId);

    /**
     * 导出资产卡片
     */
    MyJsonBean exportAssetCard(FixedAssetCardQueryParam param);

    /**
     * 导入资产卡片
     */
    MyJsonBean importAssetCard(List<FixedAssetCardSaveParam> dataList);
}

