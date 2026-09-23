package com.huabo.system.entity.yy;

import java.util.Date;

/**
 * 对外投资信息
 * @author tj
 *
 */
public class OutboundInvestment {
	private Integer inverstmentid;
	private String reportYear; //年份
    private String outcompanyName; //被投资公司
    private String regNum; //注册码
    private String creditCode;// 社会统一信用代码
    private String type; //1-人 2-公司
    private String clickId; //被投资公司id
    private Integer yycompanyid;
  	private Date yycreatedate;//保存数据时间
	public String getReportYear() {
		return reportYear;
	}
	public void setReportYear(String reportYear) {
		this.reportYear = reportYear;
	}
	public String getOutcompanyName() {
		return outcompanyName;
	}
	public void setOutcompanyName(String outcompanyName) {
		this.outcompanyName = outcompanyName;
	}
	public String getRegNum() {
		return regNum;
	}
	public void setRegNum(String regNum) {
		this.regNum = regNum;
	}
	public String getCreditCode() {
		return creditCode;
	}
	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getClickId() {
		return clickId;
	}
	public void setClickId(String clickId) {
		this.clickId = clickId;
	}
	public Integer getInverstmentid() {
		return inverstmentid;
	}
	public void setInverstmentid(Integer inverstmentid) {
		this.inverstmentid = inverstmentid;
	}
	public Integer getYycompanyid() {
		return yycompanyid;
	}
	public void setYycompanyid(Integer yycompanyid) {
		this.yycompanyid = yycompanyid;
	}
	public Date getYycreatedate() {
		return yycreatedate;
	}
	public void setYycreatedate(Date yycreatedate) {
		this.yycreatedate = yycreatedate;
	}
}
