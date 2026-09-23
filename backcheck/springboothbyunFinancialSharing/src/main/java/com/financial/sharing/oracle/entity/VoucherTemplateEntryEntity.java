package com.financial.sharing.oracle.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 凭证模板分录配置实体类
 *
 * @author 赵工
 * @since 2025-12-07
 */
@Data
public class VoucherTemplateEntryEntity {

    /**
     * 分录ID
     */
    private Long entryId;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 分录序号
     */
    private Integer entryNo;

    /**
     * 分录类型(DEBIT/CREDIT)
     */
    private String entryType;

    /**
     * 科目ID
     */
    private String subjectId;

    /**
     * 科目编码
     */
    private String subjectCode;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 部门ID
     */
    private String departmentId;

    /**
     * 员工ID
     */
    private String employeeId;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 客户ID
     */
    private String customerId;

    /**
     * 供应商ID
     */
    private String supplierId;

    /**
     * 金额类型(FIXED/CALCULATED/INPUT)
     */
    private String amountType;

    /**
     * 金额计算公式
     */
    private String amountFormula;

    /**
     * 默认金额
     */
    private BigDecimal defaultAmount;

    /**
     * 金额来源
     */
    private String amountSource;

    /**
     * 摘要模板
     */
    private String descriptionTemplate;

    /**
     * 是否必填
     */
    private Integer isRequired;

    /**
     * 是否可编辑
     */
    private Integer isEditable;

    /**
     * 是否计算字段
     */
    private Integer isCalculated;

    /**
     * 计算规则
     */
    private String calculationRule;

    /**
     * 验证规则
     */
    private String validationRule;

    /**
     * 显示顺序
     */
    private Integer displayOrder;

    /**
     * 条件规则
     */
    private String conditionRule;

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
     * 分录配置(JSON)
     */
    private String entryConfig;

    // 非持久化字段
    /**
     * 科目信息
     */
    private Map<String, Object> subjectInfo;

    /**
     * 部门信息
     */
    private Map<String, Object> departmentInfo;

    /**
     * 员工信息
     */
    private Map<String, Object> employeeInfo;

    /**
     * 计算结果
     */
    private Object calculatedValue;
}