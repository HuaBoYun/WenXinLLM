package com.financial.sharing.controller;

import com.financial.sharing.entity.TblTravelStandard;
import com.financial.sharing.entity.TblTravelStandardDetail;
import com.financial.sharing.service.TblTravelStandardService;
import com.financial.sharing.service.TblTravelStandardDetailService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 差旅标准管理控制器
 * 
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@Api(tags = "差旅标准管理")
@RestController
@RequestMapping("/travel-standards")
@CrossOrigin
public class TravelStandardController {

    @Autowired
    private TblTravelStandardService travelStandardService;
    
    @Autowired
    private TblTravelStandardDetailService detailService;

    @ApiOperation("查询差旅标准列表")
    @GetMapping
    public MyJsonBean getTravelStandardList(PageableParam pageableParam,
                                           @RequestParam(required = false) String standardName,
                                           @RequestParam(required = false) String cityLevel,
                                           @RequestParam(required = false) String positionLevel,
                                           @RequestParam(required = false) Boolean isEnabled) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("pageNum", pageableParam.getPageNum());
            params.put("pageSize", pageableParam.getSize());
            params.put("standardName", standardName);
            params.put("cityLevel", cityLevel);
            params.put("positionLevel", positionLevel);
            if (isEnabled != null) {
                params.put("isEnabled", isEnabled ? 1 : 0);
            }
            
            PageResult<TblTravelStandard> pageResult = travelStandardService.getTravelStandardPage(params);
            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("查询差旅标准列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存差旅标准")
    @PostMapping
    public MyJsonBean saveTravelStandard(@RequestBody TblTravelStandard travelStandard) {
        try {
            if (travelStandard.getStandardId() == null || travelStandard.getStandardId().isEmpty()) {
                // 生成不带连字符的UUID（32字符），适配VARCHAR(32)字段
                travelStandard.setStandardId(UUID.randomUUID().toString().replace("-", ""));
                travelStandard.setCreateTime(LocalDateTime.now());
                travelStandard.setIsEnabled(1);
            } else {
                travelStandard.setUpdateTime(LocalDateTime.now());
            }

            travelStandardService.saveOrUpdate(travelStandard);
            return MyJsonBean.successData("保存成功", travelStandard.getStandardId());
        } catch (Exception e) {
            log.error("保存差旅标准失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除差旅标准")
    @DeleteMapping("/{standardId}")
    public MyJsonBean deleteTravelStandard(@PathVariable String standardId) {
        try {
            travelStandardService.removeById(standardId);
            detailService.deleteByStandardId(standardId);
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除差旅标准失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取差旅标准详情")
    @GetMapping("/{standardId}")
    public MyJsonBean getTravelStandardDetail(@PathVariable String standardId) {
        try {
            TblTravelStandard standard = travelStandardService.getById(standardId);
            if (standard == null) {
                return MyJsonBean.errorData("标准不存在");
            }
            return MyJsonBean.successData("查询成功", standard);
        } catch (Exception e) {
            log.error("获取差旅标准详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取差旅标准明细")
    @GetMapping("/{standardId}/details")
    public MyJsonBean getTravelStandardDetails(@PathVariable String standardId) {
        try {
            // 移除UUID中的连字符，确保与数据库中的格式一致
            if (standardId != null && standardId.contains("-")) {
                standardId = standardId.replace("-", "");
            }
            List<TblTravelStandardDetail> details = detailService.getByStandardId(standardId);
            return MyJsonBean.successData("查询成功", details);
        } catch (Exception e) {
            log.error("获取差旅标准明细失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存差旅标准明细（批量）")
    @PostMapping("/{standardId}/details")
    public MyJsonBean saveTravelStandardDetails(@PathVariable String standardId,
                                               @RequestBody List<TblTravelStandardDetail> details) {
        try {
            // 移除UUID中的连字符
            if (standardId != null && standardId.contains("-")) {
                standardId = standardId.replace("-", "");
            }

            for (TblTravelStandardDetail detail : details) {
                if (detail.getDetailId() == null || detail.getDetailId().isEmpty()) {
                    detail.setDetailId(UUID.randomUUID().toString().replace("-", ""));
                    detail.setCreateTime(LocalDateTime.now());
                }
                detail.setStandardId(standardId);
            }

            detailService.saveBatchDetails(details);
            return MyJsonBean.successData("保存明细成功");
        } catch (Exception e) {
            log.error("保存差旅标准明细失败", e);
            return MyJsonBean.errorData("保存明细失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存差旅标准明细（单个）")
    @PostMapping("/details")
    public MyJsonBean saveTravelStandardDetail(@RequestBody TblTravelStandardDetail detail) {
        try {
            // 移除UUID中的连字符，确保长度不超过32
            if (detail.getDetailId() == null || detail.getDetailId().isEmpty()) {
                detail.setDetailId(UUID.randomUUID().toString().replace("-", ""));
                detail.setCreateTime(LocalDateTime.now());
            }

            // 确保standardId也移除连字符
            if (detail.getStandardId() != null && detail.getStandardId().contains("-")) {
                detail.setStandardId(detail.getStandardId().replace("-", ""));
            }

            detailService.saveOrUpdate(detail);
            return MyJsonBean.successData("保存成功", detail);
        } catch (Exception e) {
            log.error("保存差旅标准明细失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除差旅标准明细")
    @DeleteMapping("/details/{detailId}")
    public MyJsonBean deleteTravelStandardDetail(@PathVariable String detailId) {
        try {
            detailService.removeById(detailId);
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除差旅标准明细失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("启用/禁用差旅标准")
    @PutMapping("/{standardId}/status")
    public MyJsonBean updateTravelStandardStatus(@PathVariable String standardId, 
                                                @RequestParam Boolean isEnabled) {
        try {
            travelStandardService.updateStandardStatus(standardId, isEnabled ? 1 : 0);
            return MyJsonBean.successData("状态更新成功");
        } catch (Exception e) {
            log.error("更新差旅标准状态失败", e);
            return MyJsonBean.errorData("状态更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("复制差旅标准")
    @PostMapping("/{standardId}/copy")
    public MyJsonBean copyTravelStandard(@PathVariable String standardId,
                                       @RequestBody Map<String, Object> copyInfo) {
        try {
            String newStandardCode = (String) copyInfo.get("standardCode");
            String newStandardName = (String) copyInfo.get("standardName");
            
            String newStandardId = travelStandardService.copyStandard(standardId, newStandardCode, newStandardName);
            return MyJsonBean.successData("复制成功", newStandardId);
        } catch (Exception e) {
            log.error("复制差旅标准失败", e);
            return MyJsonBean.errorData("复制失败: " + e.getMessage());
        }
    }
}

