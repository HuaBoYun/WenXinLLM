package com.huabo.audit.oracle.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 央企模块-计划编制-计划备案
 * @TableName TBL_YQNS_PLANFILING
 */
@TableName(value ="TBL_YQNS_PLANFILING")
@Data
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblYqnsPlanfiling extends BaseReservedProperty {

    /**
     * 计划备案ID主键
     */
    @Schema(name = "计划备案ID主键")
    @TableId(value = "PLANFILINGID", type = IdType.INPUT)
    private BigDecimal planfilingid;

    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;
    
    /**
     * 审计项目名称
     */
    @Schema(name = "审计项目名称")
    @TableField(value = "PLANPROJECTNAME")
    private String planprojectname;

    /**
     * 被审计单位ID
     */
    @Schema(name = "被审计单位ID")
    @TableField(value = "PLANAUDITUNITID")
    private BigDecimal planauditunitid;
    /**
     * 被审计单位名称
     */
    @Schema(name = "被审计单位名称")
    @TableField(value = "PLANAUDITUNITNAME")
    private String planauditunitname;
    /**
     * 实施类型
     */
    @Schema(name = "实施类型")
    @TableField(value = "PLANIMPLEMENTATIONTYPE")
    private String planimplementationtype;
    /**
     * 实施审计机构ID
     */
    @Schema(name = "实施审计机构ID")
    @TableField(value = "PLANIMPLEMENTATIONUNITID")
    private BigDecimal planimplementationunitid;
    /**
     * 实施审计机构名称
     */
    @Schema(name = "实施审计机构名称")
    @TableField(value = "PLANIMPLEMENTATIONUNITNAME")
    private String planimplementationunitname;
    /**
     * 项目负责处（科）室ID
     */
    @Schema(name = "项目负责处（科）室ID")
    @TableField(value = "PLANPROJECTLEADERID")
    private BigDecimal planprojectleaderid;
    /**
     * 项目负责处（科）室名称
     */
    @Schema(name = "项目负责处（科）室名称")
    @TableField(value = "PLANPROJECTLEADERNAME")
    private String planprojectleadername;
    /**
     * 审计项目类型ID
     */
    @Schema(name = "审计项目类型ID")
    @TableField(value = "PLANAUDITPROJECTTYPEID")
    private BigDecimal planauditprojecttypeid;
    /**
     * 审计项目类型名称
     */
    @Schema(name = "审计项目类型名称")
    @TableField(value = "PLANAUDITPROJECTTYPENAME")
    private String planauditprojecttypename;
    /**
     * 立项依据
     */
    @Schema(name = "立项依据")
    @TableField(value = "PLANPROJECTBASIS")
    private String planprojectbasis;
    /**
     * 计划投入人日
     */
    @Schema(name = "计划投入人日")
    @TableField(value = "PLANINVESTMENTPERSIONNEL")
    private String planinvestmentpersionnel;
    /**
     * 计划实施月份
     */
    @Schema(name = "计划实施月份")
    @TableField(value = "PLANIMPLEMENTATIONMONTH")
    private String planimplementationmonth;
    /**
     * 编制人ID
     */
    @Schema(name = "编制人ID")
    @TableField(value = "PLANPREPAREDBYID")
    private String planpreparedbyid;
    /**
     * 编制人名称
     */
    @Schema(name = "编制人名称")
    @TableField(value = "PLANPREPAREDBYNAME")
    private String planpreparedbyname;
    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField(value = "PLANCREATIONTIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date plancreationtime;
    /**
     * 立项单位ID
     */
    @Schema(name = "立项单位ID")
    @TableField(value = "PLANPROJECTAPPROVALUNITID")
    private BigDecimal planprojectapprovalunitid;
    /**
     * 立项单位名称
     */
    @Schema(name = "立项单位名称")
    @TableField(value = "PLANPROJECTAPPROVALUNITNAME")
    private String planprojectapprovalunitname;

    /**
     * 项目年度
     */
    @Schema(name = "项目年度")
    @TableField(value = "PLANPROJECTYEAR")
    private String planprojectyear;

    /**
     * 是否对被审计单位的全部经营活动进行审计
     */
    @Schema(name = "是否对被审计单位的全部经营活动进行审计")
    @TableField(value = "PLANYESANDNOAUDIT")
    private String planyesandnoaudit;

    /**
     * 0 : 新增计划  1：境外项目
     */
    @Schema(name = "0 : 新增计划  1：境外项目")
    @TableField(value = "PLANISFALG")
    private BigDecimal planisfalg;

    /**
     * 审批状态
     */
    @TableField(value = "STATUS")
    @Column(name = "STATUS")
    @Schema(name = "审核状态 1 未审核;2 审核中;3 审核驳回;4 审核完成;5 需调整")
    private Integer status;

    /**
     * 项目类型
     */
    @Schema(name = "项目类型")
    @TableField(value = "PLANPROJECTTYPE")
    private String planprojecttype;

    /**
     * 审计范围
     */
    @Schema(name = "审计范围")
    @TableField(value = "PLANAUDITSCOPE")
    private String planauditscope;

    /**
     * 备用字段
     */
    @Schema(name = "备用字段")
    @TableField(value = "PLANTEXT")
    private String plantext;

    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private List<String> attIds;

    @Schema(name = "附件对象集合")
    @TableField(exist = false)
    private List<TblAttachment> attachments;

}
