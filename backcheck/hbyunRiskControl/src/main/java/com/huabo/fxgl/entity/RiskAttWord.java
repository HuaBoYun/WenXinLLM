package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
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
 * @since 2022-08-19
 */
@Data
@ToString
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
@TableName("TBL_RISK_ATT_WORD")
public class RiskAttWord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
	@Schema(name="ID")
    private BigDecimal attid;

	@Schema
    private String orgid;

	@Schema
    private String riskid;

	@Schema(name="文件名")
    private String filename;

	@Schema(name="文件大小")
    private String filesize;

	@Schema(name="文件路径")
    private String filepath;

	@Schema
    private String flowid;

	@Schema
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
        return "RiskAttWord{" +
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
