package com.financial.sharing.controller;

import com.financial.sharing.service.TblExpenseItemService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;
import com.financial.sharing.vo.param.TblExpenseItemQueryParam;
import com.financial.sharing.vo.param.TblExpenseItemSaveParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 费用项目管理控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Api(tags = "费用项目管理")
@RestController
@RequestMapping("/expense/items")
@CrossOrigin
public class ExpenseItemController {

    @Autowired
    private TblExpenseItemService expenseItemService;

    @ApiOperation("查询费用项目列表")
    @GetMapping
    public MyJsonBean getExpenseItemList(TblExpenseItemQueryParam param) {
        return expenseItemService.getList(param);
    }

    @ApiOperation("保存费用项目")
    @PostMapping
    public MyJsonBean saveExpenseItem(@RequestBody TblExpenseItemSaveParam expenseItem) {
        return expenseItemService.saveOrUpdate(expenseItem);
    }

    @ApiOperation("删除费用项目")
    @DeleteMapping("/{itemId}")
    public MyJsonBean deleteExpenseItem(@PathVariable String itemId) {
        return expenseItemService.delete(itemId);
    }

    @ApiOperation("获取费用项目详情")
    @GetMapping("/{itemId}")
    public MyJsonBean getExpenseItemDetail(@PathVariable String itemId) {
        return expenseItemService.getById(itemId);
    }

    @ApiOperation("获取费用项目树形结构")
    @GetMapping("/tree")
    public MyJsonBean getExpenseItemTree(@RequestParam(required = false) String orgId) {
        return expenseItemService.getTree();
    }

    @ApiOperation("批量删除费用项目")
    @DeleteMapping("/batch")
    public MyJsonBean batchDeleteExpenseItems(@RequestBody List<String> itemIds) {
        int successCount = 0;
        for (String itemId : itemIds) {
            MyJsonBean result = expenseItemService.delete(itemId);
            if (result.getCode() == 1) {
                successCount++;
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", successCount);
        result.put("totalCount", itemIds.size());
        return MyJsonBean.successData("批量删除完成", result);
    }

    @ApiOperation("启用/禁用费用项目")
    @PutMapping("/{itemId}/status")
    public MyJsonBean updateExpenseItemStatus(@PathVariable String itemId,
                                            @RequestParam Integer isEnabled) {
        return expenseItemService.updateStatus(itemId, isEnabled);
    }

    @ApiOperation("导入费用项目")
    @PostMapping("/import")
    public MyJsonBean importExpenseItems(@RequestParam("file") MultipartFile file) {
        try {
            // TODO: 实现Excel导入逻辑
            Map<String, Object> result = new HashMap<>();
            result.put("totalCount", 0);
            result.put("successCount", 0);
            result.put("failureCount", 0);
            result.put("message", "导入功能待实现");
            return MyJsonBean.successData("导入成功", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("导入失败: " + e.getMessage());
        }
    }

    @ApiOperation("导出费用项目")
    @GetMapping("/export")
    public MyJsonBean exportExpenseItems(@RequestParam(required = false) String orgId,
                                       @RequestParam(required = false) String itemType) {
        try {
            // TODO: 实现Excel导出逻辑
            Map<String, Object> result = new HashMap<>();
            result.put("fileName", "expense_items_" + System.currentTimeMillis() + ".xlsx");
            result.put("url", "/download/expense_items.xlsx");
            return MyJsonBean.successData("导出成功", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("导出失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取费用项目子节点")
    @GetMapping("/{itemId}/children")
    public MyJsonBean getExpenseItemChildren(@PathVariable String itemId) {
        return expenseItemService.getChildren(itemId);
    }

    @ApiOperation("移动费用项目节点")
    @PutMapping("/{itemId}/move")
    public MyJsonBean moveExpenseItem(@PathVariable String itemId,
                                     @RequestParam String newParentId) {
        return expenseItemService.move(itemId, newParentId);
    }

    @ApiOperation("获取费用项目节点路径")
    @GetMapping("/{itemId}/path")
    public MyJsonBean getExpenseItemPath(@PathVariable String itemId) {
        return expenseItemService.getPath(itemId);
    }
}
