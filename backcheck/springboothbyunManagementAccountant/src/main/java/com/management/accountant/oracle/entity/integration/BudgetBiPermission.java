package com.management.accountant.oracle.entity.integration;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * BI权限实体类
 *
 * @author AI Agent
 * @date 2026-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_BI_PERMISSION")
public class BudgetBiPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "PERMISSION_ID", type = IdType.ASSIGN_UUID)
    private String permissionId;

    @TableField("BI_ID")
    private String biId;

    @TableField("USER_NAME")
    private String userName;

    @TableField("USER_EMAIL")
    private String userEmail;

    @TableField("ROLE")
    private String role;

    @TableField("PERMISSIONS")
    private String permissions;

    @TableField("GRANT_TIME")
    private Date grantTime;

    @TableField("STATUS")
    private String status;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATED_BY")
    private String createdBy;

    @TableField("CREATED_TIME")
    private Date createdTime;

    @TableField("UPDATED_BY")
    private String updatedBy;

    @TableField("UPDATED_TIME")
    private Date updatedTime;
}
