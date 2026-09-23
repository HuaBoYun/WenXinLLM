package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
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
@Table(name = "TBL_YQNS_RESULT")
@Schema(name="审计结果确认对象")
public class TblYqnsResult extends BaseReservedProperty {
	

    @Schema(name = "主键")
    @TableId("RESULTID")
    @Id
    @KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
    private BigDecimal resultid;

    @TableField(value = "RESULTCODE")
    @Column(name = "RESULTCODE")
    @Schema(name = "编号")
    private String resultcode;



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
    private BigDecimal projectid;
    
    @Schema(name = "TblYqnsProjectAuditTemplate 主键")
    @TableField("TEMPLATEID")
    private Long templateid;


    @TableField(value = "STATUS")
    @Column(name = "STATUS")
    @Schema(name = "审核状态 1 未审核;2 审核中;3 审核驳回;6 审核完成;5 需调整")
    private Integer status;


	@Column(name = "PROJECTNAME")
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
	
	
	@TableField(value = "CONTRACTCODE")
	@Column(name = "CONTRACTCODE")
	@Schema(name = "合同编号")
	private String contractcode; 
	
	@TableField(value = "CONTRACTNAME")
	@Column(name = "CONTRACTNAME")
	@Schema(name = "合同名称")
	private String contractname; 
	
	@TableField(value = "CONTRACTMONEY")
	@Column(name = "CONTRACTMONEY")
	@Schema(name = "合同金额")
	private String contractmoney; 

	@TableField(value = "SGORGID")
	@Column(name = "SGORGID")
	@Schema(name = "施工单位id")
	private String sgorgid; 
	
	@TableField(value = "SGORGNAME")
	@Column(name = "SGORGNAME")
	@Schema(name = "施工单位名称")
	private String sgorgname; 
	
	
	@TableField(value = "HZMONEY")
	@Column(name = "HZMONEY")
	@Schema(name = "核增金额")
	private String hzmoney; 
	
	@TableField(value = "HJMONEY")
	@Column(name = "HJMONEY")
	@Schema(name = "核减金额")
	private String hjmoney; 
	
	
	@TableField(value = "SDMONEY")
	@Column(name = "SDMONEY")
	@Schema(name = "审计认定金额")
	private String sdmoney;


	@TableField(value = "OVERVIEW")
	@Column(name = "OVERVIEW")
	@Schema(name = "问题简述")
	private String overview;


	@TableField(value = "AUDITOPINION")
	@Column(name = "AUDITOPINION")
	@Schema(name = "审计意见")
	private String auditopinion;
	
	@TableField(value = "HJSTAFFID")
    @Column(name = "HJSTAFFID")
    @Schema(name = "核减人id")
    private BigDecimal hjstaffid;
	
	@TableField(value = "HJSTAFFNAME")
    @Column(name = "HJSTAFFNAME")
    @Schema(name = "核减人")
    private String hjstaffname;

	@TableField(value = "RESULTIDS")
	@Column(name = "RESULTIDS")
	@Schema(name = "子项表id")
	private BigDecimal resultids;

	@TableField(exist = false)
	@Schema(name = "子项表")
	private List<TblYqnsResult> zixbs;

	@TableField(value = "SERVED")
	@Column(name = "SERVED")
	@Schema(name = "预留字段")
	private String served ;
	
	@Schema(name = "子项表ids")
	private String zixbids ;
	
	
	@TableField(exist = false)
	@Schema(name = "审计人员")
	private String rwnames;
	
	
	@TableField(exist = false)
	@Schema(name = "核减率")
	private BigDecimal hjl;
	
	
	
	 /**
     * 工程量计算
     */
    @Schema(name = "工程量计算")
    @TableField(exist = false)
    private String gcljs;

    /**
     * 定额套用
     */
    @Schema(name = "定额套用")
    @TableField(exist = false)
    private String dety;

    /**
     * 物资金额（元）
     */
    @Schema(name = "物资金额（元）")
    @TableField(exist = false)
    private Float wzjg;

    /**
     * 其它审减
     */
    @Schema(name = "其它审减")
    @TableField(exist = false)
    private String qtsj;
    
    
    /**
     * 现场实测
     */
    @Schema(name = "现场实测")
    @TableField(exist = false)
    private String xcsc;
	
}
