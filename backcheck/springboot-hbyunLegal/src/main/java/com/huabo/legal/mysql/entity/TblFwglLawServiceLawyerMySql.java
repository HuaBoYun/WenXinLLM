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
import java.io.Serializable;
import java.util.Date;

/**
 * 法律服务-律师信息表
 */
@Schema(name="TblFwglLawServiceLawyerMySql")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_law_service_lawyer")
public class TblFwglLawServiceLawyerMySql implements Serializable {

	@Id
	@Column(name = "LAWYERID")
	@Schema(name = "律师ID")
	@GeneratedValue(generator = "JDBC")
	private Integer lawyerId;

	@Column(name = "LAWYERNAME")
	@Schema(name = "姓名")
	private String lawyerName;

	@Column(name = "SEX")
	@Schema(name = "性别")
	private Integer sex;

	@Column(name = "IDENTITYCARD")
	@Schema(name = "身份证")
	private String identityCard;

	@Column(name = "PHONE")
	@Schema(name = "联系电话")
	private String phone;

	@Column(name = "POSITION")
	@Schema(name = "职务")
	private String position;

	@Column(name = "EDUCATION")
	@Schema(name = "学历")
	private String education;

	@Column(name = "STARTYEAR")
	@Schema(name = "起始年份")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startYear;

	@Column(name = "COMMUNITYPARTTIMEWORK")
	@Schema(name = "社会兼职")
	private String communityPartTimeWork;

	@Column(name = "EXPERTISEDOMAIN")
	@Schema(name = "专长领域")
	private String expertiseDomain;

	@Column(name = "LAWOCCUPATION")
	@Schema(name = "法律职业")
	private String lawOccupation;

	@Column(name = "LAWYEROCCUPATION")
	@Schema(name = "律师职业")
	private String lawyerOccupation;

	@Column(name = "BIOGRAPHICALNOTES")
	@Schema(name = "简历")
	private String biographicalNotes;

	@Column(name = "STATE")
	@Schema(name="状态",hidden=true)
	private Integer state;

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

	public static TblFwglLawServiceLawyerMySql ofId(Integer id) {
		TblFwglLawServiceLawyerMySql tblFwglLawServiceLawyerMySql = new TblFwglLawServiceLawyerMySql();
		tblFwglLawServiceLawyerMySql.setLawyerId(id);
		return tblFwglLawServiceLawyerMySql;
	}
}
