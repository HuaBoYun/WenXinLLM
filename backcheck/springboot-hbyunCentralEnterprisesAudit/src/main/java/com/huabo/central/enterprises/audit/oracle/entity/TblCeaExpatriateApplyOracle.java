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
 * 央企内审-综合管理-外派任务
 */
@Schema(name="TblCeaExpatriateApplyOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_EXPATRIATE_APPLY")
public class TblCeaExpatriateApplyOracle implements Serializable {
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
	 * 申请人
	 */
	@Column(name = "APPLYPEOPLE")
	@Schema(name = "申请人")
	private Long applyPeople;

	@Transient
	@Schema(name = "申请人名称")
	private String applyPeopleName;

	/**
	 * 部门
	 */
	@Column(name = "APPLYWORKUNIT")
	@Schema(name = "部门")
	private Long applyWorkUnit;

	@Transient
	@Schema(name = "部门名称")
	private String applyWorkUnitName;

	/**
	 * 岗位
	 */
	@Column(name = "POST")
	@Schema(name = "岗位")
	private String post;

	/**
	 * 申请外出时间
	 */
	@Column(name = "APPLYEXPATRIATETIME")
	@Schema(name = "申请外出时间 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date applyExpatriateTime;

	@Transient
	private String applyExpatriateTime1;

	/**
	 * 申请返回时间
	 */
	@Column(name = "APPLYRETURNTIME")
	@Schema(name = "申请返回时间 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date applyReturnTime;

	@Transient
	private String applyReturnTime1;

	/**
	 * 实际返回时间
	 */
	@Column(name = "ACTUALRETURNTIME")
	@Schema(name = "实际返回时间 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date actualReturnTime;

	/**
	 * 外出事由
	 */
	@Column(name = "EXPATRIATEREASON")
	@Schema(name = "外出事由")
	private String expatriateReason;

	@Column(name = "USEBELONGGROUP")
	@Schema(name="用人单位（手填）")
	private String useBelongGroup;

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

	/**
	 * 预计请假天数
	 */
	@Column(name = "LEAVEDAYS")
	@Schema(name = "预计外出天数")
	private Integer leavedays;

	/**
	 * 实际请假天数
	 */
	@Column(name = "ACTUALLEAVEDAYS")
	@Schema(name = "实际外出天数")
	private Integer actualleavedays;

	@Transient
	@Schema(name="关联ID-销假单ID")
	private Long relationId;

	private static final long serialVersionUID = 1L;

	public static TblCeaExpatriateApplyOracle ofId(Long id) {
		TblCeaExpatriateApplyOracle tblCeaExpatriateApplyOracle = new TblCeaExpatriateApplyOracle();
		tblCeaExpatriateApplyOracle.setId(id);
		return tblCeaExpatriateApplyOracle;
	}
}