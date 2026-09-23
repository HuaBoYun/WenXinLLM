package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hbfk.entity.TblAttachment;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_AUDITPLAN")
@Data
@Schema(name="计划编号实体类")
@Accessors(chain = true)
public class TblNbsjReformSolutionEntity {

	@TableId(value = "projectid", type= IdType.INPUT)
    @Schema
    private BigDecimal solutionid;

    @TableField(value = "solutioncode")
    @Schema
    private String solutioncode;
    
    @TableField(value = "solutionname")
    @Schema
    private String solutionname;
    
    @TableField(value = "solutionstatus")
    @Schema
    private String solutionstatus;
    
	@TableField(exist = false)
	@Schema
	private TblStaff tblStaff;
	
	@TableField(value = "createdate")
	@Schema
	private Date createdate;
	
	@TableField(value = "enddate")
	@Schema
	private Date enddate;
	
	@TableField(value = "memo")
    @Schema
    private String memo;
	
	@TableField(exist = false)
    @Schema
    private TblOrganization organization;
	
	@TableField(exist = false)
    @Schema
    private TblNbsjProject relatedProject;
	
	@TableField(value = "runstatus")
    @Schema
    private Integer runstatus;
	
	@TableField(exist = false)
    @Schema
    private TblOrganization reformCompany;
	
	@TableField(exist = false)
    @Schema
    private TblStaff reformUser;
	
	@TableField(value = "zgstatus")
    @Schema
    private Integer zgstatus;
	
	@TableField(exist = false)
    @Schema
    private Set<TblNbsjRefopmEntity> tblReforms;
	
	@TableField(exist = false)
    @Schema
    private Set<TblAttachment> tblAttachments;
	
}
