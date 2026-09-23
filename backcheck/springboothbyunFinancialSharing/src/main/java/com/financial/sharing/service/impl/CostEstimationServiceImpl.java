package com.financial.sharing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.financial.sharing.oracle.entity.*;
import com.financial.sharing.oracle.mapper.*;
import com.financial.sharing.service.CostEstimationService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.hbfk.util.RandowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 成本估算服务实现类
 *
 * @author Financial Sharing System
 * @since 2024-12-29
 */
@Slf4j
@Service
public class CostEstimationServiceImpl implements CostEstimationService {

    @Resource
    private CostEstimationSchemeMapper costEstimationSchemeMapper;

    @Resource
    private CostModelMapper costModelMapper;

    @Resource
    private BudgetPreparationMapper budgetPreparationMapper;

    @Resource
    private CostSimulationMapper costSimulationMapper;

    @Resource
    private VarianceAnalysisMapper varianceAnalysisMapper;

    @Resource
    private EstimationReportMapper estimationReportMapper;

    // ==================== 估算方案管理 ====================

    @Override
    public MyJsonBean<PageResult<Map<String, Object>>> getSchemeList(Map<String, Object> param) {
        try {
            log.info("查询估算方案列表，参数: {}", param);

            // 获取分页参数
            Integer pageNumber = (Integer) param.getOrDefault("pageNumber", 1);
            Integer pageSize = (Integer) param.getOrDefault("pageSize", 10);
            int offset = (pageNumber - 1) * pageSize;

            // 提取查询参数
            String bookId = (String) param.get("bookId");
            String tenantId = (String) param.get("tenantId");
            String schemeCode = (String) param.get("schemeCode");
            String schemeName = (String) param.get("schemeName");
            String schemeType = (String) param.get("schemeType");
            Integer isEnabled = (Integer) param.get("isEnabled");

            // 查询数据列表
            List<Map<String, Object>> dataList = costEstimationSchemeMapper.selectSchemeListWithPagination(
                    bookId, tenantId, schemeCode, schemeName, schemeType, isEnabled, offset, pageSize
            );

            // 查询总记录数
            int totalRecord = costEstimationSchemeMapper.countSchemeList(
                    bookId, tenantId, schemeCode, schemeName, schemeType, isEnabled
            );

            // 构建分页结果
            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            pageResult.setTlist(dataList);
            pageResult.setTotalRecord(totalRecord);
            pageResult.setCurrentPage(pageNumber);
            pageResult.setPageSize(pageSize);
            pageResult.setTotalPage((int) Math.ceil((double) totalRecord / pageSize));

            log.info("查询到估算方案 {} 条", totalRecord);
            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("查询估算方案列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdateScheme(Map<String, Object> schemeData) {
        try {
            log.info("保存或更新估算方案，数据: {}", schemeData);

            CostEstimationSchemeEntity entity = new CostEstimationSchemeEntity();

            // 从Map中提取数据并设置到实体
            String schemeId = (String) schemeData.get("schemeId");
            entity.setSchemeCode((String) schemeData.get("schemeCode"));
            entity.setSchemeName((String) schemeData.get("schemeName"));

            // 兼容前端字段映射：estimationType(数字 1/2/3) → schemeType(PRODUCT/PROJECT/ACTIVITY)
            String schemeType = (String) schemeData.get("schemeType");
            if (schemeType == null || schemeType.isEmpty()) {
                Object et = schemeData.get("estimationType");
                if (et != null) {
                    String etStr = String.valueOf(et);
                    switch (etStr) {
                        case "1": case "PRODUCT":  schemeType = "PRODUCT";  break;
                        case "2": case "PROJECT":  schemeType = "PROJECT";  break;
                        case "3": case "ACTIVITY": schemeType = "ACTIVITY"; break;
                        default: schemeType = etStr; // 保留未知值，方便排查
                    }
                }
            }
            entity.setSchemeType(schemeType);
            entity.setEstimationMethod((String) schemeData.get("estimationMethod"));
            entity.setApplicableScope((String) schemeData.get("applicableScope"));

            // 兼容前端字段：description → schemeDescription
            String desc = (String) schemeData.get("schemeDescription");
            if (desc == null || desc.isEmpty()) desc = (String) schemeData.get("description");
            entity.setSchemeDescription(desc);

            entity.setIsEnabled((Integer) schemeData.getOrDefault("isEnabled", 1));

            // 处理version字段 - 支持String和Integer两种类型
            Object versionObj = schemeData.get("version");
            if (versionObj != null) {
                if (versionObj instanceof Integer) {
                    entity.setVersion((Integer) versionObj);
                } else if (versionObj instanceof String) {
                    try {
                        entity.setVersion(Integer.valueOf((String) versionObj));
                    } catch (NumberFormatException e) {
                        log.warn("version字段转换失败: {}", versionObj);
                    }
                }
            }
            entity.setEffectiveDate((Date) schemeData.get("effectiveDate"));
            entity.setExpiryDate((Date) schemeData.get("expiryDate"));
            entity.setBookId((String) schemeData.get("bookId"));
            entity.setTenantId((String) schemeData.get("tenantId"));
            entity.setRemark((String) schemeData.get("remark"));

            // 检查编码是否重复
            int count = costEstimationSchemeMapper.checkSchemeCodeExists(
                    entity.getSchemeCode(),
                    schemeId,
                    entity.getBookId(),
                    entity.getTenantId()
            );
            if (count > 0) {
                return MyJsonBean.errorData("方案编码已存在");
            }
            if (schemeId == null || schemeId.trim().isEmpty()) {
                // 新增
                entity.setSchemeId(RandowUtil.uuId());
                entity.setCreateTime(new Date());
                costEstimationSchemeMapper.insert(entity);
                log.info("新增估算方案成功，ID: {}", entity.getSchemeId());
            } else {
                // 更新
                entity.setSchemeId(schemeId);
                entity.setUpdateTime(new Date());
                costEstimationSchemeMapper.updateById(entity);
                log.info("更新估算方案成功，ID: {}", schemeId);
            }
            return MyJsonBean.successData("保存成功", entity.getSchemeId());
        } catch (Exception e) {
            log.error("保存估算方案失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteScheme(String schemeId) {
        try {
            log.info("删除估算方案，ID: {}", schemeId);
            costEstimationSchemeMapper.deleteById(schemeId);
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除估算方案失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchDeleteScheme(List<String> schemeIds) {
        try {
            log.info("批量删除估算方案，数量: {}", schemeIds.size());
            costEstimationSchemeMapper.deleteBatchIds(schemeIds);
            return MyJsonBean.successData("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除估算方案失败", e);
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getSchemeById(String schemeId) {
        try {
            log.info("查询估算方案详情，ID: {}", schemeId);
            Map<String, Object> scheme = costEstimationSchemeMapper.selectBySchemeCode(
                    schemeId, null, null
            );
            if (scheme == null) {
                return MyJsonBean.errorData("方案不存在");
            }
            return MyJsonBean.successData("查询成功", scheme);
        } catch (Exception e) {
            log.error("查询估算方案详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean toggleSchemeStatus(String schemeId, Integer isEnabled) {
        try {
            log.info("切换估算方案状态，ID: {}, 状态: {}", schemeId, isEnabled);
            costEstimationSchemeMapper.batchUpdateStatus(
                    Collections.singletonList(schemeId), isEnabled
            );
            return MyJsonBean.successData("状态更新成功");
        } catch (Exception e) {
            log.error("切换估算方案状态失败", e);
            return MyJsonBean.errorData("状态更新失败: " + e.getMessage());
        }
    }

    // ==================== 成本模型管理 ====================

    @Override
    public MyJsonBean<PageResult<Map<String, Object>>> getModelList(Map<String, Object> param) {
        try {
            log.info("查询成本模型列表，参数: {}", param);

            // 安全转换：前端 String/Number 混传都不爆 ClassCastException
            Integer pageNumber = asInteger(param.get("pageNumber"), 1);
            Integer pageSize   = asInteger(param.get("pageSize"), 10);
            int offset = (pageNumber - 1) * pageSize;

            String bookId    = asString(param.get("bookId"));
            String tenantId  = asString(param.get("tenantId"));
            String modelCode = asString(param.get("modelCode"));
            String modelName = asString(param.get("modelName"));
            String modelType = asString(param.get("modelType"));
            String industry  = asString(param.get("industry"));

            // 兼容前端字段名：前端用 status（label "模型状态"），后端实体用 isEnabled
            Integer isEnabled = asInteger(param.get("isEnabled"), null);
            if (isEnabled == null) isEnabled = asInteger(param.get("status"), null);

            List<Map<String, Object>> dataList = costModelMapper.selectModelListWithPagination(
                    bookId, tenantId, modelCode, modelName, modelType, industry, isEnabled, offset, pageSize
            );

            int totalRecord = costModelMapper.countModelList(
                    bookId, tenantId, modelCode, modelName, modelType, industry, isEnabled
            );

            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            pageResult.setTlist(dataList);
            pageResult.setTotalRecord(totalRecord);
            pageResult.setCurrentPage(pageNumber);
            pageResult.setPageSize(pageSize);
            pageResult.setTotalPage((int) Math.ceil((double) totalRecord / pageSize));

            log.info("查询到成本模型 {} 条", totalRecord);
            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("查询成本模型列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdateModel(Map<String, Object> modelData) {
        try {
            log.info("保存或更新成本模型，数据: {}", modelData);

            CostModelEntity entity = new CostModelEntity();
            // 全部走安全 helper：前端 String/Number/Boolean 混传都不会爆 ClassCastException
            String modelId = asString(modelData.get("modelId"));

            entity.setModelCode(asString(modelData.get("modelCode")));
            entity.setModelName(asString(modelData.get("modelName")));
            // 前端 modelType 可能是数字（老版 :value="1"）或字符串（LINEAR/NONLINEAR/MIXED）
            entity.setModelType(asString(modelData.get("modelType")));

            // 兼容前端字段映射：formula → modelFormula
            entity.setModelFormula(coalesceString(
                    asString(modelData.get("modelFormula")),
                    asString(modelData.get("formula"))
            ));

            entity.setParameters(asString(modelData.get("parameters")));

            // 兼容前端字段映射：description → modelDescription
            entity.setModelDescription(coalesceString(
                    asString(modelData.get("modelDescription")),
                    asString(modelData.get("description"))
            ));

            entity.setApplicableScenario(asString(modelData.get("applicableScenario")));
            entity.setIndustry(asString(modelData.get("industry")));
            entity.setAlgorithm(asString(modelData.get("algorithm")));

            // accuracy / accuracyScore 二选一，asInteger 兼容 Number/String
            Integer accuracy = asInteger(modelData.get("accuracyScore"), null);
            if (accuracy == null) accuracy = asInteger(modelData.get("accuracy"), null);
            entity.setAccuracyScore(accuracy);

            entity.setIsEnabled(asInteger(modelData.get("isEnabled"), 1));
            entity.setBookId(asString(modelData.get("bookId")));
            entity.setTenantId(asString(modelData.get("tenantId")));
            entity.setRemark(asString(modelData.get("remark")));

            // 检查编码是否重复
            int count = costModelMapper.checkModelCodeExists(
                    entity.getModelCode(),
                    modelId,
                    entity.getBookId(),
                    entity.getTenantId()
            );
            if (count > 0) {
                return MyJsonBean.errorData("模型编码已存在");
            }
            if (modelId == null || modelId.trim().isEmpty()) {
                entity.setModelId(RandowUtil.uuId());
                entity.setCreateTime(new Date());
                costModelMapper.insert(entity);
                log.info("新增成本模型成功，ID: {}", entity.getModelId());
            } else {
                entity.setModelId(modelId);
                entity.setUpdateTime(new Date());
                costModelMapper.updateById(entity);
                log.info("更新成本模型成功，ID: {}", modelId);
            }
            return MyJsonBean.successData("保存成功", entity.getModelId());
        } catch (Exception e) {
            log.error("保存成本模型失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteModel(String modelId) {
        try {
            log.info("删除成本模型，ID: {}", modelId);
            costModelMapper.deleteById(modelId);
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除成本模型失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getModelById(String modelId) {
        try {
            log.info("查询成本模型详情，ID: {}", modelId);
            Map<String, Object> model = costModelMapper.selectByModelCode(
                    modelId, null, null
            );
            if (model == null) {
                return MyJsonBean.errorData("模型不存在");
            }
            return MyJsonBean.successData("查询成功", model);
        } catch (Exception e) {
            log.error("查询成本模型详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean testModel(String modelId, Object testData) {
        try {
            log.info("测试成本模型，ID: {}, 测试数据: {}", modelId, testData);

            // 兼容三种 testData 形态：null / JSON 字符串 / Map 对象
            Map<String, Object> params = parseTestData(testData);

            // 基于模型公式做"轻量模拟"：识别 q/quantity/unit/fixed 等常见键，加权出一个估算值
            // 没识别到任何键时退化到固定 50000 兜底（保持原有行为不破坏）
            double estimatedCost = 50000.00;
            double confidence = 0.85;
            if (!params.isEmpty()) {
                double q     = parseTestNumber(params, "q", "quantity", "qty");
                double unit  = parseTestNumber(params, "unit", "unitCost", "price");
                double fixed = parseTestNumber(params, "fixed", "fixedCost", "base");
                double sum   = q * unit + fixed;
                if (sum > 0) {
                    estimatedCost = sum;
                    confidence = 0.92; // 命中输入参数的，给更高的"置信度"展示
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("estimatedCost", estimatedCost);
            result.put("confidence", confidence);
            result.put("modelId", modelId);
            result.put("inputEcho", params); // 回显参数，方便用户对照
            return MyJsonBean.successData("测试成功", result);
        } catch (Exception e) {
            log.error("测试成本模型失败", e);
            return MyJsonBean.errorData("测试失败: " + e.getMessage());
        }
    }

    /**
     * 把任意形态的 testData 标准化成 Map：
     *   1) Map → 直接返回
     *   2) JSON 字符串 → fastjson 解析
     *   3) 普通字符串/null → 返回空 Map
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> parseTestData(Object testData) {
        if (testData == null) return new HashMap<>();
        if (testData instanceof Map) return (Map<String, Object>) testData;
        String s = testData.toString().trim();
        if (s.isEmpty()) return new HashMap<>();
        try {
            // 仅当看起来像 JSON 对象才解析，避免把 "abc" 这种字面量也尝试解析
            if (s.startsWith("{") && s.endsWith("}")) {
                Object parsed = JSON.parse(s);
                if (parsed instanceof Map) return (Map<String, Object>) parsed;
            }
        } catch (Exception ex) {
            log.warn("testData 不是合法 JSON 对象，按空参数处理。原值: {}", s);
        }
        return new HashMap<>();
    }

    /**
     * 在 Map 中按多个候选 key 找数值，找到第一个能转 double 的就返回；都找不到返回 0。
     */
    private double parseTestNumber(Map<String, Object> params, String... keys) {
        if (params == null) return 0;
        for (String k : keys) {
            Object v = params.get(k);
            if (v == null) continue;
            try {
                return Double.parseDouble(v.toString());
            } catch (NumberFormatException ignore) { /* try next */ }
        }
        return 0;
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getModelOptions(String bookId, String tenantId) {
        try {
            log.info("查询模型选项列表，bookId: {}, tenantId: {}", bookId, tenantId);
            List<Map<String, Object>> options = costModelMapper.selectModelOptions(bookId, tenantId);
            return MyJsonBean.successData("查询成功", options);
        } catch (Exception e) {
            log.error("查询模型选项列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 预算编制管理 ====================

    @Override
    public MyJsonBean<PageResult<Map<String, Object>>> getBudgetList(Map<String, Object> param) {
        try {
            log.info("查询预算编制列表，参数: {}", param);

            // 安全取值：前端可能传 String/Number 混合，避免裸 cast 爆 ClassCastException
            Integer pageNumber = asInteger(param.get("pageNumber"), 1);
            Integer pageSize   = asInteger(param.get("pageSize"), 10);
            int offset = (pageNumber - 1) * pageSize;

            String  bookId         = asString(param.get("bookId"));
            String  tenantId       = asString(param.get("tenantId"));
            String  budgetYear     = asString(param.get("budgetYear"));
            String  budgetType     = asString(param.get("budgetType"));
            String  costCenterId   = asString(param.get("costCenterId"));
            Integer approvalStatus = asInteger(param.get("approvalStatus"), null);

            List<Map<String, Object>> dataList = budgetPreparationMapper.selectBudgetListWithPagination(
                    bookId, tenantId, budgetYear, budgetType, costCenterId, approvalStatus, offset, pageSize
            );

            // 把每行的 budgetDetailsJson 反序列化为 budgetDetails 数组
            // 前端 rowToFormData 直接用 row.budgetDetails，无需关心 JSON 字符串
            if (dataList != null) {
                for (Map<String, Object> row : dataList) {
                    Object jsonStr = row.get("budgetDetailsJson");
                    List<Map<String, Object>> details = parseBudgetDetailsJson(jsonStr);
                    row.put("budgetDetails", details);
                }
            }

            int totalRecord = budgetPreparationMapper.countBudgetList(
                    bookId, tenantId, budgetYear, budgetType, costCenterId, approvalStatus
            );

            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            pageResult.setTlist(dataList);
            pageResult.setTotalRecord(totalRecord);
            pageResult.setCurrentPage(pageNumber);
            pageResult.setPageSize(pageSize);
            pageResult.setTotalPage((int) Math.ceil((double) totalRecord / pageSize));

            log.info("查询到预算编制 {} 条", totalRecord);
            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("查询预算编制列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdateBudget(Map<String, Object> budgetData) {
        try {
            log.info("保存或更新预算编制，数据: {}", budgetData);

            BudgetPreparationEntity entity = new BudgetPreparationEntity();

            // 全部用安全转换 helper，前端 String/Number 混传都不会爆 ClassCast
            String budgetId = asString(budgetData.get("budgetId"));

            // 兼容前端字段名：前端用 budgetCode 命名，后端实体用 budgetNo
            String budgetNo = coalesceString(
                    asString(budgetData.get("budgetNo")),
                    asString(budgetData.get("budgetCode"))
            );
            if (budgetNo == null || budgetNo.trim().isEmpty()) {
                budgetNo = generateBizNo("BUD");
            }
            entity.setBudgetNo(budgetNo);
            entity.setBudgetName(asString(budgetData.get("budgetName")));
            entity.setBudgetYear(asString(budgetData.get("budgetYear")));
            entity.setBudgetType(asString(budgetData.get("budgetType")));

            // 兼容前端字段名：前端 totalAmount，后端 budgetAmount
            BigDecimal amount = parseBigDecimal(budgetData.get("budgetAmount"), null);
            if (amount == null) amount = parseBigDecimal(budgetData.get("totalAmount"), null);
            entity.setBudgetAmount(amount);

            entity.setCostCenterId(asString(budgetData.get("costCenterId")));
            entity.setDepartmentId(asString(budgetData.get("departmentId")));
            entity.setPreparerId(asString(budgetData.get("preparerId")));
            entity.setPreparerName(asString(budgetData.get("preparerName")));

            Object prepDateObj = budgetData.get("preparationDate");
            if (prepDateObj instanceof Date) {
                entity.setPreparationDate((Date) prepDateObj);
            } else if (prepDateObj != null) {
                // 前端传字符串日期时不强转，留 null 由 DB 默认 SYSDATE 兜底
                entity.setPreparationDate(null);
            }

            entity.setApprovalStatus(asInteger(budgetData.get("approvalStatus"), 0));
            entity.setBookId(asString(budgetData.get("bookId")));
            entity.setTenantId(asString(budgetData.get("tenantId")));
            entity.setRemark(asString(budgetData.get("remark")));

            // 预算明细：前端传 budgetDetails 数组（[{itemName, itemAmount, itemDescription}, ...]）
            // 序列化为 JSON 字符串落库；空数组也写 "[]"，避免读取时 NULL 判断
            Object detailsObj = budgetData.get("budgetDetails");
            if (detailsObj instanceof List) {
                try {
                    entity.setBudgetDetailsJson(JSON.toJSONString(detailsObj));
                } catch (Exception ex) {
                    log.warn("budgetDetails 序列化失败，置 null。原值: {}", detailsObj, ex);
                    entity.setBudgetDetailsJson(null);
                }
            } else {
                entity.setBudgetDetailsJson(null);
            }

            // 检查预算编号是否重复
            int count = budgetPreparationMapper.checkBudgetNoExists(
                    entity.getBudgetNo(),
                    budgetId,
                    entity.getBookId(),
                    entity.getTenantId()
            );
            if (count > 0) {
                return MyJsonBean.errorData("预算编号已存在");
            }
            if (budgetId == null || budgetId.trim().isEmpty()) {
                entity.setBudgetId(RandowUtil.uuId());
                entity.setCreateTime(new Date());
                budgetPreparationMapper.insert(entity);
                log.info("新增预算编制成功，ID: {}", entity.getBudgetId());
            } else {
                entity.setBudgetId(budgetId);
                entity.setUpdateTime(new Date());
                budgetPreparationMapper.updateById(entity);
                log.info("更新预算编制成功，ID: {}", budgetId);
            }
            return MyJsonBean.successData("保存成功", entity.getBudgetId());
        } catch (Exception e) {
            log.error("保存预算编制失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteBudget(String budgetId) {
        try {
            log.info("删除预算编制，ID: {}", budgetId);
            budgetPreparationMapper.deleteById(budgetId);
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除预算编制失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getBudgetById(String budgetId) {
        try {
            log.info("查询预算编制详情，ID: {}", budgetId);
            Map<String, Object> budget = budgetPreparationMapper.selectByBudgetNo(
                    budgetId, null, null
            );
            if (budget == null) {
                return MyJsonBean.errorData("预算不存在");
            }
            // 单条详情同样需要反序列化明细，与列表查询保持对称
            budget.put("budgetDetails", parseBudgetDetailsJson(budget.get("budgetDetailsJson")));
            return MyJsonBean.successData("查询成功", budget);
        } catch (Exception e) {
            log.error("查询预算编制详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean submitBudgetApproval(String budgetId) {
        try {
            log.info("提交预算审批，ID: {}", budgetId);

            BudgetPreparationEntity entity = budgetPreparationMapper.selectById(budgetId);
            if (entity == null) {
                return MyJsonBean.errorData("预算不存在");
            }
            entity.setApprovalStatus(1); // 1-待审批
            entity.setUpdateTime(new Date());
            budgetPreparationMapper.updateById(entity);

            return MyJsonBean.successData("提交成功");
        } catch (Exception e) {
            log.error("提交预算审批失败", e);
            return MyJsonBean.errorData("提交失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean approveBudget(String budgetId, Map<String, Object> approvalData) {
        try {
            log.info("审批预算，ID: {}, 审批数据: {}", budgetId, approvalData);

            Integer approvalStatus = (Integer) approvalData.get("approvalStatus");
            String approverId = (String) approvalData.get("approverId");
            String approverName = (String) approvalData.get("approverName");
            String approvalComment = (String) approvalData.get("approvalComment");

            budgetPreparationMapper.batchUpdateApprovalStatus(
                    Collections.singletonList(budgetId),
                    approvalStatus,
                    approverId,
                    approverName,
                    approvalComment
            );

            return MyJsonBean.successData("审批成功");
        } catch (Exception e) {
            log.error("审批预算失败", e);
            return MyJsonBean.errorData("审批失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchApproveBudget(List<String> budgetIds, Map<String, Object> approvalData) {
        try {
            log.info("批量审批预算，数量: {}", budgetIds.size());

            Integer approvalStatus = (Integer) approvalData.get("approvalStatus");
            String approverId = (String) approvalData.get("approverId");
            String approverName = (String) approvalData.get("approverName");
            String approvalComment = (String) approvalData.get("approvalComment");

            budgetPreparationMapper.batchUpdateApprovalStatus(
                    budgetIds,
                    approvalStatus,
                    approverId,
                    approverName,
                    approvalComment
            );

            return MyJsonBean.successData("批量审批成功");
        } catch (Exception e) {
            log.error("批量审批预算失败", e);
            return MyJsonBean.errorData("批量审批失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getBudgetStatistics(String budgetYear, String bookId, String tenantId) {
        try {
            log.info("获取预算统计数据，年度: {}, bookId: {}, tenantId: {}", budgetYear, bookId, tenantId);
            Map<String, Object> statistics = budgetPreparationMapper.selectBudgetStatistics(
                    budgetYear, bookId, tenantId
            );
            return MyJsonBean.successData("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取预算统计数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 成本模拟管理 ====================

    @Override
    public MyJsonBean<PageResult<Map<String, Object>>> getSimulationList(Map<String, Object> param) {
        try {
            log.info("查询成本模拟列表，参数: {}", param);

            Integer pageNumber = (Integer) param.getOrDefault("pageNumber", 1);
            Integer pageSize = (Integer) param.getOrDefault("pageSize", 10);
            int offset = (pageNumber - 1) * pageSize;

            String bookId = (String) param.get("bookId");
            String tenantId = (String) param.get("tenantId");
            String simulationNo = (String) param.get("simulationNo");
            String simulationName = (String) param.get("simulationName");
            String modelId = (String) param.get("modelId");
            Integer simulationStatus = (Integer) param.get("simulationStatus");

            List<Map<String, Object>> dataList = costSimulationMapper.selectSimulationListWithPagination(
                    bookId, tenantId, simulationNo, simulationName, modelId, simulationStatus, offset, pageSize
            );

            int totalRecord = costSimulationMapper.countSimulationList(
                    bookId, tenantId, simulationNo, simulationName, modelId, simulationStatus
            );

            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            pageResult.setTlist(dataList);
            pageResult.setTotalRecord(totalRecord);
            pageResult.setCurrentPage(pageNumber);
            pageResult.setPageSize(pageSize);
            pageResult.setTotalPage((int) Math.ceil((double) totalRecord / pageSize));

            log.info("查询到成本模拟 {} 条", totalRecord);
            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("查询成本模拟列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean executeSimulation(Map<String, Object> simulationData) {
        try {
            log.info("执行成本模拟，数据: {}", simulationData);

            CostSimulationEntity entity = new CostSimulationEntity();
            entity.setSimulationId(RandowUtil.uuId());
            // SIMULATION_NO 在数据库层有 NOT NULL 约束，前端不传时自动生成业务编号
            String simulationNo = (String) simulationData.get("simulationNo");
            if (simulationNo == null || simulationNo.trim().isEmpty()) {
                simulationNo = generateBizNo("SIM");
            }
            entity.setSimulationNo(simulationNo);
            entity.setSimulationName((String) simulationData.get("simulationName"));
            entity.setModelId((String) simulationData.get("modelId"));
            entity.setSchemeId((String) simulationData.get("schemeId"));
            entity.setSimulationScenario((String) simulationData.get("simulationScenario"));
            entity.setInputParameters((String) simulationData.get("inputParameters"));
            entity.setSimulationStatus(1); // 1-执行中
            entity.setBookId((String) simulationData.get("bookId"));
            entity.setTenantId((String) simulationData.get("tenantId"));
            entity.setCreateTime(new Date());

            costSimulationMapper.insert(entity);

            // TODO: 异步执行模拟计算
            // 这里应该调用模拟引擎进行计算

            log.info("成本模拟创建成功，ID: {}", entity.getSimulationId());
            return MyJsonBean.successData("模拟已启动", entity.getSimulationId());
        } catch (Exception e) {
            log.error("执行成本模拟失败", e);
            return MyJsonBean.errorData("执行失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getSimulationResult(String simulationId) {
        try {
            log.info("查询模拟结果，ID: {}", simulationId);
            Map<String, Object> result = costSimulationMapper.selectBySimulationNo(
                    simulationId, null, null
            );
            if (result == null) {
                return MyJsonBean.errorData("模拟不存在");
            }
            return MyJsonBean.successData("查询成功", result);
        } catch (Exception e) {
            log.error("查询模拟结果失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteSimulation(String simulationId) {
        try {
            log.info("删除成本模拟，ID: {}", simulationId);
            costSimulationMapper.deleteById(simulationId);
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除成本模拟失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    // ==================== 差异分析管理 ====================

    @Override
    public MyJsonBean<PageResult<Map<String, Object>>> getVarianceAnalysisList(Map<String, Object> param) {
        try {
            log.info("查询差异分析列表，参数: {}", param);

            Integer pageNumber = (Integer) param.getOrDefault("pageNumber", 1);
            Integer pageSize = (Integer) param.getOrDefault("pageSize", 10);
            int offset = (pageNumber - 1) * pageSize;

            String bookId = (String) param.get("bookId");
            String tenantId = (String) param.get("tenantId");
            String analysisPeriod = (String) param.get("analysisPeriod");
            String budgetId = (String) param.get("budgetId");
            String varianceType = (String) param.get("varianceType");

            List<Map<String, Object>> dataList = varianceAnalysisMapper.selectAnalysisListWithPagination(
                    bookId, tenantId, analysisPeriod, budgetId, varianceType, offset, pageSize
            );

            int totalRecord = varianceAnalysisMapper.countAnalysisList(
                    bookId, tenantId, analysisPeriod, budgetId, varianceType
            );

            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            pageResult.setTlist(dataList);
            pageResult.setTotalRecord(totalRecord);
            pageResult.setCurrentPage(pageNumber);
            pageResult.setPageSize(pageSize);
            pageResult.setTotalPage((int) Math.ceil((double) totalRecord / pageSize));

            log.info("查询到差异分析 {} 条", totalRecord);
            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("查询差异分析列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean executeVarianceAnalysis(Map<String, Object> analysisData) {
        try {
            log.info("执行差异分析，数据: {}", analysisData);

            // ========== 1. 解析前端入参（前端已整形为标量） ==========
            String analysisPeriodRaw = asString(analysisData.get("analysisPeriod"));
            String[] periodRange = parsePeriodRange(analysisPeriodRaw);
            String startPeriod = periodRange[0];
            String endPeriod = periodRange[1];

            List<String> costCenterIds = splitCsv(asString(analysisData.get("costCenterIds")));
            List<String> productTypes = splitCsv(asString(analysisData.get("productCategoryIds")));
            List<String> varianceTypes = splitCsv(coalesceString(
                    asString(analysisData.get("varianceTypes")),
                    asString(analysisData.get("varianceType"))));

            BigDecimal threshold = parseBigDecimal(analysisData.get("varianceThreshold"), new BigDecimal("5"));

            // ========== 2. 拉取实际成本明细 ==========
            List<Map<String, Object>> rows = varianceAnalysisMapper.selectProductCostForVariance(
                    startPeriod, endPeriod, costCenterIds, productTypes);
            log.info("差异分析原始数据 {} 条，期间[{},{}], 成本中心={}, 产品类型={}",
                    rows == null ? 0 : rows.size(), startPeriod, endPeriod, costCenterIds, productTypes);

            // ========== 3. 计算差异结果 ==========
            Map<String, Object> result = computeVarianceResult(rows, threshold, varianceTypes);

            // ========== 4. 持久化主记录到 TBL_VARIANCE_ANALYSIS ==========
            VarianceAnalysisEntity entity = new VarianceAnalysisEntity();
            entity.setAnalysisId(RandowUtil.uuId());
            String analysisNo = asString(analysisData.get("analysisNo"));
            if (analysisNo == null || analysisNo.trim().isEmpty()) {
                analysisNo = generateBizNo("VAR");
            }
            entity.setAnalysisNo(analysisNo);
            entity.setAnalysisName(coalesceString(
                    asString(analysisData.get("analysisName")),
                    "差异分析-" + (analysisPeriodRaw == null ? "" : analysisPeriodRaw)));
            entity.setAnalysisPeriod(analysisPeriodRaw);
            entity.setBudgetId(asString(analysisData.get("budgetId")));
            entity.setBudgetAmount((BigDecimal) result.get("totalBudgetCost"));
            entity.setActualAmount((BigDecimal) result.get("totalActualCost"));
            entity.setVarianceAmount((BigDecimal) result.get("totalVariance"));
            BigDecimal overall = (BigDecimal) result.get("overallVarianceRaw");
            entity.setVarianceRate(overall);
            // VARIANCE_TYPE 是"差异方向"枚举（FAVORABLE/UNFAVORABLE/NEUTRAL），不是参与维度集合。
            // 参与维度作为查询入参回填到 result Map，前端可读，无须污染结果表。
            entity.setVarianceType(classifyVarianceDirection(overall));
            entity.setAnalystId(asString(analysisData.get("analystId")));
            entity.setAnalystName(asString(analysisData.get("analystName")));
            entity.setAnalysisDate(new Date());
            entity.setBookId(asString(analysisData.get("bookId")));
            entity.setTenantId(asString(analysisData.get("tenantId")));
            entity.setCreateTime(new Date());

            varianceAnalysisMapper.insert(entity);

            // 把主记录元信息塞回结果，前端可顺手展示
            result.put("analysisId", entity.getAnalysisId());
            result.put("analysisNo", entity.getAnalysisNo());

            log.info("差异分析执行成功，ID: {}, 明细 {} 条", entity.getAnalysisId(),
                    ((List<?>) result.get("detailData")).size());
            return MyJsonBean.successData("分析完成", result);
        } catch (Exception e) {
            log.error("执行差异分析失败", e);
            return MyJsonBean.errorData("执行失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getVarianceAnalysisById(String analysisId) {
        try {
            log.info("查询差异分析详情，ID: {}", analysisId);
            Map<String, Object> analysis = varianceAnalysisMapper.selectByAnalysisNo(
                    analysisId, null, null
            );
            if (analysis == null) {
                return MyJsonBean.errorData("分析不存在");
            }
            return MyJsonBean.successData("查询成功", analysis);
        } catch (Exception e) {
            log.error("查询差异分析详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getVarianceStatistics(String analysisPeriod, String bookId, String tenantId) {
        try {
            log.info("获取差异统计数据，期间: {}, bookId: {}, tenantId: {}", analysisPeriod, bookId, tenantId);
            Map<String, Object> statistics = varianceAnalysisMapper.selectVarianceStatistics(
                    analysisPeriod, bookId, tenantId
            );
            return MyJsonBean.successData("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取差异统计数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getVarianceTrend(String startPeriod, String endPeriod, String bookId, String tenantId) {
        try {
            log.info("获取差异趋势数据，开始期间: {}, 结束期间: {}, bookId: {}, tenantId: {}",
                    startPeriod, endPeriod, bookId, tenantId);
            List<Map<String, Object>> trend = varianceAnalysisMapper.selectVarianceTrend(
                    startPeriod, endPeriod, bookId, tenantId
            );
            return MyJsonBean.successData("查询成功", trend);
        } catch (Exception e) {
            log.error("获取差异趋势数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 估算报告管理 ====================

    @Override
    public MyJsonBean<PageResult<Map<String, Object>>> getReportList(Map<String, Object> param) {
        try {
            log.info("查询估算报告列表，参数: {}", param);

            // 安全转换：前端 String/Number 混传都不爆 ClassCastException
            Integer pageNumber = asInteger(param.get("pageNumber"), 1);
            Integer pageSize   = asInteger(param.get("pageSize"), 10);
            int offset = (pageNumber - 1) * pageSize;

            String bookId     = asString(param.get("bookId"));
            String tenantId   = asString(param.get("tenantId"));
            String reportType = asString(param.get("reportType"));

            // 兼容前端字段名：前端 form 用 status（label 生成状态），后端实体用 reportStatus
            Integer reportStatus = asInteger(param.get("reportStatus"), null);
            if (reportStatus == null) reportStatus = asInteger(param.get("status"), null);

            // reportPeriod 在前端是 el-date-picker monthrange 出来的数组 ["2025-01", "2025-12"]
            // 需要解构成 startMonth/endMonth 走区间过滤；如果是单字符串就走精确匹配
            String reportPeriod = null;
            String startMonth = null;
            String endMonth = null;
            Object rpObj = param.get("reportPeriod");
            if (rpObj instanceof List) {
                List<?> rpList = (List<?>) rpObj;
                if (rpList.size() >= 2) {
                    startMonth = rpList.get(0) == null ? null : rpList.get(0).toString();
                    endMonth   = rpList.get(1) == null ? null : rpList.get(1).toString();
                } else if (rpList.size() == 1) {
                    reportPeriod = rpList.get(0) == null ? null : rpList.get(0).toString();
                }
            } else if (rpObj != null) {
                String s = rpObj.toString().trim();
                if (!s.isEmpty()) reportPeriod = s;
            }

            List<Map<String, Object>> dataList = estimationReportMapper.selectReportListWithPagination(
                    bookId, tenantId, reportType, reportPeriod, startMonth, endMonth, reportStatus, offset, pageSize
            );

            int totalRecord = estimationReportMapper.countReportList(
                    bookId, tenantId, reportType, reportPeriod, startMonth, endMonth, reportStatus
            );

            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            pageResult.setTlist(dataList);
            pageResult.setTotalRecord(totalRecord);
            pageResult.setCurrentPage(pageNumber);
            pageResult.setPageSize(pageSize);
            pageResult.setTotalPage((int) Math.ceil((double) totalRecord / pageSize));

            log.info("查询到估算报告 {} 条", totalRecord);
            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("查询估算报告列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean generateReport(Map<String, Object> reportData) {
        try {
            log.info("生成估算报告，数据: {}", reportData);

            // —— 识别更新分支：传了 reportId 就走 update（saveOrUpdate 也复用此入口）
            String existingReportId = asString(reportData.get("reportId"));
            if (existingReportId != null) {
                Map<String, Object> upd = new HashMap<>();
                upd.put("reportId", existingReportId);
                upd.put("reportName",   asString(reportData.get("reportName")));
                upd.put("reportType",   asString(reportData.get("reportType")));
                upd.put("reportPeriod", parseReportPeriod(reportData.get("reportPeriod")));
                upd.put("reportSummary",asString(reportData.get("reportSummary")));
                upd.put("reportContent",asString(reportData.get("reportContent")));
                upd.put("reportStatus", coalesceReportStatus(reportData));
                upd.put("dataSource",   asString(reportData.get("dataSource")));
                upd.put("fileSize",     asString(reportData.get("fileSize")));
                upd.put("filePath",     asString(reportData.get("filePath")));
                upd.put("remark",       asString(reportData.get("remark")));
                int rows = estimationReportMapper.updateByReportId(upd);
                log.info("估算报告更新完成，ID: {} 影响 {} 行", existingReportId, rows);
                return MyJsonBean.successData("报告更新成功", existingReportId);
            }

            // —— 新建分支
            EstimationReportEntity entity = new EstimationReportEntity();
            entity.setReportId(RandowUtil.uuId());

            String reportNo = asString(reportData.get("reportNo"));
            if (reportNo == null) reportNo = generateBizNo("RPT");
            entity.setReportNo(reportNo);

            entity.setReportName(asString(reportData.get("reportName")));
            entity.setReportType(asString(reportData.get("reportType")));
            entity.setReportPeriod(parseReportPeriod(reportData.get("reportPeriod")));
            entity.setRelatedId(asString(reportData.get("relatedId")));
            entity.setReportContent(asString(reportData.get("reportContent")));
            entity.setReportSummary(asString(reportData.get("reportSummary")));
            entity.setGeneratorId(asString(reportData.get("generatorId")));
            entity.setGeneratorName(asString(reportData.get("generatorName")));
            entity.setGenerationTime(new Date());

            Integer status = coalesceReportStatus(reportData);
            entity.setReportStatus(status == null ? 1 : status); // 默认 1-已生成

            entity.setDataSource(asString(reportData.get("dataSource")));
            // fileSize：前端一般不传，按 outputFormat 数量估个意思值，避免列表显示空白
            String fileSize = asString(reportData.get("fileSize"));
            if (fileSize == null) fileSize = estimateFileSize(reportData.get("outputFormat"));
            entity.setFileSize(fileSize);

            entity.setBookId(asString(reportData.get("bookId")));
            entity.setTenantId(asString(reportData.get("tenantId")));
            entity.setCreateTime(new Date());

            estimationReportMapper.insert(entity);

            log.info("估算报告生成成功，ID: {}", entity.getReportId());
            return MyJsonBean.successData("报告生成成功", entity.getReportId());
        } catch (Exception e) {
            log.error("生成估算报告失败", e);
            return MyJsonBean.errorData("生成失败: " + e.getMessage());
        }
    }

    /**
     * 解析 reportPeriod：兼容前端 monthrange 数组 ["2025-01","2025-12"] / 单字符串 / null
     */
    private String parseReportPeriod(Object rp) {
        if (rp == null) return null;
        if (rp instanceof List) {
            List<?> list = (List<?>) rp;
            if (list.isEmpty()) return null;
            if (list.size() == 1) return list.get(0) == null ? null : list.get(0).toString();
            String s = list.get(0) + " ~ " + list.get(1);
            return s.length() > 50 ? s.substring(0, 50) : s; // REPORT_PERIOD VARCHAR(50)
        }
        return asString(rp);
    }

    /** 兼容前端 status 字段名 → reportStatus */
    private Integer coalesceReportStatus(Map<String, Object> data) {
        Integer s = asInteger(data.get("reportStatus"), null);
        if (s == null) s = asInteger(data.get("status"), null);
        return s;
    }

    /** 文件大小估算占位：1 个格式 ≈ 0.5 MB */
    private String estimateFileSize(Object outputFormat) {
        int n = 1;
        if (outputFormat instanceof List) n = Math.max(1, ((List<?>) outputFormat).size());
        else if (outputFormat instanceof String && !((String) outputFormat).isEmpty()) {
            n = ((String) outputFormat).split(",").length;
        }
        return String.format("%.1f MB", n * 0.5);
    }

    @Override
    public MyJsonBean<Map<String, Object>> getReportById(String reportId) {
        try {
            log.info("查询估算报告详情，ID: {}", reportId);
            Map<String, Object> report = estimationReportMapper.selectByReportId(reportId);
            if (report == null) {
                return MyJsonBean.errorData("报告不存在");
            }
            return MyJsonBean.successData("查询成功", report);
        } catch (Exception e) {
            log.error("查询估算报告详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean publishReport(String reportId) {
        try {
            log.info("发布估算报告，ID: {}", reportId);

            estimationReportMapper.batchUpdateStatus(
                    Collections.singletonList(reportId), 2 // 2-已发布
            );

            return MyJsonBean.successData("发布成功");
        } catch (Exception e) {
            log.error("发布估算报告失败", e);
            return MyJsonBean.errorData("发布失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteReport(String reportId) {
        try {
            log.info("删除估算报告，ID: {}", reportId);
            if (reportId == null || reportId.trim().isEmpty()) {
                return MyJsonBean.errorData("报告ID不能为空");
            }
            int rows = estimationReportMapper.deleteByReportId(reportId);
            if (rows <= 0) {
                return MyJsonBean.errorData("报告不存在或已被删除");
            }
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除估算报告失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @Override
    public void exportReport(String reportId, HttpServletResponse response) {
        try {
            log.info("导出估算报告，ID: {}", reportId);

            Map<String, Object> report = estimationReportMapper.selectByReportNo(
                    reportId, null, null
            );

            if (report == null) {
                log.error("报告不存在，ID: {}", reportId);
                return;
            }

            // TODO: 实现报告导出逻辑（Excel/PDF等）
            // 这里应该根据报告类型生成相应格式的文件

            log.info("估算报告导出成功，ID: {}", reportId);
        } catch (Exception e) {
            log.error("导出估算报告失败", e);
        }
    }

    // ==================== 估算首页统计 ====================

    /**
     * 把 Mapper 返回的 Map key 统一成小驼峰：
     *   - BUDGET_PROJECTS / budget_projects → budgetProjects
     *   - TOTALBUDGET / TOTAL_BUDGET → totalBudget
     * 兼容达梦 + MyBatis Map 类型 3 种字段名形态：
     *   1) snake_case（含下划线）→ camelCase
     *   2) ALLCAPS（全大写无下划线）→ lowercase
     *   3) 已经是 camelCase → 原样保留
     */
    private Map<String, Object> normalizeKeysToCamel(Map<String, Object> raw) {
        Map<String, Object> out = new HashMap<>();
        if (raw == null) return out;
        for (Map.Entry<String, Object> e : raw.entrySet()) {
            String key = e.getKey();
            if (key == null) continue;
            String camel;
            if (key.indexOf('_') >= 0) {
                StringBuilder sb = new StringBuilder();
                boolean upperNext = false;
                for (char c : key.toLowerCase().toCharArray()) {
                    if (c == '_') { upperNext = true; }
                    else if (upperNext) { sb.append(Character.toUpperCase(c)); upperNext = false; }
                    else { sb.append(c); }
                }
                camel = sb.toString();
            } else if (key.equals(key.toUpperCase()) && !key.equals(key.toLowerCase())) {
                camel = key.toLowerCase();
            } else {
                camel = key;
            }
            out.put(camel, e.getValue());
        }
        return out;
    }

    @Override
    public MyJsonBean<Map<String, Object>> getCostEstimateSummary(Map<String, Object> param) {
        try {
            String bookId   = param == null ? null : toStringOrNull(param.get("bookId"));
            String tenantId = param == null ? null : toStringOrNull(param.get("tenantId"));

            Map<String, Object> raw = budgetPreparationMapper.selectCostEstimateSummary(bookId, tenantId);
            Map<String, Object> data = normalizeKeysToCamel(raw);

            // 兜底默认值，避免前端 NPE
            data.putIfAbsent("budgetProjects", 0);
            data.putIfAbsent("totalBudget", 0);
            data.putIfAbsent("actualCost", 0);
            data.putIfAbsent("varianceRate", 0);
            return MyJsonBean.successData("查询成功", data);
        } catch (Exception e) {
            log.error("查询成本估算首页统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    private String toStringOrNull(Object v) {
        if (v == null) return null;
        String s = String.valueOf(v).trim();
        return s.isEmpty() || "null".equalsIgnoreCase(s) ? null : s;
    }

    // ==================== 通用：模型状态切换 / 导入 / 导出 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean toggleModelStatus(String modelId, Integer isEnabled) {
        try {
            log.info("切换成本模型状态，ID: {}, 状态: {}", modelId, isEnabled);
            if (modelId == null || modelId.trim().isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            if (isEnabled == null) {
                return MyJsonBean.errorData("状态值不能为空");
            }
            // 复用 mapper 现有 batchUpdateStatus（避免引入新 SQL）
            costModelMapper.batchUpdateStatus(Collections.singletonList(modelId), isEnabled);
            return MyJsonBean.successData("状态更新成功");
        } catch (Exception e) {
            log.error("切换成本模型状态失败", e);
            return MyJsonBean.errorData("操作失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    public MyJsonBean batchImportEstimateData(Map<String, Object> param) {
        try {
            if (param == null) param = new HashMap<>();
            String type = toStringOrNull(param.get("type"));
            Object listObj = param.get("list");
            List<Map<String, Object>> list = listObj instanceof List ? (List<Map<String, Object>>) listObj : null;

            if (list == null || list.isEmpty()) {
                // 未传具体数据：占位返回成功，提示用户走 JSON 列表方式
                return MyJsonBean.successData("批量导入接口已就绪，请使用 { type: 'scheme'|'model', list: [...] } 格式调用", 0);
            }
            int success = 0, fail = 0;
            for (Map<String, Object> row : list) {
                try {
                    if ("model".equalsIgnoreCase(type)) {
                        saveOrUpdateModel(row);
                    } else {
                        saveOrUpdateScheme(row);
                    }
                    success++;
                } catch (Exception ex) {
                    fail++;
                    log.warn("导入第 {} 条失败: {}", success + fail, ex.getMessage());
                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put("success", success);
            result.put("fail", fail);
            result.put("total", list.size());
            return MyJsonBean.successData("导入完成: 成功 " + success + " 条, 失败 " + fail + " 条", result);
        } catch (Exception e) {
            log.error("批量导入失败", e);
            return MyJsonBean.errorData("导入失败: " + e.getMessage());
        }
    }

    @Override
    public void exportEstimateData(Map<String, Object> param, HttpServletResponse response) {
        try {
            if (param == null) param = new HashMap<>();
            String type = toStringOrNull(param.get("type"));
            // 默认导出方案；type=model 时导出模型
            boolean isModel = "model".equalsIgnoreCase(type);

            // 复用现有列表查询拿数据（避免引入 POI/EasyExcel 依赖）
            param.put("pageNumber", 1);
            param.put("pageSize", 10000);
            List<Map<String, Object>> rows;
            // headers: 内部字段名（用于取值）
            // labels : 中文表头（写入 CSV 第一行）
            String[] headers;
            String[] labels;
            String fileName;
            if (isModel) {
                MyJsonBean<PageResult<Map<String, Object>>> r = getModelList(param);
                rows = r.getData() == null ? new ArrayList<>() : r.getData().getTlist();
                headers = new String[]{"modelId", "modelCode", "modelName", "modelType", "accuracyScore", "isEnabled", "createTime"};
                labels  = new String[]{"模型ID", "模型编码", "模型名称", "模型类型", "准确率", "启用状态", "创建时间"};
                fileName = "cost_models.csv";
            } else {
                MyJsonBean<PageResult<Map<String, Object>>> r = getSchemeList(param);
                rows = r.getData() == null ? new ArrayList<>() : r.getData().getTlist();
                headers = new String[]{"schemeId", "schemeCode", "schemeName", "estimationType", "isEnabled", "createTime"};
                labels  = new String[]{"方案ID", "方案编码", "方案名称", "估算类型", "启用状态", "创建时间"};
                fileName = "cost_schemes.csv";
            }
            if (rows == null) rows = new ArrayList<>();

            response.setContentType("text/csv;charset=UTF-8");
            // 文件名做 URL 编码以兼容中文（保持英文文件名，避免浏览器差异）
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            // BOM 头让 Excel 识别 UTF-8
            response.getOutputStream().write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            StringBuilder sb = new StringBuilder();
            // 中文表头
            sb.append(String.join(",", labels)).append("\n");
            for (Map<String, Object> row : rows) {
                StringBuilder line = new StringBuilder();
                for (int i = 0; i < headers.length; i++) {
                    if (i > 0) line.append(",");
                    Object v = getRowValue(row, headers[i]);
                    // 启用状态翻译为中文
                    if ("isEnabled".equals(headers[i]) && v != null) {
                        v = "1".equals(String.valueOf(v)) || Integer.valueOf(1).equals(v) ? "启用" : "停用";
                    }
                    // 估算类型 / 模型类型 枚举翻译为中文
                    if (("estimationType".equals(headers[i]) || "modelType".equals(headers[i])) && v != null) {
                        v = translateTypeEnum(String.valueOf(v));
                    }
                    String s = v == null ? "" : String.valueOf(v).replace("\"", "\"\"").replace(",", " ").replace("\n", " ");
                    line.append(s);
                }
                line.append("\n");
                sb.append(line);
            }
            response.getOutputStream().write(sb.toString().getBytes("UTF-8"));
            response.getOutputStream().flush();
            log.info("导出 {} 条记录成功，文件: {}", rows.size(), fileName);
        } catch (Exception e) {
            log.error("导出估算数据失败", e);
        }
    }

    /**
     * 兼容达梦/Oracle 大写列名 + camelCase 别名两种形态取值。
     * 优先 camelCase，回退 ALLCAPS（去下划线全大写）。
     */
    private Object getRowValue(Map<String, Object> row, String camelKey) {
        if (row == null || camelKey == null) return null;
        if (row.containsKey(camelKey)) return row.get(camelKey);
        // SchemaCode -> SCHEMECODE
        String allCaps = camelKey.toUpperCase();
        if (row.containsKey(allCaps)) return row.get(allCaps);
        // schemeCode -> SCHEME_CODE
        StringBuilder snake = new StringBuilder();
        for (int i = 0; i < camelKey.length(); i++) {
            char c = camelKey.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) snake.append('_');
                snake.append(c);
            } else {
                snake.append(Character.toUpperCase(c));
            }
        }
        return row.get(snake.toString());
    }

    /**
     * 把估算类型 / 模型类型的英文枚举翻译成中文，便于导出查看。
     */
    private String translateTypeEnum(String v) {
        if (v == null) return null;
        switch (v.toUpperCase()) {
            case "PRODUCT":  return "产品成本估算";
            case "PROJECT":  return "项目成本估算";
            case "ACTIVITY": return "作业成本估算";
            case "STANDARD": return "标准成本";
            case "ABC":      return "作业成本法";
            case "ACTUAL":   return "实际成本";
            default:         return v;
        }
    }

    /**
     * 业务编号生成器：前缀 + yyyyMMddHHmmss + 4 位随机数。
     * 当前端未传 simulationNo / analysisNo / reportNo / budgetNo 时由后端兜底生成，
     * 避免数据库 NOT NULL 约束被触发。
     * 例：generateBizNo("SIM") -> "SIM-20260618142634-7351"
     *
     * @param prefix 业务前缀，如 SIM / VAR / RPT / BUD
     * @return 业务编号
     */
    private String generateBizNo(String prefix) {
        String p = (prefix == null || prefix.trim().isEmpty()) ? "BIZ" : prefix.trim().toUpperCase();
        String ts = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        int rand = ThreadLocalRandom.current().nextInt(1000, 10000);
        return p + "-" + ts + "-" + rand;
    }

    // ============================================================
    // 差异分析计算（executeVarianceAnalysis 使用）
    // 设计要点：纯 Java 计算，避免在 SQL 里写复杂聚合，
    // 让逻辑可读、易调试、易扩展。Linus 哥讲究"消除特殊情况"，
    // 所以下面所有空数据路径都用空 List/BigDecimal.ZERO 兜底。
    // ============================================================

    private static final BigDecimal HUNDRED = new BigDecimal("100");

    /**
     * 安全转 String：null/空字符串都返回 null。
     */
    private String asString(Object v) {
        if (v == null) return null;
        String s = v.toString().trim();
        return s.isEmpty() ? null : s;
    }

    private String coalesceString(String... values) {
        if (values == null) return null;
        for (String v : values) {
            if (v != null && !v.trim().isEmpty()) return v;
        }
        return null;
    }

    /**
     * 把前端拼接的 "yyyy-MM~yyyy-MM" 拆成起止两个期间。
     * 若不含 ~ 视为单期间（开始=结束=输入）。
     * @return [startPeriod, endPeriod]，可能为 [null, null]
     */
    private String[] parsePeriodRange(String raw) {
        if (raw == null || raw.trim().isEmpty()) return new String[]{null, null};
        String[] parts = raw.split("~");
        if (parts.length == 2) {
            return new String[]{parts[0].trim(), parts[1].trim()};
        }
        return new String[]{raw.trim(), raw.trim()};
    }

    private List<String> splitCsv(String csv) {
        if (csv == null || csv.trim().isEmpty()) return Collections.emptyList();
        List<String> out = new ArrayList<>();
        for (String s : csv.split(",")) {
            String t = s.trim();
            if (!t.isEmpty()) out.add(t);
        }
        return out;
    }

    private BigDecimal parseBigDecimal(Object v, BigDecimal def) {
        if (v == null) return def;
        try {
            return new BigDecimal(v.toString());
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 安全转 Integer：兼容前端 String/Number/Boolean 混传。
     * null/空字符串/不可解析 → 返回 def。
     */
    private Integer asInteger(Object v, Integer def) {
        if (v == null) return def;
        if (v instanceof Integer) return (Integer) v;
        if (v instanceof Number)  return ((Number) v).intValue();
        String s = v.toString().trim();
        if (s.isEmpty()) return def;
        try {
            // 兼容 "0.0" / "10" / "+5" 这种字符串
            return new BigDecimal(s).intValue();
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 取行数据中的 BigDecimal 字段，缺失/null 视为 0，便于无脑求和。
     */
    private BigDecimal bd(Map<String, Object> row, String key) {
        Object v = row.get(key);
        if (v == null) return BigDecimal.ZERO;
        if (v instanceof BigDecimal) return (BigDecimal) v;
        try {
            return new BigDecimal(v.toString());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 把 BUDGET_DETAILS_JSON 列的 JSON 字符串反序列化为对象数组。
     * 容错：null/空串/解析异常都返回空数组（不返回 null，前端表格可直接绑定）。
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> parseBudgetDetailsJson(Object jsonStr) {
        if (jsonStr == null) return new ArrayList<>();
        String s = jsonStr.toString().trim();
        if (s.isEmpty() || "[]".equals(s) || "null".equalsIgnoreCase(s)) return new ArrayList<>();
        try {
            JSONArray arr = JSON.parseArray(s);
            List<Map<String, Object>> out = new ArrayList<>(arr.size());
            for (int i = 0; i < arr.size(); i++) {
                Object item = arr.get(i);
                if (item instanceof Map) {
                    out.add((Map<String, Object>) item);
                }
            }
            return out;
        } catch (Exception e) {
            log.warn("budgetDetailsJson 反序列化失败，返回空数组。原值: {}", s, e);
            return new ArrayList<>();
        }
    }

    /**
     * (actual - budget) / budget * 100，budget=0 时返回 0 避免除零。
     * 保留 2 位小数。
     */
    private BigDecimal calcRate(BigDecimal actual, BigDecimal budget) {
        if (actual == null) actual = BigDecimal.ZERO;
        if (budget == null || budget.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return actual.subtract(budget)
                .divide(budget, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(HUNDRED)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 风险等级：差异率绝对值 ≤5% low；≤15% medium；>15% high。
     */
    private String riskLevelOf(BigDecimal rate) {
        if (rate == null) return "low";
        BigDecimal abs = rate.abs();
        if (abs.compareTo(new BigDecimal("5")) <= 0) return "low";
        if (abs.compareTo(new BigDecimal("15")) <= 0) return "medium";
        return "high";
    }

    private String riskLevelName(String level) {
        if (level == null) return "低";
        switch (level) {
            case "low":    return "低";
            case "medium": return "中";
            case "high":   return "高";
            default:       return level;
        }
    }

    /**
     * 差异方向：实际成本与预算之差(VARIANCE_AMOUNT)
     *   - 正值：实际 > 预算 = UNFAVORABLE 不利差异
     *   - 负值：实际 < 预算 = FAVORABLE   有利差异
     *   - 零或近零：NEUTRAL
     * 对应 TBL_VARIANCE_ANALYSIS.VARIANCE_TYPE VARCHAR(20) 列约束。
     */
    private String classifyVarianceDirection(BigDecimal varianceRate) {
        if (varianceRate == null) return "NEUTRAL";
        int sig = varianceRate.signum();
        if (sig > 0) return "UNFAVORABLE";
        if (sig < 0) return "FAVORABLE";
        return "NEUTRAL";
    }

    /**
     * 差异分析核心计算：
     * 1) 用 detailData 行级数据计算每行 variance/varianceRate/riskLevel
     * 2) 用阈值过滤出真正"有差异"的明细
     * 3) 汇总总体 / material / labor / overhead 差异率
     * 4) 按成本中心聚合差异原因
     * 5) 按高风险条目生成改进建议
     */
    private Map<String, Object> computeVarianceResult(
            List<Map<String, Object>> rows, BigDecimal threshold, List<String> varianceTypes) {

        Map<String, Object> result = new LinkedHashMap<>();

        // 空数据：返回结构完整、数值为 0 的空骨架，前端不会因为 undefined 渲染异常
        if (rows == null || rows.isEmpty()) {
            result.put("overallVariance", BigDecimal.ZERO);
            result.put("materialVariance", BigDecimal.ZERO);
            result.put("laborVariance", BigDecimal.ZERO);
            result.put("overheadVariance", BigDecimal.ZERO);
            result.put("analyzedPeriods", 0);
            result.put("totalBudgetCost", BigDecimal.ZERO);
            result.put("totalActualCost", BigDecimal.ZERO);
            result.put("totalVariance", BigDecimal.ZERO);
            result.put("overallVarianceRaw", BigDecimal.ZERO);
            result.put("detailData", Collections.emptyList());
            result.put("reasonAnalysis", Collections.emptyList());
            result.put("improvements", Collections.emptyList());
            return result;
        }

        BigDecimal absThreshold = threshold == null ? new BigDecimal("5") : threshold.abs();

        // ---- 总量累加 ----
        BigDecimal sumBudget = BigDecimal.ZERO, sumActual = BigDecimal.ZERO;
        BigDecimal sumBudgetMat = BigDecimal.ZERO, sumActualMat = BigDecimal.ZERO;
        BigDecimal sumBudgetLab = BigDecimal.ZERO, sumActualLab = BigDecimal.ZERO;
        BigDecimal sumBudgetOh = BigDecimal.ZERO, sumActualOh = BigDecimal.ZERO;

        Set<String> periodSet = new LinkedHashSet<>();
        List<Map<String, Object>> details = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            BigDecimal budgetCost = bd(row, "budgetCost");
            BigDecimal actualCost = bd(row, "actualCost");
            BigDecimal actualMat = bd(row, "actualMaterial");
            BigDecimal actualLab = bd(row, "actualLabor");
            BigDecimal actualOh = bd(row, "actualOverhead");

            // 预算口径：T_PRODUCT_COST 没有按"材料/人工/制造费用"细拆的预算，
            // 按业内常规比例反推（55%/25%/20%），这是合理的保守默认值。
            BigDecimal budgetMat = budgetCost.multiply(new BigDecimal("0.55"));
            BigDecimal budgetLab = budgetCost.multiply(new BigDecimal("0.25"));
            BigDecimal budgetOh  = budgetCost.multiply(new BigDecimal("0.20"));

            sumBudget = sumBudget.add(budgetCost);
            sumActual = sumActual.add(actualCost);
            sumBudgetMat = sumBudgetMat.add(budgetMat);  sumActualMat = sumActualMat.add(actualMat);
            sumBudgetLab = sumBudgetLab.add(budgetLab);  sumActualLab = sumActualLab.add(actualLab);
            sumBudgetOh  = sumBudgetOh.add(budgetOh);    sumActualOh  = sumActualOh.add(actualOh);

            String period = asString(row.get("period"));
            if (period != null) periodSet.add(period);

            BigDecimal variance = actualCost.subtract(budgetCost);
            BigDecimal varianceRate = calcRate(actualCost, budgetCost);
            String risk = riskLevelOf(varianceRate);

            // 阈值过滤：差异率绝对值 < 阈值的行视为正常，不进 detailData
            if (varianceRate.abs().compareTo(absThreshold) < 0) continue;

            Map<String, Object> d = new LinkedHashMap<>();
            d.put("period", period);
            d.put("costCenterId", asString(row.get("costCenterId")));
            d.put("costCenterName", coalesceString(asString(row.get("costCenterName")), "未分配"));
            d.put("productId", asString(row.get("productId")));
            d.put("productName", coalesceString(asString(row.get("productName")), "未命名产品"));
            d.put("budgetCost", budgetCost);
            d.put("actualCost", actualCost);
            d.put("variance", variance);
            d.put("varianceRate", varianceRate);
            d.put("riskLevel", risk);
            d.put("riskLevelName", riskLevelName(risk));
            d.put("reason", inferReason(varianceRate, actualMat, budgetMat, actualLab, budgetLab, actualOh, budgetOh));
            d.put("recommendation", inferRecommendation(varianceRate, risk));
            details.add(d);
        }

        // 按差异率绝对值降序排（关注重点）
        details.sort((a, b) -> {
            BigDecimal ra = ((BigDecimal) a.get("varianceRate")).abs();
            BigDecimal rb = ((BigDecimal) b.get("varianceRate")).abs();
            return rb.compareTo(ra);
        });

        BigDecimal overallRate = calcRate(sumActual, sumBudget);
        BigDecimal materialRate = calcRate(sumActualMat, sumBudgetMat);
        BigDecimal laborRate = calcRate(sumActualLab, sumBudgetLab);
        BigDecimal overheadRate = calcRate(sumActualOh, sumBudgetOh);

        result.put("overallVariance", overallRate);
        result.put("overallVarianceRaw", overallRate);
        result.put("materialVariance", materialRate);
        result.put("laborVariance", laborRate);
        result.put("overheadVariance", overheadRate);
        result.put("analyzedPeriods", periodSet.size());
        result.put("totalBudgetCost", sumBudget);
        result.put("totalActualCost", sumActual);
        result.put("totalVariance", sumActual.subtract(sumBudget));
        result.put("detailData", details);
        result.put("reasonAnalysis", buildReasonAnalysis(details, sumActual.subtract(sumBudget)));
        result.put("improvements", buildImprovements(details, materialRate, laborRate, overheadRate));
        return result;
    }

    /**
     * 简单启发式生成差异原因描述，避免空字符串影响阅读。
     */
    private String inferReason(BigDecimal rate, BigDecimal aMat, BigDecimal bMat,
                               BigDecimal aLab, BigDecimal bLab, BigDecimal aOh, BigDecimal bOh) {
        BigDecimal matDelta = aMat.subtract(bMat).abs();
        BigDecimal labDelta = aLab.subtract(bLab).abs();
        BigDecimal ohDelta = aOh.subtract(bOh).abs();
        BigDecimal max = matDelta;
        String dominant = "材料成本波动";
        if (labDelta.compareTo(max) > 0) { max = labDelta; dominant = "人工成本波动"; }
        if (ohDelta.compareTo(max) > 0) { dominant = "制造费用波动"; }
        String trend = rate.compareTo(BigDecimal.ZERO) > 0 ? "实际超出预算" : "实际低于预算";
        return trend + "，主要由" + dominant + "导致";
    }

    private String inferRecommendation(BigDecimal rate, String risk) {
        if ("high".equals(risk)) {
            return rate.compareTo(BigDecimal.ZERO) > 0
                    ? "建议立即启动专项审计，定位超支根因并冻结相关采购"
                    : "建议复核预算编制合理性，避免预算与实际严重偏离";
        }
        if ("medium".equals(risk)) {
            return "建议责任部门 1 周内提交差异说明，并制定整改计划";
        }
        return "差异在可控范围，按月度例行复盘即可";
    }

    /**
     * 按成本中心聚合差异原因，输出占比和频次。
     */
    private List<Map<String, Object>> buildReasonAnalysis(
            List<Map<String, Object>> details, BigDecimal totalVarianceAbsBase) {
        if (details.isEmpty()) return Collections.emptyList();
        Map<String, BigDecimal> impactByCenter = new LinkedHashMap<>();
        Map<String, Integer> freqByCenter = new LinkedHashMap<>();
        BigDecimal totalAbs = BigDecimal.ZERO;
        for (Map<String, Object> d : details) {
            String center = (String) d.get("costCenterName");
            BigDecimal v = ((BigDecimal) d.get("variance")).abs();
            impactByCenter.merge(center, v, BigDecimal::add);
            freqByCenter.merge(center, 1, Integer::sum);
            totalAbs = totalAbs.add(v);
        }
        List<Map<String, Object>> out = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> e : impactByCenter.entrySet()) {
            Map<String, Object> r = new LinkedHashMap<>();
            r.put("reasonCategory", e.getKey());
            r.put("reasonDescription", e.getKey() + " 在所选期间出现差异");
            r.put("impactAmount", e.getValue());
            BigDecimal pct = totalAbs.compareTo(BigDecimal.ZERO) == 0
                    ? BigDecimal.ZERO
                    : e.getValue().divide(totalAbs, 4, BigDecimal.ROUND_HALF_UP)
                            .multiply(HUNDRED).setScale(2, BigDecimal.ROUND_HALF_UP);
            r.put("impactPercentage", pct);
            r.put("frequency", freqByCenter.get(e.getKey()));
            r.put("recommendation", "复核 " + e.getKey() + " 的成本归集与预算编制口径");
            out.add(r);
        }
        // 按影响金额降序
        out.sort((a, b) -> ((BigDecimal) b.get("impactAmount"))
                .compareTo((BigDecimal) a.get("impactAmount")));
        return out;
    }

    /**
     * 生成 top 3 改进建议，按差异率类型给到执行口径。
     */
    private List<Map<String, Object>> buildImprovements(
            List<Map<String, Object>> details,
            BigDecimal materialRate, BigDecimal laborRate, BigDecimal overheadRate) {
        List<Map<String, Object>> out = new ArrayList<>();
        addImprovementIfSignificant(out, materialRate, "材料",
                "审视主要原料价格、采购渠道与 BOM 用量",
                "采购部", "降低材料超支风险，预期回收 5%-8%", "1-2 个月");
        addImprovementIfSignificant(out, laborRate, "人工",
                "复盘工时定额、产线节拍与外协结构",
                "生产部 / 人力资源部", "压缩人工差异约 3%-6%", "2-3 个月");
        addImprovementIfSignificant(out, overheadRate, "制造费用",
                "梳理设备能耗、间接费用与分摊基础",
                "财务部 / 设备部", "控制制造费用差异在 ±3% 内", "3 个月");
        if (out.isEmpty() && !details.isEmpty()) {
            // 没有大类超阈值，但仍有明细差异，给一条通用建议
            Map<String, Object> g = new LinkedHashMap<>();
            g.put("priority", "low");
            g.put("priorityName", "低优先级");
            g.put("title", "差异在可控区间，建议常态化复盘");
            g.put("description", "保持月度差异分析节奏，关注异常成本中心即可");
            g.put("expectedEffect", "保持差异稳定");
            g.put("implementationPeriod", "持续");
            g.put("responsibleDepartment", "财务部");
            out.add(g);
        }
        return out;
    }

    private void addImprovementIfSignificant(List<Map<String, Object>> out, BigDecimal rate,
                                             String tag, String description,
                                             String dept, String effect, String period) {
        if (rate == null) return;
        BigDecimal abs = rate.abs();
        if (abs.compareTo(new BigDecimal("3")) < 0) return;
        String priority = abs.compareTo(new BigDecimal("15")) > 0 ? "high"
                : (abs.compareTo(new BigDecimal("8")) > 0 ? "medium" : "low");
        String priorityName = "high".equals(priority) ? "高优先级"
                : ("medium".equals(priority) ? "中优先级" : "低优先级");
        Map<String, Object> g = new LinkedHashMap<>();
        g.put("priority", priority);
        g.put("priorityName", priorityName);
        g.put("title", tag + "成本差异治理（差异率 " + rate.toPlainString() + "%）");
        g.put("description", description);
        g.put("expectedEffect", effect);
        g.put("implementationPeriod", period);
        g.put("responsibleDepartment", dept);
        out.add(g);
    }
}
