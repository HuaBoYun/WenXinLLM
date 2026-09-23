package com.financial.sharing.consolidationReport.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.consolidationReport.dto.ReconciliationDataQueryParam;
import com.financial.sharing.consolidationReport.entity.TblReconciliationData;
import com.financial.sharing.consolidationReport.service.ReconciliationDataService;
import com.github.pagehelper.PageInfo;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 对账数据Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "合并报表-对账数据管理")
@RestController
@RequestMapping("/consolidationReport/reconciliationData")
public class ReconciliationDataController {

    @Autowired
    private ReconciliationDataService reconciliationDataService;

    /**
     * 查询对账数据列表
     */
    @ApiOperation("查询对账数据列表")
    @PostMapping("/getReconciliationList")
    public MyJsonBean getReconciliationList(@RequestBody ReconciliationDataQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            PageInfo<TblReconciliationData> pageInfo = reconciliationDataService.getReconciliationList(param);
            return MyJsonBean.ok("查询成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询对账数据
     */
    @ApiOperation("根据ID查询对账数据")
    @PostMapping("/getReconciliationById")
    public MyJsonBean getReconciliationById(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String reconciliationId = params.get("reconciliationId");
            if (reconciliationId == null || reconciliationId.isEmpty()) {
                return MyJsonBean.errorData("对账数据ID不能为空");
            }
            
            TblReconciliationData data = reconciliationDataService.getReconciliationById(reconciliationId);
            return MyJsonBean.ok("查询成功", data);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增对账数据
     */
    @ApiOperation("新增对账数据")
    @PostMapping("/saveReconciliation")
    public MyJsonBean saveReconciliation(@RequestBody TblReconciliationData data) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            reconciliationDataService.saveReconciliation(data);
            return MyJsonBean.ok("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改对账数据
     */
    @ApiOperation("修改对账数据")
    @PostMapping("/updateReconciliation")
    public MyJsonBean updateReconciliation(@RequestBody TblReconciliationData data) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            reconciliationDataService.updateReconciliation(data);
            return MyJsonBean.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除对账数据
     */
    @ApiOperation("删除对账数据")
    @PostMapping("/deleteReconciliation")
    public MyJsonBean deleteReconciliation(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String reconciliationId = params.get("reconciliationId");
            if (reconciliationId == null || reconciliationId.isEmpty()) {
                return MyJsonBean.errorData("对账数据ID不能为空");
            }
            
            reconciliationDataService.deleteReconciliation(reconciliationId);
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除对账数据
     */
    @ApiOperation("批量删除对账数据")
    @PostMapping("/deleteByModelIdAndPeriod")
    public MyJsonBean deleteByModelIdAndPeriod(@RequestBody Map<String, String> params) {
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
            
            reconciliationDataService.deleteByModelIdAndPeriod(modelId, period);
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量导入对账数据
     */
    @ApiOperation("批量导入对账数据")
    @PostMapping("/batchImport")
    public MyJsonBean batchImport(@RequestBody Map<String, Object> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String modelId = (String) params.get("modelId");
            String period = (String) params.get("period");
            @SuppressWarnings("unchecked")
            List<TblReconciliationData> dataList = (List<TblReconciliationData>) params.get("dataList");
            
            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            if (period == null || period.isEmpty()) {
                return MyJsonBean.errorData("期间不能为空");
            }
            if (dataList == null || dataList.isEmpty()) {
                return MyJsonBean.errorData("导入数据不能为空");
            }
            
            Map<String, Object> result = reconciliationDataService.batchImport(dataList, modelId, period);
            return MyJsonBean.ok(result.get("message").toString(), result);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("导入失败: " + e.getMessage());
        }
    }

    /**
     * 统计对账数据
     */
    @ApiOperation("统计对账数据")
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
            
            Map<String, Object> statistics = reconciliationDataService.getStatistics(modelId, period);
            return MyJsonBean.ok("查询成功", statistics);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}

