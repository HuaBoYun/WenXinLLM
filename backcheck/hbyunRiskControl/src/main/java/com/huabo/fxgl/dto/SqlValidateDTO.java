
package com.huabo.fxgl.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * SQL验证请求DTO
 * @author 华博云
 * @since 2025-01-21
 */
@Data
@Schema(description="SQL验证请求参数")
public class SqlValidateDTO {

    @Schema(name = "SQL内容", required = true)
    private String sqlContent;

    @Schema(name = "数据库类型", example = "DM")
    private String databaseType;
}
