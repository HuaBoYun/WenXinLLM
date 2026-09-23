package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @ClassName AuditSuggestion2LEntity
 * @Description
 * @DATE 2023/9/23
 */
@Data
@TableName("TBL_YQNS_2L_AUDIT_SUGGESTION")
@Schema(name="二级机构人中立项建议")
@Accessors(chain = true)
public class AuditSuggestion2LEntity implements Serializable {

    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;

    @TableField(value="ORG")
    @Schema(name="单位")
    private String orgId;

	@TableField(exist = false)
	private TblOrganization org;

    @TableField(value="NAME")
    @Schema(name="姓名")
    private String name;

    @TableField(value="JOB")
    @Schema(name="职务")
    private String job;

    @TableField(value="LV")
    @Schema(name="行政级别")
    private String level;

    @TableField(value="WORK_START_TIME")
    @Schema(name="任职开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date workStartTime;

    @TableField(value="WORK_END_TIME")
    @Schema(name="任职结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date workEndTime;

    @TableField(value="WORK_DURATION")
    @Schema(name="截至目前任职时间")
    private String workDuration;

    @TableField(value="HAS_FINANCE_PROBLEM")
    @Schema(name="是否存在重大财务异常 1：是 0：否")
    private Integer hasFinanceProblem;

    @TableField(value="HAS_ECONOMIC_PROBLEM")
    @Schema(name="是否发生重大经济事项 1：是 0：否")
    private Integer hasEconomicProblem;

    @TableField(value="HAS_BEEN_COMPLAIN")
    @Schema(name="是否受到投诉举报1：是 0：否")
    private Integer hasBeenComplain;

    @TableField(value="IS_LEAVE_NEXT_YEAR")
    @Schema(name="次年是否离任1：是 0：否")
    private Integer isLeaveNextYear;

    @TableField(value="NEED_AUDIT")
    @Schema(name="是否展开任中审计1：是 0：否")
    private Integer needAudit;

    @TableField(value="CREATE_USER")
    @Schema(name="创建人")
    private TblStaff createUser;

    @TableField(value="CREATE_TIME")
    @Schema(name="创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
    
    
    @TableField(value="ENTRUSTTIME")
    @Schema(name="委托时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date entrustTime;
    
    @TableField(value="AUDITSCOPE")
    @Schema(name="审计范围")
    private String auditScope;
    
    @TableField(value="PROJECTTYPE")
    @Schema(name="项目类型")
    private String projectType;
    
    @TableField(value="REMARKS")
    @Schema(name="备注")
    private String remarks;

    @TableField(value = "STATUS")
    @Schema(name="状态")
    private Integer status;
}
