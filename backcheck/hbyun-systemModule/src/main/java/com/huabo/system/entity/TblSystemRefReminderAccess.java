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
 * 催办访问记录表
 */
@Schema(name = "TblSystemRefReminderAccess")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_SYSTEM_REF_REMINDER_ACCESS")
public class TblSystemRefReminderAccess {
	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@Schema(name="主键ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
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
	 * 关联ID
	 */
	@Column(name = "REMINDERID")
	@Schema(name="关联ID")
	private Long reminderId;

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

	public static TblSystemRefReminderAccess ofId(Long id) {
		TblSystemRefReminderAccess tblSystemRefReminderAccess = new TblSystemRefReminderAccess();
		tblSystemRefReminderAccess.setId(id);
		return tblSystemRefReminderAccess;
	}
}