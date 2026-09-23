package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.CostTransferEntity;
import com.financial.sharing.oracle.mapper.CostTransferMapper;
import com.financial.sharing.service.CostTransferService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 成本结转服务实现类
 *
 * @author system
 * @since 2026-01-28
 */
@Slf4j
@Service
public class CostTransferServiceImpl implements CostTransferService {

    @Autowired(required = false)
    private CostTransferMapper costTransferMapper;

    @Autowired(required = false)
    private SnowflakeIdWorker snowflakeIdWorker;

    // 结转类型名称映射
    private static final Map<String, String> TRANSFER_TYPE_MAP = new HashMap<>();

    static {
        TRANSFER_TYPE_MAP.put("SALES_COST", "销售成本结转");
        TRANSFER_TYPE_MAP.put("PRODUCTION_COST", "生产成本结转");
        TRANSFER_TYPE_MAP.put("PERIOD_EXPENSE", "期间费用分摊");
        TRANSFER_TYPE_MAP.put("COST_VARIANCE", "成本差异结转");
    }

    @Override
    public PageResult<Map<String, Object>> getTransferPage(Map<String, Object> param) {
        try {
            // 安全地转换分页参数（兼容 String 和 Integer 类型）
            int pageNum = parseIntParam(param.get("pageNumber"), 1);
            int pageSize = parseIntParam(param.get("pageSize"), 15);

            // 设置默认租户ID
            if (!param.containsKey("tenantId")) {
                param.put("tenantId", 1L);
            }

            Page<Map<String, Object>> page = new Page<>(pageNum, pageSize);
            // 修复类型转换问题：selectTransferPage返回IPage，不需要强制转换为Page
            com.baomidou.mybatisplus.core.metadata.IPage<Map<String, Object>> result = costTransferMapper.selectTransferPage(page, param);

            return new PageResult<>(
                (int) result.getTotal(),
                (int) result.getCurrent(),
                (int) result.getPages(),
                (int) result.getSize(),
                result.getRecords()
            );
        } catch (Exception e) {
            log.error("查询成本结转列表失败", e);
            throw new RuntimeException("查询成本结转列表失败: " + e.getMessage());
        }
    }

