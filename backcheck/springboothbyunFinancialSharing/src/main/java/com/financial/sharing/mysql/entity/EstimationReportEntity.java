package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 估算报告实体类
 * 对应表：TBL_ESTIMATION_REPORT
 *
 * @author Financial Sharing System
 * @since 2024-12-29
 */
@Data
@TableName("TBL_ESTIMATION_REPORT")
public class EstimationReportEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报告ID
     */
    @TableId(value = "REPORT_ID", type = IdType.ASSIGN_ID)
    private String reportId;

    /**
     * 报告编号
     */
    @TableField("REPORT_NO")
    private String reportNo;

    /**
     * 报告名称
     */
    @TableField("REPORT_NAME")
    private String reportName;

    /**
     * 报告类型（SCHEME-方案报告, MODEL-模型报告, SIMULATION-模拟报告, VARIANCE-差异报告）
     */
    @TableField("REPORT_TYPE")
    private String reportType;

    /**
     * 报告期间
     */
    @TableField("REPORT_PERIOD")
    private String reportPeriod;

    /**
     * 关联ID（方案ID/模型ID/模拟ID等）
     */
    @TableField("RELATED_ID")
    private String relatedId;

    /**
     * 报告内容（JSON格式）
     */
    @TableField("REPORT_CONTENT")
    private String reportContent;

    /**
     * 报告摘要
     */
    @TableField("REPORT_SUMMARY")
    private String reportSummary;

    /**
     * 生成人ID
     */
    @TableField("GENERATOR_ID")
    private String generatorId;

    /**
     * 生成人姓名
     */
    @TableField("GENERATOR_NAME")
    private String generatorName;

    /**
     * 生成时间
     */
    @TableField("GENERATION_TIME")
    private Date generationTime;

    /**
     * 报告状态（0-草稿, 1-已生成, 2-已发布）
     */
    @TableField("REPORT_STATUS")
    private Integer reportStatus;

    /**
     * 文件路径
     */
    @TableField("FILE_PATH")
    private String filePath;

    /**
     * 账套ID
     */
    @TableField("BOOK_ID")
    private String bookId;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;
}

