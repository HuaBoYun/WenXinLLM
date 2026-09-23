package com.management.accountant.oracle.entity.integration;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * BI报表实体类
 *
 * @author AI Agent
 * @date 2026-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_BI_REPORT")
public class BudgetBiReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "REPORT_ID", type = IdType.ASSIGN_UUID)
    private String reportId;

    @TableField("BI_ID")
    private String biId;

    @TableField("REPORT_NAME")
    private String reportName;

    @TableField("REPORT_TYPE")
    private String reportType;

    @TableField("DATA_SOURCE")
    private String dataSource;

    @TableField("LAST_REFRESH")
    private Date lastRefresh;

    @TableField("VIEW_COUNT")
    private Integer viewCount;

    @TableField("STATUS")
    private String status;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATED_BY")
    private String createdBy;

    @TableField("CREATED_TIME")
    private Date createdTime;

    @TableField("UPDATED_BY")
    private String updatedBy;

    @TableField("UPDATED_TIME")
    private Date updatedTime;
}
