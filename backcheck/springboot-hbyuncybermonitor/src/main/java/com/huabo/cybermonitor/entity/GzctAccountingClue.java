package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ACCOUNTING_CLUE")
public class GzctAccountingClue {

    @TableId(value = "CLUE_ID", type = IdType.ASSIGN_UUID)
    private String clueId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("CLUE_TYPE")
    private String clueType;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("FOUND_TIME")
    private LocalDate foundTime;

    @TableField("ASSIGNEE")
    private String assignee;

    @TableField("STATUS")
    private String status;

    @TableField("FRAUD_TYPE")
    private String fraudType;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
