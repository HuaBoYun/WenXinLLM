package com.huabo.cybermonitor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huabo.cybermonitor.entity.EnterpriseHierarchy;
import com.huabo.cybermonitor.service.IEnterpriseHierarchyService;
import com.huabo.cybermonitor.util.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 企业层级关系管理 Controller
 *
 * @author system
 * @since 2024-01-01
 */
@Tag(name="企业层级关系管理",description="企业层级关系管理")
@RestController
@RequestMapping("/v1/enterprise/hierarchy")
public class EnterpriseHierarchyController {

	private static final Logger log = LoggerFactory.getLogger(EnterpriseHierarchyController.class);

    @Autowired
    private IEnterpriseHierarchyService hierarchyService;

    @Operation(summary = "获取企业层级关系列表")
    @PostMapping("/list")
    public R<List<EnterpriseHierarchy>> getHierarchyList(@RequestBody Map<String, String> params) {
        try {
            String enterpriseId = params.get("enterpriseId");
            if (enterpriseId == null || enterpriseId.trim().isEmpty()) {
                return R.fail("企业ID不能为空");
            }
            
            List<EnterpriseHierarchy> hierarchyList = hierarchyService.getHierarchyByEnterpriseId(enterpriseId);
            return R.success(hierarchyList);
        } catch (Exception e) {
            log.error("获取企业层级关系列表失败", e);
            return R.fail("获取企业层级关系列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增企业层级关系")
    @PostMapping("/add")
    public R<String> addHierarchy(@RequestBody EnterpriseHierarchy hierarchy) {
        try {
            boolean success = hierarchyService.addHierarchy(hierarchy);
            if (success) {
                return R.success("新增企业层级关系成功");
            } else {
                return R.fail("新增企业层级关系失败");
            }
        } catch (Exception e) {
            log.error("新增企业层级关系失败", e);
            return R.fail("新增企业层级关系失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新企业层级关系")
    @PostMapping("/update")
    public R<String> updateHierarchy(@RequestBody EnterpriseHierarchy hierarchy) {
        try {
            if (hierarchy.getHierarchyId() == null || hierarchy.getHierarchyId().trim().isEmpty()) {
                return R.fail("层级关系ID不能为空");
            }
            
            boolean success = hierarchyService.updateHierarchy(hierarchy);
            if (success) {
                return R.success("更新企业层级关系成功");
            } else {
                return R.fail("更新企业层级关系失败");
            }
        } catch (Exception e) {
            log.error("更新企业层级关系失败", e);
            return R.fail("更新企业层级关系失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除企业层级关系")
    @PostMapping("/delete")
    public R<String> deleteHierarchy(@RequestBody Map<String, String> params) {
        try {
            String hierarchyId = params.get("hierarchyId");
            if (hierarchyId == null || hierarchyId.trim().isEmpty()) {
                return R.fail("层级关系ID不能为空");
            }
            
            boolean success = hierarchyService.deleteHierarchy(hierarchyId);
            if (success) {
                return R.success("删除企业层级关系成功");
            } else {
                return R.fail("删除企业层级关系失败");
            }
        } catch (Exception e) {
            log.error("删除企业层级关系失败", e);
            return R.fail("删除企业层级关系失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除企业层级关系")
    @PostMapping("/batch-delete")
    public R<String> batchDeleteHierarchy(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> hierarchyIds = params.get("hierarchyIds");
            if (hierarchyIds == null || hierarchyIds.isEmpty()) {
                return R.fail("层级关系ID列表不能为空");
            }
            
            boolean success = hierarchyService.batchDeleteHierarchy(hierarchyIds);
            if (success) {
                return R.success("批量删除企业层级关系成功");
            } else {
                return R.fail("批量删除企业层级关系失败");
            }
        } catch (Exception e) {
            log.error("批量删除企业层级关系失败", e);
            return R.fail("批量删除企业层级关系失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业的所有上级企业（向上穿透）")
    @PostMapping("/parent-hierarchy")
    public R<List<Map<String, Object>>> getParentHierarchy(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            Integer maxLevel = (Integer) params.get("maxLevel");
            
            if (enterpriseId == null || enterpriseId.trim().isEmpty()) {
                return R.fail("企业ID不能为空");
            }
            
            List<Map<String, Object>> parentHierarchy = hierarchyService.getParentHierarchy(enterpriseId, maxLevel);
            return R.success(parentHierarchy);
        } catch (Exception e) {
            log.error("获取企业上级层级关系失败", e);
            return R.fail("获取企业上级层级关系失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业的所有下级企业（向下穿透）")
    @PostMapping("/child-hierarchy")
    public R<List<Map<String, Object>>> getChildHierarchy(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            Integer maxLevel = (Integer) params.get("maxLevel");
            
            if (enterpriseId == null || enterpriseId.trim().isEmpty()) {
                return R.fail("企业ID不能为空");
            }
            
            List<Map<String, Object>> childHierarchy = hierarchyService.getChildHierarchy(enterpriseId, maxLevel);
            return R.success(childHierarchy);
        } catch (Exception e) {
            log.error("获取企业下级层级关系失败", e);
            return R.fail("获取企业下级层级关系失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业层级关系图数据")
    @PostMapping("/chart")
    public R<Map<String, Object>> getHierarchyChartData(@RequestBody Map<String, String> params) {
        try {
            String enterpriseId = params.get("enterpriseId");
            if (enterpriseId == null || enterpriseId.trim().isEmpty()) {
                return R.fail("企业ID不能为空");
            }
            
            Map<String, Object> chartData = hierarchyService.getHierarchyChartData(enterpriseId);
            return R.success(chartData);
        } catch (Exception e) {
            log.error("获取企业层级关系图数据失败", e);
            return R.fail("获取企业层级关系图数据失败：" + e.getMessage());
        }
    }

    @Operation(summary = "检查是否存在循环引用")
    @PostMapping("/check-circular")
    public R<Map<String, Object>> checkCircularReference(@RequestBody Map<String, String> params) {
        try {
            String parentEnterpriseId = params.get("parentEnterpriseId");
            String childEnterpriseId = params.get("childEnterpriseId");
            
            if (parentEnterpriseId == null || parentEnterpriseId.trim().isEmpty()) {
                return R.fail("母公司ID不能为空");
            }
            if (childEnterpriseId == null || childEnterpriseId.trim().isEmpty()) {
                return R.fail("子公司ID不能为空");
            }
            
            boolean hasCircular = hierarchyService.checkCircularReference(parentEnterpriseId, childEnterpriseId);

            Map<String, Object> result = new HashMap<>();
            result.put("hasCircular", hasCircular);
            result.put("message", hasCircular ? "存在循环引用" : "无循环引用");

            return R.success(result);
        } catch (Exception e) {
            log.error("检查循环引用失败", e);
            return R.fail("检查循环引用失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业的直接子公司数量")
    @PostMapping("/direct-children-count")
    public R<Map<String, Object>> getDirectChildrenCount(@RequestBody Map<String, String> params) {
        try {
            String enterpriseId = params.get("enterpriseId");
            if (enterpriseId == null || enterpriseId.trim().isEmpty()) {
                return R.fail("企业ID不能为空");
            }
            
            Integer count = hierarchyService.getDirectChildrenCount(enterpriseId);

            Map<String, Object> result = new HashMap<>();
            result.put("enterpriseId", enterpriseId);
            result.put("directChildrenCount", count);

            return R.success(result);
        } catch (Exception e) {
            log.error("获取企业直接子公司数量失败", e);
            return R.fail("获取企业直接子公司数量失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业的所有子公司数量（包括间接子公司）")
    @PostMapping("/all-children-count")
    public R<Map<String, Object>> getAllChildrenCount(@RequestBody Map<String, String> params) {
        try {
            String enterpriseId = params.get("enterpriseId");
            if (enterpriseId == null || enterpriseId.trim().isEmpty()) {
                return R.fail("企业ID不能为空");
            }
            
            Integer count = hierarchyService.getAllChildrenCount(enterpriseId);

            Map<String, Object> result = new HashMap<>();
            result.put("enterpriseId", enterpriseId);
            result.put("allChildrenCount", count);

            return R.success(result);
        } catch (Exception e) {
            log.error("获取企业所有子公司数量失败", e);
            return R.fail("获取企业所有子公司数量失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业的完整层级路径")
    @PostMapping("/hierarchy-path")
    public R<List<Map<String, Object>>> getHierarchyPath(@RequestBody Map<String, String> params) {
        try {
            String enterpriseId = params.get("enterpriseId");
            if (enterpriseId == null || enterpriseId.trim().isEmpty()) {
                return R.fail("企业ID不能为空");
            }
            
            List<Map<String, Object>> hierarchyPath = hierarchyService.getHierarchyPath(enterpriseId);
            return R.success(hierarchyPath);
        } catch (Exception e) {
            log.error("获取企业层级路径失败", e);
            return R.fail("获取企业层级路径失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业层级统计信息")
    @PostMapping("/statistics")
    public R<Map<String, Object>> getHierarchyStatistics() {
        try {
            Map<String, Object> statistics = hierarchyService.getHierarchyStatistics();
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取企业层级统计信息失败", e);
            return R.fail("获取企业层级统计信息失败：" + e.getMessage());
        }
    }

    @Operation(summary = "验证层级关系的有效性")
    @PostMapping("/validate")
    public R<Map<String, Object>> validateHierarchy(@RequestBody EnterpriseHierarchy hierarchy) {
        try {
            Map<String, Object> validateResult = hierarchyService.validateHierarchy(hierarchy);
            return R.success(validateResult);
        } catch (Exception e) {
            log.error("验证企业层级关系失败", e);
            return R.fail("验证企业层级关系失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业的层级级别")
    @PostMapping("/enterprise-level")
    public R<Map<String, Object>> getEnterpriseLevel(@RequestBody Map<String, String> params) {
        try {
            String enterpriseId = params.get("enterpriseId");
            if (enterpriseId == null || enterpriseId.trim().isEmpty()) {
                return R.fail("企业ID不能为空");
            }
            
            Integer level = hierarchyService.getEnterpriseLevel(enterpriseId);

            Map<String, Object> result = new HashMap<>();
            result.put("enterpriseId", enterpriseId);
            result.put("hierarchyLevel", level);

            return R.success(result);
        } catch (Exception e) {
            log.error("获取企业层级级别失败", e);
            return R.fail("获取企业层级级别失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据层级级别获取企业列表")
    @PostMapping("/enterprises-by-level")
    public R<List<Map<String, Object>>> getEnterprisesByLevel(@RequestBody Map<String, Integer> params) {
        try {
            Integer hierarchyLevel = params.get("hierarchyLevel");
            if (hierarchyLevel == null || hierarchyLevel < 0) {
                return R.fail("层级级别不能为空且必须大于等于0");
            }
            
            List<Map<String, Object>> enterprises = hierarchyService.getEnterprisesByLevel(hierarchyLevel);
            return R.success(enterprises);
        } catch (Exception e) {
            log.error("根据层级级别获取企业列表失败", e);
            return R.fail("根据层级级别获取企业列表失败：" + e.getMessage());
        }
    }
}
