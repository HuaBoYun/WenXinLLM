package com.financial.sharing.mysql.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户表
 */
@ApiModel(value = "TblStaff")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_staff")
public class TblStaffOracle {

	@Id
	@Column(name = "STAFFID")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@ApiModelProperty(value = "主键ID,自动增长")
	private Long staffId;

	@Column(name = "REALNAME")
	@ApiModelProperty(value = "真实名字")
	private String realName;

	@Column(name = "FIXEDPHONE")
	@ApiModelProperty(value = "固定电话")
	private String fixedPhone;

	@Column(name = "ADDRESS")
	@ApiModelProperty(value = "地址")
	private String address;

	@Column(name = "EMAIL")
	@ApiModelProperty(value = "邮箱")
	private String email;

	@Column(name = "MIBLEPHONE")
	@ApiModelProperty(value = "手机号码")
	private String miblePhone;

	@Column(name = "MEMO")
	@ApiModelProperty(value = "备注")
	private String memo;

	@Column(name = "ORGID")
	@ApiModelProperty(value = "组织Id")
	private Long orgId;

	@Transient
	@ApiModelProperty("部门名称")
	private String workUnitName;

	@Column(name = "USERNAME")
	@ApiModelProperty(value = "用户名（登录名）")
	private String userName;

	@Column(name = "PASSWORD")
	@ApiModelProperty(value = "密码")
	private String password;

	@Column(name = "STATUS")
	@ApiModelProperty(value = "状态（1启用，0弃用）")
	private Integer status;

	@Column(name = "ROLEID")
	@ApiModelProperty(value = "")
	private Long roleId;

	@Column(name = "CREATETIME")
	@ApiModelProperty(value = "入司时间")
	private Date createTime;

	@Column(name = "JOBID")
	@ApiModelProperty(value = "岗位ID")
	private Long jobId;

	@Column(name = "OUTSIDEID")
	@ApiModelProperty(value = "标识用户来源 为null是本系统，1：蜂信，2,蜂信购买后的用户   3：华博云系统注册用户管理员  以后可能为2,3...来表示其它来源")
	private Integer outsideId;

	@Column(name = "OUTSIDEOPENDID")
	@ApiModelProperty(value = "")
	private String outsideopendId;

	@Column(name = "EMAILCODE")
	@ApiModelProperty(value = "")
	private String emailCode;

	@Column(name = "UPDATETIME")
	@ApiModelProperty(value = "密码修改时间")
	private Date updateTime;

	@Column(name = "DATASOURCE")
	@ApiModelProperty(value = "数据来源;")
	private String datasource;

	@Column(name = "HISTORYCODE")
	@ApiModelProperty(value = "历史id值;")
	private String historyCode;

	@Column(name = "HISTORYDEPARTMENTID")
	@ApiModelProperty(value = "")
	private String historyDepartmentId;

	@Column(name = "MANAGEORGS")
	@ApiModelProperty(value = "主管部门ids")
	private String manageOrgs;

	@Column(name = "MANAGEORGNAMES")
	@ApiModelProperty(value = "")
	private String manageOrgNames;

	@Column(name = "ROLEIDSTRS")
	@ApiModelProperty(value = "")
	private String roleIdsTrs;

	@Column(name = "PKYMSTAFFID")
	@ApiModelProperty(value = "")
	private String pkymStaffId;

	@ApiModelProperty("在岗状态：1-在岗闲置 2-在岗项目内 3-请假 4-外派")
	@Column(name = "ONDUTYSTATUS")
	private Integer onDutyStatus;

	private static final long serialVersionUID = 1L;
}
