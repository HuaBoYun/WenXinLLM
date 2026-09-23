package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetOrganizationStructure;
import com.management.accountant.oracle.mapper.budget.BudgetOrganizationStructureMapper;
import com.management.accountant.service.BudgetOrganizationStructureService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 预算组织体系管理Service实现类
 * 
 * @description 预算组织体系管理业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetOrganizationStructureServiceImpl implements BudgetOrganizationStructureService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetOrganizationStructureMapper organizationStructureMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetOrganizationStructure create(BudgetOrganizationStructure structure) {
        // 1. 参数校验
        if (structure == null) {
            throw new ServiceException("组织体系信息不能为空");
        }
        if (!StringUtils.hasText(structure.getStructureCode())) {
            throw new ServiceException("体系编码不能为空");
        }
        if (!StringUtils.hasText(structure.getStructureName())) {
            throw new ServiceException("体系名称不能为空");
        }

        // 2. 检查编码是否重复
        if (checkCodeExists(structure.getStructureCode())) {
            throw new ServiceException("体系编码已存在");
        }

        // 3. 设置默认值
        if (structure.getIsEnabled() == null) {
            structure.setIsEnabled(1);
        }
        if (structure.getIsDeleted() == null) {
            structure.setIsDeleted(0);
        }
        if (structure.getMaxLevels() == null) {
            structure.setMaxLevels(5);
        }
        structure.setCreateTime(new Date());
        structure.setUpdateTime(new Date());

        // 4. 插入数据库
        int result = organizationStructureMapper.insert(structure);
        if (result <= 0) {
            throw new ServiceException("创建组织体系失败");
        }

        log.info("创建组织体系成功，ID: {}, 编码: {}", structure.getStructureId(), structure.getStructureCode());
        return structure;
    }

    @Override
    public BudgetOrganizationStructure getById(String structureId) {
        if (!StringUtils.hasText(structureId)) {
            throw new ServiceException("组织体系ID不能为空");
        }
        
        QueryWrapper<BudgetOrganizationStructure> wrapper = new QueryWrapper<>();
        wrapper.eq("STRUCTURE_ID", structureId)
               .eq("IS_DELETED", 0);
        
        return organizationStructureMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetOrganizationStructure update(BudgetOrganizationStructure structure) {
        // 1. 参数校验
        if (structure == null || !StringUtils.hasText(structure.getStructureId())) {
            throw new ServiceException("组织体系ID不能为空");
        }

        // 2. 检查是否存在
        BudgetOrganizationStructure existing = getById(structure.getStructureId());
        if (existing == null) {
            throw new ServiceException("组织体系不存在");
        }

        // 3. 如果修改了编码，检查新编码是否重复
        if (StringUtils.hasText(structure.getStructureCode()) 
                && !structure.getStructureCode().equals(existing.getStructureCode())) {
            if (checkCodeExists(structure.getStructureCode())) {
                throw new ServiceException("体系编码已存在");
            }
        }

        // 4. 更新时间
        structure.setUpdateTime(new Date());

        // 5. 更新数据库
        int result = organizationStructureMapper.updateById(structure);
        if (result <= 0) {
            throw new ServiceException("更新组织体系失败");
        }

        log.info("更新组织体系成功，ID: {}", structure.getStructureId());
        return getById(structure.getStructureId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String structureId) {
        if (!StringUtils.hasText(structureId)) {
            throw new ServiceException("组织体系ID不能为空");
        }

        // 检查是否存在
        BudgetOrganizationStructure existing = getById(structureId);
        if (existing == null) {
            throw new ServiceException("组织体系不存在");
        }

        // 逻辑删除
        BudgetOrganizationStructure structure = new BudgetOrganizationStructure();
        structure.setStructureId(structureId);
        structure.setIsDeleted(1);
        structure.setUpdateTime(new Date());

        int result = organizationStructureMapper.updateById(structure);
        if (result <= 0) {
            throw new ServiceException("删除组织体系失败");
        }

        log.info("删除组织体系成功，ID: {}", structureId);
    }

    @Override
    public PageResult<BudgetOrganizationStructure> getPage(Map<String, Object> params) {
        // 1. 获取分页参数(兼容String和Integer类型)
        int pageNum = 1;
        int pageSize = 20;

        if (params.get("pageNum") != null) {
            Object pageNumObj = params.get("pageNum");
            pageNum = pageNumObj instanceof Integer ? (Integer) pageNumObj : Integer.parseInt(pageNumObj.toString());
        }

        if (params.get("pageSize") != null) {
            Object pageSizeObj = params.get("pageSize");
            pageSize = pageSizeObj instanceof Integer ? (Integer) pageSizeObj : Integer.parseInt(pageSizeObj.toString());
        }

        // 2. 构建查询条件
        QueryWrapper<BudgetOrganizationStructure> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);

        // 体系名称模糊查询
        if (params.get("structureName") != null && !"".equals(params.get("structureName").toString().trim())) {
            wrapper.like("STRUCTURE_NAME", params.get("structureName"));
        }

        // 体系类型
        if (params.get("structureType") != null && !"".equals(params.get("structureType").toString().trim())) {
            wrapper.eq("STRUCTURE_TYPE", params.get("structureType"));
        }

        // 控制模式
        if (params.get("controlMode") != null && !"".equals(params.get("controlMode").toString().trim())) {
            wrapper.eq("CONTROL_MODE", params.get("controlMode"));
        }

        // 状态
        if (params.get("status") != null && !"".equals(params.get("status").toString().trim())) {
            String status = (String) params.get("status");
            if ("active".equals(status)) {
                wrapper.eq("IS_ENABLED", 1);
            } else if ("inactive".equals(status)) {
                wrapper.eq("IS_ENABLED", 0);
            }
        }

        // 排序
        wrapper.orderByDesc("CREATE_TIME");

        // 3. 执行分页查询
        Page<BudgetOrganizationStructure> page = new Page<>(pageNum, pageSize);
        IPage<BudgetOrganizationStructure> pageResult = organizationStructureMapper.selectPage(page, wrapper);

        // 4. 封装返回结果
        PageResult<BudgetOrganizationStructure> result = new PageResult<>();
        result.setTlist(pageResult.getRecords());
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    public Map<String, Object> getTree(String structureId) {
        if (!StringUtils.hasText(structureId)) {
            throw new ServiceException("组织体系ID不能为空");
        }

        BudgetOrganizationStructure structure = getById(structureId);
        if (structure == null) {
            throw new ServiceException("组织体系不存在");
        }

        // 构建树结构数据
        Map<String, Object> tree = new HashMap<>();
        tree.put("structureId", structure.getStructureId());
        tree.put("structureCode", structure.getStructureCode());
        tree.put("structureName", structure.getStructureName());
        tree.put("structureType", structure.getStructureType());
        tree.put("controlMode", structure.getControlMode());
        tree.put("maxLevels", structure.getMaxLevels());
        tree.put("children", new ArrayList<>());

        return tree;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("请选择要删除的数据");
        }

        for (String id : ids) {
            delete(id);
        }

        log.info("批量删除组织体系成功，数量: {}", ids.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(String structureId) {
        if (!StringUtils.hasText(structureId)) {
            throw new ServiceException("组织体系ID不能为空");
        }

        BudgetOrganizationStructure structure = new BudgetOrganizationStructure();
        structure.setStructureId(structureId);
        structure.setIsEnabled(1);
        structure.setUpdateTime(new Date());

        int result = organizationStructureMapper.updateById(structure);
        if (result <= 0) {
            throw new ServiceException("启用组织体系失败");
        }

        log.info("启用组织体系成功，ID: {}", structureId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(String structureId) {
        if (!StringUtils.hasText(structureId)) {
            throw new ServiceException("组织体系ID不能为空");
        }

        BudgetOrganizationStructure structure = new BudgetOrganizationStructure();
        structure.setStructureId(structureId);
        structure.setIsEnabled(0);
        structure.setUpdateTime(new Date());

        int result = organizationStructureMapper.updateById(structure);
        if (result <= 0) {
            throw new ServiceException("禁用组织体系失败");
        }

        log.info("禁用组织体系成功，ID: {}", structureId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateEnabled(List<String> ids, int isEnabled) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("ID列表不能为空");
        }

        for (String structureId : ids) {
            if (!StringUtils.hasText(structureId)) {
                continue;
            }

            BudgetOrganizationStructure structure = new BudgetOrganizationStructure();
            structure.setStructureId(structureId);
            structure.setIsEnabled(isEnabled);
            structure.setUpdateTime(new Date());

            int result = organizationStructureMapper.updateById(structure);
            if (result <= 0) {
                log.warn("更新组织体系启用状态失败，ID: {}", structureId);
            }
        }

        log.info("批量更新组织体系启用状态成功，数量: {}, 状态: {}", ids.size(), isEnabled);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetOrganizationStructure copy(String structureId, String newName, String newCode) {
        // 1. 参数校验
        if (!StringUtils.hasText(structureId)) {
            throw new ServiceException("源组织体系ID不能为空");
        }
        if (!StringUtils.hasText(newName)) {
            throw new ServiceException("新体系名称不能为空");
        }
        if (!StringUtils.hasText(newCode)) {
            throw new ServiceException("新体系编码不能为空");
        }

        // 2. 查询源组织体系
        BudgetOrganizationStructure source = getById(structureId);
        if (source == null) {
            throw new ServiceException("源组织体系不存在");
        }

        // 3. 检查新编码是否重复
        if (checkCodeExists(newCode)) {
            throw new ServiceException("新体系编码已存在");
        }

        // 4. 复制组织体系
        BudgetOrganizationStructure copy = new BudgetOrganizationStructure();
        copy.setStructureCode(newCode);
        copy.setStructureName(newName);
        copy.setStructureType(source.getStructureType());
        copy.setControlMode(source.getControlMode());
        copy.setMaxLevels(source.getMaxLevels());
        copy.setIsEnabled(1);
        copy.setDescription(source.getDescription());
        copy.setRemark("复制自: " + source.getStructureName());

        return create(copy);
    }

    @Override
    public boolean checkCodeExists(String code) {
        if (!StringUtils.hasText(code)) {
            return false;
        }

        QueryWrapper<BudgetOrganizationStructure> wrapper = new QueryWrapper<>();
        wrapper.eq("STRUCTURE_CODE", code)
               .eq("IS_DELETED", 0);

        return organizationStructureMapper.selectCount(wrapper) > 0;
    }

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();

        // 总数统计
        QueryWrapper<BudgetOrganizationStructure> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);
        int total = organizationStructureMapper.selectCount(wrapper).intValue();
        stats.put("total", total);

        // 启用数量
        QueryWrapper<BudgetOrganizationStructure> enabledWrapper = new QueryWrapper<>();
        enabledWrapper.eq("IS_DELETED", 0).eq("IS_ENABLED", 1);
        int enabled = organizationStructureMapper.selectCount(enabledWrapper).intValue();
        stats.put("enabled", enabled);

        // 禁用数量
        stats.put("disabled", total - enabled);

        return stats;
    }

    @Override
    public List<BudgetOrganizationStructure> getAll(Map<String, Object> params) {
        QueryWrapper<BudgetOrganizationStructure> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);

        if (params != null) {
            if (params.get("structureName") != null && !"".equals(params.get("structureName").toString().trim())) {
                wrapper.like("STRUCTURE_NAME", params.get("structureName"));
            }
            if (params.get("structureType") != null && !"".equals(params.get("structureType").toString().trim())) {
                wrapper.eq("STRUCTURE_TYPE", params.get("structureType"));
            }
            if (params.get("controlMode") != null && !"".equals(params.get("controlMode").toString().trim())) {
                wrapper.eq("CONTROL_MODE", params.get("controlMode"));
            }
            if (params.get("status") != null && !"".equals(params.get("status").toString().trim())) {
                String status = params.get("status").toString();
                if ("active".equals(status)) {
                    wrapper.eq("IS_ENABLED", 1);
                } else if ("inactive".equals(status)) {
                    wrapper.eq("IS_ENABLED", 0);
                }
            }
        }

        wrapper.orderByDesc("CREATE_TIME");
        return organizationStructureMapper.selectList(wrapper);
    }
}

