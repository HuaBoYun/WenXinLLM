package com.huabo.system.service.impl;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblFeeLicenseOrder;
import com.huabo.system.mapper.TblFeeLicenseOrderMapper;
import com.huabo.system.service.FeeLicenseOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class FeeLicenseOrderServiceImpl implements FeeLicenseOrderService {

    @Resource
    private TblFeeLicenseOrderMapper orderMapper;
    @Resource
    private UserProvider userProvider;

    @Value("${billing.aes-key:HbYunBilling2026}")
    private String aesKey;

    // 最高权限用户名（可看所有记录）
    private static final String ADMIN_USERNAME = "星光";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean submitOrder(String token, BigDecimal purchaseAmount) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if (staff == null) return ResponseFormat.retParam(0, 20006, null);
        if (purchaseAmount == null || purchaseAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return new JsonBean(0, "购买金额必须大于0", null);
        }

        String orderNo = "LO" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + staff.getStaffid();
        TblFeeLicenseOrder order = new TblFeeLicenseOrder();
        order.setId(orderMapper.getNextId());
        order.setOrderNo(orderNo);
        order.setCompanyOrgId(staff.getLinkOrg() != null ? staff.getLinkOrg().getOrgid() : null);
        order.setCompanyName(staff.getLinkOrg() != null ? staff.getLinkOrg().getOrgname() : null);
        order.setPurchaseAmount(purchaseAmount);
        order.setStatus(0);
        order.setApplicantId(staff.getStaffid().toString());
        order.setApplicantName(staff.getRealname());
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        orderMapper.insert(order);

        Map<String, Object> result = new HashMap<>();
        result.put("orderId", order.getId());
        result.put("orderNo", orderNo);
        return ResponseFormat.retParam(1, 200, result);
    }

    @Override
    public JsonBean getOrderList(String token, Integer status, String startTime, String endTime, Integer pageNum, Integer pageSize) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if (staff == null) return ResponseFormat.retParam(0, 20006, null);

        BigDecimal companyOrgId = null;
        // 非最高权限只能看本公司
        if (!ADMIN_USERNAME.equals(staff.getRealname())) {
            companyOrgId = staff.getLinkOrg() != null ? staff.getLinkOrg().getOrgid() : null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = (startTime != null && !startTime.isEmpty()) ? sdf.parse(startTime) : null;
        Date end = (endTime != null && !endTime.isEmpty()) ? new Date(sdf.parse(endTime).getTime() + 86400000L) : null;

        PageHelper.startPage(pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 20);
        List<TblFeeLicenseOrder> list = orderMapper.findOrders(companyOrgId, status, start, end);
        PageInfo<TblFeeLicenseOrder> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("list", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("pageNum", pageInfo.getPageNum());
        result.put("pageSize", pageInfo.getPageSize());
        result.put("isAdmin", ADMIN_USERNAME.equals(staff.getRealname()));
        return ResponseFormat.retParam(1, 200, result);
    }

    @Override
    public JsonBean getOrderDetail(String token, BigDecimal orderId) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if (staff == null) return ResponseFormat.retParam(0, 20006, null);
        TblFeeLicenseOrder order = orderMapper.selectById(orderId);
        if (order == null) return new JsonBean(0, "订单不存在", null);
        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        return ResponseFormat.retParam(1, 200, result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean approveOrder(String token, BigDecimal orderId, boolean approved, String remark) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if (staff == null) return ResponseFormat.retParam(0, 20006, null);
        if (!ADMIN_USERNAME.equals(staff.getRealname())) {
            return new JsonBean(0, "无审批权限", null);
        }
        TblFeeLicenseOrder order = orderMapper.selectById(orderId);
        if (order == null) return new JsonBean(0, "订单不存在", null);
        if (order.getStatus() != 0) return new JsonBean(0, "该订单已处理", null);

        order.setStatus(approved ? 1 : 3);
        order.setApproverId(staff.getStaffid().toString());
        order.setApproverName(staff.getRealname());
        order.setApproveTime(new Date());
        order.setApproveRemark(remark);
        order.setUpdateTime(new Date());
        orderMapper.updateById(order);
        return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean generateKey(String token, BigDecimal orderId) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if (staff == null) return ResponseFormat.retParam(0, 20006, null);
        if (!ADMIN_USERNAME.equals(staff.getRealname())) {
            return new JsonBean(0, "无操作权限", null);
        }
        TblFeeLicenseOrder order = orderMapper.selectById(orderId);
        if (order == null) return new JsonBean(0, "订单不存在", null);
        if (order.getStatus() != 1) return new JsonBean(0, "订单状态不允许生成密钥", null);


        String salt = UUID.randomUUID().toString();
        long expireTs = System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000;
        String plainText = order.getCompanyOrgId().toPlainString() + "|" + order.getPurchaseAmount().toPlainString() + "|" + expireTs + "|" + salt;
        byte[] keyBytes = aesKey.getBytes(StandardCharsets.UTF_8);
        byte[] paddedKey = new byte[16];
        System.arraycopy(keyBytes, 0, paddedKey, 0, Math.min(keyBytes.length, 16));
        SecretKeySpec keySpec = new SecretKeySpec(paddedKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        String licenseKey = Base64.getEncoder().encodeToString(encrypted);
        order.setLicenseKey(licenseKey);
        order.setStatus(2);
        order.setUpdateTime(new Date());
        orderMapper.updateById(order);
        Map<String, Object> result = new HashMap<>();
        result.put("licenseKey", licenseKey);
        return ResponseFormat.retParam(1, 200, result);
    }

    @Override
    public void exportOrders(String token, Integer status, String startTime, String endTime, HttpServletResponse response) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if (staff == null) return;
        BigDecimal companyOrgId = !ADMIN_USERNAME.equals(staff.getRealname()) && staff.getLinkOrg() != null ? staff.getLinkOrg().getOrgid() : null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = (startTime != null && !startTime.isEmpty()) ? sdf.parse(startTime) : null;
        Date end = (endTime != null && !endTime.isEmpty()) ? new Date(sdf.parse(endTime).getTime() + 86400000L) : null;
        List<TblFeeLicenseOrder> list = orderMapper.findOrders(companyOrgId, status, start, end);
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("密钥购买记录");
        XSSFRow header = sheet.createRow(0);
        String[] headers = {"订单编号", "公司名称", "购买金额(元)", "状态", "申请人", "申请时间", "审批人", "审批时间"};
        for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] statusNames = {"待审批", "已审批", "已生成密钥", "已驳回"};
        for (int i = 0; i < list.size(); i++) {
            TblFeeLicenseOrder o = list.get(i);
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(o.getOrderNo() != null ? o.getOrderNo() : "");
            row.createCell(1).setCellValue(o.getCompanyName() != null ? o.getCompanyName() : "");
            row.createCell(2).setCellValue(o.getPurchaseAmount() != null ? o.getPurchaseAmount().doubleValue() : 0);
            row.createCell(3).setCellValue(o.getStatus() != null && o.getStatus() < statusNames.length ? statusNames[o.getStatus()] : "");
            row.createCell(4).setCellValue(o.getApplicantName() != null ? o.getApplicantName() : "");
            row.createCell(5).setCellValue(o.getCreateTime() != null ? dtf.format(o.getCreateTime()) : "");
            row.createCell(6).setCellValue(o.getApproverName() != null ? o.getApproverName() : "");
            row.createCell(7).setCellValue(o.getApproveTime() != null ? dtf.format(o.getApproveTime()) : "");
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("密钥购买记录.xlsx", "UTF-8"));
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}