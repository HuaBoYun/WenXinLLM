package com.huabo.fxgl.dto;

//import javax.validation.constraints.NotBlank;
import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 财务雷达图查询参数
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Data
@Schema(name="FinancialRadarQueryParam", description="财务雷达图查询参数")
public class FinancialRadarQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "企业ID", required = true)
//    @NotBlank(message = "企业ID不能为空")
    private String enterpriseId;

    @Schema(name = "企业名称")
    private String enterpriseName;

    @Schema(name="期间日期(格式: yyyy-MM-dd)")
    private String periodDate;

    @Schema(name="是否包含关键指标")
    private Boolean includeKeyIndicators = true;
}
