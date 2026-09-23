package com.huabo.fxgl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据模型返回VO
 * 
 * @author 华博云
 * @since 2025-01-21
 */
@Data
@Schema(name="RISKTOP10", description="风险TOP10")
public class RISKTOP10{


    @Schema(name = "风险编码")
    private String RISKNUMBER;

    @Schema(name = "风险名称")
    private String RISKNAME;

    @Schema(name = "风险描述")
    private String RISKDES;

    @Schema(name = "风险变化趋势")
    private String RISKCHANGE;

    @Schema(name = "月度评估汇总ID")
    private String EVALUATIONID;

    @Schema(name = "责任部门")
    private String orgname;

    @Schema(name = "责任领导")
    private String FIELD6;

    @Schema(name = "风险排序")
    private String RISKORDER;

}
