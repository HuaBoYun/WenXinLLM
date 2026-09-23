package com.huabo.system.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.system.vo.result.UserInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 催办关系表
 */
@Schema(name = "TblSystemRefopmReminder")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_SYSTEM_REFOPM_REMINDER")
public class TblSystemRefopmReminder {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@Schema(name="主键ID")
	private Long id;

	/**
	 * 后来的兄弟们 不好意思 脑抽取错名字 将就一下吧
	 */
	@NotNull(message = "催办类型不能为空")
	@Column(name = "REMINDERID")
	@Schema(name="催办类型：1-自动催办 2-手动催办")
	private Integer reminderId;

	@Column(name = "REMINDERTYPE")
	@Schema(name="催办周期类型：1-月 2-周")
	private Integer reminderType;

	@Column(name = "REMINDERMONTH")
	@Schema(name="催办为月时，有值 值为每月几号 （1-31）")
	private Integer reminderMonth;

	@Column(name = "REMINDERWEEK")
	@Schema(name="催办为周时，有值 值为周几 （1-7）")
	private Integer reminderWeek;

	@Column(name = "REMINDERCONTENT")
	@Schema(name="催办内容")
	private String reminderContent;

	@Column(name = "REMINDERSTAFFIDS")
	@Schema(name="催办人员 多个用逗号隔开")
	private String reminderStaffIds;

	@Transient
	@Schema(name="催办人员名称 多个用逗号隔开")
	private List<UserInfo> reminderStaffIdsNames;

	@Column(name = "REFOPMID")
	@Schema(name="关联ID")
	private Long refopmId;

	@Column(name = "ISSTOP")
	@Schema(name = "是否停止自动催办 0-不停止 1-停止")
	private Integer isStop;

	@Column(name = "STATE")
	@Schema(name="状态 0-未催办 1-已催办", hidden = true)
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

	@Column(name = "MODULEROUTE")
	@Schema(name="模块路由")
	private String moduleRoute;

	public static TblSystemRefopmReminder ofId(Long id) {
		TblSystemRefopmReminder tblSystemRefopmReminder = new TblSystemRefopmReminder();
		tblSystemRefopmReminder.setId(id);
		return tblSystemRefopmReminder;
	}

	public static TblSystemRefopmReminder ofIsStop(Long id, Integer isStop) {
		TblSystemRefopmReminder tblSystemRefopmReminder = new TblSystemRefopmReminder();
		tblSystemRefopmReminder.setId(id);
		tblSystemRefopmReminder.setIsStop(isStop);
		return tblSystemRefopmReminder;
	}
}