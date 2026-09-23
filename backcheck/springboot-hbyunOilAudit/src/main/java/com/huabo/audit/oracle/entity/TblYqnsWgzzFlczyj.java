package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.huabo.audit.oracle.entity.base.ReservedEntity;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YQNS_WGZZ_FLCZYJ")
@Data
@Schema(name="分类处置意见")
@Accessors(chain = true)
public class TblYqnsWgzzFlczyj  extends ReservedEntity {

    @TableField(value = "ID")
    @Schema(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    private BigDecimal id;

	@TableField(value = "CLUEID")
	@Schema(name = "移交单位")
	private BigDecimal clueid;
	
	//移交单位名称
	@Transient
    private String cluename;

//    @TableField(value = "CLUENABER")
//    @Schema(name = "线索编号")
//    private String cluenaber;
	
	@TableField("IMPCREATEUSERNAME")
    @Schema(name = "移交时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date impcreateusername;

    @TableField(value = "VERIFYCONTENT")
    @Schema(name = "事项")
    private String verifycontent;

    @TableField(value = "CLUEHSFW")
    @Schema(name = "事项概要")
    private String cluehsfw;

    @TableField(value = "CLUEGZZZ")
    @Schema(name = "初步核实结果")
    private String cluegzzz;

    @TableField(value = "CLUEZRDX")
    @Schema(name = "分类处置意见")
    private String cluezrdx;

    @TableField(value = "CLUESSRD")
    @Schema(name = "领导机构负责人审批")
    private String cluessrd;

//    @TableField(value = "CLUECLJY")
//    @Schema(name = "责任追究处理建议")
//    private String cluecljy;
//
//    @TableField(value = "VERIFYCONKZGCQK")
//    @Schema(name = "初步核实开展过程情况")
//    private String verifyconkzgcqk;
//
//    @TableField(value = "VERIFYCONHSJG")
//    @Schema(name = "初步核实结果")
//    private String verifyconhsjg;
//
//    @TableField(value = "CLUEGZJY")
//    @Schema(name = "工作建议")
//    private String cluegzjy;

    @TableField("STATUS")
    @Schema(name = "状态")
    private Integer status;

    @TableField("CREATOR")
    @Schema(name = "创建人")
    private BigDecimal creator;

    @Transient
    private String attIds;
    
    @TableField(value = "FILUE")
    @Schema(name = "附件")
    private String filue;

	@TableField("CONTENT")
	@Schema(name = "富文本")
	private String content;
}
