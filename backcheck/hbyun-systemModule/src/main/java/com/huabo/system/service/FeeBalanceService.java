package com.huabo.system.service;

import com.hbfk.util.JsonBean;
import java.math.BigDecimal;

/**
 * 余额管理服务
 */
public interface FeeBalanceService {
    JsonBean queryBalance(String token, BigDecimal companyOrgId) throws Exception;
    JsonBean initBalance(BigDecimal companyOrgId) throws Exception;
    JsonBean checkBalance(String token) throws Exception;
}
