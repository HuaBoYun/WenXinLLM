package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 *@ClassName ProjectTeamInfoEntity
 *@Description 项目小组信息
 *@Author ZiYao
 *@Date 2022/4/15 17:16
 *@Version 1.0
 **/
@Data
@Schema(name="项目小组信息")
public class ProjectTeamInfoEntity {

    @Schema(name = "小组id")
    private BigDecimal teamId;

    @Schema(name = "小组名称")
    private String teamName;

    @Schema(name = "组长Id")
    private BigDecimal groupLeaderId;

    @Schema(name = "组长")
    private String groupLeader;

    @Schema(name = "组员Id")
    @TableField(exist = false)
    private List<Integer> teamMemberId;

    @Schema(name = "组员")
    private String teamMember;

}
