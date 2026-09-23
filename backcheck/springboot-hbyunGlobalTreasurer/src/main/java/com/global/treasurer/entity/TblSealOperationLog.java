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
 * 印鉴操作日志实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-25
 */
// @Data // 已移除,使用手动编写的getter/setter
@Builder
// @NoArgsConstructor // 已移除
// @AllArgsConstructor // 已移除
@TableName("TBL_SEAL_OPERATION_LOG")
public class TblSealOperationLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 日志ID (自增主键)
     */
    @TableId(value = "LOG_ID", type = IdType.INPUT)
    private Long logId;

    /**
     * 印鉴ID
     */
    @TableField("SEAL_ID")
    private String sealId;

    /**
     * 印鉴编码
     */
    @TableField("SEAL_CODE")
    private String sealCode;

    /**
     * 印鉴名称
     */
    @TableField("SEAL_NAME")
    private String sealName;

    /**
     * 操作类型(LOCK-锁定,UNLOCK-解锁,CONFIG-配置修改,VIEW-查看,CREATE-创建,UPDATE-更新,DELETE-删除)
     */
    @TableField("OPERATION_TYPE")
    private String operationType;

    /**
     * 操作描述
     */
    @TableField("OPERATION_DESC")
    private String operationDesc;

    /**
     * 操作原因
     */
    @TableField("OPERATION_REASON")
    private String operationReason;

    /**
     * 操作人ID
     */
    @TableField("OPERATOR_ID")
    private String operatorId;

    /**
     * 操作人姓名
     */
    @TableField("OPERATOR_NAME")
    private String operatorName;

    /**
     * 操作时间
     */
    @TableField("OPERATION_TIME")
    private Date operationTime;

    /**
     * 操作IP地址
     */
    @TableField("IP_ADDRESS")
    private String ipAddress;

    /**
     * 操作结果(SUCCESS-成功,FAIL-失败)
     */
    @TableField("OPERATION_RESULT")
    private String operationResult;

    /**
     * 错误信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

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

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }
    public String getSealId() { return sealId; }
    public void setSealId(String sealId) { this.sealId = sealId; }
    public String getSealCode() { return sealCode; }
    public void setSealCode(String sealCode) { this.sealCode = sealCode; }
    public String getSealName() { return sealName; }
    public void setSealName(String sealName) { this.sealName = sealName; }
    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }
    public String getOperationDesc() { return operationDesc; }
    public void setOperationDesc(String operationDesc) { this.operationDesc = operationDesc; }
    public String getOperationReason() { return operationReason; }
    public void setOperationReason(String operationReason) { this.operationReason = operationReason; }
    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    public Date getOperationTime() { return operationTime; }
    public void setOperationTime(Date operationTime) { this.operationTime = operationTime; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getOperationResult() { return operationResult; }
    public void setOperationResult(String operationResult) { this.operationResult = operationResult; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public String getOrgId() { return orgId; }
    public void setOrgId(String orgId) { this.orgId = orgId; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

}
