package com.financial.sharing.controller;

import com.financial.sharing.business.entity.TblBill;
import com.financial.sharing.business.service.BillManagementService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 账单管理控制器
 */
@Api(tags = "账单管理")
@RestController
@RequestMapping("/bill/management")
@CrossOrigin
public class BillManagementController {

    @Autowired
    private BillManagementService billManagementService;

    @ApiOperation("查询账单列表")
    @GetMapping
    public MyJsonBean getBillList(PageableParam pageableParam,
                                 @RequestParam(required = false) String billNumber,
                                 @RequestParam(required = false) String billType,
                                 @RequestParam(required = false) String status,
                                 @RequestParam(required = false) String supplier,
                                 @RequestParam(required = false) String startDate,
                                 @RequestParam(required = false) String endDate) {
        Map<String, Object> param = new HashMap<>();
        param.put("pageNum", pageableParam.getPageNum());
        param.put("pageSize", pageableParam.getSize());
        param.put("billNumber", billNumber);
        param.put("billType", billType);
        param.put("status", status);
        param.put("supplier", supplier);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        return billManagementService.getList(param);
    }

    @ApiOperation("账单采集")
    @PostMapping("/collect")
    public MyJsonBean collectBill(@RequestParam("file") MultipartFile file,
                                 @RequestParam(required = false) String collectMethod,
                                 @RequestParam(required = false) String description) {
        try {
            TblBill bill = new TblBill();
            bill.setBillNumber("BILL" + System.currentTimeMillis());
            bill.setCollectMethod(collectMethod != null ? collectMethod : "UPLOAD");
            bill.setCollectTime(LocalDateTime.now());
            bill.setImageUrl("/api/files/bills/" + bill.getBillId() + ".jpg");
            bill.setRemark(description);

            // 保存账单到数据库
            MyJsonBean saveResult = billManagementService.saveOrUpdate(bill);

            Map<String, Object> result = new HashMap<>();
            result.put("billId", bill.getBillId());
            result.put("fileName", file.getOriginalFilename());
            result.put("fileSize", file.getSize());
            result.put("collectMethod", collectMethod);
            result.put("collectTime", LocalDateTime.now());
            result.put("status", "COLLECTED");
            result.put("message", "账单采集成功，正在进行OCR识别");

            return MyJsonBean.successData("采集成功", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("采集失败: " + e.getMessage());
        }
    }

    @ApiOperation("OCR识别")
    @PostMapping("/{billId}/ocr")
    public MyJsonBean ocrRecognition(@PathVariable String billId,
                                    @RequestBody Map<String, Object> ocrParams) {
        return billManagementService.ocrRecognition(billId, ocrParams);
    }

    @ApiOperation("智能稽核")
    @PostMapping("/{billId}/audit")
    public MyJsonBean intelligentAudit(@PathVariable String billId,
                                      @RequestBody Map<String, Object> auditParams) {
        return billManagementService.intelligentAudit(billId, auditParams);
    }

    @ApiOperation("账单应用")
    @PostMapping("/{billId}/apply")
    public MyJsonBean applyBill(@PathVariable String billId,
                               @RequestBody Map<String, Object> applyData) {
        return billManagementService.applyBill(billId, applyData);
    }

    @ApiOperation("获取账单详情")
    @GetMapping("/{billId}")
    public MyJsonBean getBillDetail(@PathVariable String billId) {
        return billManagementService.getById(billId);
    }

    @ApiOperation("删除账单")
    @DeleteMapping("/{billId}")
    public MyJsonBean deleteBill(@PathVariable String billId) {
        return billManagementService.delete(billId);
    }

    @ApiOperation("批量处理账单")
    @PostMapping("/batch")
    public MyJsonBean batchProcessBills(@RequestBody Map<String, Object> batchData) {
        return billManagementService.batchProcess(batchData);
    }

    @ApiOperation("获取账单图片")
    @GetMapping("/{billId}/image")
    public MyJsonBean getBillImage(@PathVariable String billId) {
        try {
            MyJsonBean detailResult = billManagementService.getById(billId);
            if (detailResult.getCode() == 1 && detailResult.getData() != null) {
                Map<String, Object> bill = (Map<String, Object>) detailResult.getData();
                Map<String, Object> result = new HashMap<>();
                result.put("billId", billId);
                result.put("imageUrl", bill.get("imageUrl"));
                result.put("thumbnailUrl", bill.get("imageUrl"));
                return MyJsonBean.successData("获取成功", result);
            }
            return MyJsonBean.errorData("账单不存在");
        } catch (Exception e) {
            return MyJsonBean.errorData("获取失败: " + e.getMessage());
        }
    }

    @ApiOperation("账单统计")
    @GetMapping("/statistics")
    public MyJsonBean getBillStatistics(@RequestParam(required = false) String startDate,
                                       @RequestParam(required = false) String endDate,
                                       @RequestParam(required = false) String billType) {
        Map<String, Object> param = new HashMap<>();
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("billType", billType);
        return billManagementService.getStatistics(param);
    }

    @ApiOperation("导出账单")
    @GetMapping("/export")
    public MyJsonBean exportBills(@RequestParam(required = false) String billType,
                                 @RequestParam(required = false) String status,
                                 @RequestParam(required = false) String startDate,
                                 @RequestParam(required = false) String endDate) {
        Map<String, Object> param = new HashMap<>();
        param.put("billType", billType);
        param.put("status", status);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        return billManagementService.export(param);
    }
}
