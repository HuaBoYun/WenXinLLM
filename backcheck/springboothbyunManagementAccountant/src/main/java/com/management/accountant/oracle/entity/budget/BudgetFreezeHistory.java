package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算冻结操作历史实体
 * 对应表: TBL_BUDGET_FREEZE_HISTORY
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("TBL_BUDGET_FREEZE_HISTORY")
public class BudgetFreezeHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "HISTORY_ID", type = IdType.ASSIGN_UUID)
    private String historyId;

    /** 冻结ID */
    @TableField("FREEZE_ID")
    private String freezeId;

    /** 操作类型: CREATE/UPDATE/DELETE/FREEZE/UNFREEZE/EXTEND/APPROVE */
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;

    /** IP地址 */
    @TableField("IP_ADDRESS")
    private String ipAddress;

    /** 创建时间 */
    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
