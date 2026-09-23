package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.math.BigDecimal;
import java.time.LocalDateTime;;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ACCOUNTING_ESTIMATE")
public class GzctAccountingEstimate {
    @TableId(value = "ESTIMATE_ID", type = IdType.ASSIGN_UUID) private String estimateId;
    @TableField("COMPANY_ID") private String companyId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("ESTIMATE_ITEM") private String estimateItem;
    @TableField("ESTIMATE_METHOD") private String estimateMethod;
    @TableField("KEY_ASSUMPTION") private String keyAssumption;
    @TableField("PROVISION_AMOUNT") private BigDecimal provisionAmount;
    @TableField("IMPACT_AMOUNT") private BigDecimal impactAmount;
    @TableField("PERIOD") private String period;
    @TableField("ESTIMATE_NAME") private String estimateName;
    @TableField("IS_CHANGED") private String isChanged;
    @TableField("IS_COMPLIANT") private String isCompliant;
    @TableField("PREVIOUS_VALUE") private BigDecimal previousValue;
    @TableField("CURRENT_VALUE") private BigDecimal currentValue;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
    /** 组织路径 */
    @TableField("ORG_PATH")
    private String orgPath;
}
