package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@TableName("TBL_STAFF")
@Schema(name="TblStaff对象", description="用户表")
public class TblStaff implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value="STAFFID",type= IdType.INPUT)
	@TableField("STAFFID")
	@Schema(name = "ID")
	private BigDecimal staffid;

	@TableField("REALNAME")
	@Schema(name = "用户真实姓名")
	private String realname;

	@TableField("FIXEDPHONE")
	@Schema(name = "手机号")
	private String fixedphone;

	@TableField("ADDRESS")
	@Schema(name = "地址")
	private String address;

	@TableField("EMAIL")
	@Schema(name = "邮箱")
	private String email;

	@TableField("MIBLEPHONE")
	@Schema(name = "移动手机号")
	private String miblephone;

	
	@TableField("MEMO")
	@Schema(name = "备注")
	private String memo;

	
	@TableField("ORGID")
	@Schema(name = "部门")
	private BigDecimal orgid;

	
	@TableField("USERNAME")
	@Schema(name = "登录名称")
	private String username;

	@TableField("PASSWORD")
	@Schema(name = "密码")
	private String password;

	@TableField("STATUS")
	@Schema(name = "状态")
	private BigDecimal status;

	@TableField("ROLEID")
	@Schema(name = "角色id")
	private BigDecimal roleid;

	@Schema(name="入司时间")
	@TableField("CREATETIME")
	private LocalDateTime createtime;

	@Schema
	@TableField("JOBID")
	private BigDecimal jobid;
	
	@TableField("EMAILCODE")
	private String emailcode;

	@Schema(name="密码修改时间")
	@TableField("UPDATETIME")
	private LocalDateTime updatetime;

	@TableField("DATASOURCE")
	@Schema(name="数据来源;")
	private String datasource;

	@Schema(name="历史id值;")
	@TableField("HISTORYCODE")
	private String historycode;

	@TableField("HISTORYDEPARTMENTID")
	private String historydepartmentid;

	@Schema(name="主管部门ids")
	@TableField("MANAGEORGS")
	private String manageorgs;

	@TableField("MANAGEORGNAMES")
	private String manageorgnames;

	@TableField("ROLEIDSTRS")
	private String roleidstrs;

	@TableField("PKYMSTAFFID")
	private String pkymstaffid;

	@Schema(name="出生年月")
	@TableField("BIRTHDAY")
	private LocalDateTime birthday;

	@Schema(name="政治面貌 ")
	@TableField("POLITICALOUTLOOK")
	private String politicaloutlook;

	@Schema(name="学历")
	@TableField("EDUCATION")
	private String education;

	@Schema(name="专业")
	@TableField("MAJOR")
	private String major;

	@Schema(name="毕业院校")
	@TableField("SCHOOL")
	private String school;

	@Schema(name="办公电话")
	@TableField("OFFICEPHONE")
	private String officephone;

	@Schema(name="参加工作时间")
	@TableField("WORKTIME")
	private Date worktime;

	@Schema(name="职称")
	@TableField("TITLE")
	private String title;

	@Schema(name="执业资格")
	@TableField("QUALIFICATION")
	private String qualification;

	@Schema(name="参加审计/检查情况")
	@TableField("SITUATION")
	private String situation;

	@Schema(name="入职时间")
	@TableField("ENTRYTIME")
	private Date entrytime;

	@Schema(name="离职时间")
	@TableField("RESIGNATIONTIME")
	private Date resignationtime;

	@Schema(name="主要工作经历 ")
	@TableField("JOBEXPERIENCES")
	private String jobexperiences;

	@Schema(name="审计人员类型")
	@TableField("AUDITORTYPE")
	private String auditortype;

	@Schema(name="审计岗位人员（专职、兼职）")
	@TableField("TYPE")
	private String type;

	@TableField("GENDER")
	private String gender;

	@TableField("FGORGS")
	private String fgorgs;

	@TableField("FGORGNAMES")
	private String fgorgnames;

	@TableField("PERSONTYPE")
	private String persontype;

	@TableField("APRSTATUS")
	private BigDecimal aprstatus;
	
	@TableField(exist=false)
	private Date createDate;
	
	@Schema(name="标识用户来源 为null是本系统，1：蜂信，2,蜂信购买后的用户   3：华博云系统注册用户管理员  以后可能为2,3...来表示其它来源")
	private Integer outsideid; //
	@Schema(name="外部同步企业来源Id")
	private String outsideopendid; //
	 
	@Schema(name="是否是审计人员")
	@TableField("ISAUDIT")
	private String isaudit;
	
 

	public TblOrganization getCurrentOrg() {
		return currentOrg;
	}

	public void setCurrentOrg(TblOrganization currentOrg) {
		this.currentOrg = currentOrg;
	}

    @TableField(exist=false)
	private TblOrganization currentOrg;//用户当前所在的公司
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
  
	public BigDecimal getStaffid() {
		return staffid;
	}

	public void setStaffid(BigDecimal staffid) {
		this.staffid = staffid;
	}
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getFixedphone() {
		return fixedphone;
	}

	public void setFixedphone(String fixedphone) {
		this.fixedphone = fixedphone;
	}
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getMiblephone() {
		return miblephone;
	}

	public void setMiblephone(String miblephone) {
		this.miblephone = miblephone;
	}
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	public BigDecimal getOrgid() {
		return orgid;
	}

	public void setOrgid(BigDecimal orgid) {
		this.orgid = orgid;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public BigDecimal getStatus() {
		return status;
	}

	public void setStatus(BigDecimal status) {
		this.status = status;
	}
	public BigDecimal getRoleid() {
		return roleid;
	}

	public void setRoleid(BigDecimal roleid) {
		this.roleid = roleid;
	}
	public LocalDateTime getCreatetime() {
		return createtime;
	}

	public void setCreatetime(LocalDateTime createtime) {
		this.createtime = createtime;
	}
	public BigDecimal getJobid() {
		return jobid;
	}

	public void setJobid(BigDecimal jobid) {
		this.jobid = jobid;
	}
 

	public void setOutsideopendid(String outsideopendid) {
		this.outsideopendid = outsideopendid;
	}
	public String getEmailcode() {
		return emailcode;
	}

	public void setEmailcode(String emailcode) {
		this.emailcode = emailcode;
	}
	public LocalDateTime getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(LocalDateTime updatetime) {
		this.updatetime = updatetime;
	}
	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
	public String getHistorycode() {
		return historycode;
	}

	public void setHistorycode(String historycode) {
		this.historycode = historycode;
	}
	public String getHistorydepartmentid() {
		return historydepartmentid;
	}

	public void setHistorydepartmentid(String historydepartmentid) {
		this.historydepartmentid = historydepartmentid;
	}
	public String getManageorgs() {
		return manageorgs;
	}

	public void setManageorgs(String manageorgs) {
		this.manageorgs = manageorgs;
	}
	public String getManageorgnames() {
		return manageorgnames;
	}

	public void setManageorgnames(String manageorgnames) {
		this.manageorgnames = manageorgnames;
	}
	public String getRoleidstrs() {
		return roleidstrs;
	}

	public void setRoleidstrs(String roleidstrs) {
		this.roleidstrs = roleidstrs;
	}
	public String getPkymstaffid() {
		return pkymstaffid;
	}

	public void setPkymstaffid(String pkymstaffid) {
		this.pkymstaffid = pkymstaffid;
	}
	public LocalDateTime getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDateTime birthday) {
		this.birthday = birthday;
	}
	public String getPoliticaloutlook() {
		return politicaloutlook;
	}

	public void setPoliticaloutlook(String politicaloutlook) {
		this.politicaloutlook = politicaloutlook;
	}
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}
	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}
	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}
	public String getOfficephone() {
		return officephone;
	}

	public void setOfficephone(String officephone) {
		this.officephone = officephone;
	}
	public Date getWorktime() {
		return worktime;
	}

	public void setWorktime(Date worktime) {
		this.worktime = worktime;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public String getSituation() {
		return situation;
	}

	public void setSituation(String situation) {
		this.situation = situation;
	}
	public Date getEntrytime() {
		return entrytime;
	}

	public void setEntrytime(Date entrytime) {
		this.entrytime = entrytime;
	}
	public Date getResignationtime() {
		return resignationtime;
	}

	public void setResignationtime(Date resignationtime) {
		this.resignationtime = resignationtime;
	}
	public String getJobexperiences() {
		return jobexperiences;
	}

	public void setJobexperiences(String jobexperiences) {
		this.jobexperiences = jobexperiences;
	}
	public String getAuditortype() {
		return auditortype;
	}

	public void setAuditortype(String auditortype) {
		this.auditortype = auditortype;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
 
	public String getFgorgs() {
		return fgorgs;
	}

	public void setFgorgs(String fgorgs) {
		this.fgorgs = fgorgs;
	}
	public String getFgorgnames() {
		return fgorgnames;
	}

	public void setFgorgnames(String fgorgnames) {
		this.fgorgnames = fgorgnames;
	}
	public String getPersontype() {
		return persontype;
	}

	public void setPersontype(String persontype) {
		this.persontype = persontype;
	}
	public BigDecimal getAprstatus() {
		return aprstatus;
	}

	public void setAprstatus(BigDecimal aprstatus) {
		this.aprstatus = aprstatus;
	}

	public Integer getOutsideid() {
		return outsideid;
	}

	public void setOutsideid(Integer outsideid) {
		this.outsideid = outsideid;
	}

 
	public String getIsaudit() {
		return isaudit;
	}

	public void setIsaudit(String isaudit) {
		this.isaudit = isaudit;
	}

	public String getOutsideopendid() {
		return outsideopendid;
	}

	@Override
	public String toString() {
		return "TblStaff{" +
				"staffid=" + staffid +
				", realname=" + realname +
				", fixedphone=" + fixedphone +
				", address=" + address +
				", email=" + email +
				", miblephone=" + miblephone +
				", memo=" + memo +
				", orgid=" + orgid +
				", username=" + username +
				", password="REDACTED", status=" + status +
				", roleid=" + roleid +
				", createtime=" + createtime +
				", jobid=" + jobid +
				", outsideid=" + outsideid +
				", outsideopendid=" + outsideopendid +
				", emailcode=" + emailcode +
				", updatetime=" + updatetime +
				", datasource=" + datasource +
				", historycode=" + historycode +
				", historydepartmentid=" + historydepartmentid +
				", manageorgs=" + manageorgs +
				", manageorgnames=" + manageorgnames +
				", roleidstrs=" + roleidstrs +
				", pkymstaffid=" + pkymstaffid +
				", birthday=" + birthday +
				", politicaloutlook=" + politicaloutlook +
				", education=" + education +
				", major=" + major +
				", school=" + school +
				", officephone=" + officephone +
				", worktime=" + worktime +
				", title=" + title +
				", qualification=" + qualification +
				", situation=" + situation +
				", entrytime=" + entrytime +
				", resignationtime=" + resignationtime +
				", jobexperiences=" + jobexperiences +
				", auditortype=" + auditortype +
				", type=" + type +
				", gender=" + gender +
				", isaudit=" + isaudit +
				", fgorgs=" + fgorgs +
				", fgorgnames=" + fgorgnames +
				", persontype=" + persontype +
				", aprstatus=" + aprstatus +
				"}";
	}
}
