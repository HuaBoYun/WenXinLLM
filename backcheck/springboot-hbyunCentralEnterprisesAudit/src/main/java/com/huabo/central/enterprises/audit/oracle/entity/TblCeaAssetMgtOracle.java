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
import com.huabo.central.enterprises.audit.util.excel.DictMapUtil;
import com.huabo.central.enterprises.audit.util.excel.annotation.ExcelField;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 央企内审-综合管理-资产管理
 */
@Schema(name="TblCeaAssetMgtOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_ASSET_MGT")
public class TblCeaAssetMgtOracle implements Serializable {

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
	 * 类别：上市，未上市
	 */
	@Column(name = "ASSETTYPE")
	@Schema(name = "类别：0-上移资产，1-审计部上市 2-审计部未上市 3-审计中心上市 4-审计中心未上市")
	@ExcelField(title = "类别", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.ASSET_TYPE)
	private String assetType;

	/**
	 * 资产编码
	 */
	@Column(name = "ASSETCODE")
	@ExcelField(title = "资产编码", sort = 1, column = 1, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "资产编码")
	private String assetCode;

	/**
	 * 资产名称
	 */
	@Column(name = "ASSETNAME")
	@ExcelField(title = "资产名称", sort = 2, column = 2, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "资产名称")
	private String assetName;

	/**
	 * 规格型号
	 */
	@Column(name = "SPECIFICATIONTYPE")
	@ExcelField(title = "规格型号", sort = 3, column = 3, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "规格型号")
	private String specificationType;

	/**
	 * 制造厂家
	 */
	@Column(name = "MANUFACTURER")
	@ExcelField(title = "制造厂家", sort = 4, column = 4, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "制造厂家")
	private String manufacturer;

	/**
	 * 所属单位编码
	 */
	@Column(name = "UNITCODE")
	@ExcelField(title = "所属单位编码", sort = 5, column = 5, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "所属单位编码")
	private String unitCode;

	/**
	 * 所属单位名称
	 */
	@Column(name = "UNITNAME")
	@ExcelField(title = "所属单位名称", sort = 6, column = 6, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "所属单位名称")
	private String unitName;

	/**
	 * 车牌井号
	 */
	@Column(name = "LICENSEPLATE")
	@ExcelField(title = "车牌井号", sort = 7, column = 7, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "车牌井号")
	private String licensePlate;

	/**
	 * 自编号
	 */
	@Column(name = "SELFNUM")
	@ExcelField(title = "自编号", sort = 8, column = 8, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "自编号")
	private String selfNum;

	/**
	 * 出厂编号
	 */
	@Column(name = "FACTORYNUMBER")
	@ExcelField(title = "出厂编号", sort = 9, column = 9, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "出厂编号")
	private String factoryNumber;

