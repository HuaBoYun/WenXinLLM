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
 * 执业申请表
 */
@Schema(name="TblFwglPracticeApply")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_practice_apply")
public class TblFwglPracticeApply implements Serializable {

	private static final long serialVersionUID = 1L;

	@ExcelField(title = "姓名", sort = 1, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "姓名")
	private String practiceApplyName;

	//	@ExcelField(title = "任职公司", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	//	@Schema(name = "现工作单位")
	//	private String nowWorkUnit;

	//	@ExcelField(title = "类别", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	//	@Schema(name = "工作部门")
	//	private String workDepartment;

	@ExcelField(title = "法律资格证书", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "资格证书号")
	private String certificationNumber;

	@ExcelField(title = "公司律师执业资格证号", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "公司律师执业证号")
	private String certificateNumber;

	@ExcelField(title = "联系电话", sort = 6, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "电话")
	private String phone;

	@ExcelField(title = "年龄", sort = 7, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "年龄")
	private String age;

	@ExcelField(title = "身份证号", sort = 8, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "身份证号")
	private String identityCard;

	@ExcelField(title = "毕业学校", sort = 9, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "毕业学校")
	private String schoolOfGraduation;

	@ExcelField(title = "专业", sort = 10, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "专业")
	private String specialty;

	@ExcelField(title = "具体工作部门", sort = 11, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "工作部门")
	private String workDepartment;

	@ExcelField(title = "邮编", sort = 12, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "邮编")
	private String postalCode;

	@ExcelField(title = "性别", sort = 13, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.SEX_TYPE)
	@Schema(name = "性别")
	private Integer sex;

	@ExcelField(title = "政治面貌", sort = 14, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "政治面貌")
	private String politicsStatus;

	@ExcelField(title = "住所地址", sort = 15, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "住所地址")
	private String homeAddress;

	@ExcelField(title = "最高学历", sort = 16, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.EDUCATION_TYPE)
	@Schema(name = "最高学历")
	private String highestEducation;

	@ExcelField(title = "现工作单位", sort = 17, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "现工作单位")
	private String nowWorkUnit;

	@ExcelField(title = "外语水平", sort = 18, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "外语水平")
	private String foreignLanguageLevel;

	@ExcelField(title = "状态", sort = 19, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.SHSTATE_TYPE)
	@Schema(name="状态",hidden=true)
	private Integer state;


	//@ExcelField(title = "执业申请ID", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "执业申请ID")
	private Long practiceApplyId;

	//@ExcelField(title = "民族", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "民族")
	private String nation;

	//@ExcelField(title = "技术职务", sort = 14, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "技术职务")
	private String technicalPosition;

	//@ExcelField(title = "办公电话", sort = 16, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "办公电话")
	private String officePhone;

	//	@ExcelField(title = "受过何种奖励", sort = 19, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "受过何种奖励")
	private String award;

	//	@ExcelField(title = "受过何种处分", sort = 20, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "受过何种处分")
	private String punishment;

	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	@Column(name = "PRACTICEAPPLYEXTID")
	@Schema(name = "关联id-执业申请-简历ID,多个逗号隔开")
	private String practiceApplyExtId;

	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private String creator;

	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private String workUnit;

	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;

	//	@ExcelField(title = "创建时间", sort = 22, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	//	@ExcelField(title = "更新时间", sort = 23, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	@Schema(name = "人员ID")
	private Integer staffId;

	@Schema(name = "上传文件ids 多个逗号隔开(公司律师执业证号)")
	private String certificateNumberFileIds;

	@Schema(name = "上传文件ids 多个逗号隔开(申请人居民身份证复印件)")
	private String identityCardFileIds;

	@Schema(name = "上传文件ids 多个逗号隔开(律师资格证书或法律职业资格证书（正、副本）复印件)")
	private String certificateFileIds;

	@Schema(name = "上传文件ids 多个逗号隔开(在合同有效期内的劳动合同复印件)")
	private String contractFileIds;

	@Schema(name = "上传文件ids 多个逗号隔开(申请人符合《浙资运营律师管理办法》第七条第四项规定条件的工作经历、执业经历证明)")
	private String proveFileIds;

	@Schema(name = "上传文件ids 多个逗号隔开(公司律师执业证书附件)")
	private String practisingFileIds;

	@Schema(name = "申请书")
	private String applicationBook;

	@Schema(name = "所在单位意见")
	private String opinions;

	public static TblFwglPracticeApply ofId(Long id) {
		TblFwglPracticeApply tblFwglPracticeApplyMySql = new TblFwglPracticeApply();
		tblFwglPracticeApplyMySql.setPracticeApplyId(id);
		return tblFwglPracticeApplyMySql;
	}
}
