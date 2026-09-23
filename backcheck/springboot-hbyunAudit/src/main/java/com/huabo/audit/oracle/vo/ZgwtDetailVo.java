package com.huabo.audit.oracle.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ZgwtDetailVo {
    @Schema(name = "问题ID")
    private BigDecimal issueId;

    @Schema(name = "问题编号")
    private String issueCode;

    @Schema(name = "问题名称")
    private String issueName;

    @Schema(name = "问题标题")
    private String issueTitle;

    @Schema(name = "问题备注")
    private String questionMemo;

    @Schema(name = "项目ID")
    private BigDecimal projectId;

    @Schema(name = "组织ID名称")
    private String orgIdNames;

    @Schema(name = "项目名称")
    private String projectName;

    @Schema(name = "开始日期")
    private Date startDate;

    @Schema(name = "审计类型")
    private String auditType;

    @Schema(name = "项目状态")
    private String status;

    @Schema(name = "项目来源")
    private String projectSource;

    @Schema(name = "结束日期")
    private Date endDate;

    @Schema(name = "员工ID")
    private BigDecimal staffId;

    @Schema(name = "真实姓名")
    private String realName;

    @Schema(name = "手机号")
    private String mobilePhone;

    @Schema(name = "邮箱")
    private String email;

    @Schema(name = "组织ID")
    private BigDecimal orgId;

    @Schema(name = "主管部门")
    private String orgName;

    @Schema(name = "父组织ID")
    private BigDecimal fatherOrgId;

    @Schema(name = "关系ID")
    private BigDecimal relaId;

    @Schema(name = "整改方案")
    private String rectificationPlan;

    @Schema(name = "整改措施")
    private String rectificationMeasures;

    @Schema(name = "计划截止时间")
    private Date planDeadline;

    @Schema(name = "实施ID")
    private BigDecimal implId;

    @Schema(name = "实施整改措施")
    private String implRectMeasures;

    @Schema(name = "情况概述")
    private String situationOverview;

    @Schema(name = "完成情况")
    private String achievement;

    @Schema(name = "整改结论")
    private String conclusion;

    @Schema(name = "实施截止时间")
    private Date implDeadline;

    @Schema(name = "完成时间")
    private Date finishTime;

    @Schema(name = "整改结果状态 1 or null-未整改，2-已整改未到位、3-已整改到位、4-关闭")
    private Integer resultStatus;

    // 保留旧字段以兼容前端
    @Schema(name = "截止时间(兼容)")
    private String deadline;

    @Schema(name = "问题状态(兼容)")
    private String issueStatus;
}
