package com.global.treasurer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.io.Serializable;
import java.util.Date;

/**
 * 财资公共模块 - 印鉴使用记录表
 * 
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
@ApiModel(value = "TcSealUsageLog", description = "印鉴使用记录管理")
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除
@TableName("TC_SEAL_USAGE_LOG")
public class TcSealUsageLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.INPUT)
    @TableField("ID")
    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 组合ID
     */
    @TableField("COMBINATION_ID")
    @ApiModelProperty(value = "组合ID")
    private String combinationId;

    /**
     * 印鉴ID
     */
    @TableField("SEAL_ID")
    @ApiModelProperty(value = "印鉴ID")
    private String sealId;

    /**
     * 业务单据号
     */
    @TableField("BUSINESS_DOC_NO")
    @ApiModelProperty(value = "业务单据号")
    private String businessDocNo;

    /**
     * 业务类型
     */
    @TableField("BUSINESS_TYPE")
    @ApiModelProperty(value = "业务类型")
    private String businessType;

    /**
     * 使用目的
     */
    @TableField("USAGE_PURPOSE")
    @ApiModelProperty(value = "使用目的")
    private String usagePurpose;

    /**
     * 使用人
     */
    @TableField("USAGE_USER")
    @ApiModelProperty(value = "使用人")
    private String usageUser;

    /**
     * 使用时间
     */
    @TableField("USAGE_TIME")
    @ApiModelProperty(value = "使用时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date usageTime;

    /**
     * 使用地点
     */
    @TableField("USAGE_LOCATION")
    @ApiModelProperty(value = "使用地点")
    private String usageLocation;

    /**
     * 审批人
     */
    @TableField("APPROVAL_USER")
    @ApiModelProperty(value = "审批人")
    private String approvalUser;

    /**
     * 审批时间
     */
    @TableField("APPROVAL_TIME")
    @ApiModelProperty(value = "审批时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approvalTime;

    /**
     * 使用结果
     */
    @TableField("USAGE_RESULT")
    @ApiModelProperty(value = "使用结果")
    private String usageResult;

    /**
     * 异常原因
     */
    @TableField("EXCEPTION_REASON")
    @ApiModelProperty(value = "异常原因")
    private String exceptionReason;

    /**
     * IP地址
     */
    @TableField("IP_ADDRESS")
    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    /**
     * MAC地址
     */
    @TableField("MAC_ADDRESS")
    @ApiModelProperty(value = "MAC地址")
    private String macAddress;

    /**
     * 状态：1-启用，0-停用
     */
    @TableField("STATUS")
    @ApiModelProperty(value = "状态：1-启用，0-停用")
    private String status;

    /**
     * 备注
     */
    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    @ApiModelProperty(value = "更新时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUser;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateUser;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    @ApiModelProperty(value = "版本号", hidden = true)
    private Integer versionNo;

    /**
     * 根据ID创建实例
     */
    public static TcSealUsageLog ofId(String id) {
        TcSealUsageLog log = new TcSealUsageLog();
        log.setId(id);
        return log;
    }

    /**
     * 根据印鉴ID创建实例
     */
    public static TcSealUsageLog ofSealId(String sealId) {
        TcSealUsageLog log = new TcSealUsageLog();
        log.setSealId(sealId);
        return log;
    }

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCombinationId() { return combinationId; }
    public void setCombinationId(String combinationId) { this.combinationId = combinationId; }
    public String getSealId() { return sealId; }
    public void setSealId(String sealId) { this.sealId = sealId; }
    public String getBusinessDocNo() { return businessDocNo; }
    public void setBusinessDocNo(String businessDocNo) { this.businessDocNo = businessDocNo; }
    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
    public String getUsagePurpose() { return usagePurpose; }
    public void setUsagePurpose(String usagePurpose) { this.usagePurpose = usagePurpose; }
    public String getUsageUser() { return usageUser; }
    public void setUsageUser(String usageUser) { this.usageUser = usageUser; }
    public Date getUsageTime() { return usageTime; }
    public void setUsageTime(Date usageTime) { this.usageTime = usageTime; }
    public String getUsageLocation() { return usageLocation; }
    public void setUsageLocation(String usageLocation) { this.usageLocation = usageLocation; }
    public String getApprovalUser() { return approvalUser; }
    public void setApprovalUser(String approvalUser) { this.approvalUser = approvalUser; }
    public Date getApprovalTime() { return approvalTime; }
    public void setApprovalTime(Date approvalTime) { this.approvalTime = approvalTime; }
    public String getUsageResult() { return usageResult; }
    public void setUsageResult(String usageResult) { this.usageResult = usageResult; }
    public String getExceptionReason() { return exceptionReason; }
    public void setExceptionReason(String exceptionReason) { this.exceptionReason = exceptionReason; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getMacAddress() { return macAddress; }
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public Integer getVersionNo() { return versionNo; }
    public void setVersionNo(Integer versionNo) { this.versionNo = versionNo; }

}
