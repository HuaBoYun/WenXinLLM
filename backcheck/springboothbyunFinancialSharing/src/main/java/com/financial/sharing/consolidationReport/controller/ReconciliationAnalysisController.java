package com.financial.sharing.consolidationReport.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.consolidationReport.dto.ReconciliationAnalysisQueryParam;
import com.financial.sharing.consolidationReport.entity.TblReconciliationAnalysis;
import com.financial.sharing.consolidationReport.service.ReconciliationAnalysisService;
import com.github.pagehelper.PageInfo;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 对账差异分析Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "合并报表-对账差异分析")
@RestController
@RequestMapping("/consolidationReport/reconciliationAnalysis")
public class ReconciliationAnalysisController {

    @Autowired
    private ReconciliationAnalysisService analysisService;

    /**
     * 查询差异分析列表
     */
    @ApiOperation("查询差异分析列表")
    @PostMapping("/getAnalysisList")
    public MyJsonBean getAnalysisList(@RequestBody ReconciliationAnalysisQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            PageInfo<TblReconciliationAnalysis> pageInfo = analysisService.getAnalysisList(param);
            return MyJsonBean.ok("查询成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询差异分析
     */
    @ApiOperation("根据ID查询差异分析")
    @PostMapping("/getAnalysisById")
    public MyJsonBean getAnalysisById(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String analysisId = params.get("analysisId");
            if (analysisId == null || analysisId.isEmpty()) {
                return MyJsonBean.errorData("差异分析ID不能为空");
            }
            
            TblReconciliationAnalysis analysis = analysisService.getAnalysisById(analysisId);
            return MyJsonBean.ok("查询成功", analysis);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据对账数据ID查询差异分析
     */
    @ApiOperation("根据对账数据ID查询差异分析")
    @PostMapping("/getAnalysisByReconciliationId")
    public MyJsonBean getAnalysisByReconciliationId(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String reconciliationId = params.get("reconciliationId");
            if (reconciliationId == null || reconciliationId.isEmpty()) {
                return MyJsonBean.errorData("对账数据ID不能为空");
            }
            
            TblReconciliationAnalysis analysis = analysisService.getAnalysisByReconciliationId(reconciliationId);
            return MyJsonBean.ok("查询成功", analysis);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增差异分析
     */
    @ApiOperation("新增差异分析")
    @PostMapping("/saveAnalysis")
    public MyJsonBean saveAnalysis(@RequestBody TblReconciliationAnalysis analysis) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            analysisService.saveAnalysis(analysis);
            return MyJsonBean.ok("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改差异分析
     */
    @ApiOperation("修改差异分析")
    @PostMapping("/updateAnalysis")
    public MyJsonBean updateAnalysis(@RequestBody TblReconciliationAnalysis analysis) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            analysisService.updateAnalysis(analysis);
            return MyJsonBean.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除差异分析
     */
    @ApiOperation("删除差异分析")
    @PostMapping("/deleteAnalysis")
    public MyJsonBean deleteAnalysis(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String analysisId = params.get("analysisId");
            if (analysisId == null || analysisId.isEmpty()) {
                return MyJsonBean.errorData("差异分析ID不能为空");
            }
            
            analysisService.deleteAnalysis(analysisId);
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 处理差异
     */
    @ApiOperation("处理差异")
    @PostMapping("/handleDifference")
    public MyJsonBean handleDifference(@RequestBody Map<String, Object> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String analysisId = (String) params.get("analysisId");
            String solution = (String) params.get("solution");
            BigDecimal adjustAmount = params.get("adjustAmount") != null 
                ? new BigDecimal(params.get("adjustAmount").toString()) 
                : null;
            
            if (analysisId == null || analysisId.isEmpty()) {
                return MyJsonBean.errorData("差异分析ID不能为空");
            }
            if (solution == null || solution.isEmpty()) {
                return MyJsonBean.errorData("处理方案不能为空");
            }
            
            analysisService.handleDifference(analysisId, solution, adjustAmount);
            return MyJsonBean.ok("处理成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("处理失败: " + e.getMessage());
        }
    }

    /**
     * 关闭差异
     */
    @ApiOperation("关闭差异")
    @PostMapping("/closeDifference")
    public MyJsonBean closeDifference(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String analysisId = params.get("analysisId");
            if (analysisId == null || analysisId.isEmpty()) {
                return MyJsonBean.errorData("差异分析ID不能为空");
            }
            
            analysisService.closeDifference(analysisId);
            return MyJsonBean.ok("关闭成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("关闭失败: " + e.getMessage());
        }
    }

    /**
     * 统计差异分析
     */
    @ApiOperation("统计差异分析")
    @PostMapping("/getStatistics")
    public MyJsonBean getStatistics(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String modelId = params.get("modelId");
            String period = params.get("period");
            
            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            if (period == null || period.isEmpty()) {
                return MyJsonBean.errorData("期间不能为空");
            }
            
            Map<String, Object> statistics = analysisService.getStatistics(modelId, period);
            return MyJsonBean.ok("查询成功", statistics);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 自动创建差异分析
     */
    @ApiOperation("自动创建差异分析")
    @PostMapping("/autoCreateAnalysis")
    public MyJsonBean autoCreateAnalysis(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String modelId = params.get("modelId");
            String period = params.get("period");
            
            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            if (period == null || period.isEmpty()) {
                return MyJsonBean.errorData("期间不能为空");
            }
            
            Map<String, Object> result = analysisService.autoCreateAnalysis(modelId, period);
            return MyJsonBean.ok(result.get("message").toString(), result);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("自动创建失败: " + e.getMessage());
        }
    }
}

