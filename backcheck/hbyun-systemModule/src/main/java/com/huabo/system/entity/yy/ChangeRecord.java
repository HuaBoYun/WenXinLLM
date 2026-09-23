package com.huabo.system.entity.yy;

import java.util.Date;

/**
 * 年报变更
 * @author tj
 *
 */
public class ChangeRecord {

	private Integer changeRecordid;
	private String reportYear; //年份
    private String changeItem; //变更事项
    private String contentBefore; //变更前
    private String contentAfter; //变更后
    private String changeTime; //变更时间
    private Integer companyid;
	private Date createdate;//保存数据时间

	public String getReportYear() {
		return reportYear;
	}
	public void setReportYear(String reportYear) {
		this.reportYear = reportYear;
	}
	public String getChangeItem() {
		return changeItem;
	}
	public void setChangeItem(String changeItem) {
		this.changeItem = changeItem;
	}
	public String getContentBefore() {
		return contentBefore;
	}
	public void setContentBefore(String contentBefore) {
		this.contentBefore = contentBefore;
	}
	public String getContentAfter() {
		return contentAfter;
	}
	public void setContentAfter(String contentAfter) {
		this.contentAfter = contentAfter;
	}
	public String getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(String changeTime) {
		this.changeTime = changeTime;
	}
	public Integer getChangeRecordid() {
		return changeRecordid;
	}
	public void setChangeRecordid(Integer changeRecordid) {
		this.changeRecordid = changeRecordid;
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
