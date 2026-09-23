package com.huabo.legal.vo.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.legal.util.excel.DictMapUtil;
import com.huabo.legal.util.excel.annotation.ExcelField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 法律服务表
 */
@Schema(name="TblFwglLawService")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_law_service")
public class TblFwglLawService implements Serializable {

	private static final long serialVersionUID = 1L;
	//@ExcelField(title = "常年法律服务ID", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "常年法律服务ID")
	private Long lawServiceId;
	//	@ExcelField(title = "单位名称", sort = 1, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	//	@Schema(name = "单位名称")
	//	private String unitName;
	@Schema(name = "单位ID")
	private String workUnitId;
	@ExcelField(title = "单位名称", sort = 1, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "单位名称")
	private String workUnitName;
	@ExcelField(title = "机构名称", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "机构名称")
	private String organizationName;
	//@ExcelField(title = "是否有过往合作经历", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "是否有过往合作经历")
	private Integer isAwardOfContract;
	//@ExcelField(title = "费用", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "费用")
	private String expense;
	@ExcelField(title = "聘期类型", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.EMLOYMENTTERM_TYPE)
	@Schema(name = "聘期类型")
	private Integer employmentTermType;
	//@ExcelField(title = "聘期开始年月", sort = 6, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "聘期开始年月")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date employmentTermStartTime;
	//@ExcelField(title = "聘期结束年月", sort = 7, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "聘期结束年月")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date employmentTermEndTime;
	/*@ExcelField(title = "填报人", sort = 8, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "填报人")
	private String fillInPerson;*/
	/*@ExcelField(title = "填报时间", sort = 9, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "填报时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date fillInTime;*/
	//@ExcelField(title = "审核人", sort = 10, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "审核人")
	private String auditPerson;
	@ExcelField(title = "审核人", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "审核人")
	private String auditPersonName;
	
	@ExcelField(title = "审核时间", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "审核时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date auditTime;
	//@ExcelField(title = "法律服务类型1-常年 2-专项", sort = 12, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.LAWSERVICE_TYPE)
	@Schema(name = "法律服务类型1-常年 2-专项")
	private Integer lawServiceType;
	@ExcelField(title = "状态", sort = 6, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.SHSTATE_TYPE)
	@Schema(name="状态",hidden=true)
	private Integer state;
	//@ExcelField(title = "服务范围", sort = 14, column = 0, align = ExcelField.Align.CENTER, width = 3000)
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
	
	@ExcelField(title = "填报人", sort = 7, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="填报人",hidden=true)
	private String creatorName;
	
	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private String workUnit;
	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
	@ExcelField(title = "填报时间", sort = 8, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="填报时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createdTime;
	/*@ExcelField(title = "更新时间", sort = 16, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;*/

	@Schema(name = "所属集团ID")
	private String belongGroupId;

	@Schema(name = "注册地址")
	private String registerAddress;

	@Schema(name = "过往合作经历")
	private String pastCooperationExperience;

	@Schema(name = "收费标准及支付说明")
	private String chargeExplain;

	@Schema(name = "服务项目类型")
	private Integer serviceItemType;

	@ExcelField(title = "服务团队名称", sort = 9, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "服务团队名称")
	private String serviceTeamName;

	@ExcelField(title = "团队负责人", sort = 10, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "团队负责人")
	private String leaderName;

	@ExcelField(title = "联系方式", sort = 11, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "联系方式")
	private String leaderContact;

	public static TblFwglLawService ofId(Long id) {
		TblFwglLawService tblFwglLawServiceMySql = new TblFwglLawService();
		tblFwglLawServiceMySql.setLawServiceId(id);
		return tblFwglLawServiceMySql;
	}
}
