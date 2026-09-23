package com.huabo.central.enterprises.audit.oracle.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 题目
 */
@Schema(name="TblCeaExamineTopic")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_EXAMINE_TOPIC")
public class TblCeaExamineTopic implements Serializable {
	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "主键ID")
	private Long id;
	
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

	/**
	 * 事务id
	 */
	@Column(name = "SCORETRANSACTION")
	@Schema(name = "事务id")
	private String scoreTransaction;

	@Column(name = "TOPICNAME")
	@Schema(name = "题目名称")
	private String topicName;

	/**
	 * 关联ID-题目首部分ID
	 */
	@Column(name = "FIRSTID")
	@Schema(name = "关联ID-题目首部分ID")
	private Long firstId;

	/**
	 * 考评重点
	 */
	@Column(name = "EXAMINEEMPHASIS")
	@Schema(name = "考评重点")
	private String examineEmphasis;

	/**
	 * 序号
	 */
	@Column(name = "SERIALNUMBER")
	@Schema(name = "序号")
	private String serialNumber;

	/**
	 * 工作目标
	 */
	@Column(name = "EMPLOYMENTOBJECTIVE")
	@Schema(name = "工作目标")
	private String employmentObjective;

	/**
	 * 总分值
	 */
	@Column(name = "TOTALSCORE")
	@Schema(name = "总分值")
	private String totalScore;

	/**
	 * 考评内容
	 */
	@Column(name = "SECONDCONTENT")
	@Schema(name = "考评内容")
	private String secondContent;

	/**
	 * 子-工作内容
	 */
	@Column(name = "SONCONTENT")
	@Schema(name = "子-工作内容")
	private String sonContent;

	/**
	 * 子-评分标准
	 */
	@Column(name = "SONGRADECRITERION")
	@Schema(name = "子-评分标准")
	private String sonGradeCriterion;

	/**
	 * 子-执行主体
	 */
	@Column(name = "SONEXECUTIVEBODY")
	@Schema(name = "子-执行主体")
	private String sonExecutiveBody;

	/**
	 * 子-时限
	 */
	@Column(name = "SONTIMELIMIT")
	@Schema(name = "子-时限")
	private String sonTimeLimit;

	/**
	 * 子-分值
	 */
	@Column(name = "SONSCORE")
	@Schema(name = "子-分值")
	private String sonScore;

	/**
	 * 子-排序
	 */
	@Column(name = "SONSORT")
	@Schema(name = "子-排序")
	private Integer sonSort;

	/**
	 * 排序
	 */
	@Column(name = "SORT")
	@Schema(name = "排序")
	private Integer sort;

	/**
	 * 考核类型 1-外部监管考核 2-外部监管考核
	 */
	@Column(name = "EXAMINETYPE")
	@Schema(name = "考核类型 1-外部监管考核 2-外部监管考核")
	private Integer examineType;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	@Schema(name="状态",hidden=true)
	private Integer state;

	/**
	 * 创建人ID
	 */
	@Column(name = "CREATOR")
	@Schema(name="创建人ID",hidden=true)
	private String creator;

	/**
	 * 工作单位ID
	 */
	@Column(name = "WORKUNIT")
	@Schema(name="工作单位ID",hidden=true)
	private String workUnit;

	/**
	 * 所属集团ID
	 */
	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团ID",hidden=true)
	private String belongGroup;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATEDTIME")
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	/**
	 * 更新时间
	 */
	@Column(name = "UPDATEDTIME")
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	private static final long serialVersionUID = 1L;

	public static TblCeaExamineTopic ofId(Long id) {
		TblCeaExamineTopic model = new TblCeaExamineTopic();
		model.setId(id);
		return model;
	}

	public static TblCeaExamineTopic ofScoreTransaction(String scoreTransaction) {
		TblCeaExamineTopic model = new TblCeaExamineTopic();
		model.setScoreTransaction(scoreTransaction);
		return model;
	}
}