package com.huabo.financialdata.entity.vo.gbi;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * GBI知识表
 * </p>
 *
 * @author 
 * @since 2024-04-20
 */
@Getter
@Setter
@Schema(name="数据配置-知识入参", description="数据配置-知识入参")
public class GbiKnowledgeRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="主键id")
    private String id;

    @Schema(name="自定义知识")
    @NotBlank(message = "知识不能为空")
    private String knowledgeKey;

    @Schema(name="对大模型解释这条知识")
    @NotBlank(message = "知识解释不能为空")
    private String knowledgeValue;
}
