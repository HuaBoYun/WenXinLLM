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
import com.huabo.central.enterprises.audit.vo.result.UserInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 央企内审-综合管理-群组
 */
@Schema(name="TblCeaGroupOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_GROUP")
public class TblCeaGroupOracle implements Serializable {

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
	 * 群组名称
	 */
	@Column(name = "GROUPNAME")
	@Schema(name = "群组名称")
	private String groupName;

	/**
	 * 人员ID
	 */
	@Column(name = "STAFFID")
	@Schema(name = "人员ID")
	private Long staffId;

	@Transient
	@Schema(name = "人员ID")
	private String staffName;

	/**
	 * 人员IDS
	 */
	@Column(name = "STAFFIDS")
	@Schema(name = "人员IDS")
	private String staffIds;

	@Transient
	@Schema(name="多个用户人员")
	private List<UserInfo> staffs;

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

	public static TblCeaGroupOracle ofId(Long id) {
		TblCeaGroupOracle tblCeaGroupOracle = new TblCeaGroupOracle();
		tblCeaGroupOracle.setId(id);
		return tblCeaGroupOracle;
	}
}