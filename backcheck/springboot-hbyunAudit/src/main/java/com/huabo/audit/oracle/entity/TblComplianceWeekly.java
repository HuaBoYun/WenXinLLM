package com.huabo.audit.oracle.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 合规周刊
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "TBL_COMPLIANCE_WEEKLY")
@Schema(name="TblComplianceWeekly对象")
public class TblComplianceWeekly extends FlexibleFieldEntity implements Serializable {

	@Id
	@Column(name = "ID")
    @TableId(type = IdType.INPUT)
	@Schema(name = "id")
	private BigDecimal id;

	@Column(name = "WEEKLYTITLE")
	@Schema(name = "周刊标题")
	private String weeklytitle;

	@Column(name = "MEMO")
	@Schema(name = "周刊简介")
	private String memo;

	@Column(name = "EFFDATE")
	@Schema(name = "生效日期")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date effdate;
	
	@Column(name = "EFFSTATUS")
	@Schema(name = "生效状态")
	private Integer effstatus;
	
	@Column(name = "LYTYPE")
	@Schema(name = "类型（1合规周刊，2合规案例）")
	private Integer lytype;
	
	@Column(name = "FILEIDS")
	@TableField("FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	@Column(name = "STATUS")
	@Schema(name="状态",hidden=true)
	private Integer status;

	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private BigDecimal creator;
	
	@Transient
	@TableField(exist=false)
	@Schema(name="创建人名称",hidden=true)
	private String creatorName;

	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	@TableField("WORKUNIT")
	private BigDecimal workUnit;

	@Column(name = "BELONGGROUP")
	@TableField("BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private BigDecimal belongGroup;

	@Column(name = "CREATEDTIME")
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@TableField("CREATEDTIME")
	private Date createdTime;

	@Column(name = "STAFFSCOPEIDS")
	@Schema(name = "知悉范围人员主键 ,拼接")
	@TableField("STAFFSCOPEIDS")
	private String staffScopeIds;
		
	@TableField("STAFFSCOPENAMES")
	@Column(name = "STAFFSCOPENAMES")
	@Schema(name = "知悉范围人员姓名,拼接")
	private String staffScopeNames	;
	
	@Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    @Column(name = "SECRECTLEVELID")
    private BigDecimal secrectLevelId;
	
	
	private static final long serialVersionUID = 1L;
}
