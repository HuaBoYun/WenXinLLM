package com.huabo.compliance.mysql.entity;


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
    private Integer innrulid;
    @TableField("FLOWID")
    private Integer flowid;
}
