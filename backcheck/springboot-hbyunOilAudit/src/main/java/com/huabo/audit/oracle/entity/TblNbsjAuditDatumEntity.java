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

@TableName("TBL_NBSJ_AUDITDATUM")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjAuditDatumEntity {

//	private static final long serialVersionUID = 1L;
	
	@TableId(value = "datumId", type= IdType.AUTO)
    @Schema
    private BigDecimal datumId;
	
	@TableField(value = "title")
    @Schema
    private String title;
	
	@TableField(value = "content")
    @Schema
    private String content;
	
	@TableField(value = "auditexperience")
    @Schema
    private TblNbsjAuditExperienceEntity auditExperience;
	
	@TableField(value = "createtime")
    @Schema
    private Date createTime;

    @TableField(value = "updatetime")
    @Schema
    private Date updateTime;
	
	@TableField(value = "createstaff")
    @Schema
    private TblStaff createStaff;
	
	@TableField(value = "attachments")
    @Schema
    private Set<TblAttachment> attachments;
}
