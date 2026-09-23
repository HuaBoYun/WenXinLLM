package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.contract.entity.QualityInspection;
import com.huabo.contract.vo.QualityInspectionQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 质量检查管理Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface QualityInspectionMapper extends BaseMapper<QualityInspection> {

    /**
     * 分页查询质量检查记录
     */
    IPage<QualityInspection> selectQualityInspectionPage(Page<QualityInspection> page, @Param("param") QualityInspectionQueryParam param);

    /**
     * 根据项目ID查询质量检查记录
     */
    List<QualityInspection> selectByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据检查类型查询质量检查记录
     */
    List<QualityInspection> selectByInspectionType(@Param("inspectionType") Integer inspectionType);

    /**
     * 根据检查阶段查询质量检查记录
     */
    List<QualityInspection> selectByInspectionStage(@Param("inspectionStage") Integer inspectionStage);

    /**
     * 根据检查结果查询质量检查记录
     */
    List<QualityInspection> selectByCheckResult(@Param("checkResult") Integer checkResult);

    /**
     * 根据检查负责人ID查询质量检查记录
     */
    List<QualityInspection> selectByInspectorLeaderId(@Param("inspectorLeaderId") Long inspectorLeaderId);

    /**
     * 根据被检查单位查询质量检查记录
     */
    List<QualityInspection> selectByInspectedUnit(@Param("inspectedUnit") String inspectedUnit);

    /**
     * 根据整改状态查询质量检查记录
     */
    List<QualityInspection> selectByRectificationStatus(@Param("rectificationStatus") Integer rectificationStatus);

    /**
     * 根据整改负责人ID查询质量检查记录
     */
    List<QualityInspection> selectByRectificationResponsibleId(@Param("rectificationResponsibleId") Long rectificationResponsibleId);

    /**
     * 根据复查结果查询质量检查记录
     */
    List<QualityInspection> selectByRecheckResult(@Param("recheckResult") Integer recheckResult);

    /**
     * 根据质量等级查询质量检查记录
     */
    List<QualityInspection> selectByQualityGrade(@Param("qualityGrade") Integer qualityGrade);

    /**
     * 根据创建人查询质量检查记录
     */
    List<QualityInspection> selectByCreateBy(@Param("createBy") Long createBy);

    /**
     * 根据检查结果统计数量
     */
    Integer countByCheckResult(@Param("checkResult") Integer checkResult);

    /**
     * 根据整改状态统计数量
     */
    Integer countByRectificationStatus(@Param("rectificationStatus") Integer rectificationStatus);

    /**
     * 根据质量等级统计数量
     */
    Integer countByQualityGrade(@Param("qualityGrade") Integer qualityGrade);

    /**
     * 查询不合格的质量检查记录
     */
    List<QualityInspection> selectUnqualified();

    /**
     * 查询有严重问题的质量检查记录
     */
    List<QualityInspection> selectWithSeriousIssues();

    /**
     * 查询待整改的质量检查记录
     */
    List<QualityInspection> selectPendingRectification();

    /**
     * 查询整改逾期的质量检查记录
     */
    List<QualityInspection> selectRectificationOverdue();

    /**
     * 查询优良等级的质量检查记录
     */
    List<QualityInspection> selectExcellentGrade();

    /**
     * 批量更新整改状态
     */
    Integer batchUpdateRectificationStatus(@Param("ids") List<Long> ids, 
                                         @Param("rectificationStatus") Integer rectificationStatus, 
                                         @Param("updateBy") Long updateBy);

    /**
     * 批量更新检查结果
     */
    Integer batchUpdateCheckResult(@Param("ids") List<Long> ids, 
                                 @Param("checkResult") Integer checkResult, 
                                 @Param("updateBy") Long updateBy);

    /**
     * 根据日期范围查询质量检查记录
     */
    List<QualityInspection> selectByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 根据得分范围查询质量检查记录
     */
    List<QualityInspection> selectByScoreRange(@Param("minScore") Integer minScore, @Param("maxScore") Integer maxScore);

    /**
     * 查询质量检查统计信息
     */
    List<QualityInspection> selectQualityInspectionStatistics();

    /**
     * 根据关键词搜索质量检查记录
     */
    List<QualityInspection> searchByKeyword(@Param("keyword") String keyword, @Param("limit") Integer limit);

    /**
     * 根据项目ID统计平均得分
     */
    Double calculateAverageScore(@Param("projectId") Long projectId);

    /**
     * 根据项目ID统计合格率
     */
    Double calculatePassRate(@Param("projectId") Long projectId);

    /**
     * 查询即将到期的整改任务
     */
    List<QualityInspection> selectUpcomingRectificationDeadlines(@Param("days") Integer days);
}
