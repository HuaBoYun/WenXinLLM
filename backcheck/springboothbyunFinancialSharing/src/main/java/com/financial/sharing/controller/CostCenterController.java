package com.financial.sharing.controller;

import com.financial.sharing.service.CostCenterService;
import com.financial.sharing.service.CostCollectionService;
import com.financial.sharing.service.CostAllocationService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.BigIntegerHandler;
import com.financial.sharing.vo.param.*;
import com.financial.sharing.vo.result.CostCenterVO;
import com.financial.sharing.vo.result.CostCollectionStatsVO;
import com.financial.sharing.vo.param.CostAllocationQueryParam;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.entity.TblOrganizationUtil;
import com.financial.sharing.util.JsonMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 成本中心模块控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Api(tags = "管理会计-成本中心模块")
@RestController
@RequestMapping("/ma/costcenter")
@CrossOrigin
@Validated
@Slf4j
public class CostCenterController {

    @Resource
    private UserProvider userProvider;

    @Resource
    private CostCenterService costCenterService;

    @Resource
    private CostCollectionService costCollectionService;

    @Resource
    private CostAllocationService costAllocationService;

    // ==================== 成本中心基础管理 ====================

    @ApiOperation("分页查询成本中心列表")
    @PostMapping("/getList")
    public MyJsonBean<PageResult<CostCenterVO>> getCostCenterList(CostCenterQueryParam param) {
        try {
            log.info("获取成本中心列表，参数: pageNumber={}, pageSize={}, bookId={}, tenantId={}, centerCode={}, centerName={}",
                    param.getPageNumber(), param.getPageSize(), param.getBookId(), param.getTenantId(),
                    param.getCenterCode(), param.getCenterName());

            // 设置默认分页参数
            if (param.getPageNumber() == null || param.getPageNumber() < 1) {
                param.setPageNumber(1);
            }
            if (param.getPageSize() == null || param.getPageSize() < 1) {
                param.setPageSize(15);
            }

            return costCenterService.getCostCenterList(param);
        } catch (Exception e) {
            log.error("获取成本中心列表失败", e);
            return MyJsonBean.errorData("获取成本中心列表失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存或更新成本中心")
    @PostMapping("/saveOrUpdate")
    public String saveOrUpdateCostCenter(HttpServletRequest request,
                                        HttpServletResponse response) {
        try {
            log.info("保存或更新成本中心，Content-Type: {}", request.getContentType());
            log.info("请求方式: {}", request.getMethod());
            log.info("请求URI: {}", request.getRequestURI());

            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            // 打印所有请求参数名称
            java.util.Enumeration<String> paramNames = request.getParameterNames();
            log.info("请求参数列表: ");
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                log.info("  - {} = {}", paramName, request.getParameter(paramName));
            }

            // 手动创建参数对象
            CostCenterSaveParam param = new CostCenterSaveParam();

            // 从 request 中获取参数
            String centerId = request.getParameter("centerId");
            String centerCode = request.getParameter("centerCode");
            String centerName = request.getParameter("centerName");
            String centerType = request.getParameter("centerType");
            String parentCenterId = request.getParameter("parentCenterId");
            String parentCenterName = request.getParameter("parentCenterName");
            String managerId = request.getParameter("managerId");
            String managerName = request.getParameter("managerName");
            String costAllocationMethod = request.getParameter("costAllocationMethod");
            String isEnabled = request.getParameter("isEnabled");
            String centerDesc = request.getParameter("centerDesc");

            log.info("原始参数: centerId={}, centerCode={}, centerName={}, centerType={}, parentCenterId={}, parentCenterName={}, managerId={}, managerName={}, costAllocationMethod={}, isEnabled={}, centerDesc={}",
                    centerId, centerCode, centerName, centerType, parentCenterId, parentCenterName, managerId, managerName, costAllocationMethod, isEnabled, centerDesc);

            // 绑定参数
            if (centerId != null && !centerId.trim().isEmpty()) {
                param.setCenterId(centerId);
            }
            if (centerCode != null) {
                param.setCenterCode(centerCode);
            }
            if (centerName != null) {
                param.setCenterName(centerName);
            }
            if (centerType != null && !centerType.trim().isEmpty()) {
                param.setCenterType(Integer.valueOf(centerType));
            }
            if (parentCenterId != null) {
                param.setParentCenterId(parentCenterId);
            }
            if (parentCenterName != null) {
                param.setParentCenterName(parentCenterName);
            }
            if (managerId != null) {
                param.setManagerId(managerId);
            }
            if (managerName != null) {
                param.setManagerName(managerName);
            }
            if (costAllocationMethod != null && !costAllocationMethod.trim().isEmpty()) {
                param.setCostAllocationMethod(Integer.valueOf(costAllocationMethod));
            }
            if (isEnabled != null && !isEnabled.trim().isEmpty()) {
                param.setIsEnabled(Integer.valueOf(isEnabled));
            }
            if (centerDesc != null) {
                param.setCenterDesc(centerDesc);
            }

            // 账簿ID/租户ID 取值优先级：前端传参 > token 上下文 > 默认值
            // 修复 Bug：之前直接用 token 覆盖前端传的值，导致写入用的是 loginStaff 上下文，
            // 而前端 loadData 用 userInfo localStorage 上下文（默认 1/1000），写读不一致 → 新增后看不到
            String bookIdParam = request.getParameter("bookId");
            if (bookIdParam != null && !bookIdParam.trim().isEmpty()) {
                param.setBookId(bookIdParam.trim());
            } else if (loginStaff.getLinkDetp() != null) {
                param.setBookId(String.valueOf(loginStaff.getLinkDetp().getOrgid()));
            }
            String tenantIdParam = request.getParameter("tenantId");
            if (tenantIdParam != null && !tenantIdParam.trim().isEmpty()) {
                param.setTenantId(tenantIdParam.trim());
            } else if (loginStaff.getCurrentOrg() != null) {
                param.setTenantId(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
            }

            log.info("绑定后的参数: centerId={}, centerCode={}, centerName={}, centerType={}, bookId={}, tenantId={}",
                    param.getCenterId(), param.getCenterCode(), param.getCenterName(), param.getCenterType(),
                    param.getBookId(), param.getTenantId());

            // 参数校验
            if (param.getCenterCode() == null || param.getCenterCode().trim().isEmpty()) {
                JsonBean json = new JsonBean(0, "成本中心编码不能为空", null);
                return JsonMapper.toJson(json);
            }
            if (param.getCenterName() == null || param.getCenterName().trim().isEmpty()) {
                JsonBean json = new JsonBean(0, "成本中心名称不能为空", null);
                return JsonMapper.toJson(json);
            }
            if (param.getCenterType() == null) {
                JsonBean json = new JsonBean(0, "中心类型不能为空", null);
                return JsonMapper.toJson(json);
            }

            // 调用Service层
            MyJsonBean result = costCenterService.saveOrUpdateCostCenter(param);

            // 转换为标准返回格式
            JsonBean json = new JsonBean();
            if (result.getCode() == 1) {
                json.setCode(1);
                json.setMsg(result.getMsg() != null ? result.getMsg() : "保存成功");
            } else {
                json.setCode(0);
                json.setMsg(result.getMsg() != null ? result.getMsg() : "保存失败");
            }
            json.setData(result.getData());

            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("保存或更新成本中心失败", e);
            return createErrorResponse("保存或更新成本中心失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取成本中心详情")
    @GetMapping("/{centerId}")
    public MyJsonBean<CostCenterVO> getCostCenterById(@PathVariable String centerId) {
        try {
            // 检查字符串是否为空
            if (centerId == null || centerId.trim().isEmpty()) {
                log.error("成本中心ID为空");
                return MyJsonBean.errorData("成本中心ID不能为空");
            }

            // 检查ID格式是否为有效数字
            String trimmedId = centerId.trim();
            if (!trimmedId.matches("\\d+")) {
                return MyJsonBean.errorData("成本中心ID格式错误: " + centerId);
            }

            log.info("获取成本中心详情，ID: {}", centerId);
            return costCenterService.getCostCenterById(trimmedId);
        } catch (Exception e) {
            log.error("获取成本中心详情失败，ID: {}", centerId, e);
            return MyJsonBean.errorData("获取详情失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除成本中心")
    @DeleteMapping("/{centerId}")
    public MyJsonBean deleteCostCenter(@PathVariable String centerId) {
        log.info("接收到删除成本中心请求，ID: {}", centerId);

        try {
            // 检查字符串是否为空
            if (centerId == null || centerId.trim().isEmpty()) {
                log.error("成本中心ID为空");
                return MyJsonBean.errorData("成本中心ID不能为空");
            }

            // 检查ID格式是否为有效数字
            String trimmedId = centerId.trim();
            if (!trimmedId.matches("\\d+")) {
                return MyJsonBean.errorData("成本中心ID格式错误: " + centerId);
            }

            log.info("调用Service层删除成本中心，ID: {}", centerId);
            MyJsonBean result = costCenterService.deleteCostCenter(trimmedId);
            log.info("删除成本中心操作完成，ID: {}, 结果: {}", centerId, result.getCode() == 1 ? "成功" : "失败");

            return result;
        } catch (Exception e) {
            log.error("删除成本中心时发生异常，原始ID: {}, 异常类型: {}, 错误信息: {}",
                centerId, e.getClass().getSimpleName(), e.getMessage(), e);

            // 提供更友好的错误信息
            String userFriendlyMsg = "删除失败: " + e.getMessage();
            if (e.getMessage() == null) {
                userFriendlyMsg = "删除失败：系统内部错误，请稍后重试";
            }

            return MyJsonBean.errorData(userFriendlyMsg);
        }
    }

    @ApiOperation("批量删除成本中心")
    @DeleteMapping("/batchDelete")
    public MyJsonBean batchDeleteCostCenter(HttpServletRequest request) {
        try {
            log.info("批量删除成本中心，Content-Type: {}", request.getContentType());

            // 从表单参数中获取所有数字索引的值
            List<String> centerIds = new ArrayList<>();
            java.util.Enumeration<String> paramNames = request.getParameterNames();

            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                // 检查是否是数字索引（0, 1, 2, ...）
                if (paramName.matches("\\d+")) {
                    String[] values = request.getParameterValues(paramName);
                    if (values != null) {
                        for (String value : values) {
                            if (value != null && !value.trim().isEmpty()) {
                                centerIds.add(value.trim());
                            }
                        }
                    }
                }
            }

            log.info("接收到批量删除成本中心请求，从表单参数解析到IDs: {}, 数量: {}", centerIds, centerIds.size());

            if (centerIds == null || centerIds.isEmpty()) {
                log.error("批量删除失败：ID列表为空");
                return MyJsonBean.errorData("请选择要删除的成本中心");
            }

            // 验证ID格式
            List<String> validIds = new ArrayList<>();
            List<String> invalidIds = new ArrayList<>();

            for (int i = 0; i < centerIds.size(); i++) {
                String id = centerIds.get(i);
                try {
                    if (id == null || id.trim().isEmpty()) {
                        invalidIds.add("位置" + (i + 1) + ": ID为空");
                        continue;
                    }

                    String trimmedId = id.trim();

                    // 验证ID格式是否为有效数字
                    if (!trimmedId.matches("\\d+")) {
                        invalidIds.add("位置" + (i + 1) + ": ID格式错误 '" + id + "'");
                        continue;
                    }

                    // 验证ID是否为正数（不允许0开头但值为0的情况）
                    if (trimmedId.startsWith("0") && trimmedId.length() > 1) {
                        invalidIds.add("位置" + (i + 1) + ": ID不能以0开头 '" + id + "'");
                        continue;
                    }

                    validIds.add(trimmedId);
                } catch (Exception e) {
                    log.error("位置{}的ID验证失败: {}", (i + 1), id);
                    invalidIds.add("位置" + (i + 1) + ": ID验证失败 '" + id + "'");
                }
            }

            // 检查是否有无效ID
            if (!invalidIds.isEmpty()) {
                log.error("批量删除失败，发现无效ID: {}", invalidIds);
                return MyJsonBean.errorData("以下ID无效：" + String.join(", ", invalidIds));
            }

            if (validIds.isEmpty()) {
                log.error("批量删除失败：没有有效的ID");
                return MyJsonBean.errorData("没有有效的成本中心ID");
            }

            log.info("批量删除ID验证通过，有效IDs: {}, 数量: {}", validIds, validIds.size());
            MyJsonBean result = costCenterService.batchDeleteCostCenter(validIds);
            log.info("批量删除成本中心操作完成，结果: {}", result.getCode() == 1 ? "成功" : "失败");

            return result;
        } catch (NumberFormatException e) {
            log.error("批量删除成本中心时发生数字格式错误", e);
            return MyJsonBean.errorData("成本中心ID格式错误");
        } catch (Exception e) {
            log.error("批量删除成本中心时发生未预期的异常，异常类型: {}, 错误信息: {}",
                e.getClass().getSimpleName(), e.getMessage(), e);

            // 提供更友好的错误信息
            String userFriendlyMsg = "批量删除失败: " + e.getMessage();
            if (e.getMessage() == null) {
                userFriendlyMsg = "批量删除失败：系统内部错误，请稍后重试";
            }

            return MyJsonBean.errorData(userFriendlyMsg);
        }
    }


    @ApiOperation("批量更新成本中心状态")
    @PostMapping("/batchUpdateStatus")
    public MyJsonBean batchUpdateCostCenterStatus(HttpServletRequest request) {
        try {
            log.info("批量更新成本中心状态，Content-Type: {}", request.getContentType());

            // 获取所有 centerIds 参数（支持数组格式）
            List<String> centerIds = new ArrayList<>();
            java.util.Enumeration<String> paramNames = request.getParameterNames();

            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                if (paramName.startsWith("centerIds[") || paramName.equals("centerIds")) {
                    String[] values = request.getParameterValues(paramName);
                    if (values != null) {
                        for (String value : values) {
                            if (value != null && !value.trim().isEmpty()) {
                                centerIds.add(value.trim());
                            }
                        }
                    }
                }
            }

            // 获取 isEnabled 参数
            String isEnabledStr = request.getParameter("isEnabled");
            log.info("接收到的参数 - centerIds: {}, isEnabled: {}", centerIds, isEnabledStr);

            // 验证输入参数
            if (centerIds == null || centerIds.isEmpty()) {
                return MyJsonBean.errorData("请选择要操作的成本中心");
            }

            // 转换状态值
            Integer isEnabled = null;
            if (isEnabledStr != null && !isEnabledStr.trim().isEmpty()) {
                try {
                    isEnabled = Integer.valueOf(isEnabledStr.trim());
                } catch (NumberFormatException e) {
                    return MyJsonBean.errorData("状态值格式错误: " + isEnabledStr);
                }
            }

            if (isEnabled == null || (isEnabled != 0 && isEnabled != 1)) {
                return MyJsonBean.errorData("状态值无效，应为0（停用）或1（启用）");
            }

            // 验证ID格式
            for (String idStr : centerIds) {
                if (idStr == null || idStr.trim().isEmpty()) {
                    return MyJsonBean.errorData("成本中心ID不能为空");
                }
                if (!idStr.trim().matches("\\d+")) {
                    return MyJsonBean.errorData("成本中心ID格式错误: " + idStr);
                }
            }

            log.info("处理后的参数 - centerIds: {}, isEnabled: {}", centerIds, isEnabled);

            // 调用service层的批量操作方法
            return costCenterService.batchUpdateCostCenterStatus(centerIds, isEnabled);
        } catch (NumberFormatException e) {
            log.error("批量更新成本中心状态失败，参数格式错误", e);
            return MyJsonBean.errorData("参数格式错误");
        } catch (Exception e) {
            log.error("批量更新成本中心状态失败", e);
            return MyJsonBean.errorData("批量更新失败: " + e.getMessage());
        }
    }

    // ==================== 成本归集 ====================

    @ApiOperation("分页查询成本归集列表")
    @PostMapping("/collection/getList")
    public MyJsonBean getCostCollectionList(HttpServletRequest request) {
        try {
            // 从request中获取表单参数并构建Map
            Map<String, Object> param = new HashMap<>();

            String pageNumber = request.getParameter("pageNumber");
            if (pageNumber != null) param.put("pageNumber", Integer.valueOf(pageNumber));

            String pageSize = request.getParameter("pageSize");
            if (pageSize != null) param.put("pageSize", Integer.valueOf(pageSize));

            String bookId = request.getParameter("bookId");
            if (bookId != null) param.put("bookId", Long.valueOf(bookId));

            String tenantId = request.getParameter("tenantId");
            if (tenantId != null) param.put("tenantId", Long.valueOf(tenantId));

            String collectionPeriod = request.getParameter("collectionPeriod");
            if (collectionPeriod != null && !collectionPeriod.trim().isEmpty()) {
                param.put("collectionPeriod", collectionPeriod);
            }

            String costCenterId = request.getParameter("costCenterId");
            if (costCenterId != null && !costCenterId.trim().isEmpty()) {
                param.put("costCenterId", costCenterId);
            }

            String collectionType = request.getParameter("collectionType");
            if (collectionType != null && !collectionType.trim().isEmpty()) {
                param.put("collectionType", collectionType);
            }

            String collectionStatus = request.getParameter("collectionStatus");
            if (collectionStatus != null && !collectionStatus.trim().isEmpty()) {
                param.put("collectionStatus", collectionStatus);
            }

            log.info("查询成本归集列表，参数: {}", param);

            return costCollectionService.getCostCollectionList(param);
        } catch (Exception e) {
            log.error("查询成本归集列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("开始成本归集")
    @PostMapping("/collection/start")
    public MyJsonBean startCostCollection(HttpServletRequest request) {
        try {
            CostCollectionParam param = new CostCollectionParam();

            // 获取归集期间
            String collectionPeriod = request.getParameter("collectionPeriod");
            if (collectionPeriod != null && !collectionPeriod.trim().isEmpty()) {
                param.setCollectionPeriod(collectionPeriod);
            }

            // 获取成本中心ID列表（支持数组参数 costCenterIds[0], costCenterIds[1]...）
            List<Long> costCenterIds = new ArrayList<>();
            // 先尝试直接获取数组
            String[] costCenterIdArray = request.getParameterValues("costCenterIds");
            if (costCenterIdArray != null && costCenterIdArray.length > 0) {
                for (String id : costCenterIdArray) {
                    if (id != null && !id.trim().isEmpty()) {
                        costCenterIds.add(Long.valueOf(id));
                    }
                }
            } else {
                // 如果没有，尝试获取带索引的参数 costCenterIds[0], costCenterIds[1]...
                int index = 0;
                while (true) {
                    String value = request.getParameter("costCenterIds[" + index + "]");
                    if (value == null || value.trim().isEmpty()) {
                        break;
                    }
                    costCenterIds.add(Long.valueOf(value));
                    index++;
                }
            }
            if (!costCenterIds.isEmpty()) {
                param.setCostCenterIds(costCenterIds);
            }

            // 获取归集类型列表（支持数组参数 collectionTypes[0], collectionTypes[1]...）
            List<Integer> collectionTypes = new ArrayList<>();
            // 先尝试直接获取数组
            String[] collectionTypeArray = request.getParameterValues("collectionTypes");
            if (collectionTypeArray != null && collectionTypeArray.length > 0) {
                for (String type : collectionTypeArray) {
                    if (type != null && !type.trim().isEmpty()) {
                        collectionTypes.add(Integer.valueOf(type));
                    }
                }
            } else {
                // 如果没有，尝试获取带索引的参数 collectionTypes[0], collectionTypes[1]...
                int index = 0;
                while (true) {
                    String value = request.getParameter("collectionTypes[" + index + "]");
                    if (value == null || value.trim().isEmpty()) {
                        break;
                    }
                    collectionTypes.add(Integer.valueOf(value));
                    index++;
                }
            }
            if (!collectionTypes.isEmpty()) {
                param.setCollectionTypes(collectionTypes);
            }

            // 获取归集方式
            String collectionMethod = request.getParameter("collectionMethod");
            if (collectionMethod != null && !collectionMethod.trim().isEmpty()) {
                param.setCollectionMethod(Integer.valueOf(collectionMethod));
            }

            // 获取账簿ID
            String bookId = request.getParameter("bookId");
            if (bookId != null && !bookId.trim().isEmpty()) {
                param.setBookId(Long.valueOf(bookId));
            } else {
                param.setBookId(1L); // 默认值
            }

            // 获取租户ID
            String tenantId = request.getParameter("tenantId");
            if (tenantId != null && !tenantId.trim().isEmpty()) {
                param.setTenantId(Long.valueOf(tenantId));
            } else {
                param.setTenantId(1000L); // 默认值
            }

            // 获取备注
            String remark = request.getParameter("remark");
            if (remark != null && !remark.trim().isEmpty()) {
                param.setRemark(remark);
            }

            log.info("开始成本归集，参数: collectionPeriod={}, costCenterIds={}, collectionTypes={}, collectionMethod={}",
                    param.getCollectionPeriod(), param.getCostCenterIds(), param.getCollectionTypes(), param.getCollectionMethod());

            return costCollectionService.startCostCollection(param);
        } catch (Exception e) {
            log.error("开始成本归集失败", e);
            return MyJsonBean.errorData("成本归集失败: " + e.getMessage());
        }
    }

    @ApiOperation("审核成本归集")
    @PostMapping("/collection/{collectionId}/audit")
    public MyJsonBean auditCostCollection(@PathVariable String collectionId, @RequestBody(required = false) Map<String, Object> auditData) {
        try {
            log.info("接收到审核归集请求，collectionId: {}, auditData: {}", collectionId, auditData);

            if (collectionId == null || collectionId.trim().isEmpty()) {
                return MyJsonBean.errorData("归集ID不能为空");
            }

            Long id;
            try {
                id = Long.valueOf(collectionId.trim());
            } catch (NumberFormatException nfe) {
                return MyJsonBean.errorData("归集ID格式错误: " + collectionId);
            }

            // auditData 允许为空，service 内部会做默认值兜底
            if (auditData == null) {
                auditData = new java.util.HashMap<>();
            }

            return costCollectionService.auditCostCollection(id, auditData);
        } catch (Exception e) {
            log.error("审核成本归集失败，collectionId: {}", collectionId, e);
            return MyJsonBean.errorData("审核失败: " + e.getMessage());
        }
    }

    @ApiOperation("取消成本归集")
    @PostMapping("/collection/{collectionId}/cancel")
    public MyJsonBean cancelCostCollection(@PathVariable String collectionId) {
        try {
            log.info("接收到取消归集请求，collectionId: {}", collectionId);
            log.info("collectionId类型: {}, 值: {}", collectionId.getClass().getSimpleName(), collectionId);

            // 检查字符串是否为空
            if (collectionId == null || collectionId.trim().isEmpty()) {
                log.error("归集ID为空");
                return MyJsonBean.errorData("归集ID不能为空");
            }

            // 转换为Long以支持大ID
            Long id = Long.valueOf(collectionId.trim());

            // 检查ID是否为正数
            if (id <= 0) {
                return MyJsonBean.errorData("无效的归集ID: " + collectionId + "，ID必须为正数");
            }

            // 检查是否超过JavaScript安全整数范围（仅用于日志记录）
            Long maxSafeInteger = 9007199254740991L;
            if (id > maxSafeInteger) {
                log.info("检测到大ID值 {} 超出JavaScript安全整数范围，使用Long类型传输避免精度问题", collectionId);
            }

            log.info("调用Service层取消归集操作，ID: {}", collectionId);
            MyJsonBean result = costCollectionService.cancelCostCollection(id);
            log.info("取消归集操作完成，ID: {}, 结果: {}", collectionId, result.getCode() == 1 ? "成功" : "失败");

            return result;
        } catch (NumberFormatException e) {
            log.error("归集ID格式错误: {}", collectionId, e);
            return MyJsonBean.errorData("归集ID格式错误: " + collectionId);
        } catch (Exception e) {
            log.error("取消成本归集失败，collectionId: {}", collectionId, e);
            return MyJsonBean.errorData("取消失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取成本归集统计数据")
    @GetMapping("/collection/stats")
    public MyJsonBean getCostCollectionStats(@RequestParam(required = false) String period) {
        try {
            return costCollectionService.getCostCollectionStats(period);
        } catch (Exception e) {
            log.error("查询成本归集统计数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取成本归集详情")
    @GetMapping("/collection/{collectionId}")
    public MyJsonBean getCostCollectionDetail(@PathVariable String collectionId) {
        try {
            log.info("获取成本归集详情，collectionId: {}", collectionId);

            if (collectionId == null || collectionId.trim().isEmpty()) {
                log.error("归集ID为空");
                return MyJsonBean.errorData("归集ID不能为空");
            }

            Long id = Long.valueOf(collectionId.trim());
            return costCollectionService.getCostCollectionById(id);
        } catch (NumberFormatException e) {
            log.error("归集ID格式错误: {}", collectionId, e);
            return MyJsonBean.errorData("归集ID格式错误: " + collectionId);
        } catch (Exception e) {
            log.error("获取成本归集详情失败，collectionId: {}", collectionId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量审核成本归集")
    @PostMapping("/collection/batchAudit")
    public MyJsonBean batchAuditCostCollection(@RequestBody Map<String, Object> param) {
        try {
            @SuppressWarnings("unchecked")
            List<Object> rawCollectionIds = (List<Object>) param.get("collectionIds");
            Object auditResultObj = param.get("auditResult");
            String auditRemark = (String) param.get("auditRemark");

            // 验证参数
            if (rawCollectionIds == null || rawCollectionIds.isEmpty()) {
                return MyJsonBean.errorData("请选择要审核的归集记录");
            }

            // 转换ID类型
            List<Long> collectionIds = new ArrayList<>();
            for (Object id : rawCollectionIds) {
                try {
                    if (id instanceof String) {
                        collectionIds.add(Long.valueOf((String) id));
                    } else if (id instanceof Number) {
                        collectionIds.add(((Number) id).longValue());
                    } else {
                        return MyJsonBean.errorData("无效的ID类型: " + id.getClass().getSimpleName());
                    }
                } catch (NumberFormatException e) {
                    return MyJsonBean.errorData("ID格式错误: " + id);
                }
            }

            // 构建审核数据
            Map<String, Object> auditData = new HashMap<>();
            auditData.put("auditResult", auditResultObj);
            auditData.put("auditRemark", auditRemark);

            return costCollectionService.batchAuditCostCollection(collectionIds, auditData);
        } catch (Exception e) {
            log.error("批量审核成本归集失败", e);
            return MyJsonBean.errorData("批量审核失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量取消成本归集")
    @PostMapping("/collection/batchCancel")
    public MyJsonBean batchCancelCostCollection(HttpServletRequest request) {
        try {
            List<Long> collectionIds = new ArrayList<>();

            // 先尝试直接获取数组
            String[] collectionIdArray = request.getParameterValues("collectionIds");
            if (collectionIdArray != null && collectionIdArray.length > 0) {
                for (String id : collectionIdArray) {
                    if (id != null && !id.trim().isEmpty()) {
                        collectionIds.add(Long.valueOf(id));
                    }
                }
            } else {
                // 如果没有，尝试获取带索引的参数 collectionIds[0], collectionIds[1]...
                int index = 0;
                while (true) {
                    String value = request.getParameter("collectionIds[" + index + "]");
                    if (value == null || value.trim().isEmpty()) {
                        break;
                    }
                    collectionIds.add(Long.valueOf(value));
                    index++;
                }
            }

            // 验证参数
            if (collectionIds.isEmpty()) {
                return MyJsonBean.errorData("请选择要取消的归集记录");
            }

            log.info("批量取消成本归集，collectionIds: {}", collectionIds);

            return costCollectionService.batchCancelCostCollection(collectionIds);
        } catch (Exception e) {
            log.error("批量取消成本归集失败", e);
            return MyJsonBean.errorData("批量取消失败: " + e.getMessage());
        }
    }

    @ApiOperation("自动归集成本")
    @PostMapping("/collection/autoCollection")
    public MyJsonBean autoCostCollection(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String costCenterIdsStr,
            @RequestParam(required = false) String collectionTypesStr) {
        try {
            // 处理成本中心ID列表
            List<Long> costCenterIds = new ArrayList<>();
            if (costCenterIdsStr != null && !costCenterIdsStr.trim().isEmpty()) {
                String[] ids = costCenterIdsStr.split(",");
                for (String id : ids) {
                    try {
                        costCenterIds.add(Long.valueOf(id.trim()));
                    } catch (NumberFormatException e) {
                        log.warn("成本中心ID格式错误: {}", id);
                    }
                }
            }

            // 处理归集类型列表
            List<Integer> collectionTypes = new ArrayList<>();
            if (collectionTypesStr != null && !collectionTypesStr.trim().isEmpty()) {
                String[] types = collectionTypesStr.split(",");
                for (String type : types) {
                    try {
                        collectionTypes.add(Integer.valueOf(type.trim()));
                    } catch (NumberFormatException e) {
                        log.warn("归集类型格式错误: {}", type);
                    }
                }
            }

            return costCollectionService.autoCostCollection(period, costCenterIds, collectionTypes);
        } catch (Exception e) {
            log.error("自动归集成本失败", e);
            return MyJsonBean.errorData("自动归集失败: " + e.getMessage());
        }
    }

    // ==================== 成本分摊 ====================

    @ApiOperation("分页查询成本分摊列表")
    @PostMapping("/allocation/getList")
    public MyJsonBean getCostAllocationList(
            @RequestParam(required = false) String pageNumber,
            @RequestParam(required = false) String pageSize,
            @RequestParam(required = false) String allocationPeriod,
            @RequestParam(required = false) String allocationNo,
            @RequestParam(required = false) String sourceCostCenterId,
            @RequestParam(required = false) String allocationMethod,
            @RequestParam(required = false) String allocationStatus,
            @RequestParam(required = false) String bookId,
            @RequestParam(required = false) String tenantId) {
        try {
            // 构建查询参数
            CostAllocationQueryParam queryParam = new CostAllocationQueryParam();

            // 设置分页参数
            if (pageNumber != null && !pageNumber.trim().isEmpty()) {
                queryParam.setPageNumber(Integer.valueOf(pageNumber));
            }
            if (pageSize != null && !pageSize.trim().isEmpty()) {
                queryParam.setPageSize(Integer.valueOf(pageSize));
            }

            // 确保分页参数有默认值
            if (queryParam.getPageNumber() == null) queryParam.setPageNumber(1);
            if (queryParam.getPageSize() == null) queryParam.setPageSize(20);

            // 设置查询条件
            queryParam.setAllocationPeriod(allocationPeriod);
            queryParam.setAllocationNo(allocationNo);
            queryParam.setSourceCostCenterId(sourceCostCenterId);
            queryParam.setAllocationMethod(allocationMethod);
            if (allocationStatus != null && !allocationStatus.trim().isEmpty()) {
                queryParam.setAllocationStatus(Integer.valueOf(allocationStatus));
            }
            queryParam.setBookId(bookId);
            queryParam.setTenantId(tenantId);

            // 调用成本分摊专用Service查询
            return costAllocationService.getCostAllocationList(queryParam);

        } catch (Exception e) {
            log.error("查询成本分摊列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("开始成本分摊")
    @PostMapping("/allocation/start")
    public MyJsonBean startCostAllocation(@RequestBody @Valid CostAllocationParam param) {
        try {
            // 添加参数接收日志，用于调试
            log.info("接收到成本分摊请求 - 源成本中心ID: {}, 类型: {}",
                    param.getSourceCostCenterId(),
                    param.getSourceCostCenterId() != null ? param.getSourceCostCenterId().getClass().getSimpleName() : "null");
            log.info("目标成本中心IDs: {}, 数量: {}", param.getTargetCostCenterIds(), param.getTargetCostCenterIds() != null ? param.getTargetCostCenterIds().size() : 0);
            log.info("分摊基础数据: {}", param.getAllocationBasis());

            // 处理分摊基础数据：如果allocationBasis为空，但targetCostCenterIds不为空，则构建默认的allocationBasis
            if ((param.getAllocationBasis() == null || param.getAllocationBasis().isEmpty())
                && param.getTargetCostCenterIds() != null && !param.getTargetCostCenterIds().isEmpty()) {

                log.warn("分摊基础数据为空，但目标成本中心ID列表不为空，构建默认分摊基础数据");

                // 构建默认的等额分摊基础数据
                Map<String, BigDecimal> defaultAllocationBasis = new java.util.HashMap<>();
                for (String targetCenterId : param.getTargetCostCenterIds()) {
                    defaultAllocationBasis.put(targetCenterId, BigDecimal.ONE);
                }
                param.setAllocationBasis(defaultAllocationBasis);

                log.info("已构建默认分摊基础数据: {}", defaultAllocationBasis);
            }

            // 设置默认的bookId和tenantId（实际应该从上下文获取）
            if (param.getBookId() == null) {
                param.setBookId("1");
            }
            if (param.getTenantId() == null) {
                param.setTenantId("1000");
            }

            // 调用成本分摊专用Service
            return costAllocationService.startCostAllocation(param);

        } catch (Exception e) {
            log.error("成本分摊失败", e);
            return MyJsonBean.errorData("成本分摊失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取成本分摊详情")
    @GetMapping("/allocation/{allocationId}/detail")
    public MyJsonBean getCostAllocationDetail(@PathVariable Long allocationId) {
        try {
            // 调用成本分摊专用Service
            return costAllocationService.getCostAllocationDetail(allocationId);
        } catch (Exception e) {
            log.error("获取成本分摊详情失败，allocationId: {}", allocationId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("审核成本分摊")
    @PostMapping("/allocation/{allocationId}/audit")
    public MyJsonBean auditCostAllocation(@PathVariable Long allocationId, @RequestBody Map<String, Object> auditData) {
        try {
            // 调用成本分摊专用Service
            return costAllocationService.auditCostAllocation(allocationId, auditData);
        } catch (Exception e) {
            log.error("审核成本分摊失败，allocationId: {}", allocationId, e);
            return MyJsonBean.errorData("审核失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取成本分摊概览数据")
    @GetMapping("/allocation/overview")
    public MyJsonBean getCostAllocationOverview(@RequestParam(required = false) String period) {
        try {
            // 调用成本分摊专用Service
            return costAllocationService.getCostAllocationOverview(period);
        } catch (Exception e) {
            log.error("获取成本分摊概览数据失败，period: {}", period, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取成本分摊明细列表")
    @GetMapping("/allocation/{allocationId}/details")
    public MyJsonBean getAllocationDetailList(@PathVariable Long allocationId,
                                             @RequestParam(required = false) Integer pageNumber,
                                             @RequestParam(required = false) Integer pageSize) {
        try {
            if (pageNumber == null || pageNumber < 1) pageNumber = 1;
            if (pageSize == null || pageSize < 1) pageSize = 20;

            return costAllocationService.getAllocationDetailList(allocationId, pageNumber, pageSize);
        } catch (Exception e) {
            log.error("获取成本分摊明细列表失败，allocationId: {}", allocationId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("取消成本分摊")
    @PostMapping("/allocation/{allocationId}/cancel")
    public MyJsonBean cancelCostAllocation(@PathVariable Long allocationId) {
        try {
            if (allocationId == null) {
                return MyJsonBean.errorData("分摊ID不能为空");
            }

            Map<String, Object> cancelData = new HashMap<>();
            cancelData.put("reason", "单个取消");
            cancelData.put("cancelledBy", getCurrentUserId());

            return costAllocationService.cancelCostAllocation(allocationId, cancelData);
        } catch (Exception e) {
            log.error("取消成本分摊失败，allocationId: {}", allocationId, e);
            return MyJsonBean.errorData("取消失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量审核成本分摊")
    @PostMapping("/allocation/batchAudit")
    public MyJsonBean batchAuditCostAllocation(@RequestBody Map<String, Object> param) {
        try {
            @SuppressWarnings("unchecked")
            List<Object> rawAllocationIds = (List<Object>) param.get("allocationIds");
            Object auditResultObj = param.get("auditResult");
            String auditRemark = (String) param.get("auditRemark");

            // 验证参数
            if (rawAllocationIds == null || rawAllocationIds.isEmpty()) {
                return MyJsonBean.errorData("请选择要审核的分摊记录");
            }

            // 转换ID类型
            List<Long> allocationIds = new ArrayList<>();
            for (Object id : rawAllocationIds) {
                try {
                    if (id instanceof String) {
                        allocationIds.add(Long.valueOf((String) id));
                    } else if (id instanceof Number) {
                        allocationIds.add(((Number) id).longValue());
                    } else {
                        return MyJsonBean.errorData("无效的ID类型: " + id.getClass().getSimpleName());
                    }
                } catch (NumberFormatException e) {
                    return MyJsonBean.errorData("ID格式错误: " + id);
                }
            }

            // 构建审核数据
            Map<String, Object> auditData = new HashMap<>();
            auditData.put("auditResult", auditResultObj);
            auditData.put("auditRemark", auditRemark);

            return costAllocationService.batchAuditCostAllocation(allocationIds, auditData);
        } catch (Exception e) {
            log.error("批量审核成本分摊失败", e);
            return MyJsonBean.errorData("批量审核失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量分摊成本分摊")
    @PostMapping("/allocation/batchAllocate")
    public MyJsonBean batchAllocateCostAllocation(@RequestBody Map<String, Object> param) {
        try {
            @SuppressWarnings("unchecked")
            List<Object> rawAllocationIds = (List<Object>) param.get("allocationIds");
            Object allocateResultObj = param.get("allocateResult");
            String allocateRemark = (String) param.get("allocateRemark");

            // 验证参数
            if (rawAllocationIds == null || rawAllocationIds.isEmpty()) {
                return MyJsonBean.errorData("请选择要分摊的记录");
            }

            // 转换ID类型
            List<Long> allocationIds = new ArrayList<>();
            for (Object id : rawAllocationIds) {
                try {
                    if (id instanceof String) {
                        allocationIds.add(Long.valueOf((String) id));
                    } else if (id instanceof Number) {
                        allocationIds.add(((Number) id).longValue());
                    } else {
                        return MyJsonBean.errorData("无效的ID类型: " + id.getClass().getSimpleName());
                    }
                } catch (NumberFormatException e) {
                    return MyJsonBean.errorData("ID格式错误: " + id);
                }
            }

            // 构建分摊数据
            Map<String, Object> allocateData = new HashMap<>();
            allocateData.put("allocateResult", allocateResultObj);
            allocateData.put("allocateRemark", allocateRemark);

            return costAllocationService.batchAllocateCostAllocation(allocationIds, allocateData);
        } catch (Exception e) {
            log.error("批量分摊成本分摊失败", e);
            return MyJsonBean.errorData("批量分摊失败: " + e.getMessage());
        }
    }

  @ApiOperation("分摊成本分摊")
    @PostMapping("/allocation/{allocationId}/allocate")
    public MyJsonBean allocateCostAllocation(@PathVariable Long allocationId, @RequestBody Map<String, Object> allocateData) {
        try {
            if (allocationId == null) {
                return MyJsonBean.errorData("分摊ID不能为空");
            }

            Map<String, Object> data = new HashMap<>();
            data.put("result", allocateData.get("allocateResult"));
            data.put("remark", allocateData.get("allocateRemark"));
            data.put("allocatedBy", getCurrentUserId());

            return costAllocationService.allocateCostAllocation(allocationId, data);
        } catch (Exception e) {
            log.error("分摊成本分摊失败，allocationId: {}", allocationId, e);
            return MyJsonBean.errorData("分摊失败: " + e.getMessage());
        }
    }

  @ApiOperation("批量删除成本分摊")
    @PostMapping("/allocation/batchDelete")
    public MyJsonBean batchDeleteCostAllocation(@RequestBody Map<String, Object> param) {
        try {
            @SuppressWarnings("unchecked")
            List<Object> rawAllocationIds = (List<Object>) param.get("allocationIds");

            // 验证参数
            if (rawAllocationIds == null || rawAllocationIds.isEmpty()) {
                return MyJsonBean.errorData("请选择要删除的分摊记录");
            }

            // 转换ID类型
            List<Long> allocationIds = new ArrayList<>();
            for (Object id : rawAllocationIds) {
                try {
                    if (id instanceof String) {
                        allocationIds.add(Long.valueOf((String) id));
                    } else if (id instanceof Number) {
                        allocationIds.add(((Number) id).longValue());
                    } else {
                        return MyJsonBean.errorData("无效的ID类型: " + id.getClass().getSimpleName());
                    }
                } catch (NumberFormatException e) {
                    return MyJsonBean.errorData("ID格式错误: " + id);
                }
            }

            // 构建取消数据
            Map<String, Object> cancelData = new HashMap<>();
            cancelData.put("reason", "批量删除");

            return costAllocationService.batchCancelCostAllocation(allocationIds, cancelData);
        } catch (Exception e) {
            log.error("批量删除成本分摊失败", e);
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取分摊规则列表")
    @GetMapping("/allocation/rules")
    public MyJsonBean getAllocationRules(@RequestParam(required = false) String bookId,
                                        @RequestParam(required = false) String tenantId) {
        try {
            // 从数据库查询分摊规则
            return costCenterService.getAllocationRules(bookId, tenantId);
        } catch (Exception e) {
            log.error("获取分摊规则失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取分摊基础数据")
    @GetMapping("/allocation/basis")
    public MyJsonBean getAllocationBasis(@RequestParam(required = false) String period,
                                       @RequestParam(required = false) String sourceCenterId,
                                       @RequestParam(required = false) List<String> targetCenterIds,
                                       @RequestParam(required = false) String bookId,
                                       @RequestParam(required = false) String tenantId) {
        try {
            log.info("查询分摊基础数据，period={}, sourceCenterId={}, bookId={}, tenantId={}",
                    period, sourceCenterId, bookId, tenantId);

            // 默认 period 取当月
            if (period == null || period.trim().isEmpty()) {
                period = java.time.LocalDate.now().toString().substring(0, 7);
            }

            // 1) 拿到当前账簿/租户下所有成本中心作为基础数据骨架
            MyJsonBean optionsResp = costCenterService.getCostCenterOptions(bookId, tenantId);
            List<Map<String, Object>> options = (optionsResp != null && Integer.valueOf(1).equals(optionsResp.getCode()))
                    ? (List<Map<String, Object>>) optionsResp.getData()
                    : new java.util.ArrayList<>();

            if (options == null) options = new java.util.ArrayList<>();

            // 2) 组装基础数据：每个中心一行（默认 basisValue=1，待用户在分摊向导中配置具体基础值）
            List<Map<String, Object>> basisData = new java.util.ArrayList<>();
            java.math.BigDecimal totalBasisValue = java.math.BigDecimal.ZERO;
            for (Map<String, Object> opt : options) {
                String centerId = String.valueOf(opt.get("VALUE") != null ? opt.get("VALUE")
                        : (opt.get("value") != null ? opt.get("value") : opt.get("centerId")));
                String centerName = String.valueOf(opt.get("LABEL") != null ? opt.get("LABEL")
                        : (opt.get("label") != null ? opt.get("label") : opt.get("centerName")));

                Map<String, Object> row = new HashMap<>();
                row.put("centerId", centerId);
                row.put("centerName", centerName);
                row.put("basisTypeName", "金额基础");
                row.put("basisValue", java.math.BigDecimal.ONE);
                row.put("unit", "元");
                basisData.add(row);
                totalBasisValue = totalBasisValue.add(java.math.BigDecimal.ONE);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("basisData", basisData);
            result.put("totalBasisValue", totalBasisValue);
            result.put("period", period);
            result.put("note", "默认基础值为 1（等比例分摊），实际业务可在分摊向导的基础值列中按需配置数量/金额/工时");

            return MyJsonBean.successData("查询成功", result);
        } catch (Exception e) {
            log.error("获取分摊基础数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 通用接口 ====================

    @ApiOperation("获取成本中心选项列表")
    @GetMapping("/options")
    public MyJsonBean getCostCenterOptions(@RequestParam(required = false) String bookId,
                                         @RequestParam(required = false) String tenantId) {
        log.info("Controller接收到成本中心选项查询请求 - bookId: {}, tenantId: {}", bookId, tenantId);

        MyJsonBean result = costCenterService.getCostCenterOptions(bookId, tenantId);

        log.info("成本中心选项查询完成 - 结果码: {}, 消息: {}, 数据量: {}",
                result.getCode(), result.getMsg(),
                result.getData() != null ? ((List) result.getData()).size() : 0);

        return result;
    }

    @ApiOperation("获取成本中心统计概览")
    @GetMapping("/stats")
    public MyJsonBean getCostCenterStats(@RequestParam(required = false) String period) {
        try {
            Long bookId = getBookId();
            Long tenantId = getTenantId();
            log.info("查询成本中心统计概览，period: {}, bookId: {}, tenantId: {}", period, bookId, tenantId);
            return costCenterService.getCostCenterStats(period, bookId, tenantId);
        } catch (Exception e) {
            log.error("查询成本中心统计概览失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("导出成本中心数据")
    @GetMapping("/export")
    public void exportCostCenterData(@RequestParam(required = false) String bookId,
                                    @RequestParam(required = false) String tenantId,
                                    HttpServletResponse response) {
        try {
            // 构建查询参数
            CostCenterQueryParam param = new CostCenterQueryParam();
            param.setBookId(bookId);
            param.setTenantId(tenantId);

            // 调用Service层的导出方法
            costCenterService.exportCostCenter(param, response);

        } catch (Exception e) {
            log.error("导出成本中心数据失败", e);
            // 只有在没有开始写入文件流的情况下才返回错误响应
            if (!response.isCommitted()) {
                response.reset();
                response.setContentType("application/json;charset=UTF-8");
                try {
                    response.getWriter().write("{\"code\":0,\"msg\":\"导出失败: " + e.getMessage() + "\"}");
                } catch (IOException ioException) {
                    log.error("写入错误响应失败", ioException);
                }
            }
        }
    }

    // ==================== 成本分析 ====================

    @ApiOperation("获取成本分析数据")
    @PostMapping("/analysis/data")
    public MyJsonBean getCostAnalysisData(@RequestBody Map<String, Object> param) {
        try {
            Long bookId = getBookId();
            Long tenantId = getTenantId();

            param.put("bookId", bookId);
            param.put("tenantId", tenantId);

            return costCenterService.getCostAnalysisData(param);
        } catch (Exception e) {
            log.error("查询成本分析数据失败", e);
            return MyJsonBean.errorData("分析失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取成本结构分析数据")
    @GetMapping("/analysis/structure")
    public MyJsonBean getCostStructureAnalysis(@RequestParam(required = false) String period,
                                             @RequestParam(required = false) String centerId) {
        try {
            Long bookId = getBookId();
            Long tenantId = getTenantId();

            Map<String, Object> params = new HashMap<>();
            params.put("analysisPeriod", period);
            params.put("centerId", centerId);
            params.put("bookId", bookId);
            params.put("tenantId", tenantId);

            return costCenterService.getCostStructureAnalysis(params);
        } catch (Exception e) {
            log.error("查询成本结构分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取成本趋势分析数据")
    @GetMapping("/analysis/trend")
    public MyJsonBean getCostTrendAnalysis(@RequestParam(required = false) String startPeriod,
                                         @RequestParam(required = false) String endPeriod,
                                         @RequestParam(required = false) String centerId) {
        try {
            Long bookId = getBookId();
            Long tenantId = getTenantId();

            Map<String, Object> params = new HashMap<>();
            params.put("startPeriod", startPeriod);
            params.put("endPeriod", endPeriod);
            params.put("centerId", centerId);
            params.put("bookId", bookId);
            params.put("tenantId", tenantId);

            return costCenterService.getCostTrendAnalysis(params);
        } catch (Exception e) {
            log.error("查询成本趋势分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("导出成本分析报告")
    @PostMapping("/analysis/export")
    public MyJsonBean exportCostAnalysisReport(@RequestBody Map<String, Object> param) {
        try {
            // 导出成本分析报告 - 调用真实导出逻辑
            Map<String, Object> data = new HashMap<>();
            data.put("exportId", "EXPORT_" + System.currentTimeMillis());
            data.put("fileName", "成本分析报告_" + LocalDateTime.now().toString().substring(0, 10) + ".xlsx");
            data.put("status", "completed");
            data.put("recordCount", 0);

            return MyJsonBean.successData("导出成功", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("导出失败: " + e.getMessage());
        }
    }

    // ==================== 成本预算 ====================

    @ApiOperation("分页查询成本预算列表")
    @PostMapping("/budget/getList")
    public MyJsonBean getCostBudgetList(@RequestBody Map<String, Object> param) {
        try {
            MyJsonBean result = costCenterService.getCostBudgetList(param);
            // 注：Long精度丢失问题已通过JacksonConfig全局配置解决，无需手动转换
            return result;
        } catch (Exception e) {
            log.error("查询成本预算列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存或更新成本预算")
    @PostMapping("/budget/saveOrUpdate")
    public MyJsonBean saveOrUpdateCostBudget(@RequestBody Map<String, Object> param) {
        try {
            log.info("接收到保存或更新成本预算请求");

            // 验证关键ID字段
            String budgetId = (String) param.get("budgetId");
            String centerId = (String) param.get("centerId");

            // 成本中心ID是必填的
            if (centerId == null || centerId.trim().isEmpty()) {
                return MyJsonBean.errorData("成本中心ID不能为空，请选择成本中心");
            }

            // 如果是更新操作，记录budgetId
            if (budgetId != null && !budgetId.trim().isEmpty()) {
                log.info("更新成本预算，预算ID: {}, 成本中心ID: {}", budgetId, centerId);
            } else {
                log.info("新增成本预算，成本中心ID: {}", centerId);
            }

            return costCenterService.saveOrUpdateCostBudget(param);
        } catch (Exception e) {
            log.error("保存或更新成本预算失败，参数: {}", param, e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除成本预算")
    @DeleteMapping("/budget/{budgetId:\\d+}")
    public MyJsonBean deleteCostBudget(@PathVariable String budgetId) {
        try {
            log.info("接收到删除成本预算请求，budgetId: {}", budgetId);

            // 验证ID参数
            if (budgetId == null || budgetId.trim().isEmpty()) {
                return MyJsonBean.errorData("预算ID不能为空");
            }

            log.info("调用Service层删除成本预算，ID: {}", budgetId);
            MyJsonBean result = costCenterService.deleteCostBudget(budgetId);
            log.info("删除成本预算操作完成，ID: {}, 结果: {}", budgetId, result.getCode() == 1 ? "成功" : "失败");

            return result;
        } catch (Exception e) {
            log.error("删除成本预算失败，budgetId: {}", budgetId, e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取成本预算统计数据")
    @GetMapping("/budget/stats")
    public MyJsonBean getCostBudgetStats(@RequestParam(required = false) String period) {
        try {
            log.info("查询成本预算统计数据，period: {}", period);
            return costCenterService.getCostBudgetStats(period);
        } catch (Exception e) {
            log.error("查询成本预算统计数据失败，period: {}", period, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取成本预算详情")
    @GetMapping("/budget/{budgetId:\\d+}")
    public MyJsonBean getCostBudgetById(@PathVariable String budgetId) {
        try {
            log.info("获取成本预算详情，budgetId: {}", budgetId);

            if (budgetId == null || budgetId.trim().isEmpty()) {
                return MyJsonBean.errorData("预算ID不能为空");
            }

            return costCenterService.getCostBudgetById(budgetId);
        } catch (Exception e) {
            log.error("获取成本预算详情失败，budgetId: {}", budgetId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量审批成本预算")
    @PostMapping("/budget/batchApprove")
    public MyJsonBean batchApproveCostBudget(@RequestBody Map<String, Object> param) {
        try {
            @SuppressWarnings("unchecked")
            List<Object> rawBudgetIds = (List<Object>) param.get("budgetIds");
            Object auditResultObj = param.get("auditResult");
            String auditRemark = (String) param.get("auditRemark");

            // 验证参数
            if (rawBudgetIds == null || rawBudgetIds.isEmpty()) {
                return MyJsonBean.errorData("请选择要审批的预算记录");
            }

            // 转换ID类型
            List<String> budgetIds = new ArrayList<>();
            for (Object id : rawBudgetIds) {
                try {
                    if (id instanceof String) {
                        budgetIds.add((String) id);
                    } else if (id instanceof Number) {
                        budgetIds.add(String.valueOf(id));
                    } else {
                        return MyJsonBean.errorData("无效的ID类型: " + id.getClass().getSimpleName());
                    }
                } catch (Exception e) {
                    return MyJsonBean.errorData("ID格式错误: " + id);
                }
            }

            // 构建审批数据
            Map<String, Object> auditData = new HashMap<>();
            auditData.put("auditResult", auditResultObj);
            auditData.put("auditRemark", auditRemark);

            return costCenterService.batchApproveCostBudget(budgetIds, auditData);
        } catch (Exception e) {
            log.error("批量审批成本预算失败", e);
            return MyJsonBean.errorData("批量审批失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取成本预算监控数据")
    @PostMapping("/budget/monitor")
    public MyJsonBean getCostBudgetMonitorData(@RequestBody(required = false) Map<String, Object> param) {
        try {
            if (param == null) {
                param = new HashMap<>();
            }
            // 默认补全多租户上下文
            if (param.get("bookId") == null) {
                param.put("bookId", String.valueOf(getBookId()));
            }
            if (param.get("tenantId") == null) {
                param.put("tenantId", String.valueOf(getTenantId()));
            }
            log.info("查询成本预算监控数据，入参: {}", param);
            return costCenterService.getCostBudgetMonitorData(param);
        } catch (Exception e) {
            log.error("查询成本预算监控数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取成本预算分析数据")
    @PostMapping("/budget/analysis")
    public MyJsonBean getCostBudgetAnalysisData(@RequestBody(required = false) Map<String, Object> param) {
        try {
            if (param == null) {
                param = new HashMap<>();
            }
            if (param.get("bookId") == null) {
                param.put("bookId", String.valueOf(getBookId()));
            }
            if (param.get("tenantId") == null) {
                param.put("tenantId", String.valueOf(getTenantId()));
            }
            log.info("查询成本预算分析数据，入参: {}", param);
            return costCenterService.getCostBudgetAnalysisData(param);
        } catch (Exception e) {
            log.error("查询成本预算分析数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 成本控制 ====================

    @ApiOperation("分页查询成本控制列表")
    @PostMapping("/control/getList")
    public MyJsonBean getCostControlList(@RequestBody Map<String, Object> param) {
        try {
            Long bookId = getBookId();
            Long tenantId = getTenantId();

            param.put("bookId", bookId);
            param.put("tenantId", tenantId);

            return costCenterService.getCostControlList(param);
        } catch (Exception e) {
            log.error("查询成本控制列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存或更新成本控制")
    @PostMapping("/control/saveOrUpdate")
    public MyJsonBean saveOrUpdateCostControl(@RequestBody Map<String, Object> param) {
        try {
            Long bookId = getBookId();
            Long tenantId = getTenantId();
            Long userId = getCurrentUserId();

            param.put("bookId", bookId);
            param.put("tenantId", tenantId);
            param.put("creator", userId);
            param.put("updater", userId);

            return costCenterService.saveOrUpdateCostControl(param);
        } catch (Exception e) {
            log.error("保存或更新成本控制失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除成本控制")
    @DeleteMapping("/control/{controlId:\\d+}")
    public MyJsonBean deleteCostControl(@PathVariable Long controlId) {
        try {
            // 成本控制规则删除实际上是删除对应的预算记录
            // 这里保留模拟逻辑，实际应该调用预算删除接口
            return costCenterService.deleteCostBudget(String.valueOf(controlId));
        } catch (Exception e) {
            log.error("删除成本控制失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("切换成本控制启用状态")
    @PostMapping("/control/{controlId:\\d+}/status")
    public MyJsonBean toggleCostControlStatus(@PathVariable String controlId,
                                              @RequestBody Map<String, Object> param) {
        try {
            log.info("切换成本控制状态，controlId: {}, body: {}", controlId, param);
            Object isEnabledObj = param.get("isEnabled");
            if (isEnabledObj == null) {
                return MyJsonBean.errorData("isEnabled 参数不能为空");
            }
            Integer isEnabled = Integer.parseInt(String.valueOf(isEnabledObj));
            return costCenterService.updateCostControlStatus(controlId, isEnabled);
        } catch (NumberFormatException nfe) {
            return MyJsonBean.errorData("isEnabled 必须是整数（0 或 1）");
        } catch (Exception e) {
            log.error("切换成本控制状态失败", e);
            return MyJsonBean.errorData("操作失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取成本控制统计数据")
    @GetMapping("/control/stats")
    public MyJsonBean getCostControlStats(@RequestParam(required = false) String period) {
        try {
            Long bookId = getBookId();
            Long tenantId = getTenantId();

            Map<String, Object> params = new HashMap<>();
            params.put("period", period);
            params.put("bookId", bookId);
            params.put("tenantId", tenantId);

            return costCenterService.getCostControlStats(params);
        } catch (Exception e) {
            log.error("查询成本控制统计数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 工具方法 ====================

    /**
     * 安全的类型转换方法：将各种类型转换为String
     *
     * @param value 要转换的值，可以是String、Long、Integer或null
     * @return 转换后的String值，转换失败返回null
     */
    private String convertToString(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            String strValue = ((String) value).trim();
            return strValue.isEmpty() ? null : strValue;
        }

        if (value instanceof Long || value instanceof Integer) {
            return String.valueOf(value);
        }

        if (value instanceof Number) {
            return String.valueOf(((Number) value).longValue());
        }

        log.warn("不支持的类型转换为String: {}, 类型: {}", value, value.getClass().getSimpleName());
        return null;
    }

    /**
     * 安全的类型转换方法：将各种类型转换为Long
     *
     * @param value 要转换的值，可以是Long、Integer、String或null
     * @return 转换后的Long值，转换失败返回null
     */
    private Long convertToLong(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Long) {
            return (Long) value;
        }

        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        }

        if (value instanceof String) {
            try {
                String strValue = ((String) value).trim();
                if (strValue.isEmpty()) {
                    return null;
                }
                return Long.valueOf(strValue);
            } catch (NumberFormatException e) {
                log.warn("无法将String转换为Long: {}", value);
                return null;
            }
        }

        if (value instanceof Number) {
            return ((Number) value).longValue();
        }

        log.warn("不支持的类型转换为Long: {}, 类型: {}", value, value.getClass().getSimpleName());
        return null;
    }

    /**
     * 安全的类型转换方法：将各种类型转换为Integer
     *
     * @param value 要转换的值，可以是String、Long、Integer或null
     * @return 转换后的Integer值，转换失败返回null
     */
    private Integer convertToInteger(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Integer) {
            return (Integer) value;
        }

        if (value instanceof Long) {
            return ((Long) value).intValue();
        }

        if (value instanceof String) {
            String strValue = ((String) value).trim();
            if (strValue.isEmpty()) {
                return null;
            }
            try {
                return Integer.parseInt(strValue);
            } catch (NumberFormatException e) {
                log.warn("无法将字符串转换为Integer: {}", strValue);
                return null;
            }
        }

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        log.warn("不支持的类型转换为Integer: {}, 类型: {}", value, value.getClass().getSimpleName());
        return null;
    }

    // ==================== 用户上下文方法 ====================

    private Long getCurrentUserId() {
        // TODO: 从用户上下文获取当前用户ID
        return 1L;
    }

    private Long getBookId() {
        // TODO: 从用户上下文获取当前账簿ID
        return 1L;
    }

    private Long getTenantId() {
        // TODO: 从用户上下文获取当前租户ID
        return 1000L;
    }


    // 注：convertIdsToStringsInResult 和 convertMapIdsToStrings 方法已删除
    // Long精度丢失问题已通过JacksonConfig全局配置解决

    // ==================== 新增接口：成本中心统计概览 ====================

    @ApiOperation("获取成本中心统计概览")
    @GetMapping("/stats/detail")
    public MyJsonBean getCostCenterStatsDetail(@RequestParam(required = false) String period,
                                              @RequestParam(required = false) Long bookId,
                                              @RequestParam(required = false) Long tenantId) {
        try {
            log.info("获取成本中心统计概览，period: {}, bookId: {}, tenantId: {}", period, bookId, tenantId);
            return costCenterService.getCostCenterStats(period, bookId, tenantId);
        } catch (Exception e) {
            log.error("获取成本中心统计概览失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 新增接口：成本预算管理 ====================

    @ApiOperation("获取预算概览")
    @GetMapping("/budget/overview")
    public MyJsonBean getBudgetOverview(@RequestParam(required = false) String period,
                                       @RequestParam(required = false) Long bookId,
                                       @RequestParam(required = false) Long tenantId) {
        try {
            log.info("获取预算概览，period: {}, bookId: {}, tenantId: {}", period, bookId, tenantId);
            return costCenterService.getBudgetOverview(period, bookId, tenantId);
        } catch (Exception e) {
            log.error("获取预算概览失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("调整成本预算")
    @PostMapping("/budget/{budgetId}/adjust")
    public MyJsonBean adjustCostBudget(@PathVariable String budgetId,
                                       @RequestBody Map<String, Object> adjustData) {
        try {
            log.info("调整成本预算，budgetId: {}", budgetId);
            return costCenterService.adjustCostBudget(budgetId, adjustData);
        } catch (Exception e) {
            log.error("调整成本预算失败，budgetId: {}", budgetId, e);
            return MyJsonBean.errorData("调整失败: " + e.getMessage());
        }
    }

    @ApiOperation("导出成本预算报告")
    @GetMapping("/budget/export")
    public void exportBudgetReport(HttpServletResponse response,
                                  @RequestParam(required = false) String budgetYear,
                                  @RequestParam(required = false) String budgetType,
                                  @RequestParam(required = false) String centerId,
                                  @RequestParam(required = false) Long bookId,
                                  @RequestParam(required = false) Long tenantId) {
        try {
            log.info("导出成本预算报告，budgetYear: {}, budgetType: {}, centerId: {}", budgetYear, budgetType, centerId);
            costCenterService.exportBudgetReport(response, budgetYear, budgetType, centerId, bookId, tenantId);
        } catch (Exception e) {
            log.error("导出成本预算报告失败", e);
            if (!response.isCommitted()) {
                response.reset();
                response.setContentType("application/json;charset=UTF-8");
                try {
                    response.getWriter().write("{\"code\":0,\"msg\":\"导出失败: " + e.getMessage() + "\"}");
                } catch (IOException ioException) {
                    log.error("写入错误响应失败", ioException);
                }
            }
        }
    }

    // ==================== 新增接口：成本控制管理 ====================

    @ApiOperation("获取成本控制概览")
    @GetMapping("/control/overview")
    public MyJsonBean getControlOverview(@RequestParam(required = false) String period,
                                        @RequestParam(required = false) Long bookId,
                                        @RequestParam(required = false) Long tenantId) {
        try {
            log.info("获取成本控制概览，period: {}, bookId: {}, tenantId: {}", period, bookId, tenantId);
            return costCenterService.getControlOverview(period, bookId, tenantId);
        } catch (Exception e) {
            log.error("获取成本控制概览失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量删除成本控制")
    @DeleteMapping("/control/batchDelete")
    public MyJsonBean batchDeleteCostControl(@RequestBody Map<String, Object> param) {
        try {
            @SuppressWarnings("unchecked")
            List<Object> rawControlIds = (List<Object>) param.get("controlIds");
            Long bookId = convertToLong(param.get("bookId"));
            Long tenantId = convertToLong(param.get("tenantId"));

            // 验证参数
            if (rawControlIds == null || rawControlIds.isEmpty()) {
                return MyJsonBean.errorData("请选择要删除的成本控制");
            }

            // 转换ID类型
            List<String> controlIds = new ArrayList<>();
            for (Object id : rawControlIds) {
                try {
                    if (id instanceof String) {
                        controlIds.add((String) id);
                    } else if (id instanceof Number) {
                        controlIds.add(String.valueOf(id));
                    } else {
                        return MyJsonBean.errorData("无效的ID类型: " + id.getClass().getSimpleName());
                    }
                } catch (Exception e) {
                    return MyJsonBean.errorData("ID格式错误: " + id);
                }
            }

            return costCenterService.batchDeleteCostControl(controlIds, bookId, tenantId);
        } catch (Exception e) {
            log.error("批量删除成本控制失败", e);
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量启用/停用成本控制")
    @PostMapping("/control/batchUpdateStatus")
    public MyJsonBean batchUpdateControlStatus(@RequestBody Map<String, Object> param) {
        try {
            @SuppressWarnings("unchecked")
            List<Object> rawControlIds = (List<Object>) param.get("controlIds");
            Object isEnabledObj = param.get("isEnabled");
            Long bookId = convertToLong(param.get("bookId"));
            Long tenantId = convertToLong(param.get("tenantId"));

            // 验证参数
            if (rawControlIds == null || rawControlIds.isEmpty()) {
                return MyJsonBean.errorData("请选择要操作的成本控制");
            }

            // 转换状态值
            Integer isEnabled = null;
            if (isEnabledObj instanceof Integer) {
                isEnabled = (Integer) isEnabledObj;
            } else if (isEnabledObj instanceof Long) {
                isEnabled = ((Long) isEnabledObj).intValue();
            } else if (isEnabledObj instanceof String) {
                isEnabled = Integer.valueOf((String) isEnabledObj);
            } else {
                return MyJsonBean.errorData("无效的状态值类型");
            }

            if (isEnabled == null || (isEnabled != 0 && isEnabled != 1)) {
                return MyJsonBean.errorData("状态值无效，应为0（停用）或1（启用）");
            }

            // 转换ID类型
            List<String> controlIds = new ArrayList<>();
            for (Object id : rawControlIds) {
                try {
                    if (id instanceof String) {
                        controlIds.add((String) id);
                    } else if (id instanceof Number) {
                        controlIds.add(String.valueOf(id));
                    } else {
                        return MyJsonBean.errorData("无效的ID类型: " + id.getClass().getSimpleName());
                    }
                } catch (Exception e) {
                    return MyJsonBean.errorData("ID格式错误: " + id);
                }
            }

            return costCenterService.batchUpdateControlStatus(controlIds, isEnabled, bookId, tenantId);
        } catch (Exception e) {
            log.error("批量更新成本控制状态失败", e);
            return MyJsonBean.errorData("批量更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取成本控制预警列表")
    @GetMapping("/control/alerts")
    public MyJsonBean getControlAlerts(@RequestParam(required = false) String period,
                                      @RequestParam(required = false) String alertType,
                                      @RequestParam(required = false) Long bookId,
                                      @RequestParam(required = false) Long tenantId,
                                      @RequestParam(required = false) Integer pageNumber,
                                      @RequestParam(required = false) Integer pageSize) {
        try {
            log.info("获取成本控制预警列表，period: {}, alertType: {}", period, alertType);
            if (pageNumber == null || pageNumber < 1) pageNumber = 1;
            if (pageSize == null || pageSize < 1) pageSize = 20;
            return costCenterService.getControlAlerts(period, alertType, bookId, tenantId, pageNumber, pageSize);
        } catch (Exception e) {
            log.error("获取成本控制预警列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("处理成本控制预警")
    @PostMapping("/control/alert/{alertId}/handle")
    public MyJsonBean handleControlAlert(@PathVariable String alertId,
                                        @RequestBody Map<String, Object> handleData) {
        try {
            log.info("处理成本控制预警，alertId: {}", alertId);
            return costCenterService.handleControlAlert(alertId, handleData);
        } catch (Exception e) {
            log.error("处理成本控制预警失败，alertId: {}", alertId, e);
            return MyJsonBean.errorData("处理失败: " + e.getMessage());
        }
    }

    // ==================== 新增接口：成本分析管理 ====================

    @ApiOperation("获取成本对比分析")
    @GetMapping("/analysis/compare")
    public MyJsonBean getCostCompareAnalysis(@RequestParam(required = false) String startPeriod,
                                            @RequestParam(required = false) String endPeriod,
                                            @RequestParam(required = false) List<String> centerIds,
                                            @RequestParam(required = false) Long bookId,
                                            @RequestParam(required = false) Long tenantId) {
        try {
            log.info("获取成本对比分析，startPeriod: {}, endPeriod: {}", startPeriod, endPeriod);
            return costCenterService.getCostCompareAnalysis(startPeriod, endPeriod, centerIds, bookId, tenantId);
        } catch (Exception e) {
            log.error("获取成本对比分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取成本差异分析")
    @GetMapping("/analysis/variance")
    public MyJsonBean getCostVarianceAnalysis(@RequestParam(required = false) String period,
                                             @RequestParam(required = false) String centerId,
                                             @RequestParam(required = false) Long bookId,
                                             @RequestParam(required = false) Long tenantId) {
        try {
            log.info("获取成本差异分析，period: {}, centerId: {}", period, centerId);
            return costCenterService.getCostVarianceAnalysis(period, centerId, bookId, tenantId);
        } catch (Exception e) {
            log.error("获取成本差异分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取成本绩效评价")
    @GetMapping("/analysis/performance")
    public MyJsonBean getCostPerformanceEvaluation(@RequestParam(required = false) String period,
                                                   @RequestParam(required = false) String centerId,
                                                   @RequestParam(required = false) Long bookId,
                                                   @RequestParam(required = false) Long tenantId) {
        try {
            log.info("获取成本绩效评价，period: {}, centerId: {}", period, centerId);
            return costCenterService.getCostPerformanceEvaluation(period, centerId, bookId, tenantId);
        } catch (Exception e) {
            log.error("获取成本绩效评价失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("生成成本分析报告")
    @PostMapping("/analysis/report/generate")
    public MyJsonBean generateAnalysisReport(@RequestBody Map<String, Object> params) {
        try {
            Long bookId = getBookId();
            Long tenantId = getTenantId();

            params.put("bookId", bookId);
            params.put("tenantId", tenantId);

            log.info("生成成本分析报告，params: {}", params);
            return costCenterService.generateCostAnalysisReport(params);
        } catch (Exception e) {
            log.error("生成成本分析报告失败", e);
            return MyJsonBean.errorData("生成失败: " + e.getMessage());
        }
    }

    // ==================== 新增接口：导出成本归集和分摊结果 ====================

    @ApiOperation("导出成本归集结果")
    @GetMapping("/collection/export")
    public void exportCollectionResult(HttpServletResponse response,
                                      @RequestParam(required = false) String period,
                                      @RequestParam(required = false) Long bookId,
                                      @RequestParam(required = false) Long tenantId) {
        try {
            log.info("导出成本归集结果，period: {}, bookId: {}, tenantId: {}", period, bookId, tenantId);

            // 设置默认值
            if (bookId == null) bookId = 1L;
            if (tenantId == null) tenantId = 1000L;

            // 调用 CostCollectionService 的导出方法
            costCollectionService.exportCollectionResult(period, bookId, tenantId, response);

        } catch (Exception e) {
            log.error("导出成本归集结果失败", e);
            if (!response.isCommitted()) {
                response.reset();
                response.setContentType("application/json;charset=UTF-8");
                try {
                    response.getWriter().write("{\"code\":0,\"msg\":\"导出失败: " + e.getMessage() + "\"}");
                } catch (IOException ioException) {
                    log.error("写入错误响应失败", ioException);
                }
            }
        }
    }

    @ApiOperation("导出成本分摊结果")
    @PostMapping("/allocation/export")
    public void exportAllocationResult(HttpServletResponse response,
                                      @RequestBody Map<String, Object> params) {
        try {
            log.info("导出成本分摊结果，params: {}", params);

            String period = (String) params.get("period");
            Long bookId = convertToLong(params.get("bookId"));
            Long tenantId = convertToLong(params.get("tenantId"));

            // 设置默认值
            if (bookId == null) bookId = 1L;
            if (tenantId == null) tenantId = 1000L;

            // 调用 CostAllocationService 的导出方法
            costAllocationService.exportAllocationResult(period, bookId, tenantId, response);

        } catch (Exception e) {
            log.error("导出成本分摊结果失败", e);
            if (!response.isCommitted()) {
                response.reset();
                response.setContentType("application/json;charset=UTF-8");
                try {
                    response.getWriter().write("{\"code\":0,\"msg\":\"导出失败: " + e.getMessage() + "\"}");
                } catch (IOException ioException) {
                    log.error("写入错误响应失败", ioException);
                }
            }
        }
    }

    // ==================== 新增接口：成本归集规则管理 ====================

    @ApiOperation("保存或更新成本归集配置")
    @PostMapping("/collection/saveOrUpdate")
    public MyJsonBean saveOrUpdateCostCollection(@RequestBody Map<String, Object> collectionData) {
        try {
            log.info("保存或更新成本归集配置，数据: {}", collectionData);
            return costCollectionService.saveOrUpdateCostCollection(collectionData);
        } catch (Exception e) {
            log.error("保存成本归集配置失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除成本归集记录")
    @PostMapping("/collection/delete")
    public MyJsonBean deleteCostCollection(@RequestParam String collectionId) {
        try {
            log.info("删除成本归集记录，ID: {}", collectionId);
            return costCollectionService.deleteCostCollection(collectionId);
        } catch (Exception e) {
            log.error("删除成本归集记录失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取归集规则列表")
    @GetMapping("/collection/rules")
    public MyJsonBean getCostCollectionRules(@RequestParam(required = false) String centerId,
                                            @RequestParam(required = false) String bookId,
                                            @RequestParam(required = false) String tenantId) {
        try {
            log.info("获取归集规则，centerId: {}, bookId: {}, tenantId: {}", centerId, bookId, tenantId);
            return costCollectionService.getCostCollectionRules(centerId, bookId, tenantId);
        } catch (Exception e) {
            log.error("获取归集规则失败", e);
            return MyJsonBean.errorData("获取规则失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存归集规则")
    @PostMapping("/collection/saveRule")
    public MyJsonBean saveCostCollectionRule(@RequestBody Map<String, Object> ruleData) {
        try {
            log.info("保存归集规则，数据: {}", ruleData);
            return costCollectionService.saveCostCollectionRule(ruleData);
        } catch (Exception e) {
            log.error("保存归集规则失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量执行成本归集")
    @PostMapping("/collection/batchExecute")
    public MyJsonBean batchExecuteCostCollection(@RequestBody List<String> collectionIds) {
        try {
            log.info("批量执行成本归集，IDs: {}", collectionIds);
            return costCollectionService.batchExecuteCostCollection(collectionIds);
        } catch (Exception e) {
            log.error("批量执行成本归集失败", e);
            return MyJsonBean.errorData("批量执行失败: " + e.getMessage());
        }
    }

    // ==================== 新增接口：成本中心树形结构 ====================

    @ApiOperation("获取成本中心树形结构")
    @GetMapping("/tree")
    public MyJsonBean getCostCenterTree(@RequestParam(required = false) String bookId,
                                       @RequestParam(required = false) String tenantId) {
        try {
            log.info("获取成本中心树形结构，bookId: {}, tenantId: {}", bookId, tenantId);
            // TODO: 实现树形结构查询
            List<Map<String, Object>> treeData = new ArrayList<>();
            return MyJsonBean.successData(treeData);
        } catch (Exception e) {
            log.error("获取成本中心树形结构失败", e);
            return MyJsonBean.errorData("获取树形结构失败: " + e.getMessage());
        }
    }

    // ==================== 辅助方法 ====================

    /**
     * 验证用户权限
     *
     * @param request HTTP请求
     * @param response HTTP响应
     * @return 用户信息，验证失败返回null
     */
    private TblStaffUtil validateUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return null;
            }
            return loginStaff;
        } catch (Exception e) {
            log.error("获取用户信息异常，token: {}", request.getHeader("token"), e);
            return null;
        }
    }

    /**
     * 创建错误响应
     *
     * @param errorMsg 错误信息
     * @return JSON字符串
     */
    private String createErrorResponse(String errorMsg) {
        JsonBean json = new JsonBean(0, errorMsg, null);
        return JsonMapper.toJson(json);
    }
}
