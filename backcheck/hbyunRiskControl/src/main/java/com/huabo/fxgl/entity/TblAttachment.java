package com.huabo.fxgl.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * TblAttachment entity. @author MyEclipse Persistence Tools
 */

/**
 * ����
 * @author SongXiangYing
 *
 */
public class TblAttachment implements java.io.Serializable {

	// Fields

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
	private Set tblTesttasks = new HashSet(0);
	private Set tblOtherarticles = new HashSet(0);
	private Set tblDoubtfulpoints = new HashSet(0);
	private Set tblNbsjDoubtfulpoints = new HashSet(0);//疑点--审计
	private Set tblWorksheets = new HashSet(0);
	private Set tblProblems = new HashSet(0);
	private Set tblTestplans = new HashSet(0);
	private Set tblBugs = new HashSet(0);
	private Set tblNbsjBugs = new HashSet(0);
	private Set tblManuals = new HashSet(0);
	private Set tblOuterrules = new HashSet(0);
	private Set tblNbsjOuterrules = new HashSet(0);//法律规章--审计
	private Set tblInnerrules = new HashSet(0);
	private Set tblNbsjInnerrules = new HashSet(0);//管理制度--审计


	/** default constructor */
	public TblAttachment() {
	}

	/** full constructor */
	public TblAttachment(String attname, String attpath, double attsize,
                         String memo, Date uploadtime, String uploader, Set tblTesttasks,
                         Set tblOtherarticles, Set tblDoubtfulpoints, Set tblWorksheets,
                         Set tblProblems, Set tblTestplans, Set tblBugs, Set tblManuals,
                         Set tblOuterrules, Set tblInnerrules) {
		this.attname = attname;
		this.attpath = attpath;
		this.attsize = attsize;
		this.memo = memo;
		this.uploadtime = uploadtime;
		this.uploader = uploader;
		this.tblTesttasks = tblTesttasks;
		this.tblOtherarticles = tblOtherarticles;
		this.tblDoubtfulpoints = tblDoubtfulpoints;
		this.tblWorksheets = tblWorksheets;
		this.tblProblems = tblProblems;
		this.tblTestplans = tblTestplans;
		this.tblBugs = tblBugs;
		this.tblManuals = tblManuals;
		this.tblOuterrules = tblOuterrules;
		this.tblInnerrules = tblInnerrules;
		
	}

	
	public String getIspythonflag() {
		return ispythonflag;
	}
	public void setIspythonflag(String ispythonflag) {
		this.ispythonflag = ispythonflag;
	}
	// Property accessors
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public BigDecimal getAttid() {
		return this.attid;
	}

	public void setAttid(BigDecimal attid) {
		this.attid = attid;
	}

	public String getAttname() {
		return this.attname;
	}

	public void setAttname(String attname) {
		this.attname = attname;
	}

	public String getAttpath() {
		return this.attpath;
	}

	public void setAttpath(String attpath) {
		this.attpath = attpath;
	}

	public double getAttsize() {
		return this.attsize;
	}

	public void setAttsize(double attsize) {
		this.attsize = attsize;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getUploadtime() {
		return this.uploadtime;
	}

	public void setUploadtime(Date uploadtime) {
		this.uploadtime = uploadtime;
	}

	public String getUploader() {
		return this.uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public Set getTblTesttasks() {
		return this.tblTesttasks;
	}

	public void setTblTesttasks(Set tblTesttasks) {
		this.tblTesttasks = tblTesttasks;
	}

	public Set getTblOtherarticles() {
		return this.tblOtherarticles;
	}

	public void setTblOtherarticles(Set tblOtherarticles) {
		this.tblOtherarticles = tblOtherarticles;
	}

	public Set getTblDoubtfulpoints() {
		return this.tblDoubtfulpoints;
	}

	public void setTblDoubtfulpoints(Set tblDoubtfulpoints) {
		this.tblDoubtfulpoints = tblDoubtfulpoints;
	}

	public Set getTblWorksheets() {
		return this.tblWorksheets;
	}

	public void setTblWorksheets(Set tblWorksheets) {
		this.tblWorksheets = tblWorksheets;
	}

	public Set getTblProblems() {
		return this.tblProblems;
	}

	public void setTblProblems(Set tblProblems) {
		this.tblProblems = tblProblems;
	}

	public Set getTblTestplans() {
		return this.tblTestplans;
	}

	public void setTblTestplans(Set tblTestplans) {
		this.tblTestplans = tblTestplans;
	}

	public Set getTblBugs() {
		return this.tblBugs;
	}

	public void setTblBugs(Set tblBugs) {
		this.tblBugs = tblBugs;
	}

	public Set getTblManuals() {
		return this.tblManuals;
	}

	public void setTblManuals(Set tblManuals) {
		this.tblManuals = tblManuals;
	}

	public Set getTblOuterrules() {
		return this.tblOuterrules;
	}

	public void setTblOuterrules(Set tblOuterrules) {
		this.tblOuterrules = tblOuterrules;
	}

	public Set getTblInnerrules() {
		return this.tblInnerrules;
	}

	public void setTblInnerrules(Set tblInnerrules) {
		this.tblInnerrules = tblInnerrules;
	}


	
}