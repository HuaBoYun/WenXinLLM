package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.dto.BillPledgeFinancingDTO;
import com.global.treasurer.entity.TblBillPool;
import com.global.treasurer.entity.TblPledgeFinancing;
import com.global.treasurer.mapper.BillPoolMapper;
import com.global.treasurer.mapper.PledgeFinancingMapper;
import com.global.treasurer.service.IPledgeFinancingService;
import com.global.treasurer.vo.PledgeFinancingRecordVO;
import com.hbfk.util.BizException;
import com.hbfk.util.SnowflakeIdWorker;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * 质押融资Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@Service
public class PledgeFinancingServiceImpl extends ServiceImpl<PledgeFinancingMapper, TblPledgeFinancing>
        implements IPledgeFinancingService {
    @Resource
    private BillPoolMapper billPoolMapper;

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public List<PledgeFinancingRecordVO> selectFinancingRecordsByPoolId(Long poolId) {
        if (poolId == null) {
            throw new BizException("票据池ID不能为空");
        }
        return baseMapper.selectFinancingRecordsByPoolId(poolId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblPledgeFinancing insertPledgeFinancing(BillPledgeFinancingDTO dto) {
        // 校验票据池是否存在
        if (dto.getPoolId() == null) {
            throw new BizException("票据池ID不能为空");
        }
        TblBillPool pool = billPoolMapper.selectById(dto.getPoolId());
        if (pool == null) {
            throw new BizException("票据池不存在: " + dto.getPoolId());
        }

        // 生成融资编号
        String financingNumber = "FN" + System.currentTimeMillis();

        TblPledgeFinancing financing = new TblPledgeFinancing();
        BeanUtils.copyProperties(dto, financing);

        // 设置质押金额默认值：如果前端没传，取票据池的总金额
        if (financing.getPledgeAmount() == null) {
            financing.setPledgeAmount(pool.getTotalAmount() != null ? pool.getTotalAmount() : BigDecimal.ZERO);
        }

        // 设置质押率默认值：如果前端没传，取票据池的质押率，再没有就默认70%
        if (financing.getPledgeRate() == null) {
            if (pool.getPledgeRate() != null && pool.getPledgeRate().compareTo(BigDecimal.ZERO) > 0) {
                // 票据池的pledgeRate是百分比(如70)，需要转为小数(0.70)
                financing.setPledgeRate(pool.getPledgeRate().divide(new BigDecimal(100), 4, RoundingMode.HALF_UP));
            } else {
                financing.setPledgeRate(new BigDecimal("0.7000"));
            }
        }

        // 如果融资金额为空，根据质押金额和质押率计算
        if (financing.getFinancingAmount() == null) {
            financing.setFinancingAmount(financing.getPledgeAmount()
                    .multiply(financing.getPledgeRate())
                    .setScale(2, RoundingMode.HALF_UP));
        }

        // 计算利息和还款金额
        if (financing.getFinancingAmount() != null && financing.getInterestRate() != null && financing.getFinancingPeriod() != null) {
            BigDecimal interestAmount = financing.getFinancingAmount()
                    .multiply(financing.getInterestRate())
                    .multiply(new BigDecimal(financing.getFinancingPeriod()))
                    .divide(new BigDecimal(36000), 2, RoundingMode.HALF_UP);
            financing.setInterestAmount(interestAmount);
            financing.setRepayAmount(financing.getFinancingAmount().add(interestAmount));
        }

        // 生成融资ID
        financing.setFinancingId(snowflakeIdWorker.nextId());
        financing.setFinancingNumber(financingNumber);

        // 设置默认值
        if (financing.getFinancingStatus() == null) {
            financing.setFinancingStatus("PENDING");
        }
        if (financing.getRepayStatus() == null) {
            financing.setRepayStatus("PENDING");
        }
        if (financing.getBillCount() == null) {
            financing.setBillCount(0);
        }
        if (financing.getDeleteFlag() == null) {
            financing.setDeleteFlag(0);
        }

        baseMapper.insert(financing);
        return financing;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblPledgeFinancing updatePledgeFinancing(BillPledgeFinancingDTO dto) {
        if (dto.getFinancingId() == null) {
            throw new BizException("融资ID不能为空");
        }

        // 校验融资记录是否存在
        TblPledgeFinancing existFinancing = baseMapper.selectById(dto.getFinancingId());
        if (existFinancing == null) {
            throw new BizException("融资记录不存在: " + dto.getFinancingId());
        }

        // 已执行的融资不能修改
        if ("EXECUTED".equals(existFinancing.getFinancingStatus())) {
            throw new BizException("已执行的融资不能修改");
        }

        TblPledgeFinancing financing = new TblPledgeFinancing();
        BeanUtils.copyProperties(dto, financing);
        baseMapper.updateById(financing);
        return financing;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePledgeFinancing(Long[] financingIds) {
        if (financingIds == null || financingIds.length == 0) {
            throw new BizException("请选择要删除的融资记录");
        }

        int result = baseMapper.deletePledgeFinancingByIds(financingIds);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean executePledgeFinancing(BillPledgeFinancingDTO dto) {
        if (dto.getFinancingId() == null) {
            throw new BizException("融资ID不能为空");
        }

        // 校验融资记录是否存在
        TblPledgeFinancing financing = baseMapper.selectById(dto.getFinancingId());
        if (financing == null) {
            throw new BizException("融资记录不存在: " + dto.getFinancingId());
        }

        // 只有待执行状态才能执行
        if (!"PENDING".equals(financing.getFinancingStatus())) {
            throw new BizException("只有待执行状态的融资才能执行");
        }

        // 更新融资状态为已执行
        financing.setFinancingStatus("EXECUTED");
        financing.setUpdateTime(new Date());
        baseMapper.updateById(financing);

        // 更新票据池融资金额
        TblBillPool pool = billPoolMapper.selectById(financing.getPoolId());
        if (pool != null) {
            BigDecimal newFinancingAmount = pool.getFinancingAmount().add(financing.getFinancingAmount());
            billPoolMapper.updatePoolStatistics(pool.getPoolId(), pool.getTotalAmount(), pool.getBillCount());
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processRepayment(Long financingId) {
        if (financingId == null) {
            throw new BizException("融资ID不能为空");
        }

        // 校验融资记录是否存在
        TblPledgeFinancing financing = baseMapper.selectById(financingId);
        if (financing == null) {
            throw new BizException("融资记录不存在: " + financingId);
        }

        // 更新还款信息
        baseMapper.updateRepaymentInfo(financingId, new Date(), "REPAID");

        // 更新票据池融资金额（减少已还融资）
        TblBillPool pool = billPoolMapper.selectById(financing.getPoolId());
        if (pool != null) {
            BigDecimal newFinancingAmount = pool.getFinancingAmount().subtract(financing.getFinancingAmount());
            billPoolMapper.updatePoolStatistics(pool.getPoolId(), pool.getTotalAmount(), pool.getBillCount());
        }

        return true;
    }
}

