package com.management.accountant.controller;

import com.management.accountant.oracle.entity.budget.PeriodSetting;
import com.management.accountant.service.PeriodSettingService;
import com.management.accountant.util.MyJsonBean;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(tags = {"NCV65全面预算-期间设置"})
@RequestMapping(value = "/accountant/budget/period/setting")
@Slf4j
public class PeriodSettingController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private PeriodSettingService periodSettingService;

    @Operation(summary = "获取期间设置")
    @ApiOperation("获取期间设置") @GetMapping("/get")
    public MyJsonBean<PeriodSetting> getSetting() {
        MyJsonBean<PeriodSetting> r = new MyJsonBean<>();
        try {
            r.setCode(1);
            r.setMsg("查询成功");
            r.setData(periodSettingService.getSetting());
        } catch (Exception e) {
            log.error("获取期间设置异常", e);
            r.setCode(0);
            r.setMsg("查询失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "保存期间设置")
    @ApiOperation("保存期间设置") @PostMapping("/save")
    public MyJsonBean<PeriodSetting> save(@RequestBody PeriodSetting setting) {
        MyJsonBean<PeriodSetting> r = new MyJsonBean<>();
        try {
            r.setCode(1);
            r.setMsg("保存成功");
            r.setData(periodSettingService.save(setting));
        } catch (Exception e) {
            log.error("保存期间设置异常", e);
            r.setCode(0);
            r.setMsg("保存失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "重置期间设置")
    @ApiOperation("重置期间设置") @PostMapping("/reset")
    public MyJsonBean<Void> reset() {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try {
            periodSettingService.reset();
            r.setCode(1);
            r.setMsg("重置成功");
        } catch (Exception e) {
            log.error("重置期间设置异常", e);
            r.setCode(0);
            r.setMsg("重置失败：" + e.getMessage());
        }
        return r;
    }
}
