package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblBondCategory;
import com.global.treasurer.mapper.TblBondCategoryMapper;
import com.global.treasurer.service.TblBondCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 债券类别Service实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
@Service
public class TblBondCategoryServiceImpl implements TblBondCategoryService {
    @Resource
    private TblBondCategoryMapper tblBondCategoryMapper;

    /**
     * 分页查询债券类别
     */
    @Override
    public PageInfo<TblBondCategory> getBondCategoryPage(Integer pageNum, Integer pageSize,
                                                         String categoryName, String categoryCode, String status) {
        // 使用PageHelper进行分页
        PageHelper.startPage(pageNum, pageSize);

        // 调用Mapper的条件查询方法
        List<TblBondCategory> list = tblBondCategoryMapper.selectByCondition(
                categoryName, categoryCode, status);

        // 返回分页结果
        return new PageInfo<>(list);
    }
}
