package com.huabo.compliance.mysql.entity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 页面查找公共类
 *
 * @author taoyb
 */
public class FindMySql extends org.mockito.internal.matchers.Find {

    private String code; //编号
    private String num;//编号
    private String name;//名称
    private String userName;
    private String str1;
    private String str2;
    private String startDate;//开始时间
    private String endDate;//结束时间
    private String date1;
    private String date2;
    private String date3;
    private String order;//排序
    private String state;
    private String title;
    private String view;
    private BigDecimal id;
    private Integer orgid;
    private String businessDescription;
    private String bugproperty;
    private String buglevel;
    private String type;
    private String bugsource;
    private String bugdescripte;
    private String awKhmcs;
    private String cwglcode;
    private String status;
    private String pmid;
    private String staffname1;
    private String umpireid;
    private String staffname;
    private String interfacename;
    private String staffid;

    private String companyname;

    private String fxtype;

    public FindMySql(String regex) {
        super(regex);
    }

    public String getBugdescripte() {
        return bugdescripte;
    }

    public void setBugdescripte(String bugdescripte) {
        this.bugdescripte = bugdescripte;
    }

    public String getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }

    public String getBugproperty() {
        return bugproperty;
    }

    public void setBugproperty(String bugproperty) {
        this.bugproperty = bugproperty;
    }

    public String getBuglevel() {
        return buglevel;
    }

    public void setBuglevel(String buglevel) {
        this.buglevel = buglevel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public String getDate3() {
        return date3;
    }

    public FindMySql setDate3(String date3) {
        this.date3 = date3;
        return this;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        try {
            startDate = sdf.format(sdf.parse(startDate));
        } catch (ParseException e) {
        }
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        try {
            endDate = sdf.format(sdf.parse(endDate));
        } catch (ParseException e) {
        }
        this.endDate = endDate;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    public Integer getOrgid() {
        return orgid;
    }

    public void setOrgid(Integer orgid) {
        this.orgid = orgid;
    }

    public String getDate1() {
        return date1;
    }

    public FindMySql setDate1(String date1) {
        this.date1 = date1;
        return this;
    }

    public String getDate2() {
        return date2;
    }

    public FindMySql setDate2(String date2) {
        this.date2 = date2;
        return this;
    }

    public String getBugsource() {
        return bugsource;
    }

    public void setBugsource(String bugsource) {
        this.bugsource = bugsource;
    }

    public String getAwKhmcs() {
        return awKhmcs;
    }

    public void setAwKhmcs(String awKhmcs) {
        this.awKhmcs = awKhmcs;
    }

    public String getCwglcode() {
        return cwglcode;
    }

    public void setCwglcode(String cwglcode) {
        this.cwglcode = cwglcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        this.pmid = pmid;
    }

    public String getStaffname1() {
        return staffname1;
    }

    public void setStaffname1(String staffname1) {
        this.staffname1 = staffname1;
    }

    public String getUmpireid() {
        return umpireid;
    }

    public void setUmpireid(String umpireid) {
        this.umpireid = umpireid;
    }

    public String getStaffname() {
        return staffname;
    }

    public void setStaffname(String staffname) {
        this.staffname = staffname;
    }

    public String getInterfacename() {
        return interfacename;
    }

    public void setInterfacename(String interfacename) {
        this.interfacename = interfacename;
    }

    public String getStaffid() {
        return staffid;
    }

    public void setStaffid(String staffid) {
        this.staffid = staffid;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getFxtype() {
        return fxtype;
    }

    public void setFxtype(String fxtype) {
        this.fxtype = fxtype;
    }

}
