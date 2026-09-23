package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BillPoolDTO;
import com.global.treasurer.dto.BillPoolQueryDTO;
import com.global.treasurer.entity.TblBillPool;
import com.global.treasurer.mapper.BillPoolDetailMapper;
import com.global.treasurer.mapper.BillPoolMapper;
import com.global.treasurer.mapper.PledgeFinancingMapper;
import com.global.treasurer.service.IBillPoolService;
import com.global.treasurer.vo.BillPoolVO;
import com.global.treasurer.vo.BillInPoolVO;
import com.global.treasurer.vo.PledgeFinancingRecordVO;
import com.hbfk.util.BizException;
import com.hbfk.util.SnowflakeIdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 票据池Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@Service
public class BillPoolServiceImpl extends ServiceImpl<BillPoolMapper, TblBillPool>
        implements IBillPoolService {
    private static final Logger log = LoggerFactory.getLogger(BillPoolServiceImpl.class);

    @Resource
    private BillPoolDetailMapper billPoolDetailMapper;

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Resource
    private PledgeFinancingMapper pledgeFinancingMapper;

    @Override
    public PageInfo<BillPoolVO> selectBillPoolList(BillPoolQueryDTO queryDTO) {
        // 使用默认值处理分页参数
        Integer pageNum = queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1;
        Integer pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;
        PageHelper.startPage(pageNum, pageSize);
        List<BillPoolVO> list = baseMapper.selectBillPoolList(queryDTO);
        return new PageInfo<>(list);
    }

    @Override
    public BillPoolVO selectBillPoolById(Long poolId) {
        if (poolId == null) {
            throw new BizException("票据池ID不能为空");
        }
        return baseMapper.selectBillPoolById(poolId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblBillPool insertBillPool(BillPoolDTO dto) {
        // 校验票据池编码是否已存在
        if (dto.getPoolCode() != null && !dto.getPoolCode().isEmpty()) {
            TblBillPool existPool = baseMapper.selectByPoolCode(dto.getPoolCode());
            if (existPool != null) {
                throw new BizException("票据池编码已存在: " + dto.getPoolCode());
            }
        }

        TblBillPool pool = new TblBillPool();
        BeanUtils.copyProperties(dto, pool);

        // 生成票据池ID
        pool.setPoolId(snowflakeIdWorker.nextId());

        // 如果没有传入编码，自动生成
        if (pool.getPoolCode() == null || pool.getPoolCode().isEmpty()) {
            pool.setPoolCode("POOL" + System.currentTimeMillis());
        }

        // 设置默认值
        if (pool.getTotalAmount() == null) {
            pool.setTotalAmount(BigDecimal.ZERO);
        }
        if (pool.getBillCount() == null) {
            pool.setBillCount(0);
        }
        if (pool.getFinancingAmount() == null) {
            pool.setFinancingAmount(BigDecimal.ZERO);
        }
        if (pool.getPledgeRate() == null) {
            pool.setPledgeRate(BigDecimal.ZERO);
        }
        if (pool.getPoolStatus() == null) {
            pool.setPoolStatus("NORMAL");
        }
        if (pool.getDeleteFlag() == null) {
            pool.setDeleteFlag(0);
        }

        // 设置创建时间
        pool.setCreateTime(new Date());

        // TODO: 从当前登录用户获取创建人信息
        // pool.setCreateUser(currentUser.getUsername());
        // pool.setOwnerId(currentUser.getUserId());
        // pool.setOwnerName(currentUser.getRealName());

        baseMapper.insert(pool);
        return pool;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblBillPool updateBillPool(BillPoolDTO dto) {
        if (dto.getPoolId() == null) {
            throw new BizException("票据池ID不能为空");
        }

        // 校验票据池是否存在
        TblBillPool existPool = baseMapper.selectById(dto.getPoolId());
        if (existPool == null) {
            throw new BizException("票据池不存在: " + dto.getPoolId());
        }

        // 如果修改了编码，检查新编码是否已存在
        if (!existPool.getPoolCode().equals(dto.getPoolCode())) {
            TblBillPool codePool = baseMapper.selectByPoolCode(dto.getPoolCode());
            if (codePool != null) {
                throw new BizException("票据池编码已存在: " + dto.getPoolCode());
            }
        }

        TblBillPool pool = new TblBillPool();
        BeanUtils.copyProperties(dto, pool);
        baseMapper.updateById(pool);
        return pool;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBillPool(Long[] poolIds) {
        if (poolIds == null || poolIds.length == 0) {
            throw new BizException("请选择要删除的票据池");
        }

        // 检查池内是否有票据
        for (Long poolId : poolIds) {
            List<BillInPoolVO> bills = billPoolDetailMapper.selectBillsByPoolId(poolId);
            if (bills != null && !bills.isEmpty()) {
                throw new BizException("票据池ID " + poolId + " 内还有票据，无法删除");
            }
        }

        int result = baseMapper.deleteBillPoolByIds(poolIds);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addBillsToPool(Long poolId, Long[] billIds) {
        if (poolId == null) {
            throw new BizException("票据池ID不能为空");
        }
        if (billIds == null || billIds.length == 0) {
            throw new BizException("请选择要添加的票据");
        }

        // 校验票据池是否存在
        TblBillPool pool = baseMapper.selectById(poolId);
        if (pool == null) {
            throw new BizException("票据池不存在: " + poolId);
        }

        // 查询票据登记表获取票据信息
        // TODO: 从BillRegistrationMapper查询票据信息

        // 构建明细列表并批量插入
        List<com.global.treasurer.entity.TblBillPoolDetail> details = new ArrayList<>();
        for (Long billId : billIds) {
            com.global.treasurer.entity.TblBillPoolDetail detail = new com.global.treasurer.entity.TblBillPoolDetail();
            detail.setDetailId(snowflakeIdWorker.nextId());
            detail.setPoolId(poolId);
            detail.setBillId(billId);
            detail.setBillNumber(""); // TODO: 从票据表获取
            detail.setJoinDate(new Date());
            detail.setDetailStatus("ACTIVE");
            details.add(detail);
        }

        int result = billPoolDetailMapper.batchInsertBillPoolDetails(details);

        // 更新票据池统计信息
        if (result > 0) {
            baseMapper.updatePoolStatistics(poolId,
                    pool.getTotalAmount().add(new BigDecimal(result * 10000)),
                    pool.getBillCount() + result);
        }

        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeBillsFromPool(Long poolId, Long[] billIds) {
        if (poolId == null) {
            throw new BizException("票据池ID不能为空");
        }
        if (billIds == null || billIds.length == 0) {
            throw new BizException("请选择要移除的票据");
        }

        // 获取明细ID列表
        List<BillInPoolVO> bills = billPoolDetailMapper.selectBillsByPoolId(poolId);
        List<Long> detailIds = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (BillInPoolVO bill : bills) {
            if (Arrays.asList(billIds).contains(bill.getBillId())) {
                detailIds.add(bill.getDetailId());
                if (bill.getBillAmount() != null) {
                    totalAmount = totalAmount.add(bill.getBillAmount());
                }
            }
        }

        Long[] detailIdArray = detailIds.toArray(new Long[0]);
        int result = billPoolDetailMapper.batchRemoveBills(detailIdArray);

        // 更新票据池统计信息
        if (result > 0) {
            TblBillPool pool = baseMapper.selectById(poolId);
            baseMapper.updatePoolStatistics(poolId,
                    pool.getTotalAmount().subtract(totalAmount),
                    pool.getBillCount() - result);
        }

        return result > 0;
    }

    @Override
    public List<Map<String, Object>> getBillsInPool(Long poolId) {
        if (poolId == null) {
            throw new BizException("票据池ID不能为空");
        }

        List<BillInPoolVO> bills = billPoolDetailMapper.selectBillsByPoolId(poolId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (BillInPoolVO bill : bills) {
            Map<String, Object> map = new HashMap<>();
            map.put("detailId", bill.getDetailId());
            map.put("poolId", bill.getPoolId());
            map.put("poolName", bill.getPoolName());
            map.put("billId", bill.getBillId());
            map.put("billNumber", bill.getBillNumber());
            map.put("billType", bill.getBillType());
            map.put("billAmount", bill.getBillAmount());
            map.put("joinDate", bill.getJoinDate());
            map.put("detailStatus", bill.getDetailStatus());
            result.add(map);
        }

        return result;
    }

    @Override
    public Map<String, Object> getBillPoolStatistics(BillPoolQueryDTO queryDTO) {
        Map<String, Object> statistics = new HashMap<>();

        try {
            // 查询所有票据池
            List<TblBillPool> allPools = baseMapper.selectList(null);

            // 1. 票据池数量
            int totalPools = allPools.size();
            statistics.put("totalPools", totalPools);

            // 2. 池内票据总数
            int totalBillsInPool = allPools.stream()
                    .mapToInt(p -> p.getBillCount() != null ? p.getBillCount() : 0)
                    .sum();
            statistics.put("totalBillsInPool", totalBillsInPool);

            // 3. 池内总金额（转换为万元）
            BigDecimal totalAmount = allPools.stream()
                    .map(p -> p.getTotalAmount() != null ? p.getTotalAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // 转换为万元，保留2位小数
            BigDecimal totalPoolAmount = totalAmount.divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_HALF_UP);
            statistics.put("totalPoolAmount", totalPoolAmount);

            // 4. 融资余额（转换为万元）
            BigDecimal financingTotal = allPools.stream()
                    .map(p -> p.getFinancingAmount() != null ? p.getFinancingAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal financingBalance = financingTotal.divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_HALF_UP);
            statistics.put("financingBalance", financingBalance);

            // 5. 按票据池类型统计（用于饼图）
            List<Map<String, Object>> poolTypeStats = new ArrayList<>();
            Map<String, Integer> typeCountMap = new HashMap<>();
            Map<String, BigDecimal> typeAmountMap = new HashMap<>();

            for (TblBillPool pool : allPools) {
                String poolType = pool.getPoolType() != null ? pool.getPoolType() : "OTHER";
                typeCountMap.put(poolType, typeCountMap.getOrDefault(poolType, 0) +
                        (pool.getBillCount() != null ? pool.getBillCount() : 0));
                typeAmountMap.put(poolType, typeAmountMap.getOrDefault(poolType, BigDecimal.ZERO)
                        .add(pool.getTotalAmount() != null ? pool.getTotalAmount() : BigDecimal.ZERO));
            }

            for (Map.Entry<String, Integer> entry : typeCountMap.entrySet()) {
                Map<String, Object> typeStat = new HashMap<>();
                typeStat.put("type", entry.getKey());
                typeStat.put("typeName", getPoolTypeName(entry.getKey()));
                typeStat.put("count", entry.getValue());
                typeStat.put("amount", typeAmountMap.getOrDefault(entry.getKey(), BigDecimal.ZERO));
                poolTypeStats.add(typeStat);
            }
            statistics.put("poolTypeStats", poolTypeStats);

            // 6. 按状态统计
            Map<String, Long> statusCountMap = new HashMap<>();
            for (TblBillPool pool : allPools) {
                String status = pool.getPoolStatus() != null ? pool.getPoolStatus() : "UNKNOWN";
                statusCountMap.put(status, statusCountMap.getOrDefault(status, 0L) + 1);
            }
            statistics.put("statusStats", statusCountMap);

            // 7. 正常状态的票据池数量
            long normalCount = allPools.stream()
                    .filter(p -> "NORMAL".equals(p.getPoolStatus()) || "ACTIVE".equals(p.getPoolStatus()))
                    .count();
            statistics.put("normalPoolCount", normalCount);

            // 8. 质押中的票据池数量
            long pledgedCount = allPools.stream()
                    .filter(p -> "PLEDGED".equals(p.getPoolStatus()))
                    .count();
            statistics.put("pledgedPoolCount", pledgedCount);

        } catch (Exception e) {
            log.error("获取票据池统计数据失败", e);
            // 返回默认值
            statistics.put("totalPools", 0);
            statistics.put("totalBillsInPool", 0);
            statistics.put("totalPoolAmount", BigDecimal.ZERO);
            statistics.put("financingBalance", BigDecimal.ZERO);
            statistics.put("poolTypeStats", new ArrayList<>());
            statistics.put("statusStats", new HashMap<>());
        }

        return statistics;
    }

    /**
     * 获取票据池类型名称
     */
    private String getPoolTypeName(String type) {
        if (type == null) return "其他";
        switch (type) {
            case "PLEDGE_FINANCING": return "质押融资池";
            case "LIQUIDITY_MANAGEMENT": return "流动性管理池";
            case "RISK_DIVERSIFICATION": return "风险分散池";
            case "PORTFOLIO": return "投资组合池";
            default: return type;
        }
    }

    @Override
    public boolean freezeBillPool(Long poolId) {
        if (poolId == null) {
            throw new BizException("票据池ID不能为空");
        }
        TblBillPool pool = baseMapper.selectById(poolId);
        if (pool == null) {
            throw new BizException("票据池不存在");
        }
        pool.setPoolStatus("FROZEN");
        pool.setUpdateTime(new Date());
        return baseMapper.updateById(pool) > 0;
    }

    @Override
    public boolean closeBillPool(Long poolId) {
        if (poolId == null) {
            throw new BizException("票据池ID不能为空");
        }
        TblBillPool pool = baseMapper.selectById(poolId);
        if (pool == null) {
            throw new BizException("票据池不存在");
        }
        // 检查池内是否还有票据
        List<BillInPoolVO> bills = billPoolDetailMapper.selectBillsByPoolId(poolId);
        if (bills != null && !bills.isEmpty()) {
            throw new BizException("票据池内还有票据，无法关闭");
        }
        pool.setPoolStatus("CLOSED");
        pool.setUpdateTime(new Date());
        return baseMapper.updateById(pool) > 0;
    }

    @Override
    public Map<String, Object> generatePoolReport(Long poolId) {
        if (poolId == null) {
            throw new BizException("票据池ID不能为空");
        }

        // 1. 查询票据池基本信息
        BillPoolVO poolInfo = baseMapper.selectBillPoolById(poolId);
        if (poolInfo == null) {
            throw new BizException("票据池不存在");
        }

        // 2. 查询池内票据列表
        List<BillInPoolVO> bills = billPoolDetailMapper.selectBillsByPoolId(poolId);
        if (bills == null) {
            bills = new ArrayList<>();
        }

        // 3. 查询融资记录
        List<PledgeFinancingRecordVO> financingRecords = pledgeFinancingMapper.selectFinancingRecordsByPoolId(poolId);
        if (financingRecords == null) {
            financingRecords = new ArrayList<>();
        }

        // 4. 计算统计摘要
        Map<String, Object> summary = new HashMap<>();
        // 票据统计
        summary.put("billCount", bills.size());
        BigDecimal totalBillAmount = BigDecimal.ZERO;
        Map<String, Integer> billTypeDistribution = new HashMap<>();
        for (BillInPoolVO bill : bills) {
            if (bill.getBillAmount() != null) {
                totalBillAmount = totalBillAmount.add(bill.getBillAmount());
            }
            String billType = bill.getBillType() != null ? bill.getBillType() : "未知";
            billTypeDistribution.put(billType, billTypeDistribution.getOrDefault(billType, 0) + 1);
        }
        summary.put("totalBillAmount", totalBillAmount);
        summary.put("avgBillAmount", bills.isEmpty() ? BigDecimal.ZERO : totalBillAmount.divide(new BigDecimal(bills.size()), 2, BigDecimal.ROUND_HALF_UP));
        summary.put("billTypeDistribution", billTypeDistribution);

        // 融资统计
        summary.put("financingCount", financingRecords.size());
        BigDecimal totalFinancingAmount = BigDecimal.ZERO;
        BigDecimal totalPledgeAmount = BigDecimal.ZERO;
        int repaidCount = 0;
        int pendingCount = 0;
        for (PledgeFinancingRecordVO record : financingRecords) {
            if (record.getFinancingAmount() != null) {
                totalFinancingAmount = totalFinancingAmount.add(record.getFinancingAmount());
            }
            if (record.getPledgeAmount() != null) {
                totalPledgeAmount = totalPledgeAmount.add(record.getPledgeAmount());
            }
            if ("REPAID".equals(record.getRepayStatus())) {
                repaidCount++;
            } else {
                pendingCount++;
            }
        }
        summary.put("totalFinancingAmount", totalFinancingAmount);
        summary.put("totalPledgeAmount", totalPledgeAmount);
        summary.put("repaidCount", repaidCount);
        summary.put("pendingCount", pendingCount);

        // 5. 组装报告
        Map<String, Object> report = new HashMap<>();
        report.put("poolInfo", poolInfo);
        report.put("bills", bills);
        report.put("financingRecords", financingRecords);
        report.put("summary", summary);
        report.put("reportTime", new Date());

        return report;
    }
}

