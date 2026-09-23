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
 * TBL_YQNS_AUDIT_PROJECT_DETAIL
 * @author 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YQNS_AUDIT_PROJECT_DETAIL")
@Schema(name="TblYqnsAuditProjectDetail对象")
public class TblYqnsAuditProjectDetailEntity implements Serializable {
    /**
     * 审计项目情况明细主键ID
     */
    @TableId(value = "ID")
    @Schema(name = "审计项目情况明细主键ID")
    @Column(name = "ID")
    private Long id;

    /**
     * 审计项目情况主键ID
     */
    @Schema(name = "审计项目情况主键ID")
    @TableField("AUDITPROJECTID")
    private Long auditProjectId;

    /**
     * 更新时间
     */
    @Schema(name = "修改时间")
    @TableField("UPDATETIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatetime;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField("CREATETIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createtime;

    /**
     * 填写时间
     */
    @Schema(name = "填写时间", required = true)
    @TableField("FILLINTIME")
    private String fillInTime;

    /**
     * 填写内容
     */
    @Schema(name = "填写内容", required = true)
    @TableField("FILLINCONTEN")
    private String fillInConten;

    private static final long serialVersionUID = 1L;
}