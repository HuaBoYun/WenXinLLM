package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 滚动预算计划实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_ROLLING_BUDGET_PLAN")
public class RollingBudgetPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 计划ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String planId;

    /**
     * 计划名称
     */
    private String planName;

    /**
     * 计划编码
     */
    private String planCode;

    /**
     * 滚动类型: MONTHLY-月度滚动, QUARTERLY-季度滚动
     */
    private String rollingType;

    /**
     * 滚动窗口(月数)
     */
    private Integer rollingWindow;

    /**
     * 基础期间
     */
    private String basePeriod;

    /**
     * 更新频率: DAILY-每日, WEEKLY-每周, MONTHLY-每月
     */
    private String updateFrequency;

    /**
     * 预测方法: TREND-趋势法, AVERAGE-平均法, MOVING_AVERAGE-移动平均, REGRESSION-回归分析
     */
    private String forecastMethod;

    /**
     * 计划状态: DRAFT-草稿, ACTIVE-激活, PAUSED-暂停, COMPLETED-完成
     */
    private String planStatus;

    /**
     * 最后执行时间
     */
    private Date lastExecutionTime;

    /**
     * 下次执行时间
     */
    private Date nextExecutionTime;

    /**
     * 计划描述
     */
    private String description;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标志(0-未删除, 1-已删除)
     */
    private Integer delFlag;
}

