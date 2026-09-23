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


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "TBL_PROJECT_DATAPRE")
@Schema(name="项目资料")
public class TblNbsjProjectDataEntity {
	private static final long serialVersionUID = 1L;
	
	@TableId(value = "ID")
	@Schema(name = "主键")
	@Id
     //@KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
	private BigDecimal id;
	
	@TableField(value = "PROJECT_DATAPRE_ID")
	@Schema(name = "资料编号")
	private String projectDatapreId;
	
	
	@TableField(value = "PROJECTID")
	@Schema(name = "关联项目id")
	private BigDecimal projectid;
	
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private BigDecimal oldProjectId;
	
	@TableField(value = "DATA_NAME")
	@Schema(name = "资料名称")
	private String dataName;
	
	@TableField(value = "DATA_CAPACITY")
	@Schema
	private String dataCapacity;
	
	@TableField(value = "DATA_DATE")
	@Schema(name="创建时间",hidden=true)
	private Date dataDate;
	
	@TableField(value = "PROJECTNAME")
	@Schema(name = "项目名称")
	private String projectname;
	
	@TableField(value = "CREATESTAFF")
	@Schema
	private BigDecimal createstaff;
	
	@TableField(value = "USERNAME")
	@Schema(name="用户名",hidden=true)
	private String username;
	
	@TableField(value = "ORGID")
	@Schema(hidden=true)
	private String orgid;
	
	@TableField(value = "FRISTUSERID")
	@Schema(name="存储下发人员id",hidden=true)
	private String fristuserid;
	
	@Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    @Column(name = "SECRECTLEVELID")
    private BigDecimal secrectLevelId;
    
    @Schema(name = "知悉范围 多个逗号分隔")
    @TableField("STAFFSCOPEIDS")
    @Column(name = "STAFFSCOPEIDS")
    private String staffScopeIds;
    
    @Schema(name = "知悉访问人员姓名 多个逗号分隔")
    @TableField("STAFFSCOPENAMES")
    @Column(name = "STAFFSCOPENAMES")
    private String staffScopeNames;
	
}
