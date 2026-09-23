package com.huabo.audit.oracle.entity;

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
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.oracle.entity
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/27
 * @Time:17:58
 */
@TableName("TBL_WGZZ_WGJY_YS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(name="违规经营移送实体")
@Accessors(chain = true)
public class TblWgzzWgjyYs {

   @TableField(value = "ID")
   @Id
   @KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
   @Schema(name = "主键id")
   private BigDecimal id;

    @TableField("WGHCID")
    @Schema(name = "違規核查ID")
    private BigDecimal wghcid;

    @TableField("CLUENABER")
    @Schema(name = "线索编号")
    private String cluenaber;

    @TableField(value = "CLUERESOURCE")
    @Schema(name = "线索来源")
    private String clueresource;

    @TableField(value = "DEPARTMENT")
    @Schema(name = "部门id")
    private String department;
    
    @TableField(value = "DEPARTMENTNAME")
    @Schema(name = "部门名称")
    private String departmentname;

    @TableField(value = "PERSONNEL")
    @Schema(name = "人员名称")
    private String personnel;
    
    @TableField(value = "PERSONNELID")
    @Schema(name = "人员id")
    private String personnelid;

    @TableField(value = "HSCONTENT")
    @Schema(name = "核实情况")
    private String hscontent;

    @TableField(value = "ZCSSQK")
    @Schema(name = "资产损失和损失风险初步核实情况")
    private String zcssqk;

    @TableField(value = "WJWTXS")
    @Schema(name = "涉嫌违纪问题线索说明")
    private String wjwtxs;

    @TableField(value = "JBRNAME")
    @Schema(name = "经办人名称")
    private String jbrname;

    @TableField(value = "JBRID")
    @Schema(name = "经办人ID")
    private BigDecimal jbrid;

    @TableField(value = "BGSZR")
    @Schema(name = "违规经营投资责任追究工作领导小组办公室主任")
    private String bgszr;

    @TableField(value = "LDXZZZ")
    @Schema(name = "违规经营投资责任追究工作领导小组组长")
    private String ldxzzz;

	@TableField(value = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileids;
	
	@TableField("CONTENT")
	@Schema(name = "富文本")
	private String content;
	 
	
	@TableField("CREATOR")
    @Schema(name = "创建人")
    private BigDecimal creator;
	
	
	@TableField("IMPCREATEUSERNAME")
    @Schema(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date impcreateusername;

	
	@TableField(value = "VERIFYCONTENT")
    @Schema(name = "核实内容")
    private String verifycontent;
	
	
	@TableField("YSSTUTS")
    @Schema
    private Integer ysstuts;
	
}
