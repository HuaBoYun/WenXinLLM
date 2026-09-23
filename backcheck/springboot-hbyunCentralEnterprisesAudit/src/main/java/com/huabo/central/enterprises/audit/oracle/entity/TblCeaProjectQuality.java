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
 * 央企内生-项目评优-质量
 */
@Schema(name="TblCeaProjectQuality")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_PROJECT_QUALITY")
public class TblCeaProjectQuality implements Serializable {

	/**
	 * 主键ID
	 */
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "ID")
	@Schema(name = "主键ID")
	private Long id;
	
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

	/**
	 * 评估人员ID
	 */
	@Column(name = "ASSESSID")
	@Schema(name = "评估人员ID")
	private Long assessId;

	@Transient
	@Schema(name = "评估人员名称")
	private String assessName;

	/**
	 * 评估项目
	 */
	@Column(name = "ASSESSPROJECTID")
	@Schema(name = "评估项目")
	private Long assessProjectId;

	@Transient
	@Schema(name = "评估项目名称")
	private String assessProjectName;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	@Schema(name="状态 0-未处理 1-已提交",hidden=true)
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
	 * 评估内容JSON
	 */
	@Column(name = "ASSESSJSON")
	@Schema(name = "评估内容JSON")
	private String assessJson;

	private static final long serialVersionUID = 1L;

	public static TblCeaProjectQuality ofId(Long id) {
		TblCeaProjectQuality tblCeaProjectQuality = new TblCeaProjectQuality();
		tblCeaProjectQuality.setId(id);
		return tblCeaProjectQuality;
	}
}