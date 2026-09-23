package com.financial.sharing.service.impl;

import com.financial.sharing.config.DateBaseConfig;
import com.financial.sharing.oracle.entity.RevenueAllocationEntity;
import com.financial.sharing.oracle.mapper.RevenueAllocationMapper;
import com.financial.sharing.service.RevenueAllocationService;
import com.financial.sharing.util.LegalDealUserToken;
import com.financial.sharing.util.PageableParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收入分配Service实现类
 */
@Slf4j
@Service
public class RevenueAllocationServiceImpl implements RevenueAllocationService {

    @Autowired
    private DateBaseConfig dateBaseConfig;

    @Override
    public Map<String, Object> getAllocationList(PageableParam param) {
        try {
            // 获取Mapper
            RevenueAllocationMapper mapper = dateBaseConfig.getOracleRevenueAllocationMapper();

            // 计算偏移量
            int offset = (param.getPageNumber() - 1) * param.getPageSize();

            // 获取搜索参数 - 从param.getParam()中获取
            Map<String, Object> searchParams = (Map<String, Object>) param.getParam();
            String allocationNo = null;
            Integer allocationType = null;
            Integer allocationStatus = null;
            String allocationPeriod = null;
            Long tenantId = 1L; // 默认租户ID

            if (searchParams != null) {
                allocationNo = (String) searchParams.get("allocationNo");
                allocationPeriod = (String) searchParams.get("allocationPeriod");

                // 获取租户ID
                Object tenantIdObj = searchParams.get("tenantId");
                if (tenantIdObj != null) {
                    if (tenantIdObj instanceof Long) {
                        tenantId = (Long) tenantIdObj;
                    } else if (tenantIdObj instanceof Integer) {
                        tenantId = ((Integer) tenantIdObj).longValue();
                    } else {
                        tenantId = Long.parseLong(tenantIdObj.toString());
                    }
                }

                // 处理allocationType
                Object allocationTypeObj = searchParams.get("allocationType");
                if (allocationTypeObj != null && !allocationTypeObj.toString().isEmpty()) {
                    allocationType = Integer.parseInt(allocationTypeObj.toString());
                }

                // 处理allocationStatus
                Object allocationStatusObj = searchParams.get("allocationStatus");
                if (allocationStatusObj != null && !allocationStatusObj.toString().isEmpty()) {
                    allocationStatus = Integer.parseInt(allocationStatusObj.toString());
                }
            }

            // 查询列表
            List<Map<String, Object>> list = mapper.selectAllocationList(
                    allocationNo, allocationType, allocationStatus, allocationPeriod,
                    tenantId, offset, param.getPageSize()
            );

            // 查询总数
            Integer total = mapper.countAllocationList(
                    allocationNo, allocationType, allocationStatus, allocationPeriod, tenantId
            );

            // 处理列表数据,转换字段名为驼峰命名并添加类型名称
            for (Map<String, Object> item : list) {
                // 兼容大写和小写字段名 - 获取分配类型
                Object typeObj = item.get("allocationType");
                if (typeObj == null) {
                    typeObj = item.get("ALLOCATIONTYPE");
                }
                Integer type = null;
                if (typeObj != null) {
                    if (typeObj instanceof Integer) {
                        type = (Integer) typeObj;
                    } else if (typeObj instanceof Number) {
                        type = ((Number) typeObj).intValue();
                    }
                }
                item.put("allocationTypeName", getAllocationTypeName(type));

                // 统一转换字段名为小写驼峰命名，确保前端能正确显示
                convertFieldNamesToCamelCase(item);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("tlist", list);
            result.put("total", total);
            result.put("pageNumber", param.getPageNumber());
            result.put("pageSize", param.getPageSize());

            return result;
        } catch (Exception e) {
            log.error("查询收入分配列表失败", e);
            throw new RuntimeException("查询收入分配列表失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> createAllocation(Map<String, Object> allocationData) {
        try {
            log.info("创建收入分配,参数: {}", allocationData);

            // 获取Mapper
            RevenueAllocationMapper mapper = dateBaseConfig.getOracleRevenueAllocationMapper();

            // 构建实体
            RevenueAllocationEntity entity = new RevenueAllocationEntity();

            // 生成主键ID - 使用雪花算法生成唯一ID
            long allocationId = com.baomidou.mybatisplus.core.toolkit.IdWorker.getId();
            entity.setAllocationId(allocationId);
            log.info("生成分配ID: {}", allocationId);

            // 分配单号
            String allocationNo = (String) allocationData.get("allocationNo");
            entity.setAllocationNo(allocationNo);

            // 分配类型
            Object allocationTypeObj = allocationData.get("allocationType");
            if (allocationTypeObj != null) {
                if (allocationTypeObj instanceof Integer) {
                    entity.setAllocationType((Integer) allocationTypeObj);
                } else {
                    entity.setAllocationType(Integer.parseInt(allocationTypeObj.toString()));
                }
            }

            // 分配期间
            String allocationPeriod = (String) allocationData.get("allocationPeriod");
            entity.setAllocationPeriod(allocationPeriod);

            // 总金额
            Object totalAmountObj = allocationData.get("totalAmount");
            if (totalAmountObj != null) {
                if (totalAmountObj instanceof BigDecimal) {
                    entity.setTotalAmount((BigDecimal) totalAmountObj);
                } else {
                    entity.setTotalAmount(new BigDecimal(totalAmountObj.toString()));
                }
            } else {
                entity.setTotalAmount(BigDecimal.ZERO);
            }

            // 已分配金额 - 新建时默认为0
            entity.setAllocatedAmount(BigDecimal.ZERO);

            // 剩余金额 - 等于总金额
            entity.setRemainingAmount(entity.getTotalAmount());

            // 分配状态 - 新建时默认为待分配(0)
            entity.setAllocationStatus(0);

            // 分配日期
            entity.setAllocationDate(LocalDate.now());

            // 分配规则
            Object allocationRuleIdObj = allocationData.get("allocationRuleId");
            if (allocationRuleIdObj != null && !allocationRuleIdObj.toString().isEmpty()) {
                entity.setAllocationRule(allocationRuleIdObj.toString());
            }

            // 租户ID - 从参数或使用默认值
            Object tenantIdObj = allocationData.get("tenantId");
            if (tenantIdObj != null) {
                if (tenantIdObj instanceof Long) {
                    entity.setTenantId((Long) tenantIdObj);
                } else {
                    entity.setTenantId(Long.parseLong(tenantIdObj.toString()));
                }
            } else {
                entity.setTenantId(1L); // 默认租户ID
            }

            // 账簿ID
            Object bookIdObj = allocationData.get("bookId");
            if (bookIdObj != null) {
                if (bookIdObj instanceof Long) {
                    entity.setBookId((Long) bookIdObj);
                } else {
                    entity.setBookId(Long.parseLong(bookIdObj.toString()));
                }
            } else {
                entity.setBookId(1L); // 默认账簿ID
            }

            // 版本号
            entity.setVersion(1);

            // 删除标识
            entity.setIsDeleted(0);

            // 创建时间和更新时间
            LocalDateTime now = LocalDateTime.now();
            entity.setCreateTime(now);
            entity.setUpdateTime(now);

            // 创建人和更新人
            Object creatorObj = allocationData.get("creator");
            if (creatorObj != null) {
                if (creatorObj instanceof Long) {
                    entity.setCreator((Long) creatorObj);
                } else {
                    entity.setCreator(Long.parseLong(creatorObj.toString()));
                }
            } else {
                entity.setCreator(1L); // 默认创建人
            }
            entity.setUpdater(entity.getCreator());

            // 使用MyBatis-Plus保存
            int rows = mapper.insert(entity);
            log.info("插入收入分配记录,影响行数: {}, allocationId: {}", rows, entity.getAllocationId());

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("allocationId", entity.getAllocationId());
            result.put("allocationNo", entity.getAllocationNo());
            result.put("createTime", entity.getCreateTime().toString());

            return result;
        } catch (Exception e) {
            log.error("创建收入分配失败", e);
            throw new RuntimeException("创建收入分配失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> updateAllocation(Long allocationId, Map<String, Object> allocationData) {
        // TODO: 实现更新收入分配逻辑
        throw new UnsupportedOperationException("更新收入分配功能开发中");
    }

    @Override
    public Map<String, Object> deleteAllocation(Long allocationId) {
        // TODO: 实现删除收入分配逻辑
        throw new UnsupportedOperationException("删除收入分配功能开发中");
    }

    @Override
    public Map<String, Object> getAllocationStats(Long tenantId) {
        try {
            // 获取Mapper
            RevenueAllocationMapper mapper = dateBaseConfig.getOracleRevenueAllocationMapper();

            // 统计各类型的分配数量
            Integer departmentCount = mapper.countAllocationList(null, 1, null, null, tenantId);
            Integer productCount = mapper.countAllocationList(null, 2, null, null, tenantId);
            Integer projectCount = mapper.countAllocationList(null, 3, null, null, tenantId);

            Map<String, Object> stats = new HashMap<>();
            stats.put("departmentAllocations", departmentCount != null ? departmentCount : 0);
            stats.put("productAllocations", productCount != null ? productCount : 0);
            stats.put("projectAllocations", projectCount != null ? projectCount : 0);

            return stats;
        } catch (Exception e) {
            log.error("查询收入分配统计失败", e);
            throw new RuntimeException("查询收入分配统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取分配类型名称
     */
    private String getAllocationTypeName(Integer type) {
        if (type == null) return "";
        switch (type) {
            case 1: return "部门";
            case 2: return "产品";
            case 3: return "项目";
            case 4: return "客户";
            default: return "未知";
        }
    }

    /**
     * 将Map中的大写字段名转换为小写驼峰命名
     * 例如: ALLOCATION_ID -> allocationId, ALLOCATIONTYPE -> allocationType
     */
    private void convertFieldNamesToCamelCase(Map<String, Object> item) {
        // 定义需要转换的字段映射 (大写 -> 小写驼峰)
        Map<String, String> fieldMappings = new HashMap<>();
        fieldMappings.put("ALLOCATIONID", "allocationId");
        fieldMappings.put("ALLOCATION_ID", "allocationId");
        fieldMappings.put("ALLOCATIONNO", "allocationNo");
        fieldMappings.put("ALLOCATION_NO", "allocationNo");
        fieldMappings.put("ALLOCATIONTYPE", "allocationType");
        fieldMappings.put("ALLOCATION_TYPE", "allocationType");
        fieldMappings.put("ALLOCATIONPERIOD", "allocationPeriod");
        fieldMappings.put("ALLOCATION_PERIOD", "allocationPeriod");
        fieldMappings.put("TOTALAMOUNT", "totalAmount");
        fieldMappings.put("TOTAL_AMOUNT", "totalAmount");
        fieldMappings.put("ALLOCATEDAMOUNT", "allocatedAmount");
        fieldMappings.put("ALLOCATED_AMOUNT", "allocatedAmount");
        fieldMappings.put("REMAININGAMOUNT", "remainingAmount");
        fieldMappings.put("REMAINING_AMOUNT", "remainingAmount");
        fieldMappings.put("ALLOCATIONSTATUS", "allocationStatus");
        fieldMappings.put("ALLOCATION_STATUS", "allocationStatus");
        fieldMappings.put("ALLOCATIONDATE", "allocationDate");
        fieldMappings.put("ALLOCATION_DATE", "allocationDate");
        fieldMappings.put("ALLOCATIONRULE", "allocationRule");
        fieldMappings.put("ALLOCATION_RULE", "allocationRule");
        fieldMappings.put("CREATETIME", "createTime");
        fieldMappings.put("CREATE_TIME", "createTime");
        fieldMappings.put("RN", "rn");

        // 遍历映射，如果存在大写字段名，则添加对应的小写驼峰字段
        for (Map.Entry<String, String> mapping : fieldMappings.entrySet()) {
            String upperKey = mapping.getKey();
            String camelKey = mapping.getValue();
            if (item.containsKey(upperKey) && !item.containsKey(camelKey)) {
                item.put(camelKey, item.get(upperKey));
            }
        }
    }
}

