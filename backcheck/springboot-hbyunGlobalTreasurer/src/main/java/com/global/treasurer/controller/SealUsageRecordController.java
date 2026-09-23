package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.global.treasurer.entity.TblSealUsageRecord;
import com.global.treasurer.service.TblSealUsageRecordService;
import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 印鉴使用记录管理Controller
 * 匹配前端API路径: /qqsk/financial/basicConfig/sealUsageRecord/*
 * 基于 01_create_seal_usage_record.sql 表结构
 *
 * @author 华博云开发团队
 * @since 2026-01-27
 */
@RestController
@RequestMapping("/financial/basicConfig/sealUsageRecord")
@Api(tags = "印鉴使用记录管理")
public class SealUsageRecordController {
    private static final Logger log = LoggerFactory.getLogger(SealUsageRecordController.class);

    @Autowired
    private TblSealUsageRecordService tblSealUsageRecordService;

    /**
     * 分页查询印鉴使用记录列表
     */
    @PostMapping("/list")
    @ApiOperation("分页查询印鉴使用记录列表")
    public String getList(@FlexibleRequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            if (params == null) params = new HashMap<>();

            Integer pageNo = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) :
                              (params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) : 20);
            String recordNumber = params.get("recordNumber") != null ? params.get("recordNumber").toString() : null;
            String sealCode = params.get("sealCode") != null ? params.get("sealCode").toString() : null;
            String sealName = params.get("sealName") != null ? params.get("sealName").toString() : null;
            String operatorName = params.get("operatorName") != null ? params.get("operatorName").toString() : null;
            String businessType = params.get("businessType") != null ? params.get("businessType").toString() : null;
            String usageStatus = params.get("usageStatus") != null ? params.get("usageStatus").toString() : null;

            // 使用PageHelper分页（避免与MyBatis-Plus分页冲突）
            com.github.pagehelper.PageHelper.startPage(pageNo, pageSize);

            // 构建查询条件
            QueryWrapper<TblSealUsageRecord> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(recordNumber)) {
                queryWrapper.like("RECORD_NUMBER", recordNumber);
            }
            if (StringUtils.hasText(sealCode)) {
                queryWrapper.like("SEAL_CODE", sealCode);
            }
            if (StringUtils.hasText(sealName)) {
                queryWrapper.like("SEAL_NAME", sealName);
            }
            if (StringUtils.hasText(operatorName)) {
                queryWrapper.like("OPERATOR_NAME", operatorName);
            }
            if (StringUtils.hasText(businessType)) {
                queryWrapper.eq("BUSINESS_TYPE", businessType);
            }
            if (StringUtils.hasText(usageStatus)) {
                queryWrapper.eq("USAGE_STATUS", usageStatus);
            }
            queryWrapper.orderByDesc("CREATE_TIME");

            List<TblSealUsageRecord> list = tblSealUsageRecordService.list(queryWrapper);
            com.github.pagehelper.PageInfo<TblSealUsageRecord> pageInfo = new com.github.pagehelper.PageInfo<>(list);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());

            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("获取印鉴使用记录列表失败", e);
            return JsonBean.error("获取印鉴使用记录列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取印鉴使用记录详情
     */
    @GetMapping("/detail")
    @ApiOperation("获取印鉴使用记录详情")
    public String getDetail(@RequestParam String id, HttpServletResponse response) {
        try {
            Long recordId = Long.parseLong(id);
            TblSealUsageRecord record = tblSealUsageRecordService.getByRecordId(recordId);
            if (record == null) {
                return JsonBean.error("印鉴使用记录不存在");
            }
            return new JsonBean(1, "查询成功", record).toString();
        } catch (NumberFormatException e) {
            return JsonBean.error("记录ID格式错误");
        } catch (Exception e) {
            log.error("获取印鉴使用记录详情失败", e);
            return JsonBean.error("获取印鉴使用记录详情失败: " + e.getMessage());
        }
    }

    /**
     * 获取印鉴使用统计
     */
    @GetMapping("/statistics")
    @ApiOperation("获取印鉴使用统计")
    public String getStatistics(HttpServletResponse response) {
        try {
            Map<String, Object> statistics = tblSealUsageRecordService.getStatistics();
            return new JsonBean(1, "查询成功", statistics).toString();
        } catch (Exception e) {
            log.error("获取印鉴使用统计失败", e);
            return JsonBean.error("获取印鉴使用统计失败: " + e.getMessage());
        }
    }

    /**
     * 新增印鉴使用记录
     */
    @PostMapping("/save")
    @ApiOperation("新增印鉴使用记录")
    public String save(@FlexibleRequestBody TblSealUsageRecord record, HttpServletResponse response) {
        try {
            // 参数校验 - sealCode 或 sealName 至少有一个
            if (!StringUtils.hasText(record.getSealCode()) && !StringUtils.hasText(record.getSealName())) {
                return JsonBean.error("印鉴编码或印鉴名称不能为空");
            }
            if (!StringUtils.hasText(record.getOperatorName())) {
                return JsonBean.error("使用人员不能为空");
            }

            boolean result = tblSealUsageRecordService.saveRecord(record);
            if (result) {
                return new JsonBean(1, "保存成功", record).toString();
            } else {
                return JsonBean.error("保存失败");
            }
        } catch (Exception e) {
            log.error("新增印鉴使用记录失败", e);
            return JsonBean.error("新增印鉴使用记录失败: " + e.getMessage());
        }
    }

    /**
     * 更新印鉴使用记录
     */
    @PostMapping("/update")
    @ApiOperation("更新印鉴使用记录")
    public String update(@FlexibleRequestBody TblSealUsageRecord record, HttpServletResponse response) {
        try {
            if (record.getId() == null) {
                return JsonBean.error("记录ID不能为空");
            }

            boolean result = tblSealUsageRecordService.updateRecord(record);
            if (result) {
                return new JsonBean(1, "更新成功", record).toString();
            } else {
                return JsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新印鉴使用记录失败", e);
            return JsonBean.error("更新印鉴使用记录失败: " + e.getMessage());
        }
    }

    /**
     * 删除印鉴使用记录
     */
    @PostMapping("/delete")
    @ApiOperation("删除印鉴使用记录")
    public String delete(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            Object idObj = params.get("id");
            if (idObj == null) {
                return JsonBean.error("记录ID不能为空");
            }
            Long id = Long.parseLong(idObj.toString());

            boolean result = tblSealUsageRecordService.deleteByRecordId(id);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (NumberFormatException e) {
            return JsonBean.error("记录ID格式错误");
        } catch (Exception e) {
            log.error("删除印鉴使用记录失败", e);
            return JsonBean.error("删除印鉴使用记录失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除印鉴使用记录
     */
    @PostMapping("/deleteBatch")
    @ApiOperation("批量删除印鉴使用记录")
    public String deleteBatch(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            @SuppressWarnings("unchecked")
            List<Object> idObjs = (List<Object>) params.get("ids");
            if (idObjs == null || idObjs.isEmpty()) {
                return JsonBean.error("记录ID列表不能为空");
            }

            List<Long> ids = new ArrayList<>();
            for (Object idObj : idObjs) {
                ids.add(Long.parseLong(idObj.toString()));
            }

            boolean result = tblSealUsageRecordService.deleteBatchByIds(ids);
            if (result) {
                return JsonBean.success("批量删除成功");
            } else {
                return JsonBean.error("批量删除失败");
            }
        } catch (NumberFormatException e) {
            return JsonBean.error("记录ID格式错误");
        } catch (Exception e) {
            log.error("批量删除印鉴使用记录失败", e);
            return JsonBean.error("批量删除印鉴使用记录失败: " + e.getMessage());
        }
    }

    /**
     * 审批印鉴使用记录
     */
    @PostMapping("/approve")
    @ApiOperation("审批印鉴使用记录")
    public String approve(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            Object idObj = params.get("id");
            if (idObj == null) {
                idObj = params.get("recordId");
            }
            if (idObj == null) {
                return JsonBean.error("记录ID不能为空");
            }
            Long id = Long.parseLong(idObj.toString());

            String approver = params.get("approver") != null ? params.get("approver").toString() : null;
            String status = params.get("status") != null ? params.get("status").toString() : "APPROVED";

            boolean result = tblSealUsageRecordService.approveRecord(id, approver, status);
            if (result) {
                return JsonBean.success("审批成功");
            } else {
                return JsonBean.error("审批失败");
            }
        } catch (NumberFormatException e) {
            return JsonBean.error("记录ID格式错误");
        } catch (Exception e) {
            log.error("审批印鉴使用记录失败", e);
            return JsonBean.error("审批印鉴使用记录失败: " + e.getMessage());
        }
    }

    /**
     * 导出印鉴使用记录
     */
    @GetMapping("/export")
    @ApiOperation("导出印鉴使用记录")
    public String export(@RequestParam(required = false) String sealCode,
                        @RequestParam(required = false) String sealName,
                        @RequestParam(required = false) String operatorName,
                        @RequestParam(required = false) String businessType,
                        @RequestParam(required = false) String usageStatus,
                        HttpServletResponse response) {
        try {
            QueryWrapper<TblSealUsageRecord> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(sealCode)) queryWrapper.like("SEAL_CODE", sealCode);
            if (StringUtils.hasText(sealName)) queryWrapper.like("SEAL_NAME", sealName);
            if (StringUtils.hasText(operatorName)) queryWrapper.like("OPERATOR_NAME", operatorName);
            if (StringUtils.hasText(businessType)) queryWrapper.eq("BUSINESS_TYPE", businessType);
            if (StringUtils.hasText(usageStatus)) queryWrapper.eq("USAGE_STATUS", usageStatus);
            queryWrapper.orderByDesc("CREATE_TIME");

            List<TblSealUsageRecord> list = tblSealUsageRecordService.list(queryWrapper);

            Map<String, Object> data = new HashMap<>();
            data.put("exportList", list);
            data.put("total", list.size());
            data.put("exportTime", new Date());

            return new JsonBean(1, "导出成功", data).toString();
        } catch (Exception e) {
            log.error("导出印鉴使用记录失败", e);
            return JsonBean.error("导出印鉴使用记录失败: " + e.getMessage());
        }
    }

    /**
     * 获取使用类型下拉选项
     */
    @GetMapping("/usageTypes")
    @ApiOperation("获取使用类型下拉选项")
    public String getUsageTypes(HttpServletResponse response) {
        try {
            List<Map<String, String>> types = new ArrayList<>();
            types.add(createOption("单次盖章", "单次盖章"));
            types.add(createOption("多次盖章", "多次盖章"));
            types.add(createOption("批量盖章", "批量盖章"));
            return new JsonBean(1, "查询成功", types).toString();
        } catch (Exception e) {
            log.error("获取使用类型失败", e);
            return JsonBean.error("获取使用类型失败: " + e.getMessage());
        }
    }

    /**
     * 获取业务类型下拉选项
     */
    @GetMapping("/businessTypes")
    @ApiOperation("获取业务类型下拉选项")
    public String getBusinessTypes(HttpServletResponse response) {
        try {
            List<Map<String, String>> types = new ArrayList<>();
            types.add(createOption("合同签署", "合同签署"));
            types.add(createOption("财务报销", "财务报销"));
            types.add(createOption("行政管理", "行政管理"));
            types.add(createOption("人事管理", "人事管理"));
            types.add(createOption("其他", "其他"));
            return new JsonBean(1, "查询成功", types).toString();
        } catch (Exception e) {
            log.error("获取业务类型失败", e);
            return JsonBean.error("获取业务类型失败: " + e.getMessage());
        }
    }

    /**
     * 获取使用状态下拉选项
     */
    @GetMapping("/statusOptions")
    @ApiOperation("获取使用状态下拉选项")
    public String getStatusOptions(HttpServletResponse response) {
        try {
            List<Map<String, String>> options = new ArrayList<>();
            options.add(createOption("SUCCESS", "成功"));
            options.add(createOption("FAILED", "失败"));
            options.add(createOption("PENDING", "待审批"));
            return new JsonBean(1, "查询成功", options).toString();
        } catch (Exception e) {
            log.error("获取状态选项失败", e);
            return JsonBean.error("获取状态选项失败: " + e.getMessage());
        }
    }

    private Map<String, String> createOption(String value, String label) {
        Map<String, String> option = new HashMap<>();
        option.put("value", value);
        option.put("label", label);
        return option;
    }
}

