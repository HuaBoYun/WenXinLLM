package com.huabo.audit.oracle.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
    private Integer teamId;

    @Schema(name = "小组名称")
    private String teamName;

    @Schema(name = "组长Id")
    private Integer groupLeaderId;

    @Schema(name = "组长")
    private String groupLeader;

    @Schema(name = "组员Id")
    private List<Integer> teamMemberId;

    @Schema(name = "组员")
    private String teamMember;

}
