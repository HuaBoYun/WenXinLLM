package com.financial.sharing.controller;

import com.financial.sharing.service.TblTravelArchiveService;
import com.financial.sharing.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商旅档案管理控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@Api(tags = "商旅档案管理")
@RestController
@RequestMapping("/travel-archives")
@CrossOrigin
public class TravelArchiveController {

    @Autowired
    private TblTravelArchiveService travelArchiveService;

    @ApiOperation("查询商旅档案列表")
    @GetMapping
    public MyJsonBean getTravelArchiveList(@RequestParam(required = false) String archiveName,
                                         @RequestParam(required = false) String archiveType,
                                         @RequestParam(required = false) String providerName,
                                         @RequestParam(required = false) Integer isEnabled,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer size,
                                         @RequestParam(required = false) Integer pageNo,
                                         @RequestParam(required = false) Integer pageSize) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("archiveName", archiveName);
            param.put("archiveType", archiveType);
            param.put("providerName", providerName);
            param.put("isEnabled", isEnabled);

            // 支持 page/size 和 pageNo/pageSize 两种参数格式
            if (page != null) {
                param.put("page", page);
            }
            if (size != null) {
                param.put("size", size);
            }
            if (pageNo != null) {
                param.put("pageNo", pageNo);
            }
            if (pageSize != null) {
                param.put("pageSize", pageSize);
            }

            return travelArchiveService.getList(param);
        } catch (Exception e) {
            log.error("查询商旅档案列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存商旅档案")
    @PostMapping
    public MyJsonBean saveTravelArchive(@RequestBody Map<String, Object> param) {
        try {
            return travelArchiveService.saveOrUpdate(param);
        } catch (Exception e) {
            log.error("保存商旅档案失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除商旅档案")
    @DeleteMapping("/{archiveId}")
    public MyJsonBean deleteTravelArchive(@PathVariable String archiveId) {
        try {
            return travelArchiveService.delete(archiveId);
        } catch (Exception e) {
            log.error("删除商旅档案失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取商旅档案详情")
    @GetMapping("/{archiveId}")
    public MyJsonBean getTravelArchiveDetail(@PathVariable String archiveId) {
        try {
            return travelArchiveService.getById(archiveId);
        } catch (Exception e) {
            log.error("获取商旅档案详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取商旅档案价格信息")
    @GetMapping("/{archiveId}/prices")
    public MyJsonBean getTravelArchivePrices(@PathVariable String archiveId) {
        try {
            return travelArchiveService.getPriceList(archiveId);
        } catch (Exception e) {
            log.error("获取商旅档案价格信息失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存商旅档案价格信息")
    @PostMapping("/{archiveId}/prices")
    public MyJsonBean saveTravelArchivePrices(@PathVariable String archiveId,
                                             @RequestBody List<Map<String, Object>> prices) {
        try {
            return travelArchiveService.savePriceList(archiveId, prices);
        } catch (Exception e) {
            log.error("保存商旅档案价格信息失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取商旅档案合作协议")
    @GetMapping("/{archiveId}/agreements")
    public MyJsonBean getTravelArchiveAgreements(@PathVariable String archiveId) {
        try {
            return travelArchiveService.getAgreements(archiveId);
        } catch (Exception e) {
            log.error("获取商旅档案合作协议失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存商旅档案合作协议")
    @PostMapping("/{archiveId}/agreements")
    public MyJsonBean saveTravelArchiveAgreements(@PathVariable String archiveId,
                                                 @RequestBody List<Map<String, Object>> agreements) {
        try {
            return travelArchiveService.saveAgreementList(archiveId, agreements);
        } catch (Exception e) {
            log.error("保存商旅档案合作协议失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("启用/禁用商旅档案")
    @PutMapping("/{archiveId}/status")
    public MyJsonBean updateTravelArchiveStatus(@PathVariable String archiveId,
                                              @RequestParam Integer isEnabled) {
        try {
            return travelArchiveService.updateStatus(archiveId, isEnabled);
        } catch (Exception e) {
            log.error("更新商旅档案状态失败", e);
            return MyJsonBean.errorData("状态更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取商旅档案评价记录")
    @GetMapping("/{archiveId}/evaluations")
    public MyJsonBean getTravelArchiveEvaluations(@PathVariable String archiveId) {
        try {
            return travelArchiveService.getEvaluations(archiveId);
        } catch (Exception e) {
            log.error("获取商旅档案评价记录失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存商旅档案评价")
    @PostMapping("/{archiveId}/evaluation")
    public MyJsonBean saveTravelArchiveEvaluation(@PathVariable String archiveId,
                                                 @RequestBody Map<String, Object> evaluation) {
        try {
            return travelArchiveService.saveEvaluation(archiveId, evaluation);
        } catch (Exception e) {
            log.error("保存商旅档案评价失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取商旅档案平均评分")
    @GetMapping("/{archiveId}/avg-score")
    public MyJsonBean getTravelArchiveAvgScore(@PathVariable String archiveId) {
        try {
            return travelArchiveService.getAvgScore(archiveId);
        } catch (Exception e) {
            log.error("获取商旅档案平均评分失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}
