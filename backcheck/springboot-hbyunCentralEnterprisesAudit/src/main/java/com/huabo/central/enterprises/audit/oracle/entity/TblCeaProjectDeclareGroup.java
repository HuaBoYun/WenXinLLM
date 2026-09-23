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
import com.huabo.central.enterprises.audit.vo.result.TblCeaProjectDeclareInfoResult;
import com.huabo.central.enterprises.audit.vo.result.UserInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 央企内审-项目评优-申报-分组
 */
@Schema(name="TblCeaProjectDeclareGroup")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_PROJECT_DECLARE_GROUP")
public class TblCeaProjectDeclareGroup implements Serializable {
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
	 * 编号
	 */
	@Column(name = "NUM")
	@Schema(name = "编号")
	private String num;

	/**
	 * 填报年度
	 */
	@Column(name = "FILLYEAR")
	@Schema(name = "填报年度")
	private String fillYear;

	/**
	 * 评审组人员IDS 多个用逗号隔开
	 */
	@Column(name = "REVIEWTEAMPERSONNELIDS")
	@Schema(name = "评审组人员IDS 多个用逗号隔开")
	private String reviewTeamPersonnelIds;

	@Transient
	@Schema(name = "评审组人员集合")
	private List<UserInfo> userInfoList;

	@Transient
	@Schema(name="申报集合")
	private List<TblCeaProjectDeclareInfoResult> projectDeclareInfoList;

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

	@Schema(name="删除申报集合IDS")
	private List<Long> deleteIds;

	private static final long serialVersionUID = 1L;

	public static TblCeaProjectDeclareGroup ofId(Long id) {
		TblCeaProjectDeclareGroup tblCeaProjectDeclareGroup = new TblCeaProjectDeclareGroup();
		tblCeaProjectDeclareGroup.setId(id);
		return tblCeaProjectDeclareGroup;
	}
}