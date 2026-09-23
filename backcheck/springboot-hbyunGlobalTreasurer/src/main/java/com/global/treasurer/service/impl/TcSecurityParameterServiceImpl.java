package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.TcSecurityParameter;
import com.global.treasurer.mapper.TcSecurityParameterMapper;
import com.global.treasurer.service.TcSecurityParameterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 安全参数配置Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-28
 */
@Service
public class TcSecurityParameterServiceImpl extends ServiceImpl<TcSecurityParameterMapper, TcSecurityParameter>
        implements TcSecurityParameterService {
    @Override
    public List<TcSecurityParameter> selectPageList(Map<String, Object> params) {
        return this.baseMapper.selectPageList(params);
    }

    @Override
    public TcSecurityParameter selectDetailById(Long id) {
        return this.baseMapper.selectDetailById(id);
    }

    @Override
    public TcSecurityParameter selectByParamCode(String paramCode) {
        return this.baseMapper.selectByParamCode(paramCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveParameter(TcSecurityParameter entity) {
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        return this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateParameter(TcSecurityParameter entity) {
        entity.setUpdateTime(new Date());
        return this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteParameter(Long id) {
        return this.removeById(id);
    }
}
