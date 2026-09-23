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

@TableName("TBL_NBSJ_AUDITEXPEROUTRUL")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjAuditExperOutRulEntity {
//	private static final long serialVersionUID = 1L;
	
	@TableId(value = "outid", type= IdType.INPUT)
    @Schema
    private BigDecimal outId;

    @TableField(exist = false)
    @Schema
    private TblNbsjAuditExperienceEntity auditExperience;

    @TableField(exist = false)
    @Schema
    private TblNbsjOuterruleEntity outerrule;
}
