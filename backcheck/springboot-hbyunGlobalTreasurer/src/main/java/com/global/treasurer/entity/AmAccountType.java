package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除
// import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账户类型实体类
 * 
 * @author system
 * @since 2024-01-01
 */
// @Data // 已移除,使用手动编写的getter/setter
// @EqualsAndHashCode // 已移除
@TableName("t_am_account_type")
public class AmAccountType implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 类型编码
     */
    @TableField("type_code")
    private String typeCode;

    /**
     * 类型名称
     */
    @TableField("type_name")
    private String typeName;

    /**
     * 类型英文名称
     */
    @TableField("type_english_name")
    private String typeEnglishName;

    /**
     * 上级类型ID
     */
    @TableField("parent_type_id")
    private Long parentTypeId;

    /**
     * 类型层级
     */
    @TableField("type_level")
    private Integer typeLevel;

    /**
     * 类型描述
     */
    @TableField("type_description")
    private String typeDescription;

    /**
     * 适用范围：ALL-全部，DOMESTIC-境内，OVERSEAS-境外
     */
    @TableField("applicable_scope")
    private String applicableScope;

    /**
     * 支持币种（JSON数组）
     */
    @TableField("supported_currencies")
    private String supportedCurrencies;

    /**
     * 开户条件
     */
    @TableField("opening_conditions")
    private String openingConditions;

    /**
     * 使用规则
     */
    @TableField("usage_rules")
    private String usageRules;

    /**
     * 监管要求
     */
    @TableField("regulatory_requirements")
    private String regulatoryRequirements;

    /**
     * 业务功能（JSON数组）
     */
    @TableField("business_functions")
    private String businessFunctions;

    /**
     * 是否需要审批
     */
    @TableField("require_approval")
    private String requireApproval;

    /**
     * 审批流程ID
     */
    @TableField("approval_workflow_id")
    private Long approvalWorkflowId;

    /**
     * 是否支持银企直连
     */
    @TableField("support_direct_connection")
    private String supportDirectConnection;

    /**
     * 是否支持网银
     */
    @TableField("support_online_banking")
    private String supportOnlineBanking;

    /**
     * 是否支持手机银行
     */
    @TableField("support_mobile_banking")
    private String supportMobileBanking;

    /**
     * 默认最低余额
     */
    @TableField("default_minimum_balance")
    private String defaultMinimumBalance;

    /**
     * 默认透支额度
     */
    @TableField("default_overdraft_limit")
    private String defaultOverdraftLimit;

    /**
     * 排序号
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 状态：1-有效，0-无效
     */
    @TableField("status")
    private String status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField("create_user")
    private Long createUser;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @TableField("update_user")
    private Long updateUser;

    /**
     * 版本号
     */
    @TableField("version_no")
    private Long versionNo;

    /**
     * 客户端IP
     */
    @TableField("client_ip")
    private String clientIp;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTypeCode() { return typeCode; }
    public void setTypeCode(String typeCode) { this.typeCode = typeCode; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public String getTypeEnglishName() { return typeEnglishName; }
    public void setTypeEnglishName(String typeEnglishName) { this.typeEnglishName = typeEnglishName; }
    public Long getParentTypeId() { return parentTypeId; }
    public void setParentTypeId(Long parentTypeId) { this.parentTypeId = parentTypeId; }
    public Integer getTypeLevel() { return typeLevel; }
    public void setTypeLevel(Integer typeLevel) { this.typeLevel = typeLevel; }
    public String getTypeDescription() { return typeDescription; }
    public void setTypeDescription(String typeDescription) { this.typeDescription = typeDescription; }
    public String getApplicableScope() { return applicableScope; }
    public void setApplicableScope(String applicableScope) { this.applicableScope = applicableScope; }
    public String getSupportedCurrencies() { return supportedCurrencies; }
    public void setSupportedCurrencies(String supportedCurrencies) { this.supportedCurrencies = supportedCurrencies; }
    public String getOpeningConditions() { return openingConditions; }
    public void setOpeningConditions(String openingConditions) { this.openingConditions = openingConditions; }
    public String getUsageRules() { return usageRules; }
    public void setUsageRules(String usageRules) { this.usageRules = usageRules; }
    public String getRegulatoryRequirements() { return regulatoryRequirements; }
    public void setRegulatoryRequirements(String regulatoryRequirements) { this.regulatoryRequirements = regulatoryRequirements; }
    public String getBusinessFunctions() { return businessFunctions; }
    public void setBusinessFunctions(String businessFunctions) { this.businessFunctions = businessFunctions; }
    public String getRequireApproval() { return requireApproval; }
    public void setRequireApproval(String requireApproval) { this.requireApproval = requireApproval; }
    public Long getApprovalWorkflowId() { return approvalWorkflowId; }
    public void setApprovalWorkflowId(Long approvalWorkflowId) { this.approvalWorkflowId = approvalWorkflowId; }
    public String getSupportDirectConnection() { return supportDirectConnection; }
    public void setSupportDirectConnection(String supportDirectConnection) { this.supportDirectConnection = supportDirectConnection; }
    public String getSupportOnlineBanking() { return supportOnlineBanking; }
    public void setSupportOnlineBanking(String supportOnlineBanking) { this.supportOnlineBanking = supportOnlineBanking; }
    public String getSupportMobileBanking() { return supportMobileBanking; }
    public void setSupportMobileBanking(String supportMobileBanking) { this.supportMobileBanking = supportMobileBanking; }
    public String getDefaultMinimumBalance() { return defaultMinimumBalance; }
    public void setDefaultMinimumBalance(String defaultMinimumBalance) { this.defaultMinimumBalance = defaultMinimumBalance; }
    public String getDefaultOverdraftLimit() { return defaultOverdraftLimit; }
    public void setDefaultOverdraftLimit(String defaultOverdraftLimit) { this.defaultOverdraftLimit = defaultOverdraftLimit; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public Long getCreateUser() { return createUser; }
    public void setCreateUser(Long createUser) { this.createUser = createUser; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Long getUpdateUser() { return updateUser; }
    public void setUpdateUser(Long updateUser) { this.updateUser = updateUser; }
    public Long getVersionNo() { return versionNo; }
    public void setVersionNo(Long versionNo) { this.versionNo = versionNo; }
    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }

}
