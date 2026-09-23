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
 * 专项法律服务表-考核表(此表名实体类名称与oracle数据库不一致 注意)
 */
@Schema(name="TblFwglSpecialLawServiceExamineOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_special_examine")
public class TblFwglSpecialLawServiceExamineOracle implements Serializable {

	@Id
	@Column(name = "EXAMINEID")
	@Schema(name = "考核ID")
	@GeneratedValue(generator = "JDBC")
	private Long examineId;

	@Column(name = "TEAMNAME")
	@Schema(name = "团队名称")
	private String teamName;

	@Column(name = "ISCOLLABORATION")
	@Schema(name = "有无合作")
	private String isCollaboration;

	@Column(name = "ENTRUSTSUBJECT")
	@Schema(name = "委托主体")
	private String entrustSubject;

	@Column(name = "SERVICEPROJECT")
	@Schema(name = "服务项目")
	private String serviceProject;

	@Column(name = "PROFESSIONALABILITYGRADE")
	@Schema(name = "专业能力（60分）")
	private String professionalAbilityGrade;

	@Column(name = "REACTIONEFFICIENCYGRADE")
	@Schema(name = "响应效率（30分）")
	private String reactionEfficiencyGrade;

	@Column(name = "APPRECIATIONSERVICEGRADE")
	@Schema(name = "增值服务（10分）")
	private String appreciationServiceGrade;

	@Column(name = "REMARK")
	@Schema(name = "备注")
	private String remark;

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

	@Schema(name = "合计")
	@Column(name = "TOTAL")
	private String total;

	@Schema(name = "平均分")
	@Column(name = "AVERAGE")
	private String average;

	private static final long serialVersionUID = 1L;

	public static TblFwglSpecialLawServiceExamineOracle ofId(Long id) {
		TblFwglSpecialLawServiceExamineOracle tblFwglSpecialLawServiceExamineMySql = new TblFwglSpecialLawServiceExamineOracle();
		tblFwglSpecialLawServiceExamineMySql.setExamineId(id);
		return tblFwglSpecialLawServiceExamineMySql;
	}
}
