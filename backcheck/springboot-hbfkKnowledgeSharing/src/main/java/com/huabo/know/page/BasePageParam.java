package com.huabo.know.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * base page
 *
 * @date 2024/04/03
 */
@Getter
@Setter
public class BasePageParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(name = "页码，默认1")
    private Integer pageNumber = 1;
    @Schema(name = "每页数量，默认10")
    private Integer pageSize = 10;

}
