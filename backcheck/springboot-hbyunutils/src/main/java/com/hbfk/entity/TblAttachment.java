package com.hbfk.entity;

import java.math.BigDecimal;
import java.util.Date;

public class TblAttachment implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal attid;
	private String attname;
	private String attpath;
	private double attsize;
	private String memo;
	private Date uploadtime;
	private String uploader;
	private String fileName;
	private String ispythonflag; //法律法规爬虫抓取
    private String contentText;
    private String jmurl;
    private Boolean isEncrypted = true;
    private BigDecimal attachmentlevel;
    
    private String previewUrl;

     
	public String getPreviewUrl() {
		return previewUrl;
	}
	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}
 
	public Boolean getIsEncrypted() {
		return isEncrypted;
	}
	public void setIsEncrypted(Boolean isEncrypted) {
		this.isEncrypted = isEncrypted;
	}
	public BigDecimal getAttachmentlevel() {
		return attachmentlevel;
	}
	public void setAttachmentlevel(BigDecimal attachmentlevel) {
		this.attachmentlevel = attachmentlevel;
	}
 
	public String getJmurl() {
		return jmurl;
	}
	public void setJmurl(String jmurl) {
		this.jmurl = jmurl;
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
	public double getAttsize() {
		return attsize;
	}
	public void setAttsize(double attsize) {
		this.attsize = attsize;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Date getUploadtime() {
		return uploadtime;
	}
	public void setUploadtime(Date uploadtime) {
		this.uploadtime = uploadtime;
	}
	public String getUploader() {
		return uploader;
	}
	public void setUploader(String uploader) {
		this.uploader = uploader;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getIspythonflag() {
		return ispythonflag;
	}
	public void setIspythonflag(String ispythonflag) {
		this.ispythonflag = ispythonflag;
	}
	public String getContentText() {
		return contentText;
	}
	public void setContentText(String contentText) {
		this.contentText = contentText;
	}
}