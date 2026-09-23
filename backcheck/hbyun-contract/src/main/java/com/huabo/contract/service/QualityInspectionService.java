package com.huabo.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.contract.entity.QualityInspection;
import com.huabo.contract.vo.QualityInspectionQueryParam;

import java.util.Date;
import java.util.List;

/**
 * 质量检查管理服务接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface QualityInspectionService extends IService<QualityInspection> {

    /**
     * 分页查询质量检查记录
     */
    IPage<QualityInspection> getQualityInspectionPage(QualityInspectionQueryParam queryParam);

    /**
     * 根据项目ID查询质量检查记录
     */
    List<QualityInspection> getByProjectId(Long projectId);

    /**
     * 根据检查类型查询质量检查记录
     */
    List<QualityInspection> getByInspectionType(Integer inspectionType);

    /**
     * 根据检查阶段查询质量检查记录
     */
    List<QualityInspection> getByInspectionStage(Integer inspectionStage);

    /**
     * 根据检查结果查询质量检查记录
     */
    List<QualityInspection> getByCheckResult(Integer checkResult);

    /**
     * 根据检查负责人ID查询质量检查记录
     */
    List<QualityInspection> getByInspectorLeaderId(Long inspectorLeaderId);

    /**
     * 根据被检查单位查询质量检查记录
     */
    List<QualityInspection> getByInspectedUnit(String inspectedUnit);

    /**
     * 根据整改状态查询质量检查记录
     */
    List<QualityInspection> getByRectificationStatus(Integer rectificationStatus);

    /**
     * 根据整改负责人ID查询质量检查记录
     */
    List<QualityInspection> getByRectificationResponsibleId(Long rectificationResponsibleId);

    /**
     * 根据复查结果查询质量检查记录
     */
    List<QualityInspection> getByRecheckResult(Integer recheckResult);

    /**
     * 根据质量等级查询质量检查记录
     */
    List<QualityInspection> getByQualityGrade(Integer qualityGrade);

    /**
     * 根据创建人查询质量检查记录
     */
    List<QualityInspection> getByCreateBy(Long createBy);

    /**
     * 根据检查结果统计数量
     */
    Integer countByCheckResult(Integer checkResult);

    /**
     * 根据整改状态统计数量
     */
    Integer countByRectificationStatus(Integer rectificationStatus);

    /**
     * 根据质量等级统计数量
     */
    Integer countByQualityGrade(Integer qualityGrade);

    /**
     * 查询不合格的质量检查记录
     */
    List<QualityInspection> getUnqualified();

    /**
     * 查询有严重问题的质量检查记录
     */
    List<QualityInspection> getWithSeriousIssues();

    /**
     * 查询待整改的质量检查记录
     */
    List<QualityInspection> getPendingRectification();

    /**
     * 查询整改逾期的质量检查记录
     */
    List<QualityInspection> getRectificationOverdue();

    /**
     * 查询优良等级的质量检查记录
     */
    List<QualityInspection> getExcellentGrade();

    /**
     * 批量更新整改状态
     */
    Integer batchUpdateRectificationStatus(List<Long> ids, Integer rectificationStatus, Long updateBy);

    /**
     * 批量更新检查结果
     */
    Integer batchUpdateCheckResult(List<Long> ids, Integer checkResult, Long updateBy);

    /**
     * 根据日期范围查询质量检查记录
     */
    List<QualityInspection> getByDateRange(Date startDate, Date endDate);

    /**
     * 根据得分范围查询质量检查记录
     */
    List<QualityInspection> getByScoreRange(Integer minScore, Integer maxScore);

    /**
     * 查询质量检查统计信息
     */
    List<QualityInspection> getQualityInspectionStatistics();

    /**
     * 根据关键词搜索质量检查记录
     */
    List<QualityInspection> searchByKeyword(String keyword, Integer limit);

    /**
     * 根据项目ID统计平均得分
     */
    Double calculateAverageScore(Long projectId);

    /**
     * 根据项目ID统计合格率
     */
    Double calculatePassRate(Long projectId);

    /**
     * 查询即将到期的整改任务
     */
    List<QualityInspection> getUpcomingRectificationDeadlines(Integer days);

    /**
     * 质量问题整改
     */
    Boolean rectifyIssues(Long id, String rectificationDescription, Long rectificationResponsibleId, Long updateBy);

    /**
     * 整改验证
     */
    Boolean verifyRectification(Long id, Integer recheckResult, String recheckComments, Long updateBy);

    /**
     * 生成检查编号
     */
    String generateInspectionNo();

    /**
     * 验证质量检查信息
     */
    Boolean validateInspectionInfo(QualityInspection qualityInspection);

    /**
     * 获取项目质量检查报告
     */
    List<String> getProjectQualityReport(Long projectId);
}
