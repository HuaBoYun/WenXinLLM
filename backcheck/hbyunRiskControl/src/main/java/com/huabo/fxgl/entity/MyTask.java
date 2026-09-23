package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-17
 */
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
@TableName("TBL_MY_TASK")
public class MyTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private BigDecimal id;

    private String taskid;

    private String processdefinitionid;

    private String processinstanceid;

    private String usrid;

    private String fromid;

    private String fromname;

    private String approver;

    private String examination;

    private String processname;

    private String result;

    private String approvalrole;

    private LocalDateTime approvaldate;

    private String cirid;

    private String handle;

    private String analid;

    private String fxexam;

    private String kzjzexam;

    private String imgbasestr;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }
    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }
    public String getProcessdefinitionid() {
        return processdefinitionid;
    }

    public void setProcessdefinitionid(String processdefinitionid) {
        this.processdefinitionid = processdefinitionid;
    }
    public String getProcessinstanceid() {
        return processinstanceid;
    }

    public void setProcessinstanceid(String processinstanceid) {
        this.processinstanceid = processinstanceid;
    }
    public String getUsrid() {
        return usrid;
    }

    public void setUsrid(String usrid) {
        this.usrid = usrid;
    }
    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }
    public String getFromname() {
        return fromname;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname;
    }
    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }
    public String getExamination() {
        return examination;
    }

    public void setExamination(String examination) {
        this.examination = examination;
    }
    public String getProcessname() {
        return processname;
    }

    public void setProcessname(String processname) {
        this.processname = processname;
    }
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    public String getApprovalrole() {
        return approvalrole;
    }

    public void setApprovalrole(String approvalrole) {
        this.approvalrole = approvalrole;
    }
    public LocalDateTime getApprovaldate() {
        return approvaldate;
    }

    public void setApprovaldate(LocalDateTime approvaldate) {
        this.approvaldate = approvaldate;
    }
    public String getCirid() {
        return cirid;
    }

    public void setCirid(String cirid) {
        this.cirid = cirid;
    }
    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }
    public String getAnalid() {
        return analid;
    }

    public void setAnalid(String analid) {
        this.analid = analid;
    }
    public String getFxexam() {
        return fxexam;
    }

    public void setFxexam(String fxexam) {
        this.fxexam = fxexam;
    }
    public String getKzjzexam() {
        return kzjzexam;
    }

    public void setKzjzexam(String kzjzexam) {
        this.kzjzexam = kzjzexam;
    }
    public String getImgbasestr() {
        return imgbasestr;
    }

    public void setImgbasestr(String imgbasestr) {
        this.imgbasestr = imgbasestr;
    }

    @Override
    public String toString() {
        return "MyTask{" +
            "id=" + id +
            ", taskid=" + taskid +
            ", processdefinitionid=" + processdefinitionid +
            ", processinstanceid=" + processinstanceid +
            ", usrid=" + usrid +
            ", fromid=" + fromid +
            ", fromname=" + fromname +
            ", approver=" + approver +
            ", examination=" + examination +
            ", processname=" + processname +
            ", result=" + result +
            ", approvalrole=" + approvalrole +
            ", approvaldate=" + approvaldate +
            ", cirid=" + cirid +
            ", handle=" + handle +
            ", analid=" + analid +
            ", fxexam=" + fxexam +
            ", kzjzexam=" + kzjzexam +
            ", imgbasestr=" + imgbasestr +
        "}";
    }
}
