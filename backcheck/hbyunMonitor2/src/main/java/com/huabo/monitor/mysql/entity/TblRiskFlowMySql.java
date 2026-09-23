package com.huabo.monitor.mysql.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_RISK_FLOW")
@Schema(name="TblRiskFlowMySql")
public class TblRiskFlowMySql {

    @TableId("RISKID")
    private Long riskid;
    @TableId("FLOWID")
    private Long flowid;
}
