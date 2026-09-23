package com.huabo.legal.oracle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 法务机构及负责人扩展表 (此表名实体类名称与oracle数据库不一致 注意)
 */
@Schema(name="TblFwglLegalOrganizationExtOracle")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_organization_ext")
public class TblFwglLegalOrganizationExtOracle implements Serializable {

	@Id
	@Column(name = "ORGANIZATIONEXTID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "法务机构及负责人扩展ID")
	private Long organizationExtId;

	@Column(name = "FIRMREGULATIONSAUDITNUMBER")
	@Schema(name = "企业规章制度审核件数")
	private String firmRegulationsAuditNumber;

	@Column(name = "REGULATIONSAUDITRATIO")
	@Schema(name = "规章制度审核率(%)")
	private String regulationsAuditRatio;

	@Column(name = "FIRMECONOMICSCONTRACTNUMBER")
	@Schema(name = "企业经济合同审核件数")
	private String firmEconomicsContractNumber;

	@Column(name = "ECONOMICSCONTRACTRATIO")
	@Schema(name = "经济合同审核率(%)")
	private String economicsContractRatio;

	@Column(name = "FIRMMAJORDECISIONNUMBER")
	@Schema(name = "企业重要决策审核件数")
	private String firmMajorDecisionNumber;

	@Column(name = "MAJORDECISIONRATIO")
	@Schema(name = "重要决策审核率(%)")
	private String majorDecisionRatio;

	@Column(name = "REMARK")
	@Schema(name = "备注")
	private String remark;

	@Column(name = "STATE")
	@Schema(name="状态",hidden=true)
	private Integer state;

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

	public static TblFwglLegalOrganizationExtOracle ofId(Long id) {
		TblFwglLegalOrganizationExtOracle legalOrganizationExtMySql = new TblFwglLegalOrganizationExtOracle();
		legalOrganizationExtMySql.setOrganizationExtId(id);
		return legalOrganizationExtMySql;
	}
}
