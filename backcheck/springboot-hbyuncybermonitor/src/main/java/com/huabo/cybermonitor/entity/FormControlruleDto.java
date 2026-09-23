package com.huabo.cybermonitor.entity;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author：yhr
 * @date:2022-08-19 09:29
 * @description:
 */

@Data
public class FormControlruleDto {
    @Schema(name="规则id")
    private BigDecimal ruleid;
    @Schema(name="规则编码")
    String ruleno;
    @Schema(name="规则名称")
    private String rulename;
    @Schema(name="规则结果")
    private String returnresult;
    @Schema(name="规则提示")
    String ruletip;

    @Schema(name="规则状态")
    BigDecimal  rulestatus;
    @Schema(name="规则对应sql")
    String       rulesql;
    @Schema(name="规则描述")
    String rulememo;

}
