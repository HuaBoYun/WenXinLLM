package com.financial.sharing.enterpriseReport.controller;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.enterpriseReport.dto.IndicatorQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblIndicatorInfo;
import com.financial.sharing.enterpriseReport.service.IndicatorService;
import com.hbfk.util.user.UserProvider;
import com.financial.sharing.util.JsonMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/enterpriseReport/indicator")
@Tag(name = "指标信息管理")
public class IndicatorController {

    @Autowired
    private IndicatorService indicatorService;

    @PostMapping("/getPage")
    @Operation(summary = "分页查询指标列表")
    public String getPage(@RequestBody IndicatorQueryParam param) {
        try {
            if (UserUtils.getUser() == null) {
                return JsonMapper.buildFalseJson("用户未登录");
            }
            Page<TblIndicatorInfo> page = indicatorService.getPage(param);
            return JsonMapper.buildSuccessJson(page);
        } catch (Exception e) {
            log.error("查询指标列表失败", e);
            return JsonMapper.buildFalseJson("查询指标列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/detail")
    @Operation(summary = "查询指标详情")
    public String detail(@RequestBody Map<String, String> params) {
        try {
            if (UserUtils.getUser() == null) {
                return JsonMapper.buildFalseJson("用户未登录");
            }
            String indicatorId = params.get("indicatorId");
            if (indicatorId == null || indicatorId.trim().isEmpty()) {
                return JsonMapper.buildFalseJson("指标ID不能为空");
            }
            TblIndicatorInfo indicator = indicatorService.getDetail(indicatorId);
            if (indicator == null) {
                return JsonMapper.buildFalseJson("指标不存在");
            }
            return JsonMapper.buildSuccessJson(indicator);
        } catch (Exception e) {
            log.error("查询指标详情失败", e);
            return JsonMapper.buildFalseJson("查询指标详情失败: " + e.getMessage());
        }
    }

    @PostMapping("/save")
    @Operation(summary = "保存指标")
    public String save(@RequestBody TblIndicatorInfo indicator) {
        try {
            if (UserUtils.getUser() == null) {
                return JsonMapper.buildFalseJson("用户未登录");
            }
            if (indicator.getIndicatorCode() == null || indicator.getIndicatorCode().trim().isEmpty()) {
                return JsonMapper.buildFalseJson("指标编码不能为空");
            }
            if (indicator.getIndicatorName() == null || indicator.getIndicatorName().trim().isEmpty()) {
                return JsonMapper.buildFalseJson("指标名称不能为空");
            }
            if (indicator.getIndicatorType() == null || indicator.getIndicatorType().trim().isEmpty()) {
                return JsonMapper.buildFalseJson("指标类型不能为空");
            }
            boolean result = indicatorService.save(indicator);
            if (result) {
                return JsonMapper.buildSuccessJson("保存成功");
            } else {
                return JsonMapper.buildFalseJson("保存失败");
            }
        } catch (Exception e) {
            log.error("保存指标失败", e);
            return JsonMapper.buildFalseJson("保存指标失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @Operation(summary = "删除指标")
    public String delete(@RequestBody Map<String, String> params) {
        try {
            if (UserUtils.getUser() == null) {
                return JsonMapper.buildFalseJson("用户未登录");
            }
            String indicatorId = params.get("indicatorId");
            if (indicatorId == null || indicatorId.trim().isEmpty()) {
                return JsonMapper.buildFalseJson("指标ID不能为空");
            }
            boolean result = indicatorService.delete(indicatorId);
            if (result) {
                return JsonMapper.buildSuccessJson("删除成功");
            } else {
                return JsonMapper.buildFalseJson("删除失败");
            }
        } catch (Exception e) {
            log.error("删除指标失败", e);
            return JsonMapper.buildFalseJson("删除指标失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除指标")
    public String batchDelete(@RequestBody List<String> indicatorIds) {
        try {
            if (UserUtils.getUser() == null) {
                return JsonMapper.buildFalseJson("用户未登录");
            }
            if (indicatorIds == null || indicatorIds.isEmpty()) {
                return JsonMapper.buildFalseJson("指标ID列表不能为空");
            }
            boolean result = indicatorService.batchDelete(indicatorIds);
            if (result) {
                return JsonMapper.buildSuccessJson("批量删除成功");
            } else {
                return JsonMapper.buildFalseJson("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除指标失败", e);
            return JsonMapper.buildFalseJson("批量删除指标失败: " + e.getMessage());
        }
    }

    @PostMapping("/updateStatus")
    @Operation(summary = "更新指标状态")
    public String updateStatus(@RequestBody Map<String, String> params) {
        try {
            if (UserUtils.getUser() == null) {
                return JsonMapper.buildFalseJson("用户未登录");
            }
            String indicatorId = params.get("indicatorId");
            String status = params.get("status");
            if (indicatorId == null || indicatorId.trim().isEmpty()) {
                return JsonMapper.buildFalseJson("指标ID不能为空");
            }
            if (status == null || status.trim().isEmpty()) {
                return JsonMapper.buildFalseJson("状态不能为空");
            }
            boolean result = indicatorService.updateStatus(indicatorId, status);
            if (result) {
                return JsonMapper.buildSuccessJson("更新状态成功");
            } else {
                return JsonMapper.buildFalseJson("更新状态失败");
            }
        } catch (Exception e) {
            log.error("更新指标状态失败", e);
            return JsonMapper.buildFalseJson("更新指标状态失败: " + e.getMessage());
        }
    }
}

