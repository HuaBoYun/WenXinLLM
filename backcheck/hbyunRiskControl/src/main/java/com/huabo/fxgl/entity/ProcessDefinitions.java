package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-17
 */
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
@TableName("TBL_PROCESS_DEFINITIONS")
public class ProcessDefinitions implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private BigDecimal settingid;

    private String processdefinitionid;

    private String processdefinitionname;

    private String version;

    private String resourcename;

    private String processdefinitionkey;

    private String deploymentobjectid;

    private String resourceimagename;

    private String hangup;

    public BigDecimal getSettingid() {
        return settingid;
    }

    public void setSettingid(BigDecimal settingid) {
        this.settingid = settingid;
    }
    public String getProcessdefinitionid() {
        return processdefinitionid;
    }

    public void setProcessdefinitionid(String processdefinitionid) {
        this.processdefinitionid = processdefinitionid;
    }
    public String getProcessdefinitionname() {
        return processdefinitionname;
    }

    public void setProcessdefinitionname(String processdefinitionname) {
        this.processdefinitionname = processdefinitionname;
    }
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    public String getResourcename() {
        return resourcename;
    }

    public void setResourcename(String resourcename) {
        this.resourcename = resourcename;
    }
    public String getProcessdefinitionkey() {
        return processdefinitionkey;
    }

    public void setProcessdefinitionkey(String processdefinitionkey) {
        this.processdefinitionkey = processdefinitionkey;
    }
    public String getDeploymentobjectid() {
        return deploymentobjectid;
    }

    public void setDeploymentobjectid(String deploymentobjectid) {
        this.deploymentobjectid = deploymentobjectid;
    }
    public String getResourceimagename() {
        return resourceimagename;
    }

    public void setResourceimagename(String resourceimagename) {
        this.resourceimagename = resourceimagename;
    }
    public String getHangup() {
        return hangup;
    }

    public void setHangup(String hangup) {
        this.hangup = hangup;
    }

    @Override
    public String toString() {
        return "ProcessDefinitions{" +
            "settingid=" + settingid +
            ", processdefinitionid=" + processdefinitionid +
            ", processdefinitionname=" + processdefinitionname +
            ", version=" + version +
            ", resourcename=" + resourcename +
            ", processdefinitionkey=" + processdefinitionkey +
            ", deploymentobjectid=" + deploymentobjectid +
            ", resourceimagename=" + resourceimagename +
            ", hangup=" + hangup +
        "}";
    }
}
