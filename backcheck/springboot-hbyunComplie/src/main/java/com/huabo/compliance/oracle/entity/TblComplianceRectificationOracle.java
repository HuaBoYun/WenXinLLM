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
 * 合规-问题整改
 */
@Schema(name="TblComplianceRectificationOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_COMPLIANCE_RECTIFICATION")
public class TblComplianceRectificationOracle implements Serializable {

	/**
	 * id
	 */
	@Id
	@Column(name = "ID")
	@Schema(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	private Integer id;

	/**
	 * 检查实施主键ID（待整改问题-是）
	 */
	@Column(name = "IMPID")
	@Schema(name = "检查实施主键ID（待整改问题-是）")
	private Integer impId;

	/**
	 * 整改状态：0-未完成 1-已完成
	 */
	@Column(name = "RECTIFICATIONSTATE")
	@Schema(name = "整改状态：0-未完成 1-已完成")
	private Integer rectificationState;

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

	@Transient
	@Schema(name="检查实施-对象")
	private TblComplianceInspectImpOracle inspectImp;

	private static final long serialVersionUID = 1L;

	public static TblComplianceRectificationOracle ofId(Integer id) {
		TblComplianceRectificationOracle tblComplianceRectificationOracle = new TblComplianceRectificationOracle();
		tblComplianceRectificationOracle.setId(id);
		return tblComplianceRectificationOracle;
	}
}