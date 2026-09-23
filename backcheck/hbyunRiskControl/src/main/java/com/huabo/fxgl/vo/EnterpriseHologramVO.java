package com.huabo.fxgl.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 企业全息画像VO
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Data
@Schema(name="EnterpriseHologramVO", description="企业全息画像VO")
public class EnterpriseHologramVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="企业标签列表")
    private List<EnterpriseTag> enterpriseTags;

    @Schema(name="关键指标列表")
    private List<KeyIndicator> keyIndicators;

    @Schema(name="风险等级")
    private String riskLevel;

    @Schema(name="风险评分")
    private String riskScore;

    @Schema(name="风险因素说明")
    private String riskFactors;

    /**
     * 企业标签
     */
    @Data
    @Schema(name="EnterpriseTag", description="企业标签")
    public static class EnterpriseTag implements Serializable {
        
        @Schema(name="标签ID")
        private String tagId;
        
        @Schema(name="标签编码")
        private String tagCode;
        
        @Schema(name="标签名称")
        private String tagName;
        
        @Schema(name="标签类型")
        private String tagType;
        
        @Schema(name="标签分类")
        private String tagCategory;
        
        @Schema(name="标签值")
        private String tagValue;
    }

    /**
     * 关键指标
     */
    @Data
    @Schema(name="KeyIndicator", description="关键指标")
    public static class KeyIndicator implements Serializable {
        
        @Schema(name="指标编码")
        private String code;
        
        @Schema(name="指标名称")
        private String name;
        
        @Schema(name="指标值")
        private String value;
        
        @Schema(name="指标单位")
        private String unit;
        
        @Schema(name="指标类型")
        private String type;
    }
}
