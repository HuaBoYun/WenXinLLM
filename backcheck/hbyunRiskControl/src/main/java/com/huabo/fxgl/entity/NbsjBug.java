package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("TBL_NBSJ_BUG")
public class NbsjBug implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal bugid;

    private String bugnumber;

    private String bugdescripte;

    private LocalDateTime discovertime;

    private String discoverperson;

    private String bugproperty;

    private String bugsource;

    private String bugdepartment;

    private String needreform;

    private String resonfornoreform;

    private String bugreformstatus;

    private String projectname;

    private String memo;

    private String bugdapartment;

    public String getBugdapartment() {
        return bugdapartment;
    }

    public void setBugdapartment(String bugdapartment) {
        this.bugdapartment = bugdapartment;
    }

    private BigDecimal fatherbugid;

    private BigDecimal inbugidb;

    private String bugbysystem;

    private String businessdescription;

    private BigDecimal projectid;

    private Set<NbsjBug> children=new HashSet<NbsjBug>();

    public Set<NbsjBug> getChildren() {
        return children;
    }

    public void setChildren(Set<NbsjBug> children) {
        this.children = children;
    }

    private Set tblInnerrules = new HashSet(0);

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

    private Set tblOuterrules = new HashSet(0);

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
        return "NbsjBug{" +
                "bugid=" + bugid +
                ", bugnumber='" + bugnumber + '\'' +
                ", bugdescripte='" + bugdescripte + '\'' +
                ", discovertime=" + discovertime +
                ", discoverperson='" + discoverperson + '\'' +
                ", bugproperty='" + bugproperty + '\'' +
                ", bugsource='" + bugsource + '\'' +
                ", bugdepartment='" + bugdepartment + '\'' +
                ", needreform='" + needreform + '\'' +
                ", resonfornoreform='" + resonfornoreform + '\'' +
                ", bugreformstatus='" + bugreformstatus + '\'' +
                ", projectname='" + projectname + '\'' +
                ", memo='" + memo + '\'' +
                ", fatherbugid=" + fatherbugid +
                ", inbugidb=" + inbugidb +
                ", bugbysystem='" + bugbysystem + '\'' +
                ", businessdescription='" + businessdescription + '\'' +
                ", projectid=" + projectid +
                ", children=" + children +
                ", tblInnerrules=" + tblInnerrules +
                ", tblOuterrules=" + tblOuterrules +
                '}';
    }
}
