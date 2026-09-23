package com.financial.sharing.enterpriseReport.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.enterpriseReport.dto.IndicatorQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblIndicatorInfo;

import java.util.List;

/**
 * 指标信息服务接口
 * 
 * @author system
 * @since 2026-01-30
 */
public interface IndicatorService {

    /**
     * 分页查询指标列表
     */
    Page<TblIndicatorInfo> getPage(IndicatorQueryParam param);

    /**
     * 查询指标详情
     */
    TblIndicatorInfo getDetail(String indicatorId);

    /**
     * 保存指标
     */
    boolean save(TblIndicatorInfo indicator);

    /**
     * 删除指标
     */
    boolean delete(String indicatorId);

    /**
     * 批量删除指标
     */
    boolean batchDelete(List<String> indicatorIds);

    /**
     * 更新指标状态
     */
    boolean updateStatus(String indicatorId, String status);

    /**
     * 检查编码是否存在
     */
    boolean checkCodeExists(String indicatorCode, String excludeId);
}

