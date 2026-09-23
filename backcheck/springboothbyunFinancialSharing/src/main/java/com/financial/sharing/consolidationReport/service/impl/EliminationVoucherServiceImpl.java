package com.financial.sharing.consolidationReport.service.impl;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.consolidationReport.dto.EliminationVoucherQueryParam;
import com.financial.sharing.consolidationReport.dto.VoucherGenerateParam;
import com.financial.sharing.consolidationReport.entity.TblEliminationTemplate;
import com.financial.sharing.consolidationReport.entity.TblEliminationVoucher;
import com.financial.sharing.consolidationReport.entity.TblEquityInfo;
import com.financial.sharing.consolidationReport.mapper.EliminationTemplateMapper;
import com.financial.sharing.consolidationReport.mapper.EliminationVoucherMapper;
import com.financial.sharing.consolidationReport.mapper.EquityInfoMapper;
import com.financial.sharing.consolidationReport.service.EliminationVoucherService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 抵消凭证Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class EliminationVoucherServiceImpl implements EliminationVoucherService {

    @Autowired
    private EliminationVoucherMapper eliminationVoucherMapper;

    @Autowired
    private EliminationTemplateMapper eliminationTemplateMapper;

    @Autowired
    private EquityInfoMapper equityInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> generateVouchers(VoucherGenerateParam param) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        Map<String, Object> result = new HashMap<>();
        int totalCount = 0;
        int successCount = 0;
        List<String> errorMessages = new ArrayList<>();

        try {
            // 1. 如果是重新生成,先删除旧数据
            if (param.getRegenerate() != null && param.getRegenerate()) {
                eliminationVoucherMapper.deleteByModelIdAndPeriod(param.getModelId(), param.getPeriod());
            }

            // 2. 获取该模型的所有启用模板
            List<TblEliminationTemplate> templates = eliminationTemplateMapper.selectByModelId(param.getModelId());
            if (templates == null || templates.isEmpty()) {
                throw new RuntimeException("该模型没有可用的抵消凭证模板");
            }

            // 过滤启用的模板
            templates = templates.stream()
                .filter(t -> "Y".equals(t.getIsActive()))
                .sorted(Comparator.comparing(TblEliminationTemplate::getSortOrder))
                .collect(java.util.stream.Collectors.toList());

            if (templates.isEmpty()) {
                throw new RuntimeException("该模型没有启用的抵消凭证模板");
            }

            totalCount = templates.size();

            // 3. 按排序号依次处理每个模板
            List<TblEliminationVoucher> allVouchers = new ArrayList<>();
            int voucherNoSeq = 1;

            for (TblEliminationTemplate template : templates) {
                try {
                    // 生成凭证号
                    String voucherNo = generateVoucherNo(param.getPeriod(), voucherNoSeq++);

                    // 根据模板生成凭证
                    List<TblEliminationVoucher> vouchers = generateVouchersByTemplate(
                        template, param, voucherNo, tenantId, userId, now);

                    if (vouchers != null && !vouchers.isEmpty()) {
                        allVouchers.addAll(vouchers);
                        successCount++;
                    }
                } catch (Exception e) {
                    errorMessages.add("模板[" + template.getTemplateName() + "]生成失败: " + e.getMessage());
                }
            }

            // 4. 批量插入凭证
            if (!allVouchers.isEmpty()) {
                eliminationVoucherMapper.batchInsert(allVouchers);
            }

            result.put("success", true);
            result.put("totalCount", totalCount);
            result.put("successCount", successCount);
            result.put("voucherCount", allVouchers.size());
            result.put("errorMessages", errorMessages);
            result.put("message", "生成成功,共处理" + totalCount + "个模板,成功" + successCount + "个,生成" + allVouchers.size() + "条凭证");

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "生成失败: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        return result;
    }

    /**
     * 根据模板生成凭证
     */
    private List<TblEliminationVoucher> generateVouchersByTemplate(
            TblEliminationTemplate template, VoucherGenerateParam param,
            String voucherNo, String tenantId, String userId, Date now) {

        List<TblEliminationVoucher> vouchers = new ArrayList<>();

        // 这里是简化的实现,实际应该根据模板的计算规则获取数据并计算金额
        // 为了演示,这里使用固定金额
        BigDecimal amount = new BigDecimal("10000.00");

        // 解析借方科目
        String[] debitAccounts = template.getDebitAccount().split(",");
        // 解析贷方科目
        String[] creditAccounts = template.getCreditAccount().split(",");

        // 生成借方分录
        for (String accountCode : debitAccounts) {
            accountCode = accountCode.trim();
            if (!accountCode.isEmpty()) {
                TblEliminationVoucher voucher = new TblEliminationVoucher();
                voucher.setVoucherId(UUID.randomUUID().toString().replace("-", ""));
                voucher.setModelId(param.getModelId());
                voucher.setTemplateId(template.getTemplateId());
                voucher.setPeriod(param.getPeriod());
                voucher.setVoucherNo(voucherNo);
                voucher.setVoucherDate(param.getVoucherDate() != null ? param.getVoucherDate() : now);
                voucher.setEntryType("DEBIT");
                voucher.setAccountCode(accountCode);
                voucher.setAccountName(accountCode); // 实际应该查询科目名称
                voucher.setAmount(amount);
                voucher.setOrgId("");
                voucher.setOrgName("");
                voucher.setDescription(template.getTemplateName());
                voucher.setStatus("DRAFT");
                voucher.setTenantId(tenantId);
                voucher.setCreateUser(userId);
                voucher.setCreateTime(now);
                voucher.setUpdateUser(userId);
                voucher.setUpdateTime(now);
                vouchers.add(voucher);
            }
        }

        // 生成贷方分录
        for (String accountCode : creditAccounts) {
            accountCode = accountCode.trim();
            if (!accountCode.isEmpty()) {
                TblEliminationVoucher voucher = new TblEliminationVoucher();
                voucher.setVoucherId(UUID.randomUUID().toString().replace("-", ""));
                voucher.setModelId(param.getModelId());
                voucher.setTemplateId(template.getTemplateId());
                voucher.setPeriod(param.getPeriod());
                voucher.setVoucherNo(voucherNo);
                voucher.setVoucherDate(param.getVoucherDate() != null ? param.getVoucherDate() : now);
                voucher.setEntryType("CREDIT");
                voucher.setAccountCode(accountCode);
                voucher.setAccountName(accountCode); // 实际应该查询科目名称
                voucher.setAmount(amount);
                voucher.setOrgId("");
                voucher.setOrgName("");
                voucher.setDescription(template.getTemplateName());
                voucher.setStatus("DRAFT");
                voucher.setTenantId(tenantId);
                voucher.setCreateUser(userId);
                voucher.setCreateTime(now);
                voucher.setUpdateUser(userId);
                voucher.setUpdateTime(now);
                vouchers.add(voucher);
            }
        }

        return vouchers;
    }

    /**
     * 生成凭证号
     */
    private String generateVoucherNo(String period, int seq) {
        return "DC-" + period + "-" + String.format("%04d", seq);
    }

    @Override
    public PageInfo<TblEliminationVoucher> getVoucherList(EliminationVoucherQueryParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<TblEliminationVoucher> list = eliminationVoucherMapper.selectVoucherList(param);
        return new PageInfo<>(list);
    }

    @Override
    public List<TblEliminationVoucher> getVouchersByNo(String voucherNo) {
        return eliminationVoucherMapper.selectByVoucherNo(voucherNo);
    }

    @Override
    public List<String> getVoucherNoList(String modelId, String period) {
        return eliminationVoucherMapper.selectVoucherNoList(modelId, period);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteVouchers(String modelId, String period) {
        eliminationVoucherMapper.deleteByModelIdAndPeriod(modelId, period);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmVouchers(String modelId, String period) {
        eliminationVoucherMapper.updateStatusByModelIdAndPeriod(modelId, period, "CONFIRMED");
    }
}


