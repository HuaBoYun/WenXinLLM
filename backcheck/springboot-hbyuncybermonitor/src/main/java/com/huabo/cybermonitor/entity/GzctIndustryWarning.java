package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.time.LocalDateTime;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_INDUSTRY_WARNING")
public class GzctIndustryWarning {
    @TableId(value = "WARNING_ID", type = IdType.ASSIGN_UUID) private String warningId;
    @TableField("INDUSTRY_NAME") private String industryName;
    @TableField("COMPANY_ID") private String companyId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("WARNING_TYPE") private String warningType;
    @TableField("WARNING_CONTENT") private String warningContent;
    @TableField("LEVEL") private String level;
    @TableField("STATUS") private String status;
    @TableField("WARN_NO") private String warnNo;
    @TableField("WARN_TIME") private LocalDateTime warnTime;
    @TableField("RISK_SCORE") private Integer riskScore;
    @TableField("HANDLER") private String handler;
    @TableField("DESCRIPTION") private String description;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
    /** 组织路径 */
    @TableField("ORG_PATH")
    private String orgPath;
}
