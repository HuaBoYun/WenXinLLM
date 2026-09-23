package com.financial.sharing.vo.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 辅助核算项保存参数
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
public class AuxiliaryItemSaveParam {

    /**
     * 辅助核算项ID（更新时必填）
     */
    private Long auxiliaryId;

    /**
     * 辅助核算项编码
     */
    @NotBlank(message = "辅助核算项编码不能为空")
    @Size(max = 50, message = "辅助核算项编码长度不能超过50个字符")
    private String auxiliaryCode;

    /**
     * 辅助核算项名称
     */
    @NotBlank(message = "辅助核算项名称不能为空")
    @Size(max = 200, message = "辅助核算项名称长度不能超过200个字符")
    private String auxiliaryName;

    /**
     * 辅助核算类型
     */
    @NotBlank(message = "辅助核算类型不能为空")
    @Size(max = 50, message = "辅助核算类型长度不能超过50个字符")
    private String auxiliaryType;

    /**
     * 上级ID
     */
    private Long parentId;

    /**
     * 是否末级(0否1是)
     */
    private Integer isLeaf;

    /**
     * 是否启用(0否1是)
     */
    private Integer isEnabled;

    /**
     * 排序号
     */
    private Integer sortOrder;

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
     * 创建人ID
     */
    private Long creator;

    /**
     * 更新人ID
     */
    private Long updater;
}
