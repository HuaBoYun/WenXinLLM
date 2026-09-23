package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 智慧首页-报告实体
 *
 * @author cybermonitor
 * @date 2026-05-28
 */
@Data
@TableName("TBL_GZCT_SMART_HOME_REPORT")
public class GzctSmartHomeReport implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.ASSIGN_UUID)
    @TableField("ID")
    private String id;

    /** 标签 */
    @TableField("TAG")
    private String tag;

    /** 标题 */
    @TableField("TITLE")
    private String title;

    /** 时间描述 */
    @TableField("TIME_STR")
    private String timeStr;

    /** 作者 */
    @TableField("AUTHOR")
    private String author;

    /** 标签背景色 */
    @TableField("TAG_BG")
    private String tagBg;

    /** 标签颜色 */
    @TableField("TAG_COLOR")
    private String tagColor;

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
