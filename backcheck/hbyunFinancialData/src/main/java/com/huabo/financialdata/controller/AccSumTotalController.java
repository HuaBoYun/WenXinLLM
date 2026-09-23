package com.huabo.financialdata.controller;


import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.vo.accSumTotal.AccSumTotalRequestVo;
import com.huabo.financialdata.entity.vo.accSumTotal.AccSumTotalResponseVo;
import com.huabo.financialdata.service.AccSumTotalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 总分类账  接口请求类
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-11-18
 */
@RestController
@RequestMapping(value = "/accSumTotal")
@Tag(name="总分类账",description="总分类账")
public class AccSumTotalController {

    @Resource
    AccSumTotalService accSumTotalService;

    /**
     * 总分类账 列表分页查询
     *
     * @param token                用户登录token
     * @param accSumTotalRequestVo 请求参数封装公共类
     * @return
     */
    @PostMapping("/list")
    @Operation(summary="总分类账 - 列表分页查询",description="总分类账 - 列表分页查询")
    public ApiResponse<PageInfo<AccSumTotalResponseVo>> getListByPage(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "accSumTotalRequestVo", description = "总分类账信息请求公共类accSumTotalRequestVo", required = true) @RequestBody AccSumTotalRequestVo accSumTotalRequestVo) {

        return accSumTotalService.getList(token, accSumTotalRequestVo);
    }


}
