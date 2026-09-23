package com.huabo.legal.exam.modules.sys.depart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* <p>
* 部门排序请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-03-14 10:37
*/
@Data
@Schema(name="部门排序请求类", description="部门排序请求类")
public class DepartSortReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "分类ID")
    private String id;

    @Schema(name = "排序，0下降，1上升")
    private Integer sort;
}
