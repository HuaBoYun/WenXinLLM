package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算配额操作历史实体
 * 对应表: TBL_BUDGET_QUOTA_HISTORY
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("TBL_BUDGET_QUOTA_HISTORY")
public class BudgetQuotaHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "HISTORY_ID", type = IdType.ASSIGN_UUID)
    private String historyId;

    /** 配额ID */
    @TableField("QUOTA_ID")
    private String quotaId;

    /** 操作类型: CREATE/UPDATE/DELETE/ADJUST/TRANSFER/ENABLE/DISABLE */
    @TableField("OPERATION_TYPE")
    private String operationType;

    /** 操作描述 */
    @TableField("OPERATION_DESC")
    private String operationDesc;

    /** 操作金额 */
    @TableField("AMOUNT")
    private BigDecimal amount;

    /** 修改前的值 */
    @TableField("BEFORE_VALUE")
    private String beforeValue;

    /** 修改后的值 */
    @TableField("AFTER_VALUE")
    private String afterValue;

    /** 操作人 */
    @TableField("OPERATOR")
    private String operator;

    /** 操作时间 */
    @TableField("OPERATE_TIME")
    private Date operateTime;

    /** IP地址 */
    @TableField("IP_ADDRESS")
    private String ipAddress;

    /** 创建时间 */
    @TableField("CREATE_TIME")
    private Date createTime;
}
