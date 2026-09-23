package com.huabo.cybermonitor.vo;
import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报表模板查询参数VO
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReportTemplateQueryVO extends BaseVo {

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 报表类型
     */
    private String reportType;

    /**
     * 报表分类
     */
    private String reportCategory;

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
