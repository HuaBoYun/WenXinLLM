package com.huabo.financialdata.entity.dto.gbi;

import com.huabo.financialdata.entity.base.BasePageDO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;


@Data
@Schema(name="数据详情查询入参")
public class GbiTableDataQuery extends BasePageDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(name = "表id")
    private String tableId;


}
