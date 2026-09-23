package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算多币种实体类
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_BUDGET_MULTI_CURRENCY")
public class BudgetMultiCurrency implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 多币种ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String currencyId;

    /**
     * 关联预算ID
     */
    private String budgetId;

    /**
     * 原币种代码
     */
    private String fromCurrency;

    /**
     * 币种代码
     */
    private String currencyCode;

    /**
     * 目标币种代码
     */
    private String toCurrency;

    /**
     * 币种名称
     */
    private String currencyName;

    /**
     * 币种状态
     */
    private String currencyStatus;

    /**
     * 汇率类型: FIXED-固定汇率, MARKET-市场汇率, MONTHLY-月度汇率
     */
    private String exchangeRateType;

    /**
     * 汇率值
     */
    private Double exchangeRate;

    /**
     * 汇率生效日期
     */
    private Date effectiveDate;

    /**
     * 汇率失效日期
     */
    private Date expiryDate;

    /**
     * 原币种金额
     */
    private Double fromAmount;

    /**
     * 目标币种金额
     */
    private Double toAmount;

    /**
     * 汇率差额
     */
    private Double exchangeDifference;

    /**
     * 汇率说明
     */
    private String exchangeDescription;

    /**
     * 币种符号
     */
    private String currencySymbol;

    /**
     * 是否基准币种: 0-否, 1-是
     */
    private Integer isBaseCurrency;

    /**
     * 币种类型: MAJOR-主要货币, ASIA-亚洲货币, EUROPE-欧洲货币, OTHER-其他
     */
    private String currencyType;

    /**
     * 汇率变动百分比
     */
    private Double rateChange;

    /**
     * 汇率来源: MANUAL-手动输入, API-API接口, BANK-银行汇率, MARKET-市场汇率
     */
    private String rateSource;

    /**
     * 更新频率: REAL_TIME-实时, HOURLY-每小时, DAILY-每日, WEEKLY-每周
     */
    private String updateFrequency;

    /**
     * 精度设置
     */
    private Integer precisionVal;

    /**
     * 舍入规则: ROUND_HALF_UP-四舍五入, ROUND_DOWN-向下舍入, ROUND_UP-向上舍入, ROUND_HALF_EVEN-银行家舍入
     */
    private String roundingRule;

    /**
     * 描述
     */
    private String description;

    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志: 0-正常, 1-已删除
     */
    private Integer delFlag;

}
