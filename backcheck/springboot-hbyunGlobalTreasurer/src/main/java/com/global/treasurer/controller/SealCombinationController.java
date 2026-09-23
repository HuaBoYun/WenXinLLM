package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.global.treasurer.entity.TcSealCombination;
import com.global.treasurer.service.TcSealCombinationService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
// import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // 已移除,使用手动声明
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 印鉴组合配置Controller
 * 匹配前端API路径: /zbgl/financial/xjgl/basicConfig/seal/*
 *
 * @author 华博云开发团队
 * @since 2024-12-23
 */
// // 已移除,使用手动声明
@RestController
@RequestMapping({"/qqsk/financial/basicConfig/sealCombination", "/financial/xjgl/seal/combination"})
@Api(tags = "印鉴组合配置")
public class SealCombinationController {

    private static final Logger log = LoggerFactory.getLogger(SealCombinationController.class);


    @Resource
    private TcSealCombinationService sealCombinationService;

    @Resource
    private UserProvider userProvider;

    @PostMapping({"/combinationList", "/list"})
    @ResponseBody
    @ApiOperation("获取印鉴组合列表(分页)")
    public String getSealCombinationListPost(
            @RequestBody(required = false) Map<String, Object> params,
            HttpServletResponse response) {
        try {
            // 权限验证
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }

            // 从请求体中获取参数
            Integer page = params != null && params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
            Integer limit = params != null && params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) : 20;
            String combinationName = params != null ? (String) params.get("combinationName") : null;
            String combinationCode = params != null ? (String) params.get("combinationCode") : null;
            Integer status = params != null && params.get("status") != null ? Integer.parseInt(params.get("status").toString()) : null;

            // 使用PageHelper进行分页,避免MyBatis-Plus分页与达梦数据库的兼容性问题
            PageHelper.startPage(page, limit);

            // 构建查询条件
            QueryWrapper<TcSealCombination> queryWrapper = new QueryWrapper<>();

            if (StringUtils.hasText(combinationName)) {
                queryWrapper.like("COMBINATION_NAME", combinationName);
            }
            if (StringUtils.hasText(combinationCode)) {
                queryWrapper.like("COMBINATION_CODE", combinationCode);
            }
            if (status != null) {
                queryWrapper.eq("IS_ENABLED", status);
            }

            queryWrapper.orderByDesc("CREATE_TIME");

            // 查询列表
            List<TcSealCombination> list = sealCombinationService.list(queryWrapper);

