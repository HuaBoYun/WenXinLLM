package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ Author: Striker dev@example.com
 * @ Date: 2023-09-07 15:58
 * @ TODO:
 **/

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YQNS_PROPOSE")
@Schema(name="TblYqnsPropose对象")
public class TblYqnsProposeEntity extends BaseReservedProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID")
    @Schema(name = "建议表主键ID")
    @Column(name = "ID")
    private BigDecimal id;
    
    /**
     * 审计报告定稿主键
     */
    @Schema(name = "审计报告定稿主键")
    @TableField(value = "SJBGDGID")
    private Long sjbgdgid;
    
    /**
     * 标题
     */
    @Schema(name = "审计报告定稿标题")
    @TableField(value = "SJBGDGTITLE")
    private String sjbgdgTitle;

    @Schema(name = "层级类型")
    @TableField("HIERARCHYTYPE")
    private String hierarchyType;

    @Schema(name = "业务类型")
    @TableField("BUSINESSTYPE")
    private String businessType;

    @Schema(name = "建议标题")
    @TableField("TITLE")
    private String title;

    @Schema(name = "关联底稿名称，多个用逗号分割")
    @TableField("DRAFT")
    private String draft;
    
    @Schema(name = "关联底稿名称，多个用逗号分割")
    @TableField("DRAFTIDSTRS")
    private String draftIdStrs;

    @Schema(name = "建议描述")
    @TableField("DETAILS")
    private String details;


    @Schema(name = "新增时间")
    @TableField("ADDTIME")
    private Date addTime;

    @Schema(name = "更新时间")
    @TableField("UPDATETIME")
    private Date updateTime;

    @Schema(name = "操作员")
    @TableField("OPERATOR")
    private String operator;

    @Schema(name="操作员ID")
    @TableField("OPERATORID")
    private BigDecimal operatorId;
    
    
    @Schema(name="所属部门主键")
    @TableField("LINKDEPTID")
    private BigDecimal linkDeptId;
    
    @Schema(name="所属公司主键")
    @TableField("LINKORGID")
    private BigDecimal linkOrgId;
    
    @Schema(name="审批状态  0-未审批 ，1-审批中、2-需调整、3-已通过、4-已终止、5-已跟踪、6-已完成、")
    @TableField("STATUS ")
    private Integer status;
    
    @Schema(name="是否采纳，1-是，0-否")
    @TableField("ISADOPT ")
    private Integer isAdopt;
    
    @Schema(name="直接经济成果类型")
    @TableField("ZJJJCGLX ")
    private String zjjcglx;
    
    @Schema(name="直接经济成果金额（元）")
    @TableField("ZJJJCGJE ")
    private BigDecimal zjjcgje;
    
    @Schema(name="其他经济成果类型")
    @TableField("QTJJCGLX ")
    private String qtjjcglx;
    
    @Schema(name="其他经济成果金额（元）")
    @TableField("QTJJCGJE ")
    private BigDecimal qtjjcgje;
    
    @Schema(name="不采纳原因")
    @TableField("NOTREASON ")
    private String notReason;
    
    @Schema(name = "采纳主键")
    @TableField(exist = false)
    private String adoptId;
    
    @Schema(name = "问题整改主键")
    @TableField(exist = false)
    private BigDecimal wtzgid;
    
    @Schema(name = "项目名称")
    @TableField(exist = false)
    private String projectName;

}
