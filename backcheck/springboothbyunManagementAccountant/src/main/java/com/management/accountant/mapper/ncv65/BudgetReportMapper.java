package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算报告数据访问接口
 * 
 * @description 预算报告数据访问层，提供预算报告的数据库操作方法
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetReportMapper extends BaseMapper<BudgetReport> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据报告编码查询
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE REPORT_CODE = #{reportCode} AND IS_DELETED = 0")
    BudgetReport selectByReportCode(@Param("reportCode") String reportCode);

    /**
     * 根据报告类型查询报告列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE REPORT_TYPE = #{reportType} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetReport> selectByReportType(@Param("reportType") String reportType);

    /**
     * 根据报告分类查询报告列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE REPORT_CATEGORY = #{reportCategory} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetReport> selectByReportCategory(@Param("reportCategory") String reportCategory);

    /**
     * 根据模板ID查询报告列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE TEMPLATE_ID = #{templateId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetReport> selectByTemplateId(@Param("templateId") String templateId);

    /**
     * 根据预算年度查询报告列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE FISCAL_YEAR = #{fiscalYear} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetReport> selectByFiscalYear(@Param("fiscalYear") Integer fiscalYear);

    /**
     * 根据报告期间查询报告列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE REPORT_PERIOD = #{reportPeriod} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetReport> selectByReportPeriod(@Param("reportPeriod") String reportPeriod);

    /**
     * 根据组织ID查询报告列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE ORGANIZATION_ID = #{organizationId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetReport> selectByOrganizationId(@Param("organizationId") String organizationId);

    /**
     * 根据报告状态查询报告列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE REPORT_STATUS = #{reportStatus} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetReport> selectByReportStatus(@Param("reportStatus") String reportStatus);

    /**
     * 根据生成状态查询报告列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE GENERATION_STATUS = #{generationStatus} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetReport> selectByGenerationStatus(@Param("generationStatus") String generationStatus);

    /**
     * 根据审批状态查询报告列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE APPROVAL_STATUS = #{approvalStatus} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetReport> selectByApprovalStatus(@Param("approvalStatus") String approvalStatus);

    /**
     * 根据发布状态查询报告列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE PUBLISH_STATUS = #{publishStatus} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetReport> selectByPublishStatus(@Param("publishStatus") String publishStatus);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询预算报告
     */
    IPage<BudgetReport> selectBudgetReportPage(Page<BudgetReport> page, @Param("params") Map<String, Object> params);

    /**
     * 查询我创建的报告列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE CREATE_BY = #{userId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetReport> selectMyReports(@Param("userId") String userId);

    /**
     * 查询待我审批的报告列表
     */
    List<BudgetReport> selectPendingApprovals(@Param("userId") String userId);

    /**
     * 查询待生成的报告列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE GENERATION_STATUS = 'PENDING' AND IS_DELETED = 0 ORDER BY CREATE_TIME")
    List<BudgetReport> selectPendingGeneration();

    /**
     * 查询生成中的报告列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE GENERATION_STATUS = 'GENERATING' AND IS_DELETED = 0 ORDER BY CREATE_TIME")
    List<BudgetReport> selectGeneratingReports();

    /**
     * 查询生成失败的报告列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE GENERATION_STATUS = 'FAILED' AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetReport> selectFailedReports();

    /**
     * 查询已发布的报告列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE PUBLISH_STATUS = 'PUBLISHED' AND IS_DELETED = 0 ORDER BY PUBLISH_TIME DESC")
    List<BudgetReport> selectPublishedReports();

    /**
     * 查询自动生成的报告列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE IS_AUTO_GENERATION = 1 AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetReport> selectAutoGenerationReports();

    /**
     * 查询定期生成的报告列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE GENERATION_FREQUENCY != 'MANUAL' AND REPORT_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY NEXT_GENERATION_TIME")
    List<BudgetReport> selectPeriodicReports();

    /**
     * 查询需要生成的报告列表
     */
    List<BudgetReport> selectReportsToGenerate();

    /**
     * 查询报告统计信息
     */
    Map<String, Object> selectReportStatistics(@Param("fiscalYear") Integer fiscalYear);

    /**
     * 根据关键字搜索报告
     */
    List<BudgetReport> searchReports(@Param("keyword") String keyword);

    /**
     * 查询热门报告
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE DOWNLOAD_COUNT > 0 AND PUBLISH_STATUS = 'PUBLISHED' AND IS_DELETED = 0 ORDER BY DOWNLOAD_COUNT DESC LIMIT #{limit}")
    List<BudgetReport> selectPopularReports(@Param("limit") Integer limit);

    /**
     * 查询最新报告
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE PUBLISH_STATUS = 'PUBLISHED' AND IS_DELETED = 0 ORDER BY PUBLISH_TIME DESC LIMIT #{limit}")
    List<BudgetReport> selectLatestReports(@Param("limit") Integer limit);

    /**
     * 查询大文件报告
     */
    @Select("SELECT * FROM NCV65_BUDGET_REPORT WHERE FILE_SIZE > #{sizeThreshold} AND IS_DELETED = 0 ORDER BY FILE_SIZE DESC")
    List<BudgetReport> selectLargeFileReports(@Param("sizeThreshold") Long sizeThreshold);

    // ==================== 业务操作方法 ====================

    /**
     * 开始生成报告
     */
    int startGeneration(@Param("reportId") String reportId, @Param("updateBy") String updateBy);

    /**
     * 完成报告生成
     */
    int completeGeneration(@Param("reportId") String reportId, 
                          @Param("filePath") String filePath,
                          @Param("fileName") String fileName,
                          @Param("fileSize") Long fileSize,
                          @Param("updateBy") String updateBy);

    /**
     * 报告生成失败
     */
    int failGeneration(@Param("reportId") String reportId, @Param("errorMessage") String errorMessage, @Param("updateBy") String updateBy);

    /**
     * 审批通过
     */
    int approveReport(@Param("reportId") String reportId, @Param("approvedBy") String approvedBy, @Param("approvalComments") String approvalComments);

    /**
     * 审批拒绝
     */
    int rejectReport(@Param("reportId") String reportId, @Param("approvedBy") String approvedBy, @Param("approvalComments") String approvalComments);

    /**
     * 发布报告
     */
    int publishReport(@Param("reportId") String reportId, @Param("publishedBy") String publishedBy);

    /**
     * 取消发布
     */
    int unpublishReport(@Param("reportId") String reportId, @Param("updateBy") String updateBy);

    /**
     * 归档报告
     */
    int archiveReport(@Param("reportId") String reportId, @Param("updateBy") String updateBy);

    /**
     * 增加下载次数
     */
    int incrementDownloadCount(@Param("reportId") String reportId);

    /**
     * 增加查看次数
     */
    int incrementViewCount(@Param("reportId") String reportId);

    /**
     * 批量更新报告状态
     */
    int batchUpdateReportStatus(@Param("reportIds") List<String> reportIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量审批报告
     */
    int batchApproveReports(@Param("reportIds") List<String> reportIds, @Param("approvedBy") String approvedBy, @Param("approvalComments") String approvalComments);

    /**
     * 批量发布报告
     */
    int batchPublishReports(@Param("reportIds") List<String> reportIds, @Param("publishedBy") String publishedBy);

    /**
     * 批量归档报告
     */
    int batchArchiveReports(@Param("reportIds") List<String> reportIds, @Param("updateBy") String updateBy);

    // ==================== 统计分析方法 ====================

    /**
     * 统计报告总数
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_REPORT WHERE IS_DELETED = 0")
    int countTotalReports();

    /**
     * 按报告类型统计数量
     */
    List<Map<String, Object>> countReportsByType();

    /**
     * 按报告分类统计数量
     */
    List<Map<String, Object>> countReportsByCategory();

    /**
     * 按报告状态统计数量
     */
    List<Map<String, Object>> countReportsByStatus();

    /**
     * 按生成状态统计数量
     */
    List<Map<String, Object>> countReportsByGenerationStatus();

    /**
     * 按审批状态统计数量
     */
    List<Map<String, Object>> countReportsByApprovalStatus();

    /**
     * 按发布状态统计数量
     */
    List<Map<String, Object>> countReportsByPublishStatus();

    /**
     * 按预算年度统计数量
     */
    List<Map<String, Object>> countReportsByFiscalYear();

    /**
     * 按组织统计数量
     */
    List<Map<String, Object>> countReportsByOrganization();

    /**
     * 统计文件大小汇总
     */
    Map<String, Object> sumReportFileSize();

    /**
     * 统计下载次数汇总
     */
    Map<String, Object> sumReportDownloadCount();

    /**
     * 统计查看次数汇总
     */
    Map<String, Object> sumReportViewCount();

    /**
     * 统计自动生成报告数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_REPORT WHERE IS_AUTO_GENERATION = 1 AND IS_DELETED = 0")
    int countAutoGenerationReports();

    /**
     * 统计定期生成报告数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_REPORT WHERE GENERATION_FREQUENCY != 'MANUAL' AND IS_DELETED = 0")
    int countPeriodicReports();

    // ==================== 数据验证方法 ====================

    /**
     * 检查报告编码是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_REPORT WHERE REPORT_CODE = #{reportCode} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkReportCodeExists(@Param("reportCode") String reportCode, @Param("excludeId") String excludeId);

    /**
     * 检查报告名称是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_REPORT WHERE REPORT_NAME = #{reportName} AND FISCAL_YEAR = #{fiscalYear} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkReportNameExists(@Param("reportName") String reportName, @Param("fiscalYear") Integer fiscalYear, @Param("excludeId") String excludeId);

    /**
     * 检查报告是否可以删除
     */
    boolean checkReportCanDelete(@Param("reportId") String reportId);

    /**
     * 检查报告是否可以修改
     */
    boolean checkReportCanModify(@Param("reportId") String reportId);

    /**
     * 检查报告是否在使用中
     */
    boolean checkReportInUse(@Param("reportId") String reportId);

    /**
     * 检查报告权限
     */
    boolean checkReportPermission(@Param("reportId") String reportId, @Param("userId") String userId, @Param("operation") String operation);

    /**
     * 验证报告数据源
     */
    boolean validateReportDataSource(@Param("dataSource") String dataSource);

    /**
     * 检查报告文件是否存在
     */
    boolean checkReportFileExists(@Param("filePath") String filePath);

    /**
     * 验证报告格式
     */
    boolean validateReportFormat(@Param("reportFormat") String reportFormat);

    /**
     * 检查报告生成冲突
     */
    boolean checkReportGenerationConflict(@Param("reportId") String reportId, @Param("generationTime") LocalDateTime generationTime);
}
