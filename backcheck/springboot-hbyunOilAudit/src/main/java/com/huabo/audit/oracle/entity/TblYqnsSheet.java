package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

/**
 * 描述:
 * author: tj
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "TBL_YQNS_SHEET")
@Schema(name="TblYqnsSheet对象")
public class TblYqnsSheet {
	

    @Schema(name = "主键")
    @TableId("SHEETID")
    @Id
    @KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
    private BigDecimal sheetid;

    @TableField(value = "SHEETCODE")
    @Column(name = "SHEETCODE")
    @Schema(name = "索引号")
    private String sheetcode;

    @TableField(value = "AUDITMATTERS")
    @Column(name = "AUDITMATTERS")
    @Schema(name = "审计事实")
    private String auditmatters;


    @TableField(value = "CREATESTAFF")
    @Column(name = "CREATESTAFF")
    @Schema(name="创建人id",hidden=true)
    private BigDecimal createstaff;

    @TableField(value = "CREATETIME")
    @Column(name = "CREATETIME")
    @Schema(name="创建时间",hidden=true)
    @IgnoreSwaggerParameter
    private Date createtime;

    @TableField(value = "PROJECTID")
    @Column(name = "PROJECTID")
    @Schema(name = "所属项目id")
    private Integer projectid;

    @TableField(value = "UPDATETIME")
    @Column(name = "UPDATETIME")
    @Schema(name="修改时间",hidden=true)
    @IgnoreSwaggerParameter
    private Date updatetime;

   
    @TableField(value = "STATE")
    @Column(name = "STATE")
    @Schema(name = "标识：是否是问题底稿")
    @IgnoreSwaggerParameter
    private Integer state;

    @TableField(value = "STATUS")
    @Column(name = "STATUS")
    @Schema(name = "审核状态 1 未审核;2 审核中;3 审核驳回;4 审核完成;5 需调整")
    private Integer status;


    @TableField(exist = false)
    @Schema(name = "项目名称")
    private String projectname;

    
    @TableField(exist = false)
    @Schema(name="拟稿人",hidden=true)
    private String realname;
  
    
    @TableField(value = "ORGIDS")
    @Column(name = "ORGIDS")
	@Schema(name = "被审计对象id")
	private String orgids;

	@TableField(value = "ORGIDNAMES")
	@Column(name = "ORGIDNAMES")
	@Schema(name = "被审计对象名称")
	private String orgidnames;
	 
	 
	 @TableField(value = "AUDITCONCLUSION")
	 @Column(name = "AUDITCONCLUSION")
	 @Schema(name = "审计结论及依据")
	 private String auditconclusion;
	 
	 @TableField(value = "AUDITHANDLING")
	 @Column(name = "AUDITHANDLING")
	 @Schema(name = "审计处理意见及建议")
	 private String audithandling;
	 
	 
	 @TableField(value = "AUDITMATTERSSP")
	 @Column(name = "AUDITMATTERSSP")
	 @Schema(name = "审批后修改的审计事实")
	 private String auditmatterssp;
	 
	 @TableField(value = "AUDITCONCLUSIONSP")
	 @Column(name = "AUDITCONCLUSIONSP")
	 @Schema(name = "审批后修改的审计结论及依据")
	 private String auditconclusionsp;
	 
	 @TableField(value = "AUDITHANDLINGSP")
	 @Column(name = "AUDITHANDLINGSP")
	 @Schema(name = "审批后修改的审计处理意见及建议")
	 private String audithandlingsp;
	 
	 
	 @TableField(value = "AUDITMATTERSJH")
	 @Column(name = "AUDITMATTERSJH")
	 @Schema(name = "被审计单位交换意见后修改的审计事实")
	 private String auditmattersjh;
	 
	 @TableField(value = "AUDITCONCLUSIONJH")
	 @Column(name = "AUDITCONCLUSIONJH")
	 @Schema(name = "被审计单位交换意见后修改的审计结论及依据")
	 private String auditconclusionjh;
	 
	 @TableField(value = "AUDITHANDLINGJH")
	 @Column(name = "AUDITHANDLINGJH")
	 @Schema(name = "被审计单位交换意见后修改的审计处理意见及建议")
	 private String audithandlingjh;
	 
	 
	 
	 
	 @TableField(value = "AUDITEVIDENCE")
	 @Column(name = "AUDITEVIDENCE")
	 @Schema(name = "审计证据是否准确、真实、合法")
	 private String auditevidence;
	 
	 
	 @TableField(value = "AUDITVERIFICATION")
	 @Column(name = "AUDITVERIFICATION")
	 @Schema(name = "审计查证事实描述是否详尽、充分")
	 private String auditiverification;
	 
	 
	 @TableField(value = "AUDITCONCU")
	 @Column(name = "AUDITCONCU")
	 @Schema(name = "审计结论是否客观、公正")
	 private String auditconcu;
	 
	 @TableField(value = "AUDITCLYJ")
	 @Column(name = "AUDITCLYJ")
	 @Schema(name = "审计处理意见及建议是否正确、具有可操作性和建设性")
	 private String auditclyj;
	 
	 @TableField(value = "AUDITJCGZ")
	 @Column(name = "AUDITJCGZ")
	 @Schema(name = "基础工作")
	 private String auditjcgz;
}
