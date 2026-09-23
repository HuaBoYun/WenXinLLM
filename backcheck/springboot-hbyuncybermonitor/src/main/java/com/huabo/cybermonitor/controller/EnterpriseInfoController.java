package com.huabo.cybermonitor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huabo.cybermonitor.entity.EnterpriseInfo;
import com.huabo.cybermonitor.mapper.EnterpriseInfoMapper;
import com.huabo.cybermonitor.service.IEnterpriseInfoService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.EnterpriseInfoQueryVO;
import com.huabo.cybermonitor.vo.EnterpriseStatisticsVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 企业基础信息管理 Controller
 *
 * @author system
 * @since 2024-01-01
 */
@Tag(name="企业基础信息管理",description="企业基础信息管理")
@RestController
@RequestMapping("/v1/enterprise/info")
public class EnterpriseInfoController {

	private static final Logger log = LoggerFactory.getLogger(EnterpriseInfoController.class);

    @Autowired
    private IEnterpriseInfoService enterpriseInfoService;
    @Autowired
    private EnterpriseInfoMapper enterpriseInfoMapper;

    @Operation(summary = "分页查询企业列表")
    @PostMapping("/list")
    public R<PageResult<EnterpriseInfo>> getEnterpriseList(@RequestBody EnterpriseInfoQueryVO queryVO) {
        try {
            int pn = queryVO.getPageNumber() != null ? queryVO.getPageNumber() : 1;
            int ps = queryVO.getPageSize() != null ? queryVO.getPageSize() : 15;
            PageHelper.startPage(pn, ps);
            List<EnterpriseInfo> list = enterpriseInfoMapper.selectList(new LambdaQueryWrapper<>());
            PageInfo<EnterpriseInfo> pageInfo = new PageInfo<>(list);
            PageResult<EnterpriseInfo> pageResult = new PageResult<>();
            pageResult.setTlist(pageInfo.getList());
            pageResult.setTotalRecord((int) pageInfo.getTotal());
            pageResult.setCurrentPage(pn);
            pageResult.setPageNumber(pn);
            pageResult.setPageSize(ps);
            pageResult.setTotalPage(pageInfo.getPages());
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询企业列表失败", e);
            return R.fail("查询企业列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业详情")
    @PostMapping("/detail")
    public R<Map<String, Object>> getEnterpriseDetail(@RequestBody Map<String, String> params) {
        try {
            String enterpriseId = params.get("enterpriseId");
            if (enterpriseId == null || enterpriseId.trim().isEmpty()) {
                return R.fail("企业ID不能为空");
            }
            
            Map<String, Object> detail = enterpriseInfoService.getEnterpriseDetail(enterpriseId);
            return R.success(detail);
        } catch (Exception e) {
            log.error("获取企业详情失败", e);
            return R.fail("获取企业详情失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增企业")
    @PostMapping("/add")
    public R<String> addEnterprise(@RequestBody EnterpriseInfo enterpriseInfo) {
        try {
            boolean success = enterpriseInfoService.addEnterprise(enterpriseInfo);
            if (success) {
                return R.success("新增企业成功");
            } else {
                return R.fail("新增企业失败");
            }
        } catch (Exception e) {
            log.error("新增企业失败", e);
            return R.fail("新增企业失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新企业信息")
    @PostMapping("/update")
    public R<String> updateEnterprise(@RequestBody EnterpriseInfo enterpriseInfo) {
        try {
            if (enterpriseInfo.getEnterpriseId() == null || enterpriseInfo.getEnterpriseId().trim().isEmpty()) {
                return R.fail("企业ID不能为空");
            }
            
            boolean success = enterpriseInfoService.updateEnterprise(enterpriseInfo);
            if (success) {
                return R.success("更新企业信息成功");
            } else {
                return R.fail("更新企业信息失败");
            }
        } catch (Exception e) {
            log.error("更新企业信息失败", e);
            return R.fail("更新企业信息失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除企业")
    @PostMapping("/delete")
    public R<String> deleteEnterprise(@RequestBody Map<String, String> params) {
        try {
            String enterpriseId = params.get("enterpriseId");
            if (enterpriseId == null || enterpriseId.trim().isEmpty()) {
                return R.fail("企业ID不能为空");
            }
            
            boolean success = enterpriseInfoService.deleteEnterprise(enterpriseId);
            if (success) {
                return R.success("删除企业成功");
            } else {
                return R.fail("删除企业失败");
            }
        } catch (Exception e) {
            log.error("删除企业失败", e);
            return R.fail("删除企业失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除企业")
    @PostMapping("/batch-delete")
    public R<String> batchDeleteEnterprise(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> enterpriseIds = params.get("enterpriseIds");
            if (enterpriseIds == null || enterpriseIds.isEmpty()) {
                return R.fail("企业ID列表不能为空");
            }
            
            boolean success = enterpriseInfoService.batchDeleteEnterprise(enterpriseIds);
            if (success) {
                return R.success("批量删除企业成功");
            } else {
                return R.fail("批量删除企业失败");
            }
        } catch (Exception e) {
            log.error("批量删除企业失败", e);
            return R.fail("批量删除企业失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取母公司列表")
    @GetMapping("/parent-list")
    public R<List<Map<String, Object>>> getParentEnterpriseList() {
        try {
            List<Map<String, Object>> parentList = enterpriseInfoService.getParentEnterpriseList();
            return R.success(parentList);
        } catch (Exception e) {
            log.error("获取母公司列表失败", e);
            return R.fail("获取母公司列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "验证统一社会信用代码")
    @PostMapping("/validate-credit-code")
    public R<Map<String, Object>> validateCreditCode(@RequestBody Map<String, String> params) {
        try {
            String creditCode = params.get("creditCode");
            String excludeId = params.get("excludeId");
            
            if (creditCode == null || creditCode.trim().isEmpty()) {
                return R.fail("统一社会信用代码不能为空");
            }
            
            boolean isDuplicate = enterpriseInfoService.validateCreditCode(creditCode, excludeId);

            Map<String, Object> result = new HashMap<>();
            result.put("isDuplicate", isDuplicate);
            result.put("message", isDuplicate ? "统一社会信用代码已存在" : "统一社会信用代码可用");

            return R.success(result);
        } catch (Exception e) {
            log.error("验证统一社会信用代码失败", e);
            return R.fail("验证统一社会信用代码失败：" + e.getMessage());
        }
    }

    @Operation(summary = "验证企业名称")
    @PostMapping("/validate-name")
    public R<Map<String, Object>> validateEnterpriseName(@RequestBody Map<String, String> params) {
        try {
            String enterpriseName = params.get("enterpriseName");
            String excludeId = params.get("excludeId");
            
            if (enterpriseName == null || enterpriseName.trim().isEmpty()) {
                return R.fail("企业名称不能为空");
            }
            
            boolean isDuplicate = enterpriseInfoService.validateEnterpriseName(enterpriseName, excludeId);

            Map<String, Object> result = new HashMap<>();
            result.put("isDuplicate", isDuplicate);
            result.put("message", isDuplicate ? "企业名称已存在" : "企业名称可用");

            return R.success(result);
        } catch (Exception e) {
            log.error("验证企业名称失败", e);
            return R.fail("验证企业名称失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业统计数据")
    @PostMapping("/statistics/overview")
    public R<EnterpriseStatisticsVO> getEnterpriseStatistics() {
        try {
            EnterpriseStatisticsVO statistics = enterpriseInfoService.getEnterpriseStatistics();
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取企业统计数据失败", e);
            return R.fail("获取企业统计数据失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业类型分布")
    @PostMapping("/statistics/type-distribution")
    public R<List<Map<String, Object>>> getEnterpriseTypeDistribution() {
        try {
            List<Map<String, Object>> distribution = enterpriseInfoService.getEnterpriseTypeDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取企业类型分布失败", e);
            return R.fail("获取企业类型分布失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业地区分布")
    @PostMapping("/statistics/region-distribution")
    public R<List<Map<String, Object>>> getEnterpriseRegionDistribution() {
        try {
            List<Map<String, Object>> distribution = enterpriseInfoService.getEnterpriseRegionDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取企业地区分布失败", e);
            return R.fail("获取企业地区分布失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业行业分布")
    @PostMapping("/statistics/industry-distribution")
    public R<List<Map<String, Object>>> getEnterpriseIndustryDistribution() {
        try {
            List<Map<String, Object>> distribution = enterpriseInfoService.getEnterpriseIndustryDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取企业行业分布失败", e);
            return R.fail("获取企业行业分布失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出企业列表")
    @PostMapping("/export")
    public void exportEnterpriseList(@RequestBody EnterpriseInfoQueryVO queryVO, HttpServletResponse response) {
        try {
            enterpriseInfoService.exportEnterpriseList(queryVO, response);
        } catch (Exception e) {
            log.error("导出企业列表失败", e);
            throw new RuntimeException("导出企业列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "下载企业导入模板")
    @GetMapping("/template")
    public void downloadEnterpriseTemplate(HttpServletResponse response) {
        try {
            enterpriseInfoService.downloadEnterpriseTemplate(response);
        } catch (Exception e) {
            log.error("下载企业导入模板失败", e);
            throw new RuntimeException("下载企业导入模板失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量导入企业")
    @PostMapping("/import")
    public R<Map<String, Object>> importEnterpriseList(
            @Parameter(description = "导入文件", required = true) @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return R.fail("导入文件不能为空");
            }
            
            Map<String, Object> result = enterpriseInfoService.importEnterpriseList(file);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量导入企业失败", e);
            return R.fail("批量导入企业失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取子公司列表")
    @PostMapping("/child-enterprises")
    public R<List<EnterpriseInfo>> getChildEnterprises(@RequestBody Map<String, String> params) {
        try {
            String parentEnterpriseId = params.get("parentEnterpriseId");
            if (parentEnterpriseId == null || parentEnterpriseId.trim().isEmpty()) {
                return R.fail("母公司ID不能为空");
            }
            
            List<EnterpriseInfo> childEnterprises = enterpriseInfoService.getChildEnterprises(parentEnterpriseId);
            return R.success(childEnterprises);
        } catch (Exception e) {
            log.error("获取子公司列表失败", e);
            return R.fail("获取子公司列表失败：" + e.getMessage());
        }
    }
}
