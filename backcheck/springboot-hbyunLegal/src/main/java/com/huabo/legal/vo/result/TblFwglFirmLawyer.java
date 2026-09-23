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
 * 公司律师表
 */
@Data
@Table(name = "tbl_fwgl_firm_lawyer")
@Schema(name="TblFwglFirmLawyer")
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglFirmLawyer implements Serializable {

	private static final long serialVersionUID = 1L;
	@ExcelField(title = "律师ID", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "律师ID")
	private Long lawyerId;
	@ExcelField(title = "姓名", sort = 1, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "姓名")
	private String lawyerName;
	@ExcelField(title = "性别", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.SEX_TYPE)
	@Schema(name = "性别")
	private Integer sex;
	@ExcelField(title = "年龄", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "年龄")
	private String age;
	@ExcelField(title = "民族", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "民族")
	private String nation;
	@ExcelField(title = "照片", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "照片")
	private String photo;
	@ExcelField(title = "身份证号", sort = 6, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "身份证号")
	private String identityCard;
	@ExcelField(title = "政治面貌", sort = 7, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "政治面貌")
	private String politicsStatus;
	@ExcelField(title = "资格证书号", sort = 8, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "资格证书号")
	private String certificationNumber;
	@ExcelField(title = "住所地址", sort = 9, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "住所地址")
	private String homeAddress;
	@ExcelField(title = "毕业学校", sort = 10, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "毕业学校")
	private String schooloFgraduation;
	@ExcelField(title = "最高学历", sort = 11, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "最高学历")
	private String highestEducation;
	@ExcelField(title = "电话", sort = 12, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "电话")
	private String phone;
	@ExcelField(title = "专业", sort = 13, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "专业")
	private String specialty;
	@ExcelField(title = "现工作单位", sort = 14, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "现工作单位")
	private String nowWorkUnit;
	@ExcelField(title = "技术职务", sort = 15, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "技术职务")
	private String technicalPosition;
	@ExcelField(title = "工作部门", sort = 16, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "工作部门")
	private String workDepartment;
	@ExcelField(title = "办公电话", sort = 17, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "办公电话")
	private String officePhone;
	@ExcelField(title = "外语水平", sort = 18, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "外语水平")
	private String foreignLanguageLevel;
	@ExcelField(title = "邮编", sort = 19, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "邮编")
	private String postalCode;
	@ExcelField(title = "受过何种奖励", sort = 20, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "受过何种奖励")
	private String award;
	@ExcelField(title = "受过何种处分", sort = 21, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "受过何种处分")
	private String punishment;
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;
	@Schema(name = "类别")
	private Integer category;
	@ExcelField(title = "状态", sort = 22, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.STATE_TYPE)
	@Schema(name="状态",hidden=true)
	private Integer state;
	@Column(name = "LAWYEREXTID")
	@Schema(name = "关联id-公司律师扩展ID,多个逗号隔开")
	private String lawyerExtId;
	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private String creator;
	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private String workUnit;
	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
	@ExcelField(title = "创建时间", sort = 23, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name="创建时间",hidden=true)
	private Date createdTime;
	@ExcelField(title = "更新时间", sort = 24, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name="更新时间",hidden=true)
	private Date updatedTime;

	public static TblFwglFirmLawyer ofId(Long id) {
		TblFwglFirmLawyer tblFwglFirmLawyerMySql = new TblFwglFirmLawyer();
		tblFwglFirmLawyerMySql.setLawyerId(id);
		return tblFwglFirmLawyerMySql;
	}
}
