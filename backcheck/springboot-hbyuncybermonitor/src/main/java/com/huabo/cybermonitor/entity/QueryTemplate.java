package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 查询模板实体类
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("SYS_QUERY_TEMPLATE")
public class QueryTemplate {

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
     * 模板类型
     */
    private String templateType;

    /**
     * 查询SQL
     */
    private String querySql;

    /**
     * 查询参数配置
     */
    private String queryParams;

    /**
     * 结果字段配置
     */
    private String resultFields;

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

    // 模板类型常量
    public static final String TYPE_ENTERPRISE_QUERY = "ENTERPRISE_QUERY";
    public static final String TYPE_FINANCIAL_QUERY = "FINANCIAL_QUERY";
    public static final String TYPE_RISK_QUERY = "RISK_QUERY";
    public static final String TYPE_ASSET_QUERY = "ASSET_QUERY";
    public static final String TYPE_CUSTOM_QUERY = "CUSTOM_QUERY";
    public static final String TYPE_STATISTICAL_QUERY = "STATISTICAL_QUERY";

    // Manual getter for Lombok compatibility
    public String getTemplateId() {
        return this.templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}
