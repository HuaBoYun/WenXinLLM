package com.huabo.legal.oracle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 法律服务表
 */
@Schema(name="TblFwglLawServiceOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_law_service")
public class TblFwglLawServiceOracle implements Serializable {

	@Id
	@Column(name = "LAWSERVICEID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "常年法律服务ID")
	private Long lawServiceId;

	@Column(name = "WORKUNITID")
	@Schema(name = "单位ID")
	private String workUnitId;

	@Transient
	@Schema(name="单位名称",hidden=true)
	private String workUnitName;

	@Column(name = "BELONGGROUPID")
	@Schema(name = "所属集团ID")
	private String belongGroupId;

	@Transient
	@Schema(name="所属集团名称",hidden=true)
	private String belongGroupName;

	@Column(name = "ORGANIZATIONNAME")
	@Schema(name = "机构名称")
	private String organizationName;

	@Column(name = "ISAWARDOFCONTRACT")
	@Schema(name = "是否有过往合作经历")
	private Integer isAwardOfContract;

	@Column(name = "EXPENSE")
	@Schema(name = "费用")
	private String expense;

	@Column(name = "EMPLOYMENTTERMTYPE")
	@Schema(name = "聘期类型")
	private Integer employmentTermType;

	@Column(name = "EMPLOYMENTTERMSTARTTIME")
	@Schema(name = "聘期开始年月")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date employmentTermStartTime;

	@Column(name = "EMPLOYMENTTERMENDTIME")
	@Schema(name = "聘期结束年月")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date employmentTermEndTime;

	/**
	 * 不再使用 填报人就是创建人
	 */
	//	@Column(name = "FILLINPERSON")
	//	@Schema(name = "填报人")
	//	private String fillInPerson;

	@Column(name = "FILLINTIME")
	@Schema(name = "填报时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fillInTime;

	@Column(name = "AUDITPERSON")
	@Schema(name = "审核人")
	private String auditPerson;

	@Transient
	@Schema(name = "审核人名称")
	private String auditPersonName;

	@Column(name = "AUDITTIME")
	@Schema(name = "审核时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date auditTime;

	@NotNull(message = "lawServiceType,不能为空；法律服务类型1-常年 2-专项")
	@Column(name = "LAWSERVICETYPE")
	@Schema(name = "法律服务类型1-常年 2-专项")
	private Integer lawServiceType;

	@Column(name = "STATE")
	@Schema(name="状态",hidden=true)
	private Integer state;

	@Column(name = "SCOPEOFSERVICES")
	@Schema(name = "服务范围")
	private String scopeOfServices;

	@Column(name = "LAWYERID")
	@Schema(name = "关联id-律师ID,多个逗号隔开")
	private String lawyerId;

	@Column(name = "WORKRECORDID")
	@Schema(name = "关联id-工作记录ID,多个逗号隔开")
	private String workRecordId;

	@Column(name = "WORKREPORTID")
	@Schema(name = "关联id-工作报告ID,多个逗号隔开")
	private String workReportId;

	@Column(name = "GRADEID")
	@Schema(name = "关联id-评分ID,多个逗号隔开")
	private String gradeId;

	@Column(name = "EXAMINEID")
	@Schema(name = "关联id-考核ID,多个逗号隔开")
	private String examineId;

	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private String creator;

	@Transient
	@Schema(name="创建人名称",hidden=true)
	private String creatorName;

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

	@Schema(name = "注册地址")
	@Column(name = "REGISTERADDRESS")
	private String registerAddress;

	@Schema(name = "过往合作经历")
	@Column(name = "PASTCOOPERATIONEXPERIENCE")
	private String pastCooperationExperience;

	@Schema(name = "收费标准及支付说明")
	@Column(name = "CHARGEEXPLAIN")
	private String chargeExplain;

	@Schema(name = "服务项目类型")
	@Column(name = "SERVICEITEMTYPE")
	private Integer serviceItemType;

	@Schema(name = "服务团队名称")
	@Column(name = "SERVICETEAMNAME")
	private String serviceTeamName;

	@Schema(name = "团队负责人")
	@Column(name = "LEADERNAME")
	private String leaderName;

	@Schema(name = "联系方式")
	@Column(name = "LEADERCONTACT")
	private String leaderContact;

	private static final long serialVersionUID = 1L;

	public static TblFwglLawServiceOracle ofId(Long id) {
		TblFwglLawServiceOracle tblFwglLawServiceMySql = new TblFwglLawServiceOracle();
		tblFwglLawServiceMySql.setLawServiceId(id);
		return tblFwglLawServiceMySql;
	}
}
