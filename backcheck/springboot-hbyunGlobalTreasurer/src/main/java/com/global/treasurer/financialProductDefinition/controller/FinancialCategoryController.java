package com.global.treasurer.financialProductDefinition.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.global.treasurer.financialProductDefinition.entity.TblFinancialCategory;
import com.global.treasurer.financialProductDefinition.service.TblFinancialCategoryService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/financial/product-definition/financial-category")
@Api(tags = "金融分类管理")
public class FinancialCategoryController {
    private static final Logger log = LoggerFactory.getLogger(FinancialCategoryController.class);

    @Autowired
    private TblFinancialCategoryService categoryService;
    @Resource
    private UserProvider userProvider;

    @PostMapping("/getList")
    @ApiOperation("分页查询金融分类列表")
    public String getList(@ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
                          @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
                          @ApiParam("分类编码") @RequestParam(required = false) String categoryCode,
                          @ApiParam("分类名称") @RequestParam(required = false) String categoryName,
                          @ApiParam("父分类ID") @RequestParam(required = false) Long parentCategoryId,
                          @ApiParam("是否启用") @RequestParam(required = false) Integer isEnabled) {
        try {
            log.info("=== 金融分类查询 === pageNo={}, pageSize={}, categoryCode={}, categoryName={}, parentCategoryId={}, isEnabled={}",
                    pageNo, pageSize, categoryCode, categoryName, parentCategoryId, isEnabled);

            Long orgId = getOrgId();
            log.info("=== 查询orgId === {}", orgId);

            IPage<TblFinancialCategory> result = categoryService.getPage(pageNo, pageSize, categoryCode, categoryName, isEnabled, orgId, parentCategoryId);

            log.info("=== 查询结果 === total={}, records.size={}", result.getTotal(), result.getRecords().size());

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            String response = new JsonBean(1, "查询成功", data).toString();
            log.info("=== 返回响应 === {}", response);
            return response;
        } catch (Exception e) {
            log.error("查询金融分类列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getById")
    @ApiOperation("根据ID查询金融分类详情")
    public String getById(@ApiParam("ID") @RequestParam Long ID) {
        try {
            TblFinancialCategory entity = categoryService.getDetail(ID);
            return entity == null ? JsonBean.error("数据不存在") : new JsonBean(1, "查询成功", entity).toString();
        } catch (Exception e) {
            log.error("查询金融分类详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    @ApiOperation("新增金融分类")
    public String create(HttpServletRequest request) {
        try {
            log.info("=== 创建金融分类 ===");
            log.info("ContentType: {}", request.getContentType());

            // 读取请求体中的表单数据
            StringBuilder requestBody = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
            } catch (Exception e) {
                log.warn("读取请求体失败: {}", e.getMessage());
            }

            String body = requestBody.toString();
            log.info("请求体内容: {}", body);

            // 解析 form-urlencoded 格式的数据
            Map<String, String> formData = new HashMap<>();
            if (body != null && !body.isEmpty()) {
                String[] pairs = body.split("&");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=", 2);
                    if (keyValue.length == 2) {
                        try {
                            String key = keyValue[0];
                            String value = java.net.URLDecoder.decode(keyValue[1], "UTF-8");
                            formData.put(key, value);
                            log.info("解析参数: {} = {}", key, value);
                        } catch (Exception e) {
                            log.warn("解析参数失败: {}", pair);
                        }
                    }
                }
            }

            // 从解析的数据中获取参数
            String categoryCode = formData.get("categoryCode");
            String categoryName = formData.get("categoryName");
            String parentIdStr = formData.get("parentId");
            String description = formData.get("description");
            String isEnabledStr = formData.get("isEnabled");

            log.info("最终参数 - categoryCode: {}, categoryName: {}, parentIdStr: {}, description: {}, isEnabledStr: {}",
                    categoryCode, categoryName, parentIdStr, description, isEnabledStr);

            // 创建实体对象
            TblFinancialCategory entity = new TblFinancialCategory();
            if (categoryCode != null && !categoryCode.isEmpty()) {
                entity.setCategoryCode(categoryCode);
            }
            if (categoryName != null && !categoryName.isEmpty()) {
                entity.setCategoryName(categoryName);
            }
            if (parentIdStr != null && !parentIdStr.isEmpty()) {
                entity.setParentId(Long.parseLong(parentIdStr));
            }
            if (description != null && !description.isEmpty()) {
                entity.setDescription(description);
            }
            if (isEnabledStr != null && !isEnabledStr.isEmpty()) {
                entity.setIsEnabled(Integer.parseInt(isEnabledStr));
            }

            entity.setOrgId(getOrgId());
            TblFinancialCategory result = categoryService.create(entity, getCurrentUser());
            return new JsonBean(1, "创建成功", result).toString();
        } catch (Exception e) {
            log.error("创建金融分类失败", e);
            return JsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation("修改金融分类")
    public String update(HttpServletRequest request) {
        try {
            log.info("=== 更新金融分类 ===");
            log.info("ContentType: {}", request.getContentType());

            // 读取请求体中的表单数据
            StringBuilder requestBody = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
            } catch (Exception e) {
                log.warn("读取请求体失败: {}", e.getMessage());
            }

            String body = requestBody.toString();
            log.info("请求体内容: {}", body);

            // 解析 form-urlencoded 格式的数据
            Map<String, String> formData = new HashMap<>();
            if (body != null && !body.isEmpty()) {
                String[] pairs = body.split("&");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=", 2);
                    if (keyValue.length == 2) {
                        try {
                            String key = keyValue[0];
                            String value = java.net.URLDecoder.decode(keyValue[1], "UTF-8");
                            formData.put(key, value);
                            log.info("解析参数: {} = {}", key, value);
                        } catch (Exception e) {
                            log.warn("解析参数失败: {}", pair);
                        }
                    }
                }
            }

            // 从解析的数据中获取参数
            String categoryIdStr = formData.get("categoryId");
            String categoryCode = formData.get("categoryCode");
            String categoryName = formData.get("categoryName");
            String parentIdStr = formData.get("parentId");
            String description = formData.get("description");
            String isEnabledStr = formData.get("isEnabled");
            String orgIdStr = formData.get("orgId");
            String categoryLevel = formData.get("categoryLevel");
            String categoryPath = formData.get("categoryPath");
            String sortOrderStr = formData.get("sortOrder");

            log.info("最终参数 - categoryId: {}, categoryCode: {}, categoryName: {}, parentId: {}, description: {}, isEnabled: {}",
                    categoryIdStr, categoryCode, categoryName, parentIdStr, description, isEnabledStr);

            // 验证必填字段
            if (categoryIdStr == null || categoryIdStr.isEmpty()) {
                log.warn("!!! 警告：categoryId为空，无法更新记录 !!!");
                return JsonBean.error("更新失败：categoryId不能为空");
            }

            // 创建实体对象
            TblFinancialCategory entity = new TblFinancialCategory();
            entity.setCategoryId(Long.parseLong(categoryIdStr));
            if (categoryCode != null && !categoryCode.isEmpty()) {
                entity.setCategoryCode(categoryCode);
            }
            if (categoryName != null && !categoryName.isEmpty()) {
                entity.setCategoryName(categoryName);
            }
            if (parentIdStr != null && !parentIdStr.isEmpty()) {
                entity.setParentId(Long.parseLong(parentIdStr));
            }
            if (description != null && !description.isEmpty()) {
                entity.setDescription(description);
            }
            if (isEnabledStr != null && !isEnabledStr.isEmpty()) {
                entity.setIsEnabled(Integer.parseInt(isEnabledStr));
            }
            if (orgIdStr != null && !orgIdStr.isEmpty()) {
                entity.setOrgId(Long.parseLong(orgIdStr));
            }
            if (categoryLevel != null && !categoryLevel.isEmpty()) {
                entity.setCategoryLevel(categoryLevel);
            }
            if (categoryPath != null && !categoryPath.isEmpty()) {
                entity.setCategoryPath(categoryPath);
            }
            if (sortOrderStr != null && !sortOrderStr.isEmpty()) {
                entity.setSortOrder(Integer.parseInt(sortOrderStr));
            }

            boolean result = categoryService.update(entity, getCurrentUser());
            return result ? new JsonBean(1, "更新成功", null).toString() : JsonBean.error("更新失败");
        } catch (Exception e) {
            log.error("更新金融分类失败", e);
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation("删除金融分类")
    public String delete(@ApiParam("ID") @RequestParam Long ID) {
        try {
            return categoryService.delete(ID) ? new JsonBean(1, "删除成功", null).toString() : JsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除金融分类失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchDelete")
    @ApiOperation("批量删除金融分类")
    public String batchDelete(@ApiParam("ID列表") @RequestParam String IDs) {
        try {
            String[] idArray = IDs.split(",");
            List<Long> idList = new java.util.ArrayList<>();
            for (String id : idArray) idList.add(Long.parseLong(id.trim()));
            return categoryService.batchDelete(idList) ? new JsonBean(1, "批量删除成功", null).toString() : JsonBean.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除金融分类失败", e);
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/updateStatus")
    @ApiOperation("更新金融分类状态")
    public String updateStatus(@ApiParam("ID") @RequestParam Long ID, @ApiParam("状态") @RequestParam Integer isEnabled) {
        try {
            return categoryService.updateStatus(ID, isEnabled, getCurrentUser()) ? new JsonBean(1, "状态更新成功", null).toString() : JsonBean.error("状态更新失败");
        } catch (Exception e) {
            log.error("更新金融分类状态失败", e);
            return JsonBean.error("状态更新失败: " + e.getMessage());
        }
    }

    @GetMapping("/getEnabledList")
    @ApiOperation("获取启用的金融分类列表")
    public String getEnabledList() {
        try {
            return new JsonBean(1, "查询成功", categoryService.getEnabledList(getOrgId())).toString();
        } catch (Exception e) {
            log.error("获取启用的金融分类列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getTree")
    @ApiOperation("获取金融分类树形结构")
    public String getTree(@ApiParam("组织ID") @RequestParam(required = false) Long orgId) {
        try {
            Long queryOrgId = orgId != null ? orgId : getOrgId();
            log.info("=== 获取金融分类树 === orgId={}", queryOrgId);

            List<TblFinancialCategory> tree = categoryService.getTree(queryOrgId);
            log.info("=== 树形结构查询结果 === size={}", tree.size());

            String response = new JsonBean(1, "查询成功", tree).toString();
            log.info("=== 返回树形响应 === {}", response);
            return response;
        } catch (Exception e) {
            log.error("获取金融分类树形结构失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/checkCodeUnique")
    @ApiOperation("检查金融分类编码唯一性")
    public String checkCodeUnique(@ApiParam("编码") @RequestParam String categoryCode, @ApiParam("排除ID") @RequestParam(required = false) Long excludeId) {
        try {
            boolean isUnique = categoryService.checkCodeUnique(categoryCode, excludeId);
            return new JsonBean(isUnique ? 1 : 0, isUnique ? "编码可用" : "编码已存在", null).toString();
        } catch (Exception e) {
            log.error("检查编码唯一性失败", e);
            return JsonBean.error("检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/getByParentId")
    @ApiOperation("根据父ID获取子分类列表")
    public String getByParentId(@ApiParam("父ID") @RequestParam(required = false) Long parentId) {
        try {
            return new JsonBean(1, "查询成功", categoryService.getByParentId(parentId, getOrgId())).toString();
        } catch (Exception e) {
            log.error("根据父ID获取子分类列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/sort")
    @ApiOperation("排序")
    public String sort(@RequestBody List<TblFinancialCategory> list) {
        try {
            boolean result = categoryService.sort(list);
            return result ? new JsonBean(1, "排序成功", null).toString() : JsonBean.error("排序失败");
        } catch (Exception e) {
            log.error("排序失败", e);
            return JsonBean.error("排序失败: " + e.getMessage());
        }
    }


    private Long getOrgId() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                Long orgId = loginStaff.getCurrentOrg().getOrgid().longValue();
                log.info("当前用户orgId: {}", orgId);
                // 强制返回1,忽略用户的实际orgId
                log.info("强制使用orgId=1");
                return 1L;
            }
        } catch (Exception e) { log.error("获取组织ID失败", e); }
        log.warn("未获取到用户orgId,使用默认值1");
        return 1L;
    }

    private String getCurrentUser() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            return loginStaff != null ? loginStaff.getUsername() : "system";
        } catch (Exception e) { return "system"; }
    }
}

