package com.hbfk.entity;

import java.math.BigDecimal;

public class GeneralEntity {
	private Integer isUseSecrect;
	private boolean isSelf;
	private String companyCol;
	private String deptCol;
	private String createUserCol;
	private String secrectCol;
	private String staffScopeCol ;
	private BigDecimal staffid ;
	private String deptIds;
	private String secrectScopeIds ;
	private Integer   authorityType;
	public Integer getIsUseSecrect() {
		return isUseSecrect;
	}
	public void setIsUseSecrect(Integer isUseSecrect) {
		this.isUseSecrect = isUseSecrect;
	}
 
	public boolean isSelf() {
		return isSelf;
	}
	public void setSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}
	public String getCompanyCol() {
		return companyCol;
	}
	public void setCompanyCol(String companyCol) {
		this.companyCol = companyCol;
	}
	public String getDeptCol() {
		return deptCol;
	}
	public void setDeptCol(String deptCol) {
		this.deptCol = deptCol;
	}
	public String getCreateUserCol() {
		return createUserCol;
	}
	public void setCreateUserCol(String createUserCol) {
		this.createUserCol = createUserCol;
	}
	public String getSecrectCol() {
		return secrectCol;
	}
	public void setSecrectCol(String secrectCol) {
		this.secrectCol = secrectCol;
	}
	public String getStaffScopeCol() {
		return staffScopeCol;
	}
	public void setStaffScopeCol(String staffScopeCol) {
		this.staffScopeCol = staffScopeCol;
	}
	 
	
	public BigDecimal getStaffid() {
		return staffid;
	}
	public void setStaffid(BigDecimal staffid) {
		this.staffid = staffid;
	}
	public String getDeptIds() {
		return deptIds;
	}
	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}
	public String getSecrectScopeIds() {
		return secrectScopeIds;
	}
	public void setSecrectScopeIds(String secrectScopeIds) {
		this.secrectScopeIds = secrectScopeIds;
	}
	public Integer getAuthorityType() {
		return authorityType;
	}
	public void setAuthorityType(Integer authorityType) {
		this.authorityType = authorityType;
	}
	public GeneralEntity(){}
	
	public GeneralEntity(Integer isUseSecrect, boolean isSelf, String companyCol, String deptCol, String createUserCol,
			String secrectCol, String staffScopeCol, BigDecimal staffid, String deptIds, String secrectScopeIds,
			Integer authorityType) {
		super();
		this.isUseSecrect = isUseSecrect;
		this.isSelf = isSelf;
		this.companyCol = companyCol;
		this.deptCol = deptCol;
		this.createUserCol = createUserCol;
		this.secrectCol = secrectCol;
		this.staffScopeCol = staffScopeCol;
		this.staffid = staffid;
		this.deptIds = deptIds;
		this.secrectScopeIds = secrectScopeIds;
		this.authorityType = authorityType;
	} 
	
	
}
