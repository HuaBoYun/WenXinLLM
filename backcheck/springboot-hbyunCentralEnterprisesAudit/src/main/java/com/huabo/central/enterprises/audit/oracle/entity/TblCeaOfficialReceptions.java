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
 * 央企内审-综合管理-公务接待
 */
@Schema(name="TblCeaOfficialReceptions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_OFFICIAL_RECEPTIONS")
public class TblCeaOfficialReceptions implements Serializable {
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
	 * 来访事由
	 */
	@Column(name = "VISITORSREASON")
	@Schema(name = "来访事由")
	private String visitorsReason;

	/**
	 * 接待类别
	 */
	@Column(name = "RECEIVERTYPE")
	@Schema(name = "接待类别")
	private String receiverType;

	/**
	 * 来访开始时间
	 */
	@Column(name = "VISITORSSTARTTIME")
	@Schema(name = "来访开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date visitorsStartTime;

	/**
	 * 来访结束时间
	 */
	@Column(name = "VISITORSENDTIME")
	@Schema(name = "来访结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date visitorsEndTime;

	/**
	 * 来宾人数
	 */
	@Column(name = "VISITORSNUM")
	@Schema(name = "来宾人数")
	private Integer visitorsNum;

	/**
	 * 党委办公室协助安排事宜
	 */
	@Column(name = "ARRANGEMATTERS")
	@Schema(name = "党委办公室协助安排事宜")
	private String arrangeMatters;

	/**
	 * 陪同人员ID
	 */
	@Column(name = "ACCOMPANYID")
	@Schema(name = "陪同人员ID")
	private Long accompanyId;

	@Column(name = "ACCOMPANYNAME")
	@Schema(name = "陪同人员名称")
	private String accompanyName;

	/**
	 * 申请单位负责人员ID
	 */
	@Column(name = "APPLYBELONGGROUPCREATOR")
	@Schema(name = "申请单位负责人员ID")
	private Long applyBelongGroupCreator;

	@Transient
	@Schema(name = "申请单位负责人员名称")
	private String applyBelongGroupCreatorName;

	/**
	 * 党委办公室负责人ID
	 */
	@Column(name = "OFFICEMANAGERID")
	@Schema(name = "党委办公室负责人ID")
	private Long officeManagerId;

	@Column(name = "OFFICEMANAGERNAME")
	@Schema(name = "党委办公室负责人名称")
	private String officeManagerName;

	/**
	 * 申请单位经办人ID
	 */
	@Column(name = "APPLYBELONGGROUPOPERATOR")
	@Schema(name = "申请单位经办人ID")
	private Long applyBelongGroupOperator;

	@Transient
	@Schema(name = "申请单位经办人名称")
	private String applyBelongGroupOperatorName;

	/**
	 * 联系电话
	 */
	@Column(name = "CONTACTNUMBER")
	@Schema(name = "联系电话")
	private String contactNumber;

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

	/**
	 * 主要来访人员情况JSON
	 */
	@Column(name = "VISITORSJSON")
	@Schema(name = "主要来访人员情况JSON")
	private String visitorsJson;

	private static final long serialVersionUID = 1L;

	public static TblCeaOfficialReceptions ofId(Long id) {
		TblCeaOfficialReceptions tblCeaOfficialReceptions = new TblCeaOfficialReceptions();
		tblCeaOfficialReceptions.setId(id);
		return tblCeaOfficialReceptions;
	}
}