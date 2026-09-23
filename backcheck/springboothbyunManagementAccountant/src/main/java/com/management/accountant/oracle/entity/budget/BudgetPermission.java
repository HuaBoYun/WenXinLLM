package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("TBL_BUDGET_PERMISSION")
public class BudgetPermission implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "PERMISSION_ID", type = IdType.ASSIGN_UUID)
    private String permissionId;
    @TableField("PERMISSION_CODE")
    private String permissionCode;
    @TableField("PERMISSION_NAME")
    private String permissionName;
    @TableField("PERMISSION_TYPE")
    private String permissionType;
    @TableField("ROLE_ID")
    private String roleId;
    @TableField("ROLE_NAME")
    private String roleName;
    @TableField("USER_ID")
    private String userId;
    @TableField("USER_NAME")
    private String userName;
    @TableField("RESOURCE_TYPE")
    private String resourceType;
    @TableField("RESOURCE_ID")
    private String resourceId;
    @TableField("OPERATION_TYPE")
    private String operationType;
    @TableField("IS_ENABLED")
    private Integer isEnabled;
    @TableField("DESCRIPTION")
    private String description;
    @TableField("CREATOR_ID")
    private String creatorId;
    @TableField("CREATOR_NAME")
    private String creatorName;
    @TableField("CREATE_TIME")
    private Date createTime;
    @TableField("UPDATER_ID")
    private String updaterId;
    @TableField("UPDATER_NAME")
    private String updaterName;
    @TableField("UPDATE_TIME")
    private Date updateTime;
    @TableField("IS_DELETED")
    private Integer isDeleted;
    @TableField("COMPANY_ID")
    private String companyId;
    @TableField("COMPANY_NAME")
    private String companyName;
}

