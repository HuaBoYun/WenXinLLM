package com.global.treasurer.service.impl;

import com.global.treasurer.entity.FundTransfer;
import com.global.treasurer.mapper.FundTransferMapper;
import com.global.treasurer.service.FundTransferService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 资金调拨服务实现
 *
 * @author AI Developer
 * @date 2025-01-15
 */
@Service
public class FundTransferServiceImpl implements FundTransferService {
    @Resource
    private FundTransferMapper fundTransferMapper;

    @Resource
    private UserProvider userProvider;

    @Override
    public Map<String, Object> getTransferPage(Map<String, Object> param) {
        try {
            // 安全获取分页参数,设置默认值
            int pageNum = 1;
            int pageSize = 10;

            if (param != null) {
                // 尝试获取pageNum/page
                if (param.get("pageNum") != null) {
                    try {
                        pageNum = Integer.parseInt(param.get("pageNum").toString());
                    } catch (NumberFormatException e) {
                        // 使用默认值
                    }
                }
                if (param.get("page") != null) {
                    try {
                        pageNum = Integer.parseInt(param.get("page").toString());
                    } catch (NumberFormatException e) {
                        // 使用默认值
                    }
                }

                // 尝试获取pageSize/limit
                if (param.get("pageSize") != null) {
                    try {
                        pageSize = Integer.parseInt(param.get("pageSize").toString());
                    } catch (NumberFormatException e) {
                        // 使用默认值
                    }
                }
                if (param.get("limit") != null) {
                    try {
                        pageSize = Integer.parseInt(param.get("limit").toString());
                    } catch (NumberFormatException e) {
                        // 使用默认值
                    }
                }
            }

            // 使用PageHelper分页
            com.github.pagehelper.PageHelper.startPage(pageNum, pageSize);

            List<FundTransfer> list = fundTransferMapper.selectPage(param);

            // PageInfo格式
            com.github.pagehelper.PageInfo<FundTransfer> pageInfo = new com.github.pagehelper.PageInfo<>(list);

            Map<String, Object> result = new HashMap<>();
            result.put("list", list);
            result.put("total", pageInfo.getTotal());
            result.put("pageNo", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            // 返回空数据而不是抛出异常
            Map<String, Object> result = new HashMap<>();
            result.put("list", new ArrayList<>());
            result.put("total", 0);
            result.put("pageNo", 1);
            result.put("pageSize", 10);
            return result;
        }
    }

    @Override
    public FundTransfer getTransferById(Long transferId) {
        return fundTransferMapper.selectById(transferId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createTransfer(FundTransfer transfer) {
        // 添加调试日志：打印接收到的数据
        System.out.println("========== createTransfer 接收到的数据 ==========");
        System.out.println("transfer对象: " + transfer);
        System.out.println("fromAccountId: " + (transfer != null ? transfer.getFromAccountId() : "transfer is null"));
        System.out.println("toAccountId: " + (transfer != null ? transfer.getToAccountId() : "transfer is null"));
        System.out.println("fromAccountNumber: " + (transfer != null ? transfer.getFromAccountNumber() : "transfer is null"));
        System.out.println("toAccountNumber: " + (transfer != null ? transfer.getToAccountNumber() : "transfer is null"));
        System.out.println("transferAmount: " + (transfer != null ? transfer.getTransferAmount() : "transfer is null"));
        System.out.println("transferReason: " + (transfer != null ? transfer.getTransferReason() : "transfer is null"));
        System.out.println("=====================================================");

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

        // 从用户信息中获取当前组织ID并设置到transfer对象
        if (transfer.getOrgId() == null) {
            if (loginStaff.getCurrentOrg() == null) {
                throw new RuntimeException("用户当前组织信息获取失败，无法创建资金调拨记录");
            }
            java.math.BigDecimal orgIdBigDecimal = loginStaff.getCurrentOrg().getOrgid();
            if (orgIdBigDecimal == null) {
                throw new RuntimeException("用户当前组织ID获取失败，无法创建资金调拨记录");
            }
            transfer.setOrgId(orgIdBigDecimal.longValue());
        }

        // 生成主键ID（使用时间戳+随机数）
        if (transfer.getTransferId() == null) {
            transfer.setTransferId(System.currentTimeMillis() + (long)(Math.random() * 10000));
        }

        // 生成调拨单号
        if (transfer.getTransferNo() == null || transfer.getTransferNo().isEmpty()) {
            transfer.setTransferNo(generateTransferNo(transfer.getOrgId()));
        }

        // 设置默认状态
        if (transfer.getTransferStatus() == null || transfer.getTransferStatus().isEmpty()) {
            transfer.setTransferStatus("PENDING");
        }

        // 设置删除标志
        if (transfer.getDeleteFlag() == null) {
            transfer.setDeleteFlag(0);
        }

        // 设置创建人信息
        if (transfer.getCreatedBy() == null) {
            transfer.setCreatedBy(loginStaff.getStaffid().longValue());
        }
        transfer.setCreatedByName(loginStaff.getRealname());

        // 设置创建时间
        if (transfer.getCreatedTime() == null) {
            transfer.setCreatedTime(new Date());
        }

        // 设置申请日期（如果未设置）
        if (transfer.getApplyDate() == null) {
            transfer.setApplyDate(new Date());
        }

        // 设置币种（如果未设置）
        if (transfer.getCurrencyCode() == null || transfer.getCurrencyCode().isEmpty()) {
            transfer.setCurrencyCode("CNY");
        }

        return fundTransferMapper.insert(transfer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTransfer(FundTransfer transfer) {
        TblStaffUtil loginStaff;
        try {
            loginStaff = userProvider.get();
        } catch (Exception e) {
            throw new RuntimeException("用户信息获取失败", e);
        }

        if (loginStaff == null) {
            throw new RuntimeException("用户信息获取失败");
        }

        // BigDecimal转Long
        transfer.setUpdatedBy(loginStaff.getStaffid().longValue());
        transfer.setUpdatedByName(loginStaff.getRealname());
        transfer.setUpdatedTime(new Date());

        return fundTransferMapper.updateById(transfer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<Long> ids) {
        return fundTransferMapper.batchDelete(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchSubmit(List<Long> ids, String submitByName) {
        return fundTransferMapper.batchUpdateStatus(ids, "APPROVING", submitByName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchExecute(List<Long> ids, String executeByName) {
        return fundTransferMapper.batchUpdateStatus(ids, "EXECUTING", executeByName);
    }

    @Override
    public String generateTransferNo(Long orgId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        // 简化版：实际应使用序列或雪花算法
        return "TRN" + dateStr + String.format("%04d", new Random().nextInt(10000));
    }

    @Override
    public Map<String, Object> getTransferStatistics(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        // 获取orgId
        Long orgId = params.get("orgId") != null ? Long.parseLong(params.get("orgId").toString()) : null;

        // 构建查询条件
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("orgId", orgId);
        queryParam.put("deleteFlag", 0);

        // 查询所有数据(不分页)
        com.github.pagehelper.PageHelper.startPage(1, Integer.MAX_VALUE);
        List<FundTransfer> allList = fundTransferMapper.selectPage(queryParam);
        com.github.pagehelper.PageInfo<FundTransfer> pageInfo = new com.github.pagehelper.PageInfo<>(allList);

        // 统计数据
        int totalTransfers = (int) pageInfo.getTotal();
        int pendingTransfers = 0;
        int completedTransfers = 0;
        java.math.BigDecimal totalAmount = java.math.BigDecimal.ZERO;

        for (FundTransfer item : allList) {
            // 统计状态
            if ("PENDING".equals(item.getTransferStatus())) {
                pendingTransfers++;
            } else if ("COMPLETED".equals(item.getTransferStatus())) {
                completedTransfers++;
            }

            // 累加金额
            if (item.getTransferAmount() != null) {
                totalAmount = totalAmount.add(item.getTransferAmount());
            }
        }

        // 计算成功率
        double successRate = 0.0;
        if (totalTransfers > 0) {
            successRate = (completedTransfers * 100.0) / totalTransfers;
        }

        // 组装结果
        result.put("totalTransfers", totalTransfers);
        result.put("pendingTransfers", pendingTransfers);
        result.put("completedTransfers", completedTransfers);
        result.put("successRate", successRate);
        result.put("totalAmount", totalAmount);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchApprove(List<Long> ids, String approveByName, String approvalComment) {
        return fundTransferMapper.batchApprove(ids, "APPROVED", approveByName, approvalComment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelTransfer(Long transferId, String cancelByName) {
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

        // 如果未指定取消人，使用当前登录用户
        if (cancelByName == null || cancelByName.isEmpty()) {
            cancelByName = loginStaff.getRealname();
        }

        // 调用Mapper方法取消调拨
        int result = fundTransferMapper.cancelTransfer(transferId, cancelByName);

        if (result == 0) {
            throw new RuntimeException("取消失败：调拨记录不存在或状态不允许取消");
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int approveTransfer(Long transferId, String status, String approveByName, String approvalComment) {
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

        // 如果未指定审批人，使用当前登录用户
        if (approveByName == null || approveByName.isEmpty()) {
            approveByName = loginStaff.getRealname();
        }

        // 验证审批状态
        if (!"APPROVED".equals(status) && !"REJECTED".equals(status)) {
            throw new RuntimeException("无效的审批状态：" + status);
        }

        // 调用Mapper方法审批调拨
        int result = fundTransferMapper.approveTransfer(transferId, status, approveByName, approvalComment);

        if (result == 0) {
            throw new RuntimeException("审批失败：调拨记录不存在或状态不允许审批");
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int executeTransfer(Long transferId, String executeByName) {
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

        // 如果未指定执行人，使用当前登录用户
        if (executeByName == null || executeByName.isEmpty()) {
            executeByName = loginStaff.getRealname();
        }

        // 调用Mapper方法执行调拨
        int result = fundTransferMapper.executeTransfer(transferId, executeByName);

        if (result == 0) {
            throw new RuntimeException("执行失败：调拨记录不存在或状态不允许执行");
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getAvailableAccounts(Map<String, Object> params) {
        try {
            // 获取orgId
            Long orgId = null;
            if (params != null && params.get("orgId") != null) {
                try {
                    orgId = Long.parseLong(params.get("orgId").toString());
                } catch (NumberFormatException e) {
                    // 忽略解析错误
                }
            }

            // 查询可用账户
            List<Map<String, Object>> accounts = fundTransferMapper.selectAvailableAccounts(orgId);

            // 转换字段名，确保大小写正确（达梦数据库可能返回大写字段名）
            List<Map<String, Object>> result = new ArrayList<>();
            if (accounts != null) {
                for (Map<String, Object> account : accounts) {
                    Map<String, Object> newAccount = new HashMap<>();
                    // 手动转换字段名为小写驼峰格式
                    newAccount.put("accountId", account.get("accountId") != null ? account.get("accountId") : account.get("ACCOUNTID"));
                    newAccount.put("accountNumber", account.get("accountNumber") != null ? account.get("accountNumber") : account.get("ACCOUNTNUMBER"));
                    newAccount.put("accountName", account.get("accountName") != null ? account.get("accountName") : account.get("ACCOUNTNAME"));
                    newAccount.put("bankName", account.get("bankName") != null ? account.get("bankName") : account.get("BANKNAME"));
                    newAccount.put("accountType", account.get("accountType") != null ? account.get("accountType") : account.get("ACCOUNTTYPE"));
                    newAccount.put("currencyCode", account.get("currencyCode") != null ? account.get("currencyCode") : account.get("CURRENCYCODE"));
                    newAccount.put("balance", account.get("balance") != null ? account.get("balance") : account.get("BALANCE"));
                    newAccount.put("status", account.get("status") != null ? account.get("status") : account.get("STATUS"));
                    result.add(newAccount);
                }
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            // 发生异常时返回空列表,不抛出异常
            return new ArrayList<>();
        }
    }
}
