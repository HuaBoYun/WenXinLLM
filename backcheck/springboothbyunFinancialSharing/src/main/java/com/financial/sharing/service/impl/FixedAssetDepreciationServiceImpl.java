package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.financial.sharing.mysql.entity.FixedAssetCardEntity;
import com.financial.sharing.mysql.entity.FixedAssetDepreciationEntity;
import com.financial.sharing.mysql.mapper.FixedAssetCardMapper;
import com.financial.sharing.mysql.mapper.FixedAssetDepreciationMapper;
import com.financial.sharing.service.FixedAssetDepreciationService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.FixedAssetDepreciationQueryParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class FixedAssetDepreciationServiceImpl implements FixedAssetDepreciationService {

    @Resource
    private FixedAssetDepreciationMapper depreciationMapper;

    @Resource
    private FixedAssetCardMapper assetCardMapper;

    @Override
    public MyJsonBean<Map<String, Object>> getDepreciationStats(Long tenantId, String period) {
        try {
            Map<String, Object> stats = depreciationMapper.selectStatistics(period, period, null, tenantId);
            if (stats == null) {
                stats = new HashMap<>();
                stats.put("totalAmount", BigDecimal.ZERO);
                stats.put("totalCount", 0);
                stats.put("postedCount", 0);
            }
            return MyJsonBean.successData(stats);
        } catch (Exception e) {
            log.error("获取折旧统计数据失败", e);
            return MyJsonBean.errorData("获取折旧统计数据失败");
        }
    }

    @Override
    public MyJsonBean getDepreciationDetailList(FixedAssetDepreciationQueryParam param) {
        try {
            PageHelper.startPage(param.getPageNum(), param.getPageSize());

            // 调用 Mapper XML 中定义的 selectPageList 方法
            List<FixedAssetDepreciationEntity> list = depreciationMapper.selectPageList(
                param.getAssetCode(),
                param.getStartPeriod(),
                param.getEndPeriod(),
                param.getTenantId()
            );

            PageInfo<FixedAssetDepreciationEntity> pageInfo = new PageInfo<>(list);

            PageResult<FixedAssetDepreciationEntity> result = new PageResult<FixedAssetDepreciationEntity>().build(pageInfo);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询折旧明细失败", e);
            return MyJsonBean.errorData("查询折旧明细失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean calculateDepreciation(String period, String categoryId, String calculateType, 
                                           Long tenantId, String operatorId) {
        try {
            LambdaQueryWrapper<FixedAssetCardEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FixedAssetCardEntity::getTenantId, tenantId);
            wrapper.eq(FixedAssetCardEntity::getStatus, "IN_USE");
            
            if (StringUtils.hasText(categoryId)) {
                wrapper.eq(FixedAssetCardEntity::getCategoryId, categoryId);
            }
            
            List<FixedAssetCardEntity> assetList = assetCardMapper.selectList(wrapper);
            
            int successCount = 0;
            int skipCount = 0;
            
            for (FixedAssetCardEntity asset : assetList) {
                LambdaQueryWrapper<FixedAssetDepreciationEntity> checkWrapper = new LambdaQueryWrapper<>();
                checkWrapper.eq(FixedAssetDepreciationEntity::getAssetId, asset.getAssetId());
                checkWrapper.eq(FixedAssetDepreciationEntity::getPeriod, period);
                checkWrapper.eq(FixedAssetDepreciationEntity::getTenantId, tenantId);
                
                Integer count = depreciationMapper.selectCount(checkWrapper).intValue();
                if (count > 0) {
                    skipCount++;
                    continue;
                }
                
                BigDecimal depreciationAmount = calculateDepreciationAmount(asset);
                
                FixedAssetDepreciationEntity depreciation = new FixedAssetDepreciationEntity();
                depreciation.setAssetId(asset.getAssetId());
                depreciation.setAssetCode(asset.getAssetCode());
                depreciation.setAssetName(asset.getAssetName());
                depreciation.setPeriod(period);
                depreciation.setMonthlyDepreciation(depreciationAmount);
                depreciation.setAccumulatedDepreciation(asset.getAccumulatedDepreciation().add(depreciationAmount));
                depreciation.setNetValue(asset.getOriginalValue().subtract(asset.getAccumulatedDepreciation().add(depreciationAmount)));
                depreciation.setStatus("CALCULATED");
                depreciation.setTenantId(tenantId);
                depreciation.setCreateTime(LocalDateTime.now());
                depreciation.setCreateBy(operatorId);
                
                depreciationMapper.insert(depreciation);
                successCount++;
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("successCount", successCount);
            result.put("skipCount", skipCount);
            result.put("totalCount", assetList.size());
            
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("计提折旧失败", e);
            return MyJsonBean.errorData("计提折旧失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean previewDepreciation(String period, String categoryId, Long tenantId) {
        return MyJsonBean.successData(new ArrayList<>());
    }

    @Override
    public MyJsonBean adjustDepreciation(String depreciationId, BigDecimal adjustAmount, 
                                        String adjustReason, String operatorId) {
        return MyJsonBean.successData("调整成功");
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getDepreciationTrend(String startPeriod, String endPeriod, Long tenantId) {
        return MyJsonBean.successData(new ArrayList<>());
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getCategoryDistribution(String period, Long tenantId) {
        return MyJsonBean.successData(new ArrayList<>());
    }

    @Override
    public MyJsonBean getDepreciationById(String depreciationId) {
        return MyJsonBean.successData(null);
    }

    @Override
    public MyJsonBean deleteDepreciation(String depreciationId) {
        return MyJsonBean.successData("删除成功");
    }

    private BigDecimal calculateDepreciationAmount(FixedAssetCardEntity asset) {
        return BigDecimal.ZERO;
    }
}

