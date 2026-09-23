package com.huabo.system.mapper;

import com.huabo.system.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 数据增长趋势Mapper接口
 */
public interface DataGrowthTrendMapper {

    /**
     * 查询数据增长趋势
     * @return 数据增长趋势列表
     */
    List<DataGrowthTrendDTO> queryDataGrowthTrend();

    /**
     * 查询数据总量
     * @return 数据总量
     */
    DataTotalDTO queryDataTotal();

    /**
     * 查询核心功能使用率
     * @return 核心功能使用率
     */
    CoreFunctionUsageDTO queryCoreFunctionUsage();

    /**
     * 查询业务活跃度排名
     * @return 业务活跃度排名列表
     */
    List<BusinessActivityRankingDTO> queryBusinessActivityRanking();

    /**
     * 查询新增用户数
     * @return 新增用户数列表
     */
    List<NewUserCountDTO> queryNewUserCount();

    /**
     * 查询用户活跃度
     * @return 用户活跃度列表
     */
    List<UserActivityDTO> queryUserActivity();

    /**
     * 查询风险内控审计维度
     * @return 风险内控审计维度数据列表
     */
    List<Map<String, Object>> queryRiskAuditDimension();

    /**
     * 查询内控测试缺陷程度
     * @return 内控测试缺陷程度列表
     */
    List<InternalControlDefectDTO> queryInternalControlDefect();

    /**
     * 查询风险事件处理
     * @return 风险事件处理列表
     */
    List<Map<String, Object>> queryRiskEventHandling();

    /**
     * 查询审计问题数量
     * @return 审计问题数量列表
     */
    List<AuditIssueCountDTO> queryAuditIssueCount();

    /**
     * 查询内控缺陷整改跟进
     * @return 内控缺陷整改跟进列表
     */
    List<InternalControlRectificationDTO> queryInternalControlRectification();

    /**
     * 查询审计问题整改验证完成率
     * @return 审计问题整改验证完成率列表
     */
    List<Map<String, Object>> queryAuditIssueVerificationRate();

    /**
     * 查询风险预警响应率
     * @return 风险预警响应率列表
     */
    List<Map<String, Object>> queryRiskWarningResponse();

    /**
     * 查询基础用户信息
     * @return 基础用户信息列表
     */
    List<Map<String, Object>> queryBasicUserInfo();
}
