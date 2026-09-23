package com.huabo.system.service.impl;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.system.entity.TblFeeBalance;
import com.huabo.system.mapper.TblFeeBalanceMapper;
import com.huabo.system.service.FeeBalanceService;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.vo.FeeBalanceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FeeBalanceServiceImpl implements FeeBalanceService {

    @Resource
    private TblFeeBalanceMapper feeBalanceMapper;
    @Resource
    private UserProvider userProvider;

    @Value("${billing.aes-key:HbYunBilling2026}")
    private String aesKey;

    @Value("${billing.init-amount:3000}")
    private BigDecimal initAmount;

    @Override
    public JsonBean queryBalance(String token, BigDecimal companyOrgId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        if (companyOrgId == null) {
            companyOrgId = loginStaff.getLinkOrg() != null ? loginStaff.getLinkOrg().getOrgid() : null;
        }
        if (companyOrgId == null) {
            return ResponseFormat.retParam(0, 10002, null);
        }

        TblFeeBalance balance = feeBalanceMapper.findByCompanyOrgId(companyOrgId);
        if (balance == null) {
            // Auto-init balance for new company
            initBalance(companyOrgId);
            balance = feeBalanceMapper.findByCompanyOrgId(companyOrgId);
        }

        FeeBalanceVO vo = new FeeBalanceVO();
        vo.setCompanyOrgId(balance.getCompanyOrgId());
        // 当前余额 = 初始赠送 + 累计充值 - 累计消费
        BigDecimal initAmt = balance.getInitAmount() != null ? balance.getInitAmount() : BigDecimal.ZERO;
        BigDecimal recharged = balance.getTotalRecharged() != null ? balance.getTotalRecharged() : BigDecimal.ZERO;
        BigDecimal consumed = balance.getTotalConsumed() != null ? balance.getTotalConsumed() : BigDecimal.ZERO;
        vo.setBalance(initAmt.add(recharged).subtract(consumed));
        vo.setTotalConsumed(consumed);
        vo.setTotalRecharged(recharged);
        vo.setInitAmount(initAmt);
        vo.setLastRechargeTime(balance.getLastRechargeTime());

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("balance", vo);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean initBalance(BigDecimal companyOrgId) throws Exception {
        TblFeeBalance existing = feeBalanceMapper.findByCompanyOrgId(companyOrgId);
        if (existing != null) {
            return ResponseFormat.retParam(1, 200, null);
        }

        BigDecimal defaultInitAmount = initAmount != null ? initAmount : new BigDecimal("3000");
        TblFeeBalance balance = new TblFeeBalance();
        balance.setId(feeBalanceMapper.getNextId());
        balance.setCompanyOrgId(companyOrgId);
        balance.setBalance(encryptBalance(defaultInitAmount));
        balance.setTotalConsumed(BigDecimal.ZERO);
        balance.setTotalRecharged(BigDecimal.ZERO);
        balance.setInitAmount(defaultInitAmount);
        balance.setInitTime(new Date());
        balance.setUpdateTime(new Date());
        feeBalanceMapper.insert(balance);

        return ResponseFormat.retParam(1, 200, null);
    }

    @Value("${billing.mode:cloud}")
    private String billingMode;

    @Value("${billing.warning-threshold:100}")
    private BigDecimal warningThreshold;

    @Override
    public JsonBean checkBalance(String token) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        BigDecimal companyOrgId = loginStaff.getLinkOrg() != null ? loginStaff.getLinkOrg().getOrgid() : null;
        if (companyOrgId == null) {
            return ResponseFormat.retParam(1, 200, null);
        }

        TblFeeBalance balance = feeBalanceMapper.findByCompanyOrgId(companyOrgId);
        if (balance == null) {
            initBalance(companyOrgId);
            balance = feeBalanceMapper.findByCompanyOrgId(companyOrgId);
        }

        BigDecimal initAmt = balance.getInitAmount() != null ? balance.getInitAmount() : BigDecimal.ZERO;
        BigDecimal recharged = balance.getTotalRecharged() != null ? balance.getTotalRecharged() : BigDecimal.ZERO;
        BigDecimal consumed = balance.getTotalConsumed() != null ? balance.getTotalConsumed() : BigDecimal.ZERO;
        BigDecimal currentBalance = initAmt.add(recharged).subtract(consumed);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("balance", currentBalance);
        resultMap.put("mode", billingMode);
        resultMap.put("warningThreshold", warningThreshold);
        resultMap.put("billingStatus", "normal");

        if (currentBalance.compareTo(BigDecimal.ZERO) <= 0 && "offline".equals(billingMode)) {
            resultMap.put("billingStatus", "blocked");
            resultMap.put("msg", "余额不足，请充值后使用");
        } else if (currentBalance.compareTo(warningThreshold) <= 0) {
            resultMap.put("billingStatus", "warning");
            resultMap.put("msg", "余额不足" + warningThreshold + "元，请及时充值");
        }

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    private String encryptBalance(BigDecimal amount) throws Exception {
        String plainText = amount.toPlainString();
        SecretKeySpec keySpec = new SecretKeySpec(padKey(aesKey), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    private BigDecimal decryptBalance(String encryptedText) throws Exception {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            SecretKeySpec keySpec = new SecretKeySpec(padKey(aesKey), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new BigDecimal(new String(decrypted, StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("余额解密失败", e);
            return BigDecimal.ZERO;
        }
    }

    private byte[] padKey(String key) {
        byte[] keyBytes = new byte[16];
        byte[] raw = key.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(raw, 0, keyBytes, 0, Math.min(raw.length, 16));
        return keyBytes;
    }
}
