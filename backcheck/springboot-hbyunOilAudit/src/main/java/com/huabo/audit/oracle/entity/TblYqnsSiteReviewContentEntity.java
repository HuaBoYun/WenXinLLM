package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;

/**
 * TBL_YQNS_SITE_REVIEW_CONTENT
 * @author 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YQNS_SITE_REVIEW_CONTENT")
@Schema(name="TblYqnsSiteReviewContentEntity对象", description="现场审查主要内容")
public class TblYqnsSiteReviewContentEntity implements Serializable {
    /**
     * 现场审查主要内容主键
     */
    @TableId(value = "ID")
    @Schema(name = "现场审查主要内容主键ID")
    @Column(name = "ID")
    private Long id;
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

    /**
     * 删除标识：1是；0否
     */
    @Schema(name = "删除标识：1是；0否")
    @TableField("DELETED")
    private BigDecimal deleted;

    @Schema(name = "创建时间")
    @TableField("CREATETIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(name = "修改时间")
    @TableField("UPDATETIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatetime;

    /**
     * 结算项目编号
     */
    @Schema(name = "结算项目编号")
    @TableField("SETTLEPROJECTNUM")
    private String settleProjectNum;

    /**
     * 项目名称
     */
    @Schema(name = "项目名称")
    @TableField("PROJECTNAME")
    private String projectName;

    /**
     * 结算金额
     */
    @Schema(name = "结算金额")
    @TableField("SETTLEAMOUNT")
    private BigDecimal settleAmount;

    /**
     * 建设单位ID
     */
    @Schema(name = "建设单位ID")
    @TableField("BUILDORGID")
    private Long buildOrgId;

    /**
     * 施工单位ID
     */
    @Schema(name = "施工单位ID")
    @TableField("CONSTRUCTIONORGID")
    private Long constructionOrgId;

    /**
     * 核实主要内容
     */
    @Schema(name = "核实主要内容")
    @TableField("REVIEWCONTENT")
    private String reviewContent;

    /**
     * 审计人员ID
     */
    @Schema(name = "审计人员ID")
    @TableField("REVIEWSTAFFID")
    private Long reviewStaffId;

    /**
     * 建设单位项目经理ID
     */
    @Schema(name = "建设单位项目经理ID")
    @TableField("BUILDUNITMANAGEID")
    private BigDecimal buildUnitManageId;

    /**
     * 现场审查时间
     */
    @Schema(name = "现场审查时间")
    @TableField("SCENEREVIEWTIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sceneReviewTime;

    /**
     * 复审人员ID
     */
    @Schema(name = "复审人员ID")
    @TableField("RECHECKSTAFFID")
    private BigDecimal recheckStaffId;

    /**
     * 建设单位名称
     */
    @Schema(name = "复审人员ID")
    @TableField("BUILDORGNAME")
    private String buildOrgName;

    /**
     * 施工单位名称
     */
    @Schema(name = "施工单位名称")
    @TableField("CONSTRUCTIONORGNAME")
    private String constructionOrgName;

    /**
     * 审计人员名称
     */
    @Schema(name = "审计人员名称")
    @TableField("REVIEWSTAFFNAME")
    private String reviewStaffName;

    /**
     * 建设单位项目经理名称
     */
    @Schema(name = "建设单位项目经理名称")
    @TableField("BUILDUNITMANAGENAME")
    private String buildUnitManageName;

    /**
     * 复审人员名称
     */
    @Schema(name = "复审人员名称")
    @TableField("RECHECKSTAFFNAME")
    private String recheckStaffName;


    /**
     * 审批状态
     */
    @Schema(name = "审批状态")
    @TableField("STATUS")
    private Integer status;



    /**
     * 项目id
     */
    @Schema(name = "项目id")
    @TableField("PROJECTID")
    private BigDecimal projectId;
    
    @Schema(name = "TblYqnsProjectAuditTemplate 主键")
    @TableField("TEMPLATEID")
    private Long templateId;

    private static final long serialVersionUID = 1L;
}