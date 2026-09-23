package com.global.treasurer.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblGtUKeyInfo;
import com.global.treasurer.mapper.TblGtUKeyInfoMapper;
import com.global.treasurer.service.TblGtUKeyInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 全球司库-U盾信息Service实现类
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@Service
public class TblGtUKeyInfoServiceImpl extends ServiceImpl<TblGtUKeyInfoMapper, TblGtUKeyInfo>
        implements TblGtUKeyInfoService {
    @Override
    public IPage<TblGtUKeyInfo> getPageList(Page<TblGtUKeyInfo> page,
                                            BigDecimal orgId,
                                            Long ukeyId,
                                            String holderName,
                                            String ukeyNo,
                                            String ukeyStatus) {
        PageHelper.startPage((int) page.getCurrent(), (int) page.getSize());
        List<TblGtUKeyInfo> list = baseMapper.selectPageList(orgId, ukeyId, holderName, ukeyNo, ukeyStatus);

        // 获取分页信息
        PageInfo<TblGtUKeyInfo> pageInfo = new PageInfo<>(list);

        // 转换为 MyBatis-Plus 的 IPage 格式
        Page<TblGtUKeyInfo> pageResult = new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize());
        pageResult.setRecords(list);
        pageResult.setTotal(pageInfo.getTotal());

        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUKeyInfo(TblGtUKeyInfo entity) {
        // 设置默认U盾状态
        if (entity.getUkeyStatus() == null) {
            entity.setUkeyStatus("ACTIVE");
        }
        return save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUKeyInfo(TblGtUKeyInfo entity) {
        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUKeyInfo(BigDecimal ukeyId) {
        return removeById(ukeyId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean lockUKey(BigDecimal ukeyId, BigDecimal operatorId) {
        try {
            TblGtUKeyInfo entity = baseMapper.selectById(ukeyId);
            if (entity == null) {
                return false;
            }
            entity.setUkeyStatus("LOCKED");
            return updateById(entity);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlockUKey(BigDecimal ukeyId, BigDecimal operatorId) {
        try {
            TblGtUKeyInfo entity = baseMapper.selectById(ukeyId);
            if (entity == null) {
                return false;
            }
            entity.setUkeyStatus("ACTIVE");
            return updateById(entity);
        } catch (Exception e) {
            return false;
        }
    }
}
