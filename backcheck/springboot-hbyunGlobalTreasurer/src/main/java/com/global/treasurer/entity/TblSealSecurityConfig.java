package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.AllArgsConstructor;
import lombok.Builder;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 印鉴安全配置实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-25
 */
// @Data // 已移除,使用手动编写的getter/setter
@Builder
// @NoArgsConstructor // 已移除
// @AllArgsConstructor // 已移除
@TableName("TBL_SEAL_SECURITY_CONFIG")
public class TblSealSecurityConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 配置ID (主键)
     */
    @TableId(value = "CONFIG_ID", type = IdType.INPUT)
    private String configId;

    /**
     * 印鉴ID (唯一)
     */
    @TableField("SEAL_ID")
    private String sealId;

    /**
     * 安全级别(1-低,2-中,3-高)
     */
    @TableField("SECURITY_LEVEL")
    private Integer securityLevel;

    /**
     * 授权用户ID列表(JSON数组字符串)
     */
    @TableField("ALLOWED_USERS")
    private String allowedUsers;

    /**
     * 授权部门ID列表(JSON数组字符串)
     */
    @TableField("ALLOWED_DEPTS")
    private String allowedDepts;

    /**
     * 使用开始时间(HH:mm)
     */
    @TableField("USAGE_START_TIME")
    private String usageStartTime;

    /**
     * 使用结束时间(HH:mm)
     */
    @TableField("USAGE_END_TIME")
    private String usageEndTime;

    /**
     * 每日最大使用次数(0-无限制)
     */
    @TableField("MAX_DAILY_USAGE")
    private Integer maxDailyUsage;

    /**
     * 是否需要审批(1-是,0-否)
     */
    @TableField("REQUIRE_APPROVAL")
    private Integer requireApproval;

    /**
     * 审批人ID列表(JSON数组字符串)
     */
    @TableField("APPROVAL_USERS")
    private String approvalUsers;

    /**
     * 配置是否启用(1-启用,0-禁用)
     */
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /**
     * 备注
     */
    @TableField("REMARKS")
    private String remarks;

    /**
     * 组织ID
     */
    @TableField("ORG_ID")
    private String orgId;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建用户
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 更新用户
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getConfigId() { return configId; }
    public void setConfigId(String configId) { this.configId = configId; }
    public String getSealId() { return sealId; }
    public void setSealId(String sealId) { this.sealId = sealId; }
    public Integer getSecurityLevel() { return securityLevel; }
    public void setSecurityLevel(Integer securityLevel) { this.securityLevel = securityLevel; }
    public String getAllowedUsers() { return allowedUsers; }
    public void setAllowedUsers(String allowedUsers) { this.allowedUsers = allowedUsers; }
    public String getAllowedDepts() { return allowedDepts; }
    public void setAllowedDepts(String allowedDepts) { this.allowedDepts = allowedDepts; }
    public String getUsageStartTime() { return usageStartTime; }
    public void setUsageStartTime(String usageStartTime) { this.usageStartTime = usageStartTime; }
    public String getUsageEndTime() { return usageEndTime; }
    public void setUsageEndTime(String usageEndTime) { this.usageEndTime = usageEndTime; }
    public Integer getMaxDailyUsage() { return maxDailyUsage; }
    public void setMaxDailyUsage(Integer maxDailyUsage) { this.maxDailyUsage = maxDailyUsage; }
    public Integer getRequireApproval() { return requireApproval; }
    public void setRequireApproval(Integer requireApproval) { this.requireApproval = requireApproval; }
    public String getApprovalUsers() { return approvalUsers; }
    public void setApprovalUsers(String approvalUsers) { this.approvalUsers = approvalUsers; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public String getOrgId() { return orgId; }
    public void setOrgId(String orgId) { this.orgId = orgId; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }

}
