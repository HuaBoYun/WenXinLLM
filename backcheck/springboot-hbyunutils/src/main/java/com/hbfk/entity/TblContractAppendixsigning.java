package com.hbfk.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 合同管理，双方签署合同文件附件表
 * @author zero
 *
 */
public class TblContractAppendixsigning implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6235763355378422470L;
	
	private BigDecimal singingId;
	private String singingName;
	private String singingPath;
	private BigDecimal singingSize;
	private Integer singingType;
	private Integer singingStatus;
	private Integer constractId;
	private Date uploadTime;
	private Integer uploader;
	private String pdfViewFilePath;
	
	private String uploaderName;
	
	public BigDecimal getSingingId() {
		return singingId;
	}
	public void setSingingId(BigDecimal singingId) {
		this.singingId = singingId;
	}
	public String getSingingName() {
		return singingName;
	}
	public void setSingingName(String singingName) {
		this.singingName = singingName;
	}
	public String getSingingPath() {
		return singingPath;
	}
	public void setSingingPath(String singingPath) {
		this.singingPath = singingPath;
	}
	public BigDecimal getSingingSize() {
		return singingSize;
	}
	public void setSingingSize(BigDecimal singingSize) {
		this.singingSize = singingSize;
	}
	public Integer getSingingStatus() {
		return singingStatus;
	}
	public void setSingingStatus(Integer singingStatus) {
		this.singingStatus = singingStatus;
	}
	public Integer getConstractId() {
		return constractId;
	}
	public void setConstractId(Integer constractId) {
		this.constractId = constractId;
	}
	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	public Integer getUploader() {
		return uploader;
	}
	public void setUploader(Integer uploader) {
		this.uploader = uploader;
	}
	public Integer getSingingType() {
		return singingType;
	}
	public void setSingingType(Integer singingType) {
		this.singingType = singingType;
	}
	public String getUploaderName() {
		return uploaderName;
	}
	public void setUploaderName(String uploaderName) {
		this.uploaderName = uploaderName;
	}
	public String getPdfViewFilePath() {
		return pdfViewFilePath;
	}
	public void setPdfViewFilePath(String pdfViewFilePath) {
		this.pdfViewFilePath = pdfViewFilePath;
	}
	
	
}
