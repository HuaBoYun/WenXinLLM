package com.financial.sharing.entity;

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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 价格信息表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_TRAVEL_ARCHIVE_PRICE")
@ApiModel(value = "TblTravelArchivePrice", description = "价格信息表")
public class TblTravelArchivePrice implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "PRICE_ID")
    @ApiModelProperty(value = "价格ID")
    private String priceId;

    @TableField("ARCHIVE_ID")
    @ApiModelProperty(value = "档案ID")
    private String archiveId;

    // ========== 原有字段 ==========
    @TableField("CITY")
    @ApiModelProperty(value = "城市")
    private String city;

    @TableField("ROOM_TYPE")
    @ApiModelProperty(value = "房型")
    private String roomType;

    @TableField("VEHICLE_TYPE")
    @ApiModelProperty(value = "车型")
    private String vehicleType;

    @TableField("PRICE")
    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    // ========== 新增字段 ==========
    @TableField("PRICE_TYPE")
    @ApiModelProperty(value = "价格类型")
    private String priceType;

    @TableField("PRICE_DESCRIPTION")
    @ApiModelProperty(value = "价格说明")
    private String priceDescription;

    @TableField("UNIT_PRICE")
    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @TableField("CURRENCY")
    @ApiModelProperty(value = "币种")
    private String currency;

    @TableField("EFFECTIVE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "生效日期")
    private LocalDate effectiveDate;

    @TableField("EXPIRY_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "失效日期")
    private LocalDate expiryDate;

    @TableField("IS_ENABLED")
    @ApiModelProperty(value = "是否启用")
    private Integer isEnabled;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @TableField("UPDATE_USER")
    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
