package com.huabo.legal.mysql.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
 * 法务人员信息表
 */
@Schema(name="TblFwglLegalPersonnelMySql")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_legal_personnel")
public class TblFwglLegalPersonnelMySql implements Serializable {

	@Id
	@Column(name = "PERSONNELID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "法务人员ID")
	private Integer personnelId;

	@Column(name = "PERSONNELNAME")
	@Schema(name = "姓名")
	private String personnelName;

	@Column(name = "BELONGGROUPID")
	@Schema(name = "所属集团(列表)ID")
	private Integer belongGroupId;

	@Transient
	@Schema(name="所属集团(列表)名称",hidden=true)
	private String belongGroupName;

	@Transient
	@Schema(name="工作单位(列表)名称",hidden=true)
	private String workUnitName;

	@Column(name = "WORKUNITID")
	@Schema(name = "工作单位(列表)ID")
	private Integer workUnitId;

	@Column(name = "SEX")
	@Schema(name = "性别")
	private Integer sex;

	@Column(name = "POSITION")
	@Schema(name = "职务")
	private String position;

	@Column(name = "PHONE")
	@Schema(name = "联系电话")
	private String phone;

	@Column(name = "BIRTHDAY")
	@Schema(name = "出生年月")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date birthday;

	@Column(name = "JOBNATURE")
	@Schema(name = "工作性质")
	private String jobNature;

	@Column(name = "IDENTITYCARD")
	@Schema(name = "身份证号")
	private String identityCard;

	@Column(name = "POLITICSSTATUS")
	@Schema(name = "政治面貌")
	private String politicsStatus;

	@Column(name = "EDUCATION")
	@Schema(name = "学历")
	private String education;

	@Column(name = "DEGREE")
	@Schema(name = "学位")
	private String degree;

	@Column(name = "DUTY")
	@Schema(name = "职务")
	private String duty;

	@Column(name = "DUTYTITLE")
	@Schema(name = "职称")
	private String dutyTitle;

	@Column(name = "STARTWORKTIME")
	@Schema(name = "参加工作时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startWorkTime;

	@Column(name = "STARTYEAR")
	@Schema(name = "起始年份")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startYear;

	@Column(name = "ISLAWSPECIALTY")
	@Schema(name = "是否法律专业")
	private Integer isLawSpecialty;

	@Column(name = "ISLAWOCCUPATIONAL")
	@Schema(name = "是否法律职业资格")
	private Integer isLawOccupational;

	@Column(name = "QUALIFICATIONS")
	@Schema(name = "资格证书编号")
	private String qualifications;

	@Column(name = "ISLAWADVISER")
	@Schema(name = "是否法律顾问资格")
	private Integer isLawAdviser;

	@Column(name = "CERTIFICATIONNUMBER")
	@Schema(name = "资格证编号")
	private String certificationNumber;

	@Column(name = "LATEREGISTERTIME")
	@Schema(name = "最后注册时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lateRegisterTime;

	@Column(name = "GRADUATIONGRADUATE")
	@Schema(name = "毕业院校")
	private String graduationGraduate;

	@Column(name = "STARTHIRETIME")
	@Schema(name = "开始聘用时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startHireTime;

	@Column(name = "ENDHIRETIME")
	@Schema(name = "终止聘用时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endHireTime;

	@Column(name = "ISHIRE")
	@Schema(name = "是否聘用")
	private Integer isHire;

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

	@Column(name = "LEGALSTARTTIME")
	@Schema(name = "起始年份（从事法律顾问起始年份）")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date legalStartTime;

	@Column(name = "PERSONNELEXTID")
	@Schema(name = "关联id-法务人员扩展ID,多个逗号隔开")
	private String personnelExtId;

	@Column(name = "STATE")
	@Schema(name="状态",hidden=true)
	private Integer state;

	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private String creator;

	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;

	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private String workUnit;

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

	public static TblFwglLegalPersonnelMySql ofId(Integer id) {
		TblFwglLegalPersonnelMySql legalPersonnelMySql = new TblFwglLegalPersonnelMySql();
		legalPersonnelMySql.setPersonnelId(id);
		return legalPersonnelMySql;
	}
}
