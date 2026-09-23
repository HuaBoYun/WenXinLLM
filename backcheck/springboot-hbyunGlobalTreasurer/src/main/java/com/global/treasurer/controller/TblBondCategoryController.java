package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblBondCategory;
import com.global.treasurer.service.TblBondCategoryService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 债券类别管理Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
@Controller
@RequestMapping({"/rzgl/bond-category", "/financial/rzgl/bond-category", "/centralaudit/financial/rzgl/bond-category"})
@Api(tags = "债券类别管理")
public class TblBondCategoryController {
    private static final Logger log = LoggerFactory.getLogger(TblBondCategoryController.class);

    @Resource
    private TblBondCategoryService tblBondCategoryService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询债券类别列表
     */
    @PostMapping("/page")
    @ResponseBody
    @ApiOperation("分页查询债券类别列表")
    public String getBondCategoryPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String categoryCode,
            @RequestParam(required = false) String status,
            @RequestHeader(value = "token", required = false) String token,
            HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 调用Service的分页方法，使用PageHelper
            PageInfo<TblBondCategory> pageInfo = tblBondCategoryService.getBondCategoryPage(
                    pageNum, pageSize, categoryName, categoryCode, status);

            // 构建前端期望的响应格式
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("rows", pageInfo.getList());
            resultMap.put("total", pageInfo.getTotal());

            return JsonBean.success(resultMap);
        } catch (Exception e) {
            log.error("查询债券类别列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }
}
