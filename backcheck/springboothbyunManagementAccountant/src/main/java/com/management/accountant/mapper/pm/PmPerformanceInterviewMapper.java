package com.management.accountant.mapper.pm;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.pm.PmPerformanceInterview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 绩效面谈管理 Mapper 接口
 *
 * @author AI Assistant
 * @since 2024-01-01
 */
@Mapper
public interface PmPerformanceInterviewMapper extends BaseMapper<PmPerformanceInterview> {

    /**
     * 分页查询绩效面谈
     *
     * @param page 分页参数
     * @param interviewCode 面谈编码
     * @param interviewTitle 面谈标题
     * @param interviewType 面谈类型
     * @param interviewStatus 面谈状态
     * @param intervieweeId 被面谈人ID
     * @param intervieweeName 被面谈人姓名
     * @param interviewerId 面谈官ID
     * @param interviewerName 面谈官姓名
     * @param interviewYear 面谈年度
     * @param interviewQuarter 面谈季度
     * @param interviewMonth 面谈月份
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param priorityLevel 优先级
     * @param needFollowUp 是否需要跟进
     * @param followUpStatus 跟进状态
     * @return 分页结果
     */
    IPage<PmPerformanceInterview> selectInterviewPage(
            Page<PmPerformanceInterview> page,
            @Param("interviewCode") String interviewCode,
            @Param("interviewTitle") String interviewTitle,
            @Param("interviewType") String interviewType,
            @Param("interviewStatus") String interviewStatus,
            @Param("intervieweeId") Long intervieweeId,
            @Param("intervieweeName") String intervieweeName,
            @Param("interviewerId") Long interviewerId,
            @Param("interviewerName") String interviewerName,
            @Param("interviewYear") Integer interviewYear,
            @Param("interviewQuarter") Integer interviewQuarter,
            @Param("interviewMonth") Integer interviewMonth,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("priorityLevel") String priorityLevel,
            @Param("needFollowUp") Integer needFollowUp,
            @Param("followUpStatus") String followUpStatus
    );

    /**
     * 根据被面谈人查询面谈列表
     *
     * @param intervieweeId 被面谈人ID
     * @param interviewStatus 面谈状态
     * @param limit 限制数量
     * @return 面谈列表
     */
    List<PmPerformanceInterview> selectByIntervieweeId(
            @Param("intervieweeId") Long intervieweeId,
            @Param("interviewStatus") String interviewStatus,
            @Param("limit") Integer limit
    );

    /**
     * 根据面谈官查询面谈列表
     *
     * @param interviewerId 面谈官ID
     * @param interviewStatus 面谈状态
     * @param limit 限制数量
     * @return 面谈列表
     */
    List<PmPerformanceInterview> selectByInterviewerId(
            @Param("interviewerId") Long interviewerId,
            @Param("interviewStatus") String interviewStatus,
            @Param("limit") Integer limit
    );

    /**
     * 根据部门查询面谈列表
     *
     * @param deptId 部门ID
     * @param interviewStatus 面谈状态
     * @param limit 限制数量
     * @return 面谈列表
     */
    List<PmPerformanceInterview> selectByDeptId(
            @Param("deptId") Long deptId,
            @Param("interviewStatus") String interviewStatus,
            @Param("limit") Integer limit
    );

    /**
     * 查询待跟进的面谈
     *
     * @param followUpDeadline 跟进截止时间
     * @param limit 限制数量
     * @return 面谈列表
     */
    List<PmPerformanceInterview> selectPendingFollowUp(
            @Param("followUpDeadline") LocalDateTime followUpDeadline,
            @Param("limit") Integer limit
    );

    /**
     * 查询即将到期的面谈
     *
     * @param deadline 截止时间
     * @param limit 限制数量
     * @return 面谈列表
     */
    List<PmPerformanceInterview> selectUpcomingInterviews(
            @Param("deadline") LocalDateTime deadline,
            @Param("limit") Integer limit
    );

    /**
     * 查询逾期的面谈
     *
     * @param currentTime 当前时间
     * @param limit 限制数量
     * @return 面谈列表
     */
    List<PmPerformanceInterview> selectOverdueInterviews(
            @Param("currentTime") LocalDateTime currentTime,
            @Param("limit") Integer limit
    );

    /**
     * 统计面谈数据
     *
     * @param interviewYear 面谈年度
     * @param interviewQuarter 面谈季度
     * @param interviewMonth 面谈月份
     * @param deptId 部门ID
     * @param interviewerId 面谈官ID
     * @return 统计结果
     */
    Map<String, Object> selectInterviewStatistics(
            @Param("interviewYear") Integer interviewYear,
            @Param("interviewQuarter") Integer interviewQuarter,
            @Param("interviewMonth") Integer interviewMonth,
            @Param("deptId") Long deptId,
            @Param("interviewerId") Long interviewerId
    );

    /**
     * 统计面谈状态分布
     *
     * @param interviewYear 面谈年度
     * @param deptId 部门ID
     * @return 状态分布
     */
    List<Map<String, Object>> selectInterviewStatusDistribution(
            @Param("interviewYear") Integer interviewYear,
            @Param("deptId") Long deptId
    );

