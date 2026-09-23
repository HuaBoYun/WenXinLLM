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

import java.io.Serializable;
import java.util.Date;

/**
 * 总法律顾问表
 */
@Schema(name="TblFwglLegalAdviser")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglLegalAdviser implements Serializable {

	private static final long serialVersionUID = 1L;
	@Schema(name = "法律顾问ID")
	private Long adviserId;

	@ExcelField(title = "姓名", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "姓名")

	private String adviserName;
	@Schema(name = "所属集团(列表)ID")

	private Long belongGroupId;

	//	@ExcelField(title = "所属集团", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name="所属集团(列表)名称",hidden=true)
	private String belongGroupName;

	//	@ExcelField(title = "工作单位", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name="工作单位(列表)名称",hidden=true)

	private String workUnitName;
	@Schema(name = "工作单位(列表)ID")
	private Long workUnitId;

	@ExcelField(title = "职务", sort = 1, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "职务")
	private String position;

	@ExcelField(title = "电话", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "电话")
	private String phone;

	@ExcelField(title = "邮箱", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "邮箱")
	private String email;

	@ExcelField(title = "是否聘用", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.ISHIRE_TYPE)
	@Schema(name = "是否聘用")
	private Integer isHire;

	@ExcelField(title = "填报人", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name="创建人名称",hidden=true)
	private String creatorName;

	//	@ExcelField(title = "填报时间", sort = 6, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "填报时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date fillInTime;

	@ExcelField(title = "填报时间", sort = 6, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="创建时间(导出用的)",hidden=true)
	private String createdTimeString;

	@ExcelField(title = "状态", sort = 7, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.SHSTATE_TYPE)
	@Schema(name="状态",hidden=true)
	private Integer state;

	//	@ExcelField(title = "性别", sort = 7, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.SEX_TYPE)
	@Schema(name = "性别")
	private Integer sex;

	//@ExcelField(title = "照片", sort = 8, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "照片")
	private String photo;

	//@ExcelField(title = "职级", sort = 9, column = 0, align = ExcelField.Align.CENTER, width = 3000,dictType = DictMapUtil.SEX_TYPE)
	@Schema(name = "职级")
	private String itemRank;

	//@ExcelField(title = "出生年月", sort = 10, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "出生年月")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date birthday;

	//	@ExcelField(title = "填报人", sort = 11, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	//	@Schema(name = "填报人")
	//	private String fillInPerson;

	@Schema(name = "审核人ID")
	private String auditPerson;

	//	@ExcelField(title = "审核人名称", sort = 12, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "审核人名称")
	private String auditPersonName;

	//@ExcelField(title = "审核时间", sort = 13, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "审核时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date auditTime;

	//@ExcelField(title = "是否专职", sort = 15, column = 0, align = ExcelField.Align.CENTER, width = 3000,dictType = DictMapUtil.OCCUPATIONAL_TYPE)
	@Schema(name = "是否专职")
	private Integer isSoleDuty;

//	@ExcelField(title = "其他职务", sort = 17, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "其他职务")
	private String otherDuties;

	//@ExcelField(title = "职业背景情况", sort = 18, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "职业背景情况")
	private String professionalBackground;

	//@ExcelField(title = "证书编号", sort = 19, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "证书编号")
	private String certificateNumber;

	@Schema(name = "手机")
	private String mobilePhone;

	@Schema(name = "关联id-总法律顾问扩展ID,多个逗号隔开")
	private String adviserExtId;

	@Schema(name="创建人",hidden=true)
	private String creator;

	@Schema(name="所属集团",hidden=true)
	private String belongGroup;

	@Schema(name="工作单位",hidden=true)
	private String workUnit;

	//@ExcelField(title = "创建时间", sort = 21, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	//@ExcelField(title = "更新时间", sort = 22, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	@Schema(name = "是否持有法律资格证书")
	private Integer certificaTion;

	public static TblFwglLegalAdviser ofId(Long id) {
		TblFwglLegalAdviser tblFwglLegalAdviserMySql = new TblFwglLegalAdviser();
		tblFwglLegalAdviserMySql.setAdviserId(id);
		return tblFwglLegalAdviserMySql;
	}
}
