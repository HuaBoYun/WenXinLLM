package com.huabo.audit.oracle.entity;

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

	@TableId(value = "projectid", type= IdType.AUTO)
    @Schema
    private Integer solutionid;

    @TableField(value = "tblstaff")
    @Schema
    private String solutioncode;
    
    @TableField(value = "tblstaff")
    @Schema
    private String solutionname;
    
    @TableField(value = "tblstaff")
    @Schema
    private String solutionstatus;
    
	@TableField(value = "tblstaff")
	@Schema
	private TblStaff tblStaff;
	
	@TableField(value = "tblstaff")
	@Schema
	private Date createdate;
	
	@TableField(value = "tblstaff")
	@Schema
	private Date enddate;
	
	@TableField(value = "tblstaff")
    @Schema
    private String memo;
	
	@TableField(value = "tblstaff")
    @Schema
    private TblOrganization organization;
	
	@TableField(value = "tblstaff")
    @Schema
    private TblNbsjProject relatedProject;
	
	@TableField(value = "tblstaff")
    @Schema
    private Integer runstatus;
	
	@TableField(value = "tblstaff")
    @Schema
    private TblOrganization reformCompany;
	
	@TableField(value = "tblstaff")
    @Schema
    private TblStaff reformUser;
	
	@TableField(value = "tblstaff")
    @Schema
    private Integer zgstatus;
	
	@TableField(value = "tblstaff")
    @Schema
    private Set<TblNbsjRefopmEntity> tblReforms;
	
	@TableField(value = "tblstaff")
    @Schema
    private Set<TblAttachment> tblAttachments;
	
}
