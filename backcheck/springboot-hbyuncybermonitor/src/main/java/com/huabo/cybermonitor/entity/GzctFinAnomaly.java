package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.math.BigDecimal;
import java.time.LocalDateTime;;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_FIN_ANOMALY")
public class GzctFinAnomaly {
    @TableId(value = "ANOMALY_ID", type = IdType.ASSIGN_UUID) private String anomalyId;
    @TableField("COMPANY_ID") private String companyId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("ANOMALY_TYPE") private String anomalyType;
    @TableField("DETECTION_METHOD") private String detectionMethod;
    @TableField("INDICATOR_NAME") private String indicatorName;
    @TableField("EXPECTED_VALUE") private BigDecimal expectedValue;
    @TableField("ACTUAL_VALUE") private BigDecimal actualValue;
    @TableField("DEVIATION_RATE") private BigDecimal deviationRate;
    @TableField("PERIOD") private String period;
    @TableField("STATUS") private String status;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
}
