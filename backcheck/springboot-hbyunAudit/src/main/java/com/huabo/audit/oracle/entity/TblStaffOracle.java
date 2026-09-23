package com.huabo.audit.oracle.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户表
 */
@Schema(name="TblStaff")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_staff")
public class TblStaffOracle implements Serializable {

	@Id
	@Column(name = "STAFFID")
	//@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@Schema(name = "主键ID,自动增长")
	private BigDecimal staffId;

	@Column(name = "REALNAME")
	@Schema(name = "真实名字")
	private String realName;

	@Column(name = "FIXEDPHONE")
	@Schema(name = "固定电话")
	private String fixedPhone;

	@Column(name = "ADDRESS")
	@Schema(name = "地址")
	private String address;

	@Column(name = "EMAIL")
	@Schema(name = "邮箱")
	private String email;

	@Column(name = "MIBLEPHONE")
	@Schema(name = "手机号码")
	private String miblePhone;

	@Column(name = "MEMO")
	@Schema(name = "备注")
	private String memo;

	@Column(name = "ORGID")
	@Schema(name = "组织Id")
	private BigDecimal orgId;

	@Column(name = "USERNAME")
	@Schema(name = "用户名（登录名）")
	private String userName;

	@Column(name = "PASSWORD")
	@Schema(name = "密码")
	private String password;

	@Column(name = "STATUS")
	@Schema(name = "状态（1启用，0弃用）")
	private Integer status;

	@Column(name = "ROLEID")
	@Schema
	private BigDecimal roleId;

	@Column(name = "CREATETIME")
	@Schema(name = "入司时间")
	private Date createTime;

	@Column(name = "JOBID")
	@Schema(name = "岗位ID")
	private BigDecimal jobId;

	@Column(name = "OUTSIDEID")
	@Schema(name = "标识用户来源 为null是本系统，1：蜂信，2,蜂信购买后的用户   3：华博云系统注册用户管理员  以后可能为2,3...来表示其它来源")
	private Integer outsideId;

	@Column(name = "OUTSIDEOPENDID")
	@Schema
	private String outsideopendId;

	@Column(name = "EMAILCODE")
	@Schema
	private String emailCode;

	@Column(name = "UPDATETIME")
	@Schema(name = "密码修改时间")
	private Date updateTime;

	@Column(name = "DATASOURCE")
	@Schema(name = "数据来源;")
	private String datasource;

	@Column(name = "HISTORYCODE")
	@Schema(name = "历史id值;")
	private String historyCode;

	@Column(name = "HISTORYDEPARTMENTID")
	@Schema
	private String historyDepartmentId;

	@Column(name = "MANAGEORGS")
	@Schema(name = "主管部门ids")
	private String manageOrgs;

	@Column(name = "MANAGEORGNAMES")
	@Schema
	private String manageOrgNames;

	@Column(name = "ROLEIDSTRS")
	@Schema
	private String roleIdsTrs;

	@Column(name = "PKYMSTAFFID")
	@Schema
	private String pkymStaffId;

	private static final long serialVersionUID = 1L;
}