	/**
	 * 出厂、建筑或完井日期
	 */
	@Column(name = "FACTORYTIME")
	@Schema(name = "出厂、建筑或完井日期  格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date factoryTime;

	@Transient
	@ExcelField(title = "出厂、建筑或完井日期 格式：yyyy/MM/dd", sort = 10, column = 10, align = ExcelField.Align.CENTER, width = 3000)
	private String factoryTimeString;

	/**
	 * 投产日期
	 */
	@Column(name = "PRODUCTIONTIME")
	@Schema(name = "投产日期  格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date productionTime;

	@Transient
	@ExcelField(title = "投产日期 格式：yyyy/MM/dd", sort = 11, column = 11, align = ExcelField.Align.CENTER, width = 3000)
	private String productionTimeString;

	/**
	 * 区块
	 */
	@Column(name = "BLOCK")
	@Schema(name = "区块")
	@ExcelField(title = "区块", sort = 12, column = 12, align = ExcelField.Align.CENTER, width = 3000)
	private String block;

	/**
	 * 区块名称
	 */
	@Column(name = "BLOCKNAME")
	@Schema(name = "区块名称")
	@ExcelField(title = "区块名称", sort = 13, column = 13, align = ExcelField.Align.CENTER, width = 3000)
	private String blockName;

	/**
	 * 存放（安装）地点
	 */
	@Column(name = "DEPOSITPLACE")
	@Schema(name = "存放（安装）地点")
	@ExcelField(title = "存放（安装）地点", sort = 14, column = 14, align = ExcelField.Align.CENTER, width = 3000)
	private String depositPlace;

	/**
	 * 计量单位
	 */
	@Column(name = "MEASUREMENT")
	@Schema(name = "计量单位")
	@ExcelField(title = "计量单位", sort = 15, column = 15, align = ExcelField.Align.CENTER, width = 3000)
	private String measurement;

	/**
	 * 复合数量
	 */
	@Column(name = "COMPOSITEQUANTITY")
	@Schema(name = "复合数量")
	@ExcelField(title = "复合数量", sort = 16, column = 16, align = ExcelField.Align.CENTER, width = 3000)
	private String compositeQuantity;

	/**
	 * 功率能力
	 */
	@Column(name = "POWERCAPACITY")
	@Schema(name = "功率能力")
	@ExcelField(title = "功率能力", sort = 17, column = 17, align = ExcelField.Align.CENTER, width = 3000)
	private String powerCapacity;

	/**
	 * 技术状况名称
	 */
	@Column(name = "TECHNICALCONDITIONNAME")
	@Schema(name = "技术状况名称")
	@ExcelField(title = "技术状况名称", sort = 18, column = 18, align = ExcelField.Align.CENTER, width = 3000)
	private String technicalConditionName;

	/**
	 * 使用状态名称
	 */
	@Column(name = "USAGESTATUSNAME")
	@Schema(name = "使用状态名称")
	@ExcelField(title = "使用状态名称", sort = 19, column = 19, align = ExcelField.Align.CENTER, width = 3000)
	private String usageStatusName;

	/**
	 * 增加原因名称
	 */
	@Column(name = "ADDCAUSENAME")
	@ExcelField(title = "增加原因名称", sort = 20, column = 20, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "增加原因名称")
	private String addCauseName;

	/**
	 * 资金渠道名称
	 */
	@Column(name = "FUNDSCHANNELNAME")
	@ExcelField(title = "资金渠道名称", sort = 21, column = 21, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "资金渠道名称")
	private String fundSchannelName;

	/**
	 * 增加日期
	 */
	@Column(name = "ADDTIME")
	@Schema(name = "增加日期  格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date addTime;

	@Transient
	@ExcelField(title = "增加日期 格式：yyyy/MM/dd", sort = 22, column = 22, align = ExcelField.Align.CENTER, width = 3000)
	private String addTimeString;

	/**
	 * 停产日期
	 */
	@Column(name = "DISCONTINUEDTIME")
	@Schema(name = "停产日期  格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date discontinuedTime;

	@Transient
	@ExcelField(title = "停产日期 格式：yyyy/MM/dd", sort = 23, column = 23, align = ExcelField.Align.CENTER, width = 3000)
	private String discontinuedTimeString;

	/**
	 * 保管人
	 */
	@Column(name = "CUSTODIAN")
	@Schema(name = "保管人")
	private Long custodian;

	@Transient
	@Schema(name = "保管人名称")
	@ExcelField(title = "保管人", sort = 24, column = 24, align = ExcelField.Align.CENTER, width = 3000)
	private String custodianName;

	/**
	 * 期末原值
	 */
	@Column(name = "FINALORIGINALVALUE")
	@Schema(name = "期末原值")
	@ExcelField(title = "期末原值", sort = 25, column = 25, align = ExcelField.Align.CENTER, width = 3000)
	private String finalOriginalValue;

	/**
	 * 期末累计折旧
	 */
	@Column(name = "FINALCUMULATIVEDEPRECIATION")
	@Schema(name = "期末累计折旧")
	@ExcelField(title = "期末累计折旧", sort = 26, column = 26, align = ExcelField.Align.CENTER, width = 3000)
	private String finalCumulativeDepreciation;

	/**
	 * 期末净值
	 */
	@Column(name = "FINALNETWORTH")
	@Schema(name = "期末净值")
	@ExcelField(title = "期末净值", sort = 27, column = 27, align = ExcelField.Align.CENTER, width = 3000)
	private String finalNetWorth;

	/**
	 * 期末减值准备
	 */
	@Column(name = "FINALIMPAIRMENTPREPARE")
	@Schema(name = "期末减值准备")
	@ExcelField(title = "期末减值准备", sort = 28, column = 28, align = ExcelField.Align.CENTER, width = 3000)
	private String finalImpairmentPrepare;

	/**
	 * 在用人员
	 */
	@Column(name = "USEPEOPLE")
	@Schema(name = "在用人员")
	private Long usePeople;

	@Transient
	@Schema(name = "在用人员名称")
	@ExcelField(title = "在用人员名称", sort = 29, column = 29, align = ExcelField.Align.CENTER, width = 3000)
	private String usePeopleName;

	/**
	 * 在用部门
	 */
	@Column(name = "USEDEPARTMENT")
	@Schema(name = "在用部门")
	private Long useDepartment;

	@Transient
	@Schema(name = "在用部门名称")
	@ExcelField(title = "在用部门名称", sort = 30, column = 30, align = ExcelField.Align.CENTER, width = 3000)
	private String useDepartmentName;

	/**
	 * 特殊事项
	 */
	@Column(name = "SPECIALMATTER")
	@Schema(name = "特殊事项")
	@ExcelField(title = "特殊事项", sort = 31, column = 31, align = ExcelField.Align.CENTER, width = 3000)
	private String specialMatter;

	/**
	 * 备注
	 */
	@Column(name = "REMARK")
	@Schema(name = "备注")
	@ExcelField(title = "备注", sort = 32, column = 32, align = ExcelField.Align.CENTER, width = 3000)
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

	private static final long serialVersionUID = 1L;

	public static TblCeaAssetMgtOracle ofId(Long id) {
		TblCeaAssetMgtOracle tblCeaAssetMgtOracle = new TblCeaAssetMgtOracle();
		tblCeaAssetMgtOracle.setId(id);
		return tblCeaAssetMgtOracle;
	}
}