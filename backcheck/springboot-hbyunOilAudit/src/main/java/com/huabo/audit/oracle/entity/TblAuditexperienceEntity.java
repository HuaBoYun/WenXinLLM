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

@TableName("TBL_NBSJ_AUDITEXPERIENCE")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblAuditexperienceEntity {
	
	@TableId(value = "auditexperid", type= IdType.AUTO)
	@Schema
	private BigDecimal auditexperid;
	
	@TableField(value = "auditexpertotle")
	@Schema
	private String auditexpertotle;
	
	@TableField(value = "auditexperbody")
	@Schema
	private String auditexperbody;
	
	@TableField(value = "auditexperstatus")
	@Schema
	private String auditexperstatus;
	
	@TableField(value = "auditexperauther")
	@Schema
	private String auditexperauther;
	
	@TableField(value = "publishtime")
	@Schema
	private Date publishtime;
	
	@TableField(value = "memo")
	@Schema
	private String memo;
	
	@TableField(value = "tblorganization")
	@Schema
	private TblOrganization tblOrganization;
	
	@TableField(value = "tblattachments")
	@Schema
	private Set<TblAttachment> tblAttachments;
}
