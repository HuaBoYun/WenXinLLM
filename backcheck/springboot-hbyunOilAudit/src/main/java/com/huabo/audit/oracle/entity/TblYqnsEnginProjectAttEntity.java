package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;

/**
 * TBL_YQNS_ENGIN_PROJECT_ATT
 * @author 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YQNS_ENGIN_PROJECT_ATT")
@Schema(name="TblYqnsEnginProjectAttEntity对象")
public class TblYqnsEnginProjectAttEntity implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "ID")
    @Schema(name = "工程审计项目附件关系主键ID")
    @Column(name = "ID")
    private Long id;

    /**
     * 工程审计项目ID
     */
    @Schema(name = "工程审计项目ID")
    @TableField("ENGIN_AUDIT_PROJECT_ID")
    private Long enginAuditProjectId;

    /**
     * 附件ID
     */
    @Schema(name = "附件ID", required = true)
    @TableField("ATTACHMENTID")
    private String attachmentId;


    private static final long serialVersionUID = 1L;
}