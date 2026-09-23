package com.financial.sharing.service.impl;
import com.financial.sharing.oracle.entity.AuxiliaryItemEntity;


import com.financial.sharing.dto.AuxiliaryItemBatchParam;
import com.financial.sharing.util.Java8Collections;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.financial.sharing.config.DateBaseConfig;
import com.financial.sharing.service.AuxiliaryItemService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.AuxiliaryItemQueryParam;
import com.financial.sharing.vo.param.AuxiliaryItemSaveParam;
import com.financial.sharing.vo.result.AuxiliaryItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 辅助核算项服务实现类
 * 
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Service
public class AuxiliaryItemServiceImpl implements AuxiliaryItemService {

    @Autowired
    private DateBaseConfig dateBaseConfig;

    // 辅助核算类型名称映射
    private static final Map<String, String> AUXILIARY_TYPE_NAME_MAP = new HashMap<>();
    
    static {
        AUXILIARY_TYPE_NAME_MAP.put("DEPT", "部门");
        AUXILIARY_TYPE_NAME_MAP.put("PERSON", "人员");
        AUXILIARY_TYPE_NAME_MAP.put("PROJECT", "项目");
        AUXILIARY_TYPE_NAME_MAP.put("CUSTOMER", "客户");
        AUXILIARY_TYPE_NAME_MAP.put("SUPPLIER", "供应商");
        AUXILIARY_TYPE_NAME_MAP.put("PRODUCT", "产品");
        AUXILIARY_TYPE_NAME_MAP.put("AREA", "地区");
        AUXILIARY_TYPE_NAME_MAP.put("OTHER", "其他");
    }

