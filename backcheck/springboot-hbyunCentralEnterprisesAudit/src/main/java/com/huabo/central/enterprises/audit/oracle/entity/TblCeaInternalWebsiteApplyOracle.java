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
 * 央企内审-综合管理-内部网站申信息发布
 */
@Schema(name="TblCeaInternalWebsiteApplyOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_INTERNAL_WEBSITE_APPLY")
public class TblCeaInternalWebsiteApplyOracle implements Serializable {
	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@Schema(name = "主键ID")
	@GeneratedValue(generator = "JDBC")
	private Long id;
	
	@Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

	/**
	 * 信息标题
	 */
	@Column(name = "INFOTITLE")
	@Schema(name = "信息标题")
	private String infoTitle;

	/**
	 * 信息提供单位
	 */
	@Column(name = "INFOPROVIDERWORKUNIT")
	@Schema(name = "信息提供单位")
	private Long infoProviderWorkUnit;

	@Transient
	@Schema(name = "信息提供单位名称")
	private String infoProviderWorkUnitName;

	/**
	 * 供单位负责人
	 */
	@Column(name = "PROVIDERPEOPLE")
	@Schema(name = "供单位负责人")
	private Long providerPeople;

	@Transient
	@Schema(name = "供单位负责人名称")
	private String providerPeopleName;

	/**
	 * 联系人
	 */
	@Column(name = "CONTACT")
	@Schema(name = "联系人")
	private Long contact;

	@Transient
	@Schema(name = "联系人名称")
	private String contactName;

	/**
	 * 电话/传真
	 */
	@Column(name = "FAX")
	@Schema(name = "电话/传真")
	private String fax;

	/**
	 * 邮件地址
	 */
	@Column(name = "MAILADDRESS")
	@Schema(name = "邮件地址")
	private String mailAddress;

	/**
	 * 信息发布栏目
	 */
	@Column(name = "INFORELEASECOLUMN")
	@Schema(name = "信息发布栏目")
	private String infoReleaseColumn;

	/**
	 * 预期上网时间
	 */
	@Column(name = "EXPECTONLINETIME")
	@Schema(name = "预期上网时间 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date expectOnlineTime;

	/**
	 * 期失效时间
	 */
	@Column(name = "FAILURETIME")
	@Schema(name = "期失效时间 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date failureTime;

	/**
	 * 内容摘要
	 */
	@Column(name = "CONTENTABSTRACT")
	@Schema(name = "内容摘要")
	private String contentAbstract;

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

	public static TblCeaInternalWebsiteApplyOracle ofId(Long id) {
		TblCeaInternalWebsiteApplyOracle tblCeaInternalWebsiteApplyOracle = new TblCeaInternalWebsiteApplyOracle();
		tblCeaInternalWebsiteApplyOracle.setId(id);
		return tblCeaInternalWebsiteApplyOracle;
	}
}