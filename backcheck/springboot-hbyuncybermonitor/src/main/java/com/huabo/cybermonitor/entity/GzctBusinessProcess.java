package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_BUSINESS_PROCESS")
public class GzctBusinessProcess extends Model<GzctBusinessProcess> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("PROCESS_CODE")
    private String processCode;

    @TableField("PROCESS_NAME")
    private String processName;

    @TableField("PROCESS_TYPE")
    private String processType;

    @TableField("OWNER")
    private String owner;

    @TableField("STEP_COUNT")
    private Integer stepCount;

    @TableField("AVG_DURATION")
    private BigDecimal avgDuration;

    @TableField("EFFICIENCY")
    private BigDecimal efficiency;

    @TableField("INSTANCE_COUNT")
    private Integer instanceCount;

    @TableField("LAST_MODIFIED")
    private String lastModified;

    @TableField("STATUS")
    private String status;

    @TableField("REMARK")
    private String remark;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
