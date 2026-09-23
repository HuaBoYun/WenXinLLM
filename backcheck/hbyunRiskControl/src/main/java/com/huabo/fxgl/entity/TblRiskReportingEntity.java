package com.huabo.fxgl.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@TableName("TBL_RISK_REPORTING")
@Schema(name="风险报送-相关风险事件填报")
public class TblRiskReportingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID")
    private BigDecimal id;

    @TableField(value = "ENT_NAME")
    @Schema(name="涉及企业名称")
    private String entName;
    @TableField(value = "ENT_LEVEL")
    @Schema(name="涉及企业层级")
    private String entLevel;
    @TableField(value = "RISK_NAME")
    @Schema(name="风险事件名称")
    private String riskName;
    @TableField(value = "RISK_TYPE")
    @Schema(name="风险类别")
    private String riskType;

    @TableField(value = "EVE_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(name="事件发生时间")
    private Date eveTime;

    @TableField(value = "SIT_DETAILS")
    @Schema(name="当期情况描述")
    private String sitDetails;

    @TableField(value = "LOSS_AMOUNT")
    @Schema(name="损失（风险）金额（万元）")
    private String lossAmount;

    @TableField(value = "DIS_SITUATION")
    @Schema(name="处置进展情况")
    private String disSituation;

    @TableField(value = "IS_IVN")
    @Schema(name="是否涉诉")
    private String isIvn;

    @TableField(value = "IS_OVER")
    @Schema(name="是否境外")
    private String isOver;

    @TableField(value = "REMARKS")
    @Schema(name="备注")
    private String remarks;

    @TableField(value = "FILL_SHOW")
    @Schema(name="填报说明")
    private String fillShow;

    @TableField(value = "FILL_STAFFID")
    @Schema(name="填报人")
    private BigDecimal fillStaffid;

    @TableField(exist = false)
    @Schema(name="填报人名称")
    private String fillStaffName;

    @TableField(value = "FILL_CREATE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(name="填报创建时间")
    private Date fillCreateDate;

    @TableField(value = "FILL_DEPTID")
    @Schema(name="填报所属部门")
    private BigDecimal fillDeptid;

    @TableField(exist = false)
    @Schema(name="填报所属部门名称")
    private String fillDeptName;

    @TableField(value = "FILL_UNIT")
    @Schema(name="填报所属单位")
    private BigDecimal fillUnit;

    @TableField(exist = false)
    @Schema(name="填报所属单位名称")
    private String fillUnitName;

    @TableField(value = "STATE")
    @Schema(name="0：未下发，1：已下发")
    private BigDecimal state;


}
