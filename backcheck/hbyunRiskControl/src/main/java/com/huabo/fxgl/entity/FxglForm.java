package com.huabo.fxgl.entity;

import java.util.Date;

public class FxglForm {
    private Integer formId;
    /**
     * 	创建时间
     */
    private Date createTime;
    /**
     * 审核状态
     * 1 未提交
     * 2 审核中
     * 3 调整
     * 4 完成
     * 5 已上报
     */
    private Integer status;
    /**
     * 创建人
     */
    private String createPerson;
    /**
     * 审批人
     */
    private String examPerson;
    /**
     * 表单名称类型  
     * 1.风险整改填报
     */
    private String formName;
    
    private Integer deletetatus;
    private Integer reviewstatus;
    private Integer lrstatus;
    private Integer zgstatus;
    private Integer scstatus;
    private Integer xgstatus;
    private Integer zgfstatus;
    private Integer scfstatus;
    
    private Integer hdstatus;
    private Integer scestatus;
    private Integer zgestatus;
    
    private Integer selectFirststatus;
    private Integer selectModistatus;
    private Integer selectReviewstatus;
    private Integer selectDeletestatus;
    
	public Integer getFormId() {
		return formId;
	}
	public void setFormId(Integer formId) {
		this.formId = formId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCreatePerson() {
		return createPerson;
	}
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}
	
	public String getExamPerson() {
		return examPerson;
	}
	public void setExamPerson(String examPerson) {
		this.examPerson = examPerson;
	}
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public FxglForm() {
		super();
	}
	public FxglForm(Integer formId, Date createTime, Integer status, String createPerson, String examPerson,
                    String formName) {
		super();
		this.formId = formId;
		this.createTime = createTime;
		this.status = status;
		this.createPerson = createPerson;
		this.examPerson = examPerson;
		this.formName = formName;
	}
	@Override
	public String toString() {
		return "FxglForm [formId=" + formId + ", createTime=" + createTime + ", status=" + status + ", createPerson="
				+ createPerson + ", examPerson=" + examPerson + ", formName=" + formName + "]";
	}
	public Integer getDeletetatus() {
		return deletetatus;
	}
	public void setDeletetatus(Integer deletetatus) {
		this.deletetatus = deletetatus;
	}
	public Integer getReviewstatus() {
		return reviewstatus;
	}
	public void setReviewstatus(Integer reviewstatus) {
		this.reviewstatus = reviewstatus;
	}
	public Integer getLrstatus() {
		return lrstatus;
	}
	public void setLrstatus(Integer lrstatus) {
		this.lrstatus = lrstatus;
	}
	public Integer getZgstatus() {
		return zgstatus;
	}
	public void setZgstatus(Integer zgstatus) {
		this.zgstatus = zgstatus;
	}
	public Integer getScstatus() {
		return scstatus;
	}
	public void setScstatus(Integer scstatus) {
		this.scstatus = scstatus;
	}
	public Integer getXgstatus() {
		return xgstatus;
	}
	public void setXgstatus(Integer xgstatus) {
		this.xgstatus = xgstatus;
	}
	public Integer getZgfstatus() {
		return zgfstatus;
	}
	public void setZgfstatus(Integer zgfstatus) {
		this.zgfstatus = zgfstatus;
	}
	public Integer getScfstatus() {
		return scfstatus;
	}
	public void setScfstatus(Integer scfstatus) {
		this.scfstatus = scfstatus;
	}
	public Integer getHdstatus() {
		return hdstatus;
	}
	public void setHdstatus(Integer hdstatus) {
		this.hdstatus = hdstatus;
	}
	public Integer getScestatus() {
		return scestatus;
	}
	public void setScestatus(Integer scestatus) {
		this.scestatus = scestatus;
	}
	public Integer getZgestatus() {
		return zgestatus;
	}
	public void setZgestatus(Integer zgestatus) {
		this.zgestatus = zgestatus;
	}
	public Integer getSelectFirststatus() {
		return selectFirststatus;
	}
	public void setSelectFirststatus(Integer selectFirststatus) {
		this.selectFirststatus = selectFirststatus;
	}
	public Integer getSelectModistatus() {
		return selectModistatus;
	}
	public void setSelectModistatus(Integer selectModistatus) {
		this.selectModistatus = selectModistatus;
	}
	public Integer getSelectReviewstatus() {
		return selectReviewstatus;
	}
	public void setSelectReviewstatus(Integer selectReviewstatus) {
		this.selectReviewstatus = selectReviewstatus;
	}
	public Integer getSelectDeletestatus() {
		return selectDeletestatus;
	}
	public void setSelectDeletestatus(Integer selectDeletestatus) {
		this.selectDeletestatus = selectDeletestatus;
	}
	
	
    
}
