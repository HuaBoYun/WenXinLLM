package com.huabo.system.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

/**
 * 密钥生成工具（可独立运行）
 * 用法: java LicenseKeyGenerator <公司ID> <充值金额> [有效天数]
 */
public class LicenseKeyGenerator {

    private static final String AES_KEY = "HbYunBilling2026";

    public static void main(String[] args) throws Exception {
        String companyId = args.length > 0 ? args[0] : "1";
        String amount = args.length > 1 ? args[1] : "4000";
        int days = args.length > 2 ? Integer.parseInt(args[2]) : 30;

        String licenseKey = generate(companyId, amount, days);

        System.out.println("========== 充值密钥 ==========");
        System.out.println("密钥: " + licenseKey);
        System.out.println("金额: " + amount + " 元");
        System.out.println("公司ID: " + companyId);
        System.out.println("有效期: " + days + " 天");
        System.out.println("==============================");
    }

    public static String generate(String companyId, String amount, int validDays) throws Exception {
        String salt = UUID.randomUUID().toString();
        long expireTs = System.currentTimeMillis() + (long) validDays * 24 * 60 * 60 * 1000;
        String plainText = companyId + "|" + amount + "|" + expireTs + "|" + salt;

        byte[] keyBytes = new byte[16];
        byte[] raw = AES_KEY.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(raw, 0, keyBytes, 0, Math.min(raw.length, 16));

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(encrypted);
    }
}
