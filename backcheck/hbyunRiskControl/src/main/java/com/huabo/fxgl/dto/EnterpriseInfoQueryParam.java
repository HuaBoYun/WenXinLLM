package com.huabo.fxgl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

//import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 企业信息查询参数
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Data
@Schema(name="EnterpriseInfoQueryParam", description="企业信息查询参数")
public class EnterpriseInfoQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "企业ID", required = true)
//    @NotBlank(message = "企业ID不能为空")
    private String enterpriseId;

    @Schema(name = "企业名称")
    private String enterpriseName;

    @Schema(name="是否包含扩展信息")
    private Boolean includeExtendedInfo = true;
}
