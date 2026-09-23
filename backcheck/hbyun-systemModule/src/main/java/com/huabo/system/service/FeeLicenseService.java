package com.huabo.system.service;

import com.hbfk.util.JsonBean;
import com.huabo.system.dto.LicenseRechargeDTO;
import com.huabo.system.dto.LicenseGenerateDTO;
import java.math.BigDecimal;

/**
 * 密钥充值服务
 */
public interface FeeLicenseService {
    JsonBean recharge(String token, LicenseRechargeDTO dto) throws Exception;
    JsonBean getRechargeRecords(String token, BigDecimal companyOrgId, Integer pageNum, Integer pageSize) throws Exception;
    JsonBean generateLicense(String token, LicenseGenerateDTO dto) throws Exception;
}
