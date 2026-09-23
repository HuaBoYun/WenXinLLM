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
@TableName("TBL_RISK_PROCESS_DIC")
@Schema(name="TBL_RISK_PROCESS_DIC", description="流程字典表")
public class TblRiskProcessDic implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.INPUT)
    @TableField("ID")
	@Schema(name="主键ID")
    private Integer id;

    @TableField("PROCESSNAME")
    @Schema(name="流程名称")
    private String processname;
    
    @TableField("PROCESSNO")
    @Schema(name="流程编号")
    private String processno;
    
    @TableField("SERNO")
    @Schema(name="序号")
    private Integer serno;
    
}