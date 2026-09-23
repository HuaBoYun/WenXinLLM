package com.huabo.fxgl.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("TBL_RISK_BUSINESS_DIC")
@Schema(name="TBL_RISK_BUSINESS_DIC", description="业务字典表")
public class TblRiskBusinessDic implements Serializable {

	/**
     * ID
     */
    @TableId(type = IdType.INPUT)
    @TableField("ID")
	@Schema(name="主键ID")
    private Integer id;

    @TableField("BUSINESSNAME")
    @Schema(name="业务名称")
    private String businessname;
    
    @TableField("BUSINESSNO")
    @Schema(name="业务编号")
    private String businessno;
    
    @TableField("SERNO")
    @Schema(name="序号")
    private Integer serno;
    
    @TableField("PROCESSNAME")
    @Schema(name="流程名称")
    private String processname;

}