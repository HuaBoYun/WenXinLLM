package com.management.accountant.oracle.entity.integration;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 集成配置版本实体
 * 记录集成配置的版本变更历史，支持版本快照和状态管理（CURRENT/ARCHIVED/DEPRECATED）
 *
 * @author system
 * @date 2026-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INTEGRATION_CONFIG_VERSION")
public class IntegrationConfigVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 版本主键ID */
    @TableId(value = "VERSION_ID", type = IdType.ASSIGN_UUID)
    private String versionId;

    /** 关联的集成配置ID */
    @TableField("CONFIG_ID")
    private String configId;

    /** 版本号（如 v1、v2） */
    @TableField("VERSION_NO")
    private String versionNo;

    /** 版本名称 */
    @TableField("VERSION_NAME")
    private String versionName;

    /** 变更日志 */
    @TableField("CHANGE_LOG")
    private String changeLog;

    /** 配置快照（JSON格式） */
    @TableField("CONFIG_SNAPSHOT")
    private String configSnapshot;

    /** 版本状态（CURRENT/ARCHIVED/DEPRECATED） */
    @TableField("STATUS")
    private String status;

    /** 创建人 */
    @TableField("CREATED_BY")
    private String createdBy;

    /** 创建时间 */
    @TableField("CREATED_TIME")
    private Date createdTime;
}
