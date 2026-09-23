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
 * 央企内审-综合管理-办公经费申请
 */
@Schema(name="TblCeaOfficeExpensesOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_OFFICE_EXPENSES")
public class TblCeaOfficeExpensesOracle implements Serializable {

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
	 * 支出原因
	 */
	@Column(name = "REASON")
	@Schema(name = "支出原因")
	private String reason;

	/**
	 * 支出明细
	 */
	@Column(name = "DETAILS")
	@Schema(name = "支出明细")
	private String details;

	/**
	 * 预计支出金额
	 */
	@Column(name = "AMOUNT")
	@Schema(name = "预计支出金额")
	private String amount;
	
	/**
	 * 实际支出金额
	 */
	@Column(name = "ACTAMOUNT")
	@Schema(name = "实际支出金额")
	private String actamount;

	/**
	 * 供货单位
	 */
	@Column(name = "SUPPLYBELONGGROUP")
	@Schema(name = "供货单位")
	private Long supplyBelongGroup;

	@Column(name = "SUPPLYBELONGGROUPNAME")
	@Schema(name = "供货单位名称")
	private String supplyBelongGroupName;

	/**
	 * 时间
	 */
	@Column(name = "OFFICEEXPENSESTIME")
	@Schema(name = "时间 格式yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date officeExpensesTime;

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
	 * 科室负责人
	 */
	@Column(name = "DEPARTMENTHEAD")
	@Schema(name = "科室负责人")
	private Long departmentHead;

	@Transient
	@Schema(name = "科室负责人名称")
	private String departmentHeadName;

	/**
	 * 上传文件ids 多个逗号隔开
	 */
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	@Column(name = "APPLYBELONGGROUP")
	@Schema(name = "申请单位")
	private Long applyBelongGroup;

	@Transient
	@Schema(name = "申请单位名称")
	private String applyBelongGroupName;

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

	private static final long serialVersionUID = 1L;

	public static TblCeaOfficeExpensesOracle ofId(Long id) {
		TblCeaOfficeExpensesOracle tblCeaOfficeExpensesOracle = new TblCeaOfficeExpensesOracle();
		tblCeaOfficeExpensesOracle.setId(id);
		return tblCeaOfficeExpensesOracle;
	}
}