package com.hbfk.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2021-10-20
 */
@Data
public class TblLoginTypeUtil implements Serializable {

    private static final long serialVersionUID = 1L;

      private BigDecimal loginid;

    private String loginurl;

    private String salehotline;

    private String echnicalsupportphone;

    private String loginpage;

    private String loginpagetwo;

    private String loginpagethree;

    private String homepage;

    private String homepagepic;

    private String loginpic;

    private String loginname;

    private String orgid;

	public BigDecimal getLoginid() {
		return loginid;
	}

	public void setLoginid(BigDecimal loginid) {
		this.loginid = loginid;
	}

	public String getLoginurl() {
		return loginurl;
	}

	public void setLoginurl(String loginurl) {
		this.loginurl = loginurl;
	}

	public String getSalehotline() {
		return salehotline;
	}

	public void setSalehotline(String salehotline) {
		this.salehotline = salehotline;
	}

	public String getEchnicalsupportphone() {
		return echnicalsupportphone;
	}

	public void setEchnicalsupportphone(String echnicalsupportphone) {
		this.echnicalsupportphone = echnicalsupportphone;
	}

	public String getLoginpage() {
		return loginpage;
	}

	public void setLoginpage(String loginpage) {
		this.loginpage = loginpage;
	}

	public String getLoginpagetwo() {
		return loginpagetwo;
	}

	public void setLoginpagetwo(String loginpagetwo) {
		this.loginpagetwo = loginpagetwo;
	}

	public String getLoginpagethree() {
		return loginpagethree;
	}

	public void setLoginpagethree(String loginpagethree) {
		this.loginpagethree = loginpagethree;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getHomepagepic() {
		return homepagepic;
	}

	public void setHomepagepic(String homepagepic) {
		this.homepagepic = homepagepic;
	}

	public String getLoginpic() {
		return loginpic;
	}

	public void setLoginpic(String loginpic) {
		this.loginpic = loginpic;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}


}
