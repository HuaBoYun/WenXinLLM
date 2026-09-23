package com.financial.sharing.oracle.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 凭证模板实体类
 *
 * @author 赵工
 * @since 2025-12-07
 */
@Data
public class VoucherTemplateEntity {

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 模板类型(INCOME/EXPENSE/TRANSFER/ADJUSTMENT)
     */
    private String templateType;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 模板描述
     */
    private String description;

    /**
     * 模板版本
     */
    private String templateVersion;

    /**
     * 是否默认模板
     */
    private Integer isDefault;

    /**
     * 是否启用
     */
    private Integer isEnabled;

    /**
     * 是否系统模板
     */
    private Integer isSystem;

    /**
     * 默认借方科目ID
     */
    private String debitSubjectId;

    /**
     * 默认贷方科目ID
     */
    private String creditSubjectId;

    /**
     * 默认币种
     */
    private String currencyCode;

    /**
     * 默认汇率
     */
    private BigDecimal exchangeRate;

    /**
     * 是否需要附件
     */
    private Integer attachmentRequired;

    /**
     * 是否自动编号
     */
    private Integer autoNumbering;

    /**
     * 编号规则
     */
    private String numberingRule;

    /**
     * 有效期开始
     */
    private Date validFrom;

    /**
     * 有效期结束
     */
    private Date validTo;

    /**
     * 使用次数
     */
    private Integer usageCount;

    /**
     * 最后使用时间
     */
    private Date lastUsedDate;

    /**
     * 组织ID
     */
    private String orgId;

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

    /**
     * 模板配置(JSON)
     */
    private String templateConfig;

    /**
     * 备注
     */
    private String remark;

    // 非持久化字段
    /**
     * 模板分录列表
     */
    private List<VoucherTemplateEntryEntity> entries;

    /**
     * 模板字段列表
     */
    private List<VoucherTemplateFieldEntity> fields;

    /**
     * 当前版本信息
     */
    private VoucherTemplateVersionEntity currentVersion;

    /**
     * 使用统计
     */
    private TemplateUsageStats usageStats;

    /**
     * 测试结果
     */
    private TemplateTestResult testResult;

    @Data
    public static class TemplateUsageStats {
        private Long totalUsage;
        private Date lastUsageTime;
        private Long monthlyUsage;
        private List<String> topUsers;
        private List<String> topDepartments;
    }

    @Data
    public static class TemplateTestResult {
        private Boolean passed;
        private String status;
        private Integer testCount;
        private Integer passedCount;
        private Integer failedCount;
        private Date lastTestTime;
        private String lastTestResult;
    }
}