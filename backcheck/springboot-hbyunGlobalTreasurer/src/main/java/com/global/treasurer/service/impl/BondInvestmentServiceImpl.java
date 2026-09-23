package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BondInvestmentDTO;
import com.global.treasurer.dto.BondInvestmentQueryDTO;
import com.global.treasurer.entity.TblBondInvestment;
import com.global.treasurer.mapper.BondInvestmentMapper;
import com.global.treasurer.service.BondInvestmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 债券投资服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Service
public class BondInvestmentServiceImpl implements BondInvestmentService {
    private static final Logger log = LoggerFactory.getLogger(BondInvestmentServiceImpl.class);

    @Autowired
    private BondInvestmentMapper bondInvestmentMapper;

    @Override
    public PageInfo<TblBondInvestment> getInvestmentList(BondInvestmentQueryDTO queryDTO) {
        try {
            int pageNum = queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1;
            int pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;

            // 使用PageHelper分页
            PageHelper.startPage(pageNum, pageSize);

            // 调用Mapper中定义的XML查询方法
            List<TblBondInvestment> list = bondInvestmentMapper.selectByQueryDTO(queryDTO);

            return new PageInfo<>(list);
        } catch (Exception e) {
            log.error("查询债券投资列表失败", e);
            throw new RuntimeException("查询失败: " + e.getMessage(), e);
        }
    }

    @Override
    public TblBondInvestment getInvestmentById(Long investmentId) {
        return bondInvestmentMapper.selectById(investmentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblBondInvestment saveInvestment(BondInvestmentDTO dto) {
        TblBondInvestment investment = new TblBondInvestment();
        BeanUtils.copyProperties(dto, investment);

        // 自动计算投资金额：购买价格 × 购买数量
        if (investment.getPurchasePrice() != null && investment.getPurchaseQuantity() != null) {
            BigDecimal investmentAmount = investment.getPurchasePrice()
                    .multiply(new BigDecimal(investment.getPurchaseQuantity()));
            investment.setInvestmentAmount(investmentAmount);
        }

        // 自动设置风险等级（如果未提供）
        if (investment.getRiskLevel() == null || investment.getRiskLevel().isEmpty()) {
            investment.setRiskLevel(calculateRiskLevel(investment));
        }

        if (dto.getInvestmentId() != null) {
            // 更新
            investment.setInvestmentId(dto.getInvestmentId());
            investment.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
            bondInvestmentMapper.updateById(investment);
        } else {
            // 新增
            investment.setInvestmentStatus("ACTIVE");
            investment.setDeleteFlag(0);
            investment.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            investment.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
            bondInvestmentMapper.insert(investment);
        }

        return investment;
    }

    /**
     * 根据债券信息计算风险等级
     */
    private String calculateRiskLevel(TblBondInvestment investment) {
        String creditRating = investment.getCreditRating();
        String bondType = investment.getBondType();

        // 根据信用评级判断风险等级
        if (creditRating != null && !creditRating.isEmpty()) {
            if (creditRating.startsWith("AAA") || creditRating.startsWith("AA")) {
                return "LOW";
            } else if (creditRating.startsWith("A") || creditRating.startsWith("BBB")) {
                return "MEDIUM";
            } else {
                return "HIGH";
            }
        }

        // 根据债券类型判断风险等级
        if ("GOVERNMENT".equals(bondType)) {
            return "LOW";  // 政府债券风险低
        } else if ("FINANCIAL".equals(bondType)) {
            return "MEDIUM";  // 金融债券风险中等
        } else if ("CORPORATE".equals(bondType)) {
            return "MEDIUM";  // 企业债券风险中等
        } else if ("CONVERTIBLE".equals(bondType)) {
            return "HIGH";  // 可转换债券风险较高
        }

        // 默认中等风险
        return "MEDIUM";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInvestment(Long investmentId) {
        // 逻辑删除
        TblBondInvestment investment = new TblBondInvestment();
        investment.setInvestmentId(investmentId);
        investment.setDeleteFlag(1);
        investment.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
        bondInvestmentMapper.updateById(investment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteInvestments(List<Long> investmentIds) {
        for (Long id : investmentIds) {
            deleteInvestment(id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sellBond(Long investmentId, BigDecimal sellPrice, Integer sellQuantity) {
        TblBondInvestment investment = getInvestmentById(investmentId);
        if (investment != null && "ACTIVE".equals(investment.getInvestmentStatus())) {
            investment.setInvestmentStatus("SOLD");
            investment.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
            bondInvestmentMapper.updateById(investment);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMarketValue(Long investmentId, BigDecimal marketValue) {
        TblBondInvestment investment = getInvestmentById(investmentId);
        if (investment != null) {
            BigDecimal unrealizedPnl = marketValue.subtract(investment.getInvestmentAmount());
            investment.setCurrentMarketValue(marketValue);
            investment.setUnrealizedPnl(unrealizedPnl);
            investment.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
            bondInvestmentMapper.updateById(investment);
        }
    }

    @Override
    public Map<String, Object> getInvestmentStatistics() {
        Map<String, Object> stats = new HashMap<>();
        List<TblBondInvestment> all = bondInvestmentMapper.selectList(new QueryWrapper<TblBondInvestment>().eq("DELETE_FLAG", 0));
        stats.put("total", all.size());
        return stats;
    }

    @Override
    public List<TblBondInvestment> getMaturitySoon(Integer days) {
        // 简化实现，返回空列表
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getInvestmentAnalysisByType() {
        // 简化实现，返回空列表
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getInvestmentAnalysisByRating() {
        // 简化实现，返回空列表
        return new ArrayList<>();
    }

    @Override
    public List<TblBondInvestment> exportInvestments(BondInvestmentQueryDTO queryDTO) {
        QueryWrapper<TblBondInvestment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETE_FLAG", 0);
        return bondInvestmentMapper.selectList(queryWrapper);
    }
}
