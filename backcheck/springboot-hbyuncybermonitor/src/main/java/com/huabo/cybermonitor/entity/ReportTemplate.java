package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 报表模板实体类
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("SYS_REPORT_TEMPLATE")
public class ReportTemplate {

    /**
     * 模板ID
     */
    @TableId(value = "TEMPLATE_ID", type = IdType.ASSIGN_UUID)
    private String templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 报表类型
     */
    private String reportType;

    /**
     * 报表分类
     */
    private String reportCategory;

    /**
     * 数据源配置
     */
    private String dataSource;

    /**
     * 报表配置
     */
    private String reportConfig;

    /**
     * 图表配置
     */
    private String chartConfig;

    /**
     * 导出格式
     */
    private String exportFormat;

    /**
     * 模板描述
     */
    private String templateDescription;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 是否公开
     */
    private Boolean isPublic;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    // 报表类型常量
    public static final String TYPE_STATISTICAL = "STATISTICAL";
    public static final String TYPE_ANALYTICAL = "ANALYTICAL";
    public static final String TYPE_DASHBOARD = "DASHBOARD";
    public static final String TYPE_DETAIL = "DETAIL";
    public static final String TYPE_SUMMARY = "SUMMARY";
    public static final String TYPE_TREND = "TREND";

    // 报表分类常量
    public static final String CATEGORY_ENTERPRISE = "ENTERPRISE";
    public static final String CATEGORY_FINANCIAL = "FINANCIAL";
    public static final String CATEGORY_RISK = "RISK";
    public static final String CATEGORY_ASSET = "ASSET";
    public static final String CATEGORY_OPERATION = "OPERATION";
    public static final String CATEGORY_COMPLIANCE = "COMPLIANCE";

    // 导出格式常量
    public static final String FORMAT_EXCEL = "EXCEL";
    public static final String FORMAT_PDF = "PDF";
    public static final String FORMAT_WORD = "WORD";
    public static final String FORMAT_CSV = "CSV";
    public static final String FORMAT_JSON = "JSON";
}
