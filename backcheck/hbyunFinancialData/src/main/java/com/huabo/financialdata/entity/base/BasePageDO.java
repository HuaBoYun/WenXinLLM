package com.huabo.financialdata.entity.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页基础实体
 *
 * @author lee
 * @version 1.0.0
 */
@Data
public class BasePageDO implements Serializable {
    private static final long serialVersionUID = -5375660778833032707L;

    @Schema(name = "每页显示条数")
    private Integer pageSize;

    @Schema(name = "当前页数")
    private Integer pageNo;

    @Schema(name = "模块来源-预留")
    private String mty;
}
