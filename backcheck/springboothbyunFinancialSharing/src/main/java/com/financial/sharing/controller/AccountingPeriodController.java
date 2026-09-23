package com.financial.sharing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.financial.sharing.dto.AccountingPeriodQueryParam;
import com.financial.sharing.dto.AccountingPeriodSaveParam;
import com.financial.sharing.oracle.entity.TblAccountingPeriod;
import com.financial.sharing.service.AccountingPeriodService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 会计期间管理控制器
 *
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Api(tags = "会计期间管理")
@RestController
@RequestMapping("/accounting-period")
@CrossOrigin
public class AccountingPeriodController {

    @Resource
    private UserProvider userProvider;

    @Resource
    private AccountingPeriodService accountingPeriodService;

    @ApiOperation("分页查询会计期间")
    @PostMapping("/getList")
    public String getAccountingPeriodList(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @RequestBody AccountingPeriodQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 设置查询参数
            param.setBookId(1L); // 默认账簿ID，需要根据实际业务调整
            param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());

            // 查询期间数据
            IPage<TblAccountingPeriod> pageResult = accountingPeriodService.getPeriodPage(param);

            // 转换为返回格式
            List<Map<String, Object>> periodList = new ArrayList<>();
            for (TblAccountingPeriod period : pageResult.getRecords()) {
                Map<String, Object> periodMap = convertPeriodToMap(period);
                periodList.add(periodMap);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", periodList);
            data.put("totalRecord", pageResult.getTotal());
            data.put("pageNo", pageResult.getCurrent());
            data.put("pageSize", pageResult.getSize());

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(data);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询会计期间列表失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存或更新会计期间")
    @PostMapping("/saveOrUpdate")
    public String saveOrUpdateAccountingPeriod(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @RequestBody AccountingPeriodSaveParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 设置账簿和租户信息
            param.setBookId(1L); // 默认账簿ID，需要根据实际业务调整
            param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());

            // 保存或更新期间
            TblAccountingPeriod period = accountingPeriodService.saveOrUpdatePeriod(param, loginStaff);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(convertPeriodToMap(period));
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("保存或更新会计期间失败", e);
            return createErrorResponse("操作失败: " + e.getMessage());
        }
    }

    @ApiOperation("开启会计期间")
    @PostMapping("/{periodId}/open")
    public String openAccountingPeriod(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @PathVariable Long periodId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 开启期间
            boolean result = accountingPeriodService.openPeriod(periodId, loginStaff);

            Map<String, Object> data = new HashMap<>();
            data.put("success", result);
            data.put("periodId", periodId);
            data.put("message", result ? "会计期间开启成功" : "会计期间开启失败");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(data);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("开启会计期间失败", e);
            return createErrorResponse("开启失败: " + e.getMessage());
        }
    }

    @ApiOperation("关闭会计期间")
    @PostMapping("/{periodId}/close")
    public String closeAccountingPeriod(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @PathVariable Long periodId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 关闭期间
            boolean result = accountingPeriodService.closePeriod(periodId, loginStaff);

            Map<String, Object> data = new HashMap<>();
            data.put("success", result);
            data.put("periodId", periodId);
            data.put("message", result ? "会计期间关闭成功" : "会计期间关闭失败");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(data);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("关闭会计期间失败", e);
            return createErrorResponse("关闭失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取当前会计期间")
    @GetMapping("/current")
    public String getCurrentAccountingPeriod(HttpServletRequest request,
                                           HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 获取当前期间
            TblAccountingPeriod currentPeriod = accountingPeriodService.getCurrentPeriod(
                1L, // 默认账簿ID，需要根据实际业务调整
                loginStaff.getCurrentOrg().getOrgid().longValue()
            );

            Map<String, Object> data;
            if (currentPeriod != null) {
                data = convertPeriodToMap(currentPeriod);
            } else {
                // 如果没有当前期间，返回当前月份的默认期间信息
                Calendar now = Calendar.getInstance();
                int currentYear = now.get(Calendar.YEAR);
                int currentMonth = now.get(Calendar.MONTH) + 1;

                data = new HashMap<>();
                data.put("yearNo", currentYear);
                data.put("monthNo", currentMonth);
                data.put("periodCode", currentYear + String.format("%02d", currentMonth));
                data.put("periodName", currentYear + "年" + currentMonth + "月");
                data.put("periodStatus", "CREATED");
                data.put("periodStatusName", "未创建");
                data.put("message", "当前会计期间尚未创建");
            }

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(data);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("获取当前会计期间失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取会计期间统计信息")
    @GetMapping("/statistics")
    public String getAccountingPeriodStatistics(HttpServletRequest request,
                                                HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 获取统计信息
            Map<String, Object> statistics = accountingPeriodService.getPeriodStatistics(
                1L, // 默认账簿ID，需要根据实际业务调整
                loginStaff.getCurrentOrg().getOrgid().longValue()
            );

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(statistics);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("获取会计期间统计信息失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量创建年度期间")
    @PostMapping("/batchCreate")
    public String batchCreateYearPeriods(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @RequestBody Map<String, Object> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            Integer yearNo = (Integer) param.get("yearNo");
            if (yearNo == null) {
                return createErrorResponse("年度不能为空");
            }

            // 批量创建年度期间
            List<TblAccountingPeriod> periods = accountingPeriodService.batchCreateYearPeriods(
                yearNo,
                1L, // 默认账簿ID，需要根据实际业务调整
                loginStaff.getCurrentOrg().getOrgid().longValue(),
                loginStaff
            );

            Map<String, Object> data = new HashMap<>();
            data.put("yearNo", yearNo);
            data.put("createdCount", periods.size());
            data.put("message", "成功创建 " + periods.size() + " 个会计期间");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(data);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("批量创建年度期间失败", e);
            return createErrorResponse("批量创建失败: " + e.getMessage());
        }
    }

    @ApiOperation("设置当前会计期间")
    @PostMapping("/{periodId}/setCurrent")
    public String setCurrentAccountingPeriod(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @PathVariable Long periodId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 设置当前期间
            boolean result = accountingPeriodService.setCurrentPeriod(periodId, loginStaff);

            Map<String, Object> data = new HashMap<>();
            data.put("success", result);
            data.put("periodId", periodId);
            data.put("message", result ? "设置当前期间成功" : "设置当前期间失败");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(data);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("设置当前会计期间失败", e);
            return createErrorResponse("设置失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取期间状态列表")
    @GetMapping("/statusList")
    public String getPeriodStatusList(HttpServletRequest request,
                                      HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 获取状态列表
            List<String> statusList = accountingPeriodService.getPeriodStatusList(
                1L, // 默认账簿ID，需要根据实际业务调整
                loginStaff.getCurrentOrg().getOrgid().longValue()
            );

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(statusList);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("获取期间状态列表失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("根据ID查询会计期间详情")
    @GetMapping("/{periodId}")
    public String getAccountingPeriodById(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @PathVariable Long periodId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 查询期间详情
            TblAccountingPeriod period = accountingPeriodService.getById(periodId);
            if (period == null) {
                return createErrorResponse("会计期间不存在");
            }

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(convertPeriodToMap(period));
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询会计期间详情失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    // ==================== 辅助方法 ====================

    /**
     * 将期间实体转换为Map
     */
    private Map<String, Object> convertPeriodToMap(TblAccountingPeriod period) {
        Map<String, Object> map = new HashMap<>();
        map.put("periodId", period.getPeriodId());
        map.put("yearNo", period.getYearNo());
        map.put("monthNo", period.getMonthNo());
        map.put("periodCode", period.getPeriodCode());
        map.put("periodName", period.getPeriodName());

        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (period.getStartDate() != null) {
            map.put("startDate", sdf.format(period.getStartDate()));
        }
        if (period.getEndDate() != null) {
            map.put("endDate", sdf.format(period.getEndDate()));
        }

        map.put("periodStatus", period.getPeriodStatus());
        map.put("periodStatusName", TblAccountingPeriod.PeriodStatus.getStatusName(period.getPeriodStatus()));
        map.put("isCurrent", period.getIsCurrent());
        map.put("createdBy", period.getCreatedBy());

        if (period.getCreatedTime() != null) {
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            map.put("createdTime", sdf.format(period.getCreatedTime()));
        }

        map.put("remark", period.getRemark());

        return map;
    }

    /**
     * 创建错误响应
     */
    private String createErrorResponse(String message) {
        JsonBean json = new JsonBean();
        json.setCode(0);
        json.setMsg(message);
        return JsonMapper.nonNullMapper().toJson(json);
    }
}