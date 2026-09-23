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

@TableName("TBL_NBSJ_EXPERIENCE")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblExperienceTypeEntity {
	@TableId(value = "targetid", type= IdType.INPUT)
    @Schema
    private BigDecimal targetId;

    @TableField(value = "targetname")
    @Schema
    private String targetName;
    
    @TableField(value = "targetdesc")
    @Schema
    private String targetDesc;
    
    @TableField(value = "parentid")
    @Schema
    private BigDecimal parentId;
    
    @TableField(value = "createtime")
    @Schema
    private Date createTime;
    
    @TableField(value = "updatetime")
    @Schema
    private Date updateTime;
    
    @TableField(exist = false)
    @Schema
    private TblNbsjExperienceEntity nbsjExperience;
    
    @TableField(value = "status")
    @Schema
    private Integer status;
    
    @TableField(value = "finshcount")
    @Schema
    private Integer finshCount;
    
    @TableField(value = "sumcount")
    @Schema
    private Integer sumCount;
    
    @TableField(value = "sheetcount")
    @Schema
    private Integer sheetCount;
    
    @TableField(value = "unfinshcount")
    @Schema
    private Integer unFinshCount;
    
}
