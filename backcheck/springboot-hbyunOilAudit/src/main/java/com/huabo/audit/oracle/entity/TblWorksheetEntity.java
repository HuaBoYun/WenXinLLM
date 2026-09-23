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

@TableName("TBL_REFORM")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblWorksheetEntity {

	public final static Integer  NO_ZG=0;
	public final static Integer  YES_ZG=1;

	@TableId(value = "worksheetid", type= IdType.AUTO)
    @Schema
    private BigDecimal worksheetid;
	
    @TableField(value = "tblProblem")
    @Schema
    private TblProblemEntity tblProblem;
    
	@TableField(value = "worksheetnumber")
    @Schema
    private String worksheetnumber;
	
	@TableField(value = "auditedorg")
    @Schema
    private String auditedorg;
	
	@TableField(value = "audittarget")
    @Schema
    private String audittarget;
	
	@TableField(value = "auditdescription")
    @Schema
    private String auditdescription;
	
	@TableField(value = "auditprocess")
    @Schema
    private String auditprocess;
	
	@TableField(value = "auditjudge")
    @Schema
    private String auditjudge;
	
	@TableField(value = "checkopinin")
    @Schema
    private String checkopinin;
	
	@TableField(value = "recorder")
    @Schema
    private String recorder;
	
	@TableField(value = "recordingdate")
    @Schema
    private Date recordingdate;
	
	@TableField(value = "memo")
    @Schema
    private String memo;
	
	@TableField(value = "worksheetname")
    @Schema
    private String worksheetname;
	
	@TableField(value = "worksheetbysystem")
    @Schema
    private String worksheetbysystem;
	
	@TableField(value = "userid")
    @Schema
    private Integer userid;
	
	@TableField(value = "orgid")
    @Schema
    private Integer orgid;
	
	@TableField(value = "rectification")
    @Schema
    private Integer rectification;
	
	@TableField(value = "tblAttachments")
    @Schema
    private Set tblAttachments;
	
	@TableField(value = "tblReforms")
    @Schema
    private Set<TblReformEntity> tblReforms;
	
	@TableField(value = "tblproblemTargets")
    @Schema
    private Set<TblAssessTarget> tblproblemTargets;
}
