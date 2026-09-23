package com.huabo.central.enterprises.audit.oracle.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实施方案-项目小组
 */
@Data
@Table(name = "TBL_YQNS_IMPLEMENT_PLAN_TEAM")
@AllArgsConstructor
@NoArgsConstructor
public class ImplementPlanTeam implements Serializable {

	@Id
	@Column(name = "ID")
	@Schema(name="ID")
	@GeneratedValue(generator = "JDBC")
	private Long id;

	@Column(name = "TEAM_NAME")
	@Schema(name="小组名称")
	private String teamName;

	@Column(name = "IM_PLAN_ID")
	@Schema(name="项目管理-实施方案ID")
	private Long imPlanId;

	@Column(name = "TEAM_LEADER_ID")
	@Schema(name="组长ID")
	private Long teamLeaderId;

	@Column(name = "TEAM_MEMBERS_IDS")
	@Schema(name="组员IDS,多个用逗号隔开")
	private String teamMembersIds;

	@Schema(name="副组长ID")
	private Long fzzstaffid;
}
