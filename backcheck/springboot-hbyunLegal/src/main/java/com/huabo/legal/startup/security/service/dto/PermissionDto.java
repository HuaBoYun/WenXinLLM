package com.huabo.legal.startup.security.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户权限
 *
 * @author zhuhuix
 * @date 2021-08-31
 */
@Schema(name="用户权限信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDto {

    @Schema(name = "id")
    private Long id;

    @Schema(name = "路由")
    private String path;

    @Schema(name = "名称")
    private String name;

    @Schema(name = "组件路径")
    private String component;

    @Schema(name = "是否隐藏")
    private Boolean hidden;

    @Schema(name = "图标")
    private String icon;

    @Schema(name = "是否缓存")
    private Boolean cache;

    @Schema(name = "重定向")
    private String redirect;

    @Schema(name = "父id")
    private Long pId;


}
