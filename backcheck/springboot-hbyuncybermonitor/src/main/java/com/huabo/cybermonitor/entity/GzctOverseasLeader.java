package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.time.LocalDate;
import java.time.LocalDateTime;;
import com.baomidou.mybatisplus.extension.activerecord.Model;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_OVERSEAS_LEADER")
public class GzctOverseasLeader extends Model<GzctOverseasLeader> {
    @TableId(value = "LEADER_ID", type = IdType.ASSIGN_UUID) private String leaderId;
    @TableField("UNIT_ID") private String unitId;
    // 前端用 name，对应 leaderName
    @TableField("LEADER_NAME") private String leaderName;
    @TableField("NAME") private String name;
    @TableField("GENDER") private String gender;
    // 前端用 companyName
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("LEADER_TYPE") private String leaderType;
    @TableField("POSITION") private String position;
    @TableField("COUNTRY") private String country;
    @TableField("PHONE") private String phone;
    @TableField("EMAIL") private String email;
    // 驾场方式: ONSITE/REMOTE
    @TableField("WORK_MODE") private String workMode;
    // 在职状态: ACTIVE/INACTIVE
    @TableField("STATUS") private String status;
    @TableField("REMARK") private String remark;
    @TableField("APPOINT_DATE") private LocalDate appointDate;
    @TableField("TERM_END_DATE") private LocalDate termEndDate;
    @TableField("PERFORMANCE_RATE") private String performanceRate;
    @TableField("ASSESSMENT_YEAR") private String assessmentYear;
    @TableField("CONTACT_INFO") private String contactInfo;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
}
