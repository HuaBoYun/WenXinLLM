package com.management.accountant.service;

import java.util.Map;

/**
 * 协同预算Service接口
 * 
 * @description 协同预算业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetCollaborationService {

    /**
     * 创建协作任务
     * 
     * @param params 任务参数
     * @return 任务信息
     */
    Map<String, Object> createTask(Map<String, Object> params);

    /**
     * 分配任务
     * 
     * @param params 分配参数
     * @return 分配结果
     */
    Map<String, Object> assignTask(Map<String, Object> params);

    /**
     * 添加评论
     * 
     * @param params 评论参数
     * @return 评论信息
     */
    Map<String, Object> addComment(Map<String, Object> params);

    /**
     * 版本管理
     * 
     * @param params 版本参数
     * @return 版本信息
     */
    Map<String, Object> manageVersion(Map<String, Object> params);

    /**
     * 协作统计
     *
     * @param params 统计参数
     * @return 统计信息
     */
    Map<String, Object> getStatistics(Map<String, Object> params);

    /**
     * 获取协同预算项目列表
     *
     * @param params 查询参数
     * @return 项目列表
     */
    Map<String, Object> getCollaborationList(Map<String, Object> params);

    /**
     * 获取协同预算统计数据
     *
     * @param params 查询参数
     * @return 统计数据
     */
    Map<String, Object> getCollaborationStats(Map<String, Object> params);

    /**
     * 创建协同预算项目
     *
     * @param params 项目参数
     * @return 创建结果
     */
    Map<String, Object> createCollaborationProject(Map<String, Object> params);

    /**
     * 更新协同预算项目
     *
     * @param projectId 项目ID
     * @param params 项目参数
     */
    void updateCollaborationProject(String projectId, Map<String, Object> params);

    /**
     * 删除协同预算项目
     *
     * @param projectId 项目ID
     */
    void deleteCollaborationProject(String projectId);

    /**
     * 归档协同预算项目
     *
     * @param projectId 项目ID
     */
    void archiveCollaborationProject(String projectId);

    /**
     * 导出协同预算项目
     *
     * @param projectId 项目ID
     * @return 导出结果
     */
    Map<String, Object> exportCollaborationProject(String projectId);

    /**
     * 获取项目参与者
     *
     * @param projectId 项目ID
     * @return 参与者列表
     */
    Map<String, Object> getProjectParticipants(String projectId);

    /**
     * 获取协作活动
     *
     * @param projectId 项目ID
     * @return 活动列表
     */
    Map<String, Object> getCollaborationActivities(String projectId);

    /**
     * 获取项目评论
     *
     * @param projectId 项目ID
     * @return 评论列表
     */
    Map<String, Object> getProjectComments(String projectId);
}

