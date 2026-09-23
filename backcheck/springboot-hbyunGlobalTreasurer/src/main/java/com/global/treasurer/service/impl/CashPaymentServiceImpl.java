package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.CashPayment;
import com.global.treasurer.mapper.CashPaymentMapper;
import com.global.treasurer.service.CashPaymentService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 付款管理服务实现
 *
 * @author AI Developer
 * @date 2025-01-15
 */
@Service
public class CashPaymentServiceImpl implements CashPaymentService {
    private static final Logger log = LoggerFactory.getLogger(CashPaymentServiceImpl.class);

    @Resource
    private CashPaymentMapper cashPaymentMapper;

    @Resource
    private com.global.treasurer.mapper.TblGtAccountInfoMapper tblGtAccountInfoMapper;

    @Resource
    private UserProvider userProvider;

    @Override
    public Map<String, Object> getPaymentPage(Map<String, Object> param) {
        // 使用PageHelper分页
        com.github.pagehelper.PageHelper.startPage(
            Integer.parseInt(param.get("pageNum").toString()),
            Integer.parseInt(param.get("pageSize").toString())
        );

        List<CashPayment> list = cashPaymentMapper.selectPaymentPage(param);

        // PageInfo格式
        com.github.pagehelper.PageInfo<CashPayment> pageInfo = new com.github.pagehelper.PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("tlist", list);
        result.put("totalRecord", pageInfo.getTotal());
        result.put("pageNo", pageInfo.getPageNum());
        result.put("pageSize", pageInfo.getPageSize());

        return result;
    }

    @Override
    public CashPayment getPaymentById(Long paymentId) {
        return cashPaymentMapper.selectById(paymentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createPayment(CashPayment payment) {
        // 获取当前用户信息
        TblStaffUtil loginStaff;
        try {
            loginStaff = userProvider.get();
        } catch (Exception e) {
            throw new RuntimeException("用户信息获取失败", e);
        }

        if (loginStaff == null) {
            throw new RuntimeException("用户信息获取失败");
        }

        // 设置组织ID
        payment.setOrgId(loginStaff.getCurrentOrg().getOrgid().longValue());
        // 生成付款单号
        payment.setPaymentNo(generatePaymentNo(payment.getOrgId()));
        // 设置付款日期（如果前端没有传，使用当前日期）
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(new Date());
        }
        // 根据accountId查询账户名称
        if (payment.getAccountId() != null) {
            // Long类型转BigDecimal类型
            java.math.BigDecimal accountIdBig = java.math.BigDecimal.valueOf(payment.getAccountId());
            com.global.treasurer.entity.TblGtAccountInfo account =
                tblGtAccountInfoMapper.selectById(accountIdBig);
            if (account != null) {
                payment.setAccountName(account.getAccountName());
            }
        }
        payment.setPaymentStatus("PENDING");
        payment.setDeleteFlag(0);
        // BigDecimal转Long
        payment.setCreatedBy(loginStaff.getStaffid().longValue());
        payment.setCreatedByName(loginStaff.getRealname());
        payment.setCreatedTime(new Date());

        return cashPaymentMapper.insert(payment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePayment(CashPayment payment) {
        TblStaffUtil loginStaff;
        try {
            loginStaff = userProvider.get();
        } catch (Exception e) {
            throw new RuntimeException("用户信息获取失败", e);
        }

        if (loginStaff == null) {
            throw new RuntimeException("用户信息获取失败");
        }

        // 根据accountId查询账户名称（如果accountId有值且accountName为空，或者accountId被修改了）
        if (payment.getAccountId() != null) {
            java.math.BigDecimal accountIdBig = java.math.BigDecimal.valueOf(payment.getAccountId());
            com.global.treasurer.entity.TblGtAccountInfo account =
                tblGtAccountInfoMapper.selectById(accountIdBig);
            if (account != null) {
                payment.setAccountName(account.getAccountName());
            }
        }

        // BigDecimal转Long
        payment.setUpdatedBy(loginStaff.getStaffid().longValue());
        payment.setUpdatedByName(loginStaff.getRealname());
        payment.setUpdatedTime(new Date());

        return cashPaymentMapper.updateById(payment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<Long> ids) {
        return cashPaymentMapper.batchDelete(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchSubmit(List<Long> ids, String updateByName) {
        return cashPaymentMapper.batchUpdateStatus(ids, "APPROVING", updateByName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchExecute(List<Long> ids, String executeByName) {
        return cashPaymentMapper.batchUpdateStatus(ids, "EXECUTING", executeByName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelPayment(Long paymentId, String updateUser) {
        CashPayment payment = new CashPayment();
        payment.setPaymentId(paymentId);
        payment.setPaymentStatus("CANCELLED");
        payment.setUpdatedByName(updateUser);
        payment.setUpdatedTime(new Date());
        return cashPaymentMapper.updateById(payment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int rejectPayment(Long paymentId, String rejectUser, String rejectReason) {
        CashPayment payment = new CashPayment();
        payment.setPaymentId(paymentId);
        payment.setPaymentStatus("REJECTED");
        payment.setRemark(rejectReason);
        payment.setUpdatedByName(rejectUser);
        payment.setUpdatedTime(new Date());
        return cashPaymentMapper.updateById(payment);
    }

    @Override
    public String generatePaymentNo(Long orgId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        // 简化版：实际应使用序列或雪花算法
        return "PAY" + dateStr + String.format("%04d", new Random().nextInt(10000));
    }

    @Override
    public void exportPayments(Map<String, Object> params, HttpServletResponse response) {
        // 查询所有符合条件的付款单（不分页）
        params.put("pageNum", 1);
        params.put("pageSize", 100000); // 设置一个足够大的值

        Map<String, Object> pageResult = getPaymentPage(params);
        List<CashPayment> paymentList = (List<CashPayment>) pageResult.get("tlist");

        if (paymentList == null) {
            paymentList = new java.util.ArrayList<>();
        }

        log.info("导出付款单数据，数据量: {}", paymentList.size());

        // 使用EasyExcel导出
        com.global.treasurer.util.ExcelUtil.exportExcel(response, paymentList, CashPayment.class,
                "付款单数据_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), "付款单");
    }

    @Override
    public void exportPaymentById(Long paymentId, HttpServletResponse response) {
        CashPayment payment = getPaymentById(paymentId);

        List<CashPayment> paymentList = new ArrayList<>();
        paymentList.add(payment);

        // 使用EasyExcel导出
        com.global.treasurer.util.ExcelUtil.exportExcel(response, paymentList, CashPayment.class,
                "付款单_" + payment.getPaymentNo() + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), "付款单");
    }
}
