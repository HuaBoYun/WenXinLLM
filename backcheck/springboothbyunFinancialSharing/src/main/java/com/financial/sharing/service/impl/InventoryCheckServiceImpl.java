package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.config.DateBaseConfig;
import com.financial.sharing.oracle.entity.InventoryCheckEntity;
import com.financial.sharing.oracle.entity.InventoryCheckResultEntity;
import com.financial.sharing.service.InventoryCheckService;
import com.financial.sharing.util.PageResult;
import com.hbfk.util.user.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存货盘点服务实现类
 * 
 * @author system
 * @since 2026-01-26
 */
@Slf4j
@Service
public class InventoryCheckServiceImpl implements InventoryCheckService {

    @Autowired
    private DateBaseConfig dateBaseConfig;

    @Autowired
    private UserProvider userProvider;

    // 盘点状态名称映射
    private static final Map<Integer, String> CHECK_STATUS_MAP = new HashMap<>();
    
    // 盘点类型名称映射
    private static final Map<String, String> CHECK_TYPE_MAP = new HashMap<>();
    
    static {
        CHECK_STATUS_MAP.put(0, "计划中");
        CHECK_STATUS_MAP.put(1, "盘点中");
        CHECK_STATUS_MAP.put(2, "已完成");
        CHECK_STATUS_MAP.put(3, "已审批");
        CHECK_STATUS_MAP.put(4, "已取消");
        
        CHECK_TYPE_MAP.put("FULL", "全盘");
        CHECK_TYPE_MAP.put("PARTIAL", "抽盘");
        CHECK_TYPE_MAP.put("CYCLE", "循环盘点");
        CHECK_TYPE_MAP.put("DYNAMIC", "动态盘点");
    }

