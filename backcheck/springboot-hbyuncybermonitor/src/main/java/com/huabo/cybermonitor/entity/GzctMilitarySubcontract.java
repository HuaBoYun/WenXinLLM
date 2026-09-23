package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.math.BigDecimal;
import java.time.LocalDateTime;;
import com.baomidou.mybatisplus.extension.activerecord.Model;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_MILITARY_SUBCONTRACT")
public class GzctMilitarySubcontract extends Model<GzctMilitarySubcontract> {
    @TableId(value = "SUB_ID", type = IdType.ASSIGN_UUID) private String subId;
    @TableField("COMPANY_ID") private String companyId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("TASK_NAME") private String taskName;
    @TableField("SUBCONTRACTOR") private String subcontractor;
    @TableField("SUBCONTRACT_TYPE") private String subcontractType;
    @TableField("SUB_AMOUNT") private BigDecimal subAmount;
    @TableField("APPROVAL_STATUS") private String approvalStatus;
    @TableField("IS_COMPLIANT") private String isCompliant;
    @TableField("VIOLATION_DESC") private String violationDesc;
    @TableField("APPROVE_USER") private String approveUser;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
}
