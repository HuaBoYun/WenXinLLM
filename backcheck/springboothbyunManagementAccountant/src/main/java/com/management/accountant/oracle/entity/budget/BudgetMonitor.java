package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.management.accountant.util.excel.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算监控实体
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_BUDGET_MONITOR")
public class BudgetMonitor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 监控ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String monitorId;

    /**
     * 监控编码
     */
    @ExcelField(title = "监控编码", sort = 10, width = 4000)
    private String monitorCode;

    /**
     * 监控名称
     */
    @ExcelField(title = "监控名称", sort = 20, width = 6000)
    private String monitorName;

    /**
     * 监控类型
     */
    @ExcelField(title = "监控类型", sort = 30, width = 3000)
    private String monitorType;

    /**
     * 预算ID
     */
    private String budgetId;

    /**
     * 监控状态 (ACTIVE/WARNING/ALERT/CRITICAL)
     */
    @ExcelField(title = "监控状态", sort = 40, width = 3000)
    private String monitorStatus;

    /**
     * 阈值
     */
    @ExcelField(title = "阈值", sort = 50, width = 4000, dataFormat = "#,##0.00")
    private BigDecimal thresholdValue;

    /**
     * 当前值
     */
    @ExcelField(title = "当前值", sort = 60, width = 4000, dataFormat = "#,##0.00")
    private BigDecimal currentValue;

    /**
     * 告警级别
     */
    @ExcelField(title = "告警级别", sort = 70, width = 3000)
    private String alertLevel;

    /**
     * 组织ID
     */
    private String organizationId;

    /**
     * 最后检查时间
     */
    @ExcelField(title = "最后检查时间", sort = 80, width = 5000, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastCheckTime;

    /**
     * 检查间隔（秒）
     */
    private Integer checkInterval;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelField(title = "创建时间", sort = 90, width = 5000, dataFormat = "yyyy-MM-dd HH:mm:ss")
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
     * 删除标志 (0-未删除 1-已删除)
     */
    private Integer delFlag;

    /**
     * 备注
     */
    private String remark;
}

