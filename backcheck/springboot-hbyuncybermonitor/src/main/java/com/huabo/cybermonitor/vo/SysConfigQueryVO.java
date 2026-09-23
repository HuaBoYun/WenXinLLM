package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统配置查询VO
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysConfigQueryVO extends BaseVo {

    /**
     * 配置键
     */
    private String configKey;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 配置分组
     */
    private String configGroup;

    /**
     * 配置类型
     */
    private String configType;

    /**
     * 是否系统配置
     */
    private String isSystem;

    /**
     * 是否启用
     */
    private String isEnabled;

    /**
     * 更新时间-起始
     */
    private String updateTimeBegin;

    /**
     * 更新时间-结束
     */
    private String updateTimeEnd;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向
     */
    private String orderDirection;
}
