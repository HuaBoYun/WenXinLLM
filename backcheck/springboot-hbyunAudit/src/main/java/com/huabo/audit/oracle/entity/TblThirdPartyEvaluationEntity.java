package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 中介机构评价表
 */
@Data
@Entity
@TableName("TBL_THIRD_PARTY_EVALUATION")
public class TblThirdPartyEvaluationEntity {
    @Id
    @TableId(value = "id", type= IdType.INPUT)
    @Schema(name="主键")
    private BigDecimal id;

    @Schema(name="单位名称")
    @TableField(value= "UNIT_NAME")
    private String unitName;

    @Schema(name="审计项目名称")
    @TableField(value= "AUDIT_PROJECT_NAME")
    private String auditProjectName;

    @Schema(name="委托中介机构名称（全称）")
    @TableField(value= "AUDIT_FIRM_NAME")
    private String auditFirmName;

    @Schema(name="合同签订日期")
    @TableField(value= "CONTRACT_SIGNING_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date contractSigningDate;

    @Schema(name="合同签订日期开始查询条件")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date contractSigningDateStart;

    @Schema(name="合同签订日期结束查询条件")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date contractSigningDateEnd;

    @Schema(name="合同金额（万元）")
    @TableField(value= "CONTRACT_AMOUNT")
    private BigDecimal contractAmount;

    @Schema(name="采购方式")
    @TableField(value= "PROCUREMENT_METHOD")
    private String procurementMethod;

    @Schema(name="审计程序得分（满分20分）")
    @TableField(value= "AUDIT_PROCEDURE_SCORE")
    private Integer auditProcedureScore;

    @Schema(name="审计底稿得分（满分20分）")
    @TableField(value= "AUDIT_WORKING_PAPER_SCORE")
    private Integer auditWorkingPaperScore;

    @Schema(name="审计报告得分（满分20分）")
    @TableField(value= "AUDIT_REPORT_SCORE")
    private Integer auditReportScore;

    @Schema(name="审计人员工作时间的投入得分（满分20分）")
    @TableField(value= "AUDITOR_TIME_INVESTMENT_SCORE")
    private Integer auditorTimeInvestmentScore;

    @Schema(name="审计风险得分（满分20分）")
    @TableField(value= "AUDIT_RISK_SCORE")
    private Integer auditRiskScore;

    @Schema(name="总分")
    @TableField(value= "TOTAL_SCORE")
    private Integer totalScore;

    @Schema(name="评价结果")
    @TableField(value= "EVALUATION_RESULT")
    private String evaluationResult;

    @Schema(name="建议改进内容")
    @TableField(value= "IMPROVEMENT_SUGGESTIONS")
    private String improvementSuggestions;

    @Schema(name="备注")
    @TableField(value= "REMARKS")
    private String remarks;

    @TableField(value= "CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
