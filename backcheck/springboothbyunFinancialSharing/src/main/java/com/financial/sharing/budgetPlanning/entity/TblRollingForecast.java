package com.financial.sharing.budgetPlanning.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 滚动预测任务实体类
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Data
@TableName("TBL_ROLLING_FORECAST")
public class TblRollingForecast implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预测任务ID
     */
    @TableId
    private String forecastId;

    /**
     * 预测任务编号
     */
    private String forecastNo;

    /**
     * 预测任务名称
     */
    private String forecastName;

    /**
     * 预算模型ID
     */
    private String modelId;

    /**
     * 预测类型：MONTHLY-月度，QUARTERLY-季度
     */
    private String forecastType;

    /**
     * 开始期间
     */
    private String startPeriod;

    /**
     * 结束期间
     */
    private String endPeriod;

    /**
     * 预测版本
     */
    private String version;

    /**
     * 基准类型：HISTORY-历史数据，BUDGET-预算数据，ACTUAL-实际数据
     */
    private String baseType;

    /**
     * 预测说明
     */
    private String forecastDesc;

    /**
     * 状态：DRAFT-草稿，SUBMITTED-已提交，APPROVED-已审批，REJECTED-已驳回
     */
    private String status;

    /**
     * 提交人
     */
    private String submitUser;

    /**
     * 提交时间
     */
    private Date submitTime;

    /**
     * 审批人
     */
    private String approveUser;

    /**
     * 审批时间
     */
    private Date approveTime;

    /**
     * 审批意见
     */
    private String approveOpinion;

    /**
     * 租户ID
     */
    private String orgId;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 预算模型名称（非数据库字段）
     */
    @TableField(exist = false)
    private String modelName;

    /**
     * 预测数据列表（非数据库字段）
     */
    @TableField(exist = false)
    private List<TblRollingForecastData> dataList;
}

