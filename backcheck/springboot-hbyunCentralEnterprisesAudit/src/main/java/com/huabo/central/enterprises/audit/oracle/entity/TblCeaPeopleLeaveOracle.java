package com.huabo.central.enterprises.audit.oracle.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 央企内审-综合管理-人员请假单
 */
@Schema(name="TblCeaPeopleLeaveOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_PEOPLE_LEAVE")
public class TblCeaPeopleLeaveOracle implements Serializable {
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
	 * 员工ID
	 */
	@NotNull(message = "staffId 员工ID 不能为空")
	@Column(name = "STAFFID")
	@Schema(name = "员工ID")
	private Long staffId;

	@Transient
	@Schema(name = "用户信息")
	private TblStaffOracle staff;

	/**
	 * 员工编号
	 */
	@Column(name = "STAFFNUMBER")
	@Schema(name = "员工编号")
	private String staffNumber;

	/**
	 * 请假期限开始
	 */
	@Column(name = "LEAVEPERIODTIMESTART")
	@Schema(name = "请假期限开始 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date leavePeriodTimeStart;

	/**
	 * 请假期限结束
	 */
	@Column(name = "LEAVEPERIODTIMEEND")
	@Schema(name = "请假期限结束 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date leavePeriodTimeEnd;

	/**
	 * 请假事由
	 */
	@Column(name = "LEAVEREASON")
	@Schema(name = "请假事由")
	private String leaveReason;

	/**
	 * 简要说明
	 */
	@Column(name = "REMARK")
	@Schema(name = "简要说明")
	private String remark;

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
	 * 性别
	 */
	@Column(name = "STAFFSEX")
	@Schema(name = "性别")
	private String staffsex;

	/**
	 * 出生日期
	 */
	@Column(name = "BIRTHDAY")
	@Schema(name = "出生日期 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;

	/**
	 * 参工时间
	 */
	@NotNull(message = "参工时间必填")
	@Column(name = "PARWORKDATE")
	@Schema(name = "参工时间 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date parworkdate;

	@Transient
	private String parworkdate1;
	/**
	 * 累计工作时间(年)
	 */
	@Column(name = "ACOTWORKTIME")
	@Schema(name = "累计工作时间(年)")
	private String acotworktime;

	/**
	 * 单位及职务
	 */
	@Column(name = "ORGDUTIE")
	@Schema(name = "单位及职务")
	private String orgdutie;

	/**
	 * 休假地点
	 */
	@Column(name = "HOLIDAYLOCATION")
	@Schema(name = "休假地点")
	private String holidaylocation;

	/**
	 * 预计请假天数
	 */
	@Column(name = "LEAVEDAYS")
	@Schema(name = "预计请假天数")
	private Integer leavedays;

	/**
	 * 实际请假天数
	 */
	@Column(name = "ACTUALLEAVEDAYS")
	@Schema(name = "实际请假天数")
	private Integer actualleavedays;

	@Transient
	@Schema(name="关联ID-销假单ID")
	private Long relationId;

	@Transient
	@Schema(name="所属部门")
	private String orgname;

	@Transient
	private String leaveTimeString;

	@Transient
	private String userName;

	/**
	 * 相差天数
	 */
	@Transient
	private Integer differDays;

	private static final long serialVersionUID = 1L;

	public static TblCeaPeopleLeaveOracle ofId(Long id) {
		TblCeaPeopleLeaveOracle tblCeaPeopleLeaveOracle = new TblCeaPeopleLeaveOracle();
		tblCeaPeopleLeaveOracle.setId(id);
		return tblCeaPeopleLeaveOracle;
	}
}