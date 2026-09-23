package com.huabo.cybermonitor.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 首页风险预警统一VO
 * 用于将各领域不同结构的预警表数据规范化为统一格式
 *
 * @author system
 * @date 2026-05-28
 */
@Data
public class SmartHomeWarningVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 预警ID */
    private String id;

    /** 预警标题 */
    private String title;

    /** 所属领域 */
    private String domain;

    /** 描述 */
    private String desc;

    /** 风险等级 HIGH/MEDIUM/LOW */
    private String level;

    /** 等级标签 高/中/低 */
    private String levelLabel;

    /** 时间 */
    private String time;

    /** 跳转路由 */
    private String route;
}
