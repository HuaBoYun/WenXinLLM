package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;;
import com.baomidou.mybatisplus.extension.activerecord.Model;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_MILITARY_TASK_RECORD")
public class GzctMilitaryTaskRecord extends Model<GzctMilitaryTaskRecord> {
    @TableId(value = "RECORD_ID", type = IdType.ASSIGN_UUID) private String recordId;
    @TableField("COMPANY_ID") private String companyId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("TASK_NAME") private String taskName;
    @TableField("TASK_TYPE") private String taskType;
    @TableField("SECRET_LEVEL") private String secretLevel;
    @TableField("STATUS") private String status;
    @TableField("PROGRESS_RATE") private BigDecimal progressRate;
    @TableField("PLAN_END_DATE") private LocalDate planEndDate;
    @TableField("ACTUAL_END_DATE") private LocalDate actualEndDate;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
}
