package com.huabo.financialdata.entity.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 凭证实体类注释
 * 凭证库表
 * TBL_ACC_BKPF
 */
@Schema(name="凭证库表 TBL_ACC_BKPF实体类")
public class TblAccBkpf implements Serializable {



    /**
     * 序列化版本ID，用于保持类在序列化和反序列化过程中的兼容性
     */
    @Schema(name = "序列化版本ID")
    private static final long serialVersionUID = 4845785843385208846L;

    /**
     * HEADTEXT - 凭证抬头文本内容
     */
    @Schema(name = "凭证抬头文本内容")
    private String headText;

    /**
     * PZ_DATE - 凭证日期
     */
    @Schema(name = "凭证日期")
    private Date pzDate;

    /**
     * PZTYPE - 凭证类型
     */
    @Schema(name = "凭证类型")
    private String pztype;

    /**
     * PZH - 凭证编号
     */
    @Schema(name = "凭证编号")
    private String pzh;

    /**
     * FJ - 附件数量
     */
    @Schema(name = "附件数量")
    private String fj;

    /**
     * AYEAR - 会计年度
     */
    @Schema(name = "会计年度")
    private Integer ayear;

    /**
     * AMONTH - 会计期间
     */
    @Schema(name = "会计期间")
    private Integer amonth;

    /**
     * CWZG - 财务主管
     */
    @Schema(name = "财务主管")
    private String cwzg;

    /**
     * JZR - 记账人
     */
    @Schema(name = "记账人")
    private String jzrl;

    /**
     * CNR - 出纳人
     */
    @Schema(name = "出纳人")
    private String cnr;

    /**
     * SHR - 审核人
     */
    @Schema(name = "审核人")
    private String shr;

    /**
     * ZDR - 制单人
     */
    @Schema(name = "制单人")
    private String zdr;

    /**
     * DES - 备注信息字段
     */
    @Schema(name = "备注信息字段")
    private String des;

    /**
     * ISF - 是否外币交易标识
     */
    @Schema(name = "是否外币交易标识")
    private Integer isf;

    /**
     * ISJZ - 是否已记账标识
     */
    @Schema(name = "数据库源")
    private Integer isjz;

    /**
     * GLH - 关联号
     */
    @Schema(name = "关联号")
    private String glh;

    /**
     * startMonth - 开始月份
     */
    @Schema(name = "开始月份")
    private Integer startMonth;

    /**
     * endMonth - 结束月份
     */
    @Schema(name = "结束月份")
    private Integer endMonth;

    /**
     * accId - 账户ID
     */
    @Schema(name = "数据库源")
    private String accId;

    /**
     * accNameOne - 账户名称
     */
    @Schema(name = "账户名称")
    private String accNameOne;

    /**
     * lineText - 行项目文本描述
     */
    @Schema(name = "行项目文本描述")
    private String lineText;

    /**
     *借方
     */
    @Schema(name = "借方")
    private BigDecimal md;

    /**
     *贷方
     */
    @Schema(name = "贷方")
    private BigDecimal mc;




    public BigDecimal getMd() {
        return md;
    }

    public void setMd(BigDecimal md) {
        this.md = md;
    }

    public BigDecimal getMc() {
        return mc;
    }

    public void setMc(BigDecimal mc) {
        this.mc = mc;
    }

    public String getLineText() {
        return lineText;
    }

    public void setLineText(String lineText) {
        this.lineText = lineText;
    }

    public String getAccNameOne() {
        return accNameOne;
    }

    public void setAccNameOne(String accNameOne) {
        this.accNameOne = accNameOne;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public Integer getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(Integer startMonth) {
        this.startMonth = startMonth;
    }

    public Integer getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(Integer endMonth) {
        this.endMonth = endMonth;
    }

    public String getHeadText() {
        return headText;
    }

    public void setHeadText(String headText) {
        this.headText = headText;
    }

    public Date getPzDate() {
        return pzDate;
    }

    public void setPzDate(Date pzDate) {
        this.pzDate = pzDate;
    }

    public String getPztype() {
        return pztype;
    }

    public void setPztype(String pztype) {
        this.pztype = pztype;
    }

    public String getPzh() {
        return pzh;
    }

    public void setPzh(String pzh) {
        this.pzh = pzh;
    }

    public String getFj() {
        return fj;
    }

    public void setFj(String fj) {
        this.fj = fj;
    }

    public Integer getAyear() {
        return ayear;
    }

    public void setAyear(Integer ayear) {
        this.ayear = ayear;
    }

    public Integer getAmonth() {
        return amonth;
    }

    public void setAmonth(Integer amonth) {
        this.amonth = amonth;
    }

    public String getCwzg() {
        return cwzg;
    }

    public void setCwzg(String cwzg) {
        this.cwzg = cwzg;
    }

    public String getJzrl() {
        return jzrl;
    }

    public void setJzrl(String jzrl) {
        this.jzrl = jzrl;
    }

    public String getCnr() {
        return cnr;
    }

    public void setCnr(String cnr) {
        this.cnr = cnr;
    }

    public String getShr() {
        return shr;
    }

    public void setShr(String shr) {
        this.shr = shr;
    }

    public String getZdr() {
        return zdr;
    }

    public void setZdr(String zdr) {
        this.zdr = zdr;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Integer getIsf() {
        return isf;
    }

    public void setIsf(Integer isf) {
        this.isf = isf;
    }

    public Integer getIsjz() {
        return isjz;
    }

    public void setIsjz(Integer isjz) {
        this.isjz = isjz;
    }

    public String getGlh() {
        return glh;
    }

    public void setGlh(String glh) {
        this.glh = glh;
    }


}
