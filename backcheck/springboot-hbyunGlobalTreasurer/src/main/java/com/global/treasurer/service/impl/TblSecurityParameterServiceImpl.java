package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.TblSecurityParameter;
import com.global.treasurer.mapper.TblSecurityParameterMapper;
import com.global.treasurer.service.TblSecurityParameterService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 安全参数Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
@Service
public class TblSecurityParameterServiceImpl extends ServiceImpl<TblSecurityParameterMapper, TblSecurityParameter>
        implements TblSecurityParameterService {
    @Override
    public IPage<TblSecurityParameter> getSecurityParameterPage(Integer page, Integer limit,
                                                            String paramCode, String paramName,
                                                            String paramType, Integer isEnabled) {
        Page<TblSecurityParameter> pageParam = new Page<>(page, limit);
        QueryWrapper<TblSecurityParameter> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(paramCode)) {
            queryWrapper.like("PARAM_CODE", paramCode);
        }
        if (StringUtils.hasText(paramName)) {
            queryWrapper.like("PARAM_NAME", paramName);
        }
        if (StringUtils.hasText(paramType)) {
            queryWrapper.eq("PARAM_TYPE", paramType);
        }
        if (isEnabled != null) {
            queryWrapper.eq("IS_ENABLED", isEnabled);
        }

        // 添加排序
        queryWrapper.orderByDesc("CREATE_TIME");
        return this.page(pageParam, queryWrapper);
    }
}
