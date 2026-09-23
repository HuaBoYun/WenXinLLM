package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 会计科目查询参数
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountSubjectQueryParam extends PageableParam {

    /**
     * 科目编码
     */
    private String subjectCode;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 科目类型(1资产2负债3权益4收入5费用)
     */
    private Integer subjectType;

    /**
     * 科目级次
     */
    private Integer subjectLevel;

    /**
     * 是否启用(0否1是)
     */
    private Integer isEnabled;

    /**
     * 账簿ID
     */
    @NotNull(message = "账簿ID不能为空")
    private Long bookId;

    /**
     * 租户ID
     */
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    /**
     * 上级科目ID
     */
    private Long parentSubjectId;

    /**
     * 是否末级(0否1是)
     */
    private Integer isLeaf;

    /**
     * 余额方向(1借方2贷方)
     */
    private Integer balanceDirection;
}
