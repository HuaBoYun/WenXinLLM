package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-04
 */
@Data
@TableName("TBL_CONTROLMATRIX")
@Schema(name="TBL_CONTROLMATRIX", description="控制措施")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class Controlmatrix extends FlexibleFieldEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
	@Schema(name="主键ID")
    private BigDecimal conmatid;

    @Schema(name="风险基本信息主键")
    @TableField(exist = false)
    private BigDecimal riskid;

    @Schema(name="风险应对id")
    private BigDecimal riskcopingid;

	@Schema(name="流程名称")
    private String flowname;

	@Schema(name="无意义")
    private String controltarget;

	@Schema(name="风险控制点编号")
    private String controlnumber;

	@Schema
    private String controlname;

	@Schema(name="控制责任人  关联tbl_staff realname")
    private String controlmanager;

	@Schema(name="控制频率")
    private String controlfrequency;

	@Schema(name="控制类型  1-预防性控制  2-发现性控制  3-纠正性控制")
    private String controltype;

	@Schema(name="控制手段    1-手工，2-自动  3-依赖手工的自动化")
    private String controlmethod;

	@Schema(name="具体责任部门")
    private String  responsibledep;
	
	@Schema(name="具体责任部门Name")
    private String  responsibledepname;
	
	@Schema(name="本单位部门责任领导")
    private String ourdepleader;
	
	@Schema(name="具体责任部门Name")
    private String  ourdepleadername;
	
	@Schema(name="无意义")
    private String memo;

	@Schema(name="风险控制点描述")
    private String controldes;

	@Schema(name="无意义")
    private String insidecontroltarget;

	@Schema(name="是否关键控制  1-是  2-否")
    private String keycontrol;

	@Schema(name="控制是否有效  1-是  2-否")
    private String effective;

	@Schema(name="是否进行控制测试  1-是   2-否")
    private String controltest;

	@Schema(name="财务报表认定  1-存在与发生   2-完整性  3-权利与义务  4-估价与分摊  5-表达与披露")
    private String financialreportidentify;

	@Schema
    private String relateddepart;

	@Schema
    private String controldocument;

	@Schema
    private String flowcode;

	@Schema(name="流程分类  l来源tbl_flow  flowname")
    private String toplevelflowcat;

	@Schema(name="版本")
    private String version;

	@Schema
    private String createdtime;

	@Schema
    private String lastmodifiedtime;

	@Schema
    private String subsystem;

	@Schema(name="控制措施")
    private String conkzcs;

	@Schema
    private BigDecimal versiontype;

	@Schema(name="扩展字段：责任部门、流程节点、重点关注、责任确认")
	private String extjson;

	
	@Schema(name="子集list集合")
    @TableField(exist = false)
    private List<TblControlEntries> entries;
	
	
	public String getExtJson() {
		return extjson;
	}

	public void setExtJson(String extjson) {
		this.extjson = extjson;
	}

	public BigDecimal getRiskcopingid() {
        return riskcopingid;
    }

    public void setRiskcopingid(BigDecimal riskcopingid) {
        this.riskcopingid = riskcopingid;
    }

    public BigDecimal getRiskid() {
        return riskid;
    }

    public void setRiskid(BigDecimal riskid) {
        this.riskid = riskid;
    }

    public BigDecimal getConmatid() {
        return conmatid;
    }

    public void setConmatid(BigDecimal conmatid) {
        this.conmatid = conmatid;
    }
    public String getFlowname() {
        return flowname;
    }

    public void setFlowname(String flowname) {
        this.flowname = flowname;
    }
    public String getControltarget() {
        return controltarget;
    }

    public void setControltarget(String controltarget) {
        this.controltarget = controltarget;
    }
    public String getControlnumber() {
        return controlnumber;
    }

    public void setControlnumber(String controlnumber) {
        this.controlnumber = controlnumber;
    }
    public String getControlname() {
        return controlname;
    }

    public void setControlname(String controlname) {
        this.controlname = controlname;
    }
    public String getControlmanager() {
        return controlmanager;
    }

    public void setControlmanager(String controlmanager) {
        this.controlmanager = controlmanager;
    }
    public String getControlfrequency() {
        return controlfrequency;
    }

    public void setControlfrequency(String controlfrequency) {
        this.controlfrequency = controlfrequency;
    }
    public String getControltype() {
        return controltype;
    }

    public void setControltype(String controltype) {
        this.controltype = controltype;
    }
    public String getControlmethod() {
        return controlmethod;
    }

    public void setControlmethod(String controlmethod) {
        this.controlmethod = controlmethod;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getControldes() {
        return controldes;
    }

    public void setControldes(String controldes) {
        this.controldes = controldes;
    }
    public String getInsidecontroltarget() {
        return insidecontroltarget;
    }

    public void setInsidecontroltarget(String insidecontroltarget) {
        this.insidecontroltarget = insidecontroltarget;
    }
    public String getKeycontrol() {
        return keycontrol;
    }

    public void setKeycontrol(String keycontrol) {
        this.keycontrol = keycontrol;
    }
    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
    }
    public String getControltest() {
        return controltest;
    }

    public void setControltest(String controltest) {
        this.controltest = controltest;
    }
    public String getFinancialreportidentify() {
        return financialreportidentify;
    }

    public void setFinancialreportidentify(String financialreportidentify) {
        this.financialreportidentify = financialreportidentify;
    }
    public String getRelateddepart() {
        return relateddepart;
    }

    public void setRelateddepart(String relateddepart) {
        this.relateddepart = relateddepart;
    }
    public String getControldocument() {
        return controldocument;
    }

    public void setControldocument(String controldocument) {
        this.controldocument = controldocument;
    }
    public String getFlowcode() {
        return flowcode;
    }

    public void setFlowcode(String flowcode) {
        this.flowcode = flowcode;
    }
    public String getToplevelflowcat() {
        return toplevelflowcat;
    }

    public void setToplevelflowcat(String toplevelflowcat) {
        this.toplevelflowcat = toplevelflowcat;
    }
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }
    public String getLastmodifiedtime() {
        return lastmodifiedtime;
    }

    public void setLastmodifiedtime(String lastmodifiedtime) {
        this.lastmodifiedtime = lastmodifiedtime;
    }
    public String getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }
    public String getConkzcs() {
        return conkzcs;
    }

    public void setConkzcs(String conkzcs) {
        this.conkzcs = conkzcs;
    }
    public BigDecimal getVersiontype() {
        return versiontype;
    }

    public void setVersiontype(BigDecimal versiontype) {
        this.versiontype = versiontype;
    }

    @Override
    public String toString() {
        return "Controlmatrix{" +
            "conmatid=" + conmatid +
            ", flowname=" + flowname +
            ", controltarget=" + controltarget +
            ", controlnumber=" + controlnumber +
            ", controlname=" + controlname +
            ", controlmanager=" + controlmanager +
            ", controlfrequency=" + controlfrequency +
            ", controltype=" + controltype +
            ", controlmethod=" + controlmethod +
            ", memo=" + memo +
            ", controldes=" + controldes +
            ", insidecontroltarget=" + insidecontroltarget +
            ", keycontrol=" + keycontrol +
            ", effective=" + effective +
            ", controltest=" + controltest +
            ", financialreportidentify=" + financialreportidentify +
            ", relateddepart=" + relateddepart +
            ", controldocument=" + controldocument +
            ", flowcode=" + flowcode +
            ", toplevelflowcat=" + toplevelflowcat +
            ", version=" + version +
            ", createdtime=" + createdtime +
            ", lastmodifiedtime=" + lastmodifiedtime +
            ", subsystem=" + subsystem +
            ", conkzcs=" + conkzcs +
            ", versiontype=" + versiontype +
        "}";
    }
}
