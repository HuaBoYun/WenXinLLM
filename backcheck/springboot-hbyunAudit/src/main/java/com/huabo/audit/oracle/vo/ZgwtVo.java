package com.huabo.audit.oracle.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ZgwtVo {
    @Schema(name = "审计单位")
    private String ORGNAME;
    @Schema(name = "已整改数量")
    private String yzg;
    @Schema(name = "未整改数量")
    private String wzg;
    @Schema(name = "整改总数")
    private String zs;
    @Schema(name = "已销号数量")
    private String yxh;
    @Schema(name = "未销号数量")
    private String wxh;
    @Schema(name = "销号总数")
    private String xhzs;
}
