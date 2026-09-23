package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 预算分析报告实体
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_BUDGET_ANALYSIS_REPORT")
public class BudgetAnalysisReport {

    /** 主键ID */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 报告编码
     */
    private String reportCode;

    /**
     * 报告名称
     */
    private String reportName;

    /**
     * 报告类型(variance-差异分析, trend-趋势分析, performance-绩效分析, comprehensive-综合分析)
     */
    private String reportType;

    /**
     * 预算年度
     */
    private Integer budgetYear;

    /**
     * 报告期间
     */
    private String reportPeriod;

    /**
     * 组织ID
     */
    private String organizationId;

    /**
     * 组织名称
     */
    private String organizationName;

    /**
     * 报告摘要
     */
    private String reportSummary;

    /**
     * 报告内容(富文本)
     */
    private String reportContent;

    /**
     * 关键发现(JSON格式)
     */
    private String keyFindings;

    /**
     * 数据分析(JSON格式)
     */
    private String dataAnalysis;

    /**
     * 图表数据(JSON格式)
     */
    private String chartData;

    /**
     * 结论与建议
     */
    private String conclusionAndSuggestion;

    /**
     * 附件路径
     */
    private String attachmentPath;

    /**
     * 报告模板ID
     */
    private String templateId;

    /**
     * 报告模板名称（冗余字段，方便前端展示）
     */
    private String templateName;

    /**
     * 报告格式(pdf, excel, word)
     */
    private String reportFormat;

    /**
     * 输出格式列表（JSON，如 ["PDF","EXCEL"]）
     */
    private String outputFormats;

    /**
     * 报告状态(GENERATING-生成中, COMPLETED-已完成, FAILED-生成失败, SCHEDULED-已调度)
     */
    private String reportStatus;

    /**
     * 生成进度（0-100）
     */
    private Integer generateProgress;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 下载次数
     */
    private Integer downloadCount;

    /**
     * 创建人（前端展示用）
     */
    private String creator;

    /**
     * 生成时间（前端展示用，格式化字符串）
     */
    private String generateTime;

    /**
     * 分析期间开始日期
     */
    private String periodStart;

    /**
     * 分析期间结束日期
     */
    private String periodEnd;

    /**
     * 报告描述
     */
    private String description;

    /**
     * 生成人
     */
    private String generatedBy;

    /**
     * 生成时间（Date类型）
     */
    private Date generatedTime;

    /**
     * 审核人
     */
    private String reviewedBy;

    /**
     * 审核时间
     */
    private Date reviewedTime;

    /**
     * 发布人
     */
    private String publishedBy;

    /**
     * 发布时间
     */
    private Date publishedTime;

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
     * 删除标志: 0-正常, 1-已删除
     */
    private Integer delFlag;

    /**
     * 预览URL
     */
    private String previewUrl;

    /**
     * 生成日志（JSON格式）
     */
    private String generationLogs;

}

