package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金预警实体类
 * @author Claude
 * @date 2026-01-20
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FUND_ALERT")
@ApiModel(value = "TblFundAlert", description = "资金预警实体")
public class TblFundAlert implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ALERT_ID", type = IdType.ASSIGN_UUID)
    @ApiModelProperty("预警ID")
    private String alertId;

    @TableField("ALERT_NO")
    @ApiModelProperty("预警编号")
    private String alertNo;

    @TableField("ALERT_TYPE")
    @ApiModelProperty("预警类型(BALANCE_LOW-余额过低/BALANCE_HIGH-余额过高/TRANSACTION_ABNORMAL-交易异常/EXECUTION_FAILED-执行失败)")
    private String alertType;

    @TableField("ALERT_LEVEL")
    @ApiModelProperty("预警级别(INFO-提示/WARNING-警告/ERROR-错误/CRITICAL-严重)")
    private String alertLevel;

    @TableField("POOL_ID")
    @ApiModelProperty("资金池ID")
    private String poolId;

    @TableField("ACCOUNT_ID")
    @ApiModelProperty("账户ID")
    private String accountId;

    @TableField("ACCOUNT_NO")
    @ApiModelProperty("账号")
    private String accountNo;

    @TableField("COMPANY_ID")
    @ApiModelProperty("公司ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    @ApiModelProperty("公司名称")
    private String companyName;

    @TableField("ALERT_TITLE")
    @ApiModelProperty("预警标题")
    private String alertTitle;

    @TableField("ALERT_CONTENT")
    @ApiModelProperty("预警内容")
    private String alertContent;

    @TableField("THRESHOLD_VALUE")
    @ApiModelProperty("阈值")
    private BigDecimal thresholdValue;

    @TableField("ACTUAL_VALUE")
    @ApiModelProperty("实际值")
    private BigDecimal actualValue;

    @TableField("ALERT_TIME")
    @ApiModelProperty("预警时间")
    private Date alertTime;

    @TableField("ALERT_STATUS")
    @ApiModelProperty("预警状态(PENDING-待处理/HANDLING-处理中/RESOLVED-已解决/IGNORED-已忽略)")
    private String alertStatus;

    @TableField("HANDLE_TIME")
    @ApiModelProperty("处理时间")
    private Date handleTime;

    @TableField("HANDLE_BY")
    @ApiModelProperty("处理人")
    private String handleBy;

    @TableField("HANDLE_RESULT")
    @ApiModelProperty("处理结果")
    private String handleResult;

    @TableField("RESOLVE_TIME")
    @ApiModelProperty("解决时间")
    private Date resolveTime;

    @TableField("RESOLVE_BY")
    @ApiModelProperty("解决人")
    private String resolveBy;

    @TableField("RESOLVE_REMARK")
    @ApiModelProperty("解决备注")
    private String resolveRemark;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("UPDATE_TIME")
    private Date updateTime;


    // 以下方法由Lombok生成,手动添加以解决编译问题
    public String getAlertId() { return alertId; }
    public void setAlertId(String alertId) { this.alertId = alertId; }
    public String getAlertNo() { return alertNo; }
    public void setAlertNo(String alertNo) { this.alertNo = alertNo; }
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public String getAlertLevel() { return alertLevel; }
    public void setAlertLevel(String alertLevel) { this.alertLevel = alertLevel; }
    public String getPoolId() { return poolId; }
    public void setPoolId(String poolId) { this.poolId = poolId; }
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getAccountNo() { return accountNo; }
    public void setAccountNo(String accountNo) { this.accountNo = accountNo; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getAlertTitle() { return alertTitle; }
    public void setAlertTitle(String alertTitle) { this.alertTitle = alertTitle; }
    public String getAlertContent() { return alertContent; }
    public void setAlertContent(String alertContent) { this.alertContent = alertContent; }
    public BigDecimal getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(BigDecimal thresholdValue) { this.thresholdValue = thresholdValue; }
    public BigDecimal getActualValue() { return actualValue; }
    public void setActualValue(BigDecimal actualValue) { this.actualValue = actualValue; }
    public Date getAlertTime() { return alertTime; }
    public void setAlertTime(Date alertTime) { this.alertTime = alertTime; }
    public String getAlertStatus() { return alertStatus; }
    public void setAlertStatus(String alertStatus) { this.alertStatus = alertStatus; }
    public Date getHandleTime() { return handleTime; }
    public void setHandleTime(Date handleTime) { this.handleTime = handleTime; }
    public String getHandleBy() { return handleBy; }
    public void setHandleBy(String handleBy) { this.handleBy = handleBy; }
    public String getHandleResult() { return handleResult; }
    public void setHandleResult(String handleResult) { this.handleResult = handleResult; }
    public Date getResolveTime() { return resolveTime; }
    public void setResolveTime(Date resolveTime) { this.resolveTime = resolveTime; }
    public String getResolveBy() { return resolveBy; }
    public void setResolveBy(String resolveBy) { this.resolveBy = resolveBy; }
    public String getResolveRemark() { return resolveRemark; }
    public void setResolveRemark(String resolveRemark) { this.resolveRemark = resolveRemark; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
