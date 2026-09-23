package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.dto.export.ExportAccountFreezeDTO;
import com.global.treasurer.entity.TblGtAccountFreeze;
import com.global.treasurer.service.TblGtAccountFreezeService;
import com.global.treasurer.util.excel.ExcelExport;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全球司库-账户冻结管理控制器
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@RestController
@RequestMapping("/financial/account-freeze")
@Api(tags = "账户冻结管理")
public class TblGtAccountFreezeController {
    private static final Logger log = LoggerFactory.getLogger(TblGtAccountFreezeController.class);

    @Resource
    private TblGtAccountFreezeService tblGtAccountFreezeService;

    @Resource
    private UserProvider userProvider;

    private boolean validateUser() {
        return true;
    }

    /**
     * 分页查询冻结记录列表
     */
    @RequestMapping(value = "/page", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("分页查询冻结记录列表")
    public String getPageList(
            @ApiParam(value = "页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量") @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            @ApiParam(value = "记录ID") @RequestParam(value = "recordId", required = false) Long recordId,
            @ApiParam(value = "账户号码") @RequestParam(value = "accountNumber", required = false) String accountNumber,
            @ApiParam(value = "冻结类型") @RequestParam(value = "freezeType", required = false) String freezeType,
            @ApiParam(value = "冻结状态") @RequestParam(value = "freezeStatus", required = false) String freezeStatus) {

        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Page<TblGtAccountFreeze> pageObj = new Page<>(page, limit);
            IPage<TblGtAccountFreeze> result = tblGtAccountFreezeService.getPageList(
                    pageObj, recordId, accountNumber, freezeType, freezeStatus
            );

            if (result == null) {
                log.warn("查询结果为null");
                Map<String, Object> emptyData = new HashMap<>();
                emptyData.put("tlist", new ArrayList<>());
                emptyData.put("totalRecord", 0);
                emptyData.put("pageNo", page);
                emptyData.put("pageSize", limit);
                return JsonBean.success(emptyData);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            return JsonBean.success(data);

        } catch (Exception e) {
            log.error("查询冻结记录列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取统计数据
     */
    @GetMapping("/statistics")
    @ApiOperation("获取冻结统计数据")
    public String getStatistics() {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> statistics = tblGtAccountFreezeService.getStatistics();
            return JsonBean.success(statistics);

        } catch (Exception e) {
            log.error("获取统计数据失败", e);
            Map<String, Object> defaultStats = new HashMap<>();
            defaultStats.put("totalRecords", 0);
            defaultStats.put("frozenRecords", 0);
            defaultStats.put("unfrozenRecords", 0);
            defaultStats.put("unfreezeRate", 0.0);
            defaultStats.put("totalFrozenAmount", 0.0);
            return JsonBean.success(defaultStats);
        }
    }

    /**
     * 根据ID查询详情
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询冻结记录详情")
    public String getById(@ApiParam(value = "记录ID", required = true) @PathVariable Long id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblGtAccountFreeze entity = tblGtAccountFreezeService.getById(id);
            if (entity == null) {
                return new JsonBean(0, "记录不存在", null).toJson();
            }

            return JsonBean.success(entity);

        } catch (Exception e) {
            log.error("查询冻结记录详情失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增冻结记录
     */
    @PostMapping("/")
    @ApiOperation("新增冻结记录")
    public String save(@FlexibleRequestBody TblGtAccountFreeze entity) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            entity.setOperatorId(loginStaff.getStaffid());

            boolean success = tblGtAccountFreezeService.saveFreezeRecord(entity);
            return success ? JsonBean.success(entity) : new JsonBean(0, "新增失败", null).toJson();

        } catch (Exception e) {
            log.error("新增冻结记录失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 解冻操作
     */
    @PutMapping("/{id}/unfreeze")
    @ApiOperation("解冻操作")
    public String unfreeze(
            @ApiParam(value = "记录ID", required = true) @PathVariable Long id,
            @ApiParam(value = "解冻类型", required = true) @RequestParam String unfreezeType,
            @ApiParam(value = "解冻金额") @RequestParam(required = false) BigDecimal unfreezeAmount,
            @ApiParam(value = "解冻原因", required = true) @RequestParam String unfreezeReason) {

        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            boolean success = tblGtAccountFreezeService.unfreezeRecord(
                    id, unfreezeType, unfreezeAmount, unfreezeReason
            );

            return success ? JsonBean.success("解冻成功") : new JsonBean(0, "解冻失败", null).toJson();

        } catch (Exception e) {
            log.error("解冻操作失败", e);
            return new JsonBean(0, "解冻失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除冻结记录
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除冻结记录")
    public String delete(@ApiParam(value = "记录ID", required = true) @PathVariable Long id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            boolean success = tblGtAccountFreezeService.deleteFreezeRecord(id);
            return success ? JsonBean.success("删除成功") : new JsonBean(0, "删除失败", null).toJson();

        } catch (Exception e) {
            log.error("删除冻结记录失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量解冻
     */
    @PutMapping("/batch-unfreeze")
    @ApiOperation("批量解冻")
    public String batchUnfreeze(@RequestBody Map<String, Object> params) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Object recordIdsObj = params.get("recordIds");
            if (recordIdsObj == null) {
                return new JsonBean(0, "recordIds不能为空", null).toJson();
            }

            @SuppressWarnings("unchecked")
            List<Object> rawIds = (List<Object>) recordIdsObj;
            List<Long> recordIds = new ArrayList<>();
            for (Object id : rawIds) {
                recordIds.add(Long.valueOf(id.toString()));
            }

            int successCount = tblGtAccountFreezeService.batchUnfreeze(recordIds);
            return JsonBean.success("批量解冻成功，共解冻" + successCount + "条记录");

        } catch (Exception e) {
            log.error("批量解冻失败", e);
            return new JsonBean(0, "批量解冻失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出冻结记录
     */
    @GetMapping("/export")
    @ApiOperation("导出冻结记录")
    public void export(
            @ApiParam(value = "记录ID") @RequestParam(required = false) Long recordId,
            @ApiParam(value = "账户号码") @RequestParam(required = false) String accountNumber,
            @ApiParam(value = "冻结类型") @RequestParam(required = false) String freezeType,
            @ApiParam(value = "冻结状态") @RequestParam(required = false) String freezeStatus,
            HttpServletResponse response) {
        try {
            List<ExportAccountFreezeDTO> list = tblGtAccountFreezeService.exportList(
                    recordId, accountNumber, freezeType, freezeStatus);
            ExcelExport ee = new ExcelExport("账户冻结记录", ExportAccountFreezeDTO.class);
            ee.setDataList(list).write(response, "账户冻结记录.xlsx");
        } catch (Exception e) {
            log.error("导出冻结记录失败", e);
        }
    }
}