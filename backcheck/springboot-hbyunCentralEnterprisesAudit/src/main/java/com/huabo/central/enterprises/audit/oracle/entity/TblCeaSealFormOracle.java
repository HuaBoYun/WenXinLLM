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
 * 央企内审-综合管理-印信使用单
 */
@Schema(name="TblCeaSealFormOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_SEAL_FORM")
public class TblCeaSealFormOracle implements Serializable {
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
	 * 用印名称
	 */
	@Column(name = "SEALNAME")
	@ExcelField(title = "用印名称", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "用印名称")
	private String sealName;

	/**
	 * 用印事由
	 */
	@Column(name = "SEALREASONS")
	@Schema(name = "用印事由")
	private String sealReasons;

	/**
	 * 用印枚数
	 */
	@Column(name = "SEALNUM")
	@ExcelField(title = "用印枚数", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "用印枚数")
	private Long sealNum;

	/**
	 * 用印部门
	 */
	@Column(name = "SEALWORKUNIT")
	@Schema(name = "用印部门")
	private Long sealWorkUnit;

	@Transient
	@Schema(name = "用印部门名称")
	@ExcelField(title = "用印部门", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	private String sealWorkUnitName;

	/**
	 * 经办人
	 */
	@Column(name = "TRANSACTOR")
	@Schema(name = "经办人")
	private Long transactor;

	@Transient
	@Schema(name = "经办人名称")
	@ExcelField(title = "经办人", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	private String transactorName;

	/**
	 * 审批部门
	 */
	@Column(name = "AUDITWORKUNIT")
	@Schema(name = "审批部门")
	private Long auditWorkUnit;

	@Transient
	@Schema(name = "审批部门")
	private String auditWorkUnitName;

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
	 * 结果排序
	 */
	@Column(name = "SORT")
	@Schema(name = "结果排序")
	private Integer sort;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	@ExcelField(title = "状态", sort = 18, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.SHSTATE_TYPE)
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
	 * 发往单位
	 */
	@Column(name = "SENDORG")
	@Schema(name = "发往单位")
	private String sendorg;

	/**
	 * 签批人
	 */
	@Transient
	private String signatory;

	/**
	 * 序号
	 */
	@Transient
	private Integer serialNumber;

	private static final long serialVersionUID = 1L;

	public static TblCeaSealFormOracle ofId(Long id) {
		TblCeaSealFormOracle tblCeaSealFormOracle = new TblCeaSealFormOracle();
		tblCeaSealFormOracle.setId(id);
		return tblCeaSealFormOracle;
	}
}