package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@TableName("TBL_LOGINTYPE")
@Schema(name="TblLogintype对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblLogintype implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.INPUT)
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

    @Override
    public String toString() {
        return "TblLogintype{" +
            "loginid=" + loginid +
            ", loginurl=" + loginurl +
            ", salehotline=" + salehotline +
            ", echnicalsupportphone=" + echnicalsupportphone +
            ", loginpage=" + loginpage +
            ", loginpagetwo=" + loginpagetwo +
            ", loginpagethree=" + loginpagethree +
            ", homepage=" + homepage +
            ", homepagepic=" + homepagepic +
            ", loginpic=" + loginpic +
            ", loginname=" + loginname +
            ", orgid=" + orgid +
        "}";
    }
}
