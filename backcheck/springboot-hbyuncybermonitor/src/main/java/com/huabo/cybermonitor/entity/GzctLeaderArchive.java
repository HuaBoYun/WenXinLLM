package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 负责人档案管理实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_LEADER_ARCHIVE")
public class GzctLeaderArchive extends Model<GzctLeaderArchive> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("LEADER_ID")
    private String leaderId;

    @TableField("LEADER_NAME")
    private String leaderName;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("ARCHIVE_TYPE")
    private String archiveType;

    @TableField("ARCHIVE_NO")
    private String archiveNo;

    @TableField("ARCHIVE_TITLE")
    private String archiveTitle;

    @TableField("ARCHIVE_CONTENT")
    private String archiveContent;

    @TableField("FILE_DATE")
    private LocalDate fileDate;

    @TableField("KEEPER")
    private String keeper;

    @TableField("STATUS")
    private String status;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
