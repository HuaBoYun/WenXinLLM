package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 单据配置保存参数
 */
@Data
@ApiModel("单据配置保存参数")
public class TblBillConfigSaveParam {

    @ApiModelProperty("配置ID")
    private String configId;

    @ApiModelProperty("配置名称")
    private String configName;

    @ApiModelProperty("配置编码")
    private String configCode;

    @ApiModelProperty("单据类型")
    private String billType;

    @ApiModelProperty(value = "单据类型编码", required = true)
    @NotBlank(message = "单据类型编码不能为空")
    private String billTypeCode;

    @ApiModelProperty(value = "单据类型名称", required = true)
    @NotBlank(message = "单据类型名称不能为空")
    private String billTypeName;

    @ApiModelProperty("单据编码")
    private String billCode;

    @ApiModelProperty("单据名称")
    private String billName;

    @ApiModelProperty("单据模板")
    private String billTemplate;

    @ApiModelProperty("配置内容")
    private String configContent;

    @ApiModelProperty("OCR提供商")
    private String ocrProvider;

    @ApiModelProperty("是否启用OCR")
    private Integer ocrEnabled;

    @ApiModelProperty("是否启用审批")
    private Integer auditEnabled;

    @ApiModelProperty("是否启用自动匹配")
    private Integer autoMatchEnabled;

    @ApiModelProperty("是否启用重复检查")
    private Integer duplicateCheckEnabled;

    @ApiModelProperty("是否启用")
    private Integer isEnabled;

    @ApiModelProperty("排序号")
    private Integer sortOrder;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("组织ID")
    private String orgId;

    @ApiModelProperty("组织名称")
    private String orgName;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("创建人")
    private String createUser;

    @ApiModelProperty("更新人")
    private String updateUser;

    @ApiModelProperty("备注")
    private String remark;
}

