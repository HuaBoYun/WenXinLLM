package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.time.LocalDateTime;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ACCOUNTING_VERIFICATION")
public class GzctAccountingVerification {
    @TableId(value = "VERIFICATION_ID", type = IdType.ASSIGN_UUID) private String verificationId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("ESTIMATE_ITEM") private String estimateItem;
    @TableField("CHANGE_BEFORE") private String changeBefore;
    @TableField("CHANGE_AFTER") private String changeAfter;
    @TableField("PROFIT_IMPACT") private String profitImpact;
    @TableField("VERIFICATION_OPINION") private String verificationOpinion;
    @TableField("VERIFICATION_RESULT") private String verificationResult;
    @TableField("VERIFIER") private String verifier;
    @TableField("VERIFY_TIME") private LocalDateTime verifyTime;
    @TableField("STATUS") private String status;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
}
