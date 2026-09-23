package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_MILITARY_SECURITY")
public class GzctMilitarySecurity extends Model<GzctMilitarySecurity> {

    @TableId(value = "SECURITY_ID", type = IdType.ASSIGN_UUID)
    private String securityId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("SECRET_LEVEL")
    private String secretLevel;

    @TableField("CHECK_DATE")
    private LocalDate checkDate;

    @TableField("CHECK_RESULT")
    private String checkResult;

    @TableField("VIOLATION_COUNT")
    private Integer violationCount;

    @TableField("VIOLATION_DESC")
    private String violationDesc;

    @TableField("RECTIFICATION_STATUS")
    private String rectificationStatus;

    @TableField("RECTIFICATION_DEADLINE")
    private LocalDate rectificationDeadline;

    @TableField("CHECKER_NAME")
    private String checkerName;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
