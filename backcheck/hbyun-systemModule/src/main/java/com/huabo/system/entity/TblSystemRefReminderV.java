package com.huabo.system.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 人员催办表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_SYSTEM_REF_REMINDER_V")
public class TblSystemRefReminderV {

	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@Schema(name="主键ID")
	private Long id;

	/**
	 * 催办人员
	 */
	@Column(name = "REMINDERSTAFFID")
	@Schema(name="催办人员")
	private Long reminderStaffId;

	@Transient
	@Schema(name="催办人员名称")
	private String reminderStaffName;

	/**
	 * 催办内容
	 */
	@Column(name = "REMINDERCONTENT")
	@Schema(name="催办内容")
	private String reminderContent;

	@Schema(name="业务催办时间")
	@Column(name = "REMINDERTIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date reminderTime;

	/**
	 * 关联ID
	 */
	@Column(name = "REFOPMID")
	@Schema(name="关联ID-业务 ID")
	private Long refopmId;

	/**
	 * 是否阅读 0-未阅读 1-已阅读
	 */
	@Column(name = "ISREAD")
	@Schema(name="是否阅读 0-未阅读 1-已阅读")
	private Integer isRead;

	@Column(name = "ISCOMPLETE")
	@Schema(name="是否完成 0-未完成 1-已完成")
	private Integer isComplete;

	@Column(name = "COMPLETETIME")
	@Schema(name="完成时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date completeTime;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	@Schema(name="状态", hidden = true)
	private Integer state;

	@Column(name = "CREATOR")
	@Schema(name="创建人ID", hidden = true)
	private Long creator;

	@Transient
	@Schema(name="创建人ID", hidden = true)
	private String creatorName;

	@Column(name = "WORKUNIT")
	@Schema(name="工作单位ID", hidden = true)
	private Long workUnit;

	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团ID", hidden = true)
	private Long belongGroup;

	@Column(name = "CREATEDTIME")
	@Schema(name="创建时间", hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	@Column(name = "REMINDERBUSINESSTABLE")
	@Schema(name="催办业务表名")
	private String reminderBusinessTable;

	@Column(name = "REMINDERBUSINESSTABLEID")
	@Schema(name="催办业务表ID")
	private Long reminderBusinessTableId;

	@Transient
	@Schema(name = "类型名称")
	private String typeName;

	public static TblSystemRefReminderV ofId(Long id) {
		TblSystemRefReminderV tblSystemRefReminder = new TblSystemRefReminderV();
		tblSystemRefReminder.setId(id);
		return tblSystemRefReminder;
	}
}