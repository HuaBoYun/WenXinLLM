package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.time.LocalDate;
import java.time.LocalDateTime;;
import com.baomidou.mybatisplus.extension.activerecord.Model;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_OVERSEAS_PERSONNEL")
public class GzctOverseasPersonnel extends Model<GzctOverseasPersonnel> {
    @TableId(value = "PERSONNEL_ID", type = IdType.ASSIGN_UUID) private String personnelId;
    @TableField("UNIT_ID") private String unitId;
    // 前端用 name，对应 personnelName
    @TableField("PERSONNEL_NAME") private String personnelName;
    @TableField("NAME") private String name;
    @TableField("COUNTRY") private String country;
    @TableField("CITY") private String city;
    @TableField("UNIT") private String unit;
    @TableField("POSITION") private String position;
    @TableField("DEPARTURE_DATE") private LocalDate departureDate;
    @TableField("RETURN_DATE") private LocalDate returnDate;
    // 安全状态: SAFE/AT_RISK/EMERGENCY (前端: safe/warning/danger)
    @TableField("SAFETY_STATUS") private String safetyStatus;
    // 前端用 contact，对应 emergencyContact
    @TableField("EMERGENCY_CONTACT") private String emergencyContact;
    @TableField("CONTACT") private String contact;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
}
