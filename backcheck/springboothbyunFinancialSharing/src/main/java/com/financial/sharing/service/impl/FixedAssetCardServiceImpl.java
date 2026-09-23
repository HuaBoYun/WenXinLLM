package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.financial.sharing.mysql.entity.FixedAssetCardEntity;
import com.financial.sharing.mysql.mapper.FixedAssetCardMapper;
import com.financial.sharing.service.FixedAssetCardService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.FixedAssetCardQueryParam;
import com.financial.sharing.vo.param.FixedAssetCardSaveParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 固定资产卡片服务实现类
 * @author system
 * @since 2026-01-21
 */
@Slf4j
@Service
public class FixedAssetCardServiceImpl implements FixedAssetCardService {

    @Resource
    private FixedAssetCardMapper fixedAssetCardMapper;

    @Override
    public MyJsonBean<PageResult> getAssetCardList(FixedAssetCardQueryParam param) {
        try {
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            List<FixedAssetCardEntity> list = fixedAssetCardMapper.selectPageList(param);
            PageInfo<FixedAssetCardEntity> pageInfo = new PageInfo<>(list);

            PageResult result = new PageResult();
            result.setTlist(pageInfo.getList());
            result.setTotalRecord((int) pageInfo.getTotal());
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询资产卡片列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getAssetCardById(String assetId) {
        try {
            FixedAssetCardEntity entity = fixedAssetCardMapper.selectDetailById(assetId);
            if (entity == null) {
                return MyJsonBean.errorData("资产卡片不存在");
            }
            return MyJsonBean.successData(entity);
        } catch (Exception e) {
            log.error("查询资产卡片详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdateAssetCard(FixedAssetCardSaveParam param) {
        try {
            // 检查资产编码是否重复
            LambdaQueryWrapper<FixedAssetCardEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FixedAssetCardEntity::getAssetCode, param.getAssetCode());
            wrapper.eq(FixedAssetCardEntity::getTenantId, param.getTenantId());
            if (StringUtils.hasText(param.getAssetId())) {
                wrapper.ne(FixedAssetCardEntity::getAssetId, param.getAssetId());
            }
            long count = fixedAssetCardMapper.selectCount(wrapper);
            if (count > 0) {
                return MyJsonBean.errorData("资产编码已存在");
            }

            FixedAssetCardEntity entity = new FixedAssetCardEntity();
            BeanUtils.copyProperties(param, entity);

            // 计算净值
            BigDecimal netValue = param.getOriginalValue();
            if (param.getResidualValue() != null) {
                netValue = netValue.subtract(param.getResidualValue());
            }
            entity.setNetValue(netValue);
            entity.setAccumulatedDepreciation(BigDecimal.ZERO);

            if (StringUtils.hasText(param.getAssetId())) {
                // 更新
                entity.setUpdateTime(LocalDateTime.now());
                if (StringUtils.hasText(param.getOperatorId())) {
                    entity.setUpdateBy(param.getOperatorId());
                }
                fixedAssetCardMapper.updateById(entity);
                return MyJsonBean.successMsg("更新成功");
            } else {
                // 新增
                entity.setCreateTime(LocalDateTime.now());
                if (StringUtils.hasText(param.getOperatorId())) {
                    entity.setCreateBy(param.getOperatorId());
                }
                entity.setStatus("NORMAL"); // 默认状态为正常
                fixedAssetCardMapper.insert(entity);
                return MyJsonBean.successMsg("新增成功");
            }
        } catch (Exception e) {
            log.error("保存资产卡片失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteAssetCard(String assetId) {
        try {
            FixedAssetCardEntity entity = fixedAssetCardMapper.selectById(assetId);
            if (entity == null) {
                return MyJsonBean.errorData("资产卡片不存在");
            }
            fixedAssetCardMapper.deleteById(assetId);
            return MyJsonBean.successMsg("删除成功");
        } catch (Exception e) {
            log.error("删除资产卡片失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchDeleteAssetCard(List<String> assetIds) {
        try {
            if (assetIds == null || assetIds.isEmpty()) {
                return MyJsonBean.errorData("请选择要删除的资产");
            }
            fixedAssetCardMapper.deleteBatchIds(assetIds);
            return MyJsonBean.successMsg("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除资产卡片失败", e);
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchUpdateStatus(List<String> assetIds, String status) {
        try {
            if (assetIds == null || assetIds.isEmpty()) {
                return MyJsonBean.errorData("请选择要更新的资产");
            }
            for (String assetId : assetIds) {
                FixedAssetCardEntity entity = new FixedAssetCardEntity();
                entity.setAssetId(assetId);
                entity.setStatus(status);
                entity.setUpdateTime(LocalDateTime.now());
                fixedAssetCardMapper.updateById(entity);
            }
            return MyJsonBean.successMsg("批量更新状态成功");
        } catch (Exception e) {
            log.error("批量更新资产状态失败", e);
            return MyJsonBean.errorData("批量更新失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getAssetSummary(Long tenantId) {
        try {
            Map<String, Object> summary = fixedAssetCardMapper.selectAssetSummary(tenantId);
            return MyJsonBean.successData(summary);
        } catch (Exception e) {
            log.error("查询资产汇总信息失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getAssetByCategory(Long tenantId) {
        try {
            List<Map<String, Object>> list = fixedAssetCardMapper.selectAssetByCategory(tenantId);
            return MyJsonBean.successData(list);
        } catch (Exception e) {
            log.error("按类别统计资产失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean checkAssetCodeExists(String assetCode, String assetId, Long tenantId) {
        try {
            LambdaQueryWrapper<FixedAssetCardEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FixedAssetCardEntity::getAssetCode, assetCode);
            wrapper.eq(FixedAssetCardEntity::getTenantId, tenantId);
            if (StringUtils.hasText(assetId)) {
                wrapper.ne(FixedAssetCardEntity::getAssetId, assetId);
            }
            long count = fixedAssetCardMapper.selectCount(wrapper);
            return MyJsonBean.successData(count > 0);
        } catch (Exception e) {
            log.error("检查资产编码是否存在失败", e);
            return MyJsonBean.errorData("检查失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean exportAssetCard(FixedAssetCardQueryParam param) {
        try {
            // TODO: 实现导出功能
            return MyJsonBean.successMsg("导出功能待实现");
        } catch (Exception e) {
            log.error("导出资产卡片失败", e);
            return MyJsonBean.errorData("导出失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean importAssetCard(List<FixedAssetCardSaveParam> dataList) {
        try {
            // TODO: 实现导入功能
            return MyJsonBean.successMsg("导入功能待实现");
        } catch (Exception e) {
            log.error("导入资产卡片失败", e);
            return MyJsonBean.errorData("导入失败: " + e.getMessage());
        }
    }
}

