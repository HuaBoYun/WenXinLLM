package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_FIN_ENTRUSTED_LOAN")
public class GzctFinEntrustedLoan extends Model<GzctFinEntrustedLoan> {
    @TableId(value = "LOAN_ID", type = IdType.ASSIGN_UUID)
    private String id;
    @TableField("COMPANY_ID") private String companyId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("LENDER_NAME") private String lenderName;
    @TableField("BORROWER_NAME") private String borrowerName;
    @TableField("LOAN_AMOUNT") private BigDecimal loanAmount;
    @TableField("INTEREST_RATE") private BigDecimal interestRate;
    @TableField("START_DATE") private LocalDateTime startDate;
    @TableField("END_DATE") private LocalDateTime endDate;
    @TableField("LOAN_PURPOSE") private String loanPurpose;
    @TableField("STATUS") private String status;
    @TableField("REPAYMENT_METHOD") private String repaymentMethod;
    @TableField("GUARANTEE_MEASURES") private String guaranteeMeasures;
    @TableField("IS_RELATED_PARTY") private Integer isRelatedParty;
    @TableField("IS_OVERDUE") private String isOverdue;
    @TableField("OVERDUE_AMOUNT") private BigDecimal overdueAmount;
    @TableField("RISK_LEVEL") private String riskLevel;
    @TableField("REMARK") private String remark;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
}
