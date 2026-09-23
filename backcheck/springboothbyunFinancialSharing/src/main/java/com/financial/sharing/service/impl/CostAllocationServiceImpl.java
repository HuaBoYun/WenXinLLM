package com.financial.sharing.service.impl;

import com.financial.sharing.oracle.entity.CostAllocationEntity;
import com.financial.sharing.oracle.entity.CostAllocationDetailEntity;
import com.financial.sharing.oracle.mapper.CostAllocationMapper;
import com.financial.sharing.service.CostAllocationService;
import com.financial.sharing.service.CostCenterService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.SnowflakeIdWorker;
import com.financial.sharing.vo.param.CostAllocationParam;
import com.financial.sharing.vo.param.CostAllocationQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 成本分摊Service实现类
 *
 * @author Financial Sharing System
 * @since 2024-12-06
 */
@Service
@Slf4j
public class CostAllocationServiceImpl implements CostAllocationService {

    @Resource
    private CostAllocationMapper costAllocationMapper;

    @Resource
    private CostCenterService costCenterService;

    private final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1, 1);

    // ==================== 核心业务方法 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean<CostAllocationEntity> startCostAllocation(CostAllocationParam param) {
        try {
            log.info("开始成本分摊，参数: {}", param);

            // 1. 验证参数
            MyJsonBean validationResult = validateAllocationParameters(param);
            if (validationResult.getCode() != 1) {
                return validationResult;
            }

            // 2. 验证成本中心
            MyJsonBean centerValidationResult = validateCostCenters(param);
            if (centerValidationResult.getCode() != 1) {
                return centerValidationResult;
            }

            // 3. 生成分摊记录
            CostAllocationEntity allocation = createCostAllocationEntity(param);
            List<CostAllocationDetailEntity> details = createAllocationDetails(param, allocation.getAllocationId());

            // 4. 保存数据
            costAllocationMapper.insertCostAllocation(allocation);
            if (!CollectionUtils.isEmpty(details)) {
                costAllocationMapper.batchInsertAllocationDetails(details);
            }

            // 5. 生成凭证（如果需要）
            if (Boolean.TRUE.equals(param.getIsGenerateVoucher())) {
                generateVoucher(allocation);
            }

            log.info("成本分摊创建成功，分摊ID: {}, 分摊单号: {}", allocation.getAllocationId(), allocation.getAllocationNo());
            return MyJsonBean.successData("成本分摊创建成功", allocation);

        } catch (Exception e) {
            log.error("成本分摊创建失败", e);
            return MyJsonBean.errorData("成本分摊失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<PageResult<Map<String, Object>>> getCostAllocationList(CostAllocationQueryParam param) {
        try {
            log.info("查询成本分摊列表，参数: {}", param.buildQueryDescription());

            // 设置默认值（替代过度防御的 isValid() 检查）
            // 真正非法的参数会在 mapper SQL 里被 if 判空过滤掉，不需要前置校验
            if (param.getPageNumber() == null || param.getPageNumber() < 1) param.setPageNumber(1);
            if (param.getPageSize() == null || param.getPageSize() < 1 || param.getPageSize() > 1000) param.setPageSize(20);

            // 查询数据
            List<Map<String, Object>> list = costAllocationMapper.selectCostAllocationPage(
                param, param.getOffset(), param.getPageSize());
            Long total = costAllocationMapper.selectCostAllocationCount(param);

            // 处理显示数据
            processDisplayData(list);

            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            pageResult.setTlist(list);
            pageResult.setTotalRecord(total.intValue());
            pageResult.setPageSize(param.getPageSize());
            pageResult.setCurrentPage(param.getPageNumber());

            log.info("查询成本分摊列表完成，总数: {}", total);
            return MyJsonBean.successData("查询成功", pageResult);

        } catch (Exception e) {
            log.error("查询成本分摊列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getCostAllocationDetail(Long allocationId) {
        try {
            log.info("查询成本分摊详情，ID: {}", allocationId);

            // 查询主记录
            CostAllocationEntity allocation = costAllocationMapper.selectCostAllocationById(allocationId);
            if (allocation == null) {
                return MyJsonBean.errorData("成本分摊记录不存在");
            }

            // 查询明细记录
            List<CostAllocationDetailEntity> details = costAllocationMapper.selectDetailsByAllocationId(allocationId);

            // 构建返回数据
            Map<String, Object> result = new HashMap<>();
            result.put("allocation", allocation);
            result.put("details", details);
            result.put("detailCount", details.size());

            // 计算统计信息
            BigDecimal totalAllocatedAmount = details.stream()
                .map(CostAllocationDetailEntity::getAllocatedAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            result.put("totalAllocatedAmount", totalAllocatedAmount);

            log.info("查询成本分摊详情完成，ID: {}, 明细数量: {}", allocationId, details.size());
            return MyJsonBean.successData("查询成功", result);

        } catch (Exception e) {
            log.error("查询成本分摊详情失败，ID: {}", allocationId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<PageResult<CostAllocationDetailEntity>> getAllocationDetailList(Long allocationId,
                                                                                      Integer pageNumber,
                                                                                      Integer pageSize) {
        try {
            log.info("查询成本分摊明细列表，分摊ID: {}, 页码: {}, 页大小: {}", allocationId, pageNumber, pageSize);

            // 参数验证
            if (allocationId == null) {
                return MyJsonBean.errorData("分摊ID不能为空");
            }
            if (pageNumber == null || pageNumber < 1) pageNumber = 1;
            if (pageSize == null || pageSize < 1) pageSize = 20;

            // 查询明细数据
            List<Map<String, Object>> detailMaps = costAllocationMapper.selectAllocationDetailPage(
                allocationId, (pageNumber - 1) * pageSize, pageSize);
            Long total = costAllocationMapper.selectAllocationDetailCount(allocationId);

            // 转换为实体对象
            List<CostAllocationDetailEntity> details = detailMaps.stream()
                .map(this::convertMapToDetailEntity)
                .collect(Collectors.toList());

            PageResult<CostAllocationDetailEntity> pageResult = new PageResult<>();
            pageResult.setTlist(details);
            pageResult.setTotalRecord(total.intValue());
            pageResult.setPageSize(pageSize);
            pageResult.setCurrentPage(pageNumber);

            return MyJsonBean.successData("查询成功", pageResult);

        } catch (Exception e) {
            log.error("查询成本分摊明细列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 状态管理方法 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean auditCostAllocation(Long allocationId, Map<String, Object> auditData) {
        try {
            log.info("审核成本分摊，ID: {}, 审核数据: {}", allocationId, auditData);

            // 验证记录存在
            CostAllocationEntity allocation = costAllocationMapper.selectCostAllocationById(allocationId);
            if (allocation == null) {
                return MyJsonBean.errorData("成本分摊记录不存在");
            }

            // 验证状态是否可审核
            if (!allocation.isAuditable()) {
                return MyJsonBean.errorData("当前状态不允许审核");
            }

            // 更新状态
            int updateCount = costAllocationMapper.batchUpdateAllocationStatus(
                Collections.singletonList(allocationId), 4, getCurrentUserId());

            if (updateCount > 0) {
                log.info("成本分摊审核成功，ID: {}", allocationId);
                Map<String, Object> result = new HashMap<>();
                result.put("allocationId", allocationId);
                result.put("status", 4);
                result.put("auditTime", LocalDateTime.now());
                return MyJsonBean.successData("审核成功", result);
            } else {
                return MyJsonBean.errorData("审核失败");
            }
        } catch (Exception e) {
            log.error("审核成本分摊失败，ID: {}", allocationId, e);
            return MyJsonBean.errorData("审核失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchAuditCostAllocation(List<Long> allocationIds, Map<String, Object> auditData) {
        try {
            log.info("批量审核成本分摊，数量: {}", allocationIds.size());

            if (CollectionUtils.isEmpty(allocationIds)) {
                return MyJsonBean.errorData("请选择要审核的分摊记录");
            }

            // 验证状态
            for (Long allocationId : allocationIds) {
                CostAllocationEntity allocation = costAllocationMapper.selectCostAllocationById(allocationId);
                if (allocation == null || !allocation.isAuditable()) {
                    return MyJsonBean.errorData("存在不可审核的记录");
                }
            }

            // 批量更新状态
            int updateCount = costAllocationMapper.batchUpdateAllocationStatus(
                allocationIds, 4, getCurrentUserId());

            Map<String, Object> result = new HashMap<>();
            result.put("totalCount", allocationIds.size());
            result.put("successCount", updateCount);
            result.put("auditTime", LocalDateTime.now());

            return MyJsonBean.successData("批量审核完成", result);

        } catch (Exception e) {
            log.error("批量审核成本分摊失败", e);
            return MyJsonBean.errorData("批量审核失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean allocateCostAllocation(Long allocationId, Map<String, Object> allocateData) {
        try {
            log.info("分摊成本分摊，ID: {}", allocationId);

            if (allocationId == null) {
                return MyJsonBean.errorData("分摊ID不能为空");
            }

            // 查询分摊记录
            CostAllocationEntity allocation = costAllocationMapper.selectCostAllocationById(allocationId);
            if (allocation == null) {
                return MyJsonBean.errorData("分摊记录不存在");
            }

            // 验证状态 - 只能分摊状态为待分摊(1)或分摊中(2)的记录
            if (allocation.getAllocationStatus() >= 3) {
                return MyJsonBean.errorData("该记录已分摊，无法重复操作");
            }

            // 更新状态为已分摊(3)，设置分摊日期
            allocation.setAllocationStatus(3); // 已分摊
            allocation.setAllocationDate(java.time.LocalDate.now());
            allocation.setUpdater(getCurrentUserId());
            allocation.setUpdateTime(LocalDateTime.now());

            int updateCount = costAllocationMapper.updateCostAllocation(allocation);
            if (updateCount > 0) {
                log.info("分摊操作成功，分摊ID: {}", allocationId);
                return MyJsonBean.successData("分摊成功");
            } else {
                return MyJsonBean.errorData("分摊失败：更新数据失败");
            }
        } catch (Exception e) {
            log.error("分摊成本分摊失败，ID: {}", allocationId, e);
            return MyJsonBean.errorData("分摊失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchAllocateCostAllocation(List<Long> allocationIds, Map<String, Object> allocateData) {
        try {
            log.info("批量分摊成本分摊，数量: {}", allocationIds.size());

            if (CollectionUtils.isEmpty(allocationIds)) {
                return MyJsonBean.errorData("请选择要分摊的记录");
            }

            // 验证状态
            for (Long allocationId : allocationIds) {
                CostAllocationEntity allocation = costAllocationMapper.selectCostAllocationById(allocationId);
                if (allocation == null) {
                    return MyJsonBean.errorData("存在不存在的分摊记录");
                }
                // 只能分摊状态为待分摊(1)或分摊中(2)的记录
                if (allocation.getAllocationStatus() >= 3) {
                    return MyJsonBean.errorData("存在已分摊的记录，无法重复操作");
                }
            }

            // 批量更新状态为已分摊(3)，设置分摊日期
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            int updateCount = costAllocationMapper.batchUpdateAllocationStatusWithDate(
                allocationIds, 3, currentDate, getCurrentUserId());

            Map<String, Object> result = new HashMap<>();
            result.put("failedCount", 0);
            result.put("successCount", updateCount);
            return MyJsonBean.successData("批量分摊完成", result);
        } catch (Exception e) {
            log.error("批量分摊成本分摊失败", e);
            return MyJsonBean.errorData("批量分摊失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean cancelCostAllocation(Long allocationId, Map<String, Object> cancelData) {
        try {
            log.info("取消成本分摊，ID: {}", allocationId);

            CostAllocationEntity allocation = costAllocationMapper.selectCostAllocationById(allocationId);
            if (allocation == null) {
                return MyJsonBean.errorData("成本分摊记录不存在");
            }
            if (!allocation.isCancellable()) {
                return MyJsonBean.errorData("当前状态不允许取消");
            }

            // 软删除记录
            int updateCount = costAllocationMapper.deleteCostAllocationById(allocationId, getCurrentUserId());
            costAllocationMapper.deleteDetailsByAllocationId(allocationId, getCurrentUserId());

            if (updateCount > 0) {
                Map<String, Object> result = new HashMap<>();
                result.put("allocationId", allocationId);
                result.put("cancelTime", LocalDateTime.now());
                return MyJsonBean.successData("取消成功", result);
            } else {
                return MyJsonBean.errorData("取消失败");
            }
        } catch (Exception e) {
            log.error("取消成本分摊失败，ID: {}", allocationId, e);
            return MyJsonBean.errorData("取消失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getCostAllocationOverview(String period) {
        try {
            log.info("查询成本分摊概览数据，期间: {}", period);

            // 设置默认期间
            if (!StringUtils.hasText(period)) {
                period = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            }

            Map<String, Object> overview = costAllocationMapper.selectAllocationOverview(
                period, getCurrentBookId(), getCurrentTenantId());

            // 补充默认数据
            if (overview == null) {
                overview = new HashMap<>();
                overview.put("totalAmount", BigDecimal.ZERO);
                overview.put("allocationCount", 0);
                overview.put("centerCount", 0);
                overview.put("avgAllocationRate", 0.0);
                overview.put("growthRate", 0.0);
            }
            return MyJsonBean.successData("查询成功", overview);

        } catch (Exception e) {
            log.error("查询成本分摊概览数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 验证成本分摊参数
     */
    public MyJsonBean validateAllocationParameters(CostAllocationParam param) {
        if (param == null) {
            return MyJsonBean.errorData("参数不能为空");
        }
        if (!StringUtils.hasText(param.getAllocationPeriod())) {
            return MyJsonBean.errorData("分摊期间不能为空");
        }
        if (!StringUtils.hasText(param.getSourceCostCenterId())) {
            return MyJsonBean.errorData("源成本中心ID不能为空");
        }
        if (CollectionUtils.isEmpty(param.getTargetCostCenterIds())) {
            return MyJsonBean.errorData("目标成本中心ID不能为空");
        }
        if (!StringUtils.hasText(param.getAllocationMethod())) {
            return MyJsonBean.errorData("分摊方法不能为空");
        }
        if (param.getTotalAmount() == null || param.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return MyJsonBean.errorData("分摊总额必须大于0");
        }
        return MyJsonBean.successData("验证通过");
    }

    /**
     * 验证成本中心
     */
    private MyJsonBean validateCostCenters(CostAllocationParam param) {
        try {
            // 验证源成本中心
            MyJsonBean sourceResult = costCenterService.getCostCenterById(param.getSourceCostCenterId());
            if (sourceResult.getCode() != 1 || sourceResult.getData() == null) {
                return MyJsonBean.errorData("源成本中心不存在: " + param.getSourceCostCenterId());
            }

            // 验证目标成本中心
            Set<String> targetCenterIds = new HashSet<>(param.getTargetCostCenterIds());
            targetCenterIds.remove(param.getSourceCostCenterId()); // 源中心不能是目标中心

            for (String targetId : targetCenterIds) {
                MyJsonBean targetResult = costCenterService.getCostCenterById(targetId);
                if (targetResult.getCode() != 1 || targetResult.getData() == null) {
                    return MyJsonBean.errorData("目标成本中心不存在: " + targetId);
                }
            }
            return MyJsonBean.successData("成本中心验证通过");

        } catch (Exception e) {
            log.error("验证成本中心失败", e);
            return MyJsonBean.errorData("验证成本中心失败: " + e.getMessage());
        }
    }

    /**
     * 创建成本分摊主记录
     */
    private CostAllocationEntity createCostAllocationEntity(CostAllocationParam param) {
        String allocationNo = generateAllocationNo();
        Long allocationId = snowflakeIdWorker.nextId();

        return CostAllocationEntity.create(
            allocationNo,
            param.getAllocationPeriod(),
            param.getSourceCostCenterId(),
            param.getAllocationMethod(),
            param.getTotalAmount(),
            getCurrentBookId(),
            getCurrentTenantId()
        ).setAllocationId(allocationId)
         .setCreator(getCurrentUserId())
         .setIsGenerateVoucher(Boolean.TRUE.equals(param.getIsGenerateVoucher()) ? 1 : 0);
    }

    /**
     * 创建分摊明细记录
     */
    private List<CostAllocationDetailEntity> createAllocationDetails(CostAllocationParam param, Long allocationId) {
        List<CostAllocationDetailEntity> details = new ArrayList<>();

        // 计算总分摊基础值
        BigDecimal totalBasis = param.getAllocationBasis().values().stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 为每个目标中心创建明细记录
        for (Map.Entry<String, BigDecimal> entry : param.getAllocationBasis().entrySet()) {
            String targetCenterId = entry.getKey();
            BigDecimal basisValue = entry.getValue();

            // 计算分摊比例和金额
            BigDecimal allocationRatio = basisValue.divide(totalBasis, 4, BigDecimal.ROUND_HALF_UP);
            BigDecimal allocatedAmount = param.getTotalAmount().multiply(allocationRatio)
                .setScale(2, BigDecimal.ROUND_HALF_UP);

            Long detailId = snowflakeIdWorker.nextId();
            CostAllocationDetailEntity detail = CostAllocationDetailEntity.create(
                allocationId,
                targetCenterId,
                basisValue,
                allocationRatio,
                allocatedAmount,
                getCurrentBookId(),
                getCurrentTenantId()
            ).setDetailId(detailId)
             .setCreator(getCurrentUserId());

            details.add(detail);
        }
        return details;
    }

    /**
     * 生成分摊单号
     */
    private String generateAllocationNo() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return "CA" + timestamp.substring(timestamp.length() - 10);
    }

    /**
     * 生成凭证
     */
    private void generateVoucher(CostAllocationEntity allocation) {
        try {
            // TODO: 调用凭证生成服务
            log.info("生成凭证，分摊ID: {}", allocation.getAllocationId());
        } catch (Exception e) {
            log.error("生成凭证失败", e);
            throw new RuntimeException("生成凭证失败: " + e.getMessage());
        }
    }

    /**
     * 处理显示数据
     */
    private void processDisplayData(List<Map<String, Object>> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        for (Map<String, Object> item : list) {
            // 添加状态名称
            Integer status = (Integer) item.get("allocationStatus");
            if (status != null) {
                item.put("allocationStatusName", getStatusName(status));
            }

            // 添加方法名称
            String method = (String) item.get("allocationMethod");
            if (StringUtils.hasText(method)) {
                item.put("allocationMethodName", getMethodName(method));
            }

            // 格式化金额
            Object amount = item.get("totalAmount");
            if (amount instanceof BigDecimal) {
                BigDecimal amt = (BigDecimal) amount;
                item.put("totalAmountFormatted", amt.divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_HALF_UP) + "万");
            }
        }
    }

    /**
     * 获取状态名称
     */
    private String getStatusName(Integer status) {
        switch (status) {
            case 1: return "待分摊";
            case 2: return "分摊中";
            case 3: return "已分摊";
            case 4: return "已审核";
            default: return "未知状态";
        }
    }

    /**
     * 获取方法名称
     */
    private String getMethodName(String method) {
        switch (method) {
            case "QUANTITY_BASED": return "数量基础";
            case "AMOUNT_BASED": return "金额基础";
            case "RATIO_BASED": return "比例基础";
            case "HOUR_BASED": return "工时基础";
            default: return "未知方法";
        }
    }

    /**
     * 转换Map为明细实体
     */
    private CostAllocationDetailEntity convertMapToDetailEntity(Map<String, Object> map) {
        CostAllocationDetailEntity entity = new CostAllocationDetailEntity();

        // 防空处理 - 使用大写字段名（对应数据库返回的字段名）
        Object detailIdObj = map.get("detailId") != null ? map.get("detailId") : map.get("DETAILID");
        Object allocationIdObj = map.get("allocationId") != null ? map.get("allocationId") : map.get("ALLOCATIONID");
        Object targetCenterIdObj = map.get("targetCenterId") != null ? map.get("targetCenterId") : map.get("TARGETCENTERID");
        Object allocationBasisValueObj = map.get("allocationBasisValue") != null ? map.get("allocationBasisValue") : map.get("ALLOCATIONBASISVALUE");
        Object allocationRatioObj = map.get("allocationRatio") != null ? map.get("allocationRatio") : map.get("ALLOCATIONRATIO");
        Object allocatedAmountObj = map.get("allocatedAmount") != null ? map.get("allocatedAmount") : map.get("ALLOCATEDAMOUNT");
        Object remarkObj = map.get("remark") != null ? map.get("remark") : map.get("REMARK");
        Object targetCenterNameObj = map.get("targetCenterName") != null ? map.get("targetCenterName") : map.get("TARGETCENTERNAME");

        // 设置属性值，添加空值检查
        if (detailIdObj != null) {
            entity.setDetailId(((Number) detailIdObj).longValue());
        }
        if (allocationIdObj != null) {
            entity.setAllocationId(((Number) allocationIdObj).longValue());
        }
        if (targetCenterIdObj != null) {
            entity.setTargetCenterId(String.valueOf(targetCenterIdObj));
        }
        if (allocationBasisValueObj != null) {
            entity.setAllocationBasisValue(new BigDecimal(String.valueOf(allocationBasisValueObj)));
        }
        if (allocationRatioObj != null) {
            entity.setAllocationRatio(new BigDecimal(String.valueOf(allocationRatioObj)));
        }
        if (allocatedAmountObj != null) {
            entity.setAllocatedAmount(new BigDecimal(String.valueOf(allocatedAmountObj)));
        }
        if (remarkObj != null) {
            entity.setRemark(String.valueOf(remarkObj));
        }
        if (targetCenterNameObj != null) {
            entity.setTargetCenterName(String.valueOf(targetCenterNameObj));
        }
        return entity;
    }

    // ==================== 用户上下文方法 ====================

    private Long getCurrentUserId() {
        // TODO: 从用户上下文获取当前用户ID
        return 1L;
    }

    private String getCurrentBookId() {
        // TODO: 从用户上下文获取当前账簿ID
        return "1";
    }

    private String getCurrentTenantId() {
        // TODO: 从用户上下文获取当前租户ID
        return "1000";
    }

    // ==================== 其他接口方法的简单实现 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchCancelCostAllocation(List<Long> allocationIds, Map<String, Object> cancelData) {
        try {
            log.info("批量取消成本分摊，数量: {}", allocationIds.size());

            if (CollectionUtils.isEmpty(allocationIds)) {
                return MyJsonBean.errorData("请选择要取消的分摊记录");
            }

            // 验证状态
            int successCount = 0;
            List<Long> failedIds = new ArrayList<>();

            for (Long allocationId : allocationIds) {
                CostAllocationEntity allocation = costAllocationMapper.selectCostAllocationById(allocationId);
                if (allocation == null) {
                    failedIds.add(allocationId);
                    continue;
                }
                if (!allocation.isCancellable()) {
                    failedIds.add(allocationId);
                    continue;
                }

                // 软删除主记录
                int updateCount = costAllocationMapper.deleteCostAllocationById(allocationId, getCurrentUserId());
                if (updateCount > 0) {
                    // 软删除明细记录
                    costAllocationMapper.deleteDetailsByAllocationId(allocationId, getCurrentUserId());
                    successCount++;
                } else {
                    failedIds.add(allocationId);
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("totalCount", allocationIds.size());
            result.put("successCount", successCount);
            result.put("failedCount", failedIds.size());
            result.put("failedIds", failedIds);
            result.put("cancelTime", LocalDateTime.now());

            String message;
            if (failedIds.isEmpty()) {
                message = "批量取消完成";
            } else {
                message = String.format("批量取消完成，成功%d条，失败%d条", successCount, failedIds.size());
            }
            return MyJsonBean.successData(message, result);

        } catch (Exception e) {
            log.error("批量取消成本分摊失败", e);
            return MyJsonBean.errorData("批量取消失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getCostAllocationStats(CostAllocationQueryParam param) {
        // TODO: 实现统计信息查询
        return MyJsonBean.errorData("功能开发中");
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getAllocationTrend(String startPeriod, String endPeriod) {
        // TODO: 实现趋势数据查询
        return MyJsonBean.errorData("功能开发中");
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getAllocationMethodStats(CostAllocationQueryParam param) {
        // TODO: 实现方法统计
        return MyJsonBean.errorData("功能开发中");
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getAllocationStatusStats(CostAllocationQueryParam param) {
        // TODO: 实现状态统计
        return MyJsonBean.errorData("功能开发中");
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getCenterAllocationHistory(String centerId, Integer limit) {
        // TODO: 实现中心历史查询
        return MyJsonBean.errorData("功能开发中");
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getSourceCenterAllocation(String sourceCenterId, String period) {
        // TODO: 实现源中心分摊查询
        return MyJsonBean.errorData("功能开发中");
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getTargetCenterAllocation(String targetCenterId, String period) {
        // TODO: 实现目标中心分摊查询
        return MyJsonBean.errorData("功能开发中");
    }

    // 以下方法暂时返回错误信息，表示功能开发中
    @Override public MyJsonBean checkAllocationNoAvailable(String allocationNo, String bookId, String tenantId) { return MyJsonBean.errorData("功能开发中"); }
    @Override public MyJsonBean checkCentersForAllocation(String sourceCenterId, List<String> targetCenterIds, String period) { return MyJsonBean.errorData("功能开发中"); }
    @Override public MyJsonBean recalculateAllocationAmount(Long allocationId, Double newTotalAmount) { return MyJsonBean.errorData("功能开发中"); }
    @Override public MyJsonBean adjustAllocationDetail(Long detailId, Double newRatio) { return MyJsonBean.errorData("功能开发中"); }
    @Override public MyJsonBean batchUpdateAllocationDetails(Long allocationId, List<CostAllocationDetailEntity> details) { return MyJsonBean.errorData("功能开发中"); }
    @Override public MyJsonBean generateAllocationVoucher(Long allocationId) { return MyJsonBean.errorData("功能开发中"); }
    @Override public MyJsonBean cancelAllocationVoucher(Long allocationId) { return MyJsonBean.errorData("功能开发中"); }
    @Override public MyJsonBean batchGenerateAllocationVouchers(List<Long> allocationIds) { return MyJsonBean.errorData("功能开发中"); }
    @Override public MyJsonBean exportAllocationData(CostAllocationQueryParam param) { return MyJsonBean.errorData("功能开发中"); }
    @Override public MyJsonBean exportAllocationDetailData(Long allocationId) { return MyJsonBean.errorData("功能开发中"); }
    @Override public MyJsonBean exportAllocationStatsReport(CostAllocationQueryParam param) { return MyJsonBean.errorData("功能开发中"); }
    @Override public MyJsonBean cleanupHistoryData(String beforeDate) { return MyJsonBean.errorData("功能开发中"); }
    @Override public MyJsonBean rebuildStatistics() { return MyJsonBean.errorData("功能开发中"); }
    @Override public MyJsonBean<Map<String, Object>> getSystemConfig() { return MyJsonBean.errorData("功能开发中"); }
    @Override public MyJsonBean batchCreateAllocation(List<CostAllocationParam> params) { return MyJsonBean.errorData("功能开发中"); }
    @Override public MyJsonBean autoCostAllocation(String period, List<String> centerIds) { return MyJsonBean.errorData("功能开发中"); }
    @Override public MyJsonBean scheduleAllocation(Map<String, Object> scheduleConfig) { return MyJsonBean.errorData("功能开发中"); }
    @Override public MyJsonBean<Map<String, Object>> getAllocationExecutionStatus(Long allocationId) { return MyJsonBean.errorData("功能开发中"); }
    @Override public MyJsonBean<List<Map<String, Object>>> getExceptionAllocations(CostAllocationQueryParam param) { return MyJsonBean.errorData("功能开发中"); }
    @Override public MyJsonBean<List<Map<String, Object>>> getAllocationAlerts(String period) { return MyJsonBean.errorData("功能开发中"); }

    @Override
    public void exportAllocationResult(String period, Long bookId, Long tenantId, HttpServletResponse response) {
        try {
            log.info("导出成本分摊结果，期间: {}, bookId: {}, tenantId: {}", period, bookId, tenantId);

            // 查询导出数据
            List<Map<String, Object>> exportData = costAllocationMapper.selectCostAllocationListForExport(period, bookId, tenantId);

            if (exportData == null || exportData.isEmpty()) {
                throw new RuntimeException("没有找到可导出的数据");
            }

            // 生成文件名
            String fileName = "成本分摊结果_" + period + "_" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";

            // 创建Excel导出对象
            List<String> headers = Arrays.asList(
                "分摊ID", "分摊期间", "源中心编码", "源中心名称", "目标中心编码",
                "目标中心名称", "分摊金额", "分摊比例", "分摊方法", "分摊状态",
                "审核状态", "审核人", "审核时间", "创建时间", "账套ID"
            );
            List<Integer> widths = Arrays.asList(15, 12, 15, 20, 15, 20, 15, 12, 15, 12, 12, 15, 20, 20, 15);

            com.financial.sharing.util.excel.ExcelExport excelExport = new com.financial.sharing.util.excel.ExcelExport("成本分摊结果", headers, widths);

            // 填充数据
            for (Map<String, Object> data : exportData) {
                org.apache.poi.ss.usermodel.Row row = excelExport.addRow();

                excelExport.addCell(row, 0, getSafeStringIdValue(data, "ALLOCATION_ID"));
                excelExport.addCell(row, 1, getSafeStringValue(data, "ALLOCATION_PERIOD"));
                excelExport.addCell(row, 2, getSafeStringValue(data, "SOURCE_CENTER_CODE"));
                excelExport.addCell(row, 3, getSafeStringValue(data, "SOURCE_CENTER_NAME"));
                excelExport.addCell(row, 4, getSafeStringValue(data, "TARGET_CENTER_CODE"));
                excelExport.addCell(row, 5, getSafeStringValue(data, "TARGET_CENTER_NAME"));
                excelExport.addCell(row, 6, getSafeBigDecimalValue(data, "ALLOCATION_AMOUNT"));
                excelExport.addCell(row, 7, getSafeBigDecimalValue(data, "ALLOCATION_RATIO"));
                excelExport.addCell(row, 8, getSafeStringValue(data, "ALLOCATION_METHOD_NAME"));
                excelExport.addCell(row, 9, getSafeStringValue(data, "STATUS_NAME"));
                excelExport.addCell(row, 10, getSafeStringValue(data, "AUDIT_STATUS_NAME"));
                excelExport.addCell(row, 11, getSafeStringValue(data, "AUDITOR_NAME"));
                excelExport.addCell(row, 12, data.get("AUDIT_TIME"));
                excelExport.addCell(row, 13, data.get("CREATE_TIME"));
                excelExport.addCell(row, 14, getSafeStringIdValue(data, "BOOK_ID"));
            }

            // 导出文件
            try {
                excelExport.write(response, fileName);
            } finally {
                excelExport.close();
            }

            log.info("成本分摊结果导出成功，导出记录数: {}", exportData.size());

        } catch (Exception e) {
            log.error("导出成本分摊结果失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }

    // 辅助方法
    private String getSafeStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        String strValue = value.toString();
        return "null".equals(strValue) || "".equals(strValue) ? null : strValue;
    }

    private String getSafeStringIdValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null || "".equals(value) || "null".equals(value)) {
            return null;
        }
        return value.toString();
    }

    private java.math.BigDecimal getSafeBigDecimalValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null || "".equals(value) || "null".equals(value)) {
            return null;
        }
        try {
            return new java.math.BigDecimal(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}