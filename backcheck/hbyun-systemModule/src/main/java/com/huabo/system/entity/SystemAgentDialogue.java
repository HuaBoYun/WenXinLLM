package com.huabo.system.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.ibatis.type.JdbcType;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.ColumnType;

/**
 * 设置-智能体-最近对话
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SYSTEM_AGENT_DIALOGUE")
@Schema(name="SystemAgentDialogue")
public class SystemAgentDialogue implements Serializable {
	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@Schema(name= "主键ID")
	private Long id;

	/**
	 * 名称（标题，首条用户消息截断）
	 */
	@Column(name = "NAME")
	@Schema(name= "名称")
	private String name;

	/**
	 * 扩展字段（CLOB，存 messages 数组 JSON 字符串）
	 */
	@Column(name = "EXT")
	@ColumnType(jdbcType = JdbcType.CLOB)
	@Schema(name= "扩展(对话内容JSON)")
	private String ext;

	/**
	 * 智能体类型 contract/finance/risk/compliance/legal/audit
	 */
	@Column(name = "AGENT_TYPE")
	@Schema(name= "智能体类型")
	private String agentType;

	/**
	 * 关联智能体 ID（SYSTEM_AGENT_INFO.ID）
	 */
	@Column(name = "AGENT_ID")
	@Schema(name= "关联智能体ID")
	private Long agentId;

	/**
	 * 前端会话标识
	 */
	@Column(name = "SESSION_ID")
	@Schema(name= "前端会话ID")
	private String sessionId;

	/**
	 * WenxinAgent 会话 ID（继续对话时透传给 WenxinAgent）
	 */
	@Column(name = "CONVERSATION_ID")
	@Schema(name= "WenxinAgent会话ID")
	private String conversationId;

	/**
	 * WenxinAgent parent_message_id（消息线程链路）
	 */
	@Column(name = "LAST_MESSAGE_ID")
	@Schema(name= "WenxinAgent parent_message_id")
	private String lastMessageId;

	@Column(name = "CREATOR")
	@Schema(name= "创建人ID", hidden = true)
	private Long creator;

	@Transient
	@Schema(name= "创建人姓名", hidden = true)
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

	@Column(name = "UPDATEDTIME")
	@Schema(name= "更新时间", hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	private static final long serialVersionUID = 1L;

	public static SystemAgentDialogue ofId(Long id) {
		SystemAgentDialogue systemAgentDialogue = new SystemAgentDialogue();
		systemAgentDialogue.setId(id);
		return systemAgentDialogue;
	}
}