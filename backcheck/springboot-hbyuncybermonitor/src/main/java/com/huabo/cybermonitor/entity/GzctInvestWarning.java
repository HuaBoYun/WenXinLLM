package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_TZ_WARNING")
public class GzctInvestWarning extends Model<GzctInvestWarning> {
    @TableId(value = "WARNING_ID", type = IdType.ASSIGN_UUID)
    private String warningId;
    @TableField("WARNING_CODE")
    private String warningCode;
    @TableField("COMPANY_ID")
    private String companyId;
    @TableField("COMPANY_NAME")
    private String companyName;
    @TableField("PROJECT_ID")
    private String projectId;
    @TableField("PROJECT_NAME")
    private String projectName;
    @TableField("WARNING_TYPE")
    private String warningType;
    @TableField("LEVEL")
    private String level;
    @TableField("STATUS")
    private String status;
    @TableField("TRIGGER_TIME")
    private LocalDateTime triggerTime;
    @TableField("HANDLE_RESULT")
    private String handleResult;
    @TableField("HANDLE_USER")
    private String handleUser;
    @TableField("HANDLE_TIME")
    private LocalDateTime handleTime;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /** 组织ID */
    @TableField("ORG_ID")
    private String orgId;
}
