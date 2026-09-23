package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-04
 */
@TableName("TBL_FLOW_BUSSINESS")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class FlowBussiness implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
	@Schema(name="主键ID")
    private Long bussinessid;

	@Schema
    private String bussinessnumber;

	@Schema
    private String bussinessname;

	@Schema(name="业务名称")
    private String bussinessdes;

	@Schema
    private Long flowid;

    public Long getBussinessid() {
        return bussinessid;
    }

    public void setBussinessid(Long bussinessid) {
        this.bussinessid = bussinessid;
    }
    public String getBussinessnumber() {
        return bussinessnumber;
    }

    public void setBussinessnumber(String bussinessnumber) {
        this.bussinessnumber = bussinessnumber;
    }
    public String getBussinessname() {
        return bussinessname;
    }

    public void setBussinessname(String bussinessname) {
        this.bussinessname = bussinessname;
    }
    public String getBussinessdes() {
        return bussinessdes;
    }

    public void setBussinessdes(String bussinessdes) {
        this.bussinessdes = bussinessdes;
    }
    public Long getFlowid() {
        return flowid;
    }

    public void setFlowid(Long flowid) {
        this.flowid = flowid;
    }

    @Override
    public String toString() {
        return "FlowBussiness{" +
            "bussinessid=" + bussinessid +
            ", bussinessnumber=" + bussinessnumber +
            ", bussinessname=" + bussinessname +
            ", bussinessdes=" + bussinessdes +
            ", flowid=" + flowid +
        "}";
    }
}
