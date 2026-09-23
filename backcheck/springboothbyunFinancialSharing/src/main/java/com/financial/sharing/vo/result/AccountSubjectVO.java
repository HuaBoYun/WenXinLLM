package com.financial.sharing.vo.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会计科目返回对象
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
public class AccountSubjectVO {

    /**
     * 科目ID
     */
    private Long subjectId;

    /**
     * 科目编码
     */
    private String subjectCode;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 科目级次
     */
    private Integer subjectLevel;

    /**
     * 上级科目ID
     */
    private Long parentSubjectId;

    /**
     * 上级科目名称
     */
    private String parentSubjectName;

    /**
     * 科目类型(1资产2负债3权益4收入5费用)
     */
    private Integer subjectType;

    /**
     * 科目类型名称
     */
    private String subjectTypeName;

    /**
     * 余额方向(1借方2贷方)
     */
    private Integer balanceDirection;

    /**
     * 余额方向名称
     */
    private String balanceDirectionName;

    /**
     * 是否末级(0否1是)
     */
    private Integer isLeaf;

    /**
     * 是否启用(0否1是)
     */
    private Integer isEnabled;

    /**
     * 是否现金科目(0否1是)
     */
    private Integer isCash;

    /**
     * 是否银行科目(0否1是)
     */
    private Integer isBank;

    /**
     * 辅助核算类型
     */
    private String auxiliaryTypes;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 更新人
     */
    private Long updater;
}
