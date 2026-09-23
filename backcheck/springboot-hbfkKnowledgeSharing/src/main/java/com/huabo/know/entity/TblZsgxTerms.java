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
@TableName("TBL_ZSGX_TERMS")
@Schema(name="TblZsgxTerms对象")
public class TblZsgxTerms implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键ID")
    @TableField("ID")
    private String id;

    @Schema(name = "标题")
    @TableField("TITLE")
    private String title;

    @Schema(name = "发布时间")
    @TableField("ISSUE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date issueDate;

    @Schema(name = "发布年份")
    @TableField("ISSUE_YEAR")
    private String issueYear;

    @TableField("SOURCE_CONTRACT_NAME")
    private String sourceContractName;

    @TableField("SOURCE_CONTRACT_URL")
    private String sourceContractUrl;

    @TableField("RELATION_RULE")
    private String relationRule;

    @Schema(name = "全文")
    @TableField("CONTENT")
    private String content;

    @Schema(name = "风险提示")
    @TableField("RISK_TIPS")
    private String riskTips;

    @Schema(name = "风险等级code")
    @TableField("RISK_LEVEL_CODE")
    private String riskLevelCode;

    @Schema(name = "风险等级name")
    @TableField("RISK_LEVEL_NAME")
    private String riskLevelName;

    @Schema(name = "合同类型code")
    @TableField("CONTRACT_TYPE_CODE")
    private String contractTypeCode;

    @Schema(name = "合同类型name")
    @TableField("CONTRACT_TYPE_NAME")
    private String contractTypeName;

    @Schema(name = "条款利益倾向方code")
    @TableField("INTEREST_PARTY_CODE")
    private String interestPartyCode;

    @Schema(name = "条款利益倾向方name")
    @TableField("INTEREST_PARTY_NAME")
    private String interestPartyName;

    @Schema(name = "适用行业code")
    @TableField("INDUSTRY_TYPE_CODE")
    private String industryTypeCode;

    @Schema(name = "适用行业name")
    @TableField("INDUSTRY_TYPE_NAME")
    private String industryTypeName;

    @Schema(name = "条款类型code")
    @TableField("TERMS_TYPE_CODE")
    private String termsTypeCode;

    @Schema(name = "条款类型name")
    @TableField("TERMS_TYPE_NAME")
    private String termsTypeName;

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
