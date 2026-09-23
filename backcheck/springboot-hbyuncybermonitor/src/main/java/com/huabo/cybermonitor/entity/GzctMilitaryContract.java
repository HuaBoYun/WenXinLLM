package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.math.BigDecimal;
import java.time.LocalDateTime;;
import com.baomidou.mybatisplus.extension.activerecord.Model;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_MILITARY_CONTRACT")
public class GzctMilitaryContract extends Model<GzctMilitaryContract> {
    @TableId(value = "CONTRACT_ID", type = IdType.ASSIGN_UUID) private String contractId;
    @TableField("COMPANY_ID") private String companyId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("CONTRACT_NO") private String contractNo;
    @TableField("CONTRACT_NAME") private String contractName;
    @TableField("CONTRACT_AMOUNT") private BigDecimal contractAmount;
    @TableField("PROGRESS_RATE") private BigDecimal progressRate;
    @TableField("ACCEPTANCE_STATUS") private String acceptanceStatus;
    @TableField("IS_OVERDUE") private String isOverdue;
    @TableField("START_DATE") private String startDate;
    @TableField("END_DATE") private String endDate;
    @TableField("IS_BREACH") private String isBreach;
    @TableField("BREACH_DESC") private String breachDesc;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
    /** 组织路径 */
    @TableField("ORG_PATH")
    private String orgPath;
}
