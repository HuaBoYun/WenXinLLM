package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @ClassName ImplementPlanTeamEntity
 * @Description
 * @DATE 2023/10/27
 */
@Data
@TableName("TBL_YQNS_IMPLEMENT_PLAN_TEAM")
@Schema(name="项目管理-实施方案表-小组关联表")
@Accessors(chain = true)
public class ImplementPlanTeamEntity implements Serializable {

    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;

    @TableField(value="TEAM_NAME")
    @Schema(name="小组名称")
    private String teamName;

    @TableField(value="IM_PLAN_ID")
    @Schema(name="项目管理-实施方案ID")
    private BigDecimal imPlanId;

    @TableField(value="TEAM_LEADER_ID")
    @Schema(name="组长ID")
    private BigDecimal teamLeaderId;

    @TableField(exist = false)
    private TblStaff teamLeader;

    @TableField(value="TEAM_MEMBERS_IDS")
    @Schema(name="组员IDS,多个用逗号隔开")
    private String teamMembersIds;

    @TableField(exist = false)
    private List<TblStaff> teamMembers;
    
    
    @TableField(value="FZZSTAFFID")
    @Schema(name="副组长ID")
    private BigDecimal fzzstaffid;
    
    @TableField(value="FZZNAME")
    @Schema(name="副组长名称")
    private String fzzname;


}
