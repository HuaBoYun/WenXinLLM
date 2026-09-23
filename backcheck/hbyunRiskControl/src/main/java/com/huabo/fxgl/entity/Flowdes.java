package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-08
 */
@Data
@ToString
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
@TableName("TBL_FLOWDES")
public class Flowdes implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private BigDecimal flowdesid;
    //程序编号
    private String procedurenumber;
    //程序名称
    private String procedurename;
    //部门主管
    private String departincharge;
    //程序
    private String proceduredes;

    private String memo;

    @TableField()
    private BigDecimal flowid;

    private String attachment;

    private BigDecimal position;

    @TableField(value = "flowid", property = "flow.flowid", insertStrategy = FieldStrategy.NOT_NULL)
    private Flow flow;

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public BigDecimal getFlowdesid() {
        return flowdesid;
    }

    public void setFlowdesid(BigDecimal flowdesid) {
        this.flowdesid = flowdesid;
    }
    public String getProcedurenumber() {
        return procedurenumber;
    }

    public void setProcedurenumber(String procedurenumber) {
        this.procedurenumber = procedurenumber;
    }
    public String getProcedurename() {
        return procedurename;
    }

    public void setProcedurename(String procedurename) {
        this.procedurename = procedurename;
    }
    public String getDepartincharge() {
        return departincharge;
    }

    public void setDepartincharge(String departincharge) {
        this.departincharge = departincharge;
    }
    public String getProceduredes() {
        return proceduredes;
    }

    public void setProceduredes(String proceduredes) {
        this.proceduredes = proceduredes;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public BigDecimal getFlowid() {
        return flowid;
    }

    public void setFlowid(BigDecimal flowid) {
        this.flowid = flowid;
    }
    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
    public BigDecimal getPosition() {
        return position;
    }

    public void setPosition(BigDecimal position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Flowdes{" +
            "flowdesid=" + flowdesid +
            ", procedurenumber=" + procedurenumber +
            ", procedurename=" + procedurename +
            ", departincharge=" + departincharge +
            ", proceduredes=" + proceduredes +
            ", memo=" + memo +
            ", flowid=" + flowid +
            ", attachment=" + attachment +
            ", position=" + position +
        "}";
    }
}
