package com.huabo.monitor.oracle.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Table(name = "TBL_STAFF")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="用户TBL_STAFF对象", description="")
public class TblStaff implements Serializable {
	public final static Integer USER_DISABLE=0;//禁用
	public final static Integer USER_ENBLE=1;//启用
	
	public final static String REGISTERUSERPASSWORD = "REDACTED";
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")  
	@TableId("STAFFID")
	@ApiModelProperty(value = "主键ID,自动增长")
	private BigDecimal staffid;//主键ID,自动增长
	@TableField("REALNAME")
	@Column(name = "REALNAME")
	@ApiModelProperty(value = "真实名字")
	private String realname;//真实名字
	@TableField("FIXEDPHONE")
	@Column(name = "FIXEDPHONE")
	@ApiModelProperty(value = "固定电话")
	private String fixedphone;//固定电话
	@TableField("ADDRESS")
	@ApiModelProperty(value = "地址")
	private String address;//地址
	@TableField("EMAIL")
	@ApiModelProperty(value = "邮箱")
	private String email;//邮箱
	@TableField("MIBLEPHONE")
	@Column(name = "MIBLEPHONE")
	@ApiModelProperty(value = "手机号码")
	private String miblephone;//手机号码
	@TableField("MEMO")
	@Column(name = "MEMO")
	@ApiModelProperty(value = "备注")
	private String memo;//备注
	@TableField("USERNAME")
	@ApiModelProperty(value = "用户名（登录名）")
	private String username;//用户名（登录名）
	@TableField("PASSWORD")
	@ApiModelProperty(value = "密码")
	private String password;//密码
	@TableField("JOBID")
	@ApiModelProperty(value = "岗位ID")
	private BigDecimal jobid;//岗位ID
	@TableField("CREATETIME")
	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "创建时间")
	private Date createDate;
	@TableField("STATUS")
	@ApiModelProperty(value = "状态（1启用，0弃用）")
	private Integer status;//状态（1启用，0弃用）
	@TableField("ORGID")
	@ApiModelProperty(value = "组织Id")
	private BigDecimal orgid;//组织Id
	@TableField("ROLEID")//
	@ApiModelProperty(value = "角色id,已作废")
	private BigDecimal roleid;
	@TableField("OUTSIDEID")
	@ApiModelProperty(value = "标识用户来源 为null是本系统，1：蜂信，2,蜂信购买后的用户   3：华博云系统注册用户管理员  以后可能为2,3...来表示其它来源")
	private Integer outSideId; //标识用户来源 为null是本系统，1：蜂信，2,蜂信购买后的用户   3：华博云系统注册用户管理员  以后可能为2,3...来表示其它来源
	@TableField("OUTSIDEOPENID")
	@ApiModelProperty(value = "外部同步企业来源Id")
	private String outSideOpenId; //外部同步企业来源Id

	@Column(name="ROLEIDSTRS")
	@ApiModelProperty(value = "多个角色主键，用逗号分割 示例：1,2,3,4")
	private String roleIdStrs;
	
	@Column(name="PKYMSTAFFID")
	@TableField("PKYMSTAFFID")
	@ApiModelProperty(value = "低代码平台主键ID",hidden=true)
	private String pkYmStaffId;
	
	@TableField("BIRTHDAY")
	@Column(name = "BIRTHDAY")
	@ApiModelProperty(value = "出生年月")
	private Date birthday;
	
	@TableField("POLITICALOUTLOOK")
	@Column(name = "POLITICALOUTLOOK")
	@ApiModelProperty(value = "政治面貌")
	private String politicaloutlook;

	@TableField("EDUCATION")
	@Column(name = "EDUCATION")
	@ApiModelProperty(value = "学历")
	private String education;
	
	@TableField("MAJOR")
	@Column(name = "MAJOR")
	@ApiModelProperty(value = "专业")
	private String major;
 
	@TableField("SCHOOL")
	@Column(name = "SCHOOL")
	@ApiModelProperty(value = "毕业院校")
	private String school;
	 
	@TableField("OFFICEPHONE")
	@Column(name = "OFFICEPHONE")
	@ApiModelProperty(value = "办公电话")
	private String officephone;
	 
	@TableField("WORKTIME")
	@Column(name = "WORKTIME")
	@ApiModelProperty(value = "参加工作时间")
	private Date worktime;
	 
	@TableField("TITLE")
	@Column(name = "TITLE")
	@ApiModelProperty(value = "职称")
	private String title;
 
	@TableField("QUALIFICATION")
	@Column(name = "QUALIFICATION")
	@ApiModelProperty(value = "执业资格")
	private String qualification;
	
	@TableField("SITUATION")
	@Column(name = "SITUATION")
	@ApiModelProperty(value = "参加审计/检查情况")
	private String situation;
	
	@TableField("ENTRYTIME")
	@Column(name = "ENTRYTIME")
	@ApiModelProperty(value = "入职时间")
	private Date entrytime;
	
	
	@TableField("RESIGNATIONTIME")
	@Column(name = "RESIGNATIONTIME")
	@ApiModelProperty(value = "离职时间")
	private Date resignationtime;
	
	@TableField("JOBEXPERIENCES")
	@Column(name = "JOBEXPERIENCES")
	@ApiModelProperty(value = "主要工作经历 ")
	private String jobexperiences;
	
	@TableField("AUDITORTYPE")
	@Column(name = "AUDITORTYPE")
	@ApiModelProperty(value = "审计人员类型 ")
	private String auditortype;
	
	@TableField("TYPE")
	@Column(name = "TYPE")
	@ApiModelProperty(value = "全职/兼职 ")
	private String type;
	
	@TableField("ISAUDIT")
	@Column(name = "ISAUDIT")
	@ApiModelProperty(value = "是否是审计人员 ")
	private String isAudit;
	
	
	@TableField("GENDER")
	@Column(name = "GENDER")
	@ApiModelProperty(value = "性别 ")
	private String gender;
	 
	
	@TableField("HISTORYCODE")
	private String historycode;
	
	@TableField("HISTORYDEPARTMENTID")
	private String historydepartmentid;
	
	@TableField("DATASOURCE")
	private String dataSource;
	
	
	
	@Transient
	private String jobName;
	@Transient
	private String orgFatherName;
	@Transient
	private String rn;
	@Transient
	private String checked;
	@Transient
	@ApiModelProperty(value = "角色名，用逗号分隔")
	private String roleNames;
	
	@Transient
	@ApiModelProperty(value = "公司名称")
	private String orgname;

	@Transient
	private TblRole  trole;
	@Transient
	private TblOrganization linkDetp;//用户隶属的部门
	@Transient
	private TblOrganization currentOrg;//用户当前所在的公司
	@Transient
	private TblOrganization linkOrg;//用户隶属的公司
	@Transient
	private TblLoginType loginType;//登录页面信息
	@Transient
	private TblOrganization tblOrganization;

	@Transient
	private TblExternalExpert tblExternalExpert;

//	@JSONField(
//			serialize = false
//	)
//	@Transient
//	private Set<TblManageRight> tblManageRights = new HashSet(0);
//
//	@JSONField(serialize = false)
	public TblOrganization getTblOrganization() {
		return this.tblOrganization;
	}

	@JSONField(serialize = false)
	private Set<TblManageRight> tblManageRights = new HashSet(0);
	
	@Transient
	private TblRole tblRole;//角色
}
