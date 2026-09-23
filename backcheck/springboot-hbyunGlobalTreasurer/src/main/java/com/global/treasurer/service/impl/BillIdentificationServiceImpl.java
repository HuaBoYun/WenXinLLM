package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BillIdentificationDTO;
import com.global.treasurer.dto.BillIdentificationQueryDTO;
import com.global.treasurer.entity.TblBillIdentification;
import com.global.treasurer.mapper.BillIdentificationMapper;
import com.global.treasurer.service.IBillIdentificationService;
import com.global.treasurer.vo.BillIdentificationVO;
import com.hbfk.util.BizException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 票据标识Service实现类
 *
 * @author 华博云开发团队
 * @since 2026-02-02
 */
@Service
public class BillIdentificationServiceImpl extends ServiceImpl<BillIdentificationMapper, TblBillIdentification>
        implements IBillIdentificationService {
    @Override
    public PageInfo<BillIdentificationVO> selectBillIdentificationList(BillIdentificationQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<BillIdentificationVO> list = baseMapper.selectBillIdentificationList(queryDTO);
        // 设置类型名称
        list.forEach(this::setTypeName);
        return new PageInfo<>(list);
    }

    @Override
    public BillIdentificationVO selectBillIdentificationById(Long identificationId) {
        if (identificationId == null) {
            throw new BizException("标识ID不能为空");
        }
        BillIdentificationVO vo = baseMapper.selectBillIdentificationById(identificationId);
        if (vo != null) {
            setTypeName(vo);
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblBillIdentification insertBillIdentification(BillIdentificationDTO dto) {
        // 生成标识编码（如果未提供）
        if (!StringUtils.hasText(dto.getIdentificationCode())) {
            dto.setIdentificationCode(generateIdentificationCode(dto.getIdentificationType()));
        }
        // 检查编码是否已存在
        if (checkCodeExists(dto.getIdentificationCode(), null)) {
            throw new BizException("标识编码已存在");
        }
        TblBillIdentification entity = new TblBillIdentification();
        BeanUtils.copyProperties(dto, entity);
        entity.setIdentificationId(generateId());
        entity.setIsSystem(0); // 用户创建的标识非系统内置
        entity.setIsEnabled(dto.getIsEnabled() != null ? dto.getIsEnabled() : 1);
        entity.setDeleteFlag(0);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        baseMapper.insert(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblBillIdentification updateBillIdentification(BillIdentificationDTO dto) {
        if (dto.getIdentificationId() == null) {
            throw new BizException("标识ID不能为空");
        }
        TblBillIdentification existing = baseMapper.selectById(dto.getIdentificationId());
        if (existing == null) {
            throw new BizException("标识不存在");
        }
        if (existing.getIsSystem() != null && existing.getIsSystem() == 1) {
            throw new BizException("系统内置标识不允许修改");
        }
        // 检查编码是否已存在（排除自身）
        if (StringUtils.hasText(dto.getIdentificationCode()) && 
            checkCodeExists(dto.getIdentificationCode(), dto.getIdentificationId())) {
            throw new BizException("标识编码已存在");
        }
        BeanUtils.copyProperties(dto, existing, "identificationId", "isSystem", "deleteFlag", "createTime", "createBy");
        existing.setUpdateTime(new Date());
        baseMapper.updateById(existing);
        return existing;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBillIdentificationByIds(Long[] identificationIds) {
        if (identificationIds == null || identificationIds.length == 0) {
            throw new BizException("请选择要删除的标识");
        }
        // 检查是否有系统内置标识
        for (Long id : identificationIds) {
            TblBillIdentification entity = baseMapper.selectById(id);
            if (entity != null && entity.getIsSystem() != null && entity.getIsSystem() == 1) {
                throw new BizException("系统内置标识不允许删除");
            }
        }
        return baseMapper.deleteBillIdentificationByIds(identificationIds) > 0;
    }

    @Override
    public List<BillIdentificationVO> selectByType(String identificationType) {
        List<BillIdentificationVO> list = baseMapper.selectByType(identificationType);
        list.forEach(this::setTypeName);
        return list;
    }

    @Override
    public List<BillIdentificationVO> selectIdentificationTree(BillIdentificationQueryDTO queryDTO) {
        // 查询所有顶级标识（parentId为空）
        queryDTO.setParentId(null);
        List<BillIdentificationVO> allList = baseMapper.selectBillIdentificationList(queryDTO);
        // 构建树形结构
        List<BillIdentificationVO> rootList = allList.stream()
                .filter(item -> item.getParentId() == null)
                .collect(Collectors.toList());
        rootList.forEach(root -> {
            setTypeName(root);
            root.setChildren(getChildren(root.getIdentificationId(), allList));
        });
        return rootList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateEnabled(Long[] identificationIds, Integer isEnabled) {
        if (identificationIds == null || identificationIds.length == 0) {
            throw new BizException("请选择要操作的标识");
        }
        return baseMapper.batchUpdateEnabled(identificationIds, isEnabled) > 0;
    }

    @Override
    public boolean checkCodeExists(String identificationCode, Long excludeId) {
        return baseMapper.checkCodeExists(identificationCode, excludeId) > 0;
    }

    private List<BillIdentificationVO> getChildren(Long parentId, List<BillIdentificationVO> allList) {
        return allList.stream()
                .filter(item -> parentId.equals(item.getParentId()))
                .peek(item -> {
                    setTypeName(item);
                    item.setChildren(getChildren(item.getIdentificationId(), allList));
                })
                .collect(Collectors.toList());
    }

    private void setTypeName(BillIdentificationVO vo) {
        if (vo.getIdentificationType() != null) {
            switch (vo.getIdentificationType()) {
                case "CATEGORY": vo.setIdentificationTypeName("分类标识"); break;
                case "TAG": vo.setIdentificationTypeName("标签"); break;
                case "PRIORITY": vo.setIdentificationTypeName("优先级"); break;
                case "RISK": vo.setIdentificationTypeName("风险等级"); break;
                default: vo.setIdentificationTypeName(vo.getIdentificationType());
            }
        }
    }

    private String generateIdentificationCode(String type) {
        String prefix = type != null ? type.substring(0, Math.min(3, type.length())).toUpperCase() : "IDT";
        return prefix + "_" + System.currentTimeMillis();
    }

    private Long generateId() {
        return System.currentTimeMillis() + (long)(Math.random() * 10000);
    }
}

