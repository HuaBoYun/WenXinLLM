package com.huabo.cybermonitor.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.EquityChangeRecord;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.EquityChangeRecordQueryVO;

/**
 * 股权变动记录服务接口
 *
 * @author system
 * @since 2024-01-01
 */
public interface IEquityChangeRecordService extends IService<EquityChangeRecord> {

    /**
     * 分页查询股权变动记录列表
     *
     * @param queryVO 查询参数
     * @return 分页结果
     */
    PageResult<EquityChangeRecord> getEquityChangeRecordList(EquityChangeRecordQueryVO queryVO);

    /**
     * 根据ID获取股权变动记录详情
     *
     * @param changeId 变动记录ID
     * @return 股权变动记录详情
     */
    EquityChangeRecord getEquityChangeRecordById(String changeId);

    /**
     * 新增股权变动记录
     *
     * @param equityChangeRecord 股权变动记录信息
     * @return 是否成功
     */
    boolean addEquityChangeRecord(EquityChangeRecord equityChangeRecord);

    /**
     * 更新股权变动记录
     *
     * @param equityChangeRecord 股权变动记录信息
     * @return 是否成功
     */
    boolean updateEquityChangeRecord(EquityChangeRecord equityChangeRecord);

    /**
     * 删除股权变动记录
     *
     * @param changeId 变动记录ID
     * @return 是否成功
     */
    boolean deleteEquityChangeRecord(String changeId);

    /**
     * 批量删除股权变动记录
     *
     * @param changeIds 变动记录ID列表
     * @return 是否成功
     */
    boolean batchDeleteEquityChangeRecord(List<String> changeIds);

    /**
     * 根据股权结构ID查询变动记录
     *
     * @param equityId 股权结构ID
     * @return 变动记录列表
     */
    List<EquityChangeRecord> getEquityChangeRecordByEquityId(String equityId);

    /**
     * 根据被投资企业ID查询变动记录
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 变动记录列表
     */
    List<EquityChangeRecord> getEquityChangeRecordByInvesteeEnterpriseId(String investeeEnterpriseId);

    /**
     * 根据投资方企业ID查询变动记录
     *
     * @param investorEnterpriseId 投资方企业ID
     * @return 变动记录列表
     */
    List<EquityChangeRecord> getEquityChangeRecordByInvestorEnterpriseId(String investorEnterpriseId);

    /**
     * 查询重大变动记录
     *
     * @param isMajorChange 是否重大变动
     * @return 重大变动记录列表
     */
    List<EquityChangeRecord> getMajorChangeRecords(Boolean isMajorChange);

    /**
     * 查询需要预警的变动记录
     *
     * @param needWarning 是否需要预警
     * @return 预警变动记录列表
     */
    List<EquityChangeRecord> getWarningChangeRecords(Boolean needWarning);

    /**
     * 按变动类型查询记录
     *
     * @param changeType 变动类型
     * @return 变动记录列表
     */
    List<EquityChangeRecord> getEquityChangeRecordByChangeType(String changeType);

    /**
     * 按审批状态查询记录
     *
     * @param approvalStatus 审批状态
     * @return 变动记录列表
     */
    List<EquityChangeRecord> getEquityChangeRecordByApprovalStatus(String approvalStatus);

    /**
     * 按预警级别查询记录
     *
     * @param warningLevel 预警级别
     * @return 变动记录列表
     */
    List<EquityChangeRecord> getEquityChangeRecordByWarningLevel(String warningLevel);

    /**
     * 查询最新变动记录
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @param limit 限制数量
     * @return 最新变动记录列表
     */
    List<EquityChangeRecord> getLatestChangeRecords(String investeeEnterpriseId, Integer limit);

    /**
     * 查询变动频繁的企业
     *
     * @param days 统计天数
     * @param minChangeCount 最小变动次数
     * @return 变动频繁企业列表
     */
    List<Map<String, Object>> getFrequentChangeEnterprises(Integer days, Integer minChangeCount);

    /**
     * 按变动类型统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 变动类型统计
     */
    List<Map<String, Object>> getChangeTypeStatistics(String startDate, String endDate);

    /**
     * 按变动原因统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 变动原因统计
     */
    List<Map<String, Object>> getChangeReasonStatistics(String startDate, String endDate);

    /**
     * 按审批状态统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 审批状态统计
     */
    List<Map<String, Object>> getApprovalStatusStatistics(String startDate, String endDate);

    /**
     * 按预警级别统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 预警级别统计
     */
    List<Map<String, Object>> getWarningLevelStatistics(String startDate, String endDate);

    /**
     * 查询变动趋势
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 变动趋势数据
     */
    List<Map<String, Object>> getChangeTrend(String startDate, String endDate);

    /**
     * 查询变动金额趋势
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 变动金额趋势数据
     */
    List<Map<String, Object>> getChangeAmountTrend(String startDate, String endDate);

    /**
     * 批量更新审批状态
     *
     * @param changeIds 变动记录ID列表
     * @param approvalStatus 审批状态
     * @return 是否成功
     */
    boolean batchUpdateApprovalStatus(List<String> changeIds, String approvalStatus);

    /**
     * 批量更新预警状态
     *
     * @param changeIds 变动记录ID列表
     * @param needWarning 是否需要预警
     * @param warningLevel 预警级别
     * @return 是否成功
     */
    boolean batchUpdateWarningStatus(List<String> changeIds, Boolean needWarning, String warningLevel);

    /**
     * 删除过期变动记录
     *
     * @param days 过期天数
     * @return 删除数量
     */
    int deleteExpiredChangeRecords(Integer days);

    /**
     * 获取变动统计概览
     *
     * @return 统计概览
     */
    Map<String, Object> getChangeStatisticsOverview();

    /**
     * 获取变动类型分布
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> getChangeTypeDistribution();

    /**
     * 获取变动原因分布
     *
     * @return 原因分布统计
     */
    List<Map<String, Object>> getChangeReasonDistribution();

    /**
     * 获取审批状态分布
     *
     * @return 状态分布统计
     */
    List<Map<String, Object>> getApprovalStatusDistribution();

    /**
     * 获取预警级别分布
     *
     * @return 级别分布统计
     */
    List<Map<String, Object>> getWarningLevelDistribution();

    /**
     * 导出股权变动记录列表
     *
     * @param queryVO 查询参数
     * @return 导出数据列表
     */
    List<Map<String, Object>> exportEquityChangeRecordList(EquityChangeRecordQueryVO queryVO);

    /**
     * 股权变动影响分析
     *
     * @param changeId 变动记录ID
     * @return 影响分析结果
     */
    Map<String, Object> analyzeEquityChangeImpact(String changeId);

    /**
     * 股权变动合规性检查
     *
     * @param equityChangeRecord 股权变动记录
     * @return 合规性检查结果
     */
    Map<String, Object> checkEquityChangeCompliance(EquityChangeRecord equityChangeRecord);

    /**
     * 股权变动预警检查
     *
     * @param equityChangeRecord 股权变动记录
     * @return 预警检查结果
     */
    Map<String, Object> checkEquityChangeWarning(EquityChangeRecord equityChangeRecord);

    /**
     * 获取变动类型标签转换
     *
     * @param changeType 变动类型
     * @return 类型标签
     */
    String getChangeTypeLabel(String changeType);

    /**
     * 获取变动原因标签转换
     *
     * @param changeReason 变动原因
     * @return 原因标签
     */
    String getChangeReasonLabel(String changeReason);

    /**
     * 获取转让方式标签转换
     *
     * @param transferMethod 转让方式
     * @return 方式标签
     */
    String getTransferMethodLabel(String transferMethod);

    /**
     * 获取审批状态标签转换
     *
     * @param approvalStatus 审批状态
     * @return 状态标签
     */
    String getApprovalStatusLabel(String approvalStatus);

    /**
     * 获取预警级别标签转换
     *
     * @param warningLevel 预警级别
     * @return 级别标签
     */
    String getWarningLevelLabel(String warningLevel);
}
