package com.global.treasurer.controller;

import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 利率参数配置Controller
 * 匹配前端API路径: /qqsk/financial/basicConfig/interestRateParam/*
 */
@RestController
@RequestMapping("/financial/basicConfig/interestRateParam")
@Api(tags = "利率参数配置")
public class InterestRateParamController {
    private static final Logger log = LoggerFactory.getLogger(InterestRateParamController.class);

    @PostMapping("/list")
    @ApiOperation("分页查询利率参数列表")
    public String getList(@RequestParam(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", new ArrayList<>());
            data.put("totalRecord", 0);
            data.put("message", "利率参数配置功能待实现");
            return new JsonBean(1, "查询成功（模拟数据）", data).toString();
        } catch (Exception e) {
            log.error("获取利率参数列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }
}
