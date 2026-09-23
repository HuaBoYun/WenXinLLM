package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("TBL_BUDGET_AUDIT_TRAIL")
public class BudgetAuditTrail implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "AUDIT_ID", type = IdType.ASSIGN_UUID)
    private String auditId;
    @TableField("AUDIT_TYPE")
    private String auditType;
    @TableField("MODULE_NAME")
    private String moduleName;
    @TableField("OPERATION_TYPE")
    private String operationType;
    @TableField("TARGET_TABLE")
    private String targetTable;
    @TableField("TARGET_ID")
    private String targetId;
    @TableField("TARGET_NAME")
    private String targetName;
    @TableField("OLD_VALUE")
    private String oldValue;
    @TableField("NEW_VALUE")
    private String newValue;
    @TableField("CHANGE_DETAIL")
    private String changeDetail;
    @TableField("OPERATOR_ID")
    private String operatorId;
    @TableField("OPERATOR_NAME")
    private String operatorName;
    @TableField("OPERATION_TIME")
    private Date operationTime;
    @TableField("IP_ADDRESS")
    private String ipAddress;
    @TableField("USER_AGENT")
    private String userAgent;
    @TableField("RISK_LEVEL")
    private String riskLevel;
    @TableField("RESULT")
    private String result;
    @TableField("SESSION_ID")
    private String sessionId;
    @TableField("REQUEST_URL")
    private String requestUrl;
    @TableField("REQUEST_METHOD")
    private String requestMethod;
    @TableField("REQUEST_PARAMS")
    private String requestParams;
    @TableField("RESPONSE_DATA")
    private String responseData;
    @TableField("EXECUTION_TIME")
    private Long executionTime;
    @TableField("OPERATION_DESCRIPTION")
    private String operationDescription;
    @TableField("STATUS")
    private String status;
    @TableField("REMARK")
    private String remark;
    @TableField("IS_DELETED")
    private Integer isDeleted;
    @TableField("COMPANY_ID")
    private String companyId;
}
