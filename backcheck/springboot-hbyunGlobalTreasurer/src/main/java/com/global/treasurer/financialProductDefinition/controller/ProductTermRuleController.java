package com.global.treasurer.financialProductDefinition.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.global.treasurer.financialProductDefinition.entity.TblProductTermRule;
import com.global.treasurer.financialProductDefinition.service.TblProductTermRuleService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 产品期限规则管理Controller
 *
 * @author 华博云开发团队
 */
@RestController
@RequestMapping("/financial/product-definition/term-rule")
@Api(tags = "产品期限规则管理")
public class ProductTermRuleController {
    private static final Logger log = LoggerFactory.getLogger(ProductTermRuleController.class);

    @Autowired
    private TblProductTermRuleService termRuleService;
    @Resource
    private UserProvider userProvider;

    @PostMapping("/getList")
    @ApiOperation("分页查询产品期限规则列表")
    public String getList(@ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
                          @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
                          @ApiParam("规则编码") @RequestParam(required = false, defaultValue = "") String ruleCode,
                          @ApiParam("规则名称") @RequestParam(required = false, defaultValue = "") String ruleName,
                          @ApiParam("产品类型") @RequestParam(required = false, defaultValue = "") String productType,
                          @ApiParam("期限类型") @RequestParam(required = false, defaultValue = "") String termType,
                          @ApiParam("期限单位") @RequestParam(required = false, defaultValue = "") String termUnit,
                          @ApiParam("是否启用") @RequestParam(required = false) Integer isEnabled) {
        try {
            Long orgId = getOrgId();
            log.info("接收到查询参数 - pageNo:{}, pageSize:{}, ruleCode:{}, ruleName:{}, productType:{}, termType:{}, termUnit:{}, isEnabled:{}",
                    pageNo, pageSize, ruleCode, ruleName, productType, termType, termUnit, isEnabled);
            IPage<TblProductTermRule> result = termRuleService.getPage(pageNo, pageSize, ruleCode, ruleName, productType, termType, termUnit, isEnabled, orgId);
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());
            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("查询产品期限规则列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getById")
    @ApiOperation("根据ID查询产品期限规则详情")
    public String getById(@ApiParam("ID") @RequestParam Long ID) {
        try {
            TblProductTermRule entity = termRuleService.getDetail(ID);
            return entity == null ? JsonBean.error("数据不存在") : new JsonBean(1, "查询成功", entity).toString();
        } catch (Exception e) {
            log.error("查询产品期限规则详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    @ApiOperation("新增产品期限规则")
    public String create(@RequestBody TblProductTermRule entity) {
        try {
            entity.setOrgId(getOrgId());
            // 设置默认值
            if (entity.getProductId() == null) {
                entity.setProductId(0L); // 默认产品ID
            }
            if (entity.getIsEnabled() == null) {
                entity.setIsEnabled(1); // 默认启用
            }
            if (entity.getExtensionAllowed() == null) {
                entity.setExtensionAllowed(0); // 默认不允许展期
            }
            // 获取createBy
            String createBy = entity.getCreateBy();
            if (createBy == null || createBy.isEmpty()) {
                createBy = getCurrentUser();
            }

            TblProductTermRule result = termRuleService.create(entity, createBy);

            // 设置返回对象的前端需要的字段
            result.setRuleId(result.getId()); // 映射ID到ruleId
            if (result.getProductType() == null || result.getProductType().isEmpty()) {
                result.setProductType("BANK_WEALTH"); // 默认产品类型
            }
            if (result.getLockPeriod() == null) {
                result.setLockPeriod(0); // 默认锁定期
            }

            return new JsonBean(1, "创建成功", result).toString();
        } catch (Exception e) {
            log.error("创建产品期限规则失败", e);
            return JsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation("修改产品期限规则")
    public String update(@RequestBody TblProductTermRule entity) {
        try {
            // 确保ID存在
            if (entity.getId() == null && entity.getRuleId() != null) {
                entity.setId(entity.getRuleId());
            }

            // 设置默认值
            if (entity.getIsEnabled() == null) {
                entity.setIsEnabled(1);
            }
            if (entity.getExtensionAllowed() == null) {
                entity.setExtensionAllowed(0);
            }

            // 获取updateBy
            String updateBy = entity.getUpdateBy();
            if (updateBy == null || updateBy.isEmpty()) {
                updateBy = getCurrentUser();
            }

            boolean result = termRuleService.update(entity, updateBy);

            if (result) {
                // 查询更新后的数据返回
                TblProductTermRule updatedEntity = termRuleService.getById(entity.getId());
                if (updatedEntity != null) {
                    updatedEntity.setRuleId(updatedEntity.getId());
                    if (updatedEntity.getProductType() == null || updatedEntity.getProductType().isEmpty()) {
                        updatedEntity.setProductType("BANK_WEALTH");
                    }
                    if (updatedEntity.getLockPeriod() == null) {
                        updatedEntity.setLockPeriod(0);
                    }
                    return new JsonBean(1, "更新成功", updatedEntity).toString();
                }
            }

            return result ? new JsonBean(1, "更新成功", null).toString() : JsonBean.error("更新失败");
        } catch (Exception e) {
            log.error("更新产品期限规则失败", e);
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @GetMapping("/delete")
    @ApiOperation("删除产品期限规则")
    public String delete(@ApiParam("ID") @RequestParam Long ID) {
        try {
            log.info("删除产品期限规则，ID: {}", ID);
            if (ID == null || ID == 0) {
                log.error("ID为空，无法删除");
                return JsonBean.error("删除失败: ID不能为空");
            }

            // 先检查记录是否存在
            TblProductTermRule entity = termRuleService.getById(ID);
            if (entity == null) {
                log.error("记录不存在，ID: {}", ID);
                return JsonBean.error("删除失败: 记录不存在");
            }

            log.info("准备删除的记录: {}", entity);
            boolean result = termRuleService.delete(ID);
            log.info("删除结果: {}", result);
            return result ? new JsonBean(1, "删除成功", null).toString() : JsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除产品期限规则失败，ID: {}, 错误信息: {}", ID, e.getMessage(), e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchDelete")
    @ApiOperation("批量删除产品期限规则")
    public String batchDelete(@ApiParam("ID列表") @RequestBody Map<String, Object> params) {
        try {
            String IDs = params.get("IDs") != null ? params.get("IDs").toString() : "";
            String[] idArray = IDs.split(",");
            List<Long> idList = new java.util.ArrayList<>();
            for (String id : idArray) idList.add(Long.parseLong(id.trim()));
            return termRuleService.batchDelete(idList) ? new JsonBean(1, "批量删除成功", null).toString() : JsonBean.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除产品期限规则失败", e);
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/updateStatus")
    @ApiOperation("更新产品期限规则状态")
    public String updateStatus(@ApiParam("ID") @RequestParam Long ID, @ApiParam("状态") @RequestParam Integer isEnabled) {
        try {
            return termRuleService.updateStatus(ID, isEnabled, getCurrentUser()) ? new JsonBean(1, "状态更新成功", null).toString() : JsonBean.error("状态更新失败");
        } catch (Exception e) {
            log.error("更新产品期限规则状态失败", e);
            return JsonBean.error("状态更新失败: " + e.getMessage());
        }
    }

    @GetMapping("/getEnabledList")
    @ApiOperation("获取启用的产品期限规则列表")
    public String getEnabledList() {
        try {
            return new JsonBean(1, "查询成功", termRuleService.getEnabledList(getOrgId())).toString();
        } catch (Exception e) {
            log.error("获取启用的产品期限规则列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getByProductType")
    @ApiOperation("根据产品类型获取期限规则列表")
    public String getByProductType(@ApiParam("产品类型") @RequestParam String productType) {
        try {
            return new JsonBean(1, "查询成功", termRuleService.getByProductType(productType, getOrgId())).toString();
        } catch (Exception e) {
            log.error("根据产品类型获取期限规则列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/checkCodeUnique")
    @ApiOperation("检查产品期限规则编码唯一性")
    public String checkCodeUnique(@ApiParam("编码") @RequestParam String ruleCode, @ApiParam("排除ID") @RequestParam(required = false) Long excludeId) {
        try {
            boolean isUnique = termRuleService.checkCodeUnique(ruleCode, excludeId);
            return new JsonBean(isUnique ? 1 : 0, isUnique ? "编码可用" : "编码已存在", null).toString();
        } catch (Exception e) {
            log.error("检查编码唯一性失败", e);
            return JsonBean.error("检查失败: " + e.getMessage());
        }
    }

    @PostMapping("/calculate")
    @ApiOperation("期限计算")
    public String calculate(@ApiParam("起始日期") @RequestParam(required = false) String startDate,
                          @ApiParam("期限天数") @RequestParam(required = false) Integer termDays,
                          @ApiParam("期限月数") @RequestParam(required = false) Integer termMonths,
                          @ApiParam("期限年数") @RequestParam(required = false) Integer termYears,
                          @ApiParam("规则ID") @RequestParam Long ruleId) {
        try {
            log.info("期限计算请求参数 - startDate:{}, termDays:{}, termMonths:{}, termYears:{}, ruleId:{}",
                    startDate, termDays, termMonths, termYears, ruleId);

            String startDateStr = startDate != null ? startDate : "";

            if (ruleId == null) {
                return JsonBean.error("期限规则ID不能为空");
            }

            // 查询期限规则
            TblProductTermRule rule = termRuleService.getById(ruleId);
            if (rule == null) {
                return JsonBean.error("期限规则不存在");
            }

            log.info("期限规则详情: ruleId={}, minTerm={}, maxTerm={}, termUnit={}, lockPeriod={}",
                    rule.getRuleId(), rule.getMinTerm(), rule.getMaxTerm(), rule.getTermUnit(), rule.getLockPeriod());

            // 准备结果
            Map<String, Object> result = new HashMap<>();

            // 转换起始日期
            java.util.Date startDateDate = null;
            if (startDateStr != null && !startDateStr.isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    startDateDate = sdf.parse(startDateStr);
                } catch (Exception e) {
                    log.warn("起始日期格式错误: {}", startDateStr);
                }
            }

            // 计算总期限天数
            int totalDays = 0;
            Integer minTerm = rule.getMinTerm() != null ? rule.getMinTerm() : 0;
            Integer maxTerm = rule.getMaxTerm() != null ? rule.getMaxTerm() : 0;
            String termUnit = rule.getTermUnit();

            if ("YEARS".equals(termUnit)) {
                // 年份转天数
                if (termYears != null && termYears > 0) {
                    totalDays = termYears * 365;
                } else if (maxTerm > 0) {
                    totalDays = maxTerm * 365;
                }
            } else if ("MONTHS".equals(termUnit)) {
                // 月份数转天数
                if (termMonths != null && termMonths > 0) {
                    totalDays = termMonths * 30;
                } else if (maxTerm > 0) {
                    totalDays = maxTerm * 30;
                }
            } else {
                // DAYS单位
                if (termDays != null && termDays > 0) {
                    totalDays = termDays;
                } else if (maxTerm > 0) {
                    totalDays = maxTerm;
                }
            }

            // 计算到期日期
            java.util.Date maturityDate = null;
            if (startDateDate != null && totalDays > 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDateDate);
                calendar.add(Calendar.DAY_OF_MONTH, totalDays);
                maturityDate = calendar.getTime();
            }

            // 计算锁定到期日（如果有锁定期）
            java.util.Date lockExpiryDate = null;
            Integer lockPeriod = rule.getLockPeriod() != null ? rule.getLockPeriod() : 0;
            if (startDateDate != null && lockPeriod > 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDateDate);
                calendar.add(Calendar.DAY_OF_MONTH, lockPeriod);
                lockExpiryDate = calendar.getTime();
            }

            // 格式化输出日期
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            String maturityDateStr = maturityDate != null ? outputFormat.format(maturityDate) : "";
            String lockExpiryDateStr = lockExpiryDate != null ? outputFormat.format(lockExpiryDate) : "";
            String startDateOutputStr = startDateDate != null ? outputFormat.format(startDateDate) : "";

            // 计算剩余期限（从今天到到期日的天数）
            int remainingDays = 0;
            if (maturityDate != null) {
                Calendar today = Calendar.getInstance();
                long diff = maturityDate.getTime() - today.getTime().getTime();
                remainingDays = (int) (diff / (1000 * 60 * 60 * 24));
            }

            // 计算月份数和年份数
            int totalMonths = (int) Math.ceil((double) totalDays / 30);
            int remainingMonths = (int) Math.ceil((double) remainingDays / 30);
            int totalYears = (int) Math.ceil((double) totalDays / 365);
            int remainingYears = (int) Math.ceil((double) remainingDays / 365);

            // 设置结果
            result.put("startDate", startDateOutputStr);
            result.put("totalDays", totalDays);
            result.put("totalMonths", totalMonths);
            result.put("totalYears", totalYears);
            result.put("maturityDate", maturityDateStr);
            result.put("lockPeriod", lockPeriod);
            result.put("lockExpiryDate", lockExpiryDateStr);
            result.put("remainingDays", remainingDays > 0 ? remainingDays : 0);
            result.put("remainingMonths", remainingMonths > 0 ? remainingMonths : 0);
            result.put("remainingYears", remainingYears > 0 ? remainingYears : 0);
            Map<String, Object> ruleInfo = new HashMap<>();
            ruleInfo.put("ruleCode", rule.getRuleCode());
            ruleInfo.put("ruleName", rule.getRuleName());
            ruleInfo.put("termUnit", rule.getTermUnit());
            ruleInfo.put("minTerm", minTerm);
            ruleInfo.put("maxTerm", maxTerm);
            result.put("ruleInfo", ruleInfo);

            log.info("期限计算结果: {}", result);
            return new JsonBean(1, "计算成功", result).toString();
        } catch (Exception e) {
            log.error("期限计算失败", e);
            return JsonBean.error("计算失败: " + e.getMessage());
        }
    }

    @PostMapping("/getStatistics")
    @ApiOperation("获取产品期限规则统计信息")
    public String getStatistics() {
        try {
            Map<String, Object> result = new HashMap<>();
            Long orgId = getOrgId();

            // 获取所有期限规则
            List<TblProductTermRule> allRules = termRuleService.getEnabledList(orgId);
            int totalCount = allRules.size();

            // 统计短期（≤1年）、中期（1-5年）、长期（＞5年）
            int shortTermCount = 0;
            int mediumTermCount = 0;
            int longTermCount = 0;

            for (TblProductTermRule rule : allRules) {
                Integer maxTerm = rule.getMaxTerm();
                String termUnit = rule.getTermUnit();

                if (maxTerm == null) {
                    // 没有期限信息，默认短期
                    shortTermCount++;
                    continue;
                }

                // 根据期限单位和值判断短期/中期/长期
                if ("YEARS".equalsIgnoreCase(termUnit)) {
                    if (maxTerm <= 1) {
                        shortTermCount++;
                    } else if (maxTerm <= 5) {
                        mediumTermCount++;
                    } else {
                        longTermCount++;
                    }
                } else if ("MONTHS".equalsIgnoreCase(termUnit)) {
                    int years = maxTerm / 12;
                    if (years <= 1) {
                        shortTermCount++;
                    } else if (years <= 5) {
                        mediumTermCount++;
                    } else {
                        longTermCount++;
                    }
                } else {
                    // DAYS或其他单位，默认短期
                    shortTermCount++;
                }
            }

            // 返回前端期望的字段名
            result.put("totalRules", totalCount);
            result.put("shortTermRules", shortTermCount);
            result.put("mediumTermRules", mediumTermCount);
            result.put("longTermRules", longTermCount);

            log.info("期限规则统计: 总数={}, 短期={}, 中期={}, 长期={}", totalCount, shortTermCount, mediumTermCount, longTermCount);

            return new JsonBean(1, "查询成功", result).toString();
        } catch (Exception e) {
            log.error("获取产品期限规则统计信息失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    private Long getOrgId() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                Long orgId = loginStaff.getCurrentOrg().getOrgid().longValue();
                log.info("当前用户orgId: {}", orgId);
                // 临时方案:返回null查询所有数据
                log.warn("【临时方案】忽略用户orgId,查询所有组织数据");
                // 临时方案:返回null查询所有数据
                log.warn("【临时方案】忽略用户orgId,查询所有组织数据");
                return null; // 返回null以查询所有组织数据
            }
            return null;
        } catch (Exception e) {
            log.error("获取组织ID失败", e);
            return null;
        }
    }

    private String getCurrentUser() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            return loginStaff != null ? loginStaff.getUsername() : "system";
        } catch (Exception e) { return "system"; }
    }
}
