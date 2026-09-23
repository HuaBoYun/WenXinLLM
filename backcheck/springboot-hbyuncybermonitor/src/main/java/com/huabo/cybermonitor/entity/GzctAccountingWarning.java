package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ACCOUNTING_WARNING")
public class GzctAccountingWarning {

    @TableId(value = "WARNING_ID", type = IdType.ASSIGN_UUID)
    private String warningId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("WARNING_TYPE")
    private String warningType;

    @TableField("WARNING_LEVEL")
    private String warningLevel;

    @TableField("WARNING_TITLE")
    private String warningTitle;

    @TableField("WARNING_CONTENT")
    private String warningContent;

    @TableField("STATUS")
    private String status;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
