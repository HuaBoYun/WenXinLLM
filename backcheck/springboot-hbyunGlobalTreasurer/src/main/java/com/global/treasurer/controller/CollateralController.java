package com.global.treasurer.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblCollateral;
import com.global.treasurer.mapper.CollateralMapper;
import com.global.treasurer.service.CollateralService;
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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 担保物管理Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
@Controller
@RequestMapping({"/rzgl/guarantee/collateral",
 "/financial/rzgl/guarantee/collateral"
})
@Api(tags = "担保物管理")
public class CollateralController {
    private static final Logger log = LoggerFactory.getLogger(CollateralController.class);

    @Resource
    private CollateralMapper collateralMapper;

    @Resource
    private CollateralService collateralService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询担保物列表（GET请求）
     */
    @GetMapping("/page")
    @ResponseBody
    @ApiOperation("分页查询担保物列表(GET)")
    public String getCollateralPageByGet(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String collateralNo,
            @RequestParam(required = false) String collateralName,
            @RequestParam(required = false) String collateralType,
            @RequestParam(required = false) String collateralStatus,
            @RequestParam(required = false) Long orgId,
            @RequestHeader(value = "token", required = false) String token,
            HttpServletResponse response) {
        return getCollateralPage(pageNum, pageSize, collateralNo, collateralName,
                collateralType, collateralStatus, orgId, token, response);
    }

    /**
     * 分页查询担保物列表（POST请求）
     */
    @PostMapping("/page")
    @ResponseBody
    @ApiOperation("分页查询担保物列表(POST)")
    public String getCollateralPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String collateralNo,
            @RequestParam(required = false) String collateralName,
            @RequestParam(required = false) String collateralType,
            @RequestParam(required = false) String collateralStatus,
            @RequestParam(required = false) Long orgId,
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

            // 使用PageHelper进行分页
            PageHelper.startPage(pageNum, pageSize);

            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            params.put("collateralNo", collateralNo);
            params.put("collateralName", collateralName);
            params.put("collateralType", collateralType);
            params.put("collateralStatus", collateralStatus);

            // 调用Mapper的条件查询方法
            List<TblCollateral> list = collateralMapper.selectCollateralList(params);

            // 返回分页结果
            PageInfo<TblCollateral> pageInfo = new PageInfo<>(list);

            // 构建前端期望的响应格式
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("records", pageInfo.getList());
            resultMap.put("total", pageInfo.getTotal());
            resultMap.put("current", pageNum);
            resultMap.put("size", pageSize);

