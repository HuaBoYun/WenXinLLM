package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hbfk.entity.TblAttachment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_RISK_COPING")
@Schema(name="RiskCopingMySql")
public class RiskCopingMySql {

    public static final String PROCESS_NAMW = "FX_FXYD";
    public final static Integer STATE_SP = 1;//审批中
    public final static Integer STATE_TZ = 2;//调整
    public final static Integer STATE_WC = 3;//已完成
    @TableId("RISKCOPINGID")
    private BigDecimal riskCopingId;
    @TableField("COPINGPLOT")
    private String copingPlot;
    @TableField("COPINGSTATUS")
    private String copingStatus;
    @TableField("COPINGHEAD")
    private String copingHead;
    @TableField("COPINGDES")
    private String copingDes;
    @TableField("RISKID")
    private TblRiskMySql risk;
    @TableField("COPINGSOURCE")
    private String copingSource;
    @TableField("RISKHOPEVALUE")
    private Integer riskHopeValue;
    @TableField("YDDES")
    private String yddes;
    @TableField("REVIEWER")
    private String reviewer;
    @TableField("REVIEWER2")
    private String reviewer2;
    @TableField("STATUS")
    private Integer status;  //1.审核中 2.调整  3.复核通过 4.复核完成

    public String getYddes() {
        return yddes;
    }

    public void setYddes(String yddes) {
        this.yddes = yddes;
    }

    //private Set<TblControlmatrix> tblControlmatrixs = new HashSet<TblControlmatrix>();
    private Set<TblAttachment> tblAttachments = new HashSet<TblAttachment>();
    private Set<TblAttachment> tblAttachmentZJs = new HashSet<TblAttachment>();

    public BigDecimal getRiskCopingId() {
        return riskCopingId;
    }

    public void setRiskCopingId(BigDecimal riskCopingId) {
        this.riskCopingId = riskCopingId;
    }

    public String getCopingPlot() {
        return copingPlot;
    }

    public void setCopingPlot(String copingPlot) {
        this.copingPlot = copingPlot;
    }

    public String getCopingHead() {
        return copingHead;
    }

    public void setCopingHead(String copingHead) {
        this.copingHead = copingHead;
    }

    public String getCopingDes() {
        return copingDes;
    }

    public void setCopingDes(String copingDes) {
        this.copingDes = copingDes;
    }

    public TblRiskMySql getRisk() {
        return risk;
    }

    public void setRisk(TblRiskMySql risk) {
        this.risk = risk;
    }

    public String getCopingSource() {
        return copingSource;
    }

    public void setCopingSource(String copingSource) {
        this.copingSource = copingSource;
    }

    public String getCopingStatus() {
        return copingStatus;
    }

    public void setCopingStatus(String copingStatus) {
        this.copingStatus = copingStatus;
    }

    public Integer getRiskHopeValue() {
        return riskHopeValue;
    }

    public void setRiskHopeValue(Integer riskHopeValue) {
        this.riskHopeValue = riskHopeValue;
    }

    //	public Set<TblControlmatrix> getTblControlmatrixs() {
//		return tblControlmatrixs;
//	}
//	public void setTblControlmatrixs(Set<TblControlmatrix> tblControlmatrixs) {
//		this.tblControlmatrixs = tblControlmatrixs;
//	}
    public Set<TblAttachment> getTblAttachments() {
        return tblAttachments;
    }

    public void setTblAttachments(Set<TblAttachment> tblAttachments) {
        this.tblAttachments = tblAttachments;
    }

    public Set<TblAttachment> getTblAttachmentZJs() {
        return tblAttachmentZJs;
    }

    public void setTblAttachmentZJs(Set<TblAttachment> tblAttachmentZJs) {
        this.tblAttachmentZJs = tblAttachmentZJs;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getReviewer2() {
        return reviewer2;
    }

    public void setReviewer2(String reviewer2) {
        this.reviewer2 = reviewer2;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
