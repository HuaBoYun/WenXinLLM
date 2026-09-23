package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 异常处理实体类
 * @author Claude
 * @date 2026-01-20
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_EXCEPTION_HANDLING")
@ApiModel(value = "TblExceptionHandling", description = "异常处理实体")
public class TblExceptionHandling implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "EXCEPTION_ID", type = IdType.ASSIGN_UUID)
    @ApiModelProperty("异常ID")
    private String exceptionId;

    @TableField("EXCEPTION_NO")
    @ApiModelProperty("异常编号")
    private String exceptionNo;

    @TableField("EXCEPTION_TYPE")
    @ApiModelProperty("异常类型(TRANSFER_FAILED-转账失败/TIMEOUT-超时/DATA_ERROR-数据错误/SYSTEM_ERROR-系统错误)")
    private String exceptionType;

    @TableField("EXCEPTION_LEVEL")
    @ApiModelProperty("异常级别(NORMAL-普通/IMPORTANT-重要/URGENT-紧急)")
    private String exceptionLevel;

    @TableField("SOURCE_TYPE")
    @ApiModelProperty("来源类型(COLLECTION-归集/ALLOCATION-下拨/LOAN-借贷)")
    private String sourceType;

    @TableField("SOURCE_ID")
    @ApiModelProperty("来源ID")
    private String sourceId;

    @TableField("SOURCE_NO")
    @ApiModelProperty("来源编号")
    private String sourceNo;

    @TableField("POOL_ID")
    @ApiModelProperty("资金池ID")
    private String poolId;

    @TableField("COMPANY_ID")
    @ApiModelProperty("公司ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    @ApiModelProperty("公司名称")
    private String companyName;

    @TableField("EXCEPTION_TITLE")
    @ApiModelProperty("异常标题")
    private String exceptionTitle;

    @TableField("EXCEPTION_DESC")
    @ApiModelProperty("异常描述")
    private String exceptionDesc;

    @TableField("EXCEPTION_TIME")
    @ApiModelProperty("异常时间")
    private Date exceptionTime;

    @TableField("EXCEPTION_STATUS")
    @ApiModelProperty("异常状态(PENDING-待处理/HANDLING-处理中/RESOLVED-已解决/CLOSED-已关闭)")
    private String exceptionStatus;

    @TableField("HANDLE_METHOD")
    @ApiModelProperty("处理方式(RETRY-重试/MANUAL-人工处理/IGNORE-忽略/ROLLBACK-回滚)")
    private String handleMethod;

    @TableField("HANDLE_TIME")
    @ApiModelProperty("处理时间")
    private Date handleTime;

    @TableField("HANDLE_BY")
    @ApiModelProperty("处理人")
    private String handleBy;

    @TableField("HANDLE_RESULT")
    @ApiModelProperty("处理结果")
    private String handleResult;

    @TableField("RETRY_COUNT")
    @ApiModelProperty("重试次数")
    private Integer retryCount;

    @TableField("MAX_RETRY")
    @ApiModelProperty("最大重试次数")
    private Integer maxRetry;

    @TableField("NEXT_RETRY_TIME")
    @ApiModelProperty("下次重试时间")
    private Date nextRetryTime;

    @TableField("REMARK")
    @ApiModelProperty("备注")
    private String remark;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("DEL_FLAG")
    private String delFlag;


    // 以下方法由Lombok生成,手动添加以解决编译问题

    public String getExceptionId() { return exceptionId; }
    public void setExceptionId(String exceptionId) { this.exceptionId = exceptionId; }

    public String getExceptionNo() { return exceptionNo; }
    public void setExceptionNo(String exceptionNo) { this.exceptionNo = exceptionNo; }

    public String getExceptionType() { return exceptionType; }
    public void setExceptionType(String exceptionType) { this.exceptionType = exceptionType; }

    public String getExceptionLevel() { return exceptionLevel; }
    public void setExceptionLevel(String exceptionLevel) { this.exceptionLevel = exceptionLevel; }

    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }

    public String getSourceId() { return sourceId; }
    public void setSourceId(String sourceId) { this.sourceId = sourceId; }

    public String getSourceNo() { return sourceNo; }
    public void setSourceNo(String sourceNo) { this.sourceNo = sourceNo; }

    public String getPoolId() { return poolId; }
    public void setPoolId(String poolId) { this.poolId = poolId; }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getExceptionTitle() { return exceptionTitle; }
    public void setExceptionTitle(String exceptionTitle) { this.exceptionTitle = exceptionTitle; }

    public String getExceptionDesc() { return exceptionDesc; }
    public void setExceptionDesc(String exceptionDesc) { this.exceptionDesc = exceptionDesc; }

    public Date getExceptionTime() { return exceptionTime; }
    public void setExceptionTime(Date exceptionTime) { this.exceptionTime = exceptionTime; }

    public String getExceptionStatus() { return exceptionStatus; }
    public void setExceptionStatus(String exceptionStatus) { this.exceptionStatus = exceptionStatus; }

    public String getHandleMethod() { return handleMethod; }
    public void setHandleMethod(String handleMethod) { this.handleMethod = handleMethod; }

    public Date getHandleTime() { return handleTime; }
    public void setHandleTime(Date handleTime) { this.handleTime = handleTime; }

    public String getHandleBy() { return handleBy; }
    public void setHandleBy(String handleBy) { this.handleBy = handleBy; }

    public String getHandleResult() { return handleResult; }
    public void setHandleResult(String handleResult) { this.handleResult = handleResult; }

    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }

    public Integer getMaxRetry() { return maxRetry; }
    public void setMaxRetry(Integer maxRetry) { this.maxRetry = maxRetry; }

    public Date getNextRetryTime() { return nextRetryTime; }
    public void setNextRetryTime(Date nextRetryTime) { this.nextRetryTime = nextRetryTime; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }
}