            // 获取分页信息
            PageInfo<TcSealCombination> pageInfo = new PageInfo<>(list);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());

            return new JsonBean(1, "查询成功", data).toJson();

        } catch (Exception e) {
            log.error("获取印鉴组合列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping({"/createCombination", "/create", "/add"})
    @ResponseBody
    @ApiOperation("创建印鉴组合")
    public String createSealCombination(@RequestBody Map<String, Object> params,
                                       HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }

            TcSealCombination sealCombination = new TcSealCombination();
            // 主键ID由数据库自增生成，不需要手动设置
            // 前端字段映射
            String combinationName = params.get("combinationName") != null ? (String) params.get("combinationName") : (String) params.get("name");
            String combinationCode = params.get("combinationCode") != null ? (String) params.get("combinationCode") : (String) params.get("code");
            sealCombination.setCombinationName(combinationName);
            sealCombination.setCombinationCode(combinationCode);
            sealCombination.setCombinationType((String) params.get("combinationType"));
            sealCombination.setBusinessType((String) params.get("businessType"));
            sealCombination.setAuthorityLevel((String) params.get("authorityLevel"));
            // 处理 sealIds 数组转为逗号分隔字符串
            Object sealIds = params.get("sealIds");
            if (sealIds != null) {
                if (sealIds instanceof List) {
                    sealCombination.setSealList(String.join(",", ((List<?>) sealIds).stream().map(Object::toString).toArray(String[]::new)));
                } else {
                    sealCombination.setSealList(sealIds.toString());
                }
            }
            sealCombination.setDescription((String) params.get("description"));
            sealCombination.setStatus(params.get("isEnabled") != null ? Integer.parseInt(params.get("isEnabled").toString()) : 1);
            sealCombination.setUsageCount(0);
            sealCombination.setCreateBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);
            sealCombination.setCreateTime(new Date());
            sealCombination.setUpdateBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);
            sealCombination.setUpdateTime(new Date());

            boolean success = sealCombinationService.save(sealCombination);
            if (success) {
                return JsonBean.success("创建成功");
            } else {
                return JsonBean.error("创建失败");
            }

        } catch (Exception e) {
            log.error("创建印鉴组合失败", e);
            return JsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PostMapping({"/updateCombination", "/update"})
    @ResponseBody
    @ApiOperation("更新印鉴组合")
    public String updateSealCombination(@RequestBody Map<String, Object> params,
                                       HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }

            TcSealCombination sealCombination = new TcSealCombination();
            sealCombination.setId(params.get("id") != null ? Long.parseLong(params.get("id").toString()) : null);
            sealCombination.setCombinationName((String) params.get("combinationName"));
            sealCombination.setCombinationCode((String) params.get("combinationCode"));
            sealCombination.setCombinationType((String) params.get("combinationType"));
            sealCombination.setBusinessType((String) params.get("businessType"));
            sealCombination.setAuthorityLevel((String) params.get("authorityLevel"));
            // 处理 sealIds 数组
            Object sealIds = params.get("sealIds");
            if (sealIds != null) {
                if (sealIds instanceof List) {
                    sealCombination.setSealList(String.join(",", ((List<?>) sealIds).stream().map(Object::toString).toArray(String[]::new)));
                } else {
                    sealCombination.setSealList(sealIds.toString());
                }
            } else if (params.get("sealList") != null) {
                sealCombination.setSealList((String) params.get("sealList"));
            }
            sealCombination.setDescription((String) params.get("description"));
            if (params.get("isEnabled") != null) {
                sealCombination.setStatus(Integer.parseInt(params.get("isEnabled").toString()));
            } else if (params.get("status") != null) {
                sealCombination.setStatus(Integer.parseInt(params.get("status").toString()));
            }
            sealCombination.setUpdateBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);
            sealCombination.setUpdateTime(new Date());

            boolean success = sealCombinationService.updateById(sealCombination);
            if (success) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }

        } catch (Exception e) {
            log.error("更新印鉴组合失败", e);
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @PostMapping({"/deleteCombination", "/delete"})
    @ResponseBody
    @ApiOperation("删除印鉴组合")
    public String deleteSealCombination(@RequestBody Map<String, Object> params,
                                       HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }

            Long id = Long.parseLong(params.get("id").toString());
            boolean success = sealCombinationService.removeById(id);
            if (success) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }

        } catch (Exception e) {
            log.error("删除印鉴组合失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping({"/updateCombinationStatus", "/updateStatus"})
    @ResponseBody
    @ApiOperation("更新印鉴组合状态")
    public String updateStatus(Map<String, Object> params,
                              HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }

            Long id = Long.parseLong(params.get("id").toString());
            Integer status = Integer.parseInt(params.get("status").toString());

            TcSealCombination sealCombination = new TcSealCombination();
            sealCombination.setId(id);
            sealCombination.setStatus(status);
            sealCombination.setUpdateBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);
            sealCombination.setUpdateTime(new Date());

            boolean success = sealCombinationService.updateById(sealCombination);
            if (success) {
                return JsonBean.success("状态更新成功");
            } else {
                return JsonBean.error("状态更新失败");
            }

        } catch (Exception e) {
            log.error("更新印鉴组合状态失败", e);
            return JsonBean.error("状态更新失败: " + e.getMessage());
        }
    }

    @GetMapping({"/statistics", "/stats"})
    @ResponseBody
    @ApiOperation("获取印鉴组合统计数据")
    public String getStatistics(HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }

            // 查询总组合数
            QueryWrapper<TcSealCombination> totalWrapper = new QueryWrapper<>();
            long totalCount = sealCombinationService.count(totalWrapper);

            // 查询启用的组合数
            QueryWrapper<TcSealCombination> activeWrapper = new QueryWrapper<>();
            activeWrapper.eq("STATUS", 1);
            long activeCount = sealCombinationService.count(activeWrapper);

            // TODO: 使用场景统计 - 需要业务表支持
            Long usageScenarios = 0L;

            // TODO: 今日使用次数 - 需要使用记录表支持
            Long todayUsage = 0L;

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalCombinations", totalCount);
            stats.put("activeCombinations", activeCount);
            stats.put("usageScenarios", usageScenarios);
            stats.put("todayUsage", todayUsage);

            return new JsonBean(1, "查询成功", stats).toJson();

        } catch (Exception e) {
            log.error("获取统计数据失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping({"/combinationDetail", "/detail"})
    @ResponseBody
    @ApiOperation("获取印鉴组合详情")
    public String getSealCombinationDetail(@RequestParam Long combinationId,
                                          HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }

            TcSealCombination sealCombination = sealCombinationService.getById(combinationId);
            if (sealCombination != null) {
                return new JsonBean(1, "查询成功", sealCombination).toJson();
            } else {
                return JsonBean.error("印鉴组合不存在");
            }

        } catch (Exception e) {
            log.error("获取印鉴组合详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/ruleConfig")
    @ResponseBody
    @ApiOperation("规则配置")
    public String configRule(Map<String, Object> params,
                            HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }

            // TODO: 实现规则配置逻辑
            // 1. 获取组合ID
            // 2. 获取规则配置参数
            // 3. 保存或更新规则配置
            log.info("规则配置请求,参数: {}", params);

            return JsonBean.success("规则配置成功");

        } catch (Exception e) {
            log.error("规则配置失败", e);
            return JsonBean.error("配置失败: " + e.getMessage());
        }
    }

    @GetMapping({"/exportCombination", "/export"})
    @ResponseBody
    @ApiOperation("导出印鉴组合配置")
    public String exportSealCombination(@RequestParam(required = false) String combinationName,
                                       @RequestParam(required = false) String combinationCode,
                                       @RequestParam(required = false) Integer status,
                                       HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }

            // TODO: 实现导出逻辑
            // 1. 查询符合条件的数据
            // 2. 生成Excel或CSV文件
            // 3. 返回文件下载链接
            log.info("导出印鉴组合配置,组合名称: {}, 组合编码: {}, 状态: {}",
                    combinationName, combinationCode, status);

            return JsonBean.success("导出成功");

        } catch (Exception e) {
            log.error("导出失败", e);
            return JsonBean.error("导出失败: " + e.getMessage());
        }
    }

    /**
     * 验证用户权限
     */
    private boolean validateUser(HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("获取用户信息异常", e);
            return false;
        }
    }
}
