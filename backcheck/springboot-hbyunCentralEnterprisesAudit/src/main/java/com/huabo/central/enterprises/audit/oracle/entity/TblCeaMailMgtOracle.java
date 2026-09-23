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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 央企内审-综合管理-中石油邮箱管理
 */
@Schema(name="TblCeaMailMgtOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_MAIL_MGT")
public class TblCeaMailMgtOracle implements Serializable {

	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "主键ID")
	private Long id;
	
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

	/**
	 * 申请人姓名
	 */
	@Column(name = "APPLYID")
	@Schema(name = "申请人")
	private Long applyId;

	@Transient
	@Schema(name = "用户信息")
	private TblStaffOracle staff;

	/**
	 * 申请人姓名
	 */
	@Column(name = "APPLYBELONGGROUP")
	@Schema(name = "申请人单位")
	private Long applyBelongGroup;

	@Transient
	@Schema(name = "申请人单位")
	private String applyBelongGroupName;

	/**
	 * 员工编号
	 */
	@Column(name = "STAFFCODE")
	@Schema(name = "员工编号")
	private String staffCode;

	/**
	 * 服务类型：开通邮箱、信息变更、注销邮箱
	 */
	@Column(name = "SERVICETYPE")
	@Schema(name = "服务类型：开通邮箱、信息变更、注销邮箱")
	private String serviceType;

	/**
	 * 联系电话
	 */
	@Column(name = "CONTACTPHONE")
	@Schema(name = "联系电话")
	private String contactPhone;

	/**
	 * 邮箱后缀：petrochina.com.cn、cnpc.com.cn
	 */
	@Column(name = "MAILSUFFIX")
	@Schema(name = "邮箱后缀：petrochina.com.cn、cnpc.com.cn")
	private String mailSuffix;

	/**
	 * 首选登录名
	 */
	@Column(name = "LOGINNAME")
	@Schema(name = "首选登录名")
	private String loginName;

	/**
	 * 备选登录名1
	 */
	@Column(name = "LOGINNAMEONE")
	@Schema(name = "备选登录名1")
	private String loginNameOne;

	/**
	 * 备选登录名2
	 */
	@Column(name = "LOGINNAMETWO")
	@Schema(name = "备选登录名2")
	private String loginNameTwo;

	/**
	 * 备选登录名3
	 */
	@Column(name = "LOGINNAMETHREE")
	@Schema(name = "备选登录名3")
	private String loginNameThree;

	/**
	 * 变更类型:密码、组织机构、其他
	 */
	@Column(name = "CHANGETYPE")
	@Schema(name = "变更类型:密码、组织机构、其他")
	private String changeType;

	/**
	 * 申请人邮箱
	 */
	@Column(name = "APPLYMAIL")
	@Schema(name = "申请人邮箱")
	private String applyMail;

	/**
	 * 变更内容
	 */
	@Column(name = "CHANGECONTENT")
	@Schema(name = "变更内容")
	private String changeContent;

	/**
	 * 注销原因
	 */
	@Column(name = "LOGOUTREASON")
	@Schema(name = "注销原因")
	private String logoutReason;

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

	public static TblCeaMailMgtOracle ofId(Long id) {
		TblCeaMailMgtOracle tblCeaMailMgtOracle = new TblCeaMailMgtOracle();
		tblCeaMailMgtOracle.setId(id);
		return tblCeaMailMgtOracle;
	}
}