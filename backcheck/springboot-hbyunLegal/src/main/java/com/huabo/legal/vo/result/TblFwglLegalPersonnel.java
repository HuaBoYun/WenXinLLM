package com.huabo.legal.vo.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.legal.util.excel.DictMapUtil;
import com.huabo.legal.util.excel.annotation.ExcelField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 法务人员信息表
 */
@Schema(name="TblFwglLegalPersonnel")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_legal_personnel")
public class TblFwglLegalPersonnel implements Serializable {

	private static final long serialVersionUID = 1L;

	@ExcelField(title = "姓名", sort = 1, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "姓名")
	private String personnelName;

	@ExcelField(title = "职务", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "职务")
	private String position;

	@ExcelField(title = "起始年份", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "起始年份(导出)")
	private String startYearString;

	@ExcelField(title = "联系电话", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "联系电话")
	private String phone;

	@ExcelField(title = "是否聘用", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.ISHIRE_TYPE)
	@Schema(name = "是否聘用")
	private Integer isHire;

	@ExcelField(title = "填报人", sort = 6, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name="填报人",hidden=true)
	private String creatorName;

	@ExcelField(title = "填报时间", sort = 7, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name="创建时间(导出)",hidden=true)
	private String createdTimeString;

	@ExcelField(title = "状态", sort = 8, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.SHSTATE_TYPE)
	@Schema(name="状态",hidden=true)
	private Integer state;

	@Schema(name = "起始年份")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startYear;

	//@ExcelField(title = "法务人员ID", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "法务人员ID")
	private Long personnelId;

	@Schema(name = "所属集团(列表)ID")
	private Long belongGroupId;

	//@ExcelField(title = "所属集团(列表)名称", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name="所属集团(列表)名称",hidden=true)
	private String belongGroupName;

	//@ExcelField(title = "工作单位(列表)名称", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name="工作单位(列表)名称",hidden=true)
	private String workUnitName;

	//@ExcelField(title = "工作单位(列表)ID", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "工作单位(列表)ID")
	private Long workUnitId;

	//@ExcelField(title = "性别", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.SEX_TYPE)
	@Schema(name = "性别")
	private Integer sex;

	//@ExcelField(title = "出生年月", sort = 8, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "出生年月")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date birthday;

	//@ExcelField(title = "工作性质", sort = 9, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "工作性质")
	private String jobNature;

	//@ExcelField(title = "身份证号", sort = 10, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "身份证号")
	private String identityCard;

	//@ExcelField(title = "政治面貌", sort = 11, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "政治面貌")
	private String politicsStatus;

	//@ExcelField(title = "学历", sort = 12, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.EDUCATION_TYPE)
	@Schema(name = "学历")
	private String education;

	//@ExcelField(title = "学位", sort = 13, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "学位")
	private String degree;

	//	@ExcelField(title = "职务", sort = 14, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	//@Schema(name = "职务")
	//private String duty;
	//@ExcelField(title = "职称", sort = 15, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "职称")
	private String dutyTitle;

	//@ExcelField(title = "参加工作时间", sort = 16, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "参加工作时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startWorkTime;

	//@ExcelField(title = "是否法律专业", sort = 18, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.SPECIALTY_TYPE)
	@Schema(name = "是否法律专业")
	private Integer isLawSpecialty;

	//@ExcelField(title = "是否法律职业资格", sort = 19, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.OCCUPATIONAL_TYPE)
	@Schema(name = "是否法律职业资格")
	private Integer isLawOccupational;

	//	@ExcelField(title = "资格证书编号", sort = 20, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "资格证书编号")
	private String qualifications;

	//@ExcelField(title = "是否法律顾问资格", sort = 21, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.ADVISER_TYPE)
	@Schema(name = "是否法律顾问资格")
	private Integer isLawAdviser;

	//@ExcelField(title = "资格证编号", sort = 22, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "资格证编号")
	private String certificationNumber;

	//@ExcelField(title = "最后注册时间", sort = 23, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "最后注册时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lateRegisterTime;

	//@ExcelField(title = "毕业院校", sort = 24, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "毕业院校")
	private String graduationGraduate;

	//@ExcelField(title = "开始聘用时间", sort = 25, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "开始聘用时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startHireTime;

	//@ExcelField(title = "终止聘用时间", sort = 26, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "终止聘用时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endHireTime;

	//	@ExcelField(title = "填报人", sort = 28, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	//	@Schema(name = "填报人")
	//	private String fillInPerson;

	//	@ExcelField(title = "填报时间", sort = 29, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "填报时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date fillInTime;

	//	@ExcelField(title = "审核人", sort = 30, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "审核人ID")
	private String auditPerson;

	//@ExcelField(title = "审核人名称", sort = 30, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name="审核人名称",hidden=true)
	private String auditPersonName;

	//@ExcelField(title = "审核时间", sort = 31, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "审核时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date auditTime;

	//@ExcelField(title = "起始年份（从事法律顾问起始年份", sort = 32, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "起始年份（从事法律顾问起始年份）")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date legalStartTime;

	@Schema(name = "关联id-法务人员扩展ID,多个逗号隔开")
	private String personnelExtId;

	@Schema(name="创建人",hidden=true)
	private String creator;

	@Schema(name="所属集团",hidden=true)
	private String belongGroup;

	@Schema(name="工作单位",hidden=true)
	private String workUnit;

	//	@ExcelField(title = "创建时间", sort = 34, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	//	@ExcelField(title = "更新时间", sort = 35, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	public static TblFwglLegalPersonnel ofId(Long id) {
		TblFwglLegalPersonnel legalPersonnelMySql = new TblFwglLegalPersonnel();
		legalPersonnelMySql.setPersonnelId(id);
		return legalPersonnelMySql;
	}
}
