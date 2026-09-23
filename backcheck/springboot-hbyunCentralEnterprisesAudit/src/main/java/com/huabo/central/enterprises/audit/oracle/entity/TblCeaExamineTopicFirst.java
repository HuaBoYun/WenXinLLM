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
 * 题目-首部分
 */
@Schema(name="TblCeaExamineTopicFirst")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_EXAMINE_TOPIC_FIRST")
public class TblCeaExamineTopicFirst implements Serializable {
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

	public static TblCeaExamineTopicFirst ofId(Long id) {
		TblCeaExamineTopicFirst tblCeaExamineTopicFirst = new TblCeaExamineTopicFirst();
		tblCeaExamineTopicFirst.setId(id);
		return tblCeaExamineTopicFirst;
	}
}