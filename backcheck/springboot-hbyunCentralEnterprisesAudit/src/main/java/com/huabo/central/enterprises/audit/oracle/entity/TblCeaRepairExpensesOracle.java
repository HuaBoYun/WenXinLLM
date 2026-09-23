package com.huabo.central.enterprises.audit.oracle.entity;

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
 * 央企内审-综合管理-修理费支出
 */
@Schema(name="TblCeaRepairExpenses")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_REPAIR_EXPENSES")
public class TblCeaRepairExpensesOracle {
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
	 * 单位名称
	 */
	@Column(name = "REPAIRBELONGGROUP")
	@Schema(name = "单位ID")
	private Long repairBelongGroup;

	@Transient
	@Schema(name = "单位名称")
	private String repairBelongGroupName;

	/**
	 * 车号
	 */
	@Column(name = "VIN")
	@Schema(name = "车号")
	private String vin;

	/**
	 * 时间
	 */
	@Column(name = "REPAIRTIME")
	@Schema(name = "时间 格式 yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date repairTime;

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
	 * 委托单号
	 */
	@Column(name = "COMMISSIONNUMBER")
	@Schema(name = "委托单号")
	private String commissionNumber;

	/**
	 * 维修用料
	 */
	@Column(name = "MAINTENANCEMATERIALS")
	@Schema(name = "维修用料")
	private String maintenanceMaterials;

	/**
	 * 修理费合计
	 */
	@Column(name = "TOTALREPAIRCOST")
	@Schema(name = "修理费合计")
	private String totalRepairCost;

	/**
	 * 用料费合计
	 */
	@Column(name = "TOTALMATERIALCOST")
	@Schema(name = "支出金额")
	private String totalMaterialCost;

	/**
	 * 修理厂家
	 */
	@Column(name = "REPAIRMANUFACTURER")
	@Schema(name = "修理厂家")
	private String repairManufacturer;

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
	 * 故障情况
	 */
	@Column(name = "FAULTCONDITION")
	@Schema(name = "故障情况")
	private String faultCondition;

	/**
	 * 修理项目
	 */
	@Column(name = "REPAIRPROJECT")
	@Schema(name = "修理项目")
	private String repairProject;

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

	public static TblCeaRepairExpensesOracle ofId(Long id) {
		TblCeaRepairExpensesOracle tblCeaRepairExpensesOracle = new TblCeaRepairExpensesOracle();
		tblCeaRepairExpensesOracle.setId(id);
		return tblCeaRepairExpensesOracle;
	}
}