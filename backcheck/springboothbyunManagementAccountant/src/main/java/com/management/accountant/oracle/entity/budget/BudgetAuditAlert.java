package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("TBL_BUDGET_AUDIT_ALERT")
public class BudgetAuditAlert implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ALERT_ID", type = IdType.ASSIGN_UUID)
    private String alertId;
    @TableField("ALERT_TYPE")
    private String alertType;
    @TableField("ALERT_LEVEL")
    private String alertLevel;
    @TableField("ALERT_TITLE")
    private String alertTitle;
    @TableField("ALERT_CONTENT")
    private String alertContent;
    @TableField("RELATED_AUDIT_ID")
    private String relatedAuditId;
    @TableField("ALERT_STATUS")
    private String alertStatus;
    @TableField("HANDLER_ID")
    private String handlerId;
    @TableField("HANDLER_NAME")
    private String handlerName;
    @TableField("HANDLE_TIME")
    private Date handleTime;
    @TableField("HANDLE_REMARK")
    private String handleRemark;
    @TableField("CREATE_TIME")
    private Date createTime;
    @TableField("IS_DELETED")
    private Integer isDeleted;
    @TableField("COMPANY_ID")
    private String companyId;
}
