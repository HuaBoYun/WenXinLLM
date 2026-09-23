package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.DecisionModel;
import com.global.treasurer.service.IDecisionModelService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.List;

/**
 * 决策模型控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
@RestController
@RequestMapping({"/financial/decision-support/models", "/xjgl/decision-support/models", "/centralaudit/decision-support/models", "/decision-support/models", "/decisionModel"})
@Api(tags = "决策模型管理")
public class DecisionModelController {
    private static final Logger log = LoggerFactory.getLogger(DecisionModelController.class);

    @Resource
    private IDecisionModelService decisionModelService;

    @Resource
    private UserProvider userProvider;

    @PostMapping("/page")
    @ApiOperation("获取决策模型列表(POST-FormData)")
    public String getModelListPost(
            @RequestParam(required = false) Map<String, Object> params,
            HttpServletResponse response) {

        try {
            if (params == null) params = new HashMap<>();

            log.info("========== 决策模型查询接口(POST-FormData) ==========");
            log.info("接收参数 - params: {}", params);

            // 从params中提取分页参数
            Integer pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) :
                           (params.get("pageNo") != null ? Integer.parseInt(params.get("pageNo").toString()) :
                           (params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1));
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) :
                             (params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) : 10);

            String modelCode = params.get("modelCode") != null ? params.get("modelCode").toString() : null;
            String modelName = params.get("modelName") != null ? params.get("modelName").toString() : null;
            String modelType = params.get("modelType") != null ? params.get("modelType").toString() : null;
            String algorithmType = params.get("algorithmType") != null ? params.get("algorithmType").toString() : null;
            String modelStatus = params.get("modelStatus") != null ? params.get("modelStatus").toString() : null;

            log.info("解析后参数 - pageNum: {}, pageSize: {}", pageNum, pageSize);
            log.info("解析后参数 - modelName: {}, modelType: {}, modelStatus: {}", modelName, modelType, modelStatus);

            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 构建查询参数
            Map<String, Object> queryParams = new HashMap<>();
            if (StringUtils.hasText(modelCode)) {
                queryParams.put("modelCode", modelCode);
            }
            if (StringUtils.hasText(modelName)) {
                queryParams.put("modelName", modelName);
            }
            if (StringUtils.hasText(modelType)) {
                queryParams.put("modelType", modelType);
            }
            if (StringUtils.hasText(algorithmType)) {
                queryParams.put("algorithmType", algorithmType);
            }
            if (StringUtils.hasText(modelStatus)) {
                queryParams.put("modelStatus", modelStatus);
            }

            log.info("构建的queryParams: {}", queryParams);

            // 添加组织ID（已注释 - 允许查看所有数据）
            // TblStaffUtil user = userProvider.get();
            // if (user != null && user.getCurrentOrg() != null) {
            //     queryParams.put("orgId", getOrgId(user.getCurrentOrg()));
            // }

            // 分页查询
            Page<DecisionModel> page = new Page<>(pageNum, pageSize);
            IPage<DecisionModel> result = decisionModelService.selectPage(page, queryParams);

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            log.info("查询结果 - 当前页: {}, 每页大小: {}, 总记录数: {}, 当前页记录数: {}",
                    result.getCurrent(), result.getSize(), result.getTotal(), result.getRecords().size());

            return new JsonBean(1, "查询成功", data).toString();

        } catch (Exception e) {
            log.error("获取决策模型列表失败", e);
            return JsonBean.error("获取决策模型列表失败: " + e.getMessage());
        }
    }

    @GetMapping({"/list", "/page"})
    @ApiOperation("获取决策模型列表(GET)")
    public String getModelList(
            @ApiParam(value = "页码", example = "1") @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "模型编号") @RequestParam(required = false) String modelCode,
            @ApiParam(value = "模型名称") @RequestParam(required = false) String modelName,
            @ApiParam(value = "模型类型") @RequestParam(required = false) String modelType,
            @ApiParam(value = "算法类型") @RequestParam(required = false) String algorithmType,
            @ApiParam(value = "模型状态") @RequestParam(required = false) String modelStatus,
            HttpServletResponse response) {

        try {
            // 打印接收到的参数
            log.info("========== 决策模型查询接口 ==========");
            log.info("接收参数 - pageNo: {}, pageSize: {}", pageNum, pageSize);
            log.info("接收参数 - modelCode: {}", modelCode);
            log.info("接收参数 - modelName: {}", modelName);
            log.info("接收参数 - modelType: {}", modelType);
            log.info("接收参数 - algorithmType: {}", algorithmType);
            log.info("接收参数 - modelStatus: {}", modelStatus);

            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            if (StringUtils.hasText(modelCode)) {
                params.put("modelCode", modelCode);
                log.info("添加查询参数 - modelCode: {}", modelCode);
            }
            if (StringUtils.hasText(modelName)) {
                params.put("modelName", modelName);
                log.info("添加查询参数 - modelName: {}", modelName);
            }
            if (StringUtils.hasText(modelType)) {
                params.put("modelType", modelType);
                log.info("添加查询参数 - modelType: {}", modelType);
            }
            if (StringUtils.hasText(algorithmType)) {
                params.put("algorithmType", algorithmType);
                log.info("添加查询参数 - algorithmType: {}", algorithmType);
            }
            if (StringUtils.hasText(modelStatus)) {
                params.put("modelStatus", modelStatus);
                log.info("添加查询参数 - modelStatus: {}", modelStatus);
            }

            log.info("构建的params: {}", params);

            // 添加组织ID（已注释 - 允许查看所有数据）
            // TblStaffUtil user = userProvider.get();
            // if (user != null && user.getCurrentOrg() != null) {
            //     params.put("orgId", getOrgId(user.getCurrentOrg()));
            // }

            // 分页查询
            Page<DecisionModel> page = new Page<>(pageNum, pageSize);
            IPage<DecisionModel> result = decisionModelService.selectPage(page, params);

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            log.info("查询结果 - 当前页: {}, 每页大小: {}, 总记录数: {}, 当前页记录数: {}",
                    result.getCurrent(), result.getSize(), result.getTotal(), result.getRecords().size());

            return new JsonBean(1, "查询成功", data).toString();

        } catch (Exception e) {
            log.error("获取决策模型列表失败", e);
            return JsonBean.error("获取决策模型列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail")
    @ApiOperation("获取决策模型详情")
    public String getModelDetail(
            @ApiParam(value = "模型ID", required = true) @RequestParam Long modelId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            DecisionModel model = decisionModelService.getById(modelId);
            if (model == null) {
                return JsonBean.error("决策模型不存在");
            }

            return new JsonBean(1, "查询成功", model).toString();

        } catch (Exception e) {
            log.error("获取决策模型详情失败，modelId: {}", modelId, e);
            return JsonBean.error("获取决策模型详情失败: " + e.getMessage());
        }
    }

    @GetMapping("/{modelId}")
    @ApiOperation("获取决策模型详情(RESTful)")
    public String getModelById(
            @ApiParam(value = "模型ID", required = true) @PathVariable Long modelId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            DecisionModel model = decisionModelService.getById(modelId);
            if (model == null) {
                return JsonBean.error("决策模型不存在");
            }

            return new JsonBean(1, "查询成功", model).toString();

        } catch (Exception e) {
            log.error("获取决策模型详情失败，modelId: {}", modelId, e);
            return JsonBean.error("获取决策模型详情失败: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    @ApiOperation("创建决策模型")
    public String createModel(DecisionModel model, HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 参数校验
            if (!StringUtils.hasText(model.getModelName())) {
                return JsonBean.error("模型名称不能为空");
            }
            if (!StringUtils.hasText(model.getModelType())) {
                return JsonBean.error("模型类型不能为空");
            }
            if (!StringUtils.hasText(model.getAlgorithmType())) {
                return JsonBean.error("算法类型不能为空");
            }

            // 自动生成模型编号（如果没有提供）
            if (!StringUtils.hasText(model.getModelCode())) {
                model.setModelCode("DM" + System.currentTimeMillis());
            }

            // 设置默认值
            if (model.getModelStatus() == null) {
                model.setModelStatus("INACTIVE");
            }
            model.setCreateTime(java.time.LocalDateTime.now());
            model.setUpdateTime(java.time.LocalDateTime.now());
            model.setDelFlag("0");

            // 设置创建人
            TblStaffUtil user = userProvider.get();
            if (user != null) {
                model.setCreateBy(getStaffId(user));
                model.setUpdateBy(getStaffId(user));
                if (user.getCurrentOrg() != null) {
                    model.setOrgId(getOrgId(user.getCurrentOrg()));
                }
            }

            boolean success = decisionModelService.save(model);
            if (success) {
                return JsonBean.success("创建成功");
            } else {
                return JsonBean.error("创建失败");
            }

        } catch (Exception e) {
            log.error("创建决策模型失败", e);
            return JsonBean.error("创建决策模型失败: " + e.getMessage());
        }
    }

    @PostMapping
    @ApiOperation("创建决策模型(RESTful)")
    public String createModelRestful(@FlexibleRequestBody DecisionModel model, HttpServletResponse response) {
        return createModel(model, response);
    }

    @PostMapping("/update")
    @ApiOperation("更新决策模型")
    public String updateModel(@FlexibleRequestBody DecisionModel model, HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (model.getModelId() == null) {
                return JsonBean.error("模型ID不能为空");
            }

            // 检查是否存在
            DecisionModel existing = decisionModelService.getById(model.getModelId());
            if (existing == null) {
                return JsonBean.error("决策模型不存在");
            }

            model.setUpdateTime(java.time.LocalDateTime.now());

            // 设置更新人
            TblStaffUtil user = userProvider.get();
            if (user != null) {
                model.setUpdateBy(getStaffId(user));
            }

            boolean success = decisionModelService.updateById(model);
            if (success) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }

        } catch (Exception e) {
            log.error("更新决策模型失败", e);
            return JsonBean.error("更新决策模型失败: " + e.getMessage());
        }
    }

    @PutMapping
    @ApiOperation("更新决策模型(RESTful)")
    public String updateModelRestful(@FlexibleRequestBody DecisionModel model, HttpServletResponse response) {
        return updateModel(model, response);
    }

    @PostMapping("/delete")
    @ApiOperation("删除决策模型")
    public String deleteModel(
            @ApiParam(value = "模型ID", required = true) @RequestParam Long modelId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查是否存在
            DecisionModel existing = decisionModelService.getById(modelId);
            if (existing == null) {
                return JsonBean.error("决策模型不存在");
            }

            // 逻辑删除
            existing.setDelFlag("1");
            existing.setUpdateTime(java.time.LocalDateTime.now());

            boolean success = decisionModelService.updateById(existing);
            if (success) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }

        } catch (Exception e) {
            log.error("删除决策模型失败，modelId: {}", modelId, e);
            return JsonBean.error("删除决策模型失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{modelIds}")
    @ApiOperation("删除决策模型(RESTful)")
    public String deleteModelRestful(
            @ApiParam(value = "模型ID(多个用逗号分隔)", required = true) @PathVariable String modelIds,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            String[] ids = modelIds.split(",");
            for (String id : ids) {
                Long modelId = Long.parseLong(id.trim());

                // 检查是否存在
                DecisionModel existing = decisionModelService.getById(modelId);
                if (existing != null) {
                    // 逻辑删除
                    existing.setDelFlag("1");
                    existing.setUpdateTime(java.time.LocalDateTime.now());
                    decisionModelService.updateById(existing);
                }
            }

            return JsonBean.success("删除成功");

        } catch (Exception e) {
            log.error("删除决策模型失败，modelIds: {}", modelIds, e);
            return JsonBean.error("删除决策模型失败: " + e.getMessage());
        }
    }

    @PostMapping("/train")
    @ApiOperation("训练决策模型")
    public String trainModel(
            @ApiParam(value = "模型ID", required = true) @RequestParam Long modelId,
            @ApiParam(value = "训练数据(JSON格式)") @RequestParam(required = false) String trainingData,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            boolean success = decisionModelService.trainModel(modelId, trainingData);
            if (success) {
                return JsonBean.success("模型训练已启动");
            } else {
                return JsonBean.error("模型训练启动失败");
            }

        } catch (Exception e) {
            log.error("训练决策模型失败，modelId: {}", modelId, e);
            return JsonBean.error("训练决策模型失败: " + e.getMessage());
        }
    }

    @PostMapping("/{modelId}/train")
    @ApiOperation("训练决策模型(RESTful)")
    public String trainModelRestful(
            @ApiParam(value = "模型ID", required = true) @PathVariable Long modelId,
            @ApiParam(value = "训练数据(JSON格式)") @RequestParam(required = false) String trainingData,
            HttpServletResponse response) {
        return trainModel(modelId, trainingData, response);
    }

    @PostMapping("/activate")
    @ApiOperation("激活决策模型")
    public String activateModel(
            @ApiParam(value = "模型ID", required = true) @RequestParam Long modelId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            boolean success = decisionModelService.activateModel(modelId);
            if (success) {
                return JsonBean.success("模型激活成功");
            } else {
                return JsonBean.error("模型激活失败");
            }

        } catch (Exception e) {
            log.error("激活决策模型失败，modelId: {}", modelId, e);
            return JsonBean.error("激活决策模型失败: " + e.getMessage());
        }
    }

    @PostMapping("/deactivate")
    @ApiOperation("停用决策模型")
    public String deactivateModel(
            @ApiParam(value = "模型ID", required = true) @RequestParam Long modelId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            boolean success = decisionModelService.deactivateModel(modelId);
            if (success) {
                return JsonBean.success("模型停用成功");
            } else {
                return JsonBean.error("模型停用失败");
            }

        } catch (Exception e) {
            log.error("停用决策模型失败，modelId: {}", modelId, e);
            return JsonBean.error("停用决策模型失败: " + e.getMessage());
        }
    }

    @GetMapping("/modelTypes")
    @ApiOperation("获取模型类型选项")
    public String getModelTypes(HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            java.util.List<Map<String, Object>> modelTypes = new java.util.ArrayList<>();
            Map<String, Object> type1 = new HashMap<>();
            type1.put("label", "风险评估");
            type1.put("value", "RISK_ASSESSMENT");
            modelTypes.add(type1);

            Map<String, Object> type2 = new HashMap<>();
            type2.put("label", "投资决策");
            type2.put("value", "INVESTMENT_DECISION");
            modelTypes.add(type2);

            Map<String, Object> type3 = new HashMap<>();
            type3.put("label", "融资决策");
            type3.put("value", "FINANCING_DECISION");
            modelTypes.add(type3);

            Map<String, Object> type4 = new HashMap<>();
            type4.put("label", "现金流预测");
            type4.put("value", "CASHFLOW_FORECAST");
            modelTypes.add(type4);

            return new JsonBean(1, "查询成功", modelTypes).toString();

        } catch (Exception e) {
            log.error("获取模型类型选项失败", e);
            return JsonBean.error("获取模型类型选项失败: " + e.getMessage());
        }
    }

    @GetMapping("/algorithmTypes")
    @ApiOperation("获取算法类型选项")
    public String getAlgorithmTypes(HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            java.util.List<Map<String, Object>> algorithmTypes = new java.util.ArrayList<>();
            Map<String, Object> type1 = new HashMap<>();
            type1.put("label", "线性回归");
            type1.put("value", "LINEAR_REGRESSION");
            algorithmTypes.add(type1);

            Map<String, Object> type2 = new HashMap<>();
            type2.put("label", "逻辑回归");
            type2.put("value", "LOGISTIC_REGRESSION");
            algorithmTypes.add(type2);

            Map<String, Object> type3 = new HashMap<>();
            type3.put("label", "决策树");
            type3.put("value", "DECISION_TREE");
            algorithmTypes.add(type3);

            Map<String, Object> type4 = new HashMap<>();
            type4.put("label", "随机森林");
            type4.put("value", "RANDOM_FOREST");
            algorithmTypes.add(type4);

            Map<String, Object> type5 = new HashMap<>();
            type5.put("label", "神经网络");
            type5.put("value", "NEURAL_NETWORK");
            algorithmTypes.add(type5);

            return new JsonBean(1, "查询成功", algorithmTypes).toString();

        } catch (Exception e) {
            log.error("获取算法类型选项失败", e);
            return JsonBean.error("获取算法类型选项失败: " + e.getMessage());
        }
    }

    @PostMapping("/{modelId}/validate")
    @ApiOperation("验证决策模型")
    public String validateModel(
            @ApiParam(value = "模型ID", required = true) @PathVariable Long modelId,
            @ApiParam(value = "验证数据(JSON格式)") @RequestParam(required = false) String validationData,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查模型是否存在
            DecisionModel model = decisionModelService.getById(modelId);
            if (model == null) {
                return JsonBean.error("决策模型不存在");
            }

            // 调用Service层验证模型
            Map<String, Object> validationResult = decisionModelService.validateModel(modelId, validationData);

            return new JsonBean(1, "模型验证成功", validationResult).toString();

        } catch (Exception e) {
            log.error("验证决策模型失败，modelId: {}", modelId, e);
            return JsonBean.error("验证决策模型失败: " + e.getMessage());
        }
    }

    @PostMapping("/{modelId}/test")
    @ApiOperation("测试决策模型")
    public String testModel(
            @ApiParam(value = "模型ID", required = true) @PathVariable Long modelId,
            @ApiParam(value = "测试数据(JSON格式)") @RequestParam Map<String, Object> testData,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查模型是否存在
            DecisionModel model = decisionModelService.getById(modelId);
            if (model == null) {
                return JsonBean.error("决策模型不存在");
            }

            // 调用Service层测试模型
            Map<String, Object> testResult = decisionModelService.testModel(modelId, testData);

            return new JsonBean(1, "模型测试成功", testResult).toString();

        } catch (Exception e) {
            log.error("测试决策模型失败，modelId: {}", modelId, e);
            return JsonBean.error("测试决策模型失败: " + e.getMessage());
        }
    }

    /**
     * 验证用户权限
     */

    /**
     * 获取当前用户的StaffId（处理BigDecimal到Long的转换）
     */
    private Long getStaffId(TblStaffUtil user) {
        if (user == null || user.getStaffid() == null) {
            return null;
        }
        return user.getStaffid().longValue();
    }

    /**
     * 获取组织的OrgId（处理BigDecimal到Long的转换）
     */
    private Long getOrgId(com.hbfk.entity.TblOrganizationUtil org) {
        if (org == null || org.getOrgid() == null) {
            return null;
        }
        return org.getOrgid().longValue();
    }
    private boolean validateUser() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("获取用户信息异常", e);
            return false;
        }
    }
}
