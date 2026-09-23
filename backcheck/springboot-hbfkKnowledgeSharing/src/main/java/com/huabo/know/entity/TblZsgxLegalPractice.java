package com.huabo.know.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
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
@TableName("TBL_ZSGX_LEGAL_PRACTICE")
@Schema(name="TblZsgxLegalPractice对象")
public class TblZsgxLegalPractice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键ID")
    @TableField("ID")
    private String id;

    @Schema(name = "标题")
    @TableField("TITLE")
    private String title;

    @TableField("ARTICLE_CKEYWORD")
    private String articleCkeyword;

    @Schema(name = "全文")
    @TableField("CONTENT")
    private String content;

    @Schema(name = "全文字数")
    @TableField("CONTENT_LEN")
    private BigDecimal contentLen;

    @Schema(name = "发布时间")
    @TableField("ISSUE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date issueDate;

    @Schema(name = "实务提交时间")
    @TableField("ARTICLE_SUBMIT_DATE")
    private Date articleSubmitDate;

    @Schema(name = "实务作者")
    @TableField("ARTICLE_BY_AUTHOR")
    private String articleByAuthor;

    @TableField("ARTICLE_REMARK")
    private String articleRemark;

    @Schema(name = "发布年份")
    @TableField("ISSUE_YEAR")
    private String issueYear;

    @Schema(name = "合同机构code")
    @TableField("FIRM_ID_CODE")
    private String firmIdCode;

    @Schema(name = "合同机构name")
    @TableField("FIRM_ID_NAME")
    private String firmIdName;

    @Schema(name = "合同类型code")
    @TableField("CONTRACT_TYPE_CODE")
    private String contractTypeCode;

    @Schema(name = "合同类型name")
    @TableField("CONTRACT_TYPE_NAME")
    private String contractTypeName;

    @Schema(name = "合作刊物code")
    @TableField("JOURNAL_ID_LS_CODE")
    private String journalIdLsCode;

    @Schema(name = "合作刊物name")
    @TableField("JOURNAL_ID_LS_NAME")
    private String journalIdLsName;

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

    @Schema(name = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @Schema(name = "更新人")
    @TableField("UPDATE_BY")
    private String updateBy;

    @Schema(name = "是否删除，0：未删除；1：已删除")
    @TableField("DELETED")
    private Integer deleted;


}
