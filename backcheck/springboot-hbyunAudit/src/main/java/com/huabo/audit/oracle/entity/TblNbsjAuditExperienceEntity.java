package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_AUDIT_EXPERIENCE")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjAuditExperienceEntity {
//	private static final long serialVersionUID = 1L;

	@TableId(value = "experid", type= IdType.INPUT)
    @Schema
    private BigDecimal experId;
	
	@TableField(value = "title")
    @Schema
    private String title;
	
	@TableField(value = "kind")
    @Schema
    private String kind;

    @TableField(exist = false)
    @Schema
    private TblNbsjAuditExperienceTypeEntity auditExperienceType;
	
	@TableField(value = "author")
    @Schema
    private String author;
	
	@TableField(value = "createtime")
    @Schema
    private Date createTime;
	
	@TableField(value = "updatetime")
    @Schema
    private Date updateTime;

    @TableField(exist = false)
    @Schema
    private TblStaff createStaff;
	
	@TableField(value = "describe")
    @Schema
    private String describe;
	
	@TableField(value = "suborgname")
    @Schema
    private String subOrgName;
	
	@TableField(value = "summarize")
    @Schema
    private String summarize;
	
	@TableField(value = "docfile")
    @Schema
    private String docFile;
	
	@TableField(value = "modelurl")
    @Schema
    private String modelUrl;
	
	@TableField(value = "submittime")
    @Schema
    private Date submitTime;
	
	
}
