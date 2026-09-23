package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>
 * 
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
@TableName("TBL_MONITOR_RULE")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class MonitorRule implements Serializable {

    private static final long serialVersionUID = 1L;
    /**是否行业库  0标识否*/
    public static final Integer IS_HY0 = 0;
    /**是否行业库  1标识是*/
    public static final Integer IS_HY1 = 1;

    @Id
	@Column(name = "RULEID")
	@Schema(name = "主键")
    @TableId(type=IdType.INPUT)  //注意主键类型要指定为Input
    private BigDecimal ruleid;

    @Schema(name = "业务规则名称")
    private String rulename;

    @Schema(name = "规则描述")
    private String ruledescription;

    @Schema(name = "业务规则编码")
    private String rulecode;

    private String rulepriority;

    @Schema(name = "风险等级")
    private String risklevel;

    @Schema(name = "规则状态")
    private String satus;

    private String regexp;

    @Schema(name = "规则对应SQL")
    private String rulesql;

    @Schema(name = "规则备注")
    private String memo;

    @Schema(name = "区分是否为行业")
    private String inruledb;

    private String tips;

    private BigDecimal orgid;
    
    @Schema(name = "组织名称")
    private String orgname;

    @Schema(name = "链接字符串id")
    private String connectionstrings;
    
    
    @Schema(name = "数据源名称")
    private String dataname;
    
    
    @Schema(name = "执行状态")
    private BigDecimal runstatus;

    @Schema(name = "创建人员id")
    private BigDecimal staffid;

    @Schema(name = "报表SQL")
    private String reportsql;

    public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public String getDataname() {
		return dataname;
	}

	public void setDataname(String dataname) {
		this.dataname = dataname;
	}

	public BigDecimal getRuleid() {
        return ruleid;
    }

    public void setRuleid(BigDecimal ruleid) {
        this.ruleid = ruleid;
    }
    public String getRulename() {
        return rulename;
    }

    public void setRulename(String rulename) {
        this.rulename = rulename;
    }
    public String getRuledescription() {
        return ruledescription;
    }

    public void setRuledescription(String ruledescription) {
        this.ruledescription = ruledescription;
    }
    public String getRulecode() {
        return rulecode;
    }

    public void setRulecode(String rulecode) {
        this.rulecode = rulecode;
    }
    public String getRulepriority() {
        return rulepriority;
    }

    public void setRulepriority(String rulepriority) {
        this.rulepriority = rulepriority;
    }
    public String getRisklevel() {
        return risklevel;
    }

    public void setRisklevel(String risklevel) {
        this.risklevel = risklevel;
    }
    public String getSatus() {
        return satus;
    }

    public void setSatus(String satus) {
        this.satus = satus;
    }
    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }
    public String getRulesql() {
        return rulesql;
    }

    public void setRulesql(String rulesql) {
        this.rulesql = rulesql;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getInruledb() {
        return inruledb;
    }

    public void setInruledb(String inruledb) {
        this.inruledb = inruledb;
    }
    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }
    public String getConnectionstrings() {
        return connectionstrings;
    }

    public void setConnectionstrings(String connectionstrings) {
        this.connectionstrings = connectionstrings;
    }
    public BigDecimal getRunstatus() {
        return runstatus;
    }

    public void setRunstatus(BigDecimal runstatus) {
        this.runstatus = runstatus;
    }
    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }
    public String getReportsql() {
        return reportsql;
    }

    public void setReportsql(String reportsql) {
        this.reportsql = reportsql;
    }

    @Override
    public String toString() {
        return "MonitorRule{" +
            "ruleid=" + ruleid +
            ", rulename=" + rulename +
            ", ruledescription=" + ruledescription +
            ", rulecode=" + rulecode +
            ", rulepriority=" + rulepriority +
            ", risklevel=" + risklevel +
            ", satus=" + satus +
            ", regexp=" + regexp +
            ", rulesql=" + rulesql +
            ", memo=" + memo +
            ", inruledb=" + inruledb +
            ", tips=" + tips +
            ", orgid=" + orgid +
            ", connectionstrings=" + connectionstrings +
            ", runstatus=" + runstatus +
            ", staffid=" + staffid +
            ", reportsql=" + reportsql +
            ", dataname=" + dataname +
            ", orgname=" + orgname +
        "}";
    }
}
