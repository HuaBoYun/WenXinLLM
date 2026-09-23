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
 * TBL_YQNS_SITE_REVIEW_ATT
 * @author 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YQNS_SITE_REVIEW_ATT")
@Schema(name="TblYqnsSiteReviewAttEntity对象")
public class TblYqnsSiteReviewAttEntity implements Serializable {
    /**
     * 现场审查主要内容附件关系主键
     */
    @TableId(value = "ID")
    @Schema(name = "现场审查主要内容附件关系主键ID")
    @Column(name = "ID")
    private Long id;

    /**
     * 现场审查主要内容表主键
     */
    @Schema(name = "现场审查主要内容表主键")
    @TableField("SITEREVIEWCONTENTID")
    private Long siteReviewContentId;

    /**
     * 附件表ID
     */
    @Schema(name = "附件表ID")
    @TableField("ATTACHMENTID")
    private String attachmentId;

    private static final long serialVersionUID = 1L;
}