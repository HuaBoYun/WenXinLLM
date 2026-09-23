package com.huabo.system.vo;

import java.math.BigDecimal;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "FeeStatisticsVO", description = "费用统计结果")
public class FeeStatisticsVO {

    @Schema(name = "总费用")
    private BigDecimal totalFee;

    @Schema(name = "总调用次数")
    private Long totalCallCount;

    @Schema(name = "按模块统计列表")
    private List<ModuleStatItem> moduleStats;

    @Data
    public static class ModuleStatItem {

        @Schema(name = "模块名称")
        private String moduleName;

        @Schema(name = "模块类型")
        private String moduleType;

        @Schema(name = "费用合计")
        private BigDecimal totalFee;

        @Schema(name = "调用次数")
        private Long callCount;
    }
}
