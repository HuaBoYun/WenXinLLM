package com.huabo.system.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import com.hbfk.util.JsonBean;
import com.huabo.system.dto.FeeStandardSaveDTO;
import com.huabo.system.service.FeeStandardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 费用标准管理控制器
 * <p>提供费用标准的列表查询和保存接口</p>
 *
 * @author hbyun
 */
@Slf4j
@Tag(name = "费用标准管理")
@RestController
@RequestMapping(value = "/system/fee/standard")
public class FeeStandardController {

    @Resource
    private FeeStandardService feeStandardService;

    @RequestMapping(value = "/list", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "费用标准列表-按模块类型查询计费标准树形列表")
    public JsonBean list(HttpServletRequest request,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "moduletype", description = "模块类型", required = false) @RequestParam(value = "moduletype", required = false) String moduletype) {
        try {
            return feeStandardService.getStandardList(token, moduletype);
        } catch (Exception e) {
            log.error("查询费用标准列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @RequestMapping(value = "/save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "新增/修改费用标准")
    public JsonBean save(HttpServletRequest request,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody FeeStandardSaveDTO dto) {
        try {
            return feeStandardService.saveStandard(token, dto);
        } catch (Exception e) {
            log.error("保存费用标准失败", e);
            return new JsonBean(0, "保存失败: " + e.getMessage(), null);
        }
    }
}
