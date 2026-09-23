package com.huabo.legal.oracle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 执业申请表
 */
@Schema(name="TblFwglPracticeApplyOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_practice_apply")
public class TblFwglPracticeApplyOracle implements Serializable {

	@Id
	@Column(name = "PRACTICEAPPLYID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "执业申请ID")
	private Long practiceApplyId;

	@Column(name = "STAFFID")
	@Schema(name = "人员ID")
	private Long staffId;

	@Transient
	@Schema(name = "人员名称")
	private String realName;

	@Column(name = "PRACTICEAPPLYNAME")
	@Schema(name = "姓名")
	private String practiceApplyName;

	@Column(name = "SEX")
	@Schema(name = "性别")
	private Integer sex;

	@Column(name = "AGE")
	@Schema(name = "年龄")
	private String age;

	@Column(name = "NATION")
	@Schema(name = "民族")
	private String nation;

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
	private String schoolOfGraduation;

	@Column(name = "HIGHESTEDUCATION")
	@Schema(name = "最高学历")
	private String highestEducation;

	@Column(name = "SPECIALTY")
	@Schema(name = "专业")
	private String specialty;

	@Column(name = "NOWWORKUNIT")
	@Schema(name = "现工作单位")
	private String nowWorkUnit;

	@Column(name = "PHONE")
	@Schema(name = "电话")
	private String phone;

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

	@Column(name = "IDENTITYCARDFILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开(申请人居民身份证复印件)")
	private String identityCardFileIds;

	@Column(name = "CERTIFICATEFILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开(律师资格证书或法律职业资格证书（正、副本）复印件)")
	private String certificateFileIds;

	@Column(name = "CONTRACTFILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开(在合同有效期内的劳动合同复印件)")
	private String contractFileIds;

	@Column(name = "PROVEFILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开(申请人符合《浙资运营律师管理办法》第七条第四项规定条件的工作经历、执业经历证明)")
	private String proveFileIds;

	@Column(name = "STATE")
	@Schema(name = "提交状态0-未审批、1-审批中、2-需调整、3-已通过、4-已终止、5-已跟踪、6-已完成", hidden = true)
	private Integer state;

	@Column(name = "STATUS")
	@Schema(name = "注销状态0-未审批、1-审批中、2-需调整、3-已通过、4-已终止、5-已跟踪、6-已完成", hidden = true)
	private Integer status;

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

	@Column(name = "CERTIFICATENUMBER")
	@Schema(name = "公司律师执业证号")
	private String certificateNumber;

	@Column(name = "CERTIFICATENUMBERFILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开(公司律师执业证号)")
	private String certificateNumberFileIds;
	
	@Column(name = "PRACTISINGFILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开(公司律师执业证书附件)")
	private String practisingFileIds;

	@Column(name = "APPLICATIONBOOK")
	@Schema(name = "申请书")
	private String applicationBook;

	@Column(name = "OPINIONS")
	@Schema(name = "所在单位意见")
	private String opinions;

	private static final long serialVersionUID = 1L;

	public static TblFwglPracticeApplyOracle ofId(Long id) {
		TblFwglPracticeApplyOracle tblFwglPracticeApplyMySql = new TblFwglPracticeApplyOracle();
		tblFwglPracticeApplyMySql.setPracticeApplyId(id);
		return tblFwglPracticeApplyMySql;
	}
}
