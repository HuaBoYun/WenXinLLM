package com.huabo.monitor.entity;

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
 * @since 2022-09-13
 */
@TableName("TBL_RISK_ATT_WORD")
@Schema(name="TblRiskAttWord对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblRiskAttWord implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.INPUT)
    private BigDecimal attid;

    private String orgid;

    private String riskid;

    private String filename;

    private String filesize;

    private String filepath;

    private String flowid;

    private String assid;

    public BigDecimal getAttid() {
        return attid;
    }

    public void setAttid(BigDecimal attid) {
        this.attid = attid;
    }
    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }
    public String getRiskid() {
        return riskid;
    }

    public void setRiskid(String riskid) {
        this.riskid = riskid;
    }
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }
    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
    public String getFlowid() {
        return flowid;
    }

    public void setFlowid(String flowid) {
        this.flowid = flowid;
    }
    public String getAssid() {
        return assid;
    }

    public void setAssid(String assid) {
        this.assid = assid;
    }

    @Override
    public String toString() {
        return "TblRiskAttWord{" +
            "attid=" + attid +
            ", orgid=" + orgid +
            ", riskid=" + riskid +
            ", filename=" + filename +
            ", filesize=" + filesize +
            ", filepath=" + filepath +
            ", flowid=" + flowid +
            ", assid=" + assid +
        "}";
    }
}
