package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会计科目表 - Oracle/达梦版本
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_ACCOUNT_SUBJECT")
public class AccountSubjectEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 科目ID
     */
    @TableId(value = "SUBJECT_ID", type = IdType.ASSIGN_ID)
    private Long subjectId;

    /**
     * 科目编码
     */
    @TableField("SUBJECT_CODE")
    private String subjectCode;

    /**
     * 科目名称
     */
    @TableField("SUBJECT_NAME")
    private String subjectName;

    /**
     * 科目级次
     */
    @TableField("SUBJECT_LEVEL")
    private Integer subjectLevel;

    /**
     * 上级科目ID
     */
    @TableField("PARENT_SUBJECT_ID")
    private Long parentSubjectId;

    /**
     * 科目类型(1资产2负债3权益4收入5费用)
     */
    @TableField("SUBJECT_TYPE")
    private Integer subjectType;

    /**
     * 余额方向(1借方2贷方)
     */
    @TableField("BALANCE_DIRECTION")
    private Integer balanceDirection;

    /**
     * 是否末级(0否1是)
     */
    @TableField("IS_LEAF")
    private Integer isLeaf;

    /**
     * 是否启用(0否1是)
     */
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /**
     * 是否现金科目(0否1是)
     */
    @TableField("IS_CASH")
    private Integer isCash;

    /**
     * 是否银行科目(0否1是)
     */
    @TableField("IS_BANK")
    private Integer isBank;

    /**
     * 辅助核算类型
     */
    @TableField("AUXILIARY_TYPES")
    private String auxiliaryTypes;

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
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

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
