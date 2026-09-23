package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_BUG_CRITERION")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblBugCriterionEntity {
	
	@TableId(value = "bugcriid", type= IdType.AUTO)
    @Schema
    private BigDecimal bugcriid;
	
	@TableField(value = "bugcrilecel")
    @Schema
    private String bugcrilecel;
	
	@TableField(value = "bugcridefine")
    @Schema
    private String bugcridefine;
	
	@TableField(value = "bugcrigation")
    @Schema
    private String bugcrigation;
	
	@TableField(value = "bugcristability")
    @Schema
    private String bugcristability;
	
	@TableField(value = "status")
    @Schema
    private Integer status;
	
	@TableField(value = "version")
    @Schema
    private Integer version;
	
	@TableField(value = "orgid")
    @Schema
    private Integer orgid;
	
	@TableField(value = "tblBugs")
    @Schema
    private Set<TblBugEntity> tblBugs;
	
}
