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
 * 央企内审-综合管理-数字证书管理
 */
@Schema(name="TblCeaDigitalCertificateOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_DIGITAL_CERTIFICATE")
public class TblCeaDigitalCertificateOracle implements Serializable {

	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "主键ID")
	private Long id;

	/**
	 * 服务类型:新动证书、证书冻结、USBKey解锁、证书解冻、证书更新、证书注销、USBKey丢失补办、USBKey损坏补办、授权码更新
	 */
	@Column(name = "SERVICETYPE")
	@Schema(name = "服务类型:新动证书、证书冻结、USBKey解锁、证书解冻、证书更新、证书注销、USBKey丢失补办、USBKey损坏补办、授权码更新")
	private String serviceType;
	
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

	/**
	 * 用户姓名
	 */
	@Column(name = "USERNAME")
	@Schema(name = "用户姓名")
	private String userName;

	/**
	 * 电子邮箱
	 */
	@Column(name = "EMAIL")
	@Schema(name = "电子邮箱")
	private String email;

	/**
	 * 员工编号
	 */
	@Column(name = "STAFFCODE")
	@Schema(name = "员工编号")
	private String staffCode;

	/**
	 * 组织机构名称
	 */
	@Column(name = "ORGANIZATIONNAME")
	@Schema(name = "组织机构名称")
	private String organizationName;

	/**
	 * 组织机构编码
	 */
	@Column(name = "ORGANIZATIONNUM")
	@Schema(name = "组织机构编码")
	private String organizationNum;

	/**
	 * 联系电话
	 */
	@Column(name = "CONTACTPHONE")
	@Schema(name = "联系电话")
	private String contactPhone;

	/**
	 * 备注
	 */
	@Column(name = "REMARK")
	@Schema(name = "备注")
	private String remark;

	/**
	 * 是否代办：0-否 1-是
	 */
	@Column(name = "ISAGENCY")
	@Schema(name = "是否代办：0-否 1-是")
	private Integer isAgency;

	@Column(name = "AGENCYNAME")
	@Schema(name="代办人名称")
	private String agencyName;

	@Column(name = "AGENCYBELONGGROUP")
	@Schema(name="代办人单位")
	private String agencyBelongGroup;

	@Column(name = "AGENCYMAIL")
	@Schema(name="代办人邮箱")
	private String agencyMail;

	@Column(name = "AGENCYCONTACTPHONE")
	@Schema(name="代办人电话")
	private String agencyContactPhone;


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

	public static TblCeaDigitalCertificateOracle ofId(Long id) {
		TblCeaDigitalCertificateOracle tblCeaDigitalCertificateOracle = new TblCeaDigitalCertificateOracle();
		tblCeaDigitalCertificateOracle.setId(id);
		return tblCeaDigitalCertificateOracle;
	}
}