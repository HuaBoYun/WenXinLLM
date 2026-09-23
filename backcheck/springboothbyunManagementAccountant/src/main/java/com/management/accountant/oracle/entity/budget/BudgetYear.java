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
 * 预算年度实体类
 *
 * @author AI Agent
 * @date 2026-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_YEAR")
public class BudgetYear implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 年度ID (主键)
     */
    @TableId(value = "YEAR_ID", type = IdType.ASSIGN_UUID)
    private String yearId;

    /**
     * 预算年度
     */
    @TableField("BUDGET_YEAR")
    @ExcelField(title = "预算年度", sort = 10, width = 3000)
    private Integer budgetYear;

    /**
     * 年度状态
     * OPEN: 开启
     * CLOSED: 关闭
     * LOCKED: 锁定
     */
    @TableField("YEAR_STATUS")
    @ExcelField(title = "年度状态", sort = 20, width = 3000)
    private String yearStatus;

    /**
     * 开始日期
     */
    @TableField("START_DATE")
    @ExcelField(title = "开始日期", sort = 30, width = 4000, dataFormat = "yyyy-MM-dd")
    private Date startDate;

    /**
     * 结束日期
     */
    @TableField("END_DATE")
    @ExcelField(title = "结束日期", sort = 40, width = 4000, dataFormat = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 期间数量
     */
    @TableField("PERIOD_COUNT")
    @ExcelField(title = "期间数量", sort = 50, width = 3000)
    private Integer periodCount;

    /**
     * 年度描述
     */
    @TableField("YEAR_DESCRIPTION")
    @ExcelField(title = "年度描述", sort = 60, width = 6000)
    private String yearDescription;

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
     * 是否当前年度 0-否 1-是
     */
    @TableField("IS_CURRENT")
    private Integer isCurrent;

}
