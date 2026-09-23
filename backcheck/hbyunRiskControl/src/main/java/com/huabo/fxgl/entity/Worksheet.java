package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
@TableName("TBL_WORKSHEET")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class Worksheet implements Serializable {

    private static final long serialVersionUID = 1L;

    public final static Integer  YES_ZG=1;

    /**
     * ID
     */
    @Schema(name="主键ID")
    @TableId(type = IdType.INPUT)
    private BigDecimal worksheetid;

	@Schema
    @TableField(exist = false)
    private Set attachments = new HashSet(0);

    public Set getAttachments() {
        return attachments;
    }

    public void setAttachments(Set attachments) {
        this.attachments = attachments;
    }

    /**
     * 底稿编号
     */
	@Schema(name="底稿编号")
    private String worksheetnumber;

    /**
     * 审计对象
     */
	@Schema(name="审计对象")
    private String auditedorg;

    /**
     * 审计目标
     */
	@Schema(name="审计目标")
    private String audittarget;

    /**
     * 审计事项描述
     */
	@Schema(name="审计事项描述")
    private String auditdescription;

    /**
     * 审计过程
     */
	@Schema(name="审计过程")
    private String auditprocess;

    /**
     * 审计判断
     */
	@Schema(name="审计判断")
    private String auditjudge;

	@Schema
    private String checkopinin;

    /**
     * 创建人
     */
	@Schema(name="创建人")
    private String recorder;

    /**
     * 创建时间
     */
	@Schema(name="创建时间")
    private LocalDateTime recordingdate;

    /**
     * 备注
     */
	@Schema(name="备注")
    private String memo;

	@Schema
    private BigDecimal audfinid;

	@Schema
    private BigDecimal problemid;

    /**
     * 底稿名称
     */
	@Schema(name="底稿名称")
    private String worksheetname;

    /**
     * 所属模块
     */
	@Schema(name="所属模块")
    private String worksheetbysystem;

    /**
     * 审计对象ID
     */
	@Schema(name="审计对象ID")
    private BigDecimal orgid;

    /**
     * 创建人ID
     */
	@Schema(name="创建人ID")
    private BigDecimal userid;

	@Schema
    private BigDecimal rectification;

    public BigDecimal getWorksheetid() {
        return worksheetid;
    }

    public void setWorksheetid(BigDecimal worksheetid) {
        this.worksheetid = worksheetid;
    }
    public String getWorksheetnumber() {
        return worksheetnumber;
    }

    public void setWorksheetnumber(String worksheetnumber) {
        this.worksheetnumber = worksheetnumber;
    }
    public String getAuditedorg() {
        return auditedorg;
    }

    public void setAuditedorg(String auditedorg) {
        this.auditedorg = auditedorg;
    }
    public String getAudittarget() {
        return audittarget;
    }

    public void setAudittarget(String audittarget) {
        this.audittarget = audittarget;
    }
    public String getAuditdescription() {
        return auditdescription;
    }

    public void setAuditdescription(String auditdescription) {
        this.auditdescription = auditdescription;
    }
    public String getAuditprocess() {
        return auditprocess;
    }

    public void setAuditprocess(String auditprocess) {
        this.auditprocess = auditprocess;
    }
    public String getAuditjudge() {
        return auditjudge;
    }

    public void setAuditjudge(String auditjudge) {
        this.auditjudge = auditjudge;
    }
    public String getCheckopinin() {
        return checkopinin;
    }

    public void setCheckopinin(String checkopinin) {
        this.checkopinin = checkopinin;
    }
    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }
    public LocalDateTime getRecordingdate() {
        return recordingdate;
    }

    public void setRecordingdate(LocalDateTime recordingdate) {
        this.recordingdate = recordingdate;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public BigDecimal getAudfinid() {
        return audfinid;
    }

    public void setAudfinid(BigDecimal audfinid) {
        this.audfinid = audfinid;
    }
    public BigDecimal getProblemid() {
        return problemid;
    }

    public void setProblemid(BigDecimal problemid) {
        this.problemid = problemid;
    }
    public String getWorksheetname() {
        return worksheetname;
    }

    public void setWorksheetname(String worksheetname) {
        this.worksheetname = worksheetname;
    }
    public String getWorksheetbysystem() {
        return worksheetbysystem;
    }

    public void setWorksheetbysystem(String worksheetbysystem) {
        this.worksheetbysystem = worksheetbysystem;
    }
    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }
    public BigDecimal getUserid() {
        return userid;
    }

    public void setUserid(BigDecimal userid) {
        this.userid = userid;
    }
    public BigDecimal getRectification() {
        return rectification;
    }

    public void setRectification(BigDecimal rectification) {
        this.rectification = rectification;
    }

    @Override
    public String toString() {
        return "Worksheet{" +
            "worksheetid=" + worksheetid +
            ", worksheetnumber=" + worksheetnumber +
            ", auditedorg=" + auditedorg +
            ", audittarget=" + audittarget +
            ", auditdescription=" + auditdescription +
            ", auditprocess=" + auditprocess +
            ", auditjudge=" + auditjudge +
            ", checkopinin=" + checkopinin +
            ", recorder=" + recorder +
            ", recordingdate=" + recordingdate +
            ", memo=" + memo +
            ", audfinid=" + audfinid +
            ", problemid=" + problemid +
            ", worksheetname=" + worksheetname +
            ", worksheetbysystem=" + worksheetbysystem +
            ", orgid=" + orgid +
            ", userid=" + userid +
            ", rectification=" + rectification +
        "}";
    }
}
