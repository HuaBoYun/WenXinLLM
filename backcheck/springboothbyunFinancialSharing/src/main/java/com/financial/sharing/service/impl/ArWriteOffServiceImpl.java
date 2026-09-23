package com.financial.sharing.service.impl;

import com.financial.sharing.oracle.entity.ArReceiptEntity;
import com.financial.sharing.oracle.entity.ArReceivableEntity;
import com.financial.sharing.oracle.entity.ArWriteOffEntity;
import com.financial.sharing.oracle.mapper.ArReceiptMapper;
import com.financial.sharing.oracle.mapper.ArReceivableMapper;
import com.financial.sharing.oracle.mapper.ArWriteOffMapper;
import com.financial.sharing.service.ArWriteOffService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.ArWriteOffQueryParam;
import com.financial.sharing.vo.param.ArWriteOffSaveParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 核销服务实现类
 * @author system
 * @since 2026-01-04
 */
@Slf4j
@Service
public class ArWriteOffServiceImpl implements ArWriteOffService {

    @Resource
    private ArWriteOffMapper arWriteOffMapper;

    @Resource
    private ArReceiptMapper arReceiptMapper;

    @Resource
    private ArReceivableMapper arReceivableMapper;

    @Override
    public MyJsonBean<PageResult> getWriteOffList(ArWriteOffQueryParam param) {
        try {
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            List<ArWriteOffEntity> list = arWriteOffMapper.selectWriteOffList(param);
            PageInfo<ArWriteOffEntity> pageInfo = new PageInfo<>(list);

            PageResult result = new PageResult();
            result.setTlist(pageInfo.getList());
            result.setTotalRecord((int) pageInfo.getTotal());
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询核销记录列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getWriteOffById(String writeOffId) {
        try {
            ArWriteOffEntity entity = arWriteOffMapper.selectById(writeOffId);
            if (entity == null || entity.getIsDeleted() == 1) {
                return MyJsonBean.errorData("核销记录不存在");
            }
            return MyJsonBean.successData(entity);
        } catch (Exception e) {
            log.error("查询核销记录详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean executeWriteOff(ArWriteOffSaveParam param) {
        try {
            // 获取收款单
            ArReceiptEntity receipt = arReceiptMapper.selectById(param.getReceiptId());
            if (receipt == null) {
                return MyJsonBean.errorData("收款单不存在");
            }
            if (receipt.getRemainingAmount().compareTo(BigDecimal.ZERO) <= 0) {
                return MyJsonBean.errorData("收款单无可核销金额");
            }

            // 获取应收单
            ArReceivableEntity receivable = arReceivableMapper.selectById(param.getReceivableId());
            if (receivable == null) {
                return MyJsonBean.errorData("应收单据不存在");
            }
            if (receivable.getRemainingAmount().compareTo(BigDecimal.ZERO) <= 0) {
                return MyJsonBean.errorData("应收单据无待核销金额");
            }

            // 计算核销金额
            BigDecimal writeOffAmount = param.getWriteOffAmount();
            if (writeOffAmount.compareTo(receipt.getRemainingAmount()) > 0) {
                writeOffAmount = receipt.getRemainingAmount();
            }
            if (writeOffAmount.compareTo(receivable.getRemainingAmount()) > 0) {
                writeOffAmount = receivable.getRemainingAmount();
            }

            // 创建核销记录
            ArWriteOffEntity writeOff = new ArWriteOffEntity();
            writeOff.setWriteOffId(UUID.randomUUID().toString().replace("-", ""));
            writeOff.setReceiptId(param.getReceiptId());
            writeOff.setReceivableId(param.getReceivableId());
            writeOff.setWriteOffAmount(writeOffAmount);
            writeOff.setWriteOffDate(LocalDate.now());
            writeOff.setWriteOffType(param.getWriteOffType());
            writeOff.setRemarks(param.getRemarks());
            writeOff.setTenantId(param.getTenantId());
            writeOff.setOrgId(param.getOrgId());
            writeOff.setCreateTime(LocalDateTime.now());
            writeOff.setCreateBy(param.getOperatorId());
            writeOff.setIsDeleted(0);
            arWriteOffMapper.insert(writeOff);

            // 更新收款单已核销金额
            arReceiptMapper.updateWriteOffAmount(param.getReceiptId(), writeOffAmount);

            // 更新应收单已收金额
            arReceivableMapper.updateReceivedAmount(param.getReceivableId(), writeOffAmount);

            return MyJsonBean.successData(writeOff.getWriteOffId());
        } catch (Exception e) {
            log.error("执行核销失败", e);
            return MyJsonBean.errorData("核销失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean autoWriteOff(String customerId, Long tenantId) {
        try {
            // 获取待核销的收款单
            List<ArReceiptEntity> receipts = arReceiptMapper.selectPendingWriteOff(customerId, tenantId);
            // 获取待核销的应收单
            List<ArReceivableEntity> receivables = arReceivableMapper.selectPendingWriteOff(customerId, tenantId);

            List<ArWriteOffEntity> writeOffs = new ArrayList<>();
            int receiptIndex = 0;
            int receivableIndex = 0;

            while (receiptIndex < receipts.size() && receivableIndex < receivables.size()) {
                ArReceiptEntity receipt = receipts.get(receiptIndex);
                ArReceivableEntity receivable = receivables.get(receivableIndex);

                BigDecimal writeOffAmount = receipt.getRemainingAmount().min(receivable.getRemainingAmount());

                // 创建核销记录
                ArWriteOffEntity writeOff = new ArWriteOffEntity();
                writeOff.setWriteOffId(UUID.randomUUID().toString().replace("-", ""));
                writeOff.setReceiptId(receipt.getReceiptId());
                writeOff.setReceivableId(receivable.getReceivableId());
                writeOff.setWriteOffAmount(writeOffAmount);
                writeOff.setWriteOffDate(LocalDate.now());
                writeOff.setWriteOffType(1); // 自动核销
                writeOff.setTenantId(tenantId);
                writeOff.setCreateTime(LocalDateTime.now());
                writeOff.setIsDeleted(0);
                writeOffs.add(writeOff);

                // 更新余额
                receipt.setRemainingAmount(receipt.getRemainingAmount().subtract(writeOffAmount));
                receivable.setRemainingAmount(receivable.getRemainingAmount().subtract(writeOffAmount));

                if (receipt.getRemainingAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    receiptIndex++;
                }
                if (receivable.getRemainingAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    receivableIndex++;
                }
            }

            // 批量保存核销记录
            if (!writeOffs.isEmpty()) {
                arWriteOffMapper.batchInsert(writeOffs);
                // 更新收款单和应收单金额
                for (ArWriteOffEntity wo : writeOffs) {
                    arReceiptMapper.updateWriteOffAmount(wo.getReceiptId(), wo.getWriteOffAmount());
                    arReceivableMapper.updateReceivedAmount(wo.getReceivableId(), wo.getWriteOffAmount());
                }
            }
            return MyJsonBean.successData("自动核销完成，共核销" + writeOffs.size() + "笔");
        } catch (Exception e) {
            log.error("自动核销失败", e);
            return MyJsonBean.errorData("自动核销失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean reverseWriteOff(String receiptId, String operatorId) {
        try {
            // 查询核销记录
            List<ArWriteOffEntity> writeOffs = arWriteOffMapper.selectByReceiptId(receiptId);
            if (writeOffs.isEmpty()) {
                return MyJsonBean.errorData("无核销记录可反核销");
            }

            // 反核销：恢复收款单和应收单金额
            for (ArWriteOffEntity wo : writeOffs) {
                arReceiptMapper.updateWriteOffAmount(wo.getReceiptId(), wo.getWriteOffAmount().negate());
                arReceivableMapper.updateReceivedAmount(wo.getReceivableId(), wo.getWriteOffAmount().negate());
            }

            // 删除核销记录
            arWriteOffMapper.reverseWriteOff(receiptId);

            return MyJsonBean.successData("反核销成功");
        } catch (Exception e) {
            log.error("反核销失败", e);
            return MyJsonBean.errorData("反核销失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getWriteOffsByReceiptId(String receiptId) {
        try {
            List<ArWriteOffEntity> list = arWriteOffMapper.selectByReceiptId(receiptId);
            return MyJsonBean.successData(list);
        } catch (Exception e) {
            log.error("查询收款单核销记录失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getWriteOffsByReceivableId(String receivableId) {
        try {
            List<ArWriteOffEntity> list = arWriteOffMapper.selectByReceivableId(receivableId);
            return MyJsonBean.successData(list);
        } catch (Exception e) {
            log.error("查询应收单核销记录失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getWriteOffStatistics(ArWriteOffQueryParam param) {
        try {
            Map<String, Object> stats = arWriteOffMapper.selectWriteOffStatistics(param);
            return MyJsonBean.successData(stats);
        } catch (Exception e) {
            log.error("查询核销统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean exportWriteOffs(ArWriteOffQueryParam param) {
        return MyJsonBean.successData("导出功能待实现");
    }
}
