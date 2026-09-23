package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 整改进度记录表 - 业绩考核穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_STATEASSETS_PERFORMANCE_RECTIFY_RECORD")
public class TblStateassetsPerformanceRectifyRecord {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("RECTIFY_ID")
    private String rectifyId;

    @TableField("RECORD_TYPE")
    private String recordType;

    @TableField("PROGRESS_DESC")
    private String progressDesc;

    @TableField("ATTACHMENT_URL")
    private String attachmentUrl;

    @TableField("AUDIT_RESULT")
    private String auditResult;

    @TableField("AUDIT_OPINION")
    private String auditOpinion;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
}
