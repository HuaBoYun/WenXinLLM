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
	
	@TableId(value = "reformid", type= IdType.INPUT)
    @Schema
    private BigDecimal reformid;

    @TableField(exist = false)
    @Schema
    private TblProblemEntity tblProblem;

    @TableField(exist = false)
    @Schema
    private TblNbsjBugEntity tblBug;
    
    @TableField(value = "reformmeasure")
    @Schema
    private String reformmeasure;
    
    @TableField(value = "reformresult")
    @Schema
    private String reformresult;
    
    @TableField(value = "reformcarryout")
    @Schema
    private String reformcarryout;
    
    @TableField(value = "handling")
    @Schema
    private String handling;
    
	@TableField(value = "reformdeadline")
    @Schema
    private Date reformdeadline;
	
	@TableField(value = "memo")
    @Schema
    private String memo;
	
	@TableField(value = "peronincharge")
    @Schema
    private String peronincharge;
	
	@TableField(value = "reformtime")
    @Schema
    private Date reformtime;

    @TableField(exist = false)
    @Schema
    private TblStaff tblCeaters;

    @TableField(exist = false)
    @Schema
    private TblWorksheetEntity tblWorksheet;
	
	@TableField(value = "reformstatus")
    @Schema
    private Integer reformstatus;
	
	@TableField(value = "templetecode")
    @Schema
    private Date createDate;
	
	@TableField(value = "reformuserid")
    @Schema
    private BigDecimal reformuserid;
	
	@TableField(value = "lastreformstatus")
    @Schema
    private Integer lastreformstatus;

    @TableField(exist = false)
    @Schema
    private TblNbsjReformSolutionEntity tblReformSolution;

    @TableField(exist = false)
    @Schema
    private Set<TblAttachment> tblReformAtts;
	
}
