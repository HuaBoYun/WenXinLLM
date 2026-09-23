package com.financial.sharing.mysql.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 凭证模板字段配置实体类
 *
 * @author 赵工
 * @since 2025-12-07
 */
@Data
public class VoucherTemplateFieldEntity {

    /**
     * 字段ID
     */
    private Long fieldId;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 字段编码
     */
    private String fieldCode;

    /**
     * 字段类型(INPUT/SELECT/DATE/NUMBER/TEXT)
     */
    private String fieldType;

    /**
     * 显示名称
     */
    private String displayName;

    /**
     * 是否必填
     */
    private Integer isRequired;

    /**
     * 是否可见
     */
    private Integer isVisible;

    /**
     * 是否可编辑
     */
    private Integer isEditable;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 验证规则
     */
    private String validationRule;

    /**
     * 数据源
     */
    private String dataSource;

    /**
     * 选项配置(JSON)
     */
    private String optionsConfig;

    /**
     * 显示顺序
     */
    private Integer displayOrder;

    /**
     * 字段宽度
     */
    private Integer fieldWidth;

    /**
     * 字段高度
     */
    private Integer fieldHeight;

    /**
     * 占位符
     */
    private String placeholder;

    /**
     * 帮助文本
     */
    private String helpText;

    /**
     * 账套ID
     */
    private String bookId;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 更新人
     */
    private Long updater;

    // 非持久化字段
    /**
     * 选项列表
     */
    private List<Map<String, Object>> options;

    /**
     * 字段值
     */
    private Object fieldValue;

    /**
     * 是否有错误
     */
    private Boolean hasError;

    /**
     * 错误信息
     */
    private String errorMessage;
}