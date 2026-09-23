package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * FUND_AUDIT_PROJECT_ATT
 *  财务审计项目
 * @author 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("FUND_AUDIT_PROJECT_ATT")
@Schema(name="FundAuditProjectAttEntity对象")
public class FundAuditProjectAttEntity implements Serializable {
    /**
     * 财务审计项目附件主键
     */
    @TableId(value = "ID")
    @Schema(name = "财务审计项目附件主键")
    @Column(name = "ID")
    private Long id;

    /**
     * 财务审计项目主键ID
     */
    @Schema(name = "财务审计项目主键ID")
    @TableField("FUND_AUDIT_PROJECT_ID")
    private Long fundAuditProjectId;

    /**
     * 附件ID
     */
    @Schema(name = "附件ID")
    @TableField("ATTACHMENTID")
    private String attachmentid;

    private static final long serialVersionUID = 1L;
}