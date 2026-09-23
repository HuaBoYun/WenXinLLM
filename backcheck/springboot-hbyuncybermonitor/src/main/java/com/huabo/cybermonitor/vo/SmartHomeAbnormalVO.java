package com.huabo.cybermonitor.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 首页异常检测统一VO
 *
 * @author system
 * @date 2026-05-28
 */
@Data
public class SmartHomeAbnormalVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String domain;
    private String desc;
    private String typeLabel;
    private String route;
}
