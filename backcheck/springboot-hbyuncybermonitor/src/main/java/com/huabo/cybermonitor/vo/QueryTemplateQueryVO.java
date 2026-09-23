package com.huabo.cybermonitor.vo;
import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询模板查询参数VO
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryTemplateQueryVO extends BaseVo {

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板类型
     */
    private String templateType;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 是否公开
     */
    private Boolean isPublic;

    /**
     * 创建人
     */
    private String createBy;
}
