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
 * 央企模块-计划编制-竣工结算审计项目汇总
 * @TableName TBL_YQNS_COMPLETION_SET
 */
@TableName(value ="TBL_YQNS_COMPLETION_SET")
@Data
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblYqnsCompletionSet extends BaseReservedProperty {
	
	public static final String NOID = "60072";
	

    /**
     * 竣工结算审计项目汇总主键
     */
    @Schema(name = "竣工结算审计项目汇总主键")
    @TableId(value = "COMPLETIONID", type = IdType.INPUT)
    private BigDecimal completionid;

    /**
     * 序号
     */
    @Schema(name = "序号")
    @TableField(value = "SORTINDEX")
    private BigDecimal sortindex;
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

    /**
     * 填报单位id
     */
    @Schema(name = "填报单位id")
    @TableField(value = "COMPLETIONREPORTUNITID")
    private BigDecimal completionreportunitid;


    /**
     * 填报单位名称
     */
    @Schema(name = "填报单位名称")
    @TableField(value = "COMPLETIONREPORTUNITNAME")
    private String completionreportunitname;


    /**
     * 金额单位
     */
    @Schema(name = "金额单位")
    @TableField(value = "COMPLETIONAMOUNTUNIT")
    private String completionamountunit;
    /**
     * 合同编号
     */
    @Schema(name = "合同编号")
    @TableField(value = "COMPLETIONCODE")
    private String completioncode;
    /**
     * 工程或费用名称
     */
    @Schema(name = "工程或费用名称")
    @TableField(value = "COMPLETIONNAME")
    private String completionname;
    /**
     * 实施单位id
     */
    @Schema(name = "实施单位id")
    @TableField(value = "COMPLETIONIMPLUNITID")
    private BigDecimal completionimplunitid;

    /**
     * 实施单位名称
     */
    @Schema(name = "实施单位名称")
    @TableField(value = "COMPLETIONIMPLUNITNAME")
    private String completionimplunitname;
    /**
     * 批复概算投资
     */
    @Schema(name = "批复概算投资")
    @TableField(value = "COMPLETIONAPPROVAL")
    private String completionapproval;
    /**
     * 合同金额
     */
    @Schema(name = "合同金额")
    @TableField(value = "CONTRACTMONEY")
    private String contractmoney;
    /**
     * 结算金额
     */
    @Schema(name = "结算金额")
    @TableField(value = "COMPLETIONSETMONEY")
    private String completionsetmoney;
    /**
     * 投资节超（概算-实际完成）
     */
    @Schema(name = "投资节超（概算-实际完成）")
    @TableField(value = "COMPLETIONJIECHAO")
    private String completionjiechao;
    /**
     * 投资节超情况说明
     */
    @Schema(name = "投资节超情况说明")
    @TableField(value = "COMPLETIONJIECHAODEAL")
    private String completionjiechaodeal;
    
    @Schema(name = "是否是同步数据 ，1-是 其余否")
    @TableField(value = "ISSYNC")
    private Integer isSync;


    @TableField(exist = false)
    @Schema(name="ids")
    private String ids;
}
