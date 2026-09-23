package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("TBL_BUDGET_AUDIT_SETTING")
public class BudgetAuditSetting implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "SETTING_ID", type = IdType.ASSIGN_UUID)
    private String settingId;
    @TableField("SETTING_NAME")
    private String settingName;
    @TableField("SETTING_KEY")
    private String settingKey;
    @TableField("SETTING_VALUE")
    private String settingValue;
    @TableField("SETTING_TYPE")
    private String settingType;
    @TableField("DESCRIPTION")
    private String description;
    @TableField("IS_ENABLED")
    private Integer isEnabled;
    @TableField("CREATOR_ID")
    private String creatorId;
    @TableField("CREATOR_NAME")
    private String creatorName;
    @TableField("CREATE_TIME")
    private Date createTime;
    @TableField("UPDATE_TIME")
    private Date updateTime;
    @TableField("IS_DELETED")
    private Integer isDeleted;
    @TableField("COMPANY_ID")
    private String companyId;
}
