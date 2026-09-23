package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.FinancialReportEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 财务报表Mapper接口
 * 
 * @author Financial Sharing System
 * @since 2024-01-01
 */
public interface FinancialReportMapper extends BaseMapper<FinancialReportEntity> {

    /**
     * 分页查询财务报表列表
     * 
     * @param page 分页对象
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<Map<String, Object>> selectFinancialReportPage(Page<Map<String, Object>> page, @Param("param") Map<String, Object> param);

    /**
     * 查询财务报表详情
     * 
     * @param reportId 报表ID
     * @return 报表详情
     */
    Map<String, Object> selectFinancialReportDetail(@Param("reportId") Long reportId);

    /**
     * 查询报表统计信息
     * 
     * @param param 查询参数
     * @return 统计信息
     */
    Map<String, Object> selectReportStatistics(@Param("param") Map<String, Object> param);

    /**
     * 检查报表编码是否存在
     * 
     * @param reportCode 报表编码
     * @param reportId 报表ID（更新时排除自己）
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 存在数量
     */
    int checkReportCodeExists(@Param("reportCode") String reportCode, 
                             @Param("reportId") Long reportId,
                             @Param("bookId") Long bookId,
                             @Param("tenantId") Long tenantId);
}

