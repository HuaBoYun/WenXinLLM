package com.huabo.financialdata.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.dto.accBseg.DetailClassPageQuery;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 明细账
 *
 * @author lee
 * @version 1.0.0
 **/
@RestController
@RequestMapping("/accbseg")
@Tag(name="明细账管理",description="明细账管理")
public class AccBsegController {

    @PostMapping("/list")
    @Operation(summary="明细分类账 - 分页查询",description="明细分类账 - 分页查询")
    public ApiResponse getListByPage(@Validated @RequestBody DetailClassPageQuery request) {
        return null;
    }

    @PostMapping("/jzpz")
    @Operation(summary="记账凭证",description="记账凭证")
    public ApiResponse getAccCredentials() {
        return null;
    }
}
