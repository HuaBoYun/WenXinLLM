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
 * 设置-智能体下发
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SYSTEM_AGENT_ISSUED")
public class SystemAgentIssued implements Serializable {
	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@Schema(name= "主键ID")
	private Long id;

	/**
	 * 智能体ID
	 */
	@Column(name = "AGENTID")
	@Schema(name= "智能体ID")
	private Long agentId;

	/**
	 * 权限类型：1-公司 2-角色 3-个人
	 */
	@Schema(name= "权限类型：1-公司 2-角色 3-个人")
	@Column(name = "AUTHORITYTYPE")
	private Integer authorityType;

	/**
	 * 权限ID
	 */
	@Schema(name= "权限ID")
	@Column(name = "AUTHORITYID")
	private Long authorityId;


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

	public static SystemAgentIssued ofId(Long id) {
		SystemAgentIssued systemAgentIssued = new SystemAgentIssued();
		systemAgentIssued.setId(id);
		return systemAgentIssued;
	}
}