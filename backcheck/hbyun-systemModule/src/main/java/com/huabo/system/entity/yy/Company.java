package com.huabo.system.entity.yy;

import java.util.Date;


public class Company {
	private Integer panyid;
	private String id;
	private String name;
	private String type;
	private Integer companyid;
	private Date createdate;//保存数据时间
	
	private Judicialrisk judicialrisk;
	
	private Stock stock;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getPanyid() {
		return panyid;
	}
	public void setPanyid(Integer panyid) {
		this.panyid = panyid;
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
	public Judicialrisk getJudicialrisk() {
		return judicialrisk;
	}
	public void setJudicialrisk(Judicialrisk judicialrisk) {
		this.judicialrisk = judicialrisk;
	}
	public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	

}
