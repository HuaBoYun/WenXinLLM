package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BankWealthInvestmentDTO;
import com.global.treasurer.dto.BankWealthInvestmentQueryDTO;
import com.global.treasurer.entity.TblBankWealthInvestment;
import com.global.treasurer.mapper.BankWealthInvestmentMapper;
import com.global.treasurer.service.BankWealthInvestmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 银行理财投资Service实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BankWealthInvestmentServiceImpl implements BankWealthInvestmentService {
    @Resource
    private BankWealthInvestmentMapper bankWealthInvestmentMapper;

    @Override
    public PageInfo<TblBankWealthInvestment> getBankWealthInvestmentList(BankWealthInvestmentQueryDTO queryDTO) {
        // 设置分页参数
        int pageNum = queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1;
        int pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;
        PageHelper.startPage(pageNum, pageSize);

        // 查询列表
        List<TblBankWealthInvestment> list = bankWealthInvestmentMapper.selectByQueryDTO(queryDTO);
        return new PageInfo<>(list);
    }

    @Override
    public TblBankWealthInvestment getBankWealthInvestmentById(Long investmentId) {
        return bankWealthInvestmentMapper.selectById(investmentId);
    }

    @Override
    public TblBankWealthInvestment saveBankWealthInvestment(BankWealthInvestmentDTO dto) {
        TblBankWealthInvestment investment = new TblBankWealthInvestment();

        // 设置基本信息
        investment.setPlanId(dto.getPlanId());
        investment.setProductId(dto.getProductId());
        investment.setInvestmentNo(dto.getInvestmentNo());
        investment.setProductCode(dto.getProductCode());
        investment.setBankCode(dto.getBankCode());

        // 设置产品名称(如果前端没传,使用产品代码作为默认值)
        if (dto.getProductName() != null && !dto.getProductName().isEmpty()) {
            investment.setProductName(dto.getProductName());
        } else {
            // 使用产品代码作为产品名称的默认值
            investment.setProductName(dto.getProductCode() != null ? dto.getProductCode() : "未命名产品");
        }

        // 设置银行名称(如果前端没传,使用银行代码作为默认值)
        if (dto.getBankName() != null && !dto.getBankName().isEmpty()) {
            investment.setBankCode(dto.getBankName());
        } else {
            investment.setBankCode(dto.getBankCode() != null ? dto.getBankCode() : "未知银行");
        }

        // 设置投资期限(根据投资日期和到期日期计算)
        if (dto.getInvestmentDate() != null && dto.getMaturityDate() != null) {
            long days = (dto.getMaturityDate().getTime() - dto.getInvestmentDate().getTime()) / (1000 * 60 * 60 * 24);
            investment.setInvestmentTerm((int) days);
        } else {
            investment.setInvestmentTerm(0);
        }

        // 设置风险等级(默认值)
        if (dto.getRiskLevel() != null && !dto.getRiskLevel().isEmpty()) {
            investment.setRiskLevel(dto.getRiskLevel());
        } else {
            investment.setRiskLevel("R1"); // 默认低风险
        }

        // 设置金额信息
        investment.setInvestmentAmount(dto.getInvestmentAmount());
        investment.setExpectedReturnRate(dto.getExpectedReturnRate());
        investment.setActualReturnRate(dto.getActualReturnRate());
        investment.setExpectedReturn(dto.getExpectedReturn());
        investment.setActualReturn(dto.getActualReturn());

        // 设置日期信息
        investment.setInvestmentDate(dto.getInvestmentDate());
        investment.setMaturityDate(dto.getMaturityDate());
        investment.setRedeemDate(dto.getRedeemDate());
        investment.setRedeemAmount(dto.getRedeemAmount());

        // 设置状态
        investment.setInvestmentStatus(dto.getInvestmentStatus());

        // 设置时间戳
        investment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        investment.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        if (dto.getBankWealthInvestmentId() != null) {
            // 更新
            investment.setBankWealthInvestmentId(dto.getBankWealthInvestmentId());
            bankWealthInvestmentMapper.updateById(investment);
        } else {
            // 新增
            bankWealthInvestmentMapper.insert(investment);
        }

        return investment;
    }

    @Override
    public void deleteBankWealthInvestment(Long investmentId) {
        bankWealthInvestmentMapper.deleteById(investmentId);
    }

    @Override
    public void batchDeleteBankWealthInvestments(List<Long> investmentIds) {
        for (Long id : investmentIds) {
            bankWealthInvestmentMapper.deleteById(id);
        }
    }

    @Override
    public void subscribeProduct(BankWealthInvestmentDTO dto) {
        TblBankWealthInvestment investment = new TblBankWealthInvestment();
        investment.setPlanId(dto.getPlanId());
        investment.setProductId(dto.getProductId());

        // 生成投资编号(如果前端未提供)
        if (dto.getInvestmentNo() != null && !dto.getInvestmentNo().trim().isEmpty()) {
            investment.setInvestmentNo(dto.getInvestmentNo());
        } else {
            // 自动生成投资编号: INV + 时间戳
            investment.setInvestmentNo("INV" + System.currentTimeMillis());
        }

        investment.setProductCode(dto.getProductCode());
        investment.setBankCode(dto.getBankCode());

        // 设置产品名称(使用产品代码作为默认值)
        investment.setProductName(dto.getProductCode() != null ? dto.getProductCode() : "未命名产品");

        // 设置银行名称(使用银行代码作为默认值)
        investment.setBankCode(dto.getBankCode() != null ? dto.getBankCode() : "未知银行");

        investment.setInvestmentAmount(dto.getInvestmentAmount());

        // 设置投资日期(如果前端未提供,使用当前日期)
        if (dto.getInvestmentDate() != null) {
            investment.setInvestmentDate(new java.sql.Date(dto.getInvestmentDate().getTime()));
        } else {
            investment.setInvestmentDate(new java.sql.Date(System.currentTimeMillis()));
        }

        // 设置到期日期(如果前端未提供,默认1年后)
        if (dto.getMaturityDate() != null) {
            investment.setMaturityDate(new java.sql.Date(dto.getMaturityDate().getTime()));
        } else {
            // 默认1年后到期
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 1);
            investment.setMaturityDate(new java.sql.Date(calendar.getTimeInMillis()));
        }

        // 计算投资期限(天数)
        long days = (investment.getMaturityDate().getTime() - investment.getInvestmentDate().getTime()) / (1000 * 60 * 60 * 24);
        investment.setInvestmentTerm((int) days);

        // 设置起息日(默认为投资日期)
        investment.setInvestmentDate(investment.getInvestmentDate());

        // 设置风险等级(默认R1)
        investment.setRiskLevel("R1");

        investment.setInvestmentStatus("ACTIVE");
        investment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        investment.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        bankWealthInvestmentMapper.insert(investment);
    }

    @Override
    public void redeemProduct(Long investmentId, BigDecimal redeemAmount, String redeemDate) {
        TblBankWealthInvestment investment = bankWealthInvestmentMapper.selectById(investmentId);
        if (investment != null) {
            investment.setRedeemAmount(redeemAmount);
            if (redeemDate != null && !redeemDate.trim().isEmpty()) {
                investment.setRedeemDate(parseSqlDate(redeemDate));
            }
            investment.setInvestmentStatus("REDEEMED");
            investment.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            bankWealthInvestmentMapper.updateById(investment);
        }
    }

    @Override
    public void updateValuation(Long investmentId, BigDecimal newValue) {
        TblBankWealthInvestment investment = bankWealthInvestmentMapper.selectById(investmentId);
        if (investment != null) {
            investment.setActualReturn(newValue);
            investment.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            bankWealthInvestmentMapper.updateById(investment);
        }
    }

    @Override
    public void updateActualReturn(Long investmentId, BigDecimal actualReturn) {
        TblBankWealthInvestment investment = bankWealthInvestmentMapper.selectById(investmentId);
        if (investment != null) {
            investment.setActualReturn(actualReturn);
            investment.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            bankWealthInvestmentMapper.updateById(investment);
        }
    }

    @Override
    public Map<String, Object> getBankWealthStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 获取所有投资记录
        List<TblBankWealthInvestment> allInvestments = bankWealthInvestmentMapper.selectByQueryDTO(new BankWealthInvestmentQueryDTO());

        // 计算统计数据
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalCurrentValue = BigDecimal.ZERO;
        BigDecimal totalExpectedReturn = BigDecimal.ZERO;
        BigDecimal totalActualReturn = BigDecimal.ZERO;
        int activeCount = 0;
        int maturedCount = 0;
        int redeemedCount = 0;

        for (TblBankWealthInvestment investment : allInvestments) {
            if (investment.getInvestmentAmount() != null) {
                totalAmount = totalAmount.add(investment.getInvestmentAmount());
            }
            if (investment.getCurrentValue() != null) {
                totalCurrentValue = totalCurrentValue.add(investment.getCurrentValue());
            }
            if (investment.getExpectedReturn() != null) {
                totalExpectedReturn = totalExpectedReturn.add(investment.getExpectedReturn());
            }
            if (investment.getActualReturn() != null) {
                totalActualReturn = totalActualReturn.add(investment.getActualReturn());
            }

            String status = investment.getInvestmentStatus();
            if ("ACTIVE".equals(status)) {
                activeCount++;
            } else if ("MATURED".equals(status)) {
                maturedCount++;
            } else if ("REDEEMED".equals(status)) {
                redeemedCount++;
            }
        }

        statistics.put("totalAmount", totalAmount);
        statistics.put("totalCurrentValue", totalCurrentValue);
        statistics.put("totalExpectedReturn", totalExpectedReturn);
        statistics.put("totalActualReturn", totalActualReturn);
        statistics.put("activeCount", activeCount);
        statistics.put("maturedCount", maturedCount);
        statistics.put("redeemedCount", redeemedCount);
        statistics.put("totalCount", allInvestments.size());

        return statistics;
    }

    /**
     * 将字符串日期转换为java.util.Date对象
     */
    private java.util.Date parseUtilDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        } catch (Exception e) {
            throw new RuntimeException("日期格式错误: " + dateStr + ", 期望格式: yyyy-MM-dd");
        }
    }

    /**
     * 将字符串日期转换为java.sql.Date对象
     */
    private java.sql.Date parseSqlDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(dateStr);
            return new java.sql.Date(utilDate.getTime());
        } catch (Exception e) {
            throw new RuntimeException("日期格式错误: " + dateStr + ", 期望格式: yyyy-MM-dd");
        }
    }
}