    /**
     * 统计面谈类型分布
     *
     * @param interviewYear 面谈年度
     * @param deptId 部门ID
     * @return 类型分布
     */
    List<Map<String, Object>> selectInterviewTypeDistribution(
            @Param("interviewYear") Integer interviewYear,
            @Param("deptId") Long deptId
    );

    /**
     * 统计面谈完成趋势
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param deptId 部门ID
     * @return 完成趋势
     */
    List<Map<String, Object>> selectInterviewCompletionTrend(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("deptId") Long deptId
    );

    /**
     * 统计面谈满意度分布
     *
     * @param interviewYear 面谈年度
     * @param deptId 部门ID
     * @return 满意度分布
     */
    List<Map<String, Object>> selectSatisfactionDistribution(
            @Param("interviewYear") Integer interviewYear,
            @Param("deptId") Long deptId
    );

    /**
     * 查询面谈排行榜
     *
     * @param interviewYear 面谈年度
     * @param rankType 排行类型 (INTERVIEWER-面谈官, DEPARTMENT-部门)
     * @param limit 限制数量
     * @return 排行榜
     */
    List<Map<String, Object>> selectInterviewRanking(
            @Param("interviewYear") Integer interviewYear,
            @Param("rankType") String rankType,
            @Param("limit") Integer limit
    );

    /**
     * 批量更新面谈状态
     *
     * @param interviewIds 面谈ID列表
     * @param interviewStatus 面谈状态
     * @param updatedBy 更新人ID
     * @param updatedName 更新人姓名
     * @return 更新数量
     */
    int batchUpdateStatus(
            @Param("interviewIds") List<Long> interviewIds,
            @Param("interviewStatus") String interviewStatus,
            @Param("updatedBy") Long updatedBy,
            @Param("updatedName") String updatedName
    );

    /**
     * 批量更新跟进状态
     *
     * @param interviewIds 面谈ID列表
     * @param followUpStatus 跟进状态
     * @param updatedBy 更新人ID
     * @param updatedName 更新人姓名
     * @return 更新数量
     */
    int batchUpdateFollowUpStatus(
            @Param("interviewIds") List<Long> interviewIds,
            @Param("followUpStatus") String followUpStatus,
            @Param("updatedBy") Long updatedBy,
            @Param("updatedName") String updatedName
    );

    /**
     * 根据模板ID查询面谈列表
     *
     * @param templateId 模板ID
     * @param limit 限制数量
     * @return 面谈列表
     */
    List<PmPerformanceInterview> selectByTemplateId(
            @Param("templateId") Long templateId,
            @Param("limit") Integer limit
    );

    /**
     * 根据关联评估ID查询面谈列表
     *
     * @param assessmentId 评估ID
     * @param limit 限制数量
     * @return 面谈列表
     */
    List<PmPerformanceInterview> selectByAssessmentId(
            @Param("assessmentId") Long assessmentId,
            @Param("limit") Integer limit
    );

    /**
     * 根据关联目标ID查询面谈列表
     *
     * @param targetId 目标ID
     * @param limit 限制数量
     * @return 面谈列表
     */
    List<PmPerformanceInterview> selectByTargetId(
            @Param("targetId") Long targetId,
            @Param("limit") Integer limit
    );

    /**
     * 查询面谈详情（包含关联信息）
     *
     * @param interviewId 面谈ID
     * @return 面谈详情
     */
    Map<String, Object> selectInterviewDetail(@Param("interviewId") Long interviewId);

    /**
     * 检查面谈时间冲突
     *
     * @param interviewerId 面谈官ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param excludeId 排除的面谈ID
     * @return 冲突数量
     */
    int checkTimeConflict(
            @Param("interviewerId") Long interviewerId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeId") Long excludeId
    );

    /**
     * 自动分配面谈官
     *
     * @param deptId 部门ID
     * @param interviewType 面谈类型
     * @param limit 限制数量
     * @return 面谈官列表
     */
    List<Map<String, Object>> selectAvailableInterviewers(
            @Param("deptId") Long deptId,
            @Param("interviewType") String interviewType,
            @Param("limit") Integer limit
    );

    /**
     * 查询面谈提醒列表
     *
     * @param reminderTime 提醒时间
     * @param limit 限制数量
     * @return 提醒列表
     */
    List<PmPerformanceInterview> selectInterviewReminders(
            @Param("reminderTime") LocalDateTime reminderTime,
            @Param("limit") Integer limit
    );

    /**
     * 导出面谈数据
     *
     * @param interviewYear 面谈年度
     * @param interviewQuarter 面谈季度
     * @param interviewMonth 面谈月份
     * @param deptId 部门ID
     * @param interviewStatus 面谈状态
     * @return 导出数据
     */
    List<Map<String, Object>> selectInterviewExportData(
            @Param("interviewYear") Integer interviewYear,
            @Param("interviewQuarter") Integer interviewQuarter,
            @Param("interviewMonth") Integer interviewMonth,
            @Param("deptId") Long deptId,
            @Param("interviewStatus") String interviewStatus
    );
}
