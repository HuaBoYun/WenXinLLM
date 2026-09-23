package com.global.treasurer.service.impl;

import com.global.treasurer.entity.CashReceipt;
import com.global.treasurer.mapper.CashReceiptMapper;
import com.global.treasurer.service.CashReceiptService;
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
 * 收款管理服务实现
 *
 * @author AI Developer
 * @date 2025-01-15
 */
@Service
public class CashReceiptServiceImpl implements CashReceiptService {
    private static final Logger log = LoggerFactory.getLogger(CashReceiptServiceImpl.class);

    @Resource
    private CashReceiptMapper cashReceiptMapper;

    @Resource
    private com.global.treasurer.mapper.TblGtAccountInfoMapper tblGtAccountInfoMapper;

    @Resource
    private UserProvider userProvider;

    @Override
    public Map<String, Object> getReceiptPage(Map<String, Object> param) {
        // 使用PageHelper分页
        com.github.pagehelper.PageHelper.startPage(
            Integer.parseInt(param.get("pageNum").toString()),
            Integer.parseInt(param.get("pageSize").toString())
        );

        List<CashReceipt> list = cashReceiptMapper.selectPage(param);

        // PageInfo格式
        com.github.pagehelper.PageInfo<CashReceipt> pageInfo = new com.github.pagehelper.PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("tlist", list);
        result.put("totalRecord", pageInfo.getTotal());
        result.put("pageNo", pageInfo.getPageNum());
        result.put("pageSize", pageInfo.getPageSize());

        return result;
    }

    @Override
    public CashReceipt getReceiptById(Long receiptId) {
        return cashReceiptMapper.selectById(receiptId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createReceipt(CashReceipt receipt) {
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

        // 验证accountId不能为空
        if (receipt.getAccountId() == null) {
            throw new RuntimeException("收款账户不能为空");
        }

        // 设置组织ID
        receipt.setOrgId(loginStaff.getCurrentOrg().getOrgid().longValue());
        // 生成收款单号
        receipt.setReceiptNo(generateReceiptNo(receipt.getOrgId()));
        // 设置收款日期（如果前端没有传，使用当前日期）
        if (receipt.getReceiptDate() == null) {
            receipt.setReceiptDate(new Date());
        }
        // 根据accountId查询账户名称
        // Long类型转BigDecimal类型
        java.math.BigDecimal accountIdBig = java.math.BigDecimal.valueOf(receipt.getAccountId());
        com.global.treasurer.entity.TblGtAccountInfo account =
            tblGtAccountInfoMapper.selectById(accountIdBig);
        if (account != null) {
            receipt.setAccountName(account.getAccountName());
        }
        receipt.setReceiptStatus("PENDING");
        receipt.setDeleteFlag(0);
        // BigDecimal转Long
        receipt.setCreatedBy(loginStaff.getStaffid().longValue());
        receipt.setCreatedByName(loginStaff.getRealname());
        receipt.setCreatedTime(new Date());

        return cashReceiptMapper.insert(receipt);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateReceipt(CashReceipt receipt) {
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
        if (receipt.getAccountId() != null) {
            java.math.BigDecimal accountIdBig = java.math.BigDecimal.valueOf(receipt.getAccountId());
            com.global.treasurer.entity.TblGtAccountInfo account =
                tblGtAccountInfoMapper.selectById(accountIdBig);
            if (account != null) {
                receipt.setAccountName(account.getAccountName());
            }
        }

        // BigDecimal转Long
        receipt.setUpdatedBy(loginStaff.getStaffid().longValue());
        receipt.setUpdatedByName(loginStaff.getRealname());
        receipt.setUpdatedTime(new Date());

        return cashReceiptMapper.updateById(receipt);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<Long> ids) {
        return cashReceiptMapper.batchDelete(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchConfirm(List<Long> ids, String confirmByName) {
        return cashReceiptMapper.batchUpdateStatus(ids, "CONFIRMED", confirmByName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int verifyReceipt(Long receiptId, String verifyUser) {
        CashReceipt receipt = new CashReceipt();
        receipt.setReceiptId(receiptId);
        receipt.setReceiptStatus("VERIFIED");
        receipt.setUpdatedByName(verifyUser);
        receipt.setUpdatedTime(new Date());
        return cashReceiptMapper.updateById(receipt);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelReceipt(Long receiptId, String updateUser) {
        CashReceipt receipt = new CashReceipt();
        receipt.setReceiptId(receiptId);
        receipt.setReceiptStatus("CANCELLED");
        receipt.setUpdatedByName(updateUser);
        receipt.setUpdatedTime(new Date());
        return cashReceiptMapper.updateById(receipt);
    }

    @Override
    public String generateReceiptNo(Long orgId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        // 简化版：实际应使用序列或雪花算法
        return "REC" + dateStr + String.format("%04d", new Random().nextInt(10000));
    }

    @Override
    public void exportReceipts(Map<String, Object> params, HttpServletResponse response) {
        // 查询所有符合条件的收款单（不分页）
        params.put("pageNum", 1);
        params.put("pageSize", 100000); // 设置一个足够大的值

        Map<String, Object> pageResult = getReceiptPage(params);
        List<com.global.treasurer.entity.CashReceipt> receiptList = (List<com.global.treasurer.entity.CashReceipt>) pageResult.get("tlist");

        if (receiptList == null) {
            receiptList = new java.util.ArrayList<>();
        }

        log.info("导出收款单数据，数据量: {}", receiptList.size());

        // 使用EasyExcel导出
        com.global.treasurer.util.ExcelUtil.exportExcel(response, receiptList, com.global.treasurer.entity.CashReceipt.class,
                "收款单数据_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()), "收款单");
    }

    @Override
    public void exportReceiptById(Long receiptId, HttpServletResponse response) {
        com.global.treasurer.entity.CashReceipt receipt = getReceiptById(receiptId);

        List<com.global.treasurer.entity.CashReceipt> receiptList = new java.util.ArrayList<>();
        receiptList.add(receipt);

        // 使用EasyExcel导出
        com.global.treasurer.util.ExcelUtil.exportExcel(response, receiptList, com.global.treasurer.entity.CashReceipt.class,
                "收款单_" + receipt.getReceiptNo() + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()), "收款单");
    }
}
