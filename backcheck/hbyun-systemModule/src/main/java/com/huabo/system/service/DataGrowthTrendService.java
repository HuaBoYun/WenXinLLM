package com.huabo.system.service;

import com.huabo.system.dto.*;
import com.hbfk.util.JsonBean;

import java.util.List;

/**
 * 数据增长趋势Service接口
 */
public interface DataGrowthTrendService {

    /**
     * 获取数据增长趋势
     * @return JsonBean
     */
    JsonBean getDataGrowthTrend();

    /**
     * 获取数据总量
     * @return JsonBean
     */
    JsonBean getDataTotal();

    /**
     * 获取核心功能使用率
     * @return JsonBean
     */
    JsonBean getCoreFunctionUsage();

    /**
     * 获取业务活跃度排名
     * @return JsonBean
     */
    JsonBean getBusinessActivityRanking();

    /**
     * 获取新增用户数
     * @return JsonBean
     */
    JsonBean getNewUserCount();

    /**
     * 获取用户活跃度
     * @return JsonBean
     */
    JsonBean getUserActivity();

    /**
     * 获取风险内控审计维度
     * @return JsonBean
     */
    JsonBean getRiskAuditDimension();

    /**
     * 获取内控测试缺陷程度
     * @return JsonBean
     */
    JsonBean getInternalControlDefect();

    /**
     * 获取风险事件处理
     * @return JsonBean
     */
    JsonBean getRiskEventHandling();

    /**
     * 获取审计问题数量
     * @return JsonBean
     */
    JsonBean getAuditIssueCount();

    /**
     * 获取内控缺陷整改跟进
     * @return JsonBean
     */
    JsonBean getInternalControlRectification();

    /**
     * 获取审计问题整改验证完成率
     * @return JsonBean
     */
    JsonBean getAuditIssueVerificationRate();

    /**
     * 获取风险预警响应率
     * @return JsonBean
     */
    JsonBean getRiskWarningResponse();

    /**
     * 获取基础用户信息
     * @return JsonBean
     */
    JsonBean getBasicUserInfo();
}
