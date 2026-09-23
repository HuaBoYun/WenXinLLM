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
 * 央企内审-综合管理-销假单
 */
@Schema(name="TblCeaCancelHolidayOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_CANCEL_HOLIDAY")
public class TblCeaCancelHolidayOracle implements Serializable {

	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@Schema(name = "主键ID")
	@GeneratedValue(generator = "JDBC")
	private Long id;

	@Schema(name = "编号")
    @TableField(value = "NO")
    private String no;
	
	/**
	 * 销假人
	 */
	@Column(name = "PEOPLE")
	@Schema(name = "销假人")
	private Long people;

	@Transient
	@Schema(name = "销假人名称")
	private String peopleName;

	/**
	 * 所属部门
	 */
	@Column(name = "PEOPLEWORKUNIT")
	@Schema(name = "所属部门")
	private Long peopleWorkUnit;

	@Transient
	@Schema(name = "所属部门名称")
	private String peopleWorkUnitName;

	/**
	 * 填表日期
	 */
	@Column(name = "FILLFORMTIME")
	@Schema(name = "填表日期 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fillFormTime;

	/**
	 * 销假
	 */
	@Column(name = "CANCELHOLIDAYTYPE")
	@Schema(name = "销假")
	private String cancelHolidayType;

	/**
	 * 事由
	 */
	@Column(name = "REASONS")
	@Schema(name = "事由")
	private String reasons;

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
	@Schema(name="创建人ID名称",hidden=true)
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

	@NotNull(message = "类型：1-请假单 2-外派任务 不能为空")
	@Column(name = "CANCELTYPE")
	@Schema(name="类型：1-请假单 2-外派任务")
	private Integer cancelType;

	@NotNull(message = "关联ID-请假单ID或者外派任务ID 不能为空")
	@Column(name = "RELATIONID")
	@Schema(name="关联ID-请假单ID或者外派任务ID")
	private Long relationId;

	@Column(name = "HOLIDAYTYPE")
	@Schema(name="销假状态：超假或提前")
	private String holidayType;

	private static final long serialVersionUID = 1L;

	public static TblCeaCancelHolidayOracle ofId(Long id) {
		TblCeaCancelHolidayOracle tblCeaCancelHolidayOracle = new TblCeaCancelHolidayOracle();
		tblCeaCancelHolidayOracle.setId(id);
		return tblCeaCancelHolidayOracle;
	}
}