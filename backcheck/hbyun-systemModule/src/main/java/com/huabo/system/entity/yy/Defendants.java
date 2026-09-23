package com.huabo.system.entity.yy;

import java.util.Date;

public class Defendants {
	private Integer defendantsid;
	private String type; //1-公司 2-个人
    private String id; //人或公司id
    private String name; //人或公司名
    private Integer companyid;
 	private Date createdate;//保存数据时间
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
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
	public Integer getDefendantsid() {
		return defendantsid;
	}
	public void setDefendantsid(Integer defendantsid) {
		this.defendantsid = defendantsid;
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
