package com.huabo.contract.entity.yy;

import java.util.Date;

/**
 * 证书明细
 * @author Zxl
 *
 */
public class CertDetail {
	private Integer certDetailid;
	
	/**
	 * 证书编号
	 */
	private String content;
	/**
	 * 证书id
	 */
	private String title;
	private Integer companyid;
	private Date createdate;//保存数据时间
	private Cert cert;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getCertDetailid() {
		return certDetailid;
	}
	public void setCertDetailid(Integer certDetailid) {
		this.certDetailid = certDetailid;
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
	public Cert getCert() {
		return cert;
	}
	public void setCert(Cert cert) {
		this.cert = cert;
	}

}
