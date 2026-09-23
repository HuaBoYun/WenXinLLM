package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 辅助核算项查询参数
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AuxiliaryItemQueryParam extends PageableParam {

    /**
     * 辅助核算项编码
     */
    private String auxiliaryCode;

    /**
     * 辅助核算项名称
     */
    private String auxiliaryName;

    /**
     * 辅助核算类型
     */
    private String auxiliaryType;

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
     * 上级ID
     */
    private Long parentId;

    /**
     * 是否末级(0否1是)
     */
    private Integer isLeaf;
}
