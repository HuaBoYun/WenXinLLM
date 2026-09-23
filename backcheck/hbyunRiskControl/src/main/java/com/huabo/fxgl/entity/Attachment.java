package com.huabo.fxgl.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hbfk.entity.TblAttachment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
@TableName("TBL_ATTACHMENT")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
@Data
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 主键Id 自增
     */
    @Schema(name="主键Id 自增")
    @TableId(type = IdType.INPUT)
    private BigDecimal attid;

    /**
     * 附件名称
     */
	@Schema(name="附件名称")
    private String attname;


    /**
     * 附件路径
     */
	@Schema(name="附件路径")
    private String attpath;

    /**
     * 附件大小
     */
	@Schema(name="附件大小")
    private BigDecimal attsize;

    /**
     * 备注
     */
	@Schema(name="备注")
    private String memo;

    /**
     * 上传时间
     */
	@Schema(name="上传时间")
    private LocalDateTime uploadtime;

    /**
     * 上传人
     */
	@Schema(name="上传人")
    private String uploader;

    @TableField(exist = false)
    private String contentText;


    /**
     * 附件密级
     */
    @Schema(name="附件密级")
    private BigDecimal attachmentlevel;



    /**
     * 是否是python爬取文件 0是
     */
	@Schema(name="是否是python爬取文件 0是")
    private String ispythonflag;
    @TableField(exist = false)
    private Set tblBugs = new HashSet(0);

    public Set getTblBugs() {
        return tblBugs;
    }

    public void setTblBugs(Set tblBugs) {
        this.tblBugs = tblBugs;
    }

    public TblAttachment toTblAttachment() {
        TblAttachment tblAtt = new TblAttachment();
        tblAtt.setAttid(attid);
        tblAtt.setAttname(attname);
        tblAtt.setAttpath(attpath);
        tblAtt.setFileName(fileName);
        return tblAtt;
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

    public BigDecimal getAttachmentlevel() {
		return attachmentlevel;
	}
    public void setAttachmentlevel(BigDecimal attachmentlevel) {
		this.attachmentlevel = attachmentlevel;
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
                ", attachmentlevel=" + attachmentlevel +
                "}";
    }

	public Attachment(Attachment a) {
		this.fileName = a.fileName;
		this.attname = a.attname;
		this.attpath = a.attpath;
		this.attsize = a.attsize;
		this.memo = a.memo;
		this.ispythonflag = ispythonflag;
        this.uploadtime = a.uploadtime;
	}
	
	public Attachment() {
	}
}
