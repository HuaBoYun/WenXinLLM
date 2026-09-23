package com.financial.sharing.budgetControl.service;

import com.financial.sharing.budgetControl.dto.ControlAnalysisQueryParam;
import com.financial.sharing.util.MyJsonBean;

/**
 * 控制分析Service接口
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
public interface ControlAnalysisService {

    /**
     * 获取控制效果分析
     * 分析控制通过率、阻止率、预警率等
     *
     * @param param 查询参数
     * @return 控制效果分析数据
     */
    MyJsonBean getControlEffectAnalysis(ControlAnalysisQueryParam param);

    /**
     * 获取预算使用趋势分析
     * 按期间统计预算占用、释放、转移趋势
     *
     * @param param 查询参数
     * @return 预算使用趋势数据
     */
    MyJsonBean getBudgetUsageTrend(ControlAnalysisQueryParam param);

    /**
     * 获取异常控制分析
     * 分析被阻止的控制记录，找出异常模式
     *
     * @param param 查询参数
     * @return 异常控制分析数据
     */
    MyJsonBean getAbnormalControlAnalysis(ControlAnalysisQueryParam param);

    /**
     * 获取控制结果分布
     * 统计各种控制结果的数量和占比
     *
     * @param param 查询参数
     * @return 控制结果分布数据
     */
    MyJsonBean getControlResultDistribution(ControlAnalysisQueryParam param);

    /**
     * 获取组织控制排名
     * 按组织统计控制次数和阻止次数
     *
     * @param param 查询参数
     * @return 组织控制排名数据
     */
    MyJsonBean getOrgControlRanking(ControlAnalysisQueryParam param);

    /**
     * 获取科目控制排名
     * 按科目统计控制次数和阻止次数
     *
     * @param param 查询参数
     * @return 科目控制排名数据
     */
    MyJsonBean getSubjectControlRanking(ControlAnalysisQueryParam param);
}

