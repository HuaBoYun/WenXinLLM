package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.management.accountant.util.excel.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算控制规则实体类
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_CONTROL_RULE")
public class BudgetControlRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则ID (主键)
     */
    @TableId(value = "RULE_ID", type = IdType.ASSIGN_UUID)
    private String ruleId;

    /**
     * 规则编码
     */
    @TableField("RULE_CODE")
    @ExcelField(title = "规则编码", sort = 10, width = 4000)
    private String ruleCode;

    /**
     * 规则名称
     */
    @TableField("RULE_NAME")
    @ExcelField(title = "规则名称", sort = 20, width = 6000)
    private String ruleName;

    /**
     * 控制类型
     */
    @TableField("CONTROL_TYPE")
    @ExcelField(title = "控制类型", sort = 30, width = 3000)
    private String controlType;

    /**
     * 预警阈值 DECIMAL(5,2)
     */
    @TableField("WARNING_THRESHOLD")
    @ExcelField(title = "预警阈值", sort = 40, width = 3000, dataFormat = "0.00")
    private BigDecimal warningThreshold;

    /**
     * 控制阈值 DECIMAL(5,2)
     */
    @TableField("CONTROL_THRESHOLD")
    @ExcelField(title = "控制阈值", sort = 50, width = 3000, dataFormat = "0.00")
    private BigDecimal controlThreshold;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    @ExcelField(title = "生效日期", sort = 60, width = 4000, dataFormat = "yyyy-MM-dd")
    private Date effectiveDate;

    /**
     * 失效日期
     */
    @TableField("EXPIRY_DATE")
    @ExcelField(title = "失效日期", sort = 70, width = 4000, dataFormat = "yyyy-MM-dd")
    private Date expiryDate;

    /**
     * 是否启用 (TINYINT DEFAULT 1)
     */
    @TableField("IS_ENABLED")
    @ExcelField(title = "是否启用", sort = 80, width = 2500)
    private Boolean isEnabled;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ExcelField(title = "创建时间", sort = 90, width = 5000, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 备注
     */
    @TableField("REMARK")
    @ExcelField(title = "备注", sort = 100, width = 6000)
    private String remark;

    /**
     * 删除标志 (0:未删除, 1:已删除)
     */
    @TableField("DEL_FLAG")
    private Integer delFlag;
}

