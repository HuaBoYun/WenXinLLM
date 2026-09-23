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
import com.huabo.central.enterprises.audit.util.excel.annotation.ExcelField;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 央企内审-综合管理-资产调剂申请
 */
@Schema(name="TblCeaAssetAdjustmentOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_ASSET_ADJUSTMENT")
public class TblCeaAssetAdjustmentOracle implements Serializable {
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
	 * 关联ID（资产管理ID）
	 */
	@Column(name = "ASSETMGTID")
	@Schema(name = "关联ID（资产管理ID）")
	private Long assetMgtId;

	/**
	 * 数量
	 */
	@Column(name = "QUANTITY")
	@Schema(name = "数量")
	private String quantity;

	/**
	 * 己用年限
	 */
	@Column(name = "USEAGETIME")
	@Schema(name = "己用年限 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date useAgeTime;

	/**
	 * 效用年限
	 */
	@Column(name = "UTILITYAGETIME")
	@Schema(name = "效用年限 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date utilityAgeTime;

	/**
	 * 在用单位
	 */
	@Column(name = "USEBELONGGROUP")
	@Schema(name = "在用单位")
	private Long useBelongGroup;

	@Transient
	@Schema(name = "在用单位名称")
	private String useBelongGroupName;

	/**
	 * 原单位
	 */
	@Column(name = "ORIGINALBELONGGROUP")
	@Schema(name = "原单位")
	private Long originalBelongGroup;

	@Transient
	@Schema(name = "原单位")
	private String originalBelongGroupName;

	/**
	 * 原使用单位
	 */
	@Column(name = "ORIGINALUSEBELONGGROUP")
	@Schema(name = "原使用单位")
	private Long originalUseBelongGroup;

	@Transient
	@Schema(name = "原使用单位名称")
	private String originalUseBelongGroupName;

	/**
	 * 待调剂单位
	 */
	@Column(name = "STAYADJUSTEDBELONGGROUP")
	@Schema(name = "待调剂单位")
	private Long stayAdjustedBelongGroup;

	@Transient
	@Schema(name = "待调剂单位名称")
	private String stayAdjustedBelongGroupName;

	/**
	 * 原值
	 */
	@Column(name = "ORIGINALVALUE")
	@Schema(name = "原值")
	private String originalValue;

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

	@Transient
	@Schema(name = "资产名称")
	private String assetName;

	@Transient
	@Schema(name = "规格型号")
	private String specificationType;

	@Transient
	@Schema(name = "计量单位")
	private String measurement;
	
	/**
	 * 复合数量
	 */
	@Column(name = "COMPOSITEQUANTITY")
	@Schema(name = "工作单位ID")
	private String compositeQuantity;
	
	/**
	 * 投产日期
	 */
	@Column(name = "PRODUCTIONTIME")
	@Schema(name = "投产日期")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date productionTime;

//	@Transient
//	@Schema(name = "复合数量")
//	private String compositeQuantity;
//
//	@Transient
//	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//	@DateTimeFormat(pattern = "yyyy-MM-dd")
//	@Schema(name = "投产日期 格式：yyyy-MM-dd")
//	private Date productionTime;

	@Transient
	@Schema(name = "停产日期  格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date discontinuedTime;
	
	/**
	 * 使用人员
	 */
	@Column(name = "USESTAFF")
	@Schema(name = "使用人员")
	private Long useStaff;

	@Transient
	@Schema(name = "使用人员名称")
	@ExcelField(title = "使用人员名称", sort = 29, column = 29, align = ExcelField.Align.CENTER, width = 3000)
	private String useStaffName;

	private static final long serialVersionUID = 1L;

	public static TblCeaAssetAdjustmentOracle ofId(Long id) {
		TblCeaAssetAdjustmentOracle tblCeaAssetAdjustmentOracle = new TblCeaAssetAdjustmentOracle();
		tblCeaAssetAdjustmentOracle.setId(id);
		return tblCeaAssetAdjustmentOracle;
	}
}