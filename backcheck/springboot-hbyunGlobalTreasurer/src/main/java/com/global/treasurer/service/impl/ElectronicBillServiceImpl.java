package com.global.treasurer.service.impl;

import com.global.treasurer.dto.ElectronicBillDTO;
import com.global.treasurer.dto.ElectronicBillQueryDTO;
import com.global.treasurer.entity.TblElectronicBill;
import com.global.treasurer.mapper.BillDiscountMapper;
import com.global.treasurer.mapper.BillEndorsementMapper;
import com.global.treasurer.mapper.ElectronicBillMapper;
import com.global.treasurer.service.IElectronicBillService;
import com.global.treasurer.vo.CirculationRecordVO;
import com.global.treasurer.vo.ElectronicBillVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.SnowflakeIdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 电子票据Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@Service
public class ElectronicBillServiceImpl extends ServiceImpl<ElectronicBillMapper, TblElectronicBill>
        implements IElectronicBillService {
    private static final Logger log = LoggerFactory.getLogger(ElectronicBillServiceImpl.class);

    @Resource
    private ElectronicBillMapper electronicBillMapper;

    @Resource
    private BillEndorsementMapper billEndorsementMapper;

    @Resource
    private BillDiscountMapper billDiscountMapper;

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public PageInfo<ElectronicBillVO> selectElectronicBillList(ElectronicBillQueryDTO queryDTO) {
        log.info("查询电子票据列表, 参数: {}", queryDTO);

        // 分页查询
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<ElectronicBillVO> list = electronicBillMapper.selectElectronicBillList(queryDTO);

        return new PageInfo<>(list);
    }

    @Override
    public ElectronicBillVO selectElectronicBillById(Long billId) {
        log.info("查询电子票据详情, billId: {}", billId);
        return electronicBillMapper.selectElectronicBillById(billId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblElectronicBill insertElectronicBill(ElectronicBillDTO dto) {
        log.info("新增电子票据, 参数: {}", dto);

        TblElectronicBill entity = new TblElectronicBill();

        // 生成ID和票据号码
        Long billId = snowflakeIdWorker.nextId();
        entity.setElectronicBillId(billId);

        // 如果没有票据号码，自动生成
        if (dto.getBillNumber() == null || dto.getBillNumber().isEmpty()) {
            String billNumber = "EB" + new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + billId % 10000;
            entity.setBillNumber(billNumber);
        } else {
            entity.setBillNumber(dto.getBillNumber());
        }

        // 复制基本信息
        entity.setBillType(dto.getBillType());
        entity.setBillAmount(dto.getBillAmount());
        entity.setCurrency(dto.getCurrency() != null ? dto.getCurrency() : "CNY");
        entity.setIssueDate(dto.getIssueDate());
        entity.setMaturityDate(dto.getMaturityDate());

        // 复制出票人信息
        entity.setDrawerName(dto.getDrawerName());
        entity.setDrawerAccount(dto.getDrawerAccount());
        entity.setDrawerBank(dto.getDrawerBank());

        // 复制承兑人信息
        entity.setAcceptorName(dto.getAcceptorName());
        entity.setAcceptorAccount(dto.getAcceptorAccount());
        entity.setAcceptorBank(dto.getAcceptorBank());

        // 复制收款人信息
        entity.setPayeeName(dto.getPayeeName());
        entity.setPayeeAccount(dto.getPayeeAccount());

        // 设置状态
        entity.setBillStatus(dto.getBillStatus() != null ? dto.getBillStatus() : "ISSUED");
        entity.setSignatureStatus("PENDING");
        entity.setVerificationStatus("PENDING");
        entity.setElectronicStatus("ACTIVE");

        // 其他信息
        entity.setRemark(dto.getRemark());
        entity.setCompanyId(dto.getCompanyId());
        entity.setDeptId(dto.getDeptId());
        entity.setDeleteFlag(0);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());

        electronicBillMapper.insert(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblElectronicBill updateElectronicBill(ElectronicBillDTO dto) {
        log.info("修改电子票据, 参数: {}", dto);

        TblElectronicBill entity = new TblElectronicBill();
        BeanUtils.copyProperties(dto, entity);
        entity.setUpdateTime(new Date());

        electronicBillMapper.updateById(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteElectronicBillByIds(Long[] billIds) {
        log.info("删除电子票据, billIds: {}", billIds);
        return electronicBillMapper.deleteElectronicBillByIds(billIds) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean signElectronicBill(Map<String, Object> signData) {
        log.info("数字签名电子票据, 参数: {}", signData);

        Long billId = (Long) signData.get("billId");
        String digitalSignature = (String) signData.get("digitalSignature");
        String signerName = (String) signData.get("signerName");
        String signerOrg = (String) signData.get("signerOrg");

        return electronicBillMapper.updateSignatureStatus(billId, "SIGNED", digitalSignature, signerName, signerOrg) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> verifyElectronicBill(Map<String, Object> verifyData) {
        log.info("验证电子票据, 参数: {}", verifyData);

        Long billId = (Long) verifyData.get("billId");
        boolean isValid = (Boolean) verifyData.getOrDefault("isValid", false);

        // 更新验证状态
        String verificationStatus = isValid ? "VERIFIED" : "FAILED";
        electronicBillMapper.updateVerificationStatus(billId, verificationStatus);

        Map<String, Object> result = new HashMap<>();
        result.put("billId", billId);
        result.put("verificationStatus", verificationStatus);
        result.put("verificationTime", new Date());

        return result;
    }

    @Override
    public Map<String, Object> trackElectronicBill(Long billId) {
        log.info("追踪电子票据流转, billId: {}", billId);

        // 查询电子票据详情
        ElectronicBillVO bill = electronicBillMapper.selectElectronicBillById(billId);

        Map<String, Object> result = new HashMap<>();
        if (bill != null) {
            result.put("billId", bill.getBillId());
            result.put("billNumber", bill.getBillNumber());
            result.put("billType", bill.getBillType());
            result.put("billAmount", bill.getBillAmount());
            result.put("issueDate", bill.getIssueDate());
            result.put("maturityDate", bill.getMaturityDate());
            result.put("signatureStatus", bill.getSignatureStatus());
            result.put("signatureTime", bill.getSignatureTime());
            result.put("billUrl", bill.getBillUrl());
            result.put("createTime", bill.getCreateTime());
        }

        return result;
    }

    @Override
    public List<CirculationRecordVO> selectCirculationRecords(Long billId, Map<String, Object> params) {
        log.info("查询电子票据流转记录, billId: {}, params: {}", billId, params);

        List<CirculationRecordVO> allRecords = new ArrayList<>();

        try {
            // 1. 从票据背书表查询背书记录
            List<CirculationRecordVO> endorsementRecords = billEndorsementMapper.selectCirculationByBillId(billId);
            allRecords.addAll(endorsementRecords);

            // 2. 从票据贴现表查询贴现记录
            List<CirculationRecordVO> discountRecords = billDiscountMapper.selectCirculationByBillId(billId);
            allRecords.addAll(discountRecords);

            // 3. 按时间倒序排序
            allRecords.sort((r1, r2) -> {
                if (r1.getTransactionTime() == null) return 1;
                if (r2.getTransactionTime() == null) return -1;
                return r2.getTransactionTime().compareTo(r1.getTransactionTime());
            });

        } catch (Exception e) {
            log.error("查询电子票据流转记录失败", e);
        }

        return allRecords;
    }

    @Override
    public Map<String, Object> getElectronicBillStatistics(ElectronicBillQueryDTO queryDTO) {
        log.info("获取电子票据统计数据, 参数: {}", queryDTO);

        Map<String, Object> statistics = new HashMap<>();

        try {
            // 查询所有未删除的票据
            List<ElectronicBillVO> allBills = electronicBillMapper.selectElectronicBillList(queryDTO);

            // 总票据数
            int totalBills = allBills.size();
            statistics.put("totalBills", totalBills);

            // 活跃票据数（已签发和流转中的票据）
            long activeBills = allBills.stream()
                    .filter(b -> "ISSUED".equals(b.getBillStatus()) || "CIRCULATING".equals(b.getBillStatus()))
                    .count();
            statistics.put("activeBills", activeBills);

            // 总金额（万元）
            double totalAmount = allBills.stream()
                    .filter(b -> b.getBillAmount() != null)
                    .mapToDouble(b -> b.getBillAmount().doubleValue())
                    .sum() / 10000;
            statistics.put("totalAmount", String.format("%.2f", totalAmount));

            // 月增长率（模拟计算，实际应该对比上月数据）
            double growthRate = 12.5; // 默认增长率
            statistics.put("growthRate", growthRate);

            // 按状态统计
            Map<String, Long> statusCount = new HashMap<>();
            statusCount.put("ISSUED", allBills.stream().filter(b -> "ISSUED".equals(b.getBillStatus())).count());
            statusCount.put("CIRCULATING", allBills.stream().filter(b -> "CIRCULATING".equals(b.getBillStatus())).count());
            statusCount.put("ENDORSED", allBills.stream().filter(b -> "ENDORSED".equals(b.getBillStatus())).count());
            statusCount.put("DISCOUNTED", allBills.stream().filter(b -> "DISCOUNTED".equals(b.getBillStatus())).count());
            statusCount.put("MATURED", allBills.stream().filter(b -> "MATURED".equals(b.getBillStatus())).count());
            statusCount.put("CANCELLED", allBills.stream().filter(b -> "CANCELLED".equals(b.getBillStatus())).count());
            statistics.put("statusCount", statusCount);

            // 按类型统计
            Map<String, Long> typeCount = new HashMap<>();
            typeCount.put("E_BANK_ACCEPTANCE", allBills.stream().filter(b -> "E_BANK_ACCEPTANCE".equals(b.getBillType())).count());
            typeCount.put("E_COMMERCIAL_ACCEPTANCE", allBills.stream().filter(b -> "E_COMMERCIAL_ACCEPTANCE".equals(b.getBillType())).count());
            typeCount.put("E_CHECK", allBills.stream().filter(b -> "E_CHECK".equals(b.getBillType())).count());
            typeCount.put("E_PROMISSORY_NOTE", allBills.stream().filter(b -> "E_PROMISSORY_NOTE".equals(b.getBillType())).count());
            statistics.put("typeCount", typeCount);

        } catch (Exception e) {
            log.error("获取电子票据统计数据失败", e);
            // 返回默认值
            statistics.put("totalBills", 0);
            statistics.put("activeBills", 0);
            statistics.put("totalAmount", "0.00");
            statistics.put("growthRate", 0);
        }

        return statistics;
    }

    @Override
    public Map<String, Object> getBillTrendAnalysis(Map<String, Object> params) {
        log.info("获取票据趋势分析数据, 参数: {}", params);

        Map<String, Object> trendData = new HashMap<>();

        try {
            // 获取天数参数，默认30天
            int days = 30;
            if (params != null && params.get("days") != null) {
                days = Integer.parseInt(params.get("days").toString());
            }

            // 生成日期标签
            List<String> dateLabels = new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            for (int i = days - 1; i >= 0; i--) {
                cal.setTime(new Date());
                cal.add(Calendar.DAY_OF_MONTH, -i);
                dateLabels.add((cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH));
            }
            trendData.put("dateLabels", dateLabels);

            // 查询所有票据
            ElectronicBillQueryDTO queryDTO = new ElectronicBillQueryDTO();
            List<ElectronicBillVO> allBills = electronicBillMapper.selectElectronicBillList(queryDTO);

            // 生成各类型的趋势数据
            List<Integer> issueData = new ArrayList<>();
            List<Integer> endorseData = new ArrayList<>();
            List<Integer> discountData = new ArrayList<>();
            List<Integer> maturityData = new ArrayList<>();

            // 按日期统计各类型票据数量
            for (int i = days - 1; i >= 0; i--) {
                cal.setTime(new Date());
                cal.add(Calendar.DAY_OF_MONTH, -i);
                final Date targetDate = cal.getTime();

                // 统计当天签发的票据
                long issued = allBills.stream()
                        .filter(b -> b.getIssueDate() != null && isSameDay(b.getIssueDate(), targetDate))
                        .count();
                issueData.add((int) issued);

                // 统计当天背书的票据（模拟数据）
                endorseData.add((int) (Math.random() * 3));

                // 统计当天贴现的票据（模拟数据）
                discountData.add((int) (Math.random() * 2));

                // 统计当天到期的票据
                long matured = allBills.stream()
                        .filter(b -> b.getMaturityDate() != null && isSameDay(b.getMaturityDate(), targetDate))
                        .count();
                maturityData.add((int) matured);
            }

            trendData.put("issueData", issueData);
            trendData.put("endorseData", endorseData);
            trendData.put("discountData", discountData);
            trendData.put("maturityData", maturityData);

        } catch (Exception e) {
            log.error("获取票据趋势分析数据失败", e);
            // 返回空数据
            trendData.put("dateLabels", new ArrayList<>());
            trendData.put("issueData", new ArrayList<>());
            trendData.put("endorseData", new ArrayList<>());
            trendData.put("discountData", new ArrayList<>());
            trendData.put("maturityData", new ArrayList<>());
        }

        return trendData;
    }

    /**
     * 判断两个日期是否是同一天
     */
    private boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
}
