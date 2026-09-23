package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TblUkeyVendor;
import com.global.treasurer.service.TblUkeyVendorService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * UKey厂商管理Controller
 * 匹配前端API路径: /qqsk/financial/basicConfig/ukeyVendor/*
 *
 * @author 华博云开发团队
 * @since 2026-01-27
 */
@RestController
@RequestMapping("/financial/basicConfig/ukeyVendor")
@Api(tags = "UKey厂商管理")
public class UkeyVendorController {
    private static final Logger log = LoggerFactory.getLogger(UkeyVendorController.class);

    @Autowired
    private TblUkeyVendorService tblUkeyVendorService;

    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("分页查询UKey厂商列表")
    public String getList(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit,
                          @RequestParam(required = false) String vendorName, @RequestParam(required = false) String vendorCode,
                          @RequestParam(required = false) String vendorType,
                          @RequestParam(required = false) String certificationStatus,
                          @RequestParam(required = false) String cooperationStatus,
                          @RequestParam(required = false) Integer status, HttpServletResponse response) {
        try {
            int pageNo = page != null ? page : 1;
            int pageSize = limit != null ? limit : 20;

            // 使用PageHelper分页（避免与MyBatis-Plus分页拦截器冲突）
            com.github.pagehelper.PageHelper.startPage(pageNo, pageSize);

            QueryWrapper<TblUkeyVendor> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(vendorName)) queryWrapper.like("VENDOR_NAME", vendorName);
            if (StringUtils.hasText(vendorCode)) queryWrapper.like("VENDOR_CODE", vendorCode);
            if (StringUtils.hasText(vendorType)) queryWrapper.eq("VENDOR_TYPE", vendorType);
            if (StringUtils.hasText(certificationStatus)) queryWrapper.eq("CERTIFICATION_STATUS", certificationStatus);
            if (StringUtils.hasText(cooperationStatus)) queryWrapper.eq("COOPERATION_STATUS", cooperationStatus);
            if (status != null) queryWrapper.eq("STATUS", status);
            // 不加orderBy，Mapper XML中已有ORDER BY CREATE_TIME DESC

            List<TblUkeyVendor> list = tblUkeyVendorService.list(queryWrapper);
            com.github.pagehelper.PageInfo<TblUkeyVendor> pageInfo = new com.github.pagehelper.PageInfo<>(list);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());

            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("获取UKey厂商列表失败", e);
            return JsonBean.error("获取UKey厂商列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail")
    @ApiOperation("获取UKey厂商详情")
    public String getDetail(@RequestParam Long id, HttpServletResponse response) {
        try {
            TblUkeyVendor vendor = tblUkeyVendorService.getById(id);
            if (vendor == null) return JsonBean.error("UKey厂商不存在");
            return new JsonBean(1, "查询成功", vendor).toString();
        } catch (Exception e) {
            log.error("获取UKey厂商详情失败", e);
            return JsonBean.error("获取UKey厂商详情失败: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    @ApiOperation("新增UKey厂商")
    public String add(@FlexibleRequestBody TblUkeyVendor vendor, HttpServletResponse response) {
        try {
            // 校验厂商编码是否为空
            if (!StringUtils.hasText(vendor.getVendorCode())) {
                return JsonBean.error("厂商编码不能为空");
            }

            // 检查厂商编码是否已存在
            QueryWrapper<TblUkeyVendor> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("VENDOR_CODE", vendor.getVendorCode());
            if (tblUkeyVendorService.count(queryWrapper) > 0) {
                return JsonBean.error("厂商编码已存在，请使用其他编码");
            }

            // 生成ID（如果没有提供）
            if (vendor.getId() == null) {
                vendor.setId(System.currentTimeMillis() + new java.util.Random().nextInt(1000));
            }

            vendor.setCreateTime(new Date());
            vendor.setUpdateTime(new Date());
            boolean success = tblUkeyVendorService.save(vendor);
            return success ? JsonBean.success("创建成功") : JsonBean.error("创建失败");
        } catch (Exception e) {
            log.error("创建UKey厂商失败", e);
            return JsonBean.error("创建UKey厂商失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation("更新UKey厂商")
    public String update(@FlexibleRequestBody TblUkeyVendor vendor, HttpServletResponse response) {
        try {
            if (vendor.getId() == null) return JsonBean.error("厂商ID不能为空");
            vendor.setUpdateTime(new Date());
            boolean success = tblUkeyVendorService.updateById(vendor);
            return success ? JsonBean.success("更新成功") : JsonBean.error("更新失败");
        } catch (Exception e) {
            log.error("更新UKey厂商失败", e);
            return JsonBean.error("更新UKey厂商失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation("删除UKey厂商")
    public String delete(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            Long id = params.get("id") != null ? Long.parseLong(params.get("id").toString()) : null;
            if (id == null) return JsonBean.error("ID不能为空");
            boolean success = tblUkeyVendorService.removeById(id);
            return success ? JsonBean.success("删除成功") : JsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除UKey厂商失败", e);
            return JsonBean.error("删除UKey厂商失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("获取UKey厂商统计")
    public String getStatistics(HttpServletResponse response) {
        try {
            // 使用Service层的getStatistics方法（基于jdbcTemplate原生SQL，避免MyBatis-Plus count()在达梦数据库上的兼容性问题）
            Map<String, Object> statistics = tblUkeyVendorService.getStatistics();
            return new JsonBean(1, "查询成功", statistics).toString();
        } catch (Exception e) {
            log.error("获取UKey厂商统计失败", e);
            Map<String, Object> defaultStats = new HashMap<>();
            defaultStats.put("totalVendors", 0);
            defaultStats.put("activeVendors", 0);
            defaultStats.put("certifiedVendors", 0);
            defaultStats.put("productModels", 0);
            return new JsonBean(1, "查询成功", defaultStats).toString();
        }
    }

    @PostMapping("/certify")
    @ApiOperation("认证UKey厂商")
    public String certify(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            Long id = params.get("id") != null ? Long.parseLong(params.get("id").toString()) : null;
            if (id == null) return JsonBean.error("ID不能为空");
            TblUkeyVendor vendor = new TblUkeyVendor();
            vendor.setId(id);
            vendor.setCertificationStatus("CERTIFIED");
            vendor.setUpdateTime(new Date());
            boolean success = tblUkeyVendorService.updateById(vendor);
            return success ? JsonBean.success("认证成功") : JsonBean.error("认证失败");
        } catch (Exception e) {
            log.error("认证UKey厂商失败", e);
            return JsonBean.error("认证UKey厂商失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchCertify")
    @ApiOperation("批量认证UKey厂商")
    public String batchCertify(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            @SuppressWarnings("unchecked")
            java.util.List<Long> ids = (java.util.List<Long>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("请选择要认证的厂商");
            }
            int successCount = 0;
            for (Long id : ids) {
                TblUkeyVendor vendor = new TblUkeyVendor();
                vendor.setId(id);
                vendor.setCertificationStatus("CERTIFIED");
                vendor.setUpdateTime(new Date());
                if (tblUkeyVendorService.updateById(vendor)) {
                    successCount++;
                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put("successCount", successCount);
            result.put("failCount", ids.size() - successCount);
            return new JsonBean(1, "批量认证完成", result).toString();
        } catch (Exception e) {
            log.error("批量认证UKey厂商失败", e);
            return JsonBean.error("批量认证UKey厂商失败: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    @ApiOperation("导出UKey厂商")
    public String export(@RequestParam(required = false) String vendorName,
                        @RequestParam(required = false) String vendorCode,
                        @RequestParam(required = false) Integer status,
                        HttpServletResponse response) {
        try {
            QueryWrapper<TblUkeyVendor> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(vendorName)) queryWrapper.like("VENDOR_NAME", vendorName);
            if (StringUtils.hasText(vendorCode)) queryWrapper.like("VENDOR_CODE", vendorCode);
            if (status != null) queryWrapper.eq("STATUS", status);
            queryWrapper.orderByDesc("CREATE_TIME");

            java.util.List<TblUkeyVendor> list = tblUkeyVendorService.list(queryWrapper);

            Map<String, Object> data = new HashMap<>();
            data.put("exportList", list);
            data.put("total", list.size());
            data.put("exportTime", new Date());

            return new JsonBean(1, "导出成功", data).toString();
        } catch (Exception e) {
            log.error("导出UKey厂商失败", e);
            return JsonBean.error("导出UKey厂商失败: " + e.getMessage());
        }
    }
}

