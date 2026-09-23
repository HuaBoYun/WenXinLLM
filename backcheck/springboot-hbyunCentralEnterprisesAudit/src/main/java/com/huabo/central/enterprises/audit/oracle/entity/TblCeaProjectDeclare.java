package com.huabo.central.enterprises.audit.oracle.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.central.enterprises.audit.util.excel.annotation.ExcelField;
import com.huabo.central.enterprises.audit.vo.result.ReviewTeamJsonResult;
import com.huabo.central.enterprises.audit.vo.result.UserInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 央企内审-项目评优-申报
 */
@Schema(name="TblCeaProjectDeclare")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_PROJECT_DECLARE")
public class TblCeaProjectDeclare implements Serializable {

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
	 * 关联ID-项目管理实施方案ID
	 */
	@NotNull(message = "项目管理实施方案ID不能为空")
	@Column(name = "IMPLEMENTATIONPLANID")
	@Schema(name = "关联ID-项目管理实施方案ID")
	private Long implementationPlanId;

	@Transient
	@ExcelField(title = "项目名称", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "关联ID-项目管理实施方案-名称")
	private String implementationProjectName;

	@Transient
	@ExcelField(title = "主审", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "关联ID-项目管理实施方案-主审")
	private String implementationPlanMainReviewer;

	@Transient
	@ExcelField(title = "组长", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "关联ID-项目管理实施方案-组长")
	private String implementationPlanTeamLeader;

	@Transient
	@ExcelField(title = "副组长", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "关联ID-项目管理实施方案-副组长")
	private String implementationPlanTeamfzzname;

	@Transient
	@Schema(name = "关联ID-项目管理实施方案-参审人")
	private List<UserInfo> implementationPlanReviewers;

	@Transient
	@ExcelField(title = "项目类型", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "关联ID-项目管理实施方案-项目类型")
	private String implementationProjectType;

	@Transient
	@Schema(name = "关联ID-项目管理实施方案-项目负责人ID")
	private Long implementationProjectOrderId;

	@Transient
	@Schema(name = "关联ID-项目管理实施方案-项目负责人名称")
	private String implementationProjectOrderName;

	/**
	 * 审批单位
	 */
	@Column(name = "APPROVALBELONGGROUP")
	@Schema(name = "审批单位")
	private Long approvalBelongGroup;

	@Transient
	@Schema(name = "审批单位名称")
	private String approvalBelongGroupName;

	/**
	 * 完成单位
	 */
	@Column(name = "COMPLETEBELONGGROUP")
	@Schema(name = "完成单位")
	private Long completeBelongGroup;

	@Transient
	@ExcelField(title = "实施单位", sort = 1, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "完成单位名称")
	private String completeBelongGroupName;

	/**
	 * 主要特点
	 */
	@Column(name = "MAINFEATURES")
	@Schema(name = "主要特点")
	private String mainFeatures;

	/**
	 * 主要特点
	 */
	@Column(name = "MAINFEATURES1")
	@ExcelField(title = "项目主要成果及特点", sort = 7, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "项目主要成果及特点")
	private String mainFeatures1;

	/**
	 * 上传文件ids 多个逗号隔开
	 */
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	/**
	 * 是否废弃 0-否 1-是
	 */
	@Column(name = "FLAGABANDONED")
	@Schema(name = "是否废弃 0-否 1-是")
	private Integer flagAbandoned;

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
	 * 审计成果
	 */
	@Column(name = "CONTENT")
	@Schema(name = "审计成果")
	private String content;

	/**
	 * 结果排序
	 */
	@Column(name = "RESULTSORT")
	@Schema(name = "结果排序")
	private Integer resultSort;

	/**
	 * 最终排序
	 */
	@Column(name = "FINALSORT")
	@Schema(name = "最终排序")
	private Integer finalSort;

	/**
	 * 评审组评分JSON
	 */
	@Column(name = "REVIEWTEAMJSON")
	@Schema(name="评审组评分JSON",hidden=true)
	private String reviewTeamJson;

	@Schema(name="申报项目-评审组评分")
	@Transient
	private List<ReviewTeamJsonResult> reviewTeamList;

	@Schema(name="分组ID")
	@Column(name = "GROUPID")
	private Long groupId;

	private static final long serialVersionUID = 1L;

	public static TblCeaProjectDeclare ofId(Long id) {
		TblCeaProjectDeclare tblCeaProjectDeclare = new TblCeaProjectDeclare();
		tblCeaProjectDeclare.setId(id);
		return tblCeaProjectDeclare;
	}

	public static TblCeaProjectDeclare ofGroupId(Long groupId) {
		TblCeaProjectDeclare tblCeaProjectDeclare = new TblCeaProjectDeclare();
		tblCeaProjectDeclare.setGroupId(groupId);
		return tblCeaProjectDeclare;
	}

	@Transient
	@ExcelField(title = "参审人员", sort = 6, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "参审人员")
	private String csry;

	@Transient
	@ExcelField(title = "核减金额", sort = 8, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "核减金额")
	private Integer hjje;

	/**
	 * 审计类型
	 */
	@Transient
	@ExcelField(title = "项目类型", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	private String sjlxName;
}