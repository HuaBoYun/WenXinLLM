package com.huabo.know.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2024-04-03
 */
@Getter
@Setter
@TableName("TBL_ZSGX_JUDICIAL_CASE")
@Schema(name="TblZsgxJudicialCase对象")
public class TblZsgxJudicialCase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @Schema(name = "标题")
    @TableField("TITLE")
    private String title;

    @Schema(name = "全文")
    @TableField("CONTENT")
    private String content;

    @Schema(name = "审判官")
    @TableField("JUDGE")
    private String judge;

    @Schema(name = "案件字号")
    @TableField("CASE_FLAG")
    private String caseFlag;

    @Schema(name = "代理律师")
    @TableField("AGENT_LAWYER")
    private String agentLawyer;

    @TableField("CASE_DOC")
    private String caseDoc;

    @Schema(name = "当事人")
    @TableField("PARTY")
    private String party;

    @Schema(name = "发布时间")
    @TableField("ISSUE_DATE")
    private Date issueDate;

    @Schema(name = "实施时间")
    @TableField("LAST_INSTANCE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date lastInstanceDate;

    @TableField("CONTENT_LEN")
    private Integer contentLen;

    @Schema(name = "发布年份")
    @TableField("ISSUE_YEAR")
    private String issueYear;

    @Schema(name = "文书类型code")
    @TableField("DOCUMENT_ATTR_CODE")
    private String documentAttrCode;

    @Schema(name = "文书类型name")
    @TableField("DOCUMENT_ATTR_NAME")
    private String documentAttrName;

    @Schema(name = "合同类型code")
    @TableField("CONTRACT_TYPE_CODE")
    private String contractTypeCode;

    @Schema(name = "合同类型name")
    @TableField("CONTRACT_TYPE_NAME")
    private String contractTypeName;

    @Schema(name = "审理程序code")
    @TableField("TRIAL_STEP_CODE")
    private String trialStepCode;

    @Schema(name = "审理程序name")
    @TableField("TRIAL_STEP_NAME")
    private String trialStepName;

    @Schema(name = "案件类型code")
    @TableField("CASE_CLASS_CODE")
    private String caseClassCode;

    @Schema(name = "案件类型name")
    @TableField("CASE_CLASS_NAME")
    private String caseClassName;

    @Schema(name = "公开类型code")
    @TableField("NO_PUBLIC_REASON_CODE")
    private String noPublicReasonCode;

    @Schema(name = "公开类型name")
    @TableField("NO_PUBLIC_REASON_NAME")
    private String noPublicReasonName;

    @Schema(name = "法院等级code")
    @TableField("COURT_GRADE_CODE")
    private String courtGradeCode;

    @Schema(name = "法院等级name")
    @TableField("COURT_GRADE_NAME")
    private String courtGradeName;

    @Schema(name = "审理法院code")
    @TableField("LAST_INSTANCE_COURT_CODE")
    private String lastInstanceCourtCode;

    @Schema(name = "审理法院name")
    @TableField("LAST_INSTANCE_COURT_NAME")
    private String lastInstanceCourtName;

    @Schema(name = "参照等级code")
    @TableField("CASE_GRADE_CODE")
    private String caseGradeCode;

    @Schema(name = "参照等级name")
    @TableField("CASE_GRADE_NAME")
    private String caseGradeName;

    @Schema(name = "案由code")
    @TableField("CATEGORY_CODE")
    private String categoryCode;

    @Schema(name = "案由name")
    @TableField("CATEGORY_NAME")
    private String categoryName;

    @Schema(name = "创建公司")
    @TableField("CREATE_COMPANY")
    private String createCompany;

    @Schema(name = "创建部门")
    @TableField("CREATE_DEPT")
    private String createDept;

    @Schema(name = "创建人")
    @TableField("CREATE_BY")
    private String createBy;

    @Schema(name = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @Schema(name = "修改时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @Schema(name = "修改人")
    @TableField("UPDATE_BY")
    private String updateBy;

    @Schema(name = "是否删除，0：未删除；1：已删除")
    @TableField("DELETED")
    private Integer deleted;


}
