package com.financial.sharing.controller;

import com.financial.sharing.service.TblProxyDelegationService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 代理委托管理控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@Api(tags = "代理委托管理")
@RestController
@RequestMapping("/proxy-delegations")
@CrossOrigin
public class ProxyDelegationController {

    @Autowired
    private TblProxyDelegationService proxyDelegationService;

    @ApiOperation("查询代理委托列表")
    @GetMapping
    public MyJsonBean getProxyDelegationList(@RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                            @RequestParam(required = false, defaultValue = "10") Integer size,
                                            @RequestParam(required = false) String orgId,
                                            @RequestParam(required = false) String delegatorName,
                                            @RequestParam(required = false) String proxyName,
                                            @RequestParam(required = false) String delegationType,
                                            @RequestParam(required = false) String status) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("pageNum", pageNum);
            param.put("size", size);
            if (orgId != null) param.put("orgId", orgId);
            if (delegatorName != null) param.put("delegatorName", delegatorName);
            if (proxyName != null) param.put("proxyName", proxyName);
            if (delegationType != null) param.put("delegationType", delegationType);
            if (status != null) param.put("status", status);

            PageResult<?> pageResult = (PageResult<?>) proxyDelegationService.getList(param);
            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("查询代理委托列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存代理委托")
    @PostMapping
    public MyJsonBean saveProxyDelegation(@RequestBody Map<String, Object> proxyDelegation) {
        try {
            Object result = proxyDelegationService.saveOrUpdate(proxyDelegation);
            return MyJsonBean.successData("保存成功", result);
        } catch (Exception e) {
            log.error("保存代理委托失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除代理委托")
    @DeleteMapping("/{delegationId}")
    public MyJsonBean deleteProxyDelegation(@PathVariable String delegationId) {
        try {
            proxyDelegationService.delete(delegationId);
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除代理委托失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取代理委托详情")
    @GetMapping("/{delegationId}")
    public MyJsonBean getProxyDelegationDetail(@PathVariable String delegationId) {
        try {
            Object delegation = proxyDelegationService.getDetail(delegationId);
            return MyJsonBean.successData("查询成功", delegation);
        } catch (Exception e) {
            log.error("获取代理委托详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取代理委托权限范围")
    @GetMapping("/{delegationId}/permissions")
    public MyJsonBean getProxyDelegationPermissions(@PathVariable String delegationId) {
        try {
            Object permissions = proxyDelegationService.getPermissions(delegationId);
            return MyJsonBean.successData("查询成功", permissions);
        } catch (Exception e) {
            log.error("获取代理委托权限失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存代理委托权限范围")
    @PostMapping("/{delegationId}/permissions")
    public MyJsonBean saveProxyDelegationPermissions(@PathVariable String delegationId,
                                                    @RequestBody List<Map<String, Object>> permissions) {
        try {
            proxyDelegationService.savePermissions(delegationId, permissions);
            return MyJsonBean.successData("保存权限范围成功");
        } catch (Exception e) {
            log.error("保存代理委托权限失败", e);
            return MyJsonBean.errorData("保存权限范围失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取代理委托历史记录")
    @GetMapping("/{delegationId}/history")
    public MyJsonBean getProxyDelegationHistory(@PathVariable String delegationId) {
        try {
            Object history = proxyDelegationService.getHistory(delegationId);
            return MyJsonBean.successData("查询成功", history);
        } catch (Exception e) {
            log.error("获取代理委托历史记录失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量删除代理委托")
    @DeleteMapping("/batch")
    public MyJsonBean batchDeleteProxyDelegations(@RequestBody List<String> delegationIds) {
        try {
            proxyDelegationService.batchDelete(delegationIds);
            return MyJsonBean.successData("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除代理委托失败", e);
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("激活代理委托")
    @PutMapping("/{delegationId}/activate")
    public MyJsonBean activateProxyDelegation(@PathVariable String delegationId) {
        try {
            proxyDelegationService.activate(delegationId);
            return MyJsonBean.successData("激活成功");
        } catch (Exception e) {
            log.error("激活代理委托失败", e);
            return MyJsonBean.errorData("激活失败: " + e.getMessage());
        }
    }

    @ApiOperation("停用代理委托")
    @PutMapping("/{delegationId}/deactivate")
    public MyJsonBean deactivateProxyDelegation(@PathVariable String delegationId) {
        try {
            proxyDelegationService.deactivate(delegationId);
            return MyJsonBean.successData("停用成功");
        } catch (Exception e) {
            log.error("停用代理委托失败", e);
            return MyJsonBean.errorData("停用失败: " + e.getMessage());
        }
    }

    @ApiOperation("暂停代理委托")
    @PutMapping("/{delegationId}/suspend")
    public MyJsonBean suspendProxyDelegation(@PathVariable String delegationId,
                                           @RequestBody Map<String, Object> suspendInfo) {
        try {
            proxyDelegationService.suspend(delegationId, suspendInfo);
            return MyJsonBean.successData("暂停成功");
        } catch (Exception e) {
            log.error("暂停代理委托失败", e);
            return MyJsonBean.errorData("暂停失败: " + e.getMessage());
        }
    }

    @ApiOperation("恢复代理委托")
    @PutMapping("/{delegationId}/resume")
    public MyJsonBean resumeProxyDelegation(@PathVariable String delegationId) {
        try {
            proxyDelegationService.resume(delegationId);
            return MyJsonBean.successData("恢复成功");
        } catch (Exception e) {
            log.error("恢复代理委托失败", e);
            return MyJsonBean.errorData("恢复失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取我的代理委托")
    @GetMapping("/my-delegations")
    public MyJsonBean getMyProxyDelegations(@RequestParam String userId,
                                          @RequestParam(required = false) String delegationType) {
        try {
            Object myDelegations = proxyDelegationService.getMyDelegations(userId, delegationType);
            return MyJsonBean.successData("查询成功", myDelegations);
        } catch (Exception e) {
            log.error("获取我的代理委托失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取代理委托统计")
    @GetMapping("/{delegationId}/statistics")
    public MyJsonBean getProxyDelegationStatistics(@PathVariable String delegationId,
                                                  @RequestParam(required = false) String startDate,
                                                  @RequestParam(required = false) String endDate) {
        try {
            Object statistics = proxyDelegationService.getStatistics(delegationId, startDate, endDate);
            return MyJsonBean.successData("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取代理委托统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}
