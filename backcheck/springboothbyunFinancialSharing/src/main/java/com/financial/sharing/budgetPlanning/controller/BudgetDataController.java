package com.financial.sharing.budgetPlanning.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.budgetPlanning.dto.BudgetDataQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetData;
import com.financial.sharing.budgetPlanning.service.BudgetDataService;
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
 * 预算数据编制Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "计划预算-预算数据编制")
@RestController
@RequestMapping("/budgetPlanning/budgetData")
public class BudgetDataController {

    @Autowired
    private BudgetDataService dataService;

    @ApiOperation("查询预算数据列表(分页)")
    @PostMapping("/getDataList")
    public MyJsonBean getDataList(@RequestBody BudgetDataQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            PageInfo<TblBudgetData> pageInfo = dataService.getDataList(param);
            return MyJsonBean.ok("查询成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询预算数据列表(不分页)")
    @PostMapping("/getDataListNoPage")
    public MyJsonBean getDataListNoPage(@RequestBody BudgetDataQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            List<TblBudgetData> list = dataService.getDataListNoPage(param);
            return MyJsonBean.ok("查询成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("根据ID查询预算数据")
    @PostMapping("/getDataById")
    public MyJsonBean getDataById(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String dataId = params.get("dataId");
            if (dataId == null || dataId.isEmpty()) {
                return MyJsonBean.errorData("数据ID不能为空");
            }
            TblBudgetData data = dataService.getDataById(dataId);
            return MyJsonBean.ok("查询成功", data);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("新增预算数据")
    @PostMapping("/addData")
    public MyJsonBean addData(@RequestBody TblBudgetData data) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            dataService.addData(data);
            return MyJsonBean.ok("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("新增失败: " + e.getMessage());
        }
    }

    @ApiOperation("修改预算数据")
    @PostMapping("/updateData")
    public MyJsonBean updateData(@RequestBody TblBudgetData data) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            dataService.updateData(data);
            return MyJsonBean.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("修改失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除预算数据")
    @PostMapping("/deleteData")
    public MyJsonBean deleteData(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String dataId = params.get("dataId");
            if (dataId == null || dataId.isEmpty()) {
                return MyJsonBean.errorData("数据ID不能为空");
            }
            dataService.deleteData(dataId);
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量删除预算数据")
    @PostMapping("/batchDeleteData")
    public MyJsonBean batchDeleteData(@RequestBody Map<String, List<String>> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            List<String> dataIds = params.get("dataIds");
            if (dataIds == null || dataIds.isEmpty()) {
                return MyJsonBean.errorData("数据ID列表不能为空");
            }
            dataService.batchDeleteData(dataIds);
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("提交预算数据")
    @PostMapping("/submitData")
    public MyJsonBean submitData(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String dataId = params.get("dataId");
            if (dataId == null || dataId.isEmpty()) {
                return MyJsonBean.errorData("数据ID不能为空");
            }
            dataService.submitData(dataId);
            return MyJsonBean.ok("提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("提交失败: " + e.getMessage());
        }
    }

    @ApiOperation("撤回预算数据")
    @PostMapping("/withdrawData")
    public MyJsonBean withdrawData(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String dataId = params.get("dataId");
            if (dataId == null || dataId.isEmpty()) {
                return MyJsonBean.errorData("数据ID不能为空");
            }
            dataService.withdrawData(dataId);
            return MyJsonBean.ok("撤回成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("撤回失败: " + e.getMessage());
        }
    }

    @ApiOperation("审批预算数据")
    @PostMapping("/approveData")
    public MyJsonBean approveData(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String dataId = params.get("dataId");
            if (dataId == null || dataId.isEmpty()) {
                return MyJsonBean.errorData("数据ID不能为空");
            }
            dataService.approveData(dataId);
            return MyJsonBean.ok("审批成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("审批失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量提交预算数据")
    @PostMapping("/batchSubmitData")
    public MyJsonBean batchSubmitData(@RequestBody Map<String, List<String>> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            List<String> dataIds = params.get("dataIds");
            if (dataIds == null || dataIds.isEmpty()) {
                return MyJsonBean.errorData("数据ID列表不能为空");
            }
            dataService.batchSubmitData(dataIds);
            return MyJsonBean.ok("提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("提交失败: " + e.getMessage());
        }
    }
}

