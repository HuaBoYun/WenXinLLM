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
 * 央企内审-综合管理-督办通知单
 */
@Schema(name="TblCeaSupervisionNoticeOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_SUPERVISION_NOTICE")
public class TblCeaSupervisionNoticeOracle implements Serializable {

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
	 * 通知单编号
	 */
	@Column(name = "NOTICENUMBER")
	@Schema(name = "通知单编号")
	private String noticeNumber;

	/**
	 * 通知单名称
	 */
	@Column(name = "NOTICENAME")
	@Schema(name = "通知单名称")
	private String noticeName;

	/**
	 * 时间
	 */
	@Column(name = "NOTICETIME")
	@Schema(name = "时间 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date noticeTime;

	@Transient
	private String noticeTime1;

	/**
	 * 督办部门
	 */
	@Column(name = "SUPERVISIONWORKUNIT")
	@Schema(name = "督办部门")
	private Long supervisionWorkUnit;

	@Transient
	@Schema(name = "督办部门名称")
	private String supervisionWorkUnitName;

	/**
	 * 经办人
	 */
	@Column(name = "TRANSACTOR")
	@Schema(name = "经办人")
	private Long transactor;

	@Transient
	@Schema(name = "经办人名称")
	private String transactorName;

	/**
	 * 备注
	 */
	@Column(name = "REMARK")
	@Schema(name = "备注")
	private String remark;

	/**
	 * 上传文件ids 多个逗号隔开
	 */
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

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
	private Long creator;

	@Transient
	@Schema(name="创建人名称",hidden=true)
	private String creatorName;

	/**
	 * 工作单位ID
	 */
	@Column(name = "WORKUNIT")
	@Schema(name="工作单位ID",hidden=true)
	private Long workUnit;

	/**
	 * 所属集团ID
	 */
	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团ID",hidden=true)
	private Long belongGroup;

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
	
	/**
	 * 完成情况 
	 */
	@Column(name = "COMPSTATUS")
	@Schema(name = "完成情况")
	private String compstatus;
	
	/**
	 * 办理状态
	 */
	@Column(name = "HANDSTATUS")
	@Schema(name = "办理状态,0=未办理；1=已办理")
	private Integer handstatus;
	
	
	private static final long serialVersionUID = 1L;

	public static TblCeaSupervisionNoticeOracle ofId(Long id) {
		TblCeaSupervisionNoticeOracle tblCeaSupervisionNoticeOracle = new TblCeaSupervisionNoticeOracle();
		tblCeaSupervisionNoticeOracle.setId(id);
		return tblCeaSupervisionNoticeOracle;
	}
}