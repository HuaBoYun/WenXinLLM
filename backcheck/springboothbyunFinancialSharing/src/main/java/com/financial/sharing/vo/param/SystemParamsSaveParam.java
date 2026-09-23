package com.financial.sharing.vo.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 系统参数保存参数
 * @author system
 * @since 2024-12-19
 */
@Data
public class SystemParamsSaveParam {

    /**
     * 参数ID（更新时需要）
     */
    private Long id;

    /**
     * 参数编码
     */
    @NotBlank(message = "参数编码不能为空")
    @Size(max = 50, message = "参数编码长度不能超过50个字符")
    private String paramCode;

    /**
     * 参数名称
     */
    @NotBlank(message = "参数名称不能为空")
    @Size(max = 100, message = "参数名称长度不能超过100个字符")
    private String paramName;

    /**
     * 分类编码
     */
    @NotBlank(message = "分类编码不能为空")
    @Size(max = 30, message = "分类编码长度不能超过30个字符")
    private String categoryCode;

    /**
     * 参数类型
     */
    @NotBlank(message = "参数类型不能为空")
    private String paramType;

    /**
     * 参数值
     */
    private String paramValue;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 是否必填
     */
    @NotNull(message = "是否必填不能为空")
    private Integer isRequired;

    /**
     * 是否启用
     */
    @NotNull(message = "是否启用不能为空")
    private Integer isEnabled;

    /**
     * 参数描述
     */
    @Size(max = 500, message = "参数描述长度不能超过500个字符")
    private String description;

    /**
     * 租户ID
     */
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    /**
     * 账簿ID
     */
    private Long bookId;
}