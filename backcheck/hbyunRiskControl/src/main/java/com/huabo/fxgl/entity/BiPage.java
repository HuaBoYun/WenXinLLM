package com.huabo.fxgl.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-11
 */
@TableName("TBL_BI_PAGE")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class BiPage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private BigDecimal pageid;

    private String pagename;

    private String url;

    private BigDecimal forbidden;

    private String unit;

    private String pageuser;

    private String creater;

    private String memo1;

    private String memo2;

    private String pagecode;

    private String theme;

    private LocalDateTime createdate;

    private BigDecimal pagebody;

    private BigDecimal rightid;

    private BigDecimal treeid;

    private String pagedes;

    private String rqurl;

    private String type;

    private BigDecimal sort;

    public BigDecimal getPageid() {
        return pageid;
    }

    public void setPageid(BigDecimal pageid) {
        this.pageid = pageid;
    }
    public String getPagename() {
        return pagename;
    }

    public void setPagename(String pagename) {
        this.pagename = pagename;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public BigDecimal getForbidden() {
        return forbidden;
    }

    public void setForbidden(BigDecimal forbidden) {
        this.forbidden = forbidden;
    }
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getPageuser() {
        return pageuser;
    }

    public void setPageuser(String pageuser) {
        this.pageuser = pageuser;
    }
    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }
    public String getMemo1() {
        return memo1;
    }

    public void setMemo1(String memo1) {
        this.memo1 = memo1;
    }
    public String getMemo2() {
        return memo2;
    }

    public void setMemo2(String memo2) {
        this.memo2 = memo2;
    }
    public String getPagecode() {
        return pagecode;
    }

    public void setPagecode(String pagecode) {
        this.pagecode = pagecode;
    }
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
    public LocalDateTime getCreatedate() {
        return createdate;
    }

    public void setCreatedate(LocalDateTime createdate) {
        this.createdate = createdate;
    }
    public BigDecimal getPagebody() {
        return pagebody;
    }

    public void setPagebody(BigDecimal pagebody) {
        this.pagebody = pagebody;
    }
    public BigDecimal getRightid() {
        return rightid;
    }

    public void setRightid(BigDecimal rightid) {
        this.rightid = rightid;
    }
    public BigDecimal getTreeid() {
        return treeid;
    }

    public void setTreeid(BigDecimal treeid) {
        this.treeid = treeid;
    }
    public String getPagedes() {
        return pagedes;
    }

    public void setPagedes(String pagedes) {
        this.pagedes = pagedes;
    }
    public String getRqurl() {
        return rqurl;
    }

    public void setRqurl(String rqurl) {
        this.rqurl = rqurl;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public BigDecimal getSort() {
        return sort;
    }

    public void setSort(BigDecimal sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "BiPage{" +
            "pageid=" + pageid +
            ", pagename=" + pagename +
            ", url=" + url +
            ", forbidden=" + forbidden +
            ", unit=" + unit +
            ", pageuser=" + pageuser +
            ", creater=" + creater +
            ", memo1=" + memo1 +
            ", memo2=" + memo2 +
            ", pagecode=" + pagecode +
            ", theme=" + theme +
            ", createdate=" + createdate +
            ", pagebody=" + pagebody +
            ", rightid=" + rightid +
            ", treeid=" + treeid +
            ", pagedes=" + pagedes +
            ", rqurl=" + rqurl +
            ", type=" + type +
            ", sort=" + sort +
        "}";
    }
}
