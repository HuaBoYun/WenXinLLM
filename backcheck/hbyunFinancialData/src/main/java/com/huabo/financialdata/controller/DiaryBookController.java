package com.huabo.financialdata.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.vo.diaryBook.DiaryBookRequestVo;
import com.huabo.financialdata.service.DiaryBookSerivce;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 日记账  接口请求类
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-11-20
 */
@RestController
@RequestMapping(value = "/diaryBook")
@Tag(name="日记账",description="日记账")
public class DiaryBookController {

    @Resource
    DiaryBookSerivce diaryBookSerivce;

    /**
     * 日记账 列表分页查询
     *
     * @param token              用户登录token
     * @param diaryBookRequestVo 请求参数封装公共类
     * @return
     */
    @PostMapping("/list")
    @Operation(summary="日记账 - 列表分页查询",description="日记账 - 列表分页查询")
    public ApiResponse<Map<String, Object>> getListByPage(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "detailedBookRequestVo", description = "日志账信息请求公共类diaryBookRequestVo", required = true) @RequestBody DiaryBookRequestVo diaryBookRequestVo) throws Exception {

        return diaryBookSerivce.getList(token, diaryBookRequestVo);
    }


}
