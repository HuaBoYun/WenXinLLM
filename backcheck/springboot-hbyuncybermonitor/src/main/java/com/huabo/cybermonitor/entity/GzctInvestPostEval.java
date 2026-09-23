package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_TZ_POST_EVAL")
public class GzctInvestPostEval extends Model<GzctInvestPostEval> {
    @TableId(value = "EVAL_ID", type = IdType.ASSIGN_UUID)
    private String evalId;
    @TableField("PROJECT_ID")
    private String projectId;
    @TableField("PROJECT_NAME")
    private String projectName;
    @TableField("COMPANY_ID")
    private String companyId;
    @TableField("COMPANY_NAME")
    private String companyName;
    @TableField("EVAL_TYPE")
    private String evalType;
    @TableField("EXPECTED_RETURN")
    private BigDecimal expectedReturn;
    @TableField("ACTUAL_RETURN")
    private BigDecimal actualReturn;
    @TableField("DEVIATION")
    private BigDecimal deviation;
    @TableField("EVAL_SCORE")
    private BigDecimal evalScore;
    @TableField("EVAL_STATUS")
    private String evalStatus;
    @TableField("EVAL_CONCLUSION")
    private String evalConclusion;
    @TableField("EVAL_DATE")
    private LocalDate evalDate;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /** 组织ID */
    @TableField("ORG_ID")
    private String orgId;
}
