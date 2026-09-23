package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
@TableName("TBL_CONTRACT_APPENDIXSIGNING")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class ContractAppendixsigning implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id 自增
     */
    @TableId(type = IdType.INPUT)
    private BigDecimal singingid;

    /**
     * 附件名称
     */
    private String singingname;

    /**
     * 附件路径
     */
    private String singingpath;

    /**
     * 附件大小
     */
    private BigDecimal singingsize;

    /**
     * 附件类型 1己方用印   0对方用印
     */
    private BigDecimal singingtype;

    /**
     * 文件状态
     */
    private BigDecimal singingstatus;

    /**
     * PDF文件地址
     */
    private String pdfviewfilepath;

    /**
     * 所属合同
     */
    private BigDecimal constractid;

    /**
     * 上床时间
     */
    private LocalDateTime uploadtime;

    /**
     * 上传人
     */
    private BigDecimal uploader;

    public BigDecimal getSingingid() {
        return singingid;
    }

    public void setSingingid(BigDecimal singingid) {
        this.singingid = singingid;
    }
    public String getSingingname() {
        return singingname;
    }

    public void setSingingname(String singingname) {
        this.singingname = singingname;
    }
    public String getSingingpath() {
        return singingpath;
    }

    public void setSingingpath(String singingpath) {
        this.singingpath = singingpath;
    }
    public BigDecimal getSingingsize() {
        return singingsize;
    }

    public void setSingingsize(BigDecimal singingsize) {
        this.singingsize = singingsize;
    }
    public BigDecimal getSingingtype() {
        return singingtype;
    }

    public void setSingingtype(BigDecimal singingtype) {
        this.singingtype = singingtype;
    }
    public BigDecimal getSingingstatus() {
        return singingstatus;
    }

    public void setSingingstatus(BigDecimal singingstatus) {
        this.singingstatus = singingstatus;
    }
    public String getPdfviewfilepath() {
        return pdfviewfilepath;
    }

    public void setPdfviewfilepath(String pdfviewfilepath) {
        this.pdfviewfilepath = pdfviewfilepath;
    }
    public BigDecimal getConstractid() {
        return constractid;
    }

    public void setConstractid(BigDecimal constractid) {
        this.constractid = constractid;
    }
    public LocalDateTime getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(LocalDateTime uploadtime) {
        this.uploadtime = uploadtime;
    }
    public BigDecimal getUploader() {
        return uploader;
    }

    public void setUploader(BigDecimal uploader) {
        this.uploader = uploader;
    }

    @Override
    public String toString() {
        return "ContractAppendixsigning{" +
            "singingid=" + singingid +
            ", singingname=" + singingname +
            ", singingpath=" + singingpath +
            ", singingsize=" + singingsize +
            ", singingtype=" + singingtype +
            ", singingstatus=" + singingstatus +
            ", pdfviewfilepath=" + pdfviewfilepath +
            ", constractid=" + constractid +
            ", uploadtime=" + uploadtime +
            ", uploader=" + uploader +
        "}";
    }
}
