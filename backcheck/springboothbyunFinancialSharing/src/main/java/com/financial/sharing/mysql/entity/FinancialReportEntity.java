package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 财务报表实体类
 * 
 * @author Financial Sharing System
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("T_FINANCIAL_REPORT")
public class FinancialReportEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报表ID
     */
    @TableId(value = "REPORT_ID", type = IdType.ASSIGN_ID)
    private Long reportId;

    /**
     * 报表编码
     */
    @TableField("REPORT_CODE")
    private String reportCode;

    /**
     * 报表名称
     */
    @TableField("REPORT_NAME")
    private String reportName;

    /**
     * 报表类型(1资产负债表2利润表3现金流量表)
     */
    @TableField("REPORT_TYPE")
    private Integer reportType;

    /**
     * 报表期间
     */
    @TableField("REPORT_PERIOD")
    private String reportPeriod;

    /**
     * 报表数据(JSON格式)
     */
    @TableField("REPORT_DATA")
    private String reportData;

    /**
     * 报表状态(1草稿2已生成3已审核)
     */
    @TableField("REPORT_STATUS")
    private Integer reportStatus;

    /**
     * 生成时间
     */
    @TableField("GENERATE_TIME")
    private Date generateTime;

    /**
     * 账簿ID
     */
    @TableField("BOOK_ID")
    private Long bookId;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private Long tenantId;

    /**
     * 版本号
     */
    @TableField("VERSION")
    @Version
    private Integer version;

    /**
     * 删除标识(0否1是)
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField(value = "CREATOR", fill = FieldFill.INSERT)
    private Long creator;

    /**
     * 更新人
     */
    @TableField(value = "UPDATER", fill = FieldFill.INSERT_UPDATE)
    private Long updater;
}

