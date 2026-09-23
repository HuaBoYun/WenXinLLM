package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.RiskWarning;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 风险预警Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface RiskWarningMapper extends BaseMapper<RiskWarning> {

    /**
     * 分页查询风险预警列表
     *
     * @param param 查询参数
     * @return 风险预警列表
     */
    List<RiskWarning> selectRiskWarningPage(@Param("param") Object param);

    /**
     * 统计风险预警数据
     *
     * @param projectId 项目ID
     * @return 统计结果
     */
    Map<String, Object> selectRiskWarningStatistics(@Param("projectId") Long projectId);

    /**
     * 查询活跃的高级别预警
     *
     * @param projectId 项目ID
     * @param minLevel 最小预警级别
     * @return 高级别预警列表
     */
    List<RiskWarning> selectActiveHighLevelWarnings(@Param("projectId") Long projectId, @Param("minLevel") Integer minLevel);

    /**
     * 查询即将超期的预警
     *
     * @param projectId 项目ID
     * @param hours 超期小时数
     * @return 即将超期的预警列表
     */
    List<RiskWarning> selectOverdueWarnings(@Param("projectId") Long projectId, @Param("hours") Integer hours);

    /**
     * 按预警类型统计
     *
     * @param projectId 项目ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 预警类型统计
     */
    List<Map<String, Object>> selectWarningTypeStatistics(@Param("projectId") Long projectId, 
                                                          @Param("startDate") String startDate, 
                                                          @Param("endDate") String endDate);

    /**
     * 按月统计预警趋势
     *
     * @param projectId 项目ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 月度预警趋势
     */
    List<Map<String, Object>> selectMonthlyWarningTrend(@Param("projectId") Long projectId, 
                                                        @Param("startDate") String startDate, 
                                                        @Param("endDate") String endDate);

    /**
     * 批量更新预警状态
     *
     * @param ids 预警ID列表
     * @param warningStatus 预警状态
     * @return 更新数量
     */
    int batchUpdateWarningStatus(@Param("ids") List<Long> ids, @Param("warningStatus") Integer warningStatus);

    /**
     * 批量更新处理状态
     *
     * @param ids 预警ID列表
     * @param handleStatus 处理状态
     * @param handlePerson 处理人
     * @return 更新数量
     */
    int batchUpdateHandleStatus(@Param("ids") List<Long> ids, 
                               @Param("handleStatus") Integer handleStatus, 
                               @Param("handlePerson") String handlePerson);

    /**
     * 更新通知发送状态
     *
     * @param ids 预警ID列表
     * @param notificationSent 通知发送状态
     * @return 更新数量
     */
    int updateNotificationStatus(@Param("ids") List<Long> ids, @Param("notificationSent") Integer notificationSent);

    /**
     * 根据项目ID删除风险预警记录（软删除）
     *
     * @param projectId 项目ID
     * @return 删除数量
     */
    int deleteByProjectId(@Param("projectId") Long projectId);

    /**
     * 自动创建系统预警
     *
     * @param projectId 项目ID
     * @param warningType 预警类型
     * @param warningLevel 预警级别
     * @param warningTitle 预警标题
     * @param warningContent 预警内容
     * @param warningSource 预警来源
     * @param triggerCondition 触发条件
     * @param expectedResolveTime 预期解决时间
     * @return 插入数量
     */
    int insertSystemWarning(@Param("projectId") Long projectId,
                           @Param("warningType") Integer warningType,
                           @Param("warningLevel") Integer warningLevel,
                           @Param("warningTitle") String warningTitle,
                           @Param("warningContent") String warningContent,
                           @Param("warningSource") String warningSource,
                           @Param("triggerCondition") String triggerCondition,
                           @Param("expectedResolveTime") String expectedResolveTime);
}
