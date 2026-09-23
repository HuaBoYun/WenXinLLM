package com.huabo.legal.startup.security.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户查询条件
 *
 * @author zhuhuix
 * @date 2021-09-28
 *
 * @date 2022-04-04
 * 增加分页传入参数
 */
@Schema(name="用户查询条件")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserQueryDto {

    @Schema(name = "用户名")
    private String userName;

    @Schema(name = "注册起始时间")
    private Long createTimeStart;

    @Schema(name = "注册结束时间")
    private Long createTimeEnd;

    @Schema(name = "当前页数")
    private Integer currentPage;

    @Schema(name = "每页条数")
    private Integer pageSize;
}
