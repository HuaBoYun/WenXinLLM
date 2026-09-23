package com.huabo.legal.mysql.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 法律服务表
 */
@Schema(name="TblFwglLawServiceMySql")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_law_service")
public class TblFwglLawServiceMySql implements Serializable {

	@Id
	@Column(name = "LAWSERVICEID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "常年法律服务ID")
	private Integer lawServiceId;

	@Column(name = "UNITNAME")
	@Schema(name = "单位名称")
	private String unitName;

	@Column(name = "ORGANIZATIONNAME")
	@Schema(name = "机构名称")
	private String organizationName;

	@Column(name = "ISAWARDOFCONTRACT")
	@Schema(name = "是否签订合同")
	private Integer isAwardOfContract;

	@Column(name = "EXPENSE")
	@Schema(name = "费用")
	private String expense;

	@Column(name = "EMPLOYMENTTERMTYPE")
	@Schema(name = "聘期类型")
	private Integer employmentTermType;

	@Column(name = "EMPLOYMENTTERMSTARTTIME")
	@Schema(name = "聘期开始年月")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date employmentTermStartTime;

	@Column(name = "EMPLOYMENTTERMENDTIME")
	@Schema(name = "聘期结束年月")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date employmentTermEndTime;

	/**
	 * 不再使用 填报人就是创建人
	 */
	//	@Column(name = "FILLINPERSON")
//	@Schema(name = "填报人")
//	private String fillInPerson;

	@Column(name = "FILLINTIME")
	@Schema(name = "填报时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date fillInTime;

	@Column(name = "AUDITPERSON")
	@Schema(name = "审核人")
	private String auditPerson;

	@Column(name = "AUDITTIME")
	@Schema(name = "审核时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

	public static TblFwglLawServiceMySql ofId(Integer id) {
		TblFwglLawServiceMySql tblFwglLawServiceMySql = new TblFwglLawServiceMySql();
		tblFwglLawServiceMySql.setLawServiceId(id);
		return tblFwglLawServiceMySql;
	}
}
