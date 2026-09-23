package com.financial.sharing.oracle.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 凭证模板DTO
 *
 * @author system
 * @since 2024-12-19
 */
@Data
@ApiModel("凭证模板DTO")
public class VoucherTemplateDTO {

    @ApiModelProperty("模板ID")
    private Long templateId;

    @ApiModelProperty("模板编码")
    private String templateCode;

    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("模板类型")
    private String templateType;

    @ApiModelProperty("模板描述")
    private String description;

    @ApiModelProperty("借方科目ID")
    private Long debitSubjectId;

    @ApiModelProperty("贷方科目ID")
    private Long creditSubjectId;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("摘要")
    private String summary;

    @ApiModelProperty("辅助核算类型")
    private String auxiliaryType;

    @ApiModelProperty("辅助核算ID")
    private Long auxiliaryId;

    @ApiModelProperty("是否启用")
    private Boolean enabled = true;

    @ApiModelProperty("模板版本")
    private Integer version = 1;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("创建人")
    private Long creator;

    @ApiModelProperty("更新人")
    private Long updater;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("账簿ID")
    private Long bookId;
}