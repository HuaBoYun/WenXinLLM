package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblCreditCategory;
import com.global.treasurer.service.TblCreditCategoryService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 授信类别管理Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
@Controller
@RequestMapping({"/financing/credit-category", "/rzgl/credit-category", "/financial/rzgl/credit-category", "/centralaudit/financial/rzgl/credit-category"})
@Api(tags = "授信类别管理")
public class TblCreditCategoryController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TblCreditCategoryController.class);

    @Resource
    private TblCreditCategoryService tblCreditCategoryService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询授信类别列表
     */
    @PostMapping("/page")
    @ResponseBody
    @ApiOperation("分页查询授信类别列表")
    public String getCreditCategoryPage(@RequestParam Map<String, Object> params) {
        try {
            Integer pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            String categoryName = (String) params.get("categoryName");
            String categoryCode = (String) params.get("categoryCode");
            String status = params.get("status") != null ? params.get("status").toString() : null;

            // 调用Service的分页方法，使用PageHelper
            PageInfo<TblCreditCategory> pageInfo = tblCreditCategoryService.getCreditCategoryPage(
                    pageNum, pageSize, categoryName, categoryCode, status);

            // 构建前端期望的响应格式
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("rows", pageInfo.getList());
            resultMap.put("total", pageInfo.getTotal());

            return JsonBean.success(resultMap);
        } catch (Exception e) {
            log.error("查询授信类别列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询授信类别详情
     */
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation("根据ID查询授信类别详情")
    public String getCreditCategoryDetail(@PathVariable Long id) {
        try {
            log.info("查询授信类别详情, id: {}", id);
            TblCreditCategory category = tblCreditCategoryService.getById(id);
            return JsonBean.success(category);
        } catch (Exception e) {
            log.error("查询授信类别详情失败, id: {}", id, e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增授信类别
     */
    @PostMapping("/add")
    @ResponseBody
    @ApiOperation("新增授信类别")
    public String addCreditCategory(@FlexibleRequestBody TblCreditCategory category) {
        try {
            log.info("新增授信类别, category: {}", category);

            // 设置默认值
            Date now = new Date();
            category.setCreatedTime(now);
            category.setUpdatedTime(now);

            if (category.getParentId() == null) {
                category.setParentId(0L);
            }
            if (category.getCategoryLevel() == null) {
                category.setCategoryLevel(1);
            }
            if (category.getSortOrder() == null) {
                category.setSortOrder(0);
            }
            if (category.getDeleteFlag() == null) {
                category.setDeleteFlag(0);
            }
            if (category.getStatus() == null) {
                category.setStatus("1");
            }

            tblCreditCategoryService.save(category);

            log.info("新增成功，ID: {}", category.getCategoryId());
            return JsonBean.success(category);
        } catch (Exception e) {
            log.error("新增授信类别失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新授信类别
     */
    @PutMapping("/update")
    @ResponseBody
    @ApiOperation("更新授信类别")
    public String updateCreditCategory(@FlexibleRequestBody TblCreditCategory category) {
        try {
            log.info("更新授信类别, category: {}", category);
            category.setUpdatedTime(new Date());
            tblCreditCategoryService.updateById(category);
            return JsonBean.success(category);
        } catch (Exception e) {
            log.error("更新授信类别失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除授信类别
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation("删除授信类别")
    public String deleteCreditCategory(@PathVariable Long id) {
        try {
            log.info("删除授信类别, id: {}", id);
            tblCreditCategoryService.removeById(id);
            return JsonBean.success("删除成功");
        } catch (Exception e) {
            log.error("删除授信类别失败, id: {}", id, e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 切换授信类别状态
     */
    @PutMapping("/toggle-status")
    @ResponseBody
    @ApiOperation("切换授信类别状态")
    public String toggleCreditCategoryStatus(@RequestParam Map<String, Object> params) {
        try {
            Long id = Long.parseLong(params.get("id").toString());
            String status = params.get("status").toString();
            log.info("切换授信类别状态, id: {}, status: {}", id, status);

            TblCreditCategory category = tblCreditCategoryService.getById(id);
            if (category != null) {
                category.setStatus(status);
                category.setUpdatedTime(new Date());
                tblCreditCategoryService.updateById(category);
            }

            return JsonBean.success("状态切换成功");
        } catch (Exception e) {
            log.error("切换授信类别状态失败", e);
            return new JsonBean(0, "状态切换失败: " + e.getMessage(), null).toJson();
        }
    }
}
