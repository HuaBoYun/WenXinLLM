package com.financial.sharing.mysql.entity;

import lombok.Data;

import java.util.Date;

/**
 * 凭证模板版本控制实体类
 *
 * @author 赵工
 * @since 2025-12-07
 */
@Data
public class VoucherTemplateVersionEntity {

    /**
     * 版本ID
     */
    private Long versionId;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 版本号
     */
    private String versionNumber;

    /**
     * 版本名称
     */
    private String versionName;

    /**
     * 版本描述
     */
    private String versionDesc;

    /**
     * 版本类型(MAJOR/MINOR/PATCH)
     */
    private String versionType;

    /**
     * 版本状态(DRAFT/ACTIVE/ARCHIVED)
     */
    private String status;

    /**
     * 父版本ID
     */
    private Long parentVersionId;

    /**
     * 变更日志
     */
    private String changeLog;

    /**
     * 模板快照(JSON)
     */
    private String templateSnapshot;

    /**
     * 是否当前版本
     */
    private Integer isCurrent;

    /**
     * 发布日期
     */
    private Date publishDate;

    /**
     * 生效日期
     */
    private Date effectiveDate;

    /**
     * 过期日期
     */
    private Date expiryDate;

    /**
     * 账套ID
     */
    private String bookId;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 更新人
     */
    private Long updater;

    /**
     * 审核人
     */
    private Long reviewer;

    /**
     * 审核日期
     */
    private Date reviewDate;

    /**
     * 审核意见
     */
    private String reviewComment;
}