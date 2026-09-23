package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.time.LocalDateTime;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_PROPERTY_WARNING")
public class GzctPropertyWarning {
    @TableId(value = "WARNING_ID", type = IdType.ASSIGN_UUID) private String warningId;
    @TableField("PROPERTY_ID") private String propertyId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("WARNING_TYPE") private String warningType;
    @TableField("WARNING_LEVEL") private String warningLevel;
    @TableField("WARNING_CONTENT") private String warningContent;
    @TableField("WARNING_SOURCE") private String warningSource;
    @TableField("STATUS") private String status;
    @TableField("HANDLE_OPINION") private String handleOpinion;
    @TableField("HANDLE_TIME") private LocalDateTime handleTime;
    @TableField("HANDLE_BY") private String handleBy;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
}
