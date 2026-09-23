package com.huabo.compliance.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@TableName("TBL_BUG")
@Schema(name="TblBug对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblBug implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.INPUT)
    @Schema(name="ID")
    private BigDecimal bugid;

    @Schema(name="缺陷编号")
    private String bugnumber;

    @Schema(name="缺陷描述")
    private String bugdescripte;

    @Schema(name="发现日期")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime discovertime;

    @Schema(name="发现人")
    private String discoverperson;

    @Schema(name="缺陷性质")
    private String bugproperty;

    @Schema(name="缺陷来源")
    private String bugsource;

    @Schema(name="缺陷部门")
    private String bugdepartment;

    @Schema(name="是否需要整改")
    private String needreform;

    @Schema(name="不整改原因")
    private String resonfornoreform;

    @Schema(name="状态")
    private String bugreformstatus;

    private String projectname;

    @Schema(name="备注")
    private String memo;

    private Set tblInnerrules = new HashSet(0);
    
    private Set tblOuterrules = new HashSet(0);
    
    private Set<TblBug> children = new HashSet<>();
    
    private Set<TblBugCriterionEntity> tblBugCriterions = new HashSet<>();
    
    private Set tblAttachments = new HashSet(0);
    
    public Set getTblAttachments() {
		return tblAttachments;
	}

	public void setTblAttachments(Set tblAttachments) {
		this.tblAttachments = tblAttachments;
	}

	public Set<TblBugCriterionEntity> getTblBugCriterions() {
		return tblBugCriterions;
	}

	public void setTblBugCriterions(Set<TblBugCriterionEntity> tblBugCriterions) {
		this.tblBugCriterions = tblBugCriterions;
	}

	public Set<TblBug> getChildren() {
		return children;
	}

	public void setChildren(Set<TblBug> children) {
		this.children = children;
	}

	public Set getTblInnerrules() {
		return tblInnerrules;
	}

	public void setTblInnerrules(Set tblInnerrules) {
		this.tblInnerrules = tblInnerrules;
	}

	public Set getTblOuterrules() {
		return tblOuterrules;
	}

	public void setTblOuterrules(Set tblOuterrules) {
		this.tblOuterrules = tblOuterrules;
	}

	//TBL_BUG 外键
    @Schema(name="管理缺陷id")
    private BigDecimal fatherbugid;

    @Schema(name="类别")
    private BigDecimal inbugidb;

    @Schema(name="所属模块")
    private String bugbysystem;

    @Schema(name="业务描述")
    private String businessdescription;

    private BigDecimal projectid;

    public BigDecimal getBugid() {
        return bugid;
    }

    public void setBugid(BigDecimal bugid) {
        this.bugid = bugid;
    }
    public String getBugnumber() {
        return bugnumber;
    }

    public void setBugnumber(String bugnumber) {
        this.bugnumber = bugnumber;
    }
    public String getBugdescripte() {
        return bugdescripte;
    }

    public void setBugdescripte(String bugdescripte) {
        this.bugdescripte = bugdescripte;
    }
    public LocalDateTime getDiscovertime() {
        return discovertime;
    }

    public void setDiscovertime(LocalDateTime discovertime) {
        this.discovertime = discovertime;
    }
    public String getDiscoverperson() {
        return discoverperson;
    }

    public void setDiscoverperson(String discoverperson) {
        this.discoverperson = discoverperson;
    }
    public String getBugproperty() {
        return bugproperty;
    }

    public void setBugproperty(String bugproperty) {
        this.bugproperty = bugproperty;
    }
    public String getBugsource() {
        return bugsource;
    }

    public void setBugsource(String bugsource) {
        this.bugsource = bugsource;
    }
    public String getBugdepartment() {
        return bugdepartment;
    }

    public void setBugdepartment(String bugdepartment) {
        this.bugdepartment = bugdepartment;
    }
    public String getNeedreform() {
        return needreform;
    }

    public void setNeedreform(String needreform) {
        this.needreform = needreform;
    }
    public String getResonfornoreform() {
        return resonfornoreform;
    }

    public void setResonfornoreform(String resonfornoreform) {
        this.resonfornoreform = resonfornoreform;
    }
    public String getBugreformstatus() {
        return bugreformstatus;
    }

    public void setBugreformstatus(String bugreformstatus) {
        this.bugreformstatus = bugreformstatus;
    }
    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public BigDecimal getFatherbugid() {
        return fatherbugid;
    }

    public void setFatherbugid(BigDecimal fatherbugid) {
        this.fatherbugid = fatherbugid;
    }
    public BigDecimal getInbugidb() {
        return inbugidb;
    }

    public void setInbugidb(BigDecimal inbugidb) {
        this.inbugidb = inbugidb;
    }
    public String getBugbysystem() {
        return bugbysystem;
    }

    public void setBugbysystem(String bugbysystem) {
        this.bugbysystem = bugbysystem;
    }
    public String getBusinessdescription() {
        return businessdescription;
    }

    public void setBusinessdescription(String businessdescription) {
        this.businessdescription = businessdescription;
    }
    public BigDecimal getProjectid() {
        return projectid;
    }

    public void setProjectid(BigDecimal projectid) {
        this.projectid = projectid;
    }

    @Override
    public String toString() {
        return "TblBug{" +
            "bugid=" + bugid +
            ", bugnumber=" + bugnumber +
            ", bugdescripte=" + bugdescripte +
            ", discovertime=" + discovertime +
            ", discoverperson=" + discoverperson +
            ", bugproperty=" + bugproperty +
            ", bugsource=" + bugsource +
            ", bugdepartment=" + bugdepartment +
            ", needreform=" + needreform +
            ", resonfornoreform=" + resonfornoreform +
            ", bugreformstatus=" + bugreformstatus +
            ", projectname=" + projectname +
            ", memo=" + memo +
            ", fatherbugid=" + fatherbugid +
            ", inbugidb=" + inbugidb +
            ", bugbysystem=" + bugbysystem +
            ", businessdescription=" + businessdescription +
            ", projectid=" + projectid +
        "}";
    }
}
