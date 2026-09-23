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
public class TblReformEntity {
	
	public static final Integer WFP=0;
	public static final Integer ZGZ=1;
	public static final Integer YWC=2;
	
	@TableId(value = "reformid", type= IdType.AUTO)
    @Schema
    private BigDecimal reformid;
	
    @TableField(value = "templetecode")
    @Schema
    private TblProblemEntity tblProblem;
    
    @TableField(value = "templetecode")
    @Schema
    private TblNbsjBugEntity tblBug;
    
    @TableField(value = "templetecode")
    @Schema
    private String reformmeasure;
    
    @TableField(value = "templetecode")
    @Schema
    private String reformresult;
    
    @TableField(value = "templetecode")
    @Schema
    private String reformcarryout;
    
    @TableField(value = "templetecode")
    @Schema
    private String handling;
    
	@TableField(value = "templetecode")
    @Schema
    private Date reformdeadline;
	
	@TableField(value = "templetecode")
    @Schema
    private String memo;
	
	@TableField(value = "templetecode")
    @Schema
    private String peronincharge;
	
	@TableField(value = "templetecode")
    @Schema
    private Date reformtime;
	
	@TableField(value = "templetecode")
    @Schema
    private TblStaff tblCeaters;
	
	@TableField(value = "templetecode")
    @Schema
    private TblWorksheetEntity tblWorksheet;
	
	@TableField(value = "templetecode")
    @Schema
    private Integer reformstatus;
	
	@TableField(value = "templetecode")
    @Schema
    private Date createDate;
	
	@TableField(value = "templetecode")
    @Schema
    private Integer reformuserid;
	
	@TableField(value = "templetecode")
    @Schema
    private Integer lastreformstatus;
	
	@TableField(value = "templetecode")
    @Schema
    private TblNbsjReformSolutionEntity tblReformSolution;
	
	@TableField(value = "templetecode")
    @Schema
    private Set<TblAttachment> tblReformAtts;
	
}
