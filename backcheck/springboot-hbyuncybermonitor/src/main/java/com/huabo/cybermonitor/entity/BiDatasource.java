package com.huabo.cybermonitor.entity;

import cc.aicode.e2e.annotation.ExcelEntity;
import cc.aicode.e2e.annotation.ExcelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@ExcelEntity
public class BiDatasource implements java.io.Serializable {
	/**
	 * @author tyb
	 * @date 2016-1-24 下午3:57:49
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal dsid;
	@ExcelProperty(value="名称",required=true)
	private String dsname;
	private String dstype;
	@ExcelProperty(value="SQL",required=true)
	private String sqlstring;
	private Organization tblOrganization;
	private Staff tblStaff;
	private String memo1;
	private String memo2;
	//private String creater;
	private Date createdate;
	@ExcelProperty(value="状态",required=true)
	private BigDecimal forbidden;
	private BigDecimal fatherid;
	private String accountunit;
	private Set tblBiCharts = new HashSet(0);
	private String isleaf; //数据库字段为空：是以左侧菜单展示     如果等于1：是以列表页展示
	private String fatherName;
	@ExcelProperty(value="描述",required=true)
	private String dsdes;
	private Set<BiDatasource> childrens=new HashSet<BiDatasource>();
	public BiDatasource() {
	}

	/** full constructor */
	public BiDatasource(String dsname, String dstype, String sqlstring,
                        String memo1, String memo2,
                        Date createdate, BigDecimal forbidden, String accountunit,
                        Set tblBiCharts) {
		this.dsname = dsname;
		this.dstype = dstype;
		this.sqlstring = sqlstring;
		this.memo1 = memo1;
		this.memo2 = memo2;
		this.createdate = createdate;
		this.forbidden = forbidden;
		this.accountunit = accountunit;
		this.tblBiCharts = tblBiCharts;
	}

	// Property accessors

	public BigDecimal getDsid() {
		return this.dsid;
	}

	public void setDsid(BigDecimal dsid) {
		this.dsid = dsid;
	}

	public String getDsname() {
		return this.dsname;
	}

	public void setDsname(String dsname) {
		this.dsname = dsname;
	}

	public String getDstype() {
		return this.dstype;
	}

	public void setDstype(String dstype) {
		this.dstype = dstype;
	}

	public String getSqlstring() {
		return this.sqlstring;
	}

	public void setSqlstring(String sqlstring) {
		this.sqlstring = sqlstring;
	}

	

	public String getMemo1() {
		return this.memo1;
	}

	public void setMemo1(String memo1) {
		this.memo1 = memo1;
	}

	public String getMemo2() {
		return this.memo2;
	}

	public void setMemo2(String memo2) {
		this.memo2 = memo2;
	}

	

	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public BigDecimal getForbidden() {
		return this.forbidden;
	}

	public void setForbidden(BigDecimal forbidden) {
		this.forbidden = forbidden;
	}

	public String getAccountunit() {
		return this.accountunit;
	}

	public void setAccountunit(String accountunit) {
		this.accountunit = accountunit;
	}

	public Set getTblBiCharts() {
		return this.tblBiCharts;
	}

	public void setTblBiCharts(Set tblBiCharts) {
		this.tblBiCharts = tblBiCharts;
	}

	public String getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(String isleaf) {
		this.isleaf = isleaf;
	}

	public Organization getTblOrganization() {
		return tblOrganization;
	}

	public void setTblOrganization(Organization tblOrganization) {
		this.tblOrganization = tblOrganization;
	}

	public Staff getTblStaff() {
		return tblStaff;
	}

	public void setTblStaff(Staff tblStaff) {
		this.tblStaff = tblStaff;
	}

	public BigDecimal getFatherid() {
		return fatherid;
	}

	public void setFatherid(BigDecimal fatherid) {
		this.fatherid = fatherid;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public Set<BiDatasource> getChildrens() {
		return childrens;
	}

	public void setChildrens(Set<BiDatasource> childrens) {
		this.childrens = childrens;
	}

	public String getDsdes() {
		return dsdes;
	}

	public void setDsdes(String dsdes) {
		this.dsdes = dsdes;
	}

}