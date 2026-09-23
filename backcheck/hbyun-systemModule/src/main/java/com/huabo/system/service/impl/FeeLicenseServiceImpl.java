package com.huabo.system.service.impl;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.system.dto.LicenseGenerateDTO;
import com.huabo.system.dto.LicenseRechargeDTO;
import com.huabo.system.entity.TblFeeBalance;
import com.huabo.system.entity.TblFeeLicense;
import com.huabo.system.mapper.TblFeeBalanceMapper;
import com.huabo.system.mapper.TblFeeLicenseMapper;
import com.huabo.system.service.FeeBalanceService;
import com.huabo.system.service.FeeLicenseService;
import com.hbfk.util.user.UserProvider;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
public class FeeLicenseServiceImpl implements FeeLicenseService {

    @Resource
    private TblFeeLicenseMapper feeLicenseMapper;
    @Resource
    private TblFeeBalanceMapper feeBalanceMapper;
    @Resource
    private FeeBalanceService feeBalanceService;
    @Resource
    private UserProvider userProvider;

    @Value("${billing.aes-key:HbYunBilling2026}")
    private String aesKey;
    @Value("${billing.hmac-key:HbYunHmacKey2026}")
    private String hmacKey;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean recharge(String token, LicenseRechargeDTO dto) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (dto.getLicenseKey() == null || dto.getLicenseKey().isEmpty()) {
            return ResponseFormat.retParam(0, 10002, null);
        }

        // 1. Compute HMAC hash of the license key
        String keyHash = hmacSha256(dto.getLicenseKey());

        // 2. Check if already used
        TblFeeLicense existing = feeLicenseMapper.findByLicenseKeyHash(keyHash);
        if (existing != null && existing.getUseStatus() != null && existing.getUseStatus() == 1) {
            return new JsonBean(0, "该密钥已被使用", null);
        }

        // 3. Decode and decrypt the license key
        String decoded;
        try {
            byte[] raw = Base64.getDecoder().decode(dto.getLicenseKey());
            SecretKeySpec keySpec = new SecretKeySpec(padKey(aesKey), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            decoded = new String(cipher.doFinal(raw), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return new JsonBean(0, "密钥格式无效", null);
        }

        // 4. Parse: companyOrgId|amount|expireTime|salt
        String[] parts = decoded.split("\\|");
        if (parts.length < 3) {
            return new JsonBean(0, "密钥内容无效", null);
        }
        BigDecimal authorizedCompanyId = new BigDecimal(parts[0]);
        BigDecimal rechargeAmount = new BigDecimal(parts[1]);
        long expireTimestamp = Long.parseLong(parts[2]);

        // 5. Check expiry
        if (System.currentTimeMillis() > expireTimestamp) {
            return new JsonBean(0, "密钥已过期", null);
        }

        // 6. Check company match
        BigDecimal currentCompanyId = loginStaff.getLinkOrg() != null ? loginStaff.getLinkOrg().getOrgid() : null;
        if (currentCompanyId == null || authorizedCompanyId.compareTo(currentCompanyId) != 0) {
            return new JsonBean(0, "密钥授权公司(" + authorizedCompanyId + ")与当前登录公司(" + currentCompanyId + ")不匹配", null);
        }

        // 7. Init balance if not exists
        feeBalanceService.initBalance(currentCompanyId);

        // 8. Add balance
        TblFeeBalance balance = feeBalanceMapper.findByCompanyOrgId(currentCompanyId);
        BigDecimal currentBalance = decryptBalance(balance.getBalance());
        BigDecimal newBalance = currentBalance.add(rechargeAmount);
        feeBalanceMapper.rechargeBalance(currentCompanyId, encryptBalance(newBalance), rechargeAmount);

        // 9. Record license usage
        if (existing != null) {
            feeLicenseMapper.markAsUsed(existing.getId());
        } else {
            TblFeeLicense license = new TblFeeLicense();
            license.setId(feeLicenseMapper.getNextId());
            license.setLicenseKeyHash(keyHash);
            license.setCompanyOrgId(authorizedCompanyId);
            license.setRechargeAmount(rechargeAmount);
            license.setUseStatus(1);
            license.setUseTime(new Date());
            license.setExpireTime(new Date(expireTimestamp));
            license.setCreateTime(new Date());
            license.setOperator(loginStaff.getRealname());
            feeLicenseMapper.insert(license);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rechargeAmount", rechargeAmount);
        resultMap.put("newBalance", newBalance);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean getRechargeRecords(String token, BigDecimal companyOrgId, Integer pageNum, Integer pageSize) throws Exception {
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

        PageHelper.startPage(pageNum, pageSize);
        List<TblFeeLicense> records = feeLicenseMapper.findUsedByCompany(companyOrgId);
        PageInfo<TblFeeLicense> pageInfo = new PageInfo<>(records);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", pageInfo.getList());
        resultMap.put("total", pageInfo.getTotal());
        resultMap.put("pageNum", pageInfo.getPageNum());
        resultMap.put("pageSize", pageInfo.getPageSize());
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean generateLicense(String token, LicenseGenerateDTO dto) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (dto.getCompanyOrgId() == null || dto.getRechargeAmount() == null) {
            return ResponseFormat.retParam(0, 10002, null);
        }

        // Build plaintext: companyOrgId|amount|expireTimestamp|salt
        String salt = UUID.randomUUID().toString();
        long expireTimestamp = dto.getExpireTime() != null ? dto.getExpireTime().getTime() : (System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000);
        String plainText = dto.getCompanyOrgId().toPlainString() + "|" + dto.getRechargeAmount().toPlainString() + "|" + expireTimestamp + "|" + salt;

        // AES encrypt
        SecretKeySpec keySpec = new SecretKeySpec(padKey(aesKey), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        String licenseKey = Base64.getEncoder().encodeToString(encrypted);

        // Store license record
        String keyHash = hmacSha256(licenseKey);
        TblFeeLicense license = new TblFeeLicense();
        license.setId(feeLicenseMapper.getNextId());
        license.setLicenseKeyHash(keyHash);
        license.setCompanyOrgId(dto.getCompanyOrgId());
        license.setRechargeAmount(dto.getRechargeAmount());
        license.setUseStatus(0);
        license.setExpireTime(new Date(expireTimestamp));
        license.setCreateTime(new Date());
        license.setOperator(loginStaff.getRealname());
        feeLicenseMapper.insert(license);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("licenseKey", licenseKey);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    private String hmacSha256(String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(hmacKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(keySpec);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private String encryptBalance(BigDecimal amount) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(padKey(aesKey), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(amount.toPlainString().getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    private BigDecimal decryptBalance(String encryptedText) throws Exception {
        if (encryptedText == null || encryptedText.isEmpty()) return BigDecimal.ZERO;
        SecretKeySpec keySpec = new SecretKeySpec(padKey(aesKey), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new BigDecimal(new String(decrypted, StandardCharsets.UTF_8));
    }

    private byte[] padKey(String key) {
        byte[] keyBytes = new byte[16];
        byte[] raw = key.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(raw, 0, keyBytes, 0, Math.min(raw.length, 16));
        return keyBytes;
    }
}