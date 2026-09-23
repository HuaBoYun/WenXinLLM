package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 *
 * </p>
 *
 * @author yhr
 * @since 2022-09-07
 */
@TableName("TBL_COM_EXT_TESTPLAN")
@Schema(name="TblTestplan对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblTestplan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="ID")
    @TableId(type = IdType.INPUT)
    private BigDecimal testplanid;

    @Schema(name="计划编号")
    private String plannumber;

    @Schema(name="计划名称")
    private String planname;

    @Schema(name="计划年度")
    private String planyear;

    @Schema(name="测试类型")
    private String testtype;

    private String planmadeorg;

    @Schema(name="计划制定部门")
    private String planmadedep;

    @Schema(name="计划开始时间")
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private LocalDateTime starttime;

    @Schema(name="计划结束时间")
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private LocalDateTime endtime;

    @Schema(name="负责人")
    private String planleader;

    @Schema(name="开展费用")
    private BigDecimal planfee;

    @Schema(name="投入人力")
    private String numberofpeople;

    @Schema(name="被测试机构")
    private String testedorgs;

    @Schema(name="备注")
    private String memo;

    @Schema(name="计划状态")
    private String planstatus;

    @Schema(name="计划制定部门id")
    private BigDecimal orgid;

    @Schema(name="创建人")
    private BigDecimal creatid;

    @Schema(name="负责人")
    private BigDecimal staffid;

    @Schema(name="测试模板")
    private BigDecimal testtemid;

    @Schema(name="退回状态")
    private BigDecimal returnstatus;

    public BigDecimal getTestplanid() {
        return testplanid;
    }

    public void setTestplanid(BigDecimal testplanid) {
        this.testplanid = testplanid;
    }
    public String getPlannumber() {
        return plannumber;
    }

    public void setPlannumber(String plannumber) {
        this.plannumber = plannumber;
    }
    public String getPlanname() {
        return planname;
    }

    public void setPlanname(String planname) {
        this.planname = planname;
    }
    public String getPlanyear() {
        return planyear;
    }

    public void setPlanyear(String planyear) {
        this.planyear = planyear;
    }
    public String getTesttype() {
        return testtype;
    }

    public void setTesttype(String testtype) {
        this.testtype = testtype;
    }
    public String getPlanmadeorg() {
        return planmadeorg;
    }

    public void setPlanmadeorg(String planmadeorg) {
        this.planmadeorg = planmadeorg;
    }
    public String getPlanmadedep() {
        return planmadedep;
    }

    public void setPlanmadedep(String planmadedep) {
        this.planmadedep = planmadedep;
    }
    public LocalDateTime getStarttime() {
        return starttime;
    }

    public void setStarttime(LocalDateTime starttime) {
        this.starttime = starttime;
    }
    public LocalDateTime getEndtime() {
        return endtime;
    }

    public void setEndtime(LocalDateTime endtime) {
        this.endtime = endtime;
    }
    public String getPlanleader() {
        return planleader;
    }

    public void setPlanleader(String planleader) {
        this.planleader = planleader;
    }
    public BigDecimal getPlanfee() {
        return planfee;
    }

    public void setPlanfee(BigDecimal planfee) {
        this.planfee = planfee;
    }
    public String getNumberofpeople() {
        return numberofpeople;
    }

    public void setNumberofpeople(String numberofpeople) {
        this.numberofpeople = numberofpeople;
    }
    public String getTestedorgs() {
        return testedorgs;
    }

    public void setTestedorgs(String testedorgs) {
        this.testedorgs = testedorgs;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getPlanstatus() {
        return planstatus;
    }

    public void setPlanstatus(String planstatus) {
        this.planstatus = planstatus;
    }
    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }
    public BigDecimal getCreatid() {
        return creatid;
    }

    public void setCreatid(BigDecimal creatid) {
        this.creatid = creatid;
    }
    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }
    public BigDecimal getTesttemid() {
        return testtemid;
    }

    public void setTesttemid(BigDecimal testtemid) {
        this.testtemid = testtemid;
    }
    public BigDecimal getReturnstatus() {
        return returnstatus;
    }

    public void setReturnstatus(BigDecimal returnstatus) {
        this.returnstatus = returnstatus;
    }

    @Override
    public String toString() {
        return "TblTestplan{" +
                "testplanid=" + testplanid +
                ", plannumber=" + plannumber +
                ", planname=" + planname +
                ", planyear=" + planyear +
                ", testtype=" + testtype +
                ", planmadeorg=" + planmadeorg +
                ", planmadedep=" + planmadedep +
                ", starttime=" + starttime +
                ", endtime=" + endtime +
                ", planleader=" + planleader +
                ", planfee=" + planfee +
                ", numberofpeople=" + numberofpeople +
                ", testedorgs=" + testedorgs +
                ", memo=" + memo +
                ", planstatus=" + planstatus +
                ", orgid=" + orgid +
                ", creatid=" + creatid +
                ", staffid=" + staffid +
                ", testtemid=" + testtemid +
                ", returnstatus=" + returnstatus +
                "}";
    }
}
