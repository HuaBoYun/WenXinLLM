package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-08-29
 */
@TableName("TBL_ATTACHMENT")
@Schema(name="TblAttachment对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="主键Id 自增")

    @TableId(type= IdType.INPUT)
    private BigDecimal attid;

    @Schema(name="附件名称")
    private String attname;

    @Schema(name="附件路径")
    private String attpath;

    @Schema(name="附件大小")
    private BigDecimal attsize;

    @Schema(name="备注")
    private String memo;

    @Schema(name="上传时间")
    private LocalDateTime uploadtime;

    @Schema(name="上传人")
    private String uploader;

    @Schema(name="是否是python爬取文件 0是")
    private String ispythonflag;
    
    @TableField(exist=false)
    private Set tblBugs = new HashSet(0);

    public Set getTblBugs() {
		return tblBugs;
	}

	public void setTblBugs(Set tblBugs) {
		this.tblBugs = tblBugs;
	}

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
        return "TblAttachment{" +
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
