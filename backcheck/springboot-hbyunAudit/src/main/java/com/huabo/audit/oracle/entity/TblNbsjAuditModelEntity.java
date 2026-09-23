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

@TableName("TBL_NBSJ_AUDITMODEL")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjAuditModelEntity {
	
//	private static final long serialVersionUID = 1L;

	@TableId(value = "modelid", type= IdType.INPUT)
    @Schema
    private BigDecimal modelId;
	
	@TableField(value = "imgurl")
    @Schema
    private String IMGUrl;
	
	@TableField(value = "createtime")
    @Schema
    private Date createTime;

    @TableField(value = "updatetime")
    @Schema
    private Date updateTime;

    @TableField(exist = false)
    @Schema
    private Set<TblNbsjAuditModelResultEntity> tblNbsjAuditModelResults;

    @TableField(exist = false)
    @Schema
    private TblNbsjAuditExperienceEntity auditExperience;
    
    @TableField(value = "modelstep1")
    @Schema
    private String modelstep1;
    
    @TableField(value = "modelstep2")
    @Schema
    private String modelstep2;
    
    @TableField(value = "modelstep3")
    @Schema
    private String modelstep3;
    
    @TableField(value = "modelstep4")
    @Schema
    private String modelstep4;
    
    @TableField(value = "modelstep5")
    @Schema
    private String modelstep5;
}
