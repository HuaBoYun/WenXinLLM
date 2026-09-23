package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.EquityChangeRecord;
import com.huabo.cybermonitor.vo.EquityChangeRecordQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 股权变动记录数据访问接口
 *
 * @author system
 * @since 2024-01-01
 */
@Mapper
public interface EquityChangeRecordMapper extends BaseMapper<EquityChangeRecord> {

    /**
     * 分页查询股权变动记录列表
     *
     * @param queryVO 查询参数
     * @return 股权变动记录列表
     */
    List<EquityChangeRecord> selectEquityChangeRecordList(@Param("queryVO") EquityChangeRecordQueryVO queryVO);

    /**
     * 根据股权结构ID查询变动记录
     *
     * @param equityId 股权结构ID
     * @return 变动记录列表
     */
    List<EquityChangeRecord> selectByEquityId(@Param("equityId") String equityId);

    /**
     * 根据被投资企业ID查询变动记录
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 变动记录列表
     */
    List<EquityChangeRecord> selectByInvesteeEnterpriseId(@Param("investeeEnterpriseId") String investeeEnterpriseId);

    /**
     * 根据投资方企业ID查询变动记录
     *
     * @param investorEnterpriseId 投资方企业ID
     * @return 变动记录列表
     */
    List<EquityChangeRecord> selectByInvestorEnterpriseId(@Param("investorEnterpriseId") String investorEnterpriseId);

    /**
     * 查询重大变动记录
     *
     * @param isMajorChange 是否重大变动
     * @return 重大变动记录列表
     */
    List<EquityChangeRecord> selectMajorChangeRecords(@Param("isMajorChange") Boolean isMajorChange);

    /**
     * 查询需要预警的变动记录
     *
     * @param needWarning 是否需要预警
     * @return 预警变动记录列表
     */
    List<EquityChangeRecord> selectWarningChangeRecords(@Param("needWarning") Boolean needWarning);

    /**
     * 按变动类型查询记录
     *
     * @param changeType 变动类型
     * @return 变动记录列表
     */
    List<EquityChangeRecord> selectByChangeType(@Param("changeType") String changeType);

    /**
     * 按审批状态查询记录
     *
     * @param approvalStatus 审批状态
     * @return 变动记录列表
     */
    List<EquityChangeRecord> selectByApprovalStatus(@Param("approvalStatus") String approvalStatus);

    /**
     * 按预警级别查询记录
     *
     * @param warningLevel 预警级别
     * @return 变动记录列表
     */
    List<EquityChangeRecord> selectByWarningLevel(@Param("warningLevel") String warningLevel);

    /**
     * 查询最新变动记录
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @param limit 限制数量
     * @return 最新变动记录列表
     */
    List<EquityChangeRecord> selectLatestChangeRecords(@Param("investeeEnterpriseId") String investeeEnterpriseId,
                                                       @Param("limit") Integer limit);

    /**
     * 查询变动频繁的企业
     *
     * @param days 统计天数
     * @param minChangeCount 最小变动次数
     * @return 变动频繁企业列表
     */
    List<Map<String, Object>> selectFrequentChangeEnterprises(@Param("days") Integer days,
                                                              @Param("minChangeCount") Integer minChangeCount);

    /**
     * 按变动类型统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 变动类型统计
     */
    List<Map<String, Object>> selectChangeTypeStatistics(@Param("startDate") String startDate,
                                                         @Param("endDate") String endDate);

    /**
     * 按变动原因统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 变动原因统计
     */
    List<Map<String, Object>> selectChangeReasonStatistics(@Param("startDate") String startDate,
                                                           @Param("endDate") String endDate);

    /**
     * 按审批状态统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 审批状态统计
     */
    List<Map<String, Object>> selectApprovalStatusStatistics(@Param("startDate") String startDate,
                                                             @Param("endDate") String endDate);

    /**
     * 按预警级别统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 预警级别统计
     */
    List<Map<String, Object>> selectWarningLevelStatistics(@Param("startDate") String startDate,
                                                           @Param("endDate") String endDate);

    /**
     * 查询变动趋势
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 变动趋势数据
     */
    List<Map<String, Object>> selectChangeTrend(@Param("startDate") String startDate,
                                               @Param("endDate") String endDate);

    /**
     * 查询变动金额趋势
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 变动金额趋势数据
     */
    List<Map<String, Object>> selectChangeAmountTrend(@Param("startDate") String startDate,
                                                     @Param("endDate") String endDate);

    /**
     * 批量更新审批状态
     *
     * @param changeIds 变动记录ID列表
     * @param approvalStatus 审批状态
     * @return 更新数量
     */
    int batchUpdateApprovalStatus(@Param("changeIds") List<String> changeIds,
                                 @Param("approvalStatus") String approvalStatus);

    /**
     * 批量更新预警状态
     *
     * @param changeIds 变动记录ID列表
     * @param needWarning 是否需要预警
     * @param warningLevel 预警级别
     * @return 更新数量
     */
    int batchUpdateWarningStatus(@Param("changeIds") List<String> changeIds,
                                @Param("needWarning") Boolean needWarning,
                                @Param("warningLevel") String warningLevel);

    /**
     * 删除过期变动记录
     *
     * @param days 过期天数
     * @return 删除数量
     */
    int deleteExpiredChangeRecords(@Param("days") Integer days);

    /**
     * 获取变动统计概览
     *
     * @return 统计概览
     */
    Map<String, Object> selectChangeStatisticsOverview();

    /**
     * 获取变动类型分布
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> selectChangeTypeDistribution();

    /**
     * 获取变动原因分布
     *
     * @return 原因分布统计
     */
    List<Map<String, Object>> selectChangeReasonDistribution();

    /**
     * 获取审批状态分布
     *
     * @return 状态分布统计
     */
    List<Map<String, Object>> selectApprovalStatusDistribution();

    /**
     * 获取预警级别分布
     *
     * @return 级别分布统计
     */
    List<Map<String, Object>> selectWarningLevelDistribution();

    /**
     * 导出股权变动记录列表
     *
     * @param queryVO 查询参数
     * @return 导出数据列表
     */
    List<Map<String, Object>> exportEquityChangeRecordList(@Param("queryVO") EquityChangeRecordQueryVO queryVO);
}
