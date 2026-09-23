package com.huabo.system.entity.yy;

import java.util.Date;

/**
 * 股东股权变更信息
 * @author tj
 *
 */
public class EquityChangeInfo {
	private Integer equitychangeinfoid;
	private String reportYear; //年份
    private String investorName; //股东（发起人）
    private String ratioBefore; //变更前股权比例
    private String ratioAfter; //变更后股权比例
    private String changeTime; //股权变更日期
    private Integer companyid;
  	private Date createdate;//保存数据时间
	public String getReportYear() {
		return reportYear;
	}
	public void setReportYear(String reportYear) {
		this.reportYear = reportYear;
	}
	public String getInvestorName() {
		return investorName;
	}
	public void setInvestorName(String investorName) {
		this.investorName = investorName;
	}
	public String getRatioBefore() {
		return ratioBefore;
	}
	public void setRatioBefore(String ratioBefore) {
		this.ratioBefore = ratioBefore;
	}
	public String getRatioAfter() {
		return ratioAfter;
	}
	public void setRatioAfter(String ratioAfter) {
		this.ratioAfter = ratioAfter;
	}
	public String getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(String changeTime) {
		this.changeTime = changeTime;
	}
	public Integer getEquitychangeinfoid() {
		return equitychangeinfoid;
	}
	public void setEquitychangeinfoid(Integer equitychangeinfoid) {
		this.equitychangeinfoid = equitychangeinfoid;
	}
	public Integer getCompanyid() {
		return companyid;
	}
	public void setCompanyid(Integer companyid) {
		this.companyid = companyid;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
}
