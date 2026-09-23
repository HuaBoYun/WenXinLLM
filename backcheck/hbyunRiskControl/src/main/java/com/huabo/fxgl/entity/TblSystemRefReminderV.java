package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

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
	@TableId(value = "ID")
	@Schema(name = "主键ID")
	private Long id;

	/**
	 * 催办人员
	 */
	@TableField("REMINDERSTAFFID")
	@Schema(name = "催办人员")
	private Long reminderStaffId;

	@TableField(exist = false)
	@Schema(name = "催办人员名称")
	private String reminderStaffName;

	/**
	 * 催办内容
	 */
	@TableField("REMINDERCONTENT")
	@Schema(name = "催办内容")
	private String reminderContent;

	@Schema(name = "业务催办时间")
	@TableField("REMINDERTIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date reminderTime;

	/**
	 * 关联ID
	 */
	@TableField("REFOPMID")
	@Schema(name = "关联ID-业务 ID")
	private Long refopmId;

	/**
	 * 是否阅读 0-未阅读 1-已阅读
	 */
	@TableField("ISREAD")
	@Schema(name = "是否阅读 0-未阅读 1-已阅读")
	private Integer isRead;

	@TableField("ISCOMPLETE")
	@Schema(name = "是否完成 0-未完成 1-已完成")
	private Integer isComplete;

	@TableField("COMPLETETIME")
	@Schema(name = "完成时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date completeTime;

	/**
	 * 状态
	 */
	@TableField("STATE")
	@Schema(name="状态",hidden=true)
	private Integer state;

	@TableField("CREATOR")
	@Schema(name="创建人ID",hidden=true)
	private Long creator;

	@TableField(exist = false)
	@Schema(name="创建人ID",hidden=true)
	private String creatorName;

	@TableField("WORKUNIT")
	@Schema(name="工作单位ID",hidden=true)
	private Long workUnit;

	@TableField("BELONGGROUP")
	@Schema(name="所属集团ID",hidden=true)
	private Long belongGroup;

	@TableField("CREATEDTIME")
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	@TableField("REMINDERBUSINESSTABLE")
	@Schema(name = "催办业务表名")
	private String reminderBusinessTable;

	@TableField("REMINDERBUSINESSTABLEID")
	@Schema(name = "催办业务表ID")
	private Long reminderBusinessTableId;

	@TableField(exist = false)
	@Schema(name="类型名称")
	private String typeName;

	public static TblSystemRefReminderV ofId(Long id) {
		TblSystemRefReminderV tblSystemRefReminder = new TblSystemRefReminderV();
		tblSystemRefReminder.setId(id);
		return tblSystemRefReminder;
	}
}