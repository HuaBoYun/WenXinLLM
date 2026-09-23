package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author kangjx
 * @since 2022-07-15
 */
@TableName("TBL_ATTACHMENT")
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id 自增
     */
    @TableId
    private BigDecimal attid;

    /**
     * 附件名称
     */
    private String attname;

    /**
     * 附件路径
     */
    private String attpath;

    /**
     * 附件大小
     */
    private BigDecimal attsize;

    /**
     * 备注
     */
    private String memo;

    /**
     * 上传时间
     */
    private LocalDateTime uploadtime;

    /**
     * 上传人
     */
    private String uploader;

    /**
     * 是否是python爬取文件 0是
     */
    private String ispythonflag;

    public BigDecimal getAttid() {
        return attid;
    }

    public void setAttid(BigDecimal attid) {
        this.attid = attid;
    }
    public String getAttname() {
        return attname;
    }

    public void setAttname(String attname) {
        this.attname = attname;
    }
    public String getAttpath() {
        return attpath;
    }

    public void setAttpath(String attpath) {
        this.attpath = attpath;
    }
    public BigDecimal getAttsize() {
        return attsize;
    }

    public void setAttsize(BigDecimal attsize) {
        this.attsize = attsize;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public LocalDateTime getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(LocalDateTime uploadtime) {
        this.uploadtime = uploadtime;
    }
    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }
    public String getIspythonflag() {
        return ispythonflag;
    }

    public void setIspythonflag(String ispythonflag) {
        this.ispythonflag = ispythonflag;
    }

    @Override
    public String toString() {
        return "Attachment{" +
            "attid=" + attid +
            ", attname=" + attname +
            ", attpath=" + attpath +
            ", attsize=" + attsize +
            ", memo=" + memo +
            ", uploadtime=" + uploadtime +
            ", uploader=" + uploader +
            ", ispythonflag=" + ispythonflag +
        "}";
    }
}
