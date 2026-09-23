package com.huabo.system.entity;


import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FLOW_INNERRULE")
@Schema(name="TblFlowInnerRule")
public class TblFlowInnerRule {

    @TableField("INNRULID")
    @Schema(name="内规主键")
    private BigDecimal innrulid;
    
    @TableField("FLOWID")
    @Schema(name="流程主键")
    private BigDecimal flowid;
}
