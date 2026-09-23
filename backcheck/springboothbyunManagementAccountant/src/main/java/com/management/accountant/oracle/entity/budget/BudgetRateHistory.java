package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 汇率历史记录实体类
 *
 * @author AI Agent
 * @date 2026-04-13
 */
@Data
@TableName("TBL_BUDGET_RATE_HISTORY")
public class BudgetRateHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 历史记录ID */
    @TableId(type = IdType.ASSIGN_UUID)
    private String historyId;

    /** 关联币种ID */
    private String currencyId;

    /** 币种代码 */
    private String currencyCode;

    /** 币种名称 */
    private String currencyName;

    /** 旧汇率 */
    private Double oldRate;

    /** 新汇率 */
    private Double newRate;

    /** 汇率变动值 */
    private Double rateChange;

    /** 变动百分比 */
    private Double changePercent;

    /** 汇率来源 */
    private String rateSource;

    /** 记录日期 */
    private Date recordDate;

    /** 创建时间 */
    private Date createTime;

    /** 创建人 */
    private String createBy;

    /** 备注 */
    private String remark;

    /** 删除标志 */
    private Integer delFlag;
}
