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
 * 央企内审-综合管理-IP地址管理
 */
@Schema(name="TblCeaIpManageOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_IP_MANAGE")
public class TblCeaIpManageOracle {
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
	 * 部门/单位
	 */
	@Column(name = "APPLYBELONGGROUP")
	@Schema(name = "部门/单位")
	private Long applyBelongGroup;

	@Transient
	@Schema(name = "部门/单位 名称")
	private String applyBelongGroupName;

	/**
	 * 科室
	 */
	@Column(name = "APPLYWORKUNIT")
	@Schema(name = "科室")
	private Long applyWorkUnit;

	@Transient
	@Schema(name = "科室名称")
	private String applyWorkUnitName;

	/**
	 * 办公区
	 */
	@Column(name = "OFFICEAREA")
	@Schema(name = "办公区")
	private String officeArea;

	/**
	 * 房间号
	 */
	@Column(name = "ROOMNUMBER")
	@Schema(name = "房间号")
	private String roomNumber;

	/**
	 * 设备类型
	 */
	@Column(name = "EQUIPMENTTYPE")
	@Schema(name = "设备类型")
	private String equipmentType;

	/**
	 * 外网权限
	 */
	@Column(name = "EXTERNALNETWORKPERMISSIONS")
	@Schema(name = "外网权限")
	private String externalNetworkPermissions;

	/**
	 * 使用期限开始
	 */
	@Column(name = "APPLYPERIODTIMESTART")
	@Schema(name = "使用期限开始 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date applyPeriodTimeStart;

	/**
	 * 使用期限结束
	 */
	@Column(name = "APPLYPERIODTIMEEND")
	@Schema(name = "使用期限结束 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date applyPeriodTimeEnd;

	/**
	 * 申请时间
	 */
	@Column(name = "APPLYTIME")
	@Schema(name = "申请时间 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date applyTime;

	/**
	 * 用途
	 */
	@Column(name = "PURPOSE")
	@Schema(name = "用途")
	private String purpose;

	@Column(name = "NETWORKINTERFACE")
	@Schema(name = "网络接口")
	private String networkInterface;

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

	public static TblCeaIpManageOracle ofId(Long id) {
		TblCeaIpManageOracle ipManageOracle = new TblCeaIpManageOracle();
		ipManageOracle.setId(id);
		return ipManageOracle;
	}
}