package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.entity.base.BaseProjectEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * TBL_YQNS_AUDIT_PROJECT
 * @author 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YQNS_AUDIT_PROJECT")
@Schema(name="TblYqnsAuditProject对象", description="审计项目情况表")
public class TblYqnsAuditProjectEntity extends BaseProjectEntity implements Serializable {

    /**
     * 被审计单位
     */
    @Schema(name = "被审计单位")
    @TableField("AUDITUNIT")
    private String auditUnit;

    /**
     * 被审计单位Id
     */
    @Schema(name = "被审计单位Id")
    @TableField("AUDITUNITID")
    private Long auditUnitId;

    /**
     * 审计组
     */
    @Schema(name = "审计组")
    @TableField("AUDITGROUP")
    private String auditGroup;

    /**
     * 项目名称
     */
    @Schema(name = "项目名称")
    @TableField("NAME")
    private String name;

    /**
     * 现场审计开始时间
     */
    @Schema(name = "现场审计开始时间")
    @TableField("SCENEAPPROVESTAERTIME")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date sceneApproveStaerTime;

    /**
     * 现场审计结束时间
     */
    @Schema(name = "现场审计结束时间")
    @TableField("SCENEAPPROVEENDTIME")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date sceneApproveEndTime;

    /**
     * 计划现场工作时间
     */
    @Schema(name = "计划现场工作时间")
    @TableField("PLANSCENEAPPROVESTAERTIME")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date planSceneApproveStaerTime;

    /**
     * 计划现场结束时间
     */
    @Schema(name = "计划现场结束时间")
    @TableField("PLANSCENEAPPROVEENDTIME")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date planSceneApproveEndTime;

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

    private static final long serialVersionUID = 1L;
}