package com.global.treasurer.controller;

import com.hbfk.util.JsonBean;
import com.global.treasurer.dto.CreditLimitDTO;
import com.global.treasurer.dto.CreditLimitQueryDTO;
import com.global.treasurer.entity.TblCreditLimit;
import com.global.treasurer.service.CreditLimitService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * 授信额度管理Controller
 * 注意：/financial/rzgl/credit/limit 路径已在 RzglController 中定义，避免重复映射
 *
 * @author 华博云开发团队
 * @since 2026-02-04
 */
@RestController
@RequestMapping("/financing/credit-limit")
@Api(tags = "授信额度管理")
public class CreditLimitController {

    private static final Logger log = LoggerFactory.getLogger(CreditLimitController.class);

    @Autowired
    private CreditLimitService creditLimitService;

    @PostMapping("/page")
    @ApiOperation("授信额度分页查询")
    public String page(@FlexibleRequestBody CreditLimitQueryDTO queryDTO) {
        try {
            log.info("授信额度分页查询, queryDTO: {}", queryDTO);
            PageInfo<TblCreditLimit> pageInfo = creditLimitService.getLimitList(queryDTO);
            
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());
            
            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("授信额度分页查询失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("授信额度详情查询")
    public String detail(@ApiParam("额度ID") @PathVariable Long id) {
        try {
            log.info("查询授信额度详情, id: {}", id);
            TblCreditLimit limit = creditLimitService.getLimitById(id);
            return new JsonBean(1, "查询成功", limit).toString();
        } catch (Exception e) {
            log.error("查询授信额度详情失败, id: {}", id, e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping({"/add", ""})
    @ApiOperation("新增授信额度")
    public String add(@FlexibleRequestBody CreditLimitDTO dto) {
        try {
            log.info("新增授信额度, dto: {}", dto);
            TblCreditLimit limit = creditLimitService.saveLimit(dto);
            return new JsonBean(1, "新增成功", limit).toString();
        } catch (Exception e) {
            log.error("新增授信额度失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage()).toString();
        }
    }

    @PutMapping({"/adjust", "/{id}"})
    @ApiOperation("调整授信额度")
    public String adjust(@PathVariable(required = false) Long id, @FlexibleRequestBody CreditLimitDTO dto) {
        try {
            log.info("调整授信额度, id: {}, dto: {}", id, dto);
            if (id != null && dto.getLimitId() == null) {
                dto.setLimitId(id);
            }
            TblCreditLimit limit = creditLimitService.saveLimit(dto);
            return new JsonBean(1, "调整成功", limit).toString();
        } catch (Exception e) {
            log.error("调整授信额度失败", e);
            return new JsonBean(0, "调整失败: " + e.getMessage()).toString();
        }
    }

    @PutMapping("/freeze/{id}")
    @ApiOperation("冻结授信额度")
    public String freeze(
            @ApiParam("额度ID") @PathVariable Long id,
            @ApiParam("冻结原因") @RequestParam(required = false) String reason) {
        try {
            log.info("冻结授信额度, id: {}, reason: {}", id, reason);
            // TODO: 功能待实现，需要传入冻结金额参数
            return new JsonBean(1, "冻结成功（功能待完善）").toString();
        } catch (Exception e) {
            log.error("冻结授信额度失败, id: {}", id, e);
            return new JsonBean(0, "冻结失败: " + e.getMessage()).toString();
        }
    }

    @PutMapping("/unfreeze/{id}")
    @ApiOperation("解冻授信额度")
    public String unfreeze(@ApiParam("额度ID") @PathVariable Long id) {
        try {
            log.info("解冻授信额度, id: {}", id);
            // TODO: 功能待实现，需要传入解冻金额参数
            return new JsonBean(1, "解冻成功（功能待完善）").toString();
        } catch (Exception e) {
            log.error("解冻授信额度失败, id: {}", id, e);
            return new JsonBean(0, "解冻失败: " + e.getMessage()).toString();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除授信额度")
    public String delete(@ApiParam("额度ID") @PathVariable Long id) {
        try {
            log.info("删除授信额度, id: {}", id);
            creditLimitService.deleteLimit(id);
            return new JsonBean(1, "删除成功").toString();
        } catch (Exception e) {
            log.error("删除授信额度失败, id: {}", id, e);
            return new JsonBean(0, "删除失败: " + e.getMessage()).toString();
        }
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除授信额度")
    public String batchDelete(@ApiParam("额度ID列表") @RequestParam(value = "ids", required = false) List<Long> ids) {
        try {
            log.info("批量删除授信额度, ids: {}", ids);
            creditLimitService.batchDeleteLimits(ids);
            return new JsonBean(1, "批量删除成功").toString();
        } catch (Exception e) {
            log.error("批量删除授信额度失败, ids: {}", ids, e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage()).toString();
        }
    }
}

