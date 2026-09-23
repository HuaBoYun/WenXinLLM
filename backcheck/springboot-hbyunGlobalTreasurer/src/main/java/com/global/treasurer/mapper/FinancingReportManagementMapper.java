package com.global.treasurer.mapper;

import com.global.treasurer.entity.TblReportRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 融资报表管理Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
@Mapper
public interface FinancingReportManagementMapper {

    /**
     * 插入报表记录
     *
     * @param record 报表记录实体
     * @return 影响行数
     */
    int insert(TblReportRecord record);

    /**
     * 更新报表记录
     *
     * @param record 报表记录实体
     * @return 影响行数
     */
    int updateById(TblReportRecord record);

    /**
     * 根据ID查询报表记录
     *
     * @param recordId 记录ID
     * @return 报表记录
     */
    TblReportRecord selectReportById(@Param("recordId") Long recordId);

    /**
     * 分页查询报表列表
     *
     * @param params 查询参数
     * @return 报表记录列表
     */
    List<TblReportRecord> selectReportList(Map<String, Object> params);

    /**
     * 查询报表模板列表
     *
     * @return 模板列表
     */
    List<Map<String, Object>> selectTemplateList();

    /**
     * 批量删除报表记录
     *
     * @param recordIds 记录ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("recordIds") List<Long> recordIds);

    /**
     * 查询报表统计信息
     *
     * @param companyId 公司ID
     * @return 统计信息
     */
    Map<String, Object> selectReportStatistics(@Param("companyId") Long companyId);

    /**
     * 更新生成状态
     *
     * @param recordId 记录ID
     * @param status 状态
     * @return 影响行数
     */
    int updateGenerationStatus(@Param("recordId") Long recordId, @Param("status") String status);

    /**
     * 查询最新生成的报表
     *
     * @param companyId 公司ID
     * @param reportType 报表类型
     * @param limit 限制数量
     * @return 报表记录列表
     */
    List<TblReportRecord> selectLatestReports(@Param("companyId") Long companyId,
                                              @Param("reportType") String reportType,
                                              @Param("limit") Integer limit);
}
