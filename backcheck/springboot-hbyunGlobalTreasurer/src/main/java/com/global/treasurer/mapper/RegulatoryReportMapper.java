package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblRegulatoryReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 监管报告Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Mapper
public interface RegulatoryReportMapper extends BaseMapper<TblRegulatoryReport> {

    /**
     * 分页查询监管报告列表
     *
     * @param params 查询参数
     * @return 监管报告列表
     */
    List<TblRegulatoryReport> selectReportList(Map<String, Object> params);

    /**
     * 根据ID查询监管报告详情
     *
     * @param reportId 报告ID
     * @return 监管报告
     */
    TblRegulatoryReport selectReportById(@Param("reportId") String reportId);

    /**
     * 根据报告编号查询
     *
     * @param reportNo 报告编号
     * @return 监管报告
     */
    TblRegulatoryReport selectByReportNo(@Param("reportNo") String reportNo);

    /**
     * 查询逾期报告
     *
     * @return 监管报告列表
     */
    List<TblRegulatoryReport> selectOverdueReports();

    /**
     * 查询即将到期的报告
     *
     * @param days 天数
     * @return 监管报告列表
     */
    List<TblRegulatoryReport> selectDueSoonReports(@Param("days") Integer days);

    /**
     * 查询需要关注的报告
     *
     * @return 监管报告列表
     */
    List<TblRegulatoryReport> selectReportsNeedingAttention();

    /**
     * 批量删除监管报告（逻辑删除）
     *
     * @param reportIds 报告ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("reportIds") List<String> reportIds);

    /**
     * 更新报告状态
     *
     * @param reportId 报告ID
     * @param status 状态
     * @return 影响行数
     */
    int updateReportStatus(@Param("reportId") String reportId, @Param("status") String status);

    /**
     * 获取报告统计信息
     *
     * @param companyId 公司ID
     * @return 统计信息
     */
    Map<String, Object> selectReportStatistics(@Param("companyId") String companyId);
}

