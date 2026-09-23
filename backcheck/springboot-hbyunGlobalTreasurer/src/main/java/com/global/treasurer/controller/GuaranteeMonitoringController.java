package com.global.treasurer.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblGuaranteeMonitoring;
import com.global.treasurer.mapper.GuaranteeMonitoringMapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 担保监控管理Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
@Controller
@RequestMapping({"/rzgl/guarantee/monitoring", "/financial/rzgl/guarantee/monitoring", "/centralaudit/financial/rzgl/guarantee/monitoring"})
@Api(tags = "担保监控管理")
public class GuaranteeMonitoringController {
    private static final Logger log = LoggerFactory.getLogger(GuaranteeMonitoringController.class);

    @Resource
    private GuaranteeMonitoringMapper guaranteeMonitoringMapper;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询担保监控预警列表（GET请求）
     */
    @GetMapping("/list")
    @ResponseBody
    @ApiOperation("查询担保监控预警列表(GET)")
    public String getMonitoringListByGet(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String alertNo,
            @RequestParam(required = false) String alertType,
            @RequestParam(required = false) String alertLevel,
            @RequestParam(required = false) String alertStatus,
            @RequestParam(required = false) String companyName,
            @RequestHeader(value = "token", required = false) String token,
            HttpServletResponse response) {
        return getMonitoringList(pageNum, pageSize, alertNo, alertType,
                alertLevel, alertStatus, companyName, token, response);
    }

    /**
     * 分页查询担保监控预警列表（POST请求）
     */
    @PostMapping("/list")
    @ResponseBody
    @ApiOperation("查询担保监控预警列表(POST)")
    public String getMonitoringList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String alertNo,
            @RequestParam(required = false) String alertType,
            @RequestParam(required = false) String alertLevel,
            @RequestParam(required = false) String alertStatus,
            @RequestParam(required = false) String companyName,
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
            params.put("alertNo", alertNo);
            params.put("alertType", alertType);
            params.put("alertLevel", alertLevel);
            params.put("alertStatus", alertStatus);
            params.put("companyName", companyName);

            // 调用Mapper的查询方法
            List<TblGuaranteeMonitoring> list = guaranteeMonitoringMapper.selectMonitoringList(params);

            // 返回分页结果
            PageInfo<TblGuaranteeMonitoring> pageInfo = new PageInfo<>(list);

            // 构建前端期望的响应格式
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("records", pageInfo.getList());
            resultMap.put("total", pageInfo.getTotal());

            return JsonBean.success(resultMap);
        } catch (Exception e) {
            log.error("查询担保监控预警列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询担保监控预警详情
     */
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation("根据ID查询担保监控预警详情")
    public String getMonitoringById(@PathVariable Long id) {
        try {
            TblGuaranteeMonitoring monitoring = guaranteeMonitoringMapper.selectById(id);
            if (monitoring != null) {
                return JsonBean.success(monitoring);
            } else {
                return new JsonBean(0, "未找到对应的预警记录", null).toJson();
            }
        } catch (Exception e) {
            log.error("查询担保监控预警详情失败, id: {}", id, e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增担保监控预警
     */
    @PostMapping("")
    @ResponseBody
    @ApiOperation("新增担保监控预警")
    public String addMonitoring(@FlexibleRequestBody TblGuaranteeMonitoring monitoring) {
        try {
            log.info("新增担保监控预警, monitoring: {}", monitoring);
            // 设置默认值
            if (monitoring.getAlertStatus() == null) {
                monitoring.setAlertStatus("PENDING");
            }
            monitoring.setDeleteFlag(0);
            monitoring.setCreatedTime(new java.util.Date());
            monitoring.setUpdatedTime(new java.util.Date());
            // 生成预警编号
            if (monitoring.getAlertNo() == null || monitoring.getAlertNo().isEmpty()) {
                monitoring.setAlertNo("ALERT" + System.currentTimeMillis());
            }
            guaranteeMonitoringMapper.insert(monitoring);
            return JsonBean.success("新增成功");
        } catch (Exception e) {
            log.error("新增担保监控预警失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 修改担保监控预警
     */
    @PutMapping("")
    @ResponseBody
    @ApiOperation("修改担保监控预警")
    public String updateMonitoring(@FlexibleRequestBody TblGuaranteeMonitoring monitoring) {
        try {
            log.info("修改担保监控预警, monitoring: {}", monitoring);
            monitoring.setUpdatedTime(new java.util.Date());
            guaranteeMonitoringMapper.updateById(monitoring);
            return JsonBean.success("修改成功");
        } catch (Exception e) {
            log.error("修改担保监控预警失败", e);
            return new JsonBean(0, "修改失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除担保监控预警
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation("删除担保监控预警")
    public String deleteMonitoring(@PathVariable Long id) {
        try {
            log.info("删除担保监控预警, id: {}", id);
            guaranteeMonitoringMapper.deleteById(id);
            return JsonBean.success("删除成功");
        } catch (Exception e) {
            log.error("删除担保监控预警失败, id: {}", id, e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 处理预警
     */
    @PostMapping("/{id}/handle")
    @ResponseBody
    @ApiOperation("处理预警")
    public String handleMonitoring(@PathVariable Long id, @RequestParam(required = false) String handleComments) {
        try {
            log.info("处理预警, id: {}, comments: {}", id, handleComments);
            guaranteeMonitoringMapper.updateAlertStatus(id, "HANDLED");
            return JsonBean.success("处理成功");
        } catch (Exception e) {
            log.error("处理预警失败, id: {}", id, e);
            return new JsonBean(0, "处理失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 关闭预警
     */
    @PostMapping("/{id}/close")
    @ResponseBody
    @ApiOperation("关闭预警")
    public String closeMonitoring(@PathVariable Long id, @RequestParam(required = false) String closeReason) {
        try {
            log.info("关闭预警, id: {}, reason: {}", id, closeReason);
            guaranteeMonitoringMapper.updateAlertStatus(id, "CLOSED");
            return JsonBean.success("关闭成功");
        } catch (Exception e) {
            log.error("关闭预警失败, id: {}", id, e);
            return new JsonBean(0, "关闭失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 重新激活预警
     */
    @PostMapping("/{id}/reactivate")
    @ResponseBody
    @ApiOperation("重新激活预警")
    public String reactivateMonitoring(@PathVariable Long id) {
        try {
            log.info("重新激活预警, id: {}", id);
            guaranteeMonitoringMapper.updateAlertStatus(id, "PENDING");
            return JsonBean.success("重新激活成功");
        } catch (Exception e) {
            log.error("重新激活预警失败, id: {}", id, e);
            return new JsonBean(0, "重新激活失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 忽略预警
     */
    @PostMapping("/{id}/ignore")
    @ResponseBody
    @ApiOperation("忽略预警")
    public String ignoreMonitoring(@PathVariable Long id, @RequestParam(required = false) String ignoreReason) {
        try {
            log.info("忽略预警, id: {}, reason: {}", id, ignoreReason);
            guaranteeMonitoringMapper.updateAlertStatus(id, "IGNORED");
            return JsonBean.success("忽略成功");
        } catch (Exception e) {
            log.error("忽略预警失败, id: {}", id, e);
            return new JsonBean(0, "忽略失败: " + e.getMessage(), null).toJson();
        }
    }
}
