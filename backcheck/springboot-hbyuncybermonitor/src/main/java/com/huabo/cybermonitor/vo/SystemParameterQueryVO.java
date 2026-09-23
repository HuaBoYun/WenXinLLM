package com.huabo.cybermonitor.vo;
import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统参数查询VO
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemParameterQueryVO extends BaseVo {

    /**
     * 参数键名
     */
    private String parameterKey;

    /**
     * 参数名称
     */
    private String parameterName;

    /**
     * 参数类型
     */
    private String parameterType;

    /**
     * 参数分组
     */
    private String parameterGroup;

    /**
     * 是否系统参数
     */
    private Boolean isSystem;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 创建人
     */
    private String createBy;
}
