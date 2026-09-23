package com.financial.sharing.budgetPlanning.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 滚动预测数据实体类
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Data
@TableName("TBL_ROLLING_FORECAST_DATA")
public class TblRollingForecastData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    @TableId
    private String dataId;

    /**
     * 预测任务ID
     */
    private String forecastId;

    /**
     * 期间
     */
    private String period;

    /**
     * 科目编码
     */
    private String subjectCode;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 组织编码
     */
    private String organizationCode;

    /**
     * 组织名称
     */
    private String organizationName;

    /**
     * 维度1编码
     */
    private String dimension1Code;

    /**
     * 维度1名称
     */
    private String dimension1Name;

    /**
     * 维度2编码
     */
    private String dimension2Code;

    /**
     * 维度2名称
     */
    private String dimension2Name;

    /**
     * 预测值
     */
    private BigDecimal forecastValue;

    /**
     * 基准值
     */
    private BigDecimal baseValue;

    /**
     * 实际值
     */
    private BigDecimal actualValue;

    /**
     * 差异值
     */
    private BigDecimal varianceValue;

    /**
     * 差异率
     */
    private BigDecimal varianceRate;

    /**
     * 备注
     */
    private String remark;

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
}

