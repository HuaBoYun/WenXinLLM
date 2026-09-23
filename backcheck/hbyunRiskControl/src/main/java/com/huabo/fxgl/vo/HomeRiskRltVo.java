package com.huabo.fxgl.vo;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HomeRiskRltVo {
    @Schema(name = "公司名称")
    private String orgname;

    @Schema(name = "公司id")
    private BigDecimal unit;

    @Schema(name = "频率1")
    private BigDecimal frequency1;

    @Schema(name = "频率2")
    private BigDecimal frequency2;

    @Schema(name = "频率3")
    private BigDecimal frequency3;

    @Schema(name = "频率4")
    private BigDecimal frequency4;

    @Schema(name = "频率5")
    private BigDecimal frequency5;

    @Schema(name = "严重程度1")
    private BigDecimal severity1;

    @Schema(name = "严重程度2")
    private BigDecimal severity2;

    @Schema(name = "严重程度3")
    private BigDecimal severity3;

    @Schema(name = "严重程度4")
    private BigDecimal severity4;

    @Schema(name = "严重程度5")
    private BigDecimal severity5;
}
