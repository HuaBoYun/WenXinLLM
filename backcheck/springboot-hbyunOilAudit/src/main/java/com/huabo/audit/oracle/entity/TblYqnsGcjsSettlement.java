package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 央企模块-计划编制-工程结算审计项目汇总
 * @TableName TBL_YQNS_GCJS_SETTLEMENT
 */
@TableName(value ="TBL_YQNS_GCJS_SETTLEMENT")
@Data
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblYqnsGcjsSettlement extends BaseReservedProperty {

	public static final String NOID = "60071";
	
    /**
     * 工程结算审计项目汇总主键
     */
    @Schema(name = "工程结算审计项目汇总主键")
    @TableId(value = "SETTLEMENTID", type = IdType.INPUT)
    private BigDecimal settlementid;
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

    /**
     * 序号
     */
    @Schema(name = "序号")
    @TableField(value = "SORTINDEX")
    private BigDecimal sortindex;

    /**
     * 合同编号
     */
    @Schema(name = "合同编号")
    @TableField(value = "SETTLEMENTCODE")
    private String settlementcode;


    /**
     * 工程名称
     */
    @Schema(name = "工程名称")
    @TableField(value = "SETTLEMENTNAME")
    private String settlementname;

    /**
     * 建设单位id
     */
    @Schema(name = "建设单位id")
    @TableField(value = "SETTLEMENTUNITID")
    private BigDecimal settlementunitid;

    /**
     * 建设单位名称
     */
    @Schema(name = "建设单位名称")
    @TableField(value = "SETTLEMENTUNITNAME")
    private String settlementunitname;

    /**
     * 二审审查金额
     */
    @Schema(name = "二审审查金额")
    @TableField(value = "SETTLEMENTINSTANCEAMOUNT")
    private String settlementinstanceamount;


    /**
     * 施工单位id
     */
    @Schema(name = "施工单位id")
    @TableField(value = "SETTLEMENTCONSTTUNITID")
    private BigDecimal settlementconsttunitid;

    /**
     * 施工单位名称
     */
    @Schema(name = "施工单位名称")
    @TableField(value = "SETTLEMENTCONSTTUNITNAME")
    private String settlementconsttunitname;

    /**
     * 备用字段
     */
    @Schema(name = "备用字段")
    @TableField(value = "SETTLEMENTCONSTEXT")
    private String settlementconstext;
    
    @Schema(name = "是否是同步数据 ，1-是 其余否")
    @TableField(value = "ISSYNC")
    private Integer isSync;


    @TableField(exist = false)
    @Schema(name="ids")
    private String ids;
}
