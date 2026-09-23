package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.EstimationReportEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 估算报告Mapper接口
 *
 * @author Financial Sharing System
 * @since 2024-12-29
 */
public interface EstimationReportMapper extends BaseMapper<EstimationReportEntity> {

    /**
     * 分页查询估算报告列表
     *
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @param reportType 报告类型
     * @param reportPeriod 报告期间
     * @param reportStatus 报告状态
     * @param offset 偏移量
     * @param pageSize 每页数量
     * @return 报告列表
     */
    List<Map<String, Object>> selectReportListWithPagination(
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId,
            @Param("reportType") String reportType,
            @Param("reportPeriod") String reportPeriod,
            @Param("startMonth") String startMonth,
            @Param("endMonth") String endMonth,
            @Param("reportStatus") Integer reportStatus,
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize
    );

    /**
     * 查询总记录数
     *
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @param reportType 报告类型
     * @param reportPeriod 报告期间
     * @param reportStatus 报告状态
     * @return 总记录数
     */
    int countReportList(
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId,
            @Param("reportType") String reportType,
            @Param("reportPeriod") String reportPeriod,
            @Param("startMonth") String startMonth,
            @Param("endMonth") String endMonth,
            @Param("reportStatus") Integer reportStatus
    );

    /**
     * 根据报告编号查询报告
     *
     * @param reportNo 报告编号
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 报告信息
     */
    Map<String, Object> selectByReportNo(
            @Param("reportNo") String reportNo,
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId
    );

    /**
     * 根据报告ID查询完整报告（含 reportContent）
     */
    Map<String, Object> selectByReportId(@Param("reportId") String reportId);

    /**
     * 按报告ID更新非空字段
     */
    int updateByReportId(Map<String, Object> param);

    /**
     * 按报告ID物理删除
     */
    int deleteByReportId(@Param("reportId") String reportId);

    /**
     * 批量更新报告状态
     *
     * @param reportIds 报告ID列表
     * @param reportStatus 报告状态
     * @return 更新数量
     */
    int batchUpdateStatus(
            @Param("reportIds") List<String> reportIds,
            @Param("reportStatus") Integer reportStatus
    );

    /**
     * 获取报告统计数据
     *
     * @param reportPeriod 报告期间
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 统计数据
     */
    Map<String, Object> selectReportStatistics(
            @Param("reportPeriod") String reportPeriod,
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId
    );
}

