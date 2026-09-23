package com.huabo.compliance.mysql.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TestEntityMySql {
    @Schema(required = true, description = "用户名", example = "入参用户名")
    private String name;

    @Schema(required = true, description = "入参信息", example = "入参信息")
    private String message;

}
