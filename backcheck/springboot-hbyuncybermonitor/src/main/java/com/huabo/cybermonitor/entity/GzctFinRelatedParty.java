package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.math.BigDecimal;
import java.time.LocalDateTime;;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_FIN_RELATED_PARTY")
public class GzctFinRelatedParty {
    @TableId(value = "PARTY_ID", type = IdType.ASSIGN_UUID) private String partyId;
    @TableField("COMPANY_ID") private String companyId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("PARTY_NAME") private String partyName;
    @TableField("TRANSACTION_NAME") private String transactionName;
    @TableField("RELATION_TYPE") private String relationType;
    @TableField("TRANSACTION_TYPE") private String transactionType;
    @TableField("TRANSACTION_AMOUNT") private BigDecimal transactionAmount;
    @TableField("BALANCE_AMOUNT") private BigDecimal balanceAmount;
    @TableField("IS_MAJOR") private String isMajor;
    @TableField("PERIOD") private String period;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
}
