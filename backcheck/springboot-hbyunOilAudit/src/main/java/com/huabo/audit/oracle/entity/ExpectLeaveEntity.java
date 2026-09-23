package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author Rui
 * @ClassName ExpectLeaveEntity
 * @Description
 * @DATE 2023/9/14
 */
@Data
@TableName("TBL_YQNS_EXPECT_LEAVE")
@Schema(name="未委托及预计离任")
@Accessors(chain = true)
public class ExpectLeaveEntity extends BaseReservedProperty implements Serializable {

    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;

    @TableField(value="NAME")
    @Schema(name="姓名")
    private String name;
    
    @TableField(value="NO")
    @Schema(name="编号")
    private String no;

    @TableField(value="RETIRE_TIME")
    @Schema(name="预计退二线时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date retireTime;

    @TableField(value="AUDIT_TIME")
    @Schema(name="审计时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date auditTime;

    @TableField(value="PROJECT_NAME")
    @Schema(name="项目名称")
    private String projectName;

    @TableField(value="WORK_START_TIME")
    @Schema(name="任职起始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date workStartTime;

    @TableField(value="WORK_END_TIME")
    @Schema(name="任职结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date workEndTime;

    @TableField(value="DO_AUDIT_TIME")
    @Schema(name="审计实施时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date doAuditTime;

    @TableField(value="TEAM_LEADER_ID")
    @Schema(name="组长")
    private String teamLeaderId;

	@TableField(exist = false)
	private TblStaff teamLeader;

    @TableField(value="LEADER_ID")
    @Schema(name="牵头人")
    private String leaderId;

	@TableField(exist = false)
	private TblStaff leader;

    @TableField(value="CHIEF_REVIEWER_ID")
    @Schema(name="主审")
    private String chiefReviewerId;

	@TableField(exist = false)
	private TblStaff chiefReviewer;

    @TableField(value="DEPUTY_REVIEWER_ID")
    @Schema(name="助审")
    private String deputyReviewerId;

    @TableField(value="CHIEF_REVIEWER_NAME")
    @Schema
    private String chiefReviewerName;
    
    @TableField(value="DEPUTY_REVIEWER_NAME")
    @Schema
    private String deputyReviewerName;
    
    @TableField(value="LEADER_NAME")
    @Schema
    private String leaderName;
    
    @TableField(value="TEAM_LEADER_NAME")
    @Schema
    private String teamLeaderName;
    
	@TableField(exist = false)
	private TblStaff deputyReviewer;

    @TableField(value="PERSON_IDS")
    @Schema(name="下发的人员ID")
    private String personIds;

    @TableField(value="CREATE_USER")
    @Schema(name="创建人")
    private String createUserId;

	@TableField(exist = false)
	private TblStaff createUser;

    @TableField(value="CREATE_TIME")
    @Schema(name="创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @TableField(value = "STATUS")
    @Schema(name="状态")
    private Integer status;

    @TableField(exist = false)
    @Schema(name="用户查看数据权限部门")
    private String queryDeptIds;

    @TableField(value = "UNITID")
    @Schema(name="单位")
    private BigDecimal unitId;

    @TableField(value = "UNITNAME")
    @Schema(name="单位名称")
    private String unitName;

    @TableField(exist = false)
    @Schema(name="组织机构")
    private TblOrganization  tblOrganization;


    @TableField(exist = false)
    @Schema(name="ids")
    private String ids;
}
