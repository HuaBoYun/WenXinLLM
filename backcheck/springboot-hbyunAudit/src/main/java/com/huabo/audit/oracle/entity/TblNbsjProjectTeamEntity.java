package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 描述: 团队
 * author: ziyao
 * date: 2022-04-19
 */
@TableName("TBL_NBSJ_PROJECTTEAM")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjProjectTeamEntity {

    @TableId(value = "teamid", type= IdType.INPUT)
    @Schema
    private BigDecimal teamId;

    @TableField(value = "teamname")
    @Schema
    private String teamName;

    @TableField(value = "createtime")
    @Schema(hidden=true)
    private Date createTime;

    @TableField(value = "createuserid")
    @Schema
    private BigDecimal createUserId;
    
    @TableField(value = "leaderName")
    @Schema
    private String leaderName;

    @TableField(exist = false)
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private Set<TblNbsjProject> tblTeamProjects;

    @TableField(exist = false)
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private List<TblNbsjTeamstaffEntity> teamStaffs;
    
    @TableField(value = "zyNames")
    @Schema(hidden=true)
    private String zyNames;
    
    @TableField(exist = false)
    @Schema
    private BigDecimal leaderid;

    @TableField(exist = false)
    @Schema
    private String zystaffids;

    @TableField(exist = false)
    @Schema
    private BigDecimal projectid;
    
    
}
