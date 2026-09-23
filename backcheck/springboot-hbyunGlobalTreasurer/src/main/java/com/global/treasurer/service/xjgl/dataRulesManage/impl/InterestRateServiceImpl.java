package com.global.treasurer.service.xjgl.dataRulesManage.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblGtInterestRate;
import com.global.treasurer.mapper.TblGtInterestRateMapper;
import com.global.treasurer.service.xjgl.dataRulesManage.InterestRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 利率管理Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Service
public class InterestRateServiceImpl extends ServiceImpl<TblGtInterestRateMapper, TblGtInterestRate> implements InterestRateService {
    private static final Logger log = LoggerFactory.getLogger(InterestRateServiceImpl.class);

    @Override
    public PageInfo<TblGtInterestRate> getInterestRateList(Map<String, Object> params, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        QueryWrapper<TblGtInterestRate> queryWrapper = new QueryWrapper<>();

        // 添加查询条件
        if (params.get("rateType") != null && !"".equals(params.get("rateType"))) {
            queryWrapper.eq("RATE_TYPE", params.get("rateType"));
        }
        if (params.get("currencyCode") != null && !"".equals(params.get("currencyCode"))) {
            queryWrapper.eq("CURRENCY_CODE", params.get("currencyCode"));
        }
        if (params.get("term") != null && !"".equals(params.get("term"))) {
            queryWrapper.eq("TERM", params.get("term"));
        }
        if (params.get("termUnit") != null && !"".equals(params.get("termUnit"))) {
            queryWrapper.eq("TERM_UNIT", params.get("termUnit"));
        }
        if (params.get("rateSource") != null && !"".equals(params.get("rateSource"))) {
            queryWrapper.eq("RATE_SOURCE", params.get("rateSource"));
        }

        // 只查询未删除的数据
        queryWrapper.eq("STATUS", 1);
        queryWrapper.orderByDesc("RATE_DATE", "CREATE_TIME");

        // 使用 baseMapper.selectList() 而非 this.list()，确保 PageHelper 能正确拦截
        List<TblGtInterestRate> list = this.baseMapper.selectList(queryWrapper);
        return new PageInfo<>(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createInterestRate(TblGtInterestRate interestRate) {
        interestRate.setCreateTime(new Date());
        interestRate.setUpdateTime(new Date());
        interestRate.setStatus(1);
        interestRate.setIsActive(1);
        return this.baseMapper.insert(interestRate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateInterestRate(TblGtInterestRate interestRate) {
        interestRate.setUpdateTime(new Date());
        return this.baseMapper.updateById(interestRate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteInterestRate(Long rateId) {
        TblGtInterestRate interestRate = new TblGtInterestRate();
        interestRate.setRateId(rateId);
        interestRate.setStatus(0);
        interestRate.setUpdateTime(new Date());
        return this.baseMapper.updateById(interestRate);
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 获取基准利率 (存款利率)
        QueryWrapper<TblGtInterestRate> benchmarkWrapper = new QueryWrapper<>();
        benchmarkWrapper.eq("RATE_TYPE", "DEPOSIT");
        benchmarkWrapper.eq("CURRENCY_CODE", "CNY");
        benchmarkWrapper.eq("TERM", 12);
        benchmarkWrapper.eq("TERM_UNIT", "MONTH");
        benchmarkWrapper.eq("STATUS", 1);
        benchmarkWrapper.orderByDesc("RATE_DATE");
        benchmarkWrapper.last("LIMIT 1");
        TblGtInterestRate benchmarkRate = this.baseMapper.selectOne(benchmarkWrapper);
        statistics.put("benchmarkRate", benchmarkRate != null ? benchmarkRate.getInterestRate().toString() : "2.75");

        // 获取存款利率
        QueryWrapper<TblGtInterestRate> depositWrapper = new QueryWrapper<>();
        depositWrapper.eq("RATE_TYPE", "DEPOSIT");
        depositWrapper.eq("CURRENCY_CODE", "CNY");
        depositWrapper.eq("STATUS", 1);
        depositWrapper.orderByDesc("RATE_DATE");
        depositWrapper.last("LIMIT 1");
        TblGtInterestRate depositRate = this.baseMapper.selectOne(depositWrapper);
        statistics.put("depositRate", depositRate != null ? depositRate.getInterestRate().toString() : "2.50");

        // 获取贷款利率
        QueryWrapper<TblGtInterestRate> loanWrapper = new QueryWrapper<>();
        loanWrapper.eq("RATE_TYPE", "LOAN");
        loanWrapper.eq("CURRENCY_CODE", "CNY");
        loanWrapper.eq("STATUS", 1);
        loanWrapper.orderByDesc("RATE_DATE");
        loanWrapper.last("LIMIT 1");
        TblGtInterestRate loanRate = this.baseMapper.selectOne(loanWrapper);
        statistics.put("loanRate", loanRate != null ? loanRate.getInterestRate().toString() : "4.35");

        statistics.put("lastUpdateTime", new Date().getMinutes() + "分钟前");

        return statistics;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int syncInterestRate(String sourceType) {
        // 模拟同步利率数据
        log.info("开始从{}同步利率数据", sourceType);

        // 这里应该调用外部API获取实时利率数据
        // 示例代码:
        // List<InterestRate> rates = externalApiService.getInterestRates(sourceType);
        // for (InterestRate rate : rates) {
        //     this.createInterestRate(rate);
        // }

        // 暂时返回0,表示没有同步新数据
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (Long id : ids) {
            count += deleteInterestRate(id);
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStatus(Long id, Integer status) {
        TblGtInterestRate interestRate = new TblGtInterestRate();
        interestRate.setRateId(id);
        interestRate.setStatus(status);
        interestRate.setUpdateTime(new Date());
        return this.baseMapper.updateById(interestRate);
    }
}
