package com.huabo.central.enterprises.audit.oracle.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
 * 央企内审-项目-评优
 */
@Schema(name="TblCeaProjectAppraising")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_PROJECT_APPRAISING")
public class TblCeaProjectAppraising implements Serializable {

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
	 * 评优年度
	 */
	@Column(name = "APPRAISINGYEAR")
	@Schema(name = "评优年度")
	private String appraisingYear;

	/**
	 * 评优名称
	 */
	@Column(name = "APPRAISINGNAME")
	@Schema(name = "评优名称")
	private String appraisingName;

	/**
	 * 申报单位
	 */
	@Column(name = "DECLAREBELONGGROUP")
	@Schema(name = "申报单位")
	private Long declareBelongGroup;

	@Transient
	@Schema(name = "申报单位")
	private String declareBelongGroupName;

	/**
	 * 联系人ID
	 */
	@Column(name = "CONTACTID")
	@Schema(name = "联系人ID")
	private Long contactId;

	@Transient
	@Schema(name = "联系人名称")
	private String contactName;

	/**
	 * 联系电话
	 */
	@Column(name = "CONTACTNUMBER")
	@Schema(name = "联系电话")
	private String contactNumber;

	/**
	 * 项目状态的参评要求
	 */
	@Column(name = "PROJECTSTATEREQUIREMENT")
	@Schema(name = "项目状态的参评要求")
	private String projectStateRequirement;

	/**
	 * 项目启动时间的参数要求
	 */
	@Column(name = "PROJECTSTARTREQUIREMENT")
	@Schema(name = "项目启动时间的参数要求")
	private String projectStartRequirement;

	/**
	 * 关联ID-项目管理实施方案ID
	 */
	@Column(name = "IMPLEMENTATIONPLANID")
	@Schema(name = "关联ID-项目管理实施方案ID")
	private String implementationPlanId;

	@Transient
	@Schema(name = "关联ID-项目管理实施方案对象")
	private List<TblCeaProjectDeclare> implementationPlanList;

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

	private static final long serialVersionUID = 1L;

	public static TblCeaProjectAppraising ofId(Long id) {
		TblCeaProjectAppraising tblCeaProjectAppraising = new TblCeaProjectAppraising();
		tblCeaProjectAppraising.setId(id);
		return tblCeaProjectAppraising;
	}
}