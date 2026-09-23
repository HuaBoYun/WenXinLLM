package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_RISKTOLERABILITY")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjRisktolerability {
	
	@TableId(value = "TOLEID", type= IdType.AUTO)
    @Schema
    private Integer toleid;
	
	@TableField(value = "RTCODE")
    @Schema(name = "风险容忍度信息-序号")
    private String rtcode;
	
	@TableField(value = "RISKID")
    @Schema
    private Integer riskid;
	
	@TableField(value = "LOWERBORDER")
    @Schema(name = "风险容忍度信息-基准线下边界")
    private Integer lowerborder;
	
	@TableField(value = "UPPERBORDER")
    @Schema(name = "风险容忍度信息-基准线上边界")
    private Integer upperborder;

	@TableField(value = "COLORSTRING")
    @Schema(name = "风险容忍度信息-颜色块编码")
    private String colorstring;
	
	@TableField(value = "DESCRIPTION")
    @Schema(name = "风险容忍度信息-描述")
    private String description;
	
	@TableField(value = "MEMO")
    @Schema
    private String memo;
	
//	private TblNbsjRisk nbsjRisk;
	
}
