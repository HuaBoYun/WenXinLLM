package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 会计期间查询参数
 *
 * @author system
 * @since 2025-12-08
 */
@Data
@ApiModel(value = "AccountingPeriodQueryParam对象", description = "会计期间查询参数")
public class AccountingPeriodQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页码")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "每页数量")
    private Integer pageSize = 20;

    @ApiModelProperty(value = "期间ID")
    private Long periodId;

    @ApiModelProperty(value = "年度")
    private Integer yearNo;

    @ApiModelProperty(value = "月份")
    private Integer monthNo;

    @ApiModelProperty(value = "期间编码")
    private String periodCode;

    @ApiModelProperty(value = "期间名称")
    private String periodName;

    @ApiModelProperty(value = "期间状态")
    private String periodStatus;

    @ApiModelProperty(value = "是否当前期间")
    private Integer isCurrent;

    @ApiModelProperty(value = "开始日期-开始")
    private Date startDateBegin;

    @ApiModelProperty(value = "开始日期-结束")
    private Date startDateEnd;

    @ApiModelProperty(value = "结束日期-开始")
    private Date endDateBegin;

    @ApiModelProperty(value = "结束日期-结束")
    private Date endDateEnd;

    @ApiModelProperty(value = "创建人")
    private String createdBy;

    @ApiModelProperty(value = "账簿ID")
    private Long bookId;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}