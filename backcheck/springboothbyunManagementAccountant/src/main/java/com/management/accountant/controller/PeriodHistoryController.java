package com.management.accountant.controller;

import com.management.accountant.oracle.entity.budget.PeriodHistory;
import com.management.accountant.service.PeriodHistoryService;
import com.management.accountant.util.MyJsonBean;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = {"NCV65全面预算-期间历史"})
@RequestMapping(value = "/accountant/budget/period/history")
@Slf4j
public class PeriodHistoryController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private PeriodHistoryService periodHistoryService;

    @Operation(summary = "获取期间操作历史")
    @ApiOperation("获取期间操作历史") @GetMapping("/list/{periodId}")
    public MyJsonBean<List<PeriodHistory>> getHistory(@PathVariable String periodId) {
        MyJsonBean<List<PeriodHistory>> r = new MyJsonBean<>();
        try {
            r.setCode(1);
            r.setMsg("查询成功");
            r.setData(periodHistoryService.getHistoryByPeriodId(periodId));
        } catch (Exception e) {
            log.error("查询期间历史异常", e);
            r.setCode(0);
            r.setMsg("查询失败：" + e.getMessage());
        }
        return r;
    }
}
