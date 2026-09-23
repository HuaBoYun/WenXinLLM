package com.huabo.legal.oracle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 总法律顾问表
 */
@Schema(name="TblFwglLegalAdviserOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_legal_adviser")
public class TblFwglLegalAdviserOracle implements Serializable {

	@Id
	@Column(name = "ADVISERID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "法律顾问ID")
	private Long adviserId;

	@Column(name = "ADVISERNAME")
	@Schema(name = "姓名")
	private String adviserName;

	@Column(name = "BELONGGROUPID")
	@Schema(name = "所属集团(列表)ID")
	private Long belongGroupId;

	@Transient
	@Schema(name="所属集团(列表)名称",hidden=true)
	private String belongGroupName;

	@Transient
	@Schema(name="工作单位(列表)名称",hidden=true)
	private String workUnitName;

	@Column(name = "WORKUNITID")
	@Schema(name = "工作单位(列表)ID")
	private Long workUnitId;

	@Column(name = "POSITION")
	@Schema(name = "职务")
	private String position;

	@Column(name = "PHONE")
	@Schema(name = "电话")
	private String phone;

	@Column(name = "EMAIL")
	@Schema(name = "邮箱")
	private String email;

	@Column(name = "SEX")
	@Schema(name = "性别")
	private Integer sex;

	@Column(name = "PHOTO")
	@Schema(name = "照片")
	private String photo;

	@Column(name = "ITEMRANK")
	@Schema(name = "职级")
	private String itemRank;

	@Column(name = "BIRTHDAY")
	@Schema(name = "出生年月")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date birthday;

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

	@Transient
	@Schema(name = "审核人名称")
	private String auditPersonName;

	@Column(name = "AUDITTIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name = "审核时间")
	private Date auditTime;

	@Column(name = "STATE")
	@Schema(name="状态",hidden=true)
	private Integer state;

	@Column(name = "ISSOLEDUTY")
	@Schema(name = "是否专职")
	private Integer isSoleDuty;

	@Column(name = "ISHIRE")
	@Schema(name = "是否聘用")
	private Integer isHire;

	@Column(name = "CERTIFICATION")
	@Schema(name = "是否持有法律资格证书")
	private Integer certificaTion;

	@Column(name = "OTHERDUTIES")
	@Schema(name = "其他职务")
	private String otherDuties;

	@Column(name = "PROFESSIONALBACKGROUND")
	@Schema(name = "职业背景情况")
	private String professionalBackground;

	@Column(name = "CERTIFICATENUMBER")
	@Schema(name = "证书编号")
	private String certificateNumber;

	@Column(name = "MOBILEPHONE")
	@Schema(name = "手机")
	private String mobilePhone;

	@Column(name = "ADVISEREXTID")
	@Schema(name = "关联id-总法律顾问扩展ID,多个逗号隔开")
	private String adviserExtId;

	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private String creator;

	@Transient
	@Schema(name="创建人名称",hidden=true)
	private String creatorName;

	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;

	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private String workUnit;

	@Column(name = "CREATEDTIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name="创建时间",hidden=true)
	private Date createdTime;

	@Transient
	@Schema(name="创建时间(导出用的)",hidden=true)
	private String createdTimeString;

	@Column(name = "UPDATEDTIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name="更新时间",hidden=true)
	private Date updatedTime;

	private static final long serialVersionUID = 1L;

	public static TblFwglLegalAdviserOracle ofId(Long id) {
		TblFwglLegalAdviserOracle tblFwglLegalAdviserMySql = new TblFwglLegalAdviserOracle();
		tblFwglLegalAdviserMySql.setAdviserId(id);
		return tblFwglLegalAdviserMySql;
	}
}
