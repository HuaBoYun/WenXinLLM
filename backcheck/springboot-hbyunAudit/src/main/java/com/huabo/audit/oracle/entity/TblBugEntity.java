package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_BUG")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblBugEntity {
	
	@TableId(value = "bugid", type= IdType.INPUT)
    @Schema
    private BigDecimal bugid;
	
	@TableField(value = "bugnumber")
    @Schema
    private String bugnumber;
	
	@TableField(value = "bugdescripte")
    @Schema
    private String bugdescripte;
	
	@TableField(value = "discovertime")
    @Schema
    private Date discovertime;
	
	@TableField(value = "discoverperson")
    @Schema
    private String discoverperson;
	
	@TableField(value = "bugproperty")
    @Schema
    private String bugproperty;
	
	@TableField(value = "bugsource")
    @Schema
    private String bugsource;
	
	@TableField(value = "bugdapartment")
    @Schema
    private String bugdapartment;
	
	@TableField(value = "needreform")
    @Schema
    private String needreform;
	
	@TableField(value = "resonfornoreform")
    @Schema
    private String resonfornoreform;
	
	@TableField(value = "bugreformstatus")
    @Schema
    private String bugreformstatus;
	
	@TableField(value = "projectname")
    @Schema
    private String projectname;
	
	@TableField(value = "memo")
    @Schema
    private String memo;
	
	@TableField(value = "fatherbugid")
    @Schema
    private BigDecimal fatherbugid;
	
	@TableField(value = "bugbysystem")
    @Schema
    private String bugbysystem;
	
	@TableField(exist = false)
    @Schema
    private Set tblInnerrules;

    @TableField(exist = false)
    @Schema
    private Set tblAttachments;

    @TableField(exist = false)
    @Schema
    private Set tblOuterrules;

    @TableField(exist = false)
    @Schema
    private Set tblTracingresponsibilities;

    @TableField(exist = false)
    @Schema
    private Set tblReforms;

    @TableField(exist = false)
    @Schema
    private Set<TblBugEntity> children;

    @TableField(exist = false)
    @Schema
    private Set<TblAssessTarget> tblproblemTargets;
	
	@TableField(value = "businessDescription")
    @Schema
    private String businessDescription;
	
}
