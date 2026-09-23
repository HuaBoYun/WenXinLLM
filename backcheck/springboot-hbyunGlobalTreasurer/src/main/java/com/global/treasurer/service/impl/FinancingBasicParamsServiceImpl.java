package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.TblFinancingBasicParams;
import com.global.treasurer.mapper.TblFinancingBasicParamsMapper;
import com.global.treasurer.service.FinancingBasicParamsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 融资基础参数Service实现类
 *
 * @author 华博云开发团队
 * @since 2026-02-04
 */
@Service
public class FinancingBasicParamsServiceImpl extends ServiceImpl<TblFinancingBasicParamsMapper, TblFinancingBasicParams>
        implements FinancingBasicParamsService {
    private static final Logger log = LoggerFactory.getLogger(FinancingBasicParamsServiceImpl.class);

    @Override
    public List<TblFinancingBasicParams> getParamsList(String paramName, String paramType, String isEnabled) {
        QueryWrapper<TblFinancingBasicParams> wrapper = new QueryWrapper<>();

        // 参数名称模糊查询
        if (StringUtils.hasText(paramName)) {
            wrapper.like("PARAM_NAME", paramName);
        }

        // 参数类型精确查询
        if (StringUtils.hasText(paramType)) {
            wrapper.eq("PARAM_TYPE", paramType);
        }

        // 状态查询：将前端的isEnabled(0/1)转换为数据库的STATUS('DISABLE'/'ENABLE')
        if (StringUtils.hasText(isEnabled)) {
            String status = "1".equals(isEnabled) ? "ENABLE" : "DISABLE";
            wrapper.eq("STATUS", status);
        }

        // 按创建时间倒序排序
        wrapper.orderByDesc("CREATE_TIME");

        log.info("查询融资基础参数列表 - paramName:{}, paramType:{}, isEnabled:{}",
                 paramName, paramType, isEnabled);

        List<TblFinancingBasicParams> list = this.list(wrapper);

        // 将数据库的STATUS('ENABLE'/'DISABLE')转换为前端的isEnabled(1/0)
        for (TblFinancingBasicParams params : list) {
            if (params.getStatus() != null) {
                // 将'ENABLE'转换为"1"，'DISABLE'转换为"0"供前端使用
                params.setStatus("ENABLE".equalsIgnoreCase(params.getStatus()) ? "1" : "0");
            }
        }

        return list;
    }

    @Override
    public TblFinancingBasicParams getParamsById(Long id) {
        log.info("查询融资基础参数详情 - id:{}", id);
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveParams(TblFinancingBasicParams params) {
        log.info("保存融资基础参数 - params:{}", params);
        this.save(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateParams(TblFinancingBasicParams params) {
        log.info("更新融资基础参数 - params:{}", params);
        this.updateById(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteParams(Long id) {
        log.info("删除融资基础参数 - id:{}", id);
        this.removeById(id);
    }
}
