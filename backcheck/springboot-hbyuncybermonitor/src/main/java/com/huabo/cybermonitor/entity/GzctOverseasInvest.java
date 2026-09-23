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
@TableName("GZCT_OVERSEAS_INVEST")
public class GzctOverseasInvest extends Model<GzctOverseasInvest> {
    @TableId(value = "INVEST_ID", type = IdType.ASSIGN_UUID) private String investId;
    @TableField("UNIT_ID") private String unitId;
    // 前端使用 unitName，与 companyName 对应
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("UNIT_NAME") private String unitName;
    @TableField("PROJECT_NAME") private String projectName;
    @TableField("COUNTRY") private String country;
    @TableField("INVEST_AMOUNT") private BigDecimal investAmount;
    @TableField("SHAREHOLDING_RATIO") private BigDecimal shareholdingRatio;
    @TableField("INVEST_TYPE") private String investType;
    @TableField("APPROVAL_STATUS") private String approvalStatus;
    @TableField("APPROVAL_NO") private String approvalNo;
    @TableField("APPROVAL_DATE") private LocalDate approvalDate;
    @TableField("RISK_LEVEL") private String riskLevel;
    @TableField("EXPECTED_RETURN") private BigDecimal expectedReturn;
    @TableField("ACTUAL_RETURN") private BigDecimal actualReturn;
    @TableField("REMARK") private String remark;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;

    /** 组织路径(物化路径),格式:/根ID/子ID/孙ID/,用于穿透式查询 */
    @TableField("ORG_PATH")
    private String orgPath;
}
