package com.huabo.financialdata.entity.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 按月明细表 - 查询请求参数
 *
 * @author lee
 * @version 1.0.0
 **/
@Data
@Schema(name="按月明细表 - 查询请求参数")
public class AccBsegQuery extends AccBseg {
    private static final long serialVersionUID = 6308103180342331422L;
}