            return JsonBean.success(resultMap);
        } catch (Exception e) {
            log.error("查询担保物列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取担保物概览数据
     */
    @GetMapping("/overview")
    @ResponseBody
    @ApiOperation("获取担保物概览数据")
    public String getCollateralOverview(
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

            // 获取担保物汇总数据
            Map<String, Object> summary = collateralService.getCollateralSummary(null);

            // 构建前端期望的概览数据格式
            Map<String, Object> overview = new HashMap<>();
            if (summary != null) {
                overview.put("totalCount", summary.getOrDefault("totalCount", 0));
                overview.put("totalValue", summary.getOrDefault("totalValue", BigDecimal.ZERO));
                overview.put("availableCount", summary.getOrDefault("availableCount", 0));
                overview.put("highRiskCount", summary.getOrDefault("highRiskCount", 0));
            } else {
                overview.put("totalCount", 0);
                overview.put("totalValue", BigDecimal.ZERO);
                overview.put("availableCount", 0);
                overview.put("highRiskCount", 0);
            }

            return JsonBean.success(overview);
        } catch (Exception e) {
            log.error("获取担保物概览数据失败", e);
            return new JsonBean(0, "获取概览数据失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询担保物详情
     */
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation("根据ID查询担保物详情")
    public String getCollateralById(@PathVariable Long id) {
        try {
            TblCollateral collateral = collateralMapper.selectCollateralById(id);
            if (collateral != null) {
                return JsonBean.success(collateral);
            } else {
                return new JsonBean(0, "未找到对应的担保物记录", null).toJson();
            }
        } catch (Exception e) {
            log.error("查询担保物详情失败, id: {}", id, e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增担保物
     */
    @PostMapping("")
    @ResponseBody
    @ApiOperation("新增担保物")
    public String addCollateral(@FlexibleRequestBody TblCollateral collateral) {
        try {
            log.info("新增担保物, collateral: {}", collateral);
            // 设置默认值
            if (collateral.getCollateralStatus() == null) {
                collateral.setCollateralStatus("ACTIVE");
            }
            collateral.setDeleteFlag(0);
            collateral.setCreatedTime(new java.util.Date());
            collateral.setUpdatedTime(new java.util.Date());
            // 生成担保物编号
            if (collateral.getCollateralNo() == null || collateral.getCollateralNo().isEmpty()) {
                collateral.setCollateralNo("COL" + System.currentTimeMillis());
            }
            collateralMapper.insert(collateral);
            return JsonBean.success("新增成功");
        } catch (Exception e) {
            log.error("新增担保物失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 修改担保物
     */
    @PutMapping("")
    @ResponseBody
    @ApiOperation("修改担保物")
    public String updateCollateral(@FlexibleRequestBody TblCollateral collateral) {
        try {
            log.info("修改担保物, collateral: {}", collateral);
            collateral.setUpdatedTime(new java.util.Date());
            collateralMapper.updateById(collateral);
            return JsonBean.success("修改成功");
        } catch (Exception e) {
            log.error("修改担保物失败", e);
            return new JsonBean(0, "修改失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除担保物
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation("删除担保物")
    public String deleteCollateral(@PathVariable Long id) {
        try {
            log.info("删除担保物, id: {}", id);
            // 逻辑删除
            TblCollateral collateral = collateralMapper.selectCollateralById(id);
            if (collateral != null) {
                collateral.setDeleteFlag(1);
                collateral.setUpdatedTime(new java.util.Date());
                collateralMapper.updateById(collateral);
                return JsonBean.success("删除成功");
            } else {
                return new JsonBean(0, "未找到对应的担保物记录", null).toJson();
            }
        } catch (Exception e) {
            log.error("删除担保物失败, id: {}", id, e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量删除担保物
     */
    @DeleteMapping("/batch")
    @ResponseBody
    @ApiOperation("批量删除担保物")
    public String batchDeleteCollateral(@RequestParam(value = "ids", required = false) List<Long> ids) {
        try {
            log.info("批量删除担保物, ids: {}", ids);
            collateralMapper.batchDeleteByIds(ids);
            return JsonBean.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除担保物失败, ids: {}", ids, e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 冻结担保物
     */
    @PostMapping("/{id}/freeze")
    @ResponseBody
    @ApiOperation("冻结担保物")
    public String freezeCollateral(@PathVariable Long id) {
        try {
            log.info("冻结担保物, id: {}", id);
            collateralMapper.updateCollateralStatus(id, "FROZEN");
            return JsonBean.success("冻结成功");
        } catch (Exception e) {
            log.error("冻结担保物失败, id: {}", id, e);
            return new JsonBean(0, "冻结失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 解冻担保物
     */
    @PostMapping("/{id}/unfreeze")
    @ResponseBody
    @ApiOperation("解冻担保物")
    public String unfreezeCollateral(@PathVariable Long id) {
        try {
            log.info("解冻担保物, id: {}", id);
            collateralMapper.updateCollateralStatus(id, "AVAILABLE");
            return JsonBean.success("解冻成功");
        } catch (Exception e) {
            log.error("解冻担保物失败, id: {}", id, e);
            return new JsonBean(0, "解冻失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 质押担保物
     */
    @PostMapping("/{id}/pledge")
    @ResponseBody
    @ApiOperation("质押担保物")
    public String pledgeCollateral(@PathVariable Long id) {
        try {
            log.info("质押担保物, id: {}", id);
            collateralMapper.updateCollateralStatus(id, "PLEDGED");
            return JsonBean.success("质押成功");
        } catch (Exception e) {
            log.error("质押担保物失败, id: {}", id, e);
            return new JsonBean(0, "质押失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 抵押担保物
     */
    @PostMapping("/{id}/mortgage")
    @ResponseBody
    @ApiOperation("抵押担保物")
    public String mortgageCollateral(@PathVariable Long id) {
        try {
            log.info("抵押担保物, id: {}", id);
            collateralMapper.updateCollateralStatus(id, "MORTGAGED");
            return JsonBean.success("抵押成功");
        } catch (Exception e) {
            log.error("抵押担保物失败, id: {}", id, e);
            return new JsonBean(0, "抵押失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 释放担保物
     */
    @PostMapping("/{id}/release")
    @ResponseBody
    @ApiOperation("释放担保物")
    public String releaseCollateral(@PathVariable Long id) {
        try {
            log.info("释放担保物, id: {}", id);
            collateralMapper.updateCollateralStatus(id, "AVAILABLE");
            return JsonBean.success("释放成功");
        } catch (Exception e) {
            log.error("释放担保物失败, id: {}", id, e);
            return new JsonBean(0, "释放失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 处置担保物
     */
    @PostMapping("/{id}/dispose")
    @ResponseBody
    @ApiOperation("处置担保物")
    public String disposeCollateral(@PathVariable Long id, @RequestParam(required = false) String disposeReason) {
        try {
            log.info("处置担保物, id: {}, reason: {}", id, disposeReason);
            collateralMapper.updateCollateralStatus(id, "DISPOSED");
            return JsonBean.success("处置成功");
        } catch (Exception e) {
            log.error("处置担保物失败, id: {}", id, e);
            return new JsonBean(0, "处置失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 查询可用担保物列表
     */
    @GetMapping("/available")
    @ResponseBody
    @ApiOperation("查询可用担保物列表")
    public String getAvailableCollaterals(@RequestParam(required = false) Long companyId) {
        try {
            List<TblCollateral> list = collateralMapper.selectAvailableCollaterals(companyId);
            return JsonBean.success(list);
        } catch (Exception e) {
            log.error("查询可用担保物列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }
}
