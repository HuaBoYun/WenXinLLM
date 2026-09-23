package com.huabo.financialdata.controller;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.vo.auxiliaryBook.AuxiliaryBookRequestVo;
import com.huabo.financialdata.entity.vo.auxiliaryBook.AuxiliaryBookResponsetVo;
import com.huabo.financialdata.service.AuxiliaryBookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 辅助账  接口请求类
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-11-21
 */
@RestController
@RequestMapping(value = "/auxiliaryBook")
@Tag(name="辅助账",description="辅助账")
public class AuxiliaryBookController {

    @Resource
    AuxiliaryBookService auxiliaryBookService;

    /**
     * 辅助账 
     *
     * @param token                  用户登录token
     * @param auxiliaryBookRequestVo 请求参数封装公共类
     * @return
     */
    @PostMapping("/assTypeList")
    @Operation(summary="辅助账类型菜单查询",description="辅助账 - 列表分页查询")
    public ApiResponse<List<String>> getListByPage(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        return auxiliaryBookService.getAssTypeList(token);
    }

    /**
     * 辅助账 
     *
     * @param token                  用户登录token
     * @param auxiliaryBookRequestVo 请求参数封装公共类
     * @return
     */
    @PostMapping("/list")
    @Operation(summary="辅助账 - 列表分页查询",description="辅助账 - 列表分页查询")
    public ApiResponse<PageInfo<AuxiliaryBookResponsetVo>> getListByPage(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "auxiliaryBookRequestVo", description = "辅助账信息请求公共类auxiliaryBookRequestVo", required = true) @RequestBody AuxiliaryBookRequestVo auxiliaryBookRequestVo) throws Exception {

        return auxiliaryBookService.getList(token, auxiliaryBookRequestVo);
    }


}
