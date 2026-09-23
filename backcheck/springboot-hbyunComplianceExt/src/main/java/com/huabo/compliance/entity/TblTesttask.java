package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-09-08
 */
@TableName("TBL_COM_EXT_TESTTASK")
@Schema(name="TblTesttask对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblTesttask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="测试结果")
    private String testresult;

    @Schema(name="测试有效性")
    private String testpointvalidity;


    @TableId(type= IdType.INPUT)
    @Schema(name="ID")
    private BigDecimal testtaskid;

    @Schema(name="备注")
    private String memo;

    @Schema(name="状态")
    private String teststatus;

    @Schema(name="模板右侧id")
    private BigDecimal elementid;

    @Schema(name="计划ID")
    private BigDecimal planid;

    private BigDecimal completestaus;

    private BigDecimal attid;

    private String attname;

    private String procedures;

    private BigDecimal cpuserid;

    @Schema(name="退回状态")
    private BigDecimal returnstatus;

    @Schema(name="退回意见")
    private String proposal;

    public String getTestresult() {
        return testresult;
    }

    public void setTestresult(String testresult) {
        this.testresult = testresult;
    }
    public String getTestpointvalidity() {
        return testpointvalidity;
    }

    public void setTestpointvalidity(String testpointvalidity) {
        this.testpointvalidity = testpointvalidity;
    }
    public BigDecimal getTesttaskid() {
        return testtaskid;
    }

    public void setTesttaskid(BigDecimal testtaskid) {
        this.testtaskid = testtaskid;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getTeststatus() {
        return teststatus;
    }

    public void setTeststatus(String teststatus) {
        this.teststatus = teststatus;
    }
    public BigDecimal getElementid() {
        return elementid;
    }

    public void setElementid(BigDecimal elementid) {
        this.elementid = elementid;
    }
    public BigDecimal getPlanid() {
        return planid;
    }

    public void setPlanid(BigDecimal planid) {
        this.planid = planid;
    }
    public BigDecimal getCompletestaus() {
        return completestaus;
    }

    public void setCompletestaus(BigDecimal completestaus) {
        this.completestaus = completestaus;
    }
    public BigDecimal getAttid() {
        return attid;
    }

    public void setAttid(BigDecimal attid) {
        this.attid = attid;
    }
    public String getAttname() {
        return attname;
    }

    public void setAttname(String attname) {
        this.attname = attname;
    }
    public String getProcedures() {
        return procedures;
    }

    public void setProcedures(String procedures) {
        this.procedures = procedures;
    }
    public BigDecimal getCpuserid() {
        return cpuserid;
    }

    public void setCpuserid(BigDecimal cpuserid) {
        this.cpuserid = cpuserid;
    }
    public BigDecimal getReturnstatus() {
        return returnstatus;
    }

    public void setReturnstatus(BigDecimal returnstatus) {
        this.returnstatus = returnstatus;
    }
    public String getProposal() {
        return proposal;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    @Override
    public String toString() {
        return "TblTesttask{" +
            "testresult=" + testresult +
            ", testpointvalidity=" + testpointvalidity +
            ", testtaskid=" + testtaskid +
            ", memo=" + memo +
            ", teststatus=" + teststatus +
            ", elementid=" + elementid +
            ", planid=" + planid +
            ", completestaus=" + completestaus +
            ", attid=" + attid +
            ", attname=" + attname +
            ", procedures=" + procedures +
            ", cpuserid=" + cpuserid +
            ", returnstatus=" + returnstatus +
            ", proposal=" + proposal +
        "}";
    }
}
