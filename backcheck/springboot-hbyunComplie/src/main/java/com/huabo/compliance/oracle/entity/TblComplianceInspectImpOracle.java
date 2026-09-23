package com.huabo.compliance.oracle.entity;

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
 * 合规-检查实施
 */
@Schema(name="TblComplianceInspectImpOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_COMPLIANCE_INSPECT_IMP")
public class TblComplianceInspectImpOracle implements Serializable {
	/**
	 * id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@Schema(name = "id")
	private Integer id;

	/**
	 * 疑似问题
	 */
	@Column(name = "SUSPECTEDISSUE")
	@Schema(name = "疑似问题")
	private String suspectedIssue;

	/**
	 * 问题类型
	 */
	@Column(name = "QUESTIONTYPE")
	@Schema(name = "问题类型")
	private String questionType;

	/**
	 * 发现时间
	 */
	@Column(name = "DISCOVERTIME")
	@Schema(name = "发现时间 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date discoverTime;

	/**
	 * 业务领域
	 */
	@Column(name = "BUSINESSAREA")
	@Schema(name = "业务领域")
	private String businessArea;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIBE")
	@Schema(name = "描述")
	private String describe;

	/**
	 * 确认情况：0-未确认1-已确认
	 */
	@Column(name = "ISCONFIRM")
	@Schema(name = "确认情况：0-未确认1-已确认")
	private Integer isConfirm;

	/**
	 * 是否待整改问题：0-否 1-是
	 */
	@Column(name = "ISRECTIFICATION")
	@Schema(name = "是否待整改问题：0-否 1-是")
	private Integer isRectification;

	/**
	 * 富文本
	 */
	@Column(name = "CONTENT")
	@Schema(name = "富文本")
	private String content;

	/**
	 * 上传文件ids;多个逗号隔开
	 */
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids;多个逗号隔开")
	private String fileIds;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	@Schema(name = "状态")
	private Integer state;

	/**
	 * 创建人
	 */
	@Column(name = "CREATOR")
	@Schema(name = "创建人")
	private Integer creator;

	@Transient
	@Schema(name = "创建人名称")
	private String creatorName;

	/**
	 * 工作单位
	 */
	@Column(name = "WORKUNIT")
	@Schema(name = "工作单位")
	private Integer workUnit;

	/**
	 * 所属集团
	 */
	@Column(name = "BELONGGROUP")
	@Schema(name = "所属集团")
	private Integer belongGroup;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATEDTIME")
	@Schema(name = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	/**
	 * 更新时间
	 */
	@Column(name = "UPDATEDTIME")
	@Schema(name = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	private static final long serialVersionUID = 1L;

	public static TblComplianceInspectImpOracle ofId(Integer id) {
		TblComplianceInspectImpOracle inspectImpOracle = new TblComplianceInspectImpOracle();
		inspectImpOracle.setId(id);
		return inspectImpOracle;
	}
}