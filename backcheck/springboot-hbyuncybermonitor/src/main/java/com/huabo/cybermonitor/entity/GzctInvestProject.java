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
@TableName("GZCT_TZ_PROJECT")
public class GzctInvestProject extends Model<GzctInvestProject> {
    @TableId(value = "PROJECT_ID", type = IdType.ASSIGN_UUID)
    private String projectId;
    @TableField("PROJECT_NAME")
    private String projectName;
    @TableField("INVEST_TYPE")
    private String investType;
    @TableField("COMPANY_ID")
    private String companyId;
    @TableField("COMPANY_NAME")
    private String companyName;
    @TableField("INVEST_AMOUNT")
    private BigDecimal investAmount;
    @TableField("IS_MAIN_BIZ")
    private String isMainBiz;
    @TableField("PROJECT_STATUS")
    private String projectStatus;
    @TableField("EXPECTED_RETURN")
    private BigDecimal expectedReturn;
    @TableField("ACTUAL_RETURN")
    private BigDecimal actualReturn;
    @TableField("APPROVAL_DATE")
    private LocalDate approvalDate;
    @TableField("TARGET_COMPANY")
    private String targetCompany;
    @TableField("APPROVAL_STATUS")
    private String approvalStatus;
    @TableField("REMARK")
    private String remark;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /** 组织ID(当前数据所属公司的ORGID,关联TBL_ORGANIZATION.ORGID) */
    @TableField("ORG_ID")
    private String orgId;
}
