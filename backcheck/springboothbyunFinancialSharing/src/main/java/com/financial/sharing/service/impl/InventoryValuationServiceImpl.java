package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.SnowflakeIdWorker;
import com.financial.sharing.config.DateBaseConfig;
import com.financial.sharing.oracle.entity.InventoryValuationEntity;
import com.financial.sharing.oracle.entity.InventoryMasterEntity;
import com.financial.sharing.service.InventoryValuationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存货计价服务实现类
 * 
 * @author system
 * @since 2026-01-27
 */
@Slf4j
@Service
public class InventoryValuationServiceImpl implements InventoryValuationService {

    @Autowired
    private DateBaseConfig dateBaseConfig;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public PageResult<Map<String, Object>> getValuationPage(Map<String, Object> param) {
        try {
            // 获取分页参数
            Integer pageNumber = param.get("pageNumber") != null ? 
                Integer.parseInt(param.get("pageNumber").toString()) : 1;
            Integer pageSize = param.get("pageSize") != null ? 
                Integer.parseInt(param.get("pageSize").toString()) : 10;

            // 设置租户ID（TODO: 从上下文获取）
            param.put("tenantId", 1L);

            // 创建分页对象
            Page<Map<String, Object>> page = new Page<>(pageNumber, pageSize);

            // 调用 Mapper 查询
            IPage<Map<String, Object>> result = dateBaseConfig.getOracleInventoryValuationMapper()
                .selectValuationPage(page, param);

            // 构建返回结果
            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            pageResult.setTlist(result.getRecords());
            pageResult.setTotalRecord((int) result.getTotal());
            pageResult.setCurrentPage((int) result.getCurrent());

            return pageResult;
        } catch (Exception e) {
            log.error("查询存货计价列表失败", e);
            throw new RuntimeException("查询失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getValuationById(Long valuationId) {
        try {
            InventoryValuationEntity entity = dateBaseConfig.getOracleInventoryValuationMapper()
                .selectById(valuationId);
            
            if (entity == null) {
                return null;
            }

            // 转换为 Map
            Map<String, Object> result = new HashMap<>();
            result.put("valuationId", entity.getValuationId());
            result.put("inventoryId", entity.getInventoryId());
            result.put("inventoryCode", entity.getInventoryCode());
            result.put("inventoryName", entity.getInventoryName());
            result.put("warehouseId", entity.getWarehouseId());
            result.put("warehouseName", entity.getWarehouseName());
            result.put("pricingMethod", entity.getPricingMethod());
            result.put("unitCost", entity.getUnitCost());
            result.put("currentQuantity", entity.getCurrentQuantity());
            result.put("totalValue", entity.getTotalValue());
            result.put("beginningQuantity", entity.getBeginningQuantity());
            result.put("beginningAmount", entity.getBeginningAmount());
            result.put("inQuantity", entity.getInQuantity());
            result.put("inAmount", entity.getInAmount());
            result.put("outQuantity", entity.getOutQuantity());
            result.put("outAmount", entity.getOutAmount());
            result.put("valuationPeriod", entity.getValuationPeriod());
            result.put("status", entity.getStatus());
            result.put("updateTime", entity.getUpdateTime());

            return result;
        } catch (Exception e) {
            log.error("查询存货计价详情失败，valuationId: {}", valuationId, e);
            throw new RuntimeException("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateValuation(Map<String, Object> param) {
        try {
            log.info("保存或更新存货计价，参数: {}", param);

            // 设置租户ID（TODO: 从上下文获取）
            if (param.get("tenantId") == null) {
                param.put("tenantId", 1L);
            }

            // 从 Map 创建 Entity
            InventoryValuationEntity entity = InventoryValuationEntity.fromMap(param);

            // 判断是新增还是更新
            if (entity.getValuationId() != null) {
                // 更新操作
                log.info("更新存货计价，ID: {}", entity.getValuationId());

                // 同步更新存货主表的仓库信息（如果提供了 warehouseName）
                if (entity.getInventoryId() != null && param.get("warehouseName") != null) {
                    updateInventoryMaster(entity.getInventoryId(), param);
                }

                int count = dateBaseConfig.getOracleInventoryValuationMapper().updateById(entity);
                return count > 0;
            } else {
                // 新增操作
                log.info("新增存货计价");

                // 如果没有 inventoryId，自动生成一个
                if (entity.getInventoryId() == null) {
                    entity.setInventoryId(snowflakeIdWorker.nextId());
                    log.info("自动生成 inventoryId: {}", entity.getInventoryId());
                }

                // 设置默认状态
                if (entity.getStatus() == null) {
                    entity.setStatus(1);
                }

                // 保存存货计价记录
                int count = dateBaseConfig.getOracleInventoryValuationMapper().insert(entity);

                // 同步创建或更新存货主表记录
                if (count > 0) {
                    createOrUpdateInventoryMaster(entity, param);
                }

                return count > 0;
            }
        } catch (Exception e) {
            log.error("保存或更新存货计价失败", e);
            throw new RuntimeException("保存失败: " + e.getMessage());
        }
    }

    /**
     * 创建或更新存货主表记录
     */
    private void createOrUpdateInventoryMaster(InventoryValuationEntity entity, Map<String, Object> param) {
        try {
            // 检查存货主表中是否已存在该存货
            InventoryMasterEntity masterEntity = dateBaseConfig.getOracleInventoryMasterMapper()
                .selectById(entity.getInventoryId());

            if (masterEntity == null) {
                // 不存在则创建新记录
                masterEntity = new InventoryMasterEntity();
                masterEntity.setInventoryId(entity.getInventoryId());
                masterEntity.setInventoryCode(entity.getInventoryCode());
                masterEntity.setInventoryName(entity.getInventoryName());
                masterEntity.setTenantId(entity.getTenantId());
                masterEntity.setStatus(entity.getStatus());

                // 设置分类信息
                if (param.get("categoryId") != null) {
                    masterEntity.setCategoryId(Long.valueOf(param.get("categoryId").toString()));
                }
                if (param.get("categoryName") != null) {
                    masterEntity.setCategoryName(param.get("categoryName").toString());
                }

                // 设置仓库信息
                masterEntity.setWarehouseId(entity.getWarehouseId());
                masterEntity.setWarehouseName(entity.getWarehouseName());

                // 设置计价方法
                masterEntity.setPricingMethod(entity.getPricingMethod());

                // 设置当前库存
                masterEntity.setCurrentQuantity(entity.getCurrentQuantity());

                dateBaseConfig.getOracleInventoryMasterMapper().insert(masterEntity);
                log.info("创建存货主表记录成功，inventoryId: {}", entity.getInventoryId());
            } else {
                // 已存在则更新部分字段
                boolean needUpdate = false;

                // 更新分类信息
                if (param.get("categoryId") != null) {
                    masterEntity.setCategoryId(Long.valueOf(param.get("categoryId").toString()));
                    needUpdate = true;
                }
                if (param.get("categoryName") != null) {
                    masterEntity.setCategoryName(param.get("categoryName").toString());
                    needUpdate = true;
                }

                // 更新仓库信息
                if (entity.getWarehouseId() != null) {
                    masterEntity.setWarehouseId(entity.getWarehouseId());
                    needUpdate = true;
                }
                if (entity.getWarehouseName() != null) {
                    masterEntity.setWarehouseName(entity.getWarehouseName());
                    needUpdate = true;
                }

                if (needUpdate) {
                    dateBaseConfig.getOracleInventoryMasterMapper().updateById(masterEntity);
                    log.info("更新存货主表记录成功，inventoryId: {}", entity.getInventoryId());
                }
            }
        } catch (Exception e) {
            log.error("创建或更新存货主表记录失败", e);
            // 不抛出异常，避免影响主流程
        }
    }

    /**
     * 更新存货主表记录
     */
    private void updateInventoryMaster(Long inventoryId, Map<String, Object> param) {
        try {
            InventoryMasterEntity masterEntity = dateBaseConfig.getOracleInventoryMasterMapper()
                .selectById(inventoryId);

            if (masterEntity != null) {
                boolean needUpdate = false;

                // 更新分类信息
                if (param.get("categoryId") != null) {
                    masterEntity.setCategoryId(Long.valueOf(param.get("categoryId").toString()));
                    needUpdate = true;
                }
                if (param.get("categoryName") != null) {
                    masterEntity.setCategoryName(param.get("categoryName").toString());
                    needUpdate = true;
                }

                // 更新仓库信息
                if (param.get("warehouseId") != null) {
                    masterEntity.setWarehouseId(Long.valueOf(param.get("warehouseId").toString()));
                    needUpdate = true;
                }
                if (param.get("warehouseName") != null) {
                    masterEntity.setWarehouseName(param.get("warehouseName").toString());
                    needUpdate = true;
                }

                if (needUpdate) {
                    dateBaseConfig.getOracleInventoryMasterMapper().updateById(masterEntity);
                    log.info("更新存货主表记录成功，inventoryId: {}", inventoryId);
                }
            }
        } catch (Exception e) {
            log.error("更新存货主表记录失败", e);
            // 不抛出异常，避免影响主流程
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteValuation(Long valuationId) {
        try {
            int count = dateBaseConfig.getOracleInventoryValuationMapper().deleteById(valuationId);
            return count > 0;
        } catch (Exception e) {
            log.error("删除存货计价失败，valuationId: {}", valuationId, e);
            throw new RuntimeException("删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateValuation(Map<String, Object> param) {
        try {
            // TODO: 实现批量更新逻辑
            log.info("批量更新存货计价，参数: {}", param);
            return true;
        } catch (Exception e) {
            log.error("批量更新存货计价失败", e);
            throw new RuntimeException("批量更新失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteValuation(List<Long> valuationIds) {
        try {
            int count = dateBaseConfig.getOracleInventoryValuationMapper().batchDelete(valuationIds);
            return count > 0;
        } catch (Exception e) {
            log.error("批量删除存货计价失败，valuationIds: {}", valuationIds, e);
            throw new RuntimeException("批量删除失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> calculateCost(Long valuationId) {
        try {
            // TODO: 实现成本计算逻辑
            log.info("计算存货成本，valuationId: {}", valuationId);
            return new HashMap<>();
        } catch (Exception e) {
            log.error("计算存货成本失败，valuationId: {}", valuationId, e);
            throw new RuntimeException("计算成本失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getWarehouseList() {
        try {
            Long tenantId = 1L; // TODO: 从上下文获取
            return dateBaseConfig.getOracleInventoryValuationMapper().selectWarehouseList(tenantId);
        } catch (Exception e) {
            log.error("查询仓库列表失败", e);
            throw new RuntimeException("查询仓库列表失败: " + e.getMessage());
        }
    }

    @Override
    public String exportValuation(Map<String, Object> param) {
        try {
            // TODO: 实现导出逻辑
            log.info("导出存货计价数据，参数: {}", param);
            return "";
        } catch (Exception e) {
            log.error("导出存货计价数据失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }
}

