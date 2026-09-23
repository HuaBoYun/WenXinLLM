package com.huabo.compliance.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Schema(name="TblOrganization对象")
@KeySequence(value = "HIBERNATE_SEQUENCE", dbType = DbType.ORACLE)
@TableName("TBL_ORGANIZATION")
public class TblOrganization implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 标识为行业
     */
    public static final int TYPE_HY = 100;
    /**
     * 标识为行业问题
     */
    public static final int TYPE_HY_WT = 101;
    /**
     * 标识为行业缺陷
     */
    public static final int TYPE_HY_QX = 102;
    /**
     * 标识为行业规则库
     */
    public static final int TYPE_HY_GZK = 103;
    /**
     * 标识为行业指标库
     */
    public static final int TYPE_HY_ZBK = 104;
    /**
     * 标识为行业模型库
     */
    public static final int TYPE_HY_MXK = 105;
    /**
     * 标识为行业知识库库
     */
    public static final int TYPE_HY_ZSK = 106;
    /**
     * 审计经验库
     */
    public static final int TYPE_HY_SJ = 107;
    /**
     * 行业数据库
     */
    public static final int TYPE_HY_SJK = 108;
    public static final Integer ORGTYPE = 100;
    public static final Integer DEL_YES = 1;
    public static final Integer DEL_NO = 0;
    public static final Integer AUDITTYPE = 1;
    /**
     * 标识外聘专家库
     */
    public static final String WPZJK = "wpzjk";

    public static final String ZC = "201047";

    public static final String ZCX = "7e628e56fbf6b762";

    public static final String DCFL = "0687a232942c802e";

    @TableId
    private BigDecimal orgid;

    private String orgname;

    private BigDecimal fatherorgid;

    private String orgnumber;

    private String orgmeno;

    private String memo;

    private String icode;

    private Integer orgtype;

    public Integer getOrgtype() {
		return orgtype;
	}

	public void setOrgtype(Integer orgtype) {
		this.orgtype = orgtype;
	}

	private BigDecimal audittype;

    private Integer status;

    private String iszy;

    private String hyzsktype;

    private Integer orderid;

    private BigDecimal outsideid;

    private String outsideopendid;

    /**
     * 是否使用自动编号 0 不使用；1 使用
     */
    private BigDecimal isautonumber;


    private LocalDateTime orgcreate;

    private Long isinitialization;

    /**
     * 职务
     */
    private String duties;

    /**
     * 行业编号
     */
    private BigDecimal industryid;

    /**
     * 新增来源于微信 1为微信 0为pc
     */
    private String bywx;

    /**
     * 数据来源;
     */
    private String datasource;

    /**
     * 历史id值;
     */
    private String historycode;

    /**
     * 珠海港历史部门id
     */
    private String historydepartmentid;

    private String writtenbydept;

    private String pkymorgid;



    @JSONField(serialize = false)
    @TableField(exist = false)
    private Set<TblOrganization> children = new HashSet<TblOrganization>();

    public Set<TblOrganization> getChildren() {
        return children;
    }

    public void setChildren(Set<TblOrganization> children) {
        this.children = children;
    }


    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public BigDecimal getFatherorgid() {
        return fatherorgid;
    }

    public void setFatherorgid(BigDecimal fatherorgid) {
        this.fatherorgid = fatherorgid;
    }

    public String getOrgnumber() {
        return orgnumber;
    }

    public void setOrgnumber(String orgnumber) {
        this.orgnumber = orgnumber;
    }

    public String getOrgmeno() {
        return orgmeno;
    }

    public void setOrgmeno(String orgmeno) {
        this.orgmeno = orgmeno;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getIcode() {
        return icode;
    }

    public void setIcode(String icode) {
        this.icode = icode;
    }

    

    public BigDecimal getAudittype() {
        return audittype;
    }

    public void setAudittype(BigDecimal audittype) {
        this.audittype = audittype;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIszy() {
        return iszy;
    }

    public void setIszy(String iszy) {
        this.iszy = iszy;
    }

    public String getHyzsktype() {
        return hyzsktype;
    }

    public void setHyzsktype(String hyzsktype) {
        this.hyzsktype = hyzsktype;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public BigDecimal getOutsideid() {
        return outsideid;
    }

    public void setOutsideid(BigDecimal outsideid) {
        this.outsideid = outsideid;
    }

    public String getOutsideopendid() {
        return outsideopendid;
    }

    public void setOutsideopendid(String outsideopendid) {
        this.outsideopendid = outsideopendid;
    }

    public BigDecimal getIsautonumber() {
        return isautonumber;
    }

    public void setIsautonumber(BigDecimal isautonumber) {
        this.isautonumber = isautonumber;
    }

    public LocalDateTime getOrgcreate() {
        return orgcreate;
    }

    public void setOrgcreate(LocalDateTime orgcreate) {
        this.orgcreate = orgcreate;
    }

    public Long getIsinitialization() {
        return isinitialization;
    }

    public void setIsinitialization(Long isinitialization) {
        this.isinitialization = isinitialization;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public BigDecimal getIndustryid() {
        return industryid;
    }

    public void setIndustryid(BigDecimal industryid) {
        this.industryid = industryid;
    }

    public String getBywx() {
        return bywx;
    }

    public void setBywx(String bywx) {
        this.bywx = bywx;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getHistorycode() {
        return historycode;
    }

    public void setHistorycode(String historycode) {
        this.historycode = historycode;
    }

    public String getHistorydepartmentid() {
        return historydepartmentid;
    }

    public void setHistorydepartmentid(String historydepartmentid) {
        this.historydepartmentid = historydepartmentid;
    }

    public String getWrittenbydept() {
        return writtenbydept;
    }

    public void setWrittenbydept(String writtenbydept) {
        this.writtenbydept = writtenbydept;
    }

    public String getPkymorgid() {
        return pkymorgid;
    }

    public void setPkymorgid(String pkymorgid) {
        this.pkymorgid = pkymorgid;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "orgid=" + orgid +
                ", orgname=" + orgname +
                ", fatherorgid=" + fatherorgid +
                ", orgnumber=" + orgnumber +
                ", orgmeno=" + orgmeno +
                ", memo=" + memo +
                ", icode=" + icode +
                ", orgtype=" + orgtype +
                ", audittype=" + audittype +
                ", status=" + status +
                ", iszy=" + iszy +
                ", hyzsktype=" + hyzsktype +
                ", orderid=" + orderid +
                ", outsideid=" + outsideid +
                ", outsideopendid=" + outsideopendid +
                ", isautonumber=" + isautonumber +
                ", orgcreate=" + orgcreate +
                ", isinitialization=" + isinitialization +
                ", duties=" + duties +
                ", industryid=" + industryid +
                ", bywx=" + bywx +
                ", datasource=" + datasource +
                ", historycode=" + historycode +
                ", historydepartmentid=" + historydepartmentid +
                ", writtenbydept=" + writtenbydept +
                ", pkymorgid=" + pkymorgid +
                "}";
    }
}
