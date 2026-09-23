package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_OVERSEAS_EMERGENCY")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GzctOverseasEmergency extends Model<GzctOverseasEmergency> {
    @TableId(value = "EMERGENCY_ID", type = IdType.ASSIGN_UUID) private String emergencyId;
    @TableField("UNIT_ID") private String unitId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("COUNTRY") private String country;
    @TableField("EVENT_TYPE") private String eventType;
    @TableField("EVENT_DESC") private String eventDesc;
    @TableField("EVENT_NAME") private String eventName;
    @TableField("SEVERITY") private String severity;
    @TableField("RISK_LEVEL") private String riskLevel;
    @TableField("STATUS") private String status;
    @TableField("MEASURES") private String measures;
    @TableField("LEADER") private String leader;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("OCCUR_TIME") private LocalDateTime occurTime;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
}
