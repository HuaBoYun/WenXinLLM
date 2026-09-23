package com.huabo.audit.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@TableName("TBL_NBSJ_BUGCRITERION")
@Data
@Schema(name="缺陷标准实体类")
@Accessors(chain = true)
public class TblNbsjBugCriterion extends FlexibleFieldEntity {
	@Id
    @TableId(value = "BUGCRIID",type=IdType.INPUT)
    @Schema(name = "缺陷标准ID")
    private BigDecimal bugcriid;
	
	@TableField(value = "BUGCRILEVEL")
	@Schema(name = "缺陷级别")
	private String bugcrilevel;
	
	@TableField(value = "BUGCRIDEFINE")
	@Schema(name = "定义")
	private String bugcridefine;
	
	@TableField(value = "STATUS")
	@Schema(name = "状态  1禁用  2正常")
	private Integer status;
	
	@TableField(value = "BUGCRIRATION")
	@Schema(name = "定量标准")
	private String bugcriration;
	
	@TableField(value = "BUGCRISTABILITY")
	@Schema(name = "定性标准")
	private String bugcristability;
	
	@TableField(value = "VERSION")
	@Schema(name = "版本")
	private Integer version;
	
	@TableField(value = "BUGTYPE")
	@Schema(name = "缺陷类型（财报类、非财报类）")
	private String bugtype;
	
	@TableField(value = "ORGID")
	@Schema(name = "隶属组织")
	private BigDecimal orgid;
	//private Set<TblNbsjBug> tblNbsjBugs;
	
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
	    @Schema(name = "所属部门")
	    @TableField("LINKDEPTID")
	    @Column(name = "LINKDEPTID")
	    private BigDecimal linkdeptid;
	
	    @Schema(name="创建人")
	    @TableField("CREATESTAFFID")
	    private BigDecimal createstaffid;
	    
	    @TableField("CREATETIME")
	 	@Schema(name = "创建时间")
	    @JsonFormat(pattern = "yyyy-MM-dd")
	   	@DateTimeFormat(pattern = "yyyy-MM-dd")
	     private Date createtime;
}
