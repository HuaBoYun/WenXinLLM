package com.huabo.financialdata.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.vo.prjData.LrbRequestVo;
import com.huabo.financialdata.entity.vo.prjData.ZcfzbRequestVo;
import com.huabo.financialdata.entity.vo.prjData.ZcfzbResponseVo;
import com.huabo.financialdata.service.PrjDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 报表数据  接口请求类
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-12-01
 */
@RestController
@RequestMapping(value = "/prjData")
@Tag(name="报表数据",description="报表数据")
public class PrjDataController {

    @Resource
    PrjDataService prjDataService;

    /**
     * 报表数据  资产负债表  列表分页查询
     *
     * @param token          用户登录token
     * @param zcfzbRequestVo 请求参数封装公共类
     * @return
     */
    @PostMapping("/zcfzb/list")
    @Operation(summary="报表数据 - 资产负债表-- 列表分页查询",description="报表数据 - 资产负债表-- 列表分页查询")
    public ApiResponse<PageInfo<ZcfzbResponseVo>> getZcfzbList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "zcfzbRequestVo", description = "报表数据资产负债表请求公共类zcfzbRequestVo", required = true) @RequestBody ZcfzbRequestVo zcfzbRequestVo) throws Exception {

        return prjDataService.getZcfzbList(token, zcfzbRequestVo);
    }


    /**
     * 报表数据  利润表  列表分页查询
     *
     * @param token        用户登录token
     * @param lrbRequestVo 请求参数封装公共类
     * @return
     */
    @PostMapping("/lrb/list")
    @Operation(summary="报表数据 - 利润表-- 列表分页查询",description="报表数据 - 利润表-- 列表分页查询")
    public ApiResponse<PageInfo<ZcfzbResponseVo>> getLrbList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "lrbRequestVo", description = "报表数据利润表请求公共类lrbRequestVo", required = true) @RequestBody LrbRequestVo lrbRequestVo) throws Exception {

        return prjDataService.getLrbList(token, lrbRequestVo);
    }


}
