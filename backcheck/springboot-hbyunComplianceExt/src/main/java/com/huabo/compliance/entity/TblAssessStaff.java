package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@TableName("TBL_ASSESS_STAFF")
@Schema(name="TblAssessStaff对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblAssessStaff implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.INPUT)
    private BigDecimal assstaffid;


    private BigDecimal assweight;

    private String memo;

    //TBL_STAFF 外键
    private BigDecimal staffid;

    private BigDecimal score;

    private String reason;

    private LocalDateTime assdatetime;

    //TBL_ASSESSMARK 外键
    private BigDecimal assmarkid;

    private BigDecimal status = new BigDecimal(0);

    //TBL_ORGANIZATION 外键
    private BigDecimal orgid;

    //TBL_ATTACHMENT
    private BigDecimal attid;

    private String examination;

    public BigDecimal getAssstaffid() {
        return assstaffid;
    }

    public void setAssstaffid(BigDecimal assstaffid) {
        this.assstaffid = assstaffid;
    }
    public BigDecimal getAssweight() {
        return assweight;
    }

    public void setAssweight(BigDecimal assweight) {
        this.assweight = assweight;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }
    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    public LocalDateTime getAssdatetime() {
        return assdatetime;
    }

    public void setAssdatetime(LocalDateTime assdatetime) {
        this.assdatetime = assdatetime;
    }
    public BigDecimal getAssmarkid() {
        return assmarkid;
    }

    public void setAssmarkid(BigDecimal assmarkid) {
        this.assmarkid = assmarkid;
    }
    public BigDecimal getStatus() {
        return status;
    }

    public void setStatus(BigDecimal status) {
        this.status = status;
    }
    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }
    public BigDecimal getAttid() {
        return attid;
    }

    public void setAttid(BigDecimal attid) {
        this.attid = attid;
    }
    public String getExamination() {
        return examination;
    }

    public void setExamination(String examination) {
        this.examination = examination;
    }

    @Override
    public String toString() {
        return "TblAssessStaff{" +
            "assstaffid=" + assstaffid +
            ", assweight=" + assweight +
            ", memo=" + memo +
            ", staffid=" + staffid +
            ", score=" + score +
            ", reason=" + reason +
            ", assdatetime=" + assdatetime +
            ", assmarkid=" + assmarkid +
            ", status=" + status +
            ", orgid=" + orgid +
            ", attid=" + attid +
            ", examination=" + examination +
        "}";
    }
}
