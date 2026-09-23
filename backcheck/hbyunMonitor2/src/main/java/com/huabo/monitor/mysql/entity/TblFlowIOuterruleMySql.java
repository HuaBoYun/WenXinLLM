package com.huabo.monitor.mysql.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FLOW_OUTERRULE")
@Schema(name="TblFlowIOuterruleMySql")
public class TblFlowIOuterruleMySql {

    @TableField("OUTRULID")
    private Integer outrulid;
    @TableField("FLOWID")
    private Integer flowid;
}
