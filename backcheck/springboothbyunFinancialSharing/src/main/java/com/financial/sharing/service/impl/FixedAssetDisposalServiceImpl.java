package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.mysql.entity.FixedAssetCardEntity;
import com.financial.sharing.mysql.entity.FixedAssetDisposalEntity;
import com.financial.sharing.mysql.mapper.FixedAssetCardMapper;
import com.financial.sharing.mysql.mapper.FixedAssetDisposalMapper;
import com.financial.sharing.service.FixedAssetDisposalService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.FixedAssetDisposalQueryParam;
import com.financial.sharing.vo.param.FixedAssetDisposalSaveParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 固定资产处置服务实现类
 * @author system
 * @since 2026-01-23
 */
@Slf4j
@Service
public class FixedAssetDisposalServiceImpl implements FixedAssetDisposalService {

    @Resource
    private FixedAssetDisposalMapper disposalMapper;

    @Resource
    private FixedAssetCardMapper assetCardMapper;

    @Override
    public MyJsonBean<PageResult> getDisposalList(FixedAssetDisposalQueryParam param) {
        try {
            Page<FixedAssetDisposalEntity> page = new Page<>(param.getPageNumber(), param.getPageSize());
            
            // 使用Mapper中的自定义查询方法
            List<FixedAssetDisposalEntity> list = disposalMapper.selectPageList(
                param.getDisposalNo(),
                param.getAssetCode(),
                param.getDisposalType(),
                param.getStatus(),
                param.getTenantId()
            );
            
            // 手动分页
            int start = (param.getPageNumber() - 1) * param.getPageSize();
            int end = Math.min(start + param.getPageSize(), list.size());
            List<FixedAssetDisposalEntity> pageList = list.subList(start, end);
            
            PageResult pageResult = new PageResult();
            pageResult.setRecords(pageList);
            pageResult.setTotal(list.size());
            pageResult.setCurrent(param.getPageNumber());
            pageResult.setSize(param.getPageSize());
            
            return MyJsonBean.successData(pageResult);
        } catch (Exception e) {
            log.error("查询资产处置列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getDisposalById(String disposalId, Long tenantId) {
        try {
            FixedAssetDisposalEntity disposal = disposalMapper.selectDetailById(disposalId, tenantId);
            if (disposal == null) {
                return MyJsonBean.errorData("处置记录不存在");
            }
            return MyJsonBean.successData(disposal);
        } catch (Exception e) {
            log.error("查询资产处置详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdateDisposal(FixedAssetDisposalSaveParam param) {
        try {
            // 查询资产信息
            FixedAssetCardEntity asset = assetCardMapper.selectById(param.getAssetId());
            if (asset == null) {
                return MyJsonBean.errorData("资产不存在");
            }
            
            // 检查资产状态
            if ("DISPOSED".equals(asset.getStatus())) {
                return MyJsonBean.errorData("资产已处置，不能重复处置");
            }
            
            FixedAssetDisposalEntity entity = new FixedAssetDisposalEntity();
            BeanUtils.copyProperties(param, entity);
            
            // 设置资产原值和净值
            entity.setOriginalValue(asset.getOriginalValue());
            entity.setNetValue(asset.getNetValue());
            
            // 计算处置损益
            BigDecimal disposalIncome = param.getDisposalIncome() != null ? param.getDisposalIncome() : BigDecimal.ZERO;
            BigDecimal disposalProfitLoss = disposalIncome.subtract(asset.getNetValue());
            entity.setDisposalIncome(disposalIncome);
            entity.setDisposalProfitLoss(disposalProfitLoss);
            
            if (StringUtils.hasText(param.getDisposalId())) {
                // 更新
                entity.setUpdateTime(LocalDateTime.now());
                entity.setUpdateBy(param.getOperatorId());
                disposalMapper.updateById(entity);
                return MyJsonBean.successData(param.getDisposalId());
            } else {
                // 新增
                entity.setDisposalId(UUID.randomUUID().toString().replace("-", ""));
                entity.setDisposalNo(generateDisposalNo(param.getTenantId()));
                entity.setStatus("PENDING");
                entity.setCreateTime(LocalDateTime.now());
                entity.setCreateBy(param.getOperatorId());
                entity.setIsDeleted(0);
                disposalMapper.insert(entity);
                return MyJsonBean.successData(entity.getDisposalId());
            }
        } catch (Exception e) {
            log.error("保存资产处置失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteDisposal(String disposalId, Long tenantId) {
        try {
            FixedAssetDisposalEntity disposal = disposalMapper.selectDetailById(disposalId, tenantId);
            if (disposal == null) {
                return MyJsonBean.errorData("处置记录不存在");
            }

            if (!"PENDING".equals(disposal.getStatus())) {
                return MyJsonBean.errorData("只能删除待审批状态的处置记录");
            }

            disposalMapper.deleteById(disposalId);
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除资产处置失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchDeleteDisposal(List<String> disposalIds, Long tenantId) {
        try {
            int successCount = 0;
            for (String disposalId : disposalIds) {
                MyJsonBean result = deleteDisposal(disposalId, tenantId);
                if (result.getCode() == 1) {
                    successCount++;
                }
            }

            Map<String, Object> data = new HashMap<>();
            data.put("successCount", successCount);
            data.put("totalCount", disposalIds.size());
            return MyJsonBean.successData(data);
        } catch (Exception e) {
            log.error("批量删除资产处置失败", e);
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean approveDisposal(String disposalId, Boolean approved, String comment, String operatorId, Long tenantId) {
        try {
            FixedAssetDisposalEntity disposal = disposalMapper.selectDetailById(disposalId, tenantId);
            if (disposal == null) {
                return MyJsonBean.errorData("处置记录不存在");
            }

            if (!"PENDING".equals(disposal.getStatus())) {
                return MyJsonBean.errorData("该处置记录已审批");
            }

            String newStatus = approved ? "APPROVED" : "REJECTED";
            disposalMapper.updateStatus(disposalId, newStatus, operatorId);

            return MyJsonBean.successData("审批成功");
        } catch (Exception e) {
            log.error("审批资产处置失败", e);
            return MyJsonBean.errorData("审批失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchApproveDisposal(List<String> disposalIds, Boolean approved, String comment, String operatorId, Long tenantId) {
        try {
            int successCount = 0;
            for (String disposalId : disposalIds) {
                MyJsonBean result = approveDisposal(disposalId, approved, comment, operatorId, tenantId);
                if (result.getCode() == 1) {
                    successCount++;
                }
            }

            Map<String, Object> data = new HashMap<>();
            data.put("successCount", successCount);
            data.put("totalCount", disposalIds.size());
            return MyJsonBean.successData(data);
        } catch (Exception e) {
            log.error("批量审批资产处置失败", e);
            return MyJsonBean.errorData("批量审批失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean executeDisposal(String disposalId, String operatorId, Long tenantId) {
        try {
            FixedAssetDisposalEntity disposal = disposalMapper.selectDetailById(disposalId, tenantId);
            if (disposal == null) {
                return MyJsonBean.errorData("处置记录不存在");
            }

            if (!"APPROVED".equals(disposal.getStatus())) {
                return MyJsonBean.errorData("只能执行已审批的处置记录");
            }

            // 更新处置状态为已完成
            disposalMapper.updateStatus(disposalId, "COMPLETED", operatorId);

            // 更新资产状态为已处置
            FixedAssetCardEntity asset = assetCardMapper.selectById(disposal.getAssetId());
            if (asset != null) {
                asset.setStatus("DISPOSED");
                asset.setUpdateTime(LocalDateTime.now());
                asset.setUpdateBy(operatorId);
                assetCardMapper.updateById(asset);
            }

            return MyJsonBean.successData("执行成功");
        } catch (Exception e) {
            log.error("执行资产处置失败", e);
            return MyJsonBean.errorData("执行失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getDisposalStats(Long tenantId, String startDate, String endDate) {
        try {
            LocalDate start = StringUtils.hasText(startDate) ? LocalDate.parse(startDate) : LocalDate.now().withDayOfMonth(1);
            LocalDate end = StringUtils.hasText(endDate) ? LocalDate.parse(endDate) : LocalDate.now();

            Map<String, Object> stats = disposalMapper.selectDisposalSummary(tenantId, start, end);

            // 查询各类型统计
            LambdaQueryWrapper<FixedAssetDisposalEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FixedAssetDisposalEntity::getTenantId, tenantId);
            wrapper.ge(FixedAssetDisposalEntity::getDisposalDate, start);
            wrapper.le(FixedAssetDisposalEntity::getDisposalDate, end);
            wrapper.eq(FixedAssetDisposalEntity::getIsDeleted, 0);

            List<FixedAssetDisposalEntity> allDisposals = disposalMapper.selectList(wrapper);

            // 统计各类型数量
            long scrapCount = allDisposals.stream().filter(d -> "SCRAP".equals(d.getDisposalType())).count();
            BigDecimal saleAmount = allDisposals.stream()
                .filter(d -> "SALE".equals(d.getDisposalType()))
                .map(FixedAssetDisposalEntity::getDisposalIncome)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal disposalLoss = allDisposals.stream()
                .map(FixedAssetDisposalEntity::getDisposalProfitLoss)
                .filter(p -> p.compareTo(BigDecimal.ZERO) < 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add).abs();
            long pendingCount = allDisposals.stream().filter(d -> "PENDING".equals(d.getStatus())).count();

            Map<String, Object> result = new HashMap<>();
            result.put("scrapCount", scrapCount);
            result.put("saleAmount", saleAmount);
            result.put("disposalLoss", disposalLoss);
            result.put("pendingCount", pendingCount);
            result.putAll(stats);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取处置统计数据失败", e);
            return MyJsonBean.errorData("获取统计数据失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean exportDisposalList(FixedAssetDisposalQueryParam param) {
        // TODO: 实现导出功能
        return MyJsonBean.successData("导出功能开发中");
    }

    @Override
    public MyJsonBean generateDisposalReport(Long tenantId, String startDate, String endDate) {
        // TODO: 实现报告生成功能
        return MyJsonBean.successData("报告生成功能开发中");
    }

    /**
     * 生成处置单号
     */
    private String generateDisposalNo(Long tenantId) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "AD" + date;

        // 查询当天最大序号
        LambdaQueryWrapper<FixedAssetDisposalEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FixedAssetDisposalEntity::getTenantId, tenantId);
        wrapper.likeRight(FixedAssetDisposalEntity::getDisposalNo, prefix);
        wrapper.orderByDesc(FixedAssetDisposalEntity::getDisposalNo);
        wrapper.last("LIMIT 1");

        FixedAssetDisposalEntity last = disposalMapper.selectOne(wrapper);
        int sequence = 1;
        if (last != null && last.getDisposalNo() != null) {
            String lastNo = last.getDisposalNo();
            if (lastNo.length() > prefix.length()) {
                try {
                    sequence = Integer.parseInt(lastNo.substring(prefix.length())) + 1;
                } catch (NumberFormatException e) {
                    sequence = 1;
                }
            }
        }

        return prefix + String.format("%04d", sequence);
    }
}
