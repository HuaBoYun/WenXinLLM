package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 汇率实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_EXCHANGE_RATE")
public class ExchangeRate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 汇率ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String rateId;

    /**
     * 源币种
     */
    private String fromCurrency;

    /**
     * 目标币种
     */
    private String toCurrency;

    /**
     * 汇率
     */
    private BigDecimal exchangeRate;

    /**
     * 生效日期
     */
    private Date effectiveDate;

    /**
     * 失效日期
     */
    private Date expiryDate;

    /**
     * 数据来源: MANUAL-手工录入, AUTO-自动获取, FIXED-固定汇率
     */
    private String dataSource;

    /**
     * 是否当前有效
     */
    private Boolean isCurrent;

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

