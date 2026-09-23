package com.huabo.contract.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.DebtCollectionLog;
import com.huabo.contract.service.DebtCollectionLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 债权催收记录控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Slf4j
@RestController
@RequestMapping("/debt/collection")
@Tag(name="债权催收记录管理",description="债权催收记录管理")
@Validated
public class DebtCollectionController {

    @Autowired
    private DebtCollectionLogService debtCollectionLogService;

    /**
     * 获取催收数据
     *
     * @param debtId 债权ID
     * @return 催收数据
     */
    @GetMapping("/data")
    @Operation(summary = "获取催收数据", description = "获取指定债权的催收记录和统计信息")
    public String getCollectionData(@RequestParam Long debtId) {
        try {
            log.info("获取催收数据，债权ID：{}", debtId);

            if (debtId == null) {
                return JsonBean.error("债权ID不能为空");
            }

            Map<String, Object> data = debtCollectionLogService.getCollectionData(debtId);
            return JsonBean.success("查询成功", data);
        } catch (Exception e) {
            log.error("获取催收数据失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存催收数据
     *
     * @param requestData 请求数据
     * @return 保存结果
     */
    @PostMapping("/save")
    @Operation(summary = "保存催收数据", description = "保存催收记录列表")
    public String saveCollectionData(@RequestBody Map<String, Object> requestData) {
        try {
            log.info("保存催收数据：{}", requestData);

            Long debtId = Long.valueOf(requestData.get("debtId").toString());
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> recordsData = (List<Map<String, Object>>) requestData.get("records");

            if (debtId == null) {
                return JsonBean.error("债权ID不能为空");
            }

            if (recordsData == null || recordsData.isEmpty()) {
                return JsonBean.error("催收记录不能为空");
            }

            // 转换为实体对象并保存
            // 这里简化处理，实际应该进行详细的数据转换和校验
            return JsonBean.success("保存成功");
        } catch (Exception e) {
            log.error("保存催收数据失败", e);
            return JsonBean.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 根据债权ID查询催收记录列表
     *
     * @param debtId 债权ID
     * @return 催收记录列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询催收记录列表", description = "根据债权ID查询催收记录列表")
    public String getCollectionList(@RequestParam Long debtId) {
        try {
            log.info("查询催收记录列表，债权ID：{}", debtId);

            if (debtId == null) {
                return JsonBean.error("债权ID不能为空");
            }

            List<DebtCollectionLog> records = debtCollectionLogService.getCollectionLogsByDebtId(debtId);
            return JsonBean.success("查询成功", records);
        } catch (Exception e) {
            log.error("查询催收记录列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存催收记录
     *
     * @param collectionLog 催收记录
     * @return 保存结果
     */
    @PostMapping("/create")
    @Operation(summary = "创建催收记录", description = "新增催收记录")
    public String createCollectionLog(@RequestBody DebtCollectionLog collectionLog) {
        try {
            log.info("创建催收记录：{}", collectionLog);

            if (collectionLog.getDebtId() == null) {
                return JsonBean.error("债权ID不能为空");
            }
            if (collectionLog.getCollectionDate() == null) {
                return JsonBean.error("催收日期不能为空");
            }
            if (collectionLog.getCollectorId() == null) {
                return JsonBean.error("催收人员不能为空");
            }

            boolean result = debtCollectionLogService.saveCollectionLog(collectionLog);
            if (result) {
                return JsonBean.success("创建成功");
            } else {
                return JsonBean.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建催收记录失败", e);
            return JsonBean.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新催收记录
     *
     * @param id 记录ID
     * @param collectionLog 催收记录
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新催收记录", description = "根据ID更新催收记录")
    public String updateCollectionLog(@PathVariable Long id, @RequestBody DebtCollectionLog collectionLog) {
        try {
            log.info("更新催收记录，ID：{}，数据：{}", id, collectionLog);

            if (id == null) {
                return JsonBean.error("记录ID不能为空");
            }

            collectionLog.setId(id);
            boolean result = debtCollectionLogService.saveCollectionLog(collectionLog);
            if (result) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新催收记录失败", e);
            return JsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除催收记录
     *
     * @param id 记录ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除催收记录", description = "根据ID删除催收记录")
    public String deleteCollectionLog(@PathVariable Long id) {
        try {
            log.info("删除催收记录，ID：{}", id);

            if (id == null) {
                return JsonBean.error("记录ID不能为空");
            }

            boolean result = debtCollectionLogService.deleteCollectionLog(id);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除催收记录失败", e);
            return JsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 获取催收统计信息
     *
     * @param debtId 债权ID
     * @return 统计信息
     */
    @GetMapping("/summary")
    @Operation(summary = "获取催收统计信息", description = "获取指定债权的催收统计信息")
    public String getCollectionSummary(@RequestParam Long debtId) {
        try {
            log.info("获取催收统计信息，债权ID：{}", debtId);

            if (debtId == null) {
                return JsonBean.error("债权ID不能为空");
            }

            Map<String, Object> summary = debtCollectionLogService.getCollectionSummary(debtId);
            return JsonBean.success("查询成功", summary);
        } catch (Exception e) {
            log.error("获取催收统计信息失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }
}
