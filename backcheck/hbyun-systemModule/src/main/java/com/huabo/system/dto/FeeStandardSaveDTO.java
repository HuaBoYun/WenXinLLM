package com.huabo.system.dto;

import java.math.BigDecimal;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "FeeStandardSaveDTO", description = "费用标准保存请求参数")
public class FeeStandardSaveDTO {

    @Schema(name = "关联TBL_SYSTEM_RIGHT.ID")
    private BigDecimal rightId;

    @Schema(name = "计费方式 1=按次 2=按调用量")
    private Integer feeType;

    @Schema(name = "计费金额（按次时使用）")
    private BigDecimal feeAmount;

    @Schema(name = "变更备注")
    private String remark;

    @Schema(name = "阶梯规则（按调用量时使用）")
    private List<TierItem> tiers;

    @Data
    public static class TierItem {
        @Schema(name = "阶梯起始调用量（含）")
        private Integer minCount;
        @Schema(name = "阶梯结束调用量（含），null=无上限")
        private Integer maxCount;
        @Schema(name = "该阶梯单价（元/次）")
        private BigDecimal feeAmount;
    }
}
