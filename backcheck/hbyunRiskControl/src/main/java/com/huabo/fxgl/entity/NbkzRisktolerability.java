package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-09
 */
@TableName("TBL_NBKZ_RISKTOLERABILITY")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class NbkzRisktolerability implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 风险发现ID
     */
	@Schema(name="风险发现ID")
    private BigDecimal riskid;

    /**
     * ID
     */
	@Schema(name="主键ID")
    @TableId(type = IdType.INPUT)
    private BigDecimal toleid;

    /**
     * 序号
     */
	@Schema(name="序号")
    private String rtcode;

    /**
     * 基准线下边界
     */
	@Schema(name="基准线下边界")
    private BigDecimal lowerborder;

    /**
     * 基准线上边界
     */
	@Schema(name="基准线上边界")
    private BigDecimal upperborder;

    /**
     * 颜色块编码
     */
	@Schema(name="颜色块编码")
    private String colorstring;

    /**
     * 描述
     */
	@Schema(name="描述")
    private String description;

    /**
     * 备注
     */
	@Schema(name="备注")
    private String memo;

    @TableField(exist = false)
    private NbkzRisk nbkzRisk;

    public NbkzRisk getNbkzRisk() {
        return nbkzRisk;
    }

    public void setNbkzRisk(NbkzRisk nbkzRisk) {
        this.nbkzRisk = nbkzRisk;
    }

    public BigDecimal getRiskid() {
        return riskid;
    }

    public void setRiskid(BigDecimal riskid) {
        this.riskid = riskid;
    }
    public BigDecimal getToleid() {
        return toleid;
    }

    public void setToleid(BigDecimal toleid) {
        this.toleid = toleid;
    }
    public String getRtcode() {
        return rtcode;
    }

    public void setRtcode(String rtcode) {
        this.rtcode = rtcode;
    }
    public BigDecimal getLowerborder() {
        return lowerborder;
    }

    public void setLowerborder(BigDecimal lowerborder) {
        this.lowerborder = lowerborder;
    }
    public BigDecimal getUpperborder() {
        return upperborder;
    }

    public void setUpperborder(BigDecimal upperborder) {
        this.upperborder = upperborder;
    }
    public String getColorstring() {
        return colorstring;
    }

    public void setColorstring(String colorstring) {
        this.colorstring = colorstring;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "NbkzRisktolerability{" +
            "riskid=" + riskid +
            ", toleid=" + toleid +
            ", rtcode=" + rtcode +
            ", lowerborder=" + lowerborder +
            ", upperborder=" + upperborder +
            ", colorstring=" + colorstring +
            ", description=" + description +
            ", memo=" + memo +
        "}";
    }
}
