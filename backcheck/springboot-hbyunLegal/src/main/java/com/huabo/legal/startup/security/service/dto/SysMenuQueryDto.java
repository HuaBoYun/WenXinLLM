package com.huabo.legal.startup.security.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单查询条件
 *
 * @author zhuhuix
 * @date 2021-10-06
 */
@Schema(name="菜单查询条件")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysMenuQueryDto {

    @Schema(name = "菜单名称")
    private String name;

    @Schema(name = "创建起始时间")
    private Long createTimeStart;

    @Schema(name = "创建结束时间")
    private Long createTimeEnd;
}
