package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.CounterpartInfo;
import com.huabo.contract.mapper.CounterpartInfoMapper;
import com.huabo.contract.service.CounterpartInfoService;
import com.huabo.contract.vo.CounterpartInfoQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 相对方信息表 服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@Service
public class CounterpartInfoServiceImpl extends ServiceImpl<CounterpartInfoMapper, CounterpartInfo> implements CounterpartInfoService {

    @Autowired
    private CounterpartInfoMapper counterpartInfoMapper;

    @Override
    public PageInfo<CounterpartInfo> getCounterpartInfoList(CounterpartInfoQueryParam param) {
        try {
            // 设置分页参数
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            
            // 查询数据
            List<CounterpartInfo> list = counterpartInfoMapper.selectCounterpartInfoList(param);
            
            return new PageInfo<>(list);
        } catch (Exception e) {
            log.error("查询相对方信息列表失败", e);
            throw new RuntimeException("查询相对方信息列表失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveCounterpartInfo(CounterpartInfo counterpartInfo) {
        try {
            Date now = new Date();
            
            if (counterpartInfo.getId() == null) {
                // 新增
                counterpartInfo.setCreateTime(now);
                counterpartInfo.setUpdateTime(now);
                return this.save(counterpartInfo);
            } else {
                // 修改
                counterpartInfo.setUpdateTime(now);
                return this.updateById(counterpartInfo);
            }
        } catch (Exception e) {
            log.error("保存相对方信息失败", e);
            throw new RuntimeException("保存相对方信息失败：" + e.getMessage());
        }
    }

    @Override
    public CounterpartInfo getCounterpartInfoById(Long id) {
        try {
            return this.getById(id);
        } catch (Exception e) {
            log.error("获取相对方信息详情失败，ID：{}", id, e);
            throw new RuntimeException("获取相对方信息详情失败：" + e.getMessage());
        }
    }

    @Override
    public CounterpartInfo getCounterpartInfoByCompanyName(String companyName) {
        try {
            return counterpartInfoMapper.selectByCompanyName(companyName);
        } catch (Exception e) {
            log.error("根据公司名称获取相对方信息失败，公司名称：{}", companyName, e);
            throw new RuntimeException("根据公司名称获取相对方信息失败：" + e.getMessage());
        }
    }

    @Override
    public CounterpartInfo getCounterpartInfoByCompanyCode(String companyCode) {
        try {
            return counterpartInfoMapper.selectByCompanyCode(companyCode);
        } catch (Exception e) {
            log.error("根据公司代码获取相对方信息失败，公司代码：{}", companyCode, e);
            throw new RuntimeException("根据公司代码获取相对方信息失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCounterpartInfo(Long id) {
        try {
            return this.removeById(id);
        } catch (Exception e) {
            log.error("删除相对方信息失败，ID：{}", id, e);
            throw new RuntimeException("删除相对方信息失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteCounterpartInfo(List<Long> ids) {
        try {
            return this.removeByIds(ids);
        } catch (Exception e) {
            log.error("批量删除相对方信息失败", e);
            throw new RuntimeException("批量删除相对方信息失败：" + e.getMessage());
        }
    }

    @Override
    public boolean existsCompanyName(String companyName, Long excludeId) {
        try {
            return counterpartInfoMapper.existsCompanyName(companyName, excludeId);
        } catch (Exception e) {
            log.error("检查公司名称是否存在失败", e);
            return false;
        }
    }

    @Override
    public boolean existsCompanyCode(String companyCode, Long excludeId) {
        try {
            return counterpartInfoMapper.existsCompanyCode(companyCode, excludeId);
        } catch (Exception e) {
            log.error("检查公司代码是否存在失败", e);
            return false;
        }
    }

    @Override
    public List<CounterpartInfo> getBlacklistCounterparts() {
        try {
            return counterpartInfoMapper.selectBlacklistCounterparts();
        } catch (Exception e) {
            log.error("获取黑名单相对方列表失败", e);
            throw new RuntimeException("获取黑名单相对方列表失败：" + e.getMessage());
        }
    }

    @Override
    public List<CounterpartInfo> getHighCreditCounterparts() {
        try {
            return counterpartInfoMapper.selectHighCreditCounterparts();
        } catch (Exception e) {
            log.error("获取高信用等级相对方列表失败", e);
            throw new RuntimeException("获取高信用等级相对方列表失败：" + e.getMessage());
        }
    }

    @Override
    public List<CounterpartInfo> getLowCreditCounterparts() {
        try {
            return counterpartInfoMapper.selectLowCreditCounterparts();
        } catch (Exception e) {
            log.error("获取低信用等级相对方列表失败", e);
            throw new RuntimeException("获取低信用等级相对方列表失败：" + e.getMessage());
        }
    }

    @Override
    public List<CounterpartInfo> getLargeEnterpriseCounterparts() {
        try {
            return counterpartInfoMapper.selectLargeEnterpriseCounterparts();
        } catch (Exception e) {
            log.error("获取大型企业相对方列表失败", e);
            throw new RuntimeException("获取大型企业相对方列表失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBlacklistFlag(Long id, Integer blacklistFlag) {
        try {
            CounterpartInfo counterpartInfo = new CounterpartInfo();
            counterpartInfo.setId(id);
            counterpartInfo.setBlacklistFlag(blacklistFlag);
            counterpartInfo.setUpdateTime(new Date());
            
            return this.updateById(counterpartInfo);
        } catch (Exception e) {
            log.error("更新黑名单状态失败，ID：{}，状态：{}", id, blacklistFlag, e);
            throw new RuntimeException("更新黑名单状态失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateBlacklistFlag(List<Long> ids, Integer blacklistFlag) {
        try {
            return counterpartInfoMapper.batchUpdateBlacklistFlag(ids, blacklistFlag) > 0;
        } catch (Exception e) {
            log.error("批量更新黑名单状态失败", e);
            throw new RuntimeException("批量更新黑名单状态失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCreditRating(Long id, String creditRating) {
        try {
            CounterpartInfo counterpartInfo = new CounterpartInfo();
            counterpartInfo.setId(id);
            counterpartInfo.setCreditRating(creditRating);
            counterpartInfo.setUpdateTime(new Date());
            
            return this.updateById(counterpartInfo);
        } catch (Exception e) {
            log.error("更新信用等级失败，ID：{}，等级：{}", id, creditRating, e);
            throw new RuntimeException("更新信用等级失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateCreditRating(List<Long> ids, String creditRating) {
        try {
            return counterpartInfoMapper.batchUpdateCreditRating(ids, creditRating) > 0;
        } catch (Exception e) {
            log.error("批量更新信用等级失败", e);
            throw new RuntimeException("批量更新信用等级失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getCounterpartStatistics(CounterpartInfoQueryParam param) {
        try {
            return counterpartInfoMapper.selectCounterpartStatistics(param);
        } catch (Exception e) {
            log.error("获取相对方统计数据失败", e);
            throw new RuntimeException("获取相对方统计数据失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getCreditRatingDistribution(CounterpartInfoQueryParam param) {
        try {
            return counterpartInfoMapper.selectCreditRatingDistribution(param);
        } catch (Exception e) {
            log.error("获取信用等级分布统计失败", e);
            throw new RuntimeException("获取信用等级分布统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getRegisteredCapitalDistribution(CounterpartInfoQueryParam param) {
        try {
            return counterpartInfoMapper.selectRegisteredCapitalDistribution(param);
        } catch (Exception e) {
            log.error("获取注册资本分布统计失败", e);
            throw new RuntimeException("获取注册资本分布统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getRegionDistribution(CounterpartInfoQueryParam param) {
        try {
            return counterpartInfoMapper.selectRegionDistribution(param);
        } catch (Exception e) {
            log.error("获取地区分布统计失败", e);
            throw new RuntimeException("获取地区分布统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<CounterpartInfo> searchCounterparts(String keyword, Integer limit) {
        try {
            if (limit == null || limit <= 0) {
                limit = 10;
            }
            return counterpartInfoMapper.searchCounterparts(keyword, limit);
        } catch (Exception e) {
            log.error("搜索相对方信息失败，关键词：{}", keyword, e);
            throw new RuntimeException("搜索相对方信息失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importCounterpartInfo(List<CounterpartInfo> counterpartList) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;
        
        try {
            for (int i = 0; i < counterpartList.size(); i++) {
                CounterpartInfo counterpart = counterpartList.get(i);
                String prefix = "第" + (i + 1) + "行：";
                
                try {
                    // 验证必填字段
                    if (!StringUtils.hasText(counterpart.getCompanyName())) {
                        errors.add(prefix + "公司名称不能为空");
                        errorCount++;
                        continue;
                    }
                    
                    // 检查重复
                    if (existsCompanyName(counterpart.getCompanyName(), null)) {
                        errors.add(prefix + "公司名称已存在：" + counterpart.getCompanyName());
                        errorCount++;
                        continue;
                    }
                    
                    if (StringUtils.hasText(counterpart.getCompanyCode()) && 
                        existsCompanyCode(counterpart.getCompanyCode(), null)) {
                        errors.add(prefix + "公司代码已存在：" + counterpart.getCompanyCode());
                        errorCount++;
                        continue;
                    }
                    
                    // 保存
                    if (saveCounterpartInfo(counterpart)) {
                        successCount++;
                    } else {
                        errors.add(prefix + "保存失败");
                        errorCount++;
                    }
                } catch (Exception e) {
                    errors.add(prefix + "处理失败：" + e.getMessage());
                    errorCount++;
                }
            }
            
            result.put("success", true);
            result.put("successCount", successCount);
            result.put("errorCount", errorCount);
            result.put("errors", errors);
            
            return result;
        } catch (Exception e) {
            log.error("导入相对方信息失败", e);
            result.put("success", false);
            result.put("message", "导入失败：" + e.getMessage());
            return result;
        }
    }

    @Override
    public List<CounterpartInfo> exportCounterpartInfo(CounterpartInfoQueryParam param) {
        try {
            // 不分页，查询所有数据
            param.setPageNumber(null);
            param.setPageSize(null);
            
            return counterpartInfoMapper.selectCounterpartInfoList(param);
        } catch (Exception e) {
            log.error("导出相对方信息失败", e);
            throw new RuntimeException("导出相对方信息失败：" + e.getMessage());
        }
    }
}
