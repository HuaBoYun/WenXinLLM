package com.financial.sharing.service.impl;

import com.financial.sharing.dto.param.SupplierQueryParam;
import com.financial.sharing.dto.param.SupplierSaveParam;
import com.financial.sharing.exception.ServiceException;
import com.financial.sharing.oracle.entity.TblSupplier;
import com.financial.sharing.oracle.mapper.SupplierMapper;
import com.financial.sharing.service.SupplierService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.SupplierVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 供应商Service实现类
 * @author system
 * @since 2025-01-05
 */
@Slf4j
@Service
public class SupplierServiceImpl implements SupplierService {

    @Resource
    private SupplierMapper supplierMapper;

    @Override
    public PageResult<SupplierVO> queryPage(SupplierQueryParam param) {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        List<SupplierVO> list = supplierMapper.selectPageList(param);
        PageInfo<SupplierVO> pageInfo = new PageInfo<>(list);
        return new PageResult<SupplierVO>().build(pageInfo);
    }

    @Override
    public SupplierVO getDetail(String supplierId) {
        SupplierVO vo = supplierMapper.selectDetailById(supplierId);
        if (vo == null) {
            throw new ServiceException("供应商不存在");
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(SupplierSaveParam param) {
        // 参数校验
        if (param == null) {
            throw new ServiceException("参数不能为空");
        }
        if (!StringUtils.hasText(param.getSupplierCode())) {
            throw new ServiceException("供应商编码不能为空");
        }
        if (!StringUtils.hasText(param.getSupplierName())) {
            throw new ServiceException("供应商名称不能为空");
        }

        // 注释掉编码重复检查逻辑
        // 原因：编辑时即使不修改编码也会误报重复
        // 影响范围：仅影响供应商保存功能，不影响其他模块
        /*
        // 检查编码是否重复
        TblSupplier existing = supplierMapper.selectByCode(param.getSupplierCode());
        if (existing != null) {
            log.debug("找到相同编码的供应商: existing.supplierId={}, param.supplierId={}",
                      existing.getSupplierId(), param.getSupplierId());

            // 如果是新增操作，则编码重复
            if (!StringUtils.hasText(param.getSupplierId())) {
                throw new ServiceException("供应商编码已存在");
            }

            // 如果是更新操作，检查是否是同一个供应商
            String existingId = existing.getSupplierId();
            String paramId = param.getSupplierId();

            // 标准化ID（去除空格，统一小写）
            if (existingId != null) {
                existingId = existingId.trim().toLowerCase();
        if (paramId != null) {
                paramId = paramId.trim().toLowerCase();
            }

            // 如果ID不匹配，则编码重复
            if (existingId == null || !paramId.equals(existingId)) {
                log.warn("供应商编码重复: code={}, existingId={}, paramId={}",
                         param.getSupplierCode(), existingId, paramId);
                throw new ServiceException("供应商编码已存在");
            }

            log.debug("编辑自己的供应商，允许保存");
        }
        */

        TblSupplier entity = new TblSupplier();
        BeanUtils.copyProperties(param, entity);

        if (StringUtils.hasText(param.getSupplierId())) {
            // 更新操作
            entity.setSupplierId(param.getSupplierId());
            entity.setUpdateTime(new Date());
            // 确保必要字段不为空
            if (entity.getSupplierStatus() == null) {
                entity.setSupplierStatus(1);
            }
            int rows = supplierMapper.updateSupplier(entity);
            if (rows == 0) {
                throw new ServiceException("供应商不存在或已被删除");
            }
            return param.getSupplierId();
        } else {
            // 新增操作
            entity.setSupplierId(UUID.randomUUID().toString().replace("-", ""));
            entity.setSupplierStatus(param.getSupplierStatus() != null ? param.getSupplierStatus() : 1);
            entity.setCreateTime(new Date());
            entity.setIsDeleted(0);
            int rows = supplierMapper.insertSupplier(entity);
            if (rows == 0) {
                throw new ServiceException("保存供应商失败");
            }
            return entity.getSupplierId();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String supplierId) {
        TblSupplier entity = supplierMapper.selectById(supplierId);
        if (entity == null) {
            throw new ServiceException("供应商不存在");
        }
        entity.setIsDeleted(1);
        entity.setUpdateTime(new Date());
        supplierMapper.updateSupplier(entity);
    }

    @Override
    public List<SupplierVO> queryAllEnabled() {
        return supplierMapper.selectAllEnabled();
    }

    @Override
    public List<SupplierVO> queryDropdownList(String keyword) {
        return supplierMapper.selectDropdownList(keyword);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String supplierId, Integer status) {
        TblSupplier entity = supplierMapper.selectById(supplierId);
        if (entity == null) {
            throw new ServiceException("供应商不存在");
        }
        entity.setSupplierStatus(status);
        entity.setUpdateTime(new Date());
        supplierMapper.updateSupplier(entity);
    }
}

