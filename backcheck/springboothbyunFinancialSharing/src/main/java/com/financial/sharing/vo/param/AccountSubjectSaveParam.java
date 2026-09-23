package com.financial.sharing.vo.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 会计科目保存参数
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
public class AccountSubjectSaveParam {

    /**
     * 科目ID（更新时必填）
     */
    private Long subjectId;

    /**
     * 科目编码
     */
    @NotBlank(message = "科目编码不能为空")
    @Size(max = 50, message = "科目编码长度不能超过50个字符")
    private String subjectCode;

    /**
     * 科目名称
     */
    @NotBlank(message = "科目名称不能为空")
    @Size(max = 200, message = "科目名称长度不能超过200个字符")
    private String subjectName;

    /**
     * 上级科目ID
     */
    private Long parentSubjectId;

    /**
     * 科目类型(1资产2负债3权益4收入5费用)
     */
    @NotNull(message = "科目类型不能为空")
    private Integer subjectType;

    /**
     * 余额方向(1借方2贷方)
     */
    @NotNull(message = "余额方向不能为空")
    private Integer balanceDirection;

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
    @Size(max = 500, message = "辅助核算类型长度不能超过500个字符")
    private String auxiliaryTypes;

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
     * 版本号
     */
    private Integer version;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 更新人
     */
    private Long updater;
}
