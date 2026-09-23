package com.management.accountant.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算版本DTO
 * 
 * @author AI Agent
 * @date 2026-01-29
 */
@Data
@ApiModel("预算版本DTO")
public class BudgetVersionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("版本ID")
    private String versionId;

    @ApiModelProperty("预算ID")
    private String budgetId;

    @ApiModelProperty("预算名称")
    private String budgetName;

    @ApiModelProperty("版本号")
    private String versionNo;

    @ApiModelProperty("版本名称")
    private String versionName;

    @ApiModelProperty("版本类型")
    private String versionType;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("是否当前版本")
    private Integer isCurrent;

    @ApiModelProperty("是否基准版本")
    private Integer isBaseline;

    @ApiModelProperty("数据快照")
    private String dataSnapshot;

    @ApiModelProperty("变更说明")
    private String changeLog;

    @ApiModelProperty("版本标签")
    private String versionTag;

    @ApiModelProperty("父版本ID")
    private String parentVersionId;

    @ApiModelProperty("发布时间")
    private Date publishTime;

    @ApiModelProperty("发布人ID")
    private String publishBy;

    @ApiModelProperty("发布人姓名")
    private String publishByName;

    @ApiModelProperty("生效日期")
    private Date effectiveDate;

    @ApiModelProperty("失效日期")
    private Date expiryDate;

    @ApiModelProperty("数据条数")
    private Integer dataCount;

    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("公司ID")
    private String companyId;

    @ApiModelProperty("公司名称")
    private String companyName;
}

