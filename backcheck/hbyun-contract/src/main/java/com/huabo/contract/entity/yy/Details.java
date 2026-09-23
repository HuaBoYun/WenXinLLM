package com.huabo.contract.entity.yy;

import java.util.Date;

public class Details {
	private Integer detailsid;
	private String consult_price; //评估价格
    private String title; //标题
    private String initial_price; //起拍价格
    private String jid;//对应表id
    private Integer companyid;
 	private Date createdate;//保存数据时间
	public String getConsult_price() {
		return consult_price;
	}
	public void setConsult_price(String consult_price) {
		this.consult_price = consult_price;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getInitial_price() {
		return initial_price;
	}
	public void setInitial_price(String initial_price) {
		this.initial_price = initial_price;
	}
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public Integer getDetailsid() {
		return detailsid;
	}
	public void setDetailsid(Integer detailsid) {
		this.detailsid = detailsid;
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
