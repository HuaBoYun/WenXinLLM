package com.huabo.central.enterprises.audit.oracle.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 题目-第二部分
 */
@Schema(name="TblCeaExamineTopicSecond")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_EXAMINE_TOPIC_SECOND")
public class TblCeaExamineTopicSecond implements Serializable {
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
	 * 考核类型 1-外部监管考核 2-外部监管考核
	 */
	@Column(name = "EXAMINETYPE")
	@Schema(name = "考核类型 1-外部监管考核 2-外部监管考核")
	private Integer examineType;

	/**
	 * 关联ID-题目首部分ID
	 */
	@Column(name = "FIRSTID")
	@Schema(name = "关联ID-题目首部分ID")
	private Long firstId;

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

	@Schema(name="事务ID",hidden=true)
	@Column(name = "TRANSACTIONID")
	private String transactionId;

	@Transient
	@Schema(name="考评重点",hidden=true)
	private String examineEmphasis;

	@Transient
	@Schema(name="序号",hidden=true)
	private String serialNumber;

	@Transient
	@Schema(name="工作目标",hidden=true)
	private String employmentObjective;

	private static final long serialVersionUID = 1L;

	public static TblCeaExamineTopicSecond ofId(Long id) {
		TblCeaExamineTopicSecond tblCeaExamineTopicSecond = new TblCeaExamineTopicSecond();
		tblCeaExamineTopicSecond.setId(id);
		return tblCeaExamineTopicSecond;
	}

	public static TblCeaExamineTopicSecond ofFirstId(Long firstId) {
		TblCeaExamineTopicSecond tblCeaExamineTopicSecond = new TblCeaExamineTopicSecond();
		tblCeaExamineTopicSecond.setFirstId(firstId);
		return tblCeaExamineTopicSecond;
	}

	public static TblCeaExamineTopicSecond ofTransactionId(String transactionId) {
		TblCeaExamineTopicSecond tblCeaExamineTopicSecond = new TblCeaExamineTopicSecond();
		tblCeaExamineTopicSecond.setTransactionId(transactionId);
		return tblCeaExamineTopicSecond;
	}
}