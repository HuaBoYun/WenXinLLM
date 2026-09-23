package com.huabo.legal.oracle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 法务机构及负责人表
 */
@Schema(name="TblFwglLegalOrganizationOracle")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_legal_organization")
public class TblFwglLegalOrganizationOracle implements Serializable {

	@Id
	@Column(name = "ORGANIZATIONID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "机构ID")
	private Long organizationId;

	@Column(name = "ORGANIZATIONNAME")
	@Schema(name = "名称")
	private String organizationName;

	@Column(name = "BELONGGROUPID")
	@Schema(name = "所属集团(列表)ID")
	private Long belongGroupId;

	@Transient
	@Schema(name="所属集团(列表)名称", hidden = true)
	private String belongGroupName;

	@Transient
	@Schema(name="工作单位(列表)名称", hidden = true)
	private String workUnitName;

	@Column(name = "WORKUNITID")
	@Schema(name = "工作单位(列表)ID")
	private Long workUnitId;

	@Column(name = "NATURE")
	@Schema(name = "性质")
	private String nature;

	@Column(name = "RESPONSIBLEPERSON")
	@Schema(name = "负责人")
	private String responsiblePerson;

	@Column(name = "RESPONSIBLEPERSONPHONE")
	@Schema(name = "负责人电话")
	private String responsiblePersonPhone;

	@Column(name = "CONTACT")
	@Schema(name = "联络人")
	private String contact;

	@Column(name = "CONTACTPHONE")
	@Schema(name = "联络人电话")
	private String contactPhone;

	@Column(name = "CONTACTMOBILEPHONE")
	@Schema(name = "联络人手机")
	private String contactMobilePhone;

	@Column(name = "CONTACTEMAIL")
	@Schema(name = "联络邮箱")
	private String contactEmail;

	@Column(name = "MAKINGPEOPLE")
	@Schema(name = "制单人")
	private String makingPeople;

	@Column(name = "MAKINGTIME")
	@Schema(name = "制单日期")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date makingTime;

	@Column(name = "AUDITPERSON")
	@Schema(name = "审核人")
	private String auditPerson;

	@Transient
	@Schema(name = "审核人名称")
	private String auditPersonName;

	@Column(name = "AUDITTIME")
	@Schema(name = "审核时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date auditTime;

	@Column(name = "ANNUAL")
	@Schema(name = "年度")
	private String annual;

	@Column(name = "STATE")
	@Schema(name = "状态0-未审批、1-审批中、2-需调整、3-已通过、4-已终止、5-已跟踪、6-已完成", hidden = true)
	private Integer state;

	@Column(name = "ORGANIZATIONEXTID")
	@Schema(name = "关联id-法务机构及负责人扩展ID,多个逗号隔开")
	private String organizationExtId;

	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private String creator;

	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private String workUnit;

	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;

	@Column(name = "CREATEDTIME")
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	@Column(name = "UPDATEDTIME")
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	private static final long serialVersionUID = 1L;

	public static TblFwglLegalOrganizationOracle ofId(Long id) {
		TblFwglLegalOrganizationOracle tblFwglLegalOrganizationMySql = new TblFwglLegalOrganizationOracle();
		tblFwglLegalOrganizationMySql.setOrganizationId(id);
		return tblFwglLegalOrganizationMySql;
	}
}
