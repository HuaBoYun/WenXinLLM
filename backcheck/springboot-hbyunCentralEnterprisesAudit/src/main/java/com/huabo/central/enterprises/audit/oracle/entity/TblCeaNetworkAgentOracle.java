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
 * 央企内审-综合管理-外网代理服务
 */
@Schema(name="TblCeaNetworkAgentOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_NETWORK_AGENT")
public class TblCeaNetworkAgentOracle implements Serializable {
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
	 * IP地址
	 */
	@Column(name = "IPADDRESS")
	@Schema(name = "IP地址")
	private String ipAddress;

	/**
	 * MAC地址
	 */
	@Column(name = "MACADDRESS")
	@Schema(name = "MAC地址")
	private String macAddress;

	/**
	 * 部门名称
	 */
	@Column(name = "DEPARTMENTSNAME")
	@Schema(name = "部门名称")
	private String departmentsName;

	/**
	 * 使用人
	 */
	@Column(name = "USEPEOPLE")
	@Schema(name = "使用人")
	private String usePeople;

	/**
	 * 联系电话
	 */
	@Column(name = "CONTACTPHONE")
	@Schema(name = "联系电话")
	private String contactPhone;

	/**
	 * 电子邮件
	 */
	@Column(name = "MAILADDRESS")
	@Schema(name = "电子邮件")
	private String mailAddress;

	/**
	 * 杀毒软件
	 */
	@Column(name = "ANTIVIRUSSOFTWARE")
	@Schema(name = "杀毒软件")
	private String antivirusSoftware;

	/**
	 * 杀毒厂商
	 */
	@Column(name = "ANTIVIRUSMANUFACTURER")
	@Schema(name = "杀毒厂商")
	private String antivirusManufacturer;

	@Transient
	@Schema(name = "IP清单-地址")
	private String networkInterface;

	@Column(name = "IPINVENTORYID")
	@Schema(name = "关联ID-IP清单ID")
	private Long ipInventoryId;

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

	public static TblCeaNetworkAgentOracle ofId(Long id) {
		TblCeaNetworkAgentOracle tblCeaNetworkAgentOracle = new TblCeaNetworkAgentOracle();
		tblCeaNetworkAgentOracle.setId(id);
		return tblCeaNetworkAgentOracle;
	}
}