package com.huabo.legal.oracle.entity;

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
import java.io.Serializable;
import java.util.Date;

/**
 * 公司律师表
 */
@Data
@Table(name = "tbl_fwgl_firm_lawyer")
@Schema(name="TblFwglFirmLawyerOracle")
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglFirmLawyerOracle implements Serializable {

	@Id
	@Column(name = "LAWYERID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "律师ID")
	private Long lawyerId;

	@Column(name = "LAWYERNAME")
	@Schema(name = "姓名")
	private String lawyerName;

	@Column(name = "SEX")
	@Schema(name = "性别")
	private Integer sex;

	@Column(name = "AGE")
	@Schema(name = "年龄")
	private String age;

	@Column(name = "NATION")
	@Schema(name = "民族")
	private String nation;

	@Column(name = "PHOTO")
	@Schema(name = "照片")
	private String photo;

	@Column(name = "IDENTITYCARD")
	@Schema(name = "身份证号")
	private String identityCard;

	@Column(name = "POLITICSSTATUS")
	@Schema(name = "政治面貌")
	private String politicsStatus;

	@Column(name = "CERTIFICATIONNUMBER")
	@Schema(name = "资格证书号")
	private String certificationNumber;

	@Column(name = "HOMEADDRESS")
	@Schema(name = "住所地址")
	private String homeAddress;

	@Column(name = "SCHOOLOFGRADUATION")
	@Schema(name = "毕业学校")
	private String schooloFgraduation;

	@Column(name = "HIGHESTEDUCATION")
	@Schema(name = "最高学历")
	private String highestEducation;

	@Column(name = "PHONE")
	@Schema(name = "电话")
	private String phone;

	@Column(name = "SPECIALTY")
	@Schema(name = "专业")
	private String specialty;

	@Column(name = "nowworkunit")
	@Schema(name = "现工作单位")
	private String nowWorkUnit;

	@Column(name = "TECHNICALPOSITION")
	@Schema(name = "技术职务")
	private String technicalPosition;

	@Column(name = "WORKDEPARTMENT")
	@Schema(name = "工作部门")
	private String workDepartment;

	@Column(name = "OFFICEPHONE")
	@Schema(name = "办公电话")
	private String officePhone;

	@Column(name = "FOREIGNLANGUAGELEVEL")
	@Schema(name = "外语水平")
	private String foreignLanguageLevel;

	@Column(name = "POSTALCODE")
	@Schema(name = "邮编")
	private String postalCode;

	@Column(name = "AWARD")
	@Schema(name = "受过何种奖励")
	private String award;

	@Column(name = "PUNISHMENT")
	@Schema(name = "受过何种处分")
	private String punishment;

	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	@Column(name = "CATEGORY")
	@Schema(name = "类别")
	private Integer category;

	@Column(name = "STATE")
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

	@Column(name = "CREATEDTIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name="创建时间",hidden=true)
	private Date createdTime;

	@Column(name = "UPDATEDTIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name="更新时间",hidden=true)
	private Date updatedTime;

	private static final long serialVersionUID = 1L;

	public static TblFwglFirmLawyerOracle ofId(Long id) {
		TblFwglFirmLawyerOracle tblFwglFirmLawyerMySql = new TblFwglFirmLawyerOracle();
		tblFwglFirmLawyerMySql.setLawyerId(id);
		return tblFwglFirmLawyerMySql;
	}
}
