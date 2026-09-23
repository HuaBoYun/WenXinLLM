package com.huabo.contract.service;

import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.AssessmentIndicator;
import com.huabo.contract.entity.ProjectAssessment;
import com.huabo.contract.vo.AssessmentQueryParam;
import com.huabo.contract.vo.AssessmentRequest;
import com.huabo.contract.vo.AssessmentReviewRequest;

import java.util.List;
import java.util.Map;

/**
 * 项目考核服务接口
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
public interface AssessmentService {

    /**
     * 启动项目考核
     *
     * @param request 考核请求参数
     * @return 考核结果
     */
    ProjectAssessment startAssessment(AssessmentRequest request);

    /**
     * 查询考核结果列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<ProjectAssessment> getAssessmentList(AssessmentQueryParam param);

    /**
     * 根据项目ID查询考核结果
     *
     * @param projectId 项目ID
     * @param period 考核期间
     * @return 考核结果列表
     */
    List<ProjectAssessment> getAssessmentResultsByProject(String projectId, String period);

    /**
     * 获取考核详情
     *
     * @param assessmentId 考核ID
     * @return 考核详情
     */
    Map<String, Object> getAssessmentDetail(Long assessmentId);

    /**
     * 生成考核报告
     *
     * @param assessmentId 考核ID
     * @param reportType 报告类型
     * @param includeChart 是否包含图表
     * @param includeSuggestions 是否包含建议
     * @return 报告数据
     */
    Map<String, Object> generateAssessmentReport(Long assessmentId, String reportType, 
                                                Boolean includeChart, Boolean includeSuggestions);

    /**
     * 审核考核结果
     *
     * @param assessmentId 考核ID
     * @param request 审核请求参数
     * @return 审核结果
     */
    ProjectAssessment reviewAssessment(Long assessmentId, AssessmentReviewRequest request);

    /**
     * 获取考核指标体系
     *
     * @param isActive 是否启用
     * @param parentId 父指标ID
     * @return 指标列表
     */
    List<AssessmentIndicator> getIndicators(Boolean isActive, Long parentId);

    /**
     * 获取指标树
     * @return 指标树列表
     */
    List<AssessmentIndicator> getIndicatorTree();

    /**
     * 保存考核指标
     *
     * @param indicator 指标信息
     */
    void saveIndicator(AssessmentIndicator indicator);

    /**
     * 更新指标
     * @param indicator 指标信息
     */
    void updateIndicator(AssessmentIndicator indicator);

    /**
     * 删除考核指标
     *
     * @param indicatorId 指标ID
     */
    void deleteIndicator(Long indicatorId);

    /**
     * 更新指标状态
     * @param id 指标ID
     * @param status 状态
     */
    void updateIndicatorStatus(Long id, Integer status);

    /**
     * 获取考核统计概览
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param deptId 部门ID
     * @return 统计数据
     */
    Map<String, Object> getAssessmentStatistics(String startDate, String endDate, Long deptId);

    /**
     * 获取项目考核排名
     *
     * @param period 考核期间
     * @param assessmentType 考核类型
     * @param limit 返回数量
     * @return 排名列表
     */
    List<Map<String, Object>> getAssessmentRanking(String period, Integer assessmentType, Integer limit);

    /**
     * 获取指标分析报告
     *
     * @param period 考核期间
     * @param indicatorId 指标ID
     * @return 分析数据
     */
    List<Map<String, Object>> getIndicatorAnalysis(String period, Long indicatorId);

    /**
     * 同步考核数据
     *
     * @param projectId 项目ID
     * @param period 同步期间
     * @param modules 同步模块
     * @param forceSync 是否强制同步
     * @return 同步结果
     */
    Map<String, Object> syncAssessmentData(String projectId, String period, 
                                          List<String> modules, Boolean forceSync);

    /**
     * 导出考核结果
     *
     * @param projectIds 项目ID列表
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param exportFormat 导出格式
     * @param includeDetails 是否包含明细
     * @return 导出结果
     */
    Map<String, Object> exportAssessmentResults(List<String> projectIds, String startDate, String endDate,
                                               String exportFormat, Boolean includeDetails);

}
