package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 智慧首页-待办事项实体
 *
 * @author cybermonitor
 * @date 2026-05-28
 */
@Data
@TableName("TBL_GZCT_SMART_HOME_PENDING")
public class GzctSmartHomePending implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.ASSIGN_UUID)
    @TableField("ID")
    private String id;

    /** 标题 */
    @TableField("TITLE")
    private String title;

    /** 业务领域 */
    @TableField("DOMAIN")
    private String domain;

    /** 描述 */
    @TableField("DESCRIPTION")
    private String description;

    /** 是否紧急(0-否 1-是) */
    @TableField("URGENT")
    private Integer urgent;

    /** 状态标签 */
    @TableField("STATUS_LABEL")
    private String statusLabel;

    /** 跳转路由 */
    @TableField("ROUTE")
    private String route;

    /** 排序号 */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /** 状态 */
    @TableField("STATUS")
    private String status;

    /** 创建时间 */
    @TableField("CREATE_TIME")
    private Date createTime;

    /** 更新时间 */
    @TableField("UPDATE_TIME")
    private Date updateTime;
}