    @Override
    public PageResult<Map<String, Object>> getCheckPage(Map<String, Object> param) {
        try {
            // 处理分页参数，兼容pageNum和pageNumber
            Object pageNumObj = param.get("pageNumber") != null ? param.get("pageNumber") : param.get("pageNum");
            Object pageSizeObj = param.get("pageSize");

            int pageNum = 1;
            int pageSize = 10;

            if (pageNumObj != null) {
                pageNum = pageNumObj instanceof Integer ? (Integer) pageNumObj : Integer.parseInt(pageNumObj.toString());
            }
            if (pageSizeObj != null) {
                pageSize = pageSizeObj instanceof Integer ? (Integer) pageSizeObj : Integer.parseInt(pageSizeObj.toString());
            }

            // 获取租户ID - 使用orgid作为租户ID
            Long tenantId = 1L;
            try {
                if (userProvider.get() != null && userProvider.get().getOrgid() != null) {
                    tenantId = userProvider.get().getOrgid().longValue();
                }
            } catch (Exception e) {
                log.warn("获取租户ID失败，使用默认值", e);
            }
            param.put("tenantId", tenantId);

            Page<Map<String, Object>> page = new Page<>(pageNum, pageSize);
            com.baomidou.mybatisplus.core.metadata.IPage<Map<String, Object>> result =
                dateBaseConfig.getOracleInventoryCheckMapper().selectCheckPage(page, param);

            return new PageResult<>(
                (int) result.getTotal(),
                (int) result.getCurrent(),
                (int) result.getPages(),
                (int) result.getSize(),
                result.getRecords()
            );
        } catch (Exception e) {
            log.error("查询存货盘点列表失败", e);
            throw new RuntimeException("查询存货盘点列表失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getCheckById(Long checkId) {
        try {
            InventoryCheckEntity entity = dateBaseConfig.getOracleInventoryCheckMapper()
                .selectById(checkId);
            
            if (entity == null) {
                throw new RuntimeException("存货盘点记录不存在");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("checkId", entity.getCheckId());
            result.put("checkNo", entity.getCheckNo());
            result.put("checkName", entity.getCheckName());
            result.put("checkType", entity.getCheckType());
            result.put("checkTypeName", CHECK_TYPE_MAP.get(entity.getCheckType()));
            result.put("warehouseId", entity.getWarehouseId());
            result.put("warehouseName", entity.getWarehouseName());
            result.put("inventoryCount", entity.getInventoryCount());
            result.put("checkProgress", entity.getCheckProgress());
            result.put("varianceAmount", entity.getVarianceAmount());
            result.put("checkStatus", entity.getCheckStatus());
            result.put("checkStatusName", CHECK_STATUS_MAP.get(entity.getCheckStatus()));
            result.put("checkDate", entity.getCheckDate());
            result.put("description", entity.getDescription());
            
            return result;
        } catch (Exception e) {
            log.error("查询存货盘点详情失败, checkId: {}", checkId, e);
            throw new RuntimeException("查询存货盘点详情失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createCheck(Map<String, Object> param) {
        try {
            InventoryCheckEntity entity = new InventoryCheckEntity();

            // 自动生成盘点单号（如果前端没有传递）
            String checkNo = (String) param.get("checkNo");
            if (checkNo == null || checkNo.trim().isEmpty()) {
                checkNo = "PD" + System.currentTimeMillis();
            }
            entity.setCheckNo(checkNo);

            entity.setCheckName((String) param.get("checkName"));
            entity.setCheckType((String) param.get("checkType"));
            entity.setCheckTypeName(CHECK_TYPE_MAP.get(param.get("checkType")));
            entity.setWarehouseId(param.get("warehouseId") != null ?
                Long.parseLong(param.get("warehouseId").toString()) : null);
            entity.setWarehouseName((String) param.get("warehouseName"));
            entity.setCheckDate(param.get("checkDate") != null ?
                LocalDate.parse(param.get("checkDate").toString()) : LocalDate.now());
            entity.setDescription((String) param.get("description"));
            entity.setCheckStatus(0); // 默认计划中
            entity.setCheckProgress(0);

            // 获取租户ID - 使用orgid作为租户ID
            Long tenantId = 1L;
            try {
                if (userProvider.get() != null && userProvider.get().getOrgid() != null) {
                    tenantId = userProvider.get().getOrgid().longValue();
                }
            } catch (Exception e) {
                log.warn("获取租户ID失败，使用默认值", e);
            }
            entity.setTenantId(tenantId);

            int result = dateBaseConfig.getOracleInventoryCheckMapper().insert(entity);

            log.info("创建存货盘点任务成功, checkNo: {}", entity.getCheckNo());
            return result > 0;
        } catch (Exception e) {
            log.error("创建存货盘点任务失败", e);
            throw new RuntimeException("创建存货盘点任务失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean executeCheck(Long checkId) {
        try {
            InventoryCheckEntity entity = dateBaseConfig.getOracleInventoryCheckMapper()
                .selectById(checkId);

            if (entity == null) {
                throw new RuntimeException("存货盘点记录不存在");
            }

            if (entity.getCheckStatus() != 0) {
                throw new RuntimeException("只能执行计划中状态的盘点任务");
            }

            // 更新盘点状态为盘点中
            entity.setCheckStatus(1);
            int result = dateBaseConfig.getOracleInventoryCheckMapper().updateById(entity);

            log.info("执行存货盘点成功, checkId: {}", checkId);
            return result > 0;
        } catch (Exception e) {
            log.error("执行存货盘点失败, checkId: {}", checkId, e);
            throw new RuntimeException("执行存货盘点失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitCheckResults(Long checkId, List<Map<String, Object>> results) {
        try {
            InventoryCheckEntity entity = dateBaseConfig.getOracleInventoryCheckMapper()
                .selectById(checkId);

            if (entity == null) {
                throw new RuntimeException("存货盘点记录不存在");
            }

            // 保存盘点结果
            List<InventoryCheckResultEntity> resultList = new ArrayList<>();
            for (Map<String, Object> result : results) {
                InventoryCheckResultEntity resultEntity = new InventoryCheckResultEntity();
                resultEntity.setCheckId(checkId);
                resultEntity.setInventoryId(Long.parseLong(result.get("inventoryId").toString()));
                resultEntity.setInventoryCode((String) result.get("inventoryCode"));
                resultEntity.setInventoryName((String) result.get("inventoryName"));
                // 设置其他字段...
                resultList.add(resultEntity);
            }

            if (!resultList.isEmpty()) {
                dateBaseConfig.getOracleInventoryCheckResultMapper().batchInsert(resultList);
            }

            // 更新盘点状态为已完成
            entity.setCheckStatus(2);
            entity.setCheckProgress(100);
            dateBaseConfig.getOracleInventoryCheckMapper().updateById(entity);

            log.info("提交盘点结果成功, checkId: {}, count: {}", checkId, resultList.size());
            return true;
        } catch (Exception e) {
            log.error("提交盘点结果失败, checkId: {}", checkId, e);
            throw new RuntimeException("提交盘点结果失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveCheck(Long checkId, Map<String, Object> param) {
        try {
            InventoryCheckEntity entity = dateBaseConfig.getOracleInventoryCheckMapper()
                .selectById(checkId);

            if (entity == null) {
                throw new RuntimeException("存货盘点记录不存在");
            }

            if (entity.getCheckStatus() != 2) {
                throw new RuntimeException("只能审批已完成状态的盘点任务");
            }

            // 更新盘点状态为已审批
            entity.setCheckStatus(3);
            entity.setApproverId((String) param.get("approverId"));
            entity.setApproverName((String) param.get("approverName"));
            entity.setApproveTime(LocalDateTime.now());
            entity.setApproveRemark((String) param.get("approveRemark"));

            int result = dateBaseConfig.getOracleInventoryCheckMapper().updateById(entity);

            log.info("审批盘点结果成功, checkId: {}", checkId);
            return result > 0;
        } catch (Exception e) {
            log.error("审批盘点结果失败, checkId: {}", checkId, e);
            throw new RuntimeException("审批盘点结果失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getCheckResults(Long checkId) {
        try {
            List<InventoryCheckResultEntity> results = dateBaseConfig.getOracleInventoryCheckResultMapper()
                .selectByCheckId(checkId);

            List<Map<String, Object>> resultList = new ArrayList<>();
            for (InventoryCheckResultEntity result : results) {
                Map<String, Object> map = new HashMap<>();
                map.put("resultId", result.getResultId());
                map.put("inventoryCode", result.getInventoryCode());
                map.put("inventoryName", result.getInventoryName());
                map.put("bookQuantity", result.getBookQuantity());
                map.put("actualQuantity", result.getActualQuantity());
                map.put("varianceQuantity", result.getVarianceQuantity());
                map.put("varianceAmount", result.getVarianceAmount());
                map.put("checkResult", result.getCheckResult());
                map.put("processStatus", result.getProcessStatus());
                resultList.add(map);
            }

            return resultList;
        } catch (Exception e) {
            log.error("获取盘点结果列表失败, checkId: {}", checkId, e);
            throw new RuntimeException("获取盘点结果列表失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processDifference(Map<String, Object> param) {
        try {
            Long resultId = Long.parseLong(param.get("resultId").toString());
            Integer processStatus = Integer.parseInt(param.get("processStatus").toString());
            String processRemark = (String) param.get("processRemark");

            // 获取处理人ID - 处理BigDecimal类型
            String processorId = "SYSTEM";
            try {
                if (userProvider.get() != null && userProvider.get().getStaffid() != null) {
                    processorId = userProvider.get().getStaffid().toString();
                }
            } catch (Exception e) {
                log.warn("获取处理人ID失败，使用默认值", e);
            }

            int result = dateBaseConfig.getOracleInventoryCheckResultMapper()
                .processDifference(resultId, processStatus, processRemark, processorId);

            log.info("处理盘点差异成功, resultId: {}", resultId);
            return result > 0;
        } catch (Exception e) {
            log.error("处理盘点差异失败", e);
            throw new RuntimeException("处理盘点差异失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchExecuteCheck(List<Long> checkIds) {
        try {
            // 获取更新人ID - 处理BigDecimal类型
            String updaterId = "SYSTEM";
            try {
                if (userProvider.get() != null && userProvider.get().getStaffid() != null) {
                    updaterId = userProvider.get().getStaffid().toString();
                }
            } catch (Exception e) {
                log.warn("获取更新人ID失败，使用默认值", e);
            }

            int result = dateBaseConfig.getOracleInventoryCheckMapper()
                .batchExecuteCheck(checkIds, 1, updaterId);

            log.info("批量执行盘点任务成功, count: {}", result);
            return result > 0;
        } catch (Exception e) {
            log.error("批量执行盘点任务失败", e);
            throw new RuntimeException("批量执行盘点任务失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getCheckStatistics(Map<String, Object> param) {
        try {
            // 获取租户ID - 使用orgid作为租户ID
            Long tenantId = 1L;
            try {
                if (userProvider.get() != null && userProvider.get().getOrgid() != null) {
                    tenantId = userProvider.get().getOrgid().longValue();
                }
            } catch (Exception e) {
                log.warn("获取租户ID失败，使用默认值", e);
            }
            param.put("tenantId", tenantId);

            return dateBaseConfig.getOracleInventoryCheckMapper()
                .countCheckStatistics(param);
        } catch (Exception e) {
            log.error("获取存货盘点统计失败", e);
            throw new RuntimeException("获取存货盘点统计失败: " + e.getMessage());
        }
    }

    @Override
    public String exportCheck(Map<String, Object> param) {
        try {
            // TODO: 实现导出功能
            log.info("导出盘点任务, param: {}", param);
            return "export_file_path.xlsx";
        } catch (Exception e) {
            log.error("导出盘点任务失败", e);
            throw new RuntimeException("导出盘点任务失败: " + e.getMessage());
        }
    }

    @Override
    public String generateCheckReport(Long checkId) {
        try {
            // TODO: 实现报告生成功能
            log.info("生成盘点报告, checkId: {}", checkId);
            return "report_file_path.pdf";
        } catch (Exception e) {
            log.error("生成盘点报告失败, checkId: {}", checkId, e);
            throw new RuntimeException("生成盘点报告失败: " + e.getMessage());
        }
    }
}

