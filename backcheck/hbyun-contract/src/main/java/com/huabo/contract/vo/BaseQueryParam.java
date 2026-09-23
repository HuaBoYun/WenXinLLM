package com.huabo.contract.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 基础查询参数类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@Accessors(chain = true)
public abstract class BaseQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="当前页数")
    private Integer pageNum = 1;

    @Schema(name="每页分页数量")
    private Integer pageSize = 15;

    /**
     * 获取当前页数（兼容MyBatis-Plus的current参数）
     */
    public Integer getCurrent() {
        return pageNum;
    }

    /**
     * 设置当前页数（兼容MyBatis-Plus的current参数）
     */
    public void setCurrent(Integer current) {
        this.pageNum = current;
    }

    /**
     * 获取页大小（兼容MyBatis-Plus的size参数）
     */
    public Integer getSize() {
        return pageSize;
    }

    /**
     * 设置页大小（兼容MyBatis-Plus的size参数）
     */
    public void setSize(Integer size) {
        this.pageSize = size;
    }
}
