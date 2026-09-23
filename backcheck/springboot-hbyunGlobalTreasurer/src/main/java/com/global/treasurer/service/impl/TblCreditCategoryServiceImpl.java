package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblCreditCategory;
import com.global.treasurer.mapper.TblCreditCategoryMapper;
import com.global.treasurer.service.TblCreditCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 授信类别Service实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
@Service
public class TblCreditCategoryServiceImpl implements TblCreditCategoryService {
    @Resource
    private TblCreditCategoryMapper tblCreditCategoryMapper;

    /**
     * 分页查询授信类别
     */
    @Override
    public PageInfo<TblCreditCategory> getCreditCategoryPage(Integer pageNum, Integer pageSize,
                                                             String categoryName, String categoryCode, String status) {
        // 使用PageHelper进行分页
        PageHelper.startPage(pageNum, pageSize);

        // 调用Mapper的条件查询方法
        List<TblCreditCategory> list = tblCreditCategoryMapper.selectByCondition(
                categoryName, categoryCode, status);

        // 返回分页结果
        return new PageInfo<>(list);
    }

    /**
     * 根据ID查询授信类别
     */
    @Override
    public TblCreditCategory getById(Long id) {
        return tblCreditCategoryMapper.selectById(id);
    }

    /**
     * 新增授信类别
     */
    @Override
    public boolean save(TblCreditCategory category) {
        return tblCreditCategoryMapper.insert(category) > 0;
    }

    /**
     * 更新授信类别
     */
    @Override
    public boolean updateById(TblCreditCategory category) {
        return tblCreditCategoryMapper.updateById(category) > 0;
    }

    /**
     * 删除授信类别
     */
    @Override
    public boolean removeById(Long id) {
        return tblCreditCategoryMapper.deleteById(id) > 0;
    }
}
