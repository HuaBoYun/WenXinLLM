package com.huabo.system.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设置-智能体主表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SYSTEM_AGENT_INFO")
public class SystemAgentInfo implements Serializable {
	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@Schema(name= "主键ID")
	private Long id;

	/**
	 * 智能体名称
	 */
	@Column(name = "AGENTNAME")
	@Schema(name= "智能体名称")
	private String agentName;

	/**
	 * 智能体编号
	 */
	@Column(name = "AGENTNO")
	@Schema(name= "智能体编号")
	private String agentNo;

	/**
	 * 智能体分类
	 */
	@Column(name = "AGENTTYPE")
	@Schema(name= "智能体分类")
	private String agentType;

	/**
	 * 描述
	 */
	@Column(name = "AGENTPICTURE")
	@Schema(name= "头像")
	private String agentPicture;

	/**
	 * 头像连接
	 */
	@Column(name = "DESCRIBE")
	@Schema(name= "描述")
	private String describe;

	/**
	 * 智能体跳转地址
	 */
	@Column(name = "JUMPADDRESS")
	@Schema(name= "智能体跳转地址")
	private String jumpAddress;

	/**
	 * 模块路由
	 */
	@Column(name = "MODULEROUTE")
	@Schema(name= "模块路由")
	private String moduleRoute;

	@Column(name = "AGENTID")
	@Schema(name= "扩展字段")
	private String agentId;

	/**
	 * 是否下发 1-下发 0-未下发
	 */
	@Column(name = "FLAGISSUED")
	@Schema(name= "是否下发 1-下发 0-未下发")
	private Integer flagIssued;

	@Column(name = "STATE")
	@Schema(name= "状态 0-未催办 1-已催办", hidden = true)
	private Integer state;

	@Column(name = "CREATOR")
	@Schema(name= "创建人ID", hidden = true)
	private Long creator;

	@Transient
	@Schema(name= "创建人ID", hidden = true)
	private String creatorName;

	@Column(name = "WORKUNIT")
	@Schema(name= "工作单位ID", hidden = true)
	private Long workUnit;

	@Column(name = "BELONGGROUP")
	@Schema(name= "所属集团ID", hidden = true)
	private Long belongGroup;

	@Column(name = "CREATEDTIME")
	@Schema(name= "创建时间", hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	private static final long serialVersionUID = 1L;

	public static SystemAgentInfo ofId(Long id) {
		SystemAgentInfo systemAgentInfo = new SystemAgentInfo();
		systemAgentInfo.setId(id);
		return systemAgentInfo;
	}
}