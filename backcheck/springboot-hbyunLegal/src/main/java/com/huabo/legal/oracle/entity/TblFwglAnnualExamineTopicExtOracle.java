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
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 年度考核题目表(此表名实体类名称与oracle数据库不一致 注意)
 */
@Schema(name="TblFwglAnnualExamineTopicExt")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_examine_topic_ext")
public class TblFwglAnnualExamineTopicExtOracle implements Serializable {

	@Column(name = "ANNUALEXAMINETOPICEXTID")
	@Schema(name = "年度考核题目ID")
	@GeneratedValue(generator = "JDBC")
	private Long annualExamineTopicExtId;

	@Column(name = "EXAMINEEMPHASIS")
	@Schema(name = "考核重点")
	private String examineEmphasis;

	@Column(name = "SCORE")
	@Schema(name = "分值")
	private String score;

	@Column(name = "CONTENT")
	@Schema(name = "考核内容")
	private String content;

	@Column(name = "GRADECRITERION")
	@Schema(name = "评分标准")
	private String gradeCriterion;

	@Column(name = "EMPLOYMENTOBJECTIVE")
	@Schema(name = "工作目标")
	private String employmentObjective;

	@Column(name = "WORKCONTENT")
	@Schema(name = "工作内容")
	private String workContent;

	@Column(name = "EXECUTIVEBODY")
	@Schema(name = "执行主体")
	private String executiveBody;

	@Column(name = "TIMELIMIT")
	@Schema(name = "时限")
	private String timeLimit;

	@Column(name = "FILIALEEXAMINESCORE")
	@Schema(name = "随属公司考核分值")
	private String filialeExamineScore;

	@Column(name = "SERIALNUMBER")
	@Schema(name = "序号")
	private String serialNumber;

	@Column(name = "ROWSPAN")
	@Schema(name = "行（前端存储）")
	private String rowspan;

	@Column(name = "COLSPAN")
	@Schema(name = "列（前端存储）")
	private String colspan;

	@Column(name = "EXAMINETYPE")
	@Schema(name = "考核类型 1-外部监管考核 2-子单位考核")
	private Integer examineType;

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

	public static TblFwglAnnualExamineTopicExtOracle ofId(Long id) {
		TblFwglAnnualExamineTopicExtOracle tblFwglAnnualExamineTopicExtMySql = new TblFwglAnnualExamineTopicExtOracle();
		tblFwglAnnualExamineTopicExtMySql.setAnnualExamineTopicExtId(id);
		return tblFwglAnnualExamineTopicExtMySql;
	}

	public static TblFwglAnnualExamineTopicExtOracle ofDelete(Integer examineType) {
		TblFwglAnnualExamineTopicExtOracle tblFwglAnnualExamineTopicExtOracle = new TblFwglAnnualExamineTopicExtOracle();
		tblFwglAnnualExamineTopicExtOracle.setExamineType(examineType);
		return tblFwglAnnualExamineTopicExtOracle;
	}
}
