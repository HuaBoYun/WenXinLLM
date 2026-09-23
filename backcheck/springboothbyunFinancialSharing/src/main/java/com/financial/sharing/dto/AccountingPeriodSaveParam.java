package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 会计期间保存参数
 *
 * @author system
 * @since 2025-12-08
 */
@Data
@ApiModel(value = "AccountingPeriodSaveParam对象", description = "会计期间保存参数")
public class AccountingPeriodSaveParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "期间ID（更新时需要）")
    private Long periodId;

    @ApiModelProperty(value = "年度")
    private Integer yearNo;

    @ApiModelProperty(value = "月份")
    private Integer monthNo;

    @ApiModelProperty(value = "期间编码")
    private String periodCode;

    @ApiModelProperty(value = "期间名称")
    private String periodName;

    @ApiModelProperty(value = "开始日期")
    private Date startDate;

    @ApiModelProperty(value = "结束日期")
    private Date endDate;

    @ApiModelProperty(value = "期间状态")
    private String periodStatus;

    @ApiModelProperty(value = "是否当前期间")
    private Integer isCurrent;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "账簿ID")
    private Long bookId;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}