package com.financial.sharing.vo.param;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 系统参数查询参数
 * @author system
 * @since 2024-12-19
 */
@Data
public class SystemParamsQueryParam {

    /**
     * 参数编码
     */
    private String paramCode;

    /**
     * 参数名称
     */
    private String paramName;

    /**
     * 分类编码
     */
    private String categoryCode;

    /**
     * 参数类型
     */
    private String paramType;

    /**
     * 是否启用
     */
    private Integer isEnabled;

    /**
     * 租户ID
     */
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 当前页码 (兼容前端pageNo和pageNumber)
     */
    private Integer pageNo = 1;

    /**
     * 当前页码
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNumber = 1;

    /**
     * 每页大小
     */
    @NotNull(message = "每页大小不能为空")
    @Min(value = 1, message = "每页大小必须大于0")
    private Integer pageSize = 10;

    /**
     * 获取当前页码，优先返回pageNo
     */
    public Integer getPageNumber() {
        return pageNo != null ? pageNo : pageNumber;
    }
}