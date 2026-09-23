package com.financial.sharing.business.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 私车公用档案表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_CWGX_PRIVATE_CAR")
@ApiModel(value = "TblPrivateCar", description = "私车公用档案表")
public class TblPrivateCar implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "CAR_ID")
    @ApiModelProperty(value = "车辆ID")
    private String carId;

    @TableField("CAR_NUMBER")
    @ApiModelProperty(value = "车牌号")
    private String carNumber;

    @TableField("CAR_BRAND")
    @ApiModelProperty(value = "车辆品牌")
    private String carBrand;

    @TableField("CAR_MODEL")
    @ApiModelProperty(value = "车辆型号")
    private String carModel;

    @TableField("OWNER_NAME")
    @ApiModelProperty(value = "车主姓名")
    private String ownerName;

    @TableField("OWNER_ID")
    @ApiModelProperty(value = "车主ID")
    private String ownerId;

    @TableField("DEPARTMENT_ID")
    @ApiModelProperty(value = "所属部门ID")
    private String departmentId;

    @TableField("DEPARTMENT_NAME")
    @ApiModelProperty(value = "所属部门名称")
    private String departmentName;

    @TableField("ENGINE_DISPLACEMENT")
    @ApiModelProperty(value = "排量")
    private String engineDisplacement;

    @TableField("SUBSIDY_STANDARD")
    @ApiModelProperty(value = "补贴标准(元/公里)")
    private BigDecimal subsidyStandard;

    @TableField("TOTAL_MILEAGE")
    @ApiModelProperty(value = "总里程")
    private BigDecimal totalMileage;

    @TableField("TOTAL_SUBSIDY")
    @ApiModelProperty(value = "总补贴金额")
    private BigDecimal totalSubsidy;

    @TableField("STATUS")
    @ApiModelProperty(value = "状态(ACTIVE-正常,INACTIVE-停用)")
    private String status;

    @TableField("REGISTRATION_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "注册日期")
    private LocalDateTime registrationDate;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @TableField("IS_ENABLED")
    @ApiModelProperty(value = "是否启用")
    private Integer isEnabled;
}
