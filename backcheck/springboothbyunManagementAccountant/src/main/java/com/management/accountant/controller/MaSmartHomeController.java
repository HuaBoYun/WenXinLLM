package com.management.accountant.controller;

import com.management.accountant.service.MaSmartHomeService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 管理会计智慧首页数据接口
 *
 * @author system
 * @date 2025-01-21
 */
@RestController
@RequestMapping("/accountant/smartHome")
@Api(tags = "管理会计智慧首页")
@Slf4j
public class MaSmartHomeController {

    @Resource
    private MaSmartHomeService maSmartHomeService;

    /**
     * 获取管理会计首页汇总数据
     * 包含：欢迎区统计、KPI卡片数据、链路节点徽标计数
     *
     * @param year 查询年份
     * @return MyJsonBean
     */
    @GetMapping("/getData")
    @ApiOperation("获取管理会计首页汇总数据")
    public MyJsonBean<Map<String, Object>> getSmartHomeData() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> data = maSmartHomeService.getSmartHomeData();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("获取管理会计首页数据失败", e);
            result.setCode(0);
            result.setMsg("获取数据失败: " + e.getMessage());
        }
        return result;
    }
}
