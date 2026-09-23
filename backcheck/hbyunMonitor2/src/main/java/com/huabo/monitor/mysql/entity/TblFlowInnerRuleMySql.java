package com.huabo.monitor.mysql.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FLOW_INNERRULE")
@Schema(name="TblFlowInnerRuleMySql")
public class TblFlowInnerRuleMySql {

    @TableField("INNRULID")
    private Integer innrulid;
    @TableField("FLOWID")
    private Integer flowid;
}
