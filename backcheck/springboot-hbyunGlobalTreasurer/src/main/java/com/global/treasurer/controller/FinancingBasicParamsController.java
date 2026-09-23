package com.global.treasurer.controller;

import com.hbfk.util.JsonBean;
import com.global.treasurer.entity.TblFinancingBasicParams;
import com.global.treasurer.service.FinancingBasicParamsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 融资基础参数管理Controller
 *
 * @author 华博云开发团队
 * @since 2026-02-04
 */
@RestController
@RequestMapping("/financing/basic-params")
@Api(tags = "融资基础参数管理")
public class FinancingBasicParamsController {

    private static final Logger log = LoggerFactory.getLogger(FinancingBasicParamsController.class);

    @Autowired
    private FinancingBasicParamsService financingBasicParamsService;

    @PostMapping("/page")
    @ApiOperation("融资基础参数分页查询")
    public String page(@RequestParam Map<String, Object> params) {
        try {
            log.info("融资基础参数分页查询, params: {}", params);
            
            Integer pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            String paramName = (String) params.get("paramName");
            String paramType = (String) params.get("paramType");
            String isEnabled = params.get("isEnabled") != null ? params.get("isEnabled").toString() : null;
            
            PageHelper.startPage(pageNum, pageSize);
            List<TblFinancingBasicParams> list = financingBasicParamsService.getParamsList(paramName, paramType, isEnabled);
            PageInfo<TblFinancingBasicParams> pageInfo = new PageInfo<>(list);
            
            Map<String, Object> data = new HashMap<>();
            data.put("rows", pageInfo.getList());
            data.put("total", pageInfo.getTotal());
            
            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("融资基础参数分页查询失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("融资基础参数详情查询")
    public String detail(@ApiParam("参数ID") @PathVariable Long id) {
        try {
            log.info("查询融资基础参数详情, id: {}", id);
            TblFinancingBasicParams params = financingBasicParamsService.getParamsById(id);
            // 将数据库的STATUS("ENABLE"/"DISABLE")转换为前端的isEnabled("1"/"0")
            if (params.getStatus() != null) {
                String isEnabled = "ENABLE".equalsIgnoreCase(params.getStatus()) ? "1" : "0";
                params.setStatus(isEnabled);
            }
            return new JsonBean(1, "查询成功", params).toString();
        } catch (Exception e) {
            log.error("查询融资基础参数详情失败, id: {}", id, e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/add")
    @ApiOperation("新增融资基础参数")
    public String add(@FlexibleRequestBody TblFinancingBasicParams params) {
        try {
            log.info("新增融资基础参数, params: {}", params);

            // 设置默认值
            params.setCreateTime(new Date());
            params.setUpdateTime(new Date());

            // 设置组织ID（重要：否则数据会被租户过滤拦截）
            if (params.getOrgId() == null) {
                params.setOrgId(1L); // 默认组织ID为1
            }

            // 设置创建人（重要：否则数据可能被用户过滤拦截）
            if (params.getCreateUser() == null) {
                params.setCreateUser(1L); // 默认创建人ID为1
            }

            // 将前端的isEnabled("1"/"0")转换为数据库的STATUS("ENABLE"/"DISABLE")
            if (params.getStatus() != null) {
                String status = "1".equals(params.getStatus()) ? "ENABLE" : "DISABLE";
                params.setStatus(status);
            } else {
                params.setStatus("ENABLE"); // 默认启用
            }

            // 设置默认参数类型（如果前端未传递）
            if (params.getParamType() == null || params.getParamType().trim().isEmpty()) {
                params.setParamType("ACCRUAL_CYCLE"); // 默认为预提周期
            }

            log.info("保存前的完整数据: {}", params);
            financingBasicParamsService.saveParams(params);
            log.info("保存后的ID: {}", params.getParamId());

            return new JsonBean(1, "新增成功", params).toString();
        } catch (Exception e) {
            log.error("新增融资基础参数失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage()).toString();
        }
    }

    @PutMapping("/update")
    @ApiOperation("更新融资基础参数")
    public String update(@FlexibleRequestBody TblFinancingBasicParams params) {
        try {
            log.info("更新融资基础参数, params: {}", params);
            params.setUpdateTime(new Date());
            financingBasicParamsService.updateParams(params);
            return new JsonBean(1, "更新成功", params).toString();
        } catch (Exception e) {
            log.error("更新融资基础参数失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage()).toString();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除融资基础参数")
    public String delete(@ApiParam("参数ID") @PathVariable Long id) {
        try {
            log.info("删除融资基础参数, id: {}", id);
            financingBasicParamsService.deleteParams(id);
            return new JsonBean(1, "删除成功").toString();
        } catch (Exception e) {
            log.error("删除融资基础参数失败, id: {}", id, e);
            return new JsonBean(0, "删除失败: " + e.getMessage()).toString();
        }
    }

    @PutMapping("/toggle-status")
    @ApiOperation("切换融资基础参数状态")
    public String toggleStatus(@RequestParam Map<String, Object> params) {
        try {
            Long id = Long.parseLong(params.get("id").toString());
            String isEnabled = params.get("isEnabled").toString();
            log.info("切换融资基础参数状态, id: {}, isEnabled: {}", id, isEnabled);

            TblFinancingBasicParams param = financingBasicParamsService.getParamsById(id);
            // 将前端的isEnabled("1"/"0")转换为数据库的STATUS("ENABLE"/"DISABLE")
            String status = "1".equals(isEnabled) ? "ENABLE" : "DISABLE";
            param.setStatus(status);
            param.setUpdateTime(new Date());
            financingBasicParamsService.updateParams(param);

            return new JsonBean(1, "状态切换成功").toString();
        } catch (Exception e) {
            log.error("切换融资基础参数状态失败", e);
            return new JsonBean(0, "状态切换失败: " + e.getMessage()).toString();
        }
    }
}

