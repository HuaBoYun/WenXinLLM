package com.huabo.legal.exam.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分页查询类
 * @param <T>
 * @author bool
 */
@Schema(name="分页参数", description="分页参数")
@Data
public class PagingReqDTO<T> {


    @Schema(name = "当前页码", required = true, example = "1")
    private Integer current;

    @Schema(name = "每页数量", required = true, example = "10")
    private Integer size;

    @Schema(name = "查询参数")
    private T params;

    @Schema(name = "排序字符")
    private String orderBy;

    @JsonIgnore
    @Schema(name = "当前用户的ID")
    private String userId;

    /**
     * 转换成MyBatis的简单分页对象
     * @return
     */
    public Page toPage(){
        Page page = new Page();
        page.setCurrent(this.current);
        page.setSize(this.size);
        return page;
    }


}
