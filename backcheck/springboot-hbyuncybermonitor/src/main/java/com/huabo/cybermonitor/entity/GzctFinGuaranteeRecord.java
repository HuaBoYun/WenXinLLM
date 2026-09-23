package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_FIN_GUARANTEE_RECORD")
public class GzctFinGuaranteeRecord extends Model<GzctFinGuaranteeRecord> {
    @TableId(value = "GUARANTEE_ID", type = IdType.ASSIGN_UUID)
    private String guaranteeId;
    @TableField("COMPANY_ID") private String companyId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("GUARANTOR_NAME") private String guarantorName;
    @TableField("GUARANTEE_TYPE") private String guaranteeType;
    @TableField("GUARANTEE_AMOUNT") private BigDecimal guaranteeAmount;
    @TableField("GUARANTEE_START") private LocalDate guaranteeStart;
    @TableField("GUARANTEE_END") private LocalDate guaranteeEnd;
    @TableField("IS_OVERDUE") private String isOverdue;
    @TableField("RISK_LEVEL") private String riskLevel;
    @TableField("STATUS") private String status;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
}
