package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_PROC_ALERT")
public class GzctProcAlert extends Model<GzctProcAlert> {
    @TableId(value = "ALERT_ID", type = IdType.ASSIGN_UUID)
    private String alertId;
    @TableField("COMPANY_ID")
    private String companyId;
    @TableField("COMPANY_NAME")
    private String companyName;
    @TableField("ALERT_TYPE")
    private String alertType;
    @TableField("ALERT_CONTENT")
    private String alertContent;
    @TableField("LEVEL")
    private String level;
    @TableField("STATUS")
    private String status;
    @TableField("RELATED_ID")
    private String relatedId;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
