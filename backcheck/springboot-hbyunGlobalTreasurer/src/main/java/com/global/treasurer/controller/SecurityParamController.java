package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TcSecurityParameter;
import com.global.treasurer.service.TcSecurityParameterService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 安全参数配置管理Controller
 * 匹配前端API路径: /financial/basicConfig/securityParam/*
 *
 * @author 华博云开发团队
 * @since 2026-01-27
 */
@RestController
@RequestMapping("/financial/basicConfig/securityParam")
@Api(tags = "安全参数配置管理")
public class SecurityParamController {
    private static final Logger log = LoggerFactory.getLogger(SecurityParamController.class);

    @Autowired
    private TcSecurityParameterService tcSecurityParameterService;

    @Resource
    private UserProvider userProvider;

    @PostMapping("/list")
    @ApiOperation("分页查询安全参数列表")
    public String getList(@RequestParam(required = false) Map<String, Object> params, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            if (params == null) params = new HashMap<>();

            // 使用Service的selectPageList方法
            List<TcSecurityParameter> list = tcSecurityParameterService.selectPageList(params);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", list);
            data.put("totalRecord", list != null ? list.size() : 0);
            data.put("pageNo", 1);
            data.put("pageSize", list != null ? list.size() : 0);

            return new JsonBean(1, "查询成功", data).toJson();
        } catch (Exception e) {
            log.error("获取安全参数列表失败", e);
            return new JsonBean(0, "获取安全参数列表失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/detail")
    @ApiOperation("获取安全参数详情")
    public String getDetail(@RequestParam Long id, HttpServletResponse response) {
        try {
            TcSecurityParameter param = tcSecurityParameterService.getById(id);
            if (param == null) return new JsonBean(0, "安全参数不存在", null).toJson();
            return new JsonBean(1, "查询成功", param).toJson();
        } catch (Exception e) {
            log.error("获取安全参数详情失败", e);
            return new JsonBean(0, "获取安全参数详情失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping({"/add", "/create"})
    @ApiOperation("新增安全参数")
    public String add(@FlexibleRequestBody TcSecurityParameter param, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 检查参数编码唯一性
            if (StringUtils.hasText(param.getParamCode())) {
                QueryWrapper<TcSecurityParameter> checkWrapper = new QueryWrapper<>();
                checkWrapper.eq("PARAM_CODE", param.getParamCode());
                if (tcSecurityParameterService.count(checkWrapper) > 0) {
                    return new JsonBean(0, "参数编码已存在", null).toJson();
                }
            }

            param.setCreateBy(loginStaff.getUsername());
            param.setCreateTime(new Date());
            param.setUpdateBy(loginStaff.getUsername());
            param.setUpdateTime(new Date());
            if (param.getStatus() == null) param.setStatus(1);

            boolean success = tcSecurityParameterService.save(param);
            return success ? new JsonBean(1, "新增成功", null).toJson() : new JsonBean(0, "新增失败", null).toJson();
        } catch (Exception e) {
            log.error("新增安全参数失败", e);
            return new JsonBean(0, "新增安全参数失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/update")
    @ApiOperation("更新安全参数")
    public String update(@FlexibleRequestBody TcSecurityParameter param, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            if (param.getId() == null) {
                return new JsonBean(0, "ID不能为空", null).toJson();
            }

            param.setUpdateBy(loginStaff.getUsername());
            param.setUpdateTime(new Date());

            boolean success = tcSecurityParameterService.updateById(param);
            return success ? new JsonBean(1, "更新成功", null).toJson() : new JsonBean(0, "更新失败", null).toJson();
        } catch (Exception e) {
            log.error("更新安全参数失败", e);
            return new JsonBean(0, "更新安全参数失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新安全参数")
    public String saveOrUpdate(@FlexibleRequestBody TcSecurityParameter param, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            if (param.getId() == null) {
                param.setCreateBy(loginStaff.getUsername());
                param.setCreateTime(new Date());
            }
            param.setUpdateBy(loginStaff.getUsername());
            param.setUpdateTime(new Date());

            boolean success = tcSecurityParameterService.saveOrUpdate(param);
            return success ? new JsonBean(1, "保存成功", null).toJson() : new JsonBean(0, "保存失败", null).toJson();
        } catch (Exception e) {
            log.error("保存安全参数失败", e);
            return new JsonBean(0, "保存安全参数失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/delete")
    @ApiOperation("删除安全参数")
    public String delete(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Long id = params.get("id") != null ? Long.parseLong(params.get("id").toString()) : null;
            if (id == null) return new JsonBean(0, "ID不能为空", null).toJson();
            boolean success = tcSecurityParameterService.removeById(id);
            return success ? new JsonBean(1, "删除成功", null).toJson() : new JsonBean(0, "删除失败", null).toJson();
        } catch (Exception e) {
            log.error("删除安全参数失败", e);
            return new JsonBean(0, "删除安全参数失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batchDelete")
    @ApiOperation("批量删除安全参数")
    public String batchDelete(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return new JsonBean(0, "请选择要删除的记录", null).toJson();
            }

            boolean success = tcSecurityParameterService.removeByIds(ids);
            return success ? new JsonBean(1, "批量删除成功", null).toJson() : new JsonBean(0, "批量删除失败", null).toJson();
        } catch (Exception e) {
            log.error("批量删除安全参数失败", e);
            return new JsonBean(0, "批量删除安全参数失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/updateStatus")
    @ApiOperation("更新安全参数状态")
    public String updateStatus(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Long id = params.get("id") != null ? Long.parseLong(params.get("id").toString()) : null;
            Integer status = params.get("status") != null ? Integer.parseInt(params.get("status").toString()) : null;
            if (id == null || status == null) return new JsonBean(0, "参数不完整", null).toJson();

            TcSecurityParameter param = new TcSecurityParameter();
            param.setId(id);
            param.setStatus(status);
            param.setUpdateBy(loginStaff.getUsername());
            param.setUpdateTime(new Date());
            boolean success = tcSecurityParameterService.updateById(param);
            return success ? new JsonBean(1, "状态更新成功", null).toJson() : new JsonBean(0, "状态更新失败", null).toJson();
        } catch (Exception e) {
            log.error("更新安全参数状态失败", e);
            return new JsonBean(0, "更新安全参数状态失败: " + e.getMessage(), null).toJson();
        }
    }

    @RequestMapping(value = {"/statistics", "/getStatistics"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取安全参数统计信息")
    public String getStatistics(HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 统计总数
            long totalCount = tcSecurityParameterService.count();

            // 统计启用数
            QueryWrapper<TcSecurityParameter> enabledWrapper = new QueryWrapper<>();
            enabledWrapper.eq("STATUS", 1);
            long enabledCount = tcSecurityParameterService.count(enabledWrapper);

            // 统计禁用数
            QueryWrapper<TcSecurityParameter> disabledWrapper = new QueryWrapper<>();
            disabledWrapper.eq("STATUS", 0);
            long disabledCount = tcSecurityParameterService.count(disabledWrapper);

            // 按类型统计
            QueryWrapper<TcSecurityParameter> typeWrapper = new QueryWrapper<>();
            typeWrapper.select("PARAM_TYPE", "COUNT(*) as count");
            typeWrapper.groupBy("PARAM_TYPE");

            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalCount", totalCount);
            statistics.put("enabledCount", enabledCount);
            statistics.put("disabledCount", disabledCount);

            return new JsonBean(1, "获取成功", statistics).toJson();
        } catch (Exception e) {
            log.error("获取安全参数统计信息失败", e);
            return new JsonBean(0, "获取统计信息失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/export")
    @ApiOperation("导出安全参数配置")
    public String export(@RequestParam(required = false) String paramName,
                        @RequestParam(required = false) String paramType,
                        @RequestParam(required = false) Integer status,
                        HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            QueryWrapper<TcSecurityParameter> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(paramName)) queryWrapper.like("PARAM_NAME", paramName);
            if (StringUtils.hasText(paramType)) queryWrapper.eq("PARAM_TYPE", paramType);
            if (status != null) queryWrapper.eq("STATUS", status);
            queryWrapper.orderByDesc("CREATE_TIME");

            List<TcSecurityParameter> list = tcSecurityParameterService.list(queryWrapper);

            Map<String, Object> data = new HashMap<>();
            data.put("exportList", list);
            data.put("total", list.size());
            data.put("exportTime", new Date());

            return new JsonBean(1, "导出成功", data).toJson();
        } catch (Exception e) {
            log.error("导出安全参数配置失败", e);
            return new JsonBean(0, "导出安全参数配置失败: " + e.getMessage(), null).toJson();
        }
    }
}

