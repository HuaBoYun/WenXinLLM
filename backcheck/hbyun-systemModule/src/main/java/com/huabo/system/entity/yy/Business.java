package com.huabo.system.entity.yy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 基本信息
 * @author tj
 *
 */
public class Business {
	private Integer businessid;
	private String updatetime;               //更新时间
	private String staffNumRange;            //人员规模
	private String fromTime;                 //经营开始时间
	private String type;                     //法人类型，1 人 2 公司
	private String categoryScore;            //行业分数
	private String bondName;                 //股票名
	private String id;                       //企业id
	private String isMicroEnt;                //是否是小微企业 0不是 1是
	private String usedBondName;              //股票曾用名
	private String regNumber ;                //注册号
	private String percentileScore ;          //企业评分
	private String regCapital ;               //注册资本
	private String name  ;                    //企业名
	private String regInstitute ;             //登记机关
	private String regLocation ;              //注册地址
	private String industry  ;                //行业
	private String approvedTime ;             //核准时间
	private String socialStaffNum ;           //参保人数
	private String tags  ;                    //企业标签
	private String logo;                      //logo（不建议使用）
	private String taxNumber ;                //纳税人识别号
	private String businessScope;             //经营范围
	private String property3 ;                //英文名
	private String alias  ;                   //简称
	private String orgNumber ;                //组织机构代码
	private String regStatus;                 //经营状态
	private String estiblishTime  ;           //成立日期
	private String bondType;                  //股票类型
	private String legalPersonName ;          //法人
	private String toTime ;                   //经营结束时间
	private String legalPersonId ;            //法人id
	private String sourceFlag;                //数据来源标志
	private String actualCapital ;            //实收注册资金
	private String flag  ;                    //0-显示 1-不显示
	private String correctCompanyId;          //新公司名id
	private String companyOrgType ;           //企业类型
	private String base   ;                   //省份简称
	private String updateTimes ;              //更新时间
	private String companyType  ;             //无用
	private String creditCode ;               //统一社会信用代码
	private String companyId  ;               //对应表id
	private String historyNames ;             //曾用名
	private String bondNum   ;                //股票号
	private String regCapitalCurrency;        //注册资本币种 人民币 美元 欧元 等
	private String actualCapitalCurrency ;    //实收注册资本币种 人民币 美元 欧元 等
	private String orgApprovedInstitute ;     //核准机关（无用）
	private String nameSuffix  ;              //无用
	private String email ;                    //邮箱
	private String websiteList ;              //网址
	private String phoneNumber;               //phoneNumber
	private String revokeDate ;               //吊销日期
	private String revokeReason ;             //吊销原因
	private String cancelDate  ;              //注销日期
	private String cancelReason;              //注销原因
	private String property4 ;                  //弃用
    private String property5; //无用
    private String listCode; //无用
	
    private Integer yycompanyid;
    private Date createdate;//保存数据时间
    
	
	
	private List<Keypersonnel> list=new ArrayList<Keypersonnel>();//主要人员
	
	private String staffList;
	
	
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getStaffNumRange() {
		return staffNumRange;
	}
	public void setStaffNumRange(String staffNumRange) {
		this.staffNumRange = staffNumRange;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCategoryScore() {
		return categoryScore;
	}
	public void setCategoryScore(String categoryScore) {
		this.categoryScore = categoryScore;
	}
	public String getBondName() {
		return bondName;
	}
	public void setBondName(String bondName) {
		this.bondName = bondName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsMicroEnt() {
		return isMicroEnt;
	}
	public void setIsMicroEnt(String isMicroEnt) {
		this.isMicroEnt = isMicroEnt;
	}
	public String getUsedBondName() {
		return usedBondName;
	}
	public void setUsedBondName(String usedBondName) {
		this.usedBondName = usedBondName;
	}
	public String getRegNumber() {
		return regNumber;
	}
	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}
	public String getPercentileScore() {
		return percentileScore;
	}
	public void setPercentileScore(String percentileScore) {
		this.percentileScore = percentileScore;
	}
	public String getRegCapital() {
		return regCapital;
	}
	public void setRegCapital(String regCapital) {
		this.regCapital = regCapital;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegInstitute() {
		return regInstitute;
	}
	public void setRegInstitute(String regInstitute) {
		this.regInstitute = regInstitute;
	}
	public String getRegLocation() {
		return regLocation;
	}
	public void setRegLocation(String regLocation) {
		this.regLocation = regLocation;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getApprovedTime() {
		return approvedTime;
	}
	public void setApprovedTime(String approvedTime) {
		this.approvedTime = approvedTime;
	}
	public String getSocialStaffNum() {
		return socialStaffNum;
	}
	public void setSocialStaffNum(String socialStaffNum) {
		this.socialStaffNum = socialStaffNum;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getTaxNumber() {
		return taxNumber;
	}
	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}
	public String getBusinessScope() {
		return businessScope;
	}
	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}
	public String getProperty3() {
		return property3;
	}
	public void setProperty3(String property3) {
		this.property3 = property3;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getOrgNumber() {
		return orgNumber;
	}
	public void setOrgNumber(String orgNumber) {
		this.orgNumber = orgNumber;
	}
	public String getRegStatus() {
		return regStatus;
	}
	public void setRegStatus(String regStatus) {
		this.regStatus = regStatus;
	}
	public String getEstiblishTime() {
		return estiblishTime;
	}
	public void setEstiblishTime(String estiblishTime) {
		this.estiblishTime = estiblishTime;
	}
	public String getBondType() {
		return bondType;
	}
	public void setBondType(String bondType) {
		this.bondType = bondType;
	}
	public String getLegalPersonName() {
		return legalPersonName;
	}
	public void setLegalPersonName(String legalPersonName) {
		this.legalPersonName = legalPersonName;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	public String getLegalPersonId() {
		return legalPersonId;
	}
	public void setLegalPersonId(String legalPersonId) {
		this.legalPersonId = legalPersonId;
	}
	public String getSourceFlag() {
		return sourceFlag;
	}
	public void setSourceFlag(String sourceFlag) {
		this.sourceFlag = sourceFlag;
	}
	public String getActualCapital() {
		return actualCapital;
	}
	public void setActualCapital(String actualCapital) {
		this.actualCapital = actualCapital;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getCorrectCompanyId() {
		return correctCompanyId;
	}
	public void setCorrectCompanyId(String correctCompanyId) {
		this.correctCompanyId = correctCompanyId;
	}
	public String getCompanyOrgType() {
		return companyOrgType;
	}
	public void setCompanyOrgType(String companyOrgType) {
		this.companyOrgType = companyOrgType;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getUpdateTimes() {
		return updateTimes;
	}
	public void setUpdateTimes(String updateTimes) {
		this.updateTimes = updateTimes;
	}
	public String getCompanyType() {
		return companyType;
	}
	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}
	public String getCreditCode() {
		return creditCode;
	}
	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getHistoryNames() {
		return historyNames;
	}
	public void setHistoryNames(String historyNames) {
		this.historyNames = historyNames;
	}
	public String getBondNum() {
		return bondNum;
	}
	public void setBondNum(String bondNum) {
		this.bondNum = bondNum;
	}
	public String getRegCapitalCurrency() {
		return regCapitalCurrency;
	}
	public void setRegCapitalCurrency(String regCapitalCurrency) {
		this.regCapitalCurrency = regCapitalCurrency;
	}
	public String getActualCapitalCurrency() {
		return actualCapitalCurrency;
	}
	public void setActualCapitalCurrency(String actualCapitalCurrency) {
		this.actualCapitalCurrency = actualCapitalCurrency;
	}
	public String getOrgApprovedInstitute() {
		return orgApprovedInstitute;
	}
	public void setOrgApprovedInstitute(String orgApprovedInstitute) {
		this.orgApprovedInstitute = orgApprovedInstitute;
	}
	public String getNameSuffix() {
		return nameSuffix;
	}
	public void setNameSuffix(String nameSuffix) {
		this.nameSuffix = nameSuffix;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWebsiteList() {
		return websiteList;
	}
	public void setWebsiteList(String websiteList) {
		this.websiteList = websiteList;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getRevokeDate() {
		return revokeDate;
	}
	public void setRevokeDate(String revokeDate) {
		this.revokeDate = revokeDate;
	}
	public String getRevokeReason() {
		return revokeReason;
	}
	public void setRevokeReason(String revokeReason) {
		this.revokeReason = revokeReason;
	}
	public String getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public String getProperty4() {
		return property4;
	}
	public void setProperty4(String property4) {
		this.property4 = property4;
	}
	public String getStaffList() {
		return staffList;
	}
	public void setStaffList(String staffList) {
		this.staffList = staffList;
	}
	public List<Keypersonnel> getList() {
		return list;
	}
	public void setList(List<Keypersonnel> list) {
		this.list = list;
	}
	public String getProperty5() {
		return property5;
	}
	public void setProperty5(String property5) {
		this.property5 = property5;
	}
	public String getListCode() {
		return listCode;
	}
	public void setListCode(String listCode) {
		this.listCode = listCode;
	}
	public Integer getBusinessid() {
		return businessid;
	}
	public void setBusinessid(Integer businessid) {
		this.businessid = businessid;
	}
	 
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public Integer getYycompanyid() {
		return yycompanyid;
	}
	public void setYycompanyid(Integer yycompanyid) {
		this.yycompanyid = yycompanyid;
	}

}
