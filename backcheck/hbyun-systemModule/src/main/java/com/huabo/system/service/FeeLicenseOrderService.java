package com.huabo.system.service;

import com.hbfk.util.JsonBean;
import java.math.BigDecimal;
import javax.servlet.http.HttpServletResponse;

/**
 * 密钥购买订单服务
 */
public interface FeeLicenseOrderService {
    JsonBean submitOrder(String token, BigDecimal purchaseAmount) throws Exception;
    JsonBean getOrderList(String token, Integer status, String startTime, String endTime, Integer pageNum, Integer pageSize) throws Exception;
    JsonBean getOrderDetail(String token, BigDecimal orderId) throws Exception;
    JsonBean approveOrder(String token, BigDecimal orderId, boolean approved, String remark) throws Exception;
    JsonBean generateKey(String token, BigDecimal orderId) throws Exception;
    void exportOrders(String token, Integer status, String startTime, String endTime, HttpServletResponse response) throws Exception;
}
