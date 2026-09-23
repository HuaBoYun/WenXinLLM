package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 智慧首页-KPI指标实体
 *
 * @author cybermonitor
 * @date 2026-05-28
 */
@Data
@TableName("TBL_GZCT_SMART_HOME_KPI")
public class GzctSmartHomeKpi implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.ASSIGN_UUID)
    @TableField("ID")
    private String id;

    /** 指标标签 */
    @TableField("LABEL")
    private String label;

    /** 指标值 */
    @TableField("VALUE")
    private String value;

    /** 单位 */
    @TableField("UNIT")
    private String unit;

    /** 图标 */
    @TableField("ICON")
    private String icon;

    /** 图标背景色 */
    @TableField("ICON_BG")
    private String iconBg;

    /** 图标颜色 */
    @TableField("ICON_COLOR")
    private String iconColor;

    /** 进度条背景色 */
    @TableField("BAR_BG")
    private String barBg;

    /** 进度条颜色 */
    @TableField("BAR_COLOR")
    private String barColor;

    /** 进度条宽度 */
    @TableField("BAR_WIDTH")
    private Integer barWidth;

    /** 趋势值 */
    @TableField("TREND")
    private Double trend;

    /** 主题 */
    @TableField("THEME")
    private String theme;

    /** 排序号 */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /** 创建时间 */
    @TableField("CREATE_TIME")
    private Date createTime;

    /** 更新时间 */
    @TableField("UPDATE_TIME")
    private Date updateTime;
}
