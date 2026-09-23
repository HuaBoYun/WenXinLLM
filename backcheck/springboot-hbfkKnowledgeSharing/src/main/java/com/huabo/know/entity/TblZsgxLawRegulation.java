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
@TableName("TBL_ZSGX_LAW_REGULATION")
@Schema(name="TblZsgxLawRegulation对象")
public class TblZsgxLawRegulation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键id")
    @TableField("ID")
    private String id;

    @Schema(name = "标题")
    @TableField("TITLE")
    private String title;

    @Schema(name = "发文字号")
    @TableField("DOCUMENT_NO")
    private String documentNo;

    @Schema(name = "全文")
    @TableField("CONTENT")
    private String content;

    @Schema(name = "实施时间")
    @TableField("IMPLEMENT_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date implementDate;

    @Schema(name = "发布时间")
    @TableField("ISSUE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date issueDate;

    @Schema(name = "发布年份")
    @TableField("ISSUE_YEAR")
    private String issueYear;

    @Schema(name = "发布部门code")
    @TableField("ISSUE_DEPARTMENT_CODE")
    private String issueDepartmentCode;

    @Schema(name = "发布部门name")
    @TableField("ISSUE_DEPARTMENT_NAME")
    private String issueDepartmentName;

    @Schema(name = "合同类型code")
    @TableField("CONTRACT_TYPE_CODE")
    private String contractTypeCode;

    @Schema(name = "合同类型name")
    @TableField("CONTRACT_TYPE_NAME")
    private String contractTypeName;

    @Schema(name = "效力级别code")
    @TableField("EFFECTIVENESS_DIC_CODE")
    private String effectivenessDicCode;

    @Schema(name = "效力级别name")
    @TableField("EFFECTIVENESS_DIC_NAME")
    private String effectivenessDicName;

    @Schema(name = "时效性code")
    @TableField("TIMELINESS_DIC_CODE")
    private String timelinessDicCode;

    @Schema(name = "时效性name")
    @TableField("TIMELINESS_DIC_NAME")
    private String timelinessDicName;

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