    @Override
    public PageResult<AuxiliaryItemVO> getAuxiliaryItemPage(AuxiliaryItemQueryParam param) {
        try {
            // 参数验证
            if (param == null) {
                throw new IllegalArgumentException("查询参数不能为空");
            }

            // 设置默认值 - 修复分页参数映射
            if (param.getPageNum() == null || param.getPageNum() <= 0) {
                param.setPageNum(1);
            }
            if (param.getPageSize() == null || param.getPageSize() <= 0) {
                param.setPageSize(10);
            }

            log.info("分页参数: pageNum={}, pageSize={}, pageNumber={}",
                param.getPageNum(), param.getPageSize(), param.getPageNumber());

            // 使用PageHelper进行分页 - 确保使用正确的参数
            PageHelper.startPage(param.getPageNum(), param.getPageSize());

            List<AuxiliaryItemVO> list;
            try {
                list = dateBaseConfig.getOracleAuxiliaryItemMapper().getAuxiliaryItemList(param);
            } catch (Exception e) {
                log.error("数据库查询异常", e);
                throw new RuntimeException("数据库查询失败：" + e.getMessage(), e);
            }

            // 使用PageInfo处理分页结果
            PageInfo<AuxiliaryItemVO> pageInfo = new PageInfo<>(list);

            // 安全的集合处理
            if (pageInfo.getList() == null) {
                pageInfo.setList(Java8Collections.listOf());
            }

            // 设置类型名称
            for (AuxiliaryItemVO vo : pageInfo.getList()) {
                try {
                    setAuxiliaryTypeName(vo);
                } catch (Exception e) {
                    log.warn("设置辅助核算类型名称失败，vo={}", vo, e);
                    // 继续处理其他记录，不中断整体流程
                }
            }
            return new PageResult<>(
                (int)pageInfo.getTotal(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getPageSize(),
                pageInfo.getList()
            );
        } catch (IllegalArgumentException e) {
            throw e; // 重新抛出参数异常
        } catch (Exception e) {
            log.error("查询辅助核算项分页失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuxiliaryItemVO saveOrUpdateAuxiliaryItem(AuxiliaryItemSaveParam param) {
        // 检查编码是否重复
        if (checkAuxiliaryCodeExists(param.getAuxiliaryCode(), param.getAuxiliaryType(),
                param.getBookId(), param.getTenantId(), param.getAuxiliaryId())) {
            throw new RuntimeException("辅助核算项编码已存在");
        }

        AuxiliaryItemEntity entity = new AuxiliaryItemEntity();
        BeanUtils.copyProperties(param, entity);

        // 设置默认值
        if (entity.getIsEnabled() == null) {
            entity.setIsEnabled(1);
        }
        if (entity.getIsLeaf() == null) {
            entity.setIsLeaf(1);
        }
        if (entity.getSortOrder() == null) {
            entity.setSortOrder(0);
        }

        boolean success;

        com.financial.sharing.oracle.entity.AuxiliaryItemEntity oracleEntity =
            new com.financial.sharing.oracle.entity.AuxiliaryItemEntity();
        BeanUtils.copyProperties(entity, oracleEntity);

        if (param.getAuxiliaryId() == null) {
            success = dateBaseConfig.getOracleAuxiliaryItemMapper().insert(oracleEntity) > 0;
        } else {
            success = dateBaseConfig.getOracleAuxiliaryItemMapper().updateById(oracleEntity) > 0;
        }
        entity.setAuxiliaryId(oracleEntity.getAuxiliaryId());

        if (!success) {
            throw new RuntimeException("保存辅助核算项失败");
        }

        // 更新上级项的末级标识
        if (param.getParentId() != null) {
            updateParentLeafFlag(param.getParentId());
        }
        return getAuxiliaryItemById(entity.getAuxiliaryId());
    }

    @Override
    public AuxiliaryItemVO getAuxiliaryItemById(Long auxiliaryId) {
        AuxiliaryItemEntity entity;

        com.financial.sharing.oracle.entity.AuxiliaryItemEntity oracleEntity =
            dateBaseConfig.getOracleAuxiliaryItemMapper().selectById(auxiliaryId);
        if (oracleEntity == null) {
            return null;
        }

        entity = new AuxiliaryItemEntity();
        BeanUtils.copyProperties(oracleEntity, entity);

        if (entity == null) {
            return null;
        }

        AuxiliaryItemVO vo = new AuxiliaryItemVO();
        BeanUtils.copyProperties(entity, vo);
        setAuxiliaryTypeName(vo);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAuxiliaryItem(Long auxiliaryId) {
        // 检查是否有子项
        int childCount;

        childCount = dateBaseConfig.getOracleAuxiliaryItemMapper().countChildrenByParentId(auxiliaryId, null);

        if (childCount > 0) {
            throw new RuntimeException("存在下级辅助核算项，不能删除");
        }

        boolean success;

        success = dateBaseConfig.getOracleAuxiliaryItemMapper().deleteById(auxiliaryId) > 0;

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteAuxiliaryItems(List<Long> auxiliaryIds) {
        if (Java8Collections.isEmpty(auxiliaryIds)) {
            return false;
        }

        int result;

        result = dateBaseConfig.getOracleAuxiliaryItemMapper().batchDelete(auxiliaryIds, null);

        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAuxiliaryItemStatus(Long auxiliaryId, Integer isEnabled) {
        int result;

        result = dateBaseConfig.getOracleAuxiliaryItemMapper().batchUpdateStatus(
            Java8Collections.listOf(auxiliaryId), isEnabled, null);

        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateAuxiliaryItemStatus(List<Long> auxiliaryIds, Integer isEnabled) {
        if (Java8Collections.isEmpty(auxiliaryIds)) {
            return false;
        }

        int result;

        result = dateBaseConfig.getOracleAuxiliaryItemMapper().batchUpdateStatus(auxiliaryIds, isEnabled, null);

        return result > 0;
    }

    @Override
    public boolean checkAuxiliaryCodeExists(String auxiliaryCode, String auxiliaryType, Long bookId, Long tenantId, Long excludeId) {
        AuxiliaryItemEntity entity;

        com.financial.sharing.oracle.entity.AuxiliaryItemEntity oracleEntity =
            dateBaseConfig.getOracleAuxiliaryItemMapper().selectByCode(
                auxiliaryCode, auxiliaryType, bookId, tenantId, excludeId);
        entity = oracleEntity != null ? new AuxiliaryItemEntity() : null;

        return entity != null;
    }

    @Override
    public List<AuxiliaryItemVO> getAuxiliaryItemTree(String auxiliaryType, Long bookId, Long tenantId) {
        List<AuxiliaryItemVO> list;

        list = dateBaseConfig.getOracleAuxiliaryItemMapper().selectAuxiliaryItemTree(auxiliaryType, bookId, tenantId);

        if (!Java8Collections.isEmpty(list)) {
            list.forEach(this::setAuxiliaryTypeName);
        }
        return list;
    }

    @Override
    public List<AuxiliaryItemVO> getAuxiliaryItemsByType(String auxiliaryType, Long bookId, Long tenantId) {
        List<AuxiliaryItemVO> list;

        list = dateBaseConfig.getOracleAuxiliaryItemMapper().selectByType(auxiliaryType, bookId, tenantId);

        if (!Java8Collections.isEmpty(list)) {
            list.forEach(this::setAuxiliaryTypeName);
        }
        return list;
    }

    @Override
    public List<AuxiliaryItemVO> getAuxiliaryItemsByParentId(Long parentId, Long bookId, Long tenantId) {
        List<AuxiliaryItemVO> list;

        list = dateBaseConfig.getOracleAuxiliaryItemMapper().selectByParentId(parentId, bookId, tenantId);

        if (!Java8Collections.isEmpty(list)) {
            list.forEach(this::setAuxiliaryTypeName);
        }
        return list;
    }

    @Override
    public List<String> getAuxiliaryTypes(Long bookId, Long tenantId) {
        return dateBaseConfig.getOracleAuxiliaryItemMapper().selectAuxiliaryTypes(bookId, tenantId);
    }

    /**
     * 设置辅助核算类型名称
     */
    private void setAuxiliaryTypeName(AuxiliaryItemVO vo) {
        if (vo != null && vo.getAuxiliaryType() != null) {
            vo.setAuxiliaryTypeName(AUXILIARY_TYPE_NAME_MAP.getOrDefault(vo.getAuxiliaryType(), vo.getAuxiliaryType()));
        }
    }

    /**
     * 更新上级项的末级标识
     */
    private void updateParentLeafFlag(Long parentId) {
        dateBaseConfig.getOracleAuxiliaryItemMapper().updateLeafFlag(parentId, 0, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchOperation(AuxiliaryItemBatchParam param) {
        if (param == null || param.getOperationType() == null) {
            throw new IllegalArgumentException("操作参数不能为空");
        }

        try {
            switch (param.getOperationType().toUpperCase()) {
                case "DELETE":
                    if (CollectionUtils.isEmpty(param.getItemIds())) {
                        return false;
                    }
                    return batchDeleteAuxiliaryItems(param.getItemIds());

                case "ENABLE":
                    if (CollectionUtils.isEmpty(param.getItemIds())) {
                        return false;
                    }
                    return batchUpdateAuxiliaryItemStatus(param.getItemIds(), 1);

                case "DISABLE":
                    if (CollectionUtils.isEmpty(param.getItemIds())) {
                        return false;
                    }
                    return batchUpdateAuxiliaryItemStatus(param.getItemIds(), 0);

                default:
                    log.warn("不支持的操作类型: {}", param.getOperationType());
                    return false;
            }
        } catch (Exception e) {
            log.error("批量操作辅助核算项失败，操作类型: {}", param.getOperationType(), e);
            throw new RuntimeException("批量操作失败: " + e.getMessage(), e);
        }
    }
}