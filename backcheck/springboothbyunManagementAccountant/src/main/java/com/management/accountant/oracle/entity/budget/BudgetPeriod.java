package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.management.accountant.util.excel.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算期间实体类
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_PERIOD")
public class BudgetPeriod implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 期间ID (主键)
     */
    @TableId(value = "PERIOD_ID", type = IdType.ASSIGN_UUID)
    private String periodId;

    /**
     * 期间编码
     */
    @TableField("PERIOD_CODE")
    @ExcelField(title = "期间编码", sort = 10, width = 4000)
    private String periodCode;

    /**
     * 期间名称
     */
    @TableField("PERIOD_NAME")
    @ExcelField(title = "期间名称", sort = 20, width = 5000)
    private String periodName;

    /**
     * 预算年度
     */
    @TableField("BUDGET_YEAR")
    @ExcelField(title = "预算年度", sort = 30, width = 3000)
    private Integer budgetYear;

    /**
     * 期间类型
     * YEARLY: 年度
     * QUARTERLY: 季度
     * MONTHLY: 月度
     * WEEKLY: 周
     * DAILY: 日
     */
    @TableField("PERIOD_TYPE")
    @ExcelField(title = "期间类型", sort = 40, width = 3000)
    private String periodType;

    /**
     * 开始日期
     */
    @TableField("START_DATE")
    @ExcelField(title = "开始日期", sort = 50, width = 4000, dataFormat = "yyyy-MM-dd")
    private Date startDate;

    /**
     * 结束日期
     */
    @TableField("END_DATE")
    @ExcelField(title = "结束日期", sort = 60, width = 4000, dataFormat = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 期间状态
     * OPEN: 开放
     * CLOSED: 关闭
     * LOCKED: 锁定
     */
    @TableField("PERIOD_STATUS")
    @ExcelField(title = "期间状态", sort = 70, width = 3000)
    private String periodStatus;

    /**
     * 是否当前期间
     */
    @TableField("IS_CURRENT")
    @ExcelField(title = "是否当前期间", sort = 80, width = 3000)
    private Integer isCurrent;

    /**
     * 是否锁定
     */
    @TableField("IS_LOCKED")
    @ExcelField(title = "是否锁定", sort = 90, width = 3000)
    private Integer isLocked;

    /**
     * 期间编号
     */
    @TableField("PERIOD_NUMBER")
    private Integer periodNumber;

    /**
     * 排序序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 期间描述
     */
    @TableField("PERIOD_DESCRIPTION")
    @ExcelField(title = "期间描述", sort = 100, width = 8000)
    private String periodDescription;

    /**
     * 是否自动开启
     */
    @TableField("AUTO_OPEN")
    @ExcelField(title = "是否自动开启", sort = 110, width = 3000)
    private Integer autoOpen;

    /**
     * 是否允许调整
     */
    @TableField("ALLOW_ADJUSTMENT")
    @ExcelField(title = "是否允许调整", sort = 120, width = 3000)
    private Integer allowAdjustment;

    /**
     * 期间天数
     */
    @TableField("PERIOD_DAYS")
    @ExcelField(title = "期间天数", sort = 130, width = 3000)
    private Integer periodDays;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 删除标志
     */
    @TableField("DEL_FLAG")
    private Integer delFlag;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

}