    /**
     * 安全地解析整数参数（兼容 String 和 Integer 类型）
     */
    private int parseIntParam(Object value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                log.warn("无法解析整数参数: {}, 使用默认值: {}", value, defaultValue);
                return defaultValue;
            }
        }
        return defaultValue;
    }

    @Override
    public Map<String, Object> getTransferById(Long transferId) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("transferId", transferId);
            param.put("tenantId", 1L);
            return costTransferMapper.selectTransferById(param);
        } catch (Exception e) {
            log.error("查询成本结转详情失败, transferId: {}", transferId, e);
            throw new RuntimeException("查询成本结转详情失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createTransfer(Map<String, Object> param) {
        try {
            CostTransferEntity entity = new CostTransferEntity();

            // 生成结转单号
            String transferNo = generateTransferNo();
            entity.setTransferNo(transferNo);

            // 处理结转期间（前端传来的是ISO格式，需要转换为YYYY-MM）
            String transferPeriod = (String) param.get("transferPeriod");
            if (transferPeriod != null && transferPeriod.contains("T")) {
                transferPeriod = transferPeriod.substring(0, 7); // 取YYYY-MM部分
            }
            entity.setTransferPeriod(transferPeriod);

            String transferType = (String) param.get("transferType");
            entity.setTransferType(transferType);
            entity.setTransferTypeName(TRANSFER_TYPE_MAP.get(transferType));
            entity.setDescription((String) param.get("description"));
            entity.setTransferStatus(0); // 默认待结转

            // 设置存货数量（从categoryIds数组长度获取）
            List<?> categoryIds = (List<?>) param.get("categoryIds");
            if (categoryIds != null) {
                entity.setInventoryCount(categoryIds.size());
            }

            // 初始化金额为0
            entity.setTransferAmount(BigDecimal.ZERO);
            entity.setVarianceAmount(BigDecimal.ZERO);
            entity.setVoucherCount(0);

            entity.setTenantId(1L);
            entity.setCreatorId("SYSTEM");

            int result = costTransferMapper.insert(entity);
            log.info("创建成本结转成功, transferNo: {}", transferNo);
            return result > 0;
        } catch (Exception e) {
            log.error("创建成本结转失败", e);
            throw new RuntimeException("创建成本结转失败: " + e.getMessage());
        }
    }

    /**
     * 生成结转单号
     */
    private String generateTransferNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        long id = snowflakeIdWorker != null ? snowflakeIdWorker.nextId() : System.currentTimeMillis();
        return "TRF" + dateStr + String.format("%06d", id % 1000000);
    }




    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean executeTransfer(Long transferId) {
        try {
            CostTransferEntity entity = costTransferMapper.selectById(transferId);

            if (entity == null) {
                throw new RuntimeException("成本结转记录不存在");
            }

            if (entity.getTransferStatus() != 0) {
                throw new RuntimeException("只能执行待结转状态的记录");
            }

            // 更新结转状态为已结转
            entity.setTransferStatus(1);
            entity.setTransferTime(LocalDateTime.now());

            // 模拟计算结转金额和差异金额
            entity.setTransferAmount(new BigDecimal("125000.00"));
            entity.setVarianceAmount(new BigDecimal("2500.00"));
            entity.setVoucherCount(8);

            int result = costTransferMapper.updateById(entity);

            log.info("执行成本结转成功, transferId: {}", transferId);
            return result > 0;
        } catch (Exception e) {
            log.error("执行成本结转失败, transferId: {}", transferId, e);
            throw new RuntimeException("执行成本结转失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean revokeTransfer(Long transferId) {
        try {
            CostTransferEntity entity = costTransferMapper.selectById(transferId);

            if (entity == null) {
                throw new RuntimeException("成本结转记录不存在");
            }

            if (entity.getTransferStatus() != 1) {
                throw new RuntimeException("只能撤销已结转状态的记录");
            }

            // 更新结转状态为已撤销
            entity.setTransferStatus(2);
            int result = costTransferMapper.updateById(entity);

            log.info("撤销成本结转成功, transferId: {}", transferId);
            return result > 0;
        } catch (Exception e) {
            log.error("撤销成本结转失败, transferId: {}", transferId, e);
            throw new RuntimeException("撤销成本结转失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchExecuteTransfer(List<Long> transferIds) {
        try {
            int result = costTransferMapper.batchExecuteTransfer(transferIds, 1, "SYSTEM");
            log.info("批量执行成本结转成功, count: {}", result);
            return result > 0;
        } catch (Exception e) {
            log.error("批量执行成本结转失败", e);
            throw new RuntimeException("批量执行成本结转失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchRevokeTransfer(List<Long> transferIds) {
        try {
            int result = costTransferMapper.batchRevokeTransfer(transferIds, "SYSTEM");
            log.info("批量撤销成本结转成功, count: {}", result);
            return result > 0;
        } catch (Exception e) {
            log.error("批量撤销成本结转失败", e);
            throw new RuntimeException("批量撤销成本结转失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getTransferStatistics(Map<String, Object> param) {
        try {
            if (!param.containsKey("tenantId")) {
                param.put("tenantId", 1L);
            }
            return costTransferMapper.countTransferStatistics(param);
        } catch (Exception e) {
            log.error("获取成本结转统计失败", e);
            throw new RuntimeException("获取成本结转统计失败: " + e.getMessage());
        }
    }

    @Override
    public String exportTransfer(Map<String, Object> param) {
        try {
            // TODO: 实现导出功能
            log.info("导出成本结转数据, param: {}", param);
            return "export_file_path.xlsx";
        } catch (Exception e) {
            log.error("导出成本结转数据失败", e);
            throw new RuntimeException("导出成本结转数据失败: " + e.getMessage());
        }
    }
}