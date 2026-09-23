package com.huabo.fxgl.entity;

/**
 * 
 * @author Administrator
 *
 */
public class Mainjobinfo {
	private String id;			//主键	
	private String modifiedtime;	//修改时间	
	private String staff_id;	//所属人员	
	private String org_id;		//所属组织	
	private String dept_id;		//所属部门	
	private String job_id;		//职务	
	private String post_id;		//职位	
	private String jobgrade_id;	//职级	
	private String begindate;	//任职开始时间	
	private String enddate;	 	//任职结束时间	
	private String director;	//上级主管	
	private String psncl_id;	//人员类别	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getModifiedtime() {
		return modifiedtime;
	}
	public void setModifiedtime(String modifiedtime) {
		this.modifiedtime = modifiedtime;
	}
	public String getStaff_id() {
		return staff_id;
	}
	public void setStaff_id(String staff_id) {
		this.staff_id = staff_id;
	}
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
	public String getDept_id() {
		return dept_id;
	}
	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}
	public String getJob_id() {
		return job_id;
	}
	public void setJob_id(String job_id) {
		this.job_id = job_id;
	}
	public String getPost_id() {
		return post_id;
	}
	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}
	public String getJobgrade_id() {
		return jobgrade_id;
	}
	public void setJobgrade_id(String jobgrade_id) {
		this.jobgrade_id = jobgrade_id;
	}
	public String getBegindate() {
		return begindate;
	}
	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getPsncl_id() {
		return psncl_id;
	}
	public void setPsncl_id(String psncl_id) {
		this.psncl_id = psncl_id;
	}
	
}
