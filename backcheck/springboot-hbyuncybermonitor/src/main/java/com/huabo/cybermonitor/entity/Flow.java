package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author kangjx
 * @since 2022-08-11
 */
@TableName("TBL_FLOW")
public class Flow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程ID
     */
    @TableId
    private BigDecimal flowid;

    /**
     * 流程编号
     */
    private String flownumber;

    /**
     * 流程名称
     */
    private String flowname;

    /**
     * 公司ID
     */
    private String company;

    /**
     * 部门ID
     */
    private String departincharge;

    /**
     * 使用范围
     */
    private String flowrange;

    /**
     * 流程状态
     */
    private String flowstatus;

    /**
     * 备注
     */
    private String memo;

    /**
     * 版本
     */
    private BigDecimal version;

    private String flowchart;

    /**
     * 业务参与部门
     */
    private String departassist;

    /**
     * 录入人
     */
    private String editor;

    /**
     * 更新时间
     */
    private LocalDateTime updatetime;

    private String relatedrules;

    private String affectdegree;

    private String Interface;

    /**
     * 父流程ID
     */
    private BigDecimal fatherflowid;

    /**
     * 创建时间
     */
    private String createtime;

    /**
     * 最后更改时间
     */
    private String lastmodifiedtime;

    private Integer flowbysystem;

    private BigDecimal inflowdb;

    private BigDecimal position;

    private BigDecimal versiontype;

    private BigDecimal status;

    private String settingid;

    private BigDecimal fromid;

    private BigDecimal firingstatus;

    private String editmodule;

    private String flowmappingurl;

    public BigDecimal getFlowid() {
        return flowid;
    }

    public void setFlowid(BigDecimal flowid) {
        this.flowid = flowid;
    }
    public String getFlownumber() {
        return flownumber;
    }

    public void setFlownumber(String flownumber) {
        this.flownumber = flownumber;
    }
    public String getFlowname() {
        return flowname;
    }

    public void setFlowname(String flowname) {
        this.flowname = flowname;
    }
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
    public String getDepartincharge() {
        return departincharge;
    }

    public void setDepartincharge(String departincharge) {
        this.departincharge = departincharge;
    }
    public String getFlowrange() {
        return flowrange;
    }

    public void setFlowrange(String flowrange) {
        this.flowrange = flowrange;
    }
    public String getFlowstatus() {
        return flowstatus;
    }

    public void setFlowstatus(String flowstatus) {
        this.flowstatus = flowstatus;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public BigDecimal getVersion() {
        return version;
    }

    public void setVersion(BigDecimal version) {
        this.version = version;
    }
    public String getFlowchart() {
        return flowchart;
    }

    public void setFlowchart(String flowchart) {
        this.flowchart = flowchart;
    }
    public String getDepartassist() {
        return departassist;
    }

    public void setDepartassist(String departassist) {
        this.departassist = departassist;
    }
    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }
    public LocalDateTime getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(LocalDateTime updatetime) {
        this.updatetime = updatetime;
    }
    public String getRelatedrules() {
        return relatedrules;
    }

    public void setRelatedrules(String relatedrules) {
        this.relatedrules = relatedrules;
    }
    public String getAffectdegree() {
        return affectdegree;
    }

    public void setAffectdegree(String affectdegree) {
        this.affectdegree = affectdegree;
    }
    public String getInterface() {
        return Interface;
    }

    public void setInterface(String Interface) {
        this.Interface = Interface;
    }
    public BigDecimal getFatherflowid() {
        return fatherflowid;
    }

    public void setFatherflowid(BigDecimal fatherflowid) {
        this.fatherflowid = fatherflowid;
    }
    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
    public String getLastmodifiedtime() {
        return lastmodifiedtime;
    }

    public void setLastmodifiedtime(String lastmodifiedtime) {
        this.lastmodifiedtime = lastmodifiedtime;
    }
    public Integer getFlowbysystem() {
        return flowbysystem;
    }

    public void setFlowbysystem(Integer flowbysystem) {
        this.flowbysystem = flowbysystem;
    }
    public BigDecimal getInflowdb() {
        return inflowdb;
    }

    public void setInflowdb(BigDecimal inflowdb) {
        this.inflowdb = inflowdb;
    }
    public BigDecimal getPosition() {
        return position;
    }

    public void setPosition(BigDecimal position) {
        this.position = position;
    }
    public BigDecimal getVersiontype() {
        return versiontype;
    }

    public void setVersiontype(BigDecimal versiontype) {
        this.versiontype = versiontype;
    }
    public BigDecimal getStatus() {
        return status;
    }

    public void setStatus(BigDecimal status) {
        this.status = status;
    }
    public String getSettingid() {
        return settingid;
    }

    public void setSettingid(String settingid) {
        this.settingid = settingid;
    }
    public BigDecimal getFromid() {
        return fromid;
    }

    public void setFromid(BigDecimal fromid) {
        this.fromid = fromid;
    }
    public BigDecimal getFiringstatus() {
        return firingstatus;
    }

    public void setFiringstatus(BigDecimal firingstatus) {
        this.firingstatus = firingstatus;
    }
    public String getEditmodule() {
        return editmodule;
    }

    public void setEditmodule(String editmodule) {
        this.editmodule = editmodule;
    }
    public String getFlowmappingurl() {
        return flowmappingurl;
    }

    public void setFlowmappingurl(String flowmappingurl) {
        this.flowmappingurl = flowmappingurl;
    }

    @Override
    public String toString() {
        return "Flow{" +
            "flowid=" + flowid +
            ", flownumber=" + flownumber +
            ", flowname=" + flowname +
            ", company=" + company +
            ", departincharge=" + departincharge +
            ", flowrange=" + flowrange +
            ", flowstatus=" + flowstatus +
            ", memo=" + memo +
            ", version=" + version +
            ", flowchart=" + flowchart +
            ", departassist=" + departassist +
            ", editor=" + editor +
            ", updatetime=" + updatetime +
            ", relatedrules=" + relatedrules +
            ", affectdegree=" + affectdegree +
            ", interface=" + Interface +
            ", fatherflowid=" + fatherflowid +
            ", createtime=" + createtime +
            ", lastmodifiedtime=" + lastmodifiedtime +
            ", flowbysystem=" + flowbysystem +
            ", inflowdb=" + inflowdb +
            ", position=" + position +
            ", versiontype=" + versiontype +
            ", status=" + status +
            ", settingid=" + settingid +
            ", fromid=" + fromid +
            ", firingstatus=" + firingstatus +
            ", editmodule=" + editmodule +
            ", flowmappingurl=" + flowmappingurl +
        "}";
    }
}
