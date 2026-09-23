package com.huabo.central.enterprises.audit.oracle.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 央企内审-综合管理-VPN账号管理
 */
@Schema(name="TblCeaVpnMgtOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_VPN_MGT")
public class TblCeaVpnMgtOracle implements Serializable {
	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "主键ID")
	private Long id;

	/**
	 * 人员ID
	 */
	@Column(name = "STAFFID")
	@Schema(name = "人员ID")
	private Long staffId;

	@Transient
	@Schema(name = "用户信息")
	private TblStaffOracle staff;

	@Column(name = "NO")
	@Schema(name = "编号")
	private String no;

	@Column(name = "THISBELONGGROUP")
	@Schema(name = "所在单位")
	private Long thisBelongGroup;

	@Transient
	@Schema(name = "所在单位名称")
	private String thisBelongGroupName;

	@Column(name = "EFFECTIVESTARTTIME")
	@Schema(name = "有效开始时间 格式:yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date effectiveStartTime;

	@Column(name = "EFFECTIVEENDTIME")
	@Schema(name = "有效结束时间 格式:yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date effectiveEndTime;

	/**
	 * 岗位
	 */
	@Column(name = "POST")
	@Schema(name = "岗位")
	private String post;

	/**
	 * 邮箱
	 */
	@Column(name = "MAIL")
	@Schema(name = "邮箱")
	private String mail;

	/**
	 * 手机号码
	 */
	@Column(name = "MOBILEPHONE")
	@Schema(name = "手机号码")
	private String mobilePhone;

	/**
	 * 员工编号
	 */
	@Column(name = "STAFFNUMBER")
	@Schema(name = "员工编号")
	private String staffNumber;

	/**
	 * 服务类型
	 */
	@Column(name = "SERVICETYPE")
	@Schema(name = "服务类型")
	private String serviceType;

	/**
	 * 申请日期
	 */
	@Column(name = "APPLYTIME")
	@Schema(name = "申请日期 yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date applyTime;

	/**
	 * 备注
	 */
	@Column(name = "REMARK")
	@Schema(name = "备注")
	private String remark;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	@Schema(name="状态",hidden=true)
	private Integer state;

	/**
	 * 创建人ID
	 */
	@Column(name = "CREATOR")
	@Schema(name="创建人ID",hidden=true)
	private Long creator;

	@Transient
	@Schema(name="创建人ID名称",hidden=true)
	private String creatorName;

	/**
	 * 工作单位ID
	 */
	@Column(name = "WORKUNIT")
	@Schema(name="工作单位ID",hidden=true)
	private Long workUnit;

	/**
	 * 所属集团ID
	 */
	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团ID",hidden=true)
	private Long belongGroup;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATEDTIME")
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	/**
	 * 更新时间
	 */
	@Column(name = "UPDATEDTIME")
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	private static final long serialVersionUID = 1L;

	public static TblCeaVpnMgtOracle ofId(Long id) {
		TblCeaVpnMgtOracle tblCeaVpnMgtOracle = new TblCeaVpnMgtOracle();
		tblCeaVpnMgtOracle.setId(id);
		return tblCeaVpnMgtOracle;
	}
}