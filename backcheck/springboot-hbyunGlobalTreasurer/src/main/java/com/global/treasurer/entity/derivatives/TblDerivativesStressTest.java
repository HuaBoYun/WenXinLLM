package com.global.treasurer.entity.derivatives;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("TBL_DERIVATIVES_STRESS_TEST")
public class TblDerivativesStressTest implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "TEST_ID", type = IdType.AUTO)
    private Long testId;

    @TableField("SCENARIO_NAME")
    private String scenarioName;

    @TableField("SCENARIO_DESC")
    private String scenarioDesc;

    @TableField("PORTFOLIO_VALUE")
    private BigDecimal portfolioValue;

    @TableField("STRESS_VALUE")
    private BigDecimal stressValue;

    @TableField("LOSS")
    private BigDecimal loss;

    @TableField("LOSS_RATIO")
    private BigDecimal lossRatio;

    @TableField("SEVERITY")
    private String severity;

    @TableField("STATUS")
    private String status;

    @TableField("ORG_ID")
    private Long orgId;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("DEL_FLAG")
    private String delFlag;

    public Long getTestId() { return testId; }
    public void setTestId(Long testId) { this.testId = testId; }
    public String getScenarioName() { return scenarioName; }
    public void setScenarioName(String scenarioName) { this.scenarioName = scenarioName; }
    public String getScenarioDesc() { return scenarioDesc; }
    public void setScenarioDesc(String scenarioDesc) { this.scenarioDesc = scenarioDesc; }
    public BigDecimal getPortfolioValue() { return portfolioValue; }
    public void setPortfolioValue(BigDecimal portfolioValue) { this.portfolioValue = portfolioValue; }
    public BigDecimal getStressValue() { return stressValue; }
    public void setStressValue(BigDecimal stressValue) { this.stressValue = stressValue; }
    public BigDecimal getLoss() { return loss; }
    public void setLoss(BigDecimal loss) { this.loss = loss; }
    public BigDecimal getLossRatio() { return lossRatio; }
    public void setLossRatio(BigDecimal lossRatio) { this.lossRatio = lossRatio; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }
}

