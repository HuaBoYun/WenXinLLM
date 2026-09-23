package com.huabo.legal.startup.security.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色查询条件
 *
 * @author zhuhuix
 * @date 2021-09-22
 */
@Schema(name="角色查询条件")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleQueryDto  {

    @Schema(name = "角色名称")
    private String roleName;

    @Schema(name = "创建起始时间")
    private Long createTimeStart;

    @Schema(name = "创建结束时间")
    private Long createTimeEnd;

}
