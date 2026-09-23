package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.ProjectBriefing;
import com.huabo.contract.vo.ProjectBriefingQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 项目交底表 Mapper 接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface ProjectBriefingMapper extends BaseMapper<ProjectBriefing> {

    /**
     * 分页查询项目交底列表
     *
     * @param param 查询参数
     * @return 项目交底列表
     */
    List<ProjectBriefing> selectProjectBriefingList(@Param("param") ProjectBriefingQueryParam param);

    /**
     * 根据交底编号查询项目交底
     *
     * @param briefingNo 交底编号
     * @return 项目交底
     */
    ProjectBriefing selectByBriefingNo(@Param("briefingNo") String briefingNo);

    /**
     * 检查交底编号是否存在
     *
     * @param briefingNo 交底编号
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean existsBriefingNo(@Param("briefingNo") String briefingNo, @Param("excludeId") Long excludeId);

    /**
     * 批量更新交底状态
     *
     * @param ids 交底ID列表
     * @param briefingStatus 交底状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateBriefingStatus(@Param("ids") List<Long> ids, 
                                 @Param("briefingStatus") Integer briefingStatus, 
                                 @Param("updateBy") Long updateBy);

    /**
     * 更新完成度
     *
     * @param id 交底ID
     * @param completionRate 完成度
     * @return 更新数量
     */
    int updateCompletionRate(@Param("id") Long id, @Param("completionRate") BigDecimal completionRate);

    /**
     * 批量更新完成度
     *
     * @param ids 交底ID列表
     * @param completionRate 完成度
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateCompletionRate(@Param("ids") List<Long> ids, 
                                 @Param("completionRate") BigDecimal completionRate, 
                                 @Param("updateBy") Long updateBy);

    /**
     * 获取草稿状态的交底列表
     *
     * @return 草稿状态的交底列表
     */
    List<ProjectBriefing> selectDraftBriefings();

    /**
     * 获取待交底的交底列表
     *
     * @return 待交底的交底列表
     */
    List<ProjectBriefing> selectPendingBriefings();

    /**
     * 获取已交底的交底列表
     *
     * @return 已交底的交底列表
     */
    List<ProjectBriefing> selectBriefedBriefings();

    /**
     * 获取已接收的交底列表
     *
     * @return 已接收的交底列表
     */
    List<ProjectBriefing> selectReceivedBriefings();

    /**
     * 获取已确认的交底列表
     *
     * @return 已确认的交底列表
     */
    List<ProjectBriefing> selectConfirmedBriefings();

    /**
     * 获取已完成的交底列表
     *
     * @return 已完成的交底列表
     */
    List<ProjectBriefing> selectCompletedBriefings();

    /**
     * 获取已取消的交底列表
     *
     * @return 已取消的交底列表
     */
    List<ProjectBriefing> selectCancelledBriefings();

    /**
     * 获取紧急交底列表
     *
     * @return 紧急交底列表
     */
    List<ProjectBriefing> selectUrgentBriefings();

    /**
     * 获取重要交底列表
     *
     * @return 重要交底列表
     */
    List<ProjectBriefing> selectImportantBriefings();

    /**
     * 获取高优先级交底列表
     *
     * @return 高优先级交底列表
     */
    List<ProjectBriefing> selectHighPriorityBriefings();

    /**
     * 获取我交底的交底列表
     *
     * @param userId 用户ID
     * @return 我交底的交底列表
     */
    List<ProjectBriefing> selectMyBriefings(@Param("userId") Long userId);

    /**
     * 获取我接收的交底列表
     *
     * @param userId 用户ID
     * @return 我接收的交底列表
     */
    List<ProjectBriefing> selectMyReceivedBriefings(@Param("userId") Long userId);

    /**
     * 获取我确认的交底列表
     *
     * @param userId 用户ID
     * @return 我确认的交底列表
     */
    List<ProjectBriefing> selectMyConfirmedBriefings(@Param("userId") Long userId);

    /**
     * 获取我验收的交底列表
     *
     * @param userId 用户ID
     * @return 我验收的交底列表
     */
    List<ProjectBriefing> selectMyAcceptedBriefings(@Param("userId") Long userId);

    /**
     * 根据项目ID查询交底列表
     *
     * @param projectId 项目ID
     * @return 交底列表
     */
    List<ProjectBriefing> selectByProjectId(@Param("projectId") Long projectId);

    /**
     * 统计项目交底数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> selectProjectBriefingStatistics(@Param("param") ProjectBriefingQueryParam param);

    /**
     * 获取交底类型分布统计
     *
     * @param param 查询参数
     * @return 交底类型分布
     */
    List<Map<String, Object>> selectBriefingTypeDistribution(@Param("param") ProjectBriefingQueryParam param);

    /**
     * 获取交底状态分布统计
     *
     * @param param 查询参数
     * @return 交底状态分布
     */
    List<Map<String, Object>> selectBriefingStatusDistribution(@Param("param") ProjectBriefingQueryParam param);

    /**
     * 获取交底等级分布统计
     *
     * @param param 查询参数
     * @return 交底等级分布
     */
    List<Map<String, Object>> selectBriefingLevelDistribution(@Param("param") ProjectBriefingQueryParam param);

    /**
     * 获取优先级分布统计
     *
     * @param param 查询参数
     * @return 优先级分布
     */
    List<Map<String, Object>> selectPriorityDistribution(@Param("param") ProjectBriefingQueryParam param);

    /**
     * 获取月度交底趋势
     *
     * @param param 查询参数
     * @return 月度交底趋势
     */
    List<Map<String, Object>> selectMonthlyBriefingTrend(@Param("param") ProjectBriefingQueryParam param);

    /**
     * 获取完成度分布统计
     *
     * @param param 查询参数
     * @return 完成度分布
     */
    List<Map<String, Object>> selectCompletionRateDistribution(@Param("param") ProjectBriefingQueryParam param);

    /**
     * 获取交底人分布统计
     *
     * @param param 查询参数
     * @return 交底人分布
     */
    List<Map<String, Object>> selectBrieferDistribution(@Param("param") ProjectBriefingQueryParam param);

    /**
     * 获取部门分布统计
     *
     * @param param 查询参数
     * @return 部门分布
     */
    List<Map<String, Object>> selectDepartmentDistribution(@Param("param") ProjectBriefingQueryParam param);

    /**
     * 模糊搜索项目交底
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 项目交底列表
     */
    List<ProjectBriefing> searchProjectBriefings(@Param("keyword") String keyword, @Param("limit") Integer limit);

    /**
     * 获取今日交底的交底列表
     *
     * @return 今日交底的交底列表
     */
    List<ProjectBriefing> selectTodayBriefings();

    /**
     * 获取本周交底的交底列表
     *
     * @return 本周交底的交底列表
     */
    List<ProjectBriefing> selectThisWeekBriefings();

    /**
     * 获取本月交底的交底列表
     *
     * @return 本月交底的交底列表
     */
    List<ProjectBriefing> selectThisMonthBriefings();

    /**
     * 获取交底执行情况
     *
     * @param briefingId 交底ID
     * @return 交底执行情况
     */
    Map<String, Object> selectBriefingExecution(@Param("briefingId") Long briefingId);

    /**
     * 获取交底进度跟踪
     *
     * @param briefingId 交底ID
     * @return 交底进度跟踪
     */
    List<Map<String, Object>> selectBriefingProgressTracking(@Param("briefingId") Long briefingId);

    /**
     * 获取交底历史记录
     *
     * @param briefingId 交底ID
     * @return 交底历史记录
     */
    List<Map<String, Object>> selectBriefingHistory(@Param("briefingId") Long briefingId);

    /**
     * 获取交底变更历史
     *
     * @param briefingId 交底ID
     * @return 交底变更历史
     */
    List<Map<String, Object>> selectBriefingChangeHistory(@Param("briefingId") Long briefingId);

    /**
     * 获取相关交底列表
     *
     * @param briefingId 交底ID
     * @return 相关交底列表
     */
    List<ProjectBriefing> selectRelatedBriefings(@Param("briefingId") Long briefingId);

    /**
     * 获取交底参与人员统计
     *
     * @param param 查询参数
     * @return 参与人员统计
     */
    List<Map<String, Object>> selectParticipantStatistics(@Param("param") ProjectBriefingQueryParam param);

    /**
     * 获取交底地点统计
     *
     * @param param 查询参数
     * @return 交底地点统计
     */
    List<Map<String, Object>> selectLocationStatistics(@Param("param") ProjectBriefingQueryParam param);

    /**
     * 获取交底效果评估
     *
     * @param param 查询参数
     * @return 交底效果评估
     */
    Map<String, Object> selectBriefingEffectiveness(@Param("param") ProjectBriefingQueryParam param);

    /**
     * 获取待处理的交底提醒
     *
     * @param userId 用户ID
     * @return 待处理的交底提醒
     */
    List<Map<String, Object>> selectPendingBriefingReminders(@Param("userId") Long userId);

    /**
     * 获取交底质量评分
     *
     * @param briefingId 交底ID
     * @return 交底质量评分
     */
    Map<String, Object> selectBriefingQualityScore(@Param("briefingId") Long briefingId);
}
