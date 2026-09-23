package com.huabo.system.entity.yy;

import java.util.Date;

/**
 * 变更信息
 * @author tj
 *
 */
public class Changeinformation {
	private Integer changeInformationid;
	private String changeItem;       //变更事项
	private String createTime;       //创建时间
	private String contentBefore;    //变更前
	private String contentAfter;     //变更后
	private String changeTime ;      //变更时间
	private String name;
	private Integer companyid;
	private Date createdate;//保存数据时间
	public String getChangeItem() {
		return changeItem;
	}
	public void setChangeItem(String changeItem) {
		this.changeItem = changeItem;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getChangeInformationid() {
		return changeInformationid;
	}
	public void setChangeInformationid(Integer changeInformationid) {
		this.changeInformationid = changeInformationid;
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
