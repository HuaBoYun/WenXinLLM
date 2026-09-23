package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@TableName("TBL_NBSJ_RISKTOLERABILITY")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjRisktolerability {
	
	@TableId(value = "TOLEID", type= IdType.INPUT)
    @Schema
    private BigDecimal toleid;
	
	@TableField(value = "RTCODE")
    @Schema
    private String rtcode;
	
	@TableField(value = "RISKID")
    @Schema
    private BigDecimal riskid;
	
	@TableField(value = "LOWERBORDER")
    @Schema
    private Integer lowerborder;
	
	@TableField(value = "UPPERBORDER")
    @Schema
    private Integer upperborder;

	@TableField(value = "COLORSTRING")
    @Schema
    private String colorstring;
	
	@TableField(value = "DESCRIPTION")
    @Schema
    private String description;
	
	@TableField(value = "MEMO")
    @Schema
    private String memo;
	
//	private TblNbsjRisk nbsjRisk;
	
}
