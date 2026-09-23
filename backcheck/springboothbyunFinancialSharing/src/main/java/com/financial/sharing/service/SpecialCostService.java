package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;
import com.financial.sharing.vo.param.SpecialCostParam;
import com.financial.sharing.vo.result.SpecialCostResult;

import java.util.List;
import java.util.Map;

/**
 * 专项成本服务接口
 * 
 * @author system
 * @date 2024-12-19
 */
public interface SpecialCostService {

    // ==================== 专项成本统计 ====================

    /**
     * 获取专项成本统计概览
     * @return 统计数据
     */
    Map<String, Object> getSpecialCostStats();

    // ==================== 项目成本 ====================

    /**
     * 分页查询项目成本列表
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<SpecialCostResult.ProjectCost> getProjectCostPage(PageableParam param);

    /**
     * 保存或更新项目成本
     * @param param 项目成本数据
     */
    void saveOrUpdateProjectCost(SpecialCostParam.ProjectCostSave param);

    /**
     * 获取项目成本详情
     * @param projectId 项目ID
     * @return 项目成本详情
     */
    SpecialCostResult.ProjectCostDetail getProjectCostById(Long projectId);

    /**
     * 删除项目成本
     * @param projectId 项目ID
     */
    void deleteProjectCost(Long projectId);

    /**
     * 批量删除项目成本
     * @param projectIds 项目ID列表
     */
    void batchDeleteProjectCost(List<Long> projectIds);

    /**
     * 获取项目成本分析数据
     * @param projectId 项目ID
     * @return 分析数据
     */
    SpecialCostResult.ProjectCostAnalysis getProjectCostAnalysis(Long projectId);

    // ==================== 作业成本 ====================

    /**
     * 分页查询作业成本列表
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<SpecialCostResult.ActivityCost> getActivityCostPage(PageableParam param);

    /**
     * 保存或更新作业成本
     * @param param 作业成本数据
     */
    void saveOrUpdateActivityCost(SpecialCostParam.ActivityCostSave param);

    /**
     * 获取作业成本详情
     * @param activityId 作业ID
     * @return 作业成本详情
     */
    SpecialCostResult.ActivityCostDetail getActivityCostById(Long activityId);

    /**
     * 删除作业成本
     * @param activityId 作业ID
     */
    void deleteActivityCost(Long activityId);

    /**
     * 处理作业成本分配
     * @param param 分配参数
     */
    void processActivityCostAllocation(SpecialCostParam.ActivityAllocation param);

    // ==================== 质量成本 ====================

    /**
     * 分页查询质量成本列表
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<SpecialCostResult.QualityCost> getQualityCostPage(PageableParam param);

    /**
     * 保存或更新质量成本
     * @param param 质量成本数据
     */
    void saveOrUpdateQualityCost(SpecialCostParam.QualityCostSave param);

    /**
     * 获取质量成本详情
     * @param qualityId 质量成本ID
     * @return 质量成本详情
     */
    SpecialCostResult.QualityCostDetail getQualityCostById(Long qualityId);

    /**
     * 删除质量成本
     * @param qualityId 质量成本ID
     */
    void deleteQualityCost(Long qualityId);

    /**
     * 获取质量成本分类统计
     * @return 统计数据
     */
    Map<String, Object> getQualityCostCategoryStats();

    // ==================== 环境成本 ====================

    /**
     * 分页查询环境成本列表
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<SpecialCostResult.EnvironmentCost> getEnvironmentCostPage(PageableParam param);

    /**
     * 保存或更新环境成本
     * @param param 环境成本数据
     */
    void saveOrUpdateEnvironmentCost(SpecialCostParam.EnvironmentCostSave param);

    /**
     * 获取环境成本详情
     * @param environmentId 环境成本ID
     * @return 环境成本详情
     */
    SpecialCostResult.EnvironmentCostDetail getEnvironmentCostById(Long environmentId);

    /**
     * 删除环境成本
     * @param environmentId 环境成本ID
     */
    void deleteEnvironmentCost(Long environmentId);

    // ==================== 研发成本 ====================

    /**
     * 分页查询研发成本列表
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<SpecialCostResult.RdCost> getRdCostPage(PageableParam param);

    /**
     * 保存或更新研发成本
     * @param param 研发成本数据
     */
    void saveOrUpdateRdCost(SpecialCostParam.RdCostSave param);

    /**
     * 获取研发成本详情
     * @param rdId 研发成本ID
     * @return 研发成本详情
     */
    SpecialCostResult.RdCostDetail getRdCostById(Long rdId);

    /**
     * 删除研发成本
     * @param rdId 研发成本ID
     */
    void deleteRdCost(Long rdId);

    // ==================== 专项分析 ====================

    /**
     * 获取专项成本综合分析数据
     * @param param 分析参数
     * @return 分析结果
     */
    SpecialCostResult.ComprehensiveAnalysis getSpecialCostAnalysis(SpecialCostParam.AnalysisQuery param);

    /**
     * 获取专项成本对比分析数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param compareType 对比类型
     * @return 对比分析结果
     */
    SpecialCostResult.CompareAnalysis getSpecialCostCompareAnalysis(String startDate, String endDate, String compareType);
}
