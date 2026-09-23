package com.financial.sharing.controller;

import com.financial.sharing.service.FixedAssetDisposalService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.FixedAssetDisposalQueryParam;
import com.financial.sharing.vo.param.FixedAssetDisposalSaveParam;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 固定资产处置控制器
 * @author system
 * @since 2026-01-23
 */
@Slf4j
@RestController
@RequestMapping("/zbgl/financial/fixedAssets/disposal")
@Api(tags = "固定资产处置管理")
public class FixedAssetDisposalController {

    @Resource
    private FixedAssetDisposalService disposalService;

    @Resource
    private UserProvider userProvider;

    @ApiOperation("分页查询资产处置列表")
    @GetMapping("/list")
    public MyJsonBean<PageResult> getDisposalList(FixedAssetDisposalQueryParam param) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户未登录或组织信息缺失");
            }
            param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            return disposalService.getDisposalList(param);
        } catch (Exception e) {
            log.error("查询资产处置列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询资产处置详情")
    @GetMapping("/detail/{disposalId}")
    public MyJsonBean getDisposalDetail(@PathVariable String disposalId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户未登录或组织信息缺失");
            }
            return disposalService.getDisposalById(disposalId, loginStaff.getCurrentOrg().getOrgid().longValue());
        } catch (Exception e) {
            log.error("查询资产处置详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("新增或更新资产处置")
    @PostMapping("/save")
    public MyJsonBean saveDisposal(@Valid @RequestBody FixedAssetDisposalSaveParam param) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户未登录或组织信息缺失");
            }
            param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            param.setOperatorId(loginStaff.getStaffid().toString());
            return disposalService.saveOrUpdateDisposal(param);
        } catch (Exception e) {
            log.error("保存资产处置失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除资产处置")
    @DeleteMapping("/delete/{disposalId}")
    public MyJsonBean deleteDisposal(@PathVariable String disposalId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户未登录或组织信息缺失");
            }
            return disposalService.deleteDisposal(disposalId, loginStaff.getCurrentOrg().getOrgid().longValue());
        } catch (Exception e) {
            log.error("删除资产处置失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量删除资产处置")
    @PostMapping("/batchDelete")
    public MyJsonBean batchDeleteDisposal(@RequestBody List<String> disposalIds) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户未登录或组织信息缺失");
            }
            return disposalService.batchDeleteDisposal(disposalIds, loginStaff.getCurrentOrg().getOrgid().longValue());
        } catch (Exception e) {
            log.error("批量删除资产处置失败", e);
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("审批资产处置")
    @PostMapping("/approve")
    public MyJsonBean approveDisposal(
            @ApiParam("处置ID") @RequestParam String disposalId,
            @ApiParam("是否通过") @RequestParam Boolean approved,
            @ApiParam("审批意见") @RequestParam(required = false) String comment) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户未登录或组织信息缺失");
            }
            return disposalService.approveDisposal(
                disposalId, 
                approved, 
                comment, 
                loginStaff.getStaffid().toString(),
                loginStaff.getCurrentOrg().getOrgid().longValue()
            );
        } catch (Exception e) {
            log.error("审批资产处置失败", e);
            return MyJsonBean.errorData("审批失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量审批资产处置")
    @PostMapping("/batchApprove")
    public MyJsonBean batchApproveDisposal(
            @ApiParam("处置ID列表") @RequestBody List<String> disposalIds,
            @ApiParam("是否通过") @RequestParam Boolean approved,
            @ApiParam("审批意见") @RequestParam(required = false) String comment) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户未登录或组织信息缺失");
            }
            return disposalService.batchApproveDisposal(
                disposalIds,
                approved,
                comment,
                loginStaff.getStaffid().toString(),
                loginStaff.getCurrentOrg().getOrgid().longValue()
            );
        } catch (Exception e) {
            log.error("批量审批资产处置失败", e);
            return MyJsonBean.errorData("批量审批失败: " + e.getMessage());
        }
    }

    @ApiOperation("执行资产处置")
    @PostMapping("/execute/{disposalId}")
    public MyJsonBean executeDisposal(@PathVariable String disposalId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户未登录或组织信息缺失");
            }
            return disposalService.executeDisposal(
                disposalId,
                loginStaff.getStaffid().toString(),
                loginStaff.getCurrentOrg().getOrgid().longValue()
            );
        } catch (Exception e) {
            log.error("执行资产处置失败", e);
            return MyJsonBean.errorData("执行失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取处置统计数据")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getDisposalStats(
            @ApiParam("开始日期") @RequestParam(required = false) String startDate,
            @ApiParam("结束日期") @RequestParam(required = false) String endDate) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户未登录或组织信息缺失");
            }
            return disposalService.getDisposalStats(
                loginStaff.getCurrentOrg().getOrgid().longValue(),
                startDate,
                endDate
            );
        } catch (Exception e) {
            log.error("获取处置统计数据失败", e);
            return MyJsonBean.errorData("获取统计数据失败: " + e.getMessage());
        }
    }

    @ApiOperation("导出处置列表")
    @PostMapping("/export")
    public MyJsonBean exportDisposalList(@RequestBody FixedAssetDisposalQueryParam param) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户未登录或组织信息缺失");
            }
            param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            return disposalService.exportDisposalList(param);
        } catch (Exception e) {
            log.error("导出处置列表失败", e);
            return MyJsonBean.errorData("导出失败: " + e.getMessage());
        }
    }

    @ApiOperation("生成处置报告")
    @GetMapping("/report")
    public MyJsonBean generateDisposalReport(
            @ApiParam("开始日期") @RequestParam(required = false) String startDate,
            @ApiParam("结束日期") @RequestParam(required = false) String endDate) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户未登录或组织信息缺失");
            }
            return disposalService.generateDisposalReport(
                loginStaff.getCurrentOrg().getOrgid().longValue(),
                startDate,
                endDate
            );
        } catch (Exception e) {
            log.error("生成处置报告失败", e);
            return MyJsonBean.errorData("生成报告失败: " + e.getMessage());
        }
    }
}


