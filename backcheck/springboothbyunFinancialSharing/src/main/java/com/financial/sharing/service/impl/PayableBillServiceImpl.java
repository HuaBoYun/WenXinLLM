package com.financial.sharing.service.impl;

import com.financial.sharing.dto.param.BillEndorseParam;
import com.financial.sharing.dto.param.BillPaymentParam;
import com.financial.sharing.dto.param.PayableBillQueryParam;
import com.financial.sharing.dto.param.PayableBillSaveParam;
import com.financial.sharing.exception.ServiceException;
import com.financial.sharing.oracle.entity.TblPayableBill;
import com.financial.sharing.oracle.mapper.PayableBillMapper;
import com.financial.sharing.service.PayableBillService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.BillEndorseVO;
import com.financial.sharing.vo.result.BillPaymentVO;
import com.financial.sharing.vo.result.PayableBillVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 应付票据Service实现类
 * @author system
 * @since 2025-01-05
 */
@Service
public class PayableBillServiceImpl implements PayableBillService {

    @Resource
    private PayableBillMapper payableBillMapper;

    @Override
    public PageResult<PayableBillVO> getPayableBillList(PayableBillQueryParam param) {
        int pageNum = param.getPageNo() != null ? param.getPageNo() : 0;
        int pageSize = param.getPageSize() != null ? param.getPageSize() : 15;

        PageHelper.startPage(pageNum + 1, pageSize);
        List<PayableBillVO> list = payableBillMapper.selectPageList(param);
        com.github.pagehelper.PageInfo<PayableBillVO> pageHelperInfo = new com.github.pagehelper.PageInfo<>(list);

        // 计算剩余天数
        LocalDate today = LocalDate.now();
        for (PayableBillVO vo : list) {
            if (vo.getDueDate() != null) {
                long days = ChronoUnit.DAYS.between(today, vo.getDueDate());
                vo.setRemainingDays((int) days);
            }
        }

        PageResult result = new PageResult();
        result.setTlist(list);
        result.setTotalRecord((int) pageHelperInfo.getTotal());
        result.setCurrentPage(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    public PayableBillVO getDetail(String billId) {
        PayableBillVO vo = payableBillMapper.selectDetailById(billId);
        if (vo == null) {
            throw new ServiceException("票据不存在");
        }

        // 计算剩余天数
        if (vo.getDueDate() != null) {
            long days = ChronoUnit.DAYS.between(LocalDate.now(), vo.getDueDate());
            vo.setRemainingDays((int) days);
            vo.setIsDue(days < 0);
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveOrUpdate(PayableBillSaveParam param) {
        TblPayableBill entity = new TblPayableBill();
        BeanUtils.copyProperties(param, entity);

        if (StringUtils.hasText(param.getBillId())) {
            // 更新
            entity.setUpdateTime(LocalDateTime.now());
            entity.setUpdateBy(param.getCreateBy()); // 临时使用createBy作为updateBy
            payableBillMapper.updateById(entity);
            return param.getBillId();
        } else {
            // 新增
            entity.setBillId(UUID.randomUUID().toString().replace("-", ""));
            entity.setBillNo(generateBillNo());
            entity.setStatus("active");
            entity.setAuditStatus(0);
            entity.setCreateTime(LocalDateTime.now());
            entity.setCreateBy(param.getCreateBy());
            entity.setIsDeleted(0);
            payableBillMapper.insert(entity);
            return entity.getBillId();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String billId) {
        TblPayableBill entity = payableBillMapper.selectById(billId);
        if (entity == null) {
            throw new ServiceException("票据不存在");
        }
        entity.setIsDeleted(1);
        entity.setUpdateTime(LocalDateTime.now());
        payableBillMapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelBill(String billId, String remarks) {
        TblPayableBill entity = payableBillMapper.selectById(billId);
        if (entity == null) {
            throw new ServiceException("票据不存在");
        }
        // 检查票据状态，只有active状态的票据才能作废
        if (!"active".equals(entity.getStatus())) {
            throw new ServiceException("只有有效状态的票据才能作废");
        }
        entity.setStatus("cancelled");
        entity.setRemarks(remarks);
        entity.setUpdateTime(LocalDateTime.now());
        payableBillMapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(String billId, boolean approved, String comments) {
        TblPayableBill entity = payableBillMapper.selectById(billId);
        if (entity == null) {
            throw new ServiceException("票据不存在");
        }
        entity.setAuditStatus(approved ? 1 : 2);
        entity.setAuditTime(LocalDateTime.now());
        entity.setAuditComments(comments);
        entity.setUpdateTime(LocalDateTime.now());
        payableBillMapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void endorse(String billId, BillEndorseParam param) {
        // TODO: 待实现背书功能 - 需要先创建 TblBillEndorse 实体类和 BillEndorseMapper
        throw new ServiceException("背书功能待实现");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payment(String billId, BillPaymentParam param) {
        // TODO: 待实现兑付功能 - 需要先创建 TblBillPayment 实体类和 BillPaymentMapper
        throw new ServiceException("兑付功能待实现");
    }

    @Override
    public List<PayableBillVO> getBillDueReminders(String range, String billType) {
        int days = 7;
        boolean includeOverdue = false;

        if ("overdue".equals(range)) {
            includeOverdue = true;
            days = 0;
        } else if (StringUtils.hasText(range)) {
            try {
                days = Integer.parseInt(range);
            } catch (NumberFormatException e) {
                days = 7;
            }
        }

        List<PayableBillVO> list = payableBillMapper.selectDueReminders(days, includeOverdue, billType);

        // 计算剩余天数
        LocalDate today = LocalDate.now();
        for (PayableBillVO vo : list) {
            if (vo.getDueDate() != null) {
                long daysDiff = ChronoUnit.DAYS.between(today, vo.getDueDate());
                vo.setRemainingDays((int) daysDiff);
            }
        }
        return list;
    }

    @Override
    public Map<String, Object> getBillStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 获取总数
        Integer totalBills = payableBillMapper.countByStatus(null);
        statistics.put("totalBills", totalBills != null ? totalBills : 0);

        // 获取即将到期数量(7天内)
        Integer dueBills = payableBillMapper.countDueSoon(7);
        statistics.put("dueBills", dueBills != null ? dueBills : 0);

        // 获取总金额
        BigDecimal totalAmount = payableBillMapper.sumAmount();
        statistics.put("totalAmount", totalAmount != null ? totalAmount : BigDecimal.ZERO);

        // 获取已兑付数量
        Integer paidBills = payableBillMapper.countByStatus("paid");
        statistics.put("paidBills", paidBills != null ? paidBills : 0);

        return statistics;
    }

    @Override
    public List<BillEndorseVO> getEndorseList() {
        // TODO: 待实现背书列表查询功能 - 需要先创建 TblBillEndorse 实体类和 BillEndorseMapper
        return new ArrayList<>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveEndorse(String endorseId, boolean approved, String comments) {
        // TODO: 待实现背书审批功能 - 需要先创建 TblBillEndorse 实体类和 BillEndorseMapper
        throw new ServiceException("背书审批功能待实现");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelEndorse(String endorseId) {
        // TODO: 待实现背书撤销功能 - 需要先创建 TblBillEndorse 实体类和 BillEndorseMapper
        throw new ServiceException("背书撤销功能待实现");
    }

    @Override
    public List<BillPaymentVO> getPaymentList() {
        // TODO: 待实现兑付列表查询功能 - 需要先创建 TblBillPayment 实体类和 BillPaymentMapper
        return new ArrayList<>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmPayment(String paymentId, Map<String, Object> confirmData) {
        // TODO: 待实现兑付确认功能 - 需要先创建 TblBillPayment 实体类和 BillPaymentMapper
        throw new ServiceException("兑付确认功能待实现");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchOperation(String operation, List<String> billIds, Map<String, Object> operationData) {
        if (billIds == null || billIds.isEmpty()) {
            throw new ServiceException("请选择要操作的票据");
        }

        for (String billId : billIds) {
            switch (operation) {
                case "audit":
                    boolean approved = (boolean) operationData.getOrDefault("approved", true);
                    String comments = (String) operationData.get("comments");
                    audit(billId, approved, comments);
                    break;
                case "endorse":
                    // 批量背书逻辑需要特殊处理
                    break;
                case "delete":
                    delete(billId);
                    break;
                default:
                    throw new ServiceException("不支持的操作类型: " + operation);
            }
        }
    }

    @Override
    public void exportBills(Map<String, Object> params, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            String fileName = URLEncoder.encode("应付票据.xlsx", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);

            // 查询数据
            PayableBillQueryParam param = new PayableBillQueryParam();
            // TODO: 从params中提取查询条件

            List<PayableBillVO> list = payableBillMapper.selectPageList(param);

            // TODO: 使用EasyExcel等工具导出Excel
            try (OutputStream out = response.getOutputStream()) {
                // 临时实现：仅返回JSON数据
                // 实际应使用EasyExcel.write(out, PayableBillVO.class).sheet("票据").doWrite(list);
            }
        } catch (Exception e) {
            throw new ServiceException("导出失败: " + e.getMessage());
        }
    }

    /**
     * 生成票据号
     */
    private String generateBillNo() {
        return "BILL" + System.currentTimeMillis();
    }

    /**
     * 获取背书状态名称
     */
    private String getEndorseStatusName(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "pending": return "待审批";
            case "approved": return "已审批";
            case "rejected": return "已拒绝";
            case "cancelled": return "已撤销";
            default: return "未知";
        }
    }

    /**
     * 获取兑付状态名称
     */
    private String getPaymentStatusName(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "processing": return "处理中";
            case "confirmed": return "已确认";
            case "failed": return "失败";
            default: return "未知";
        }
    }
}
