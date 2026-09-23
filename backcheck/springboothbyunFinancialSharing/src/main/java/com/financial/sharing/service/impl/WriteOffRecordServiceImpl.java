package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.financial.sharing.dto.param.WriteOffRecordQueryParam;
import com.financial.sharing.oracle.entity.TblPaymentOrder;
import com.financial.sharing.oracle.entity.TblPayableDocument;
import com.financial.sharing.oracle.entity.TblWriteOffRecord;
import com.financial.sharing.oracle.mapper.PayableDocumentMapper;
import com.financial.sharing.oracle.mapper.PaymentOrderMapper;
import com.financial.sharing.oracle.mapper.WriteOffRecordMapper;
import com.financial.sharing.service.WriteOffRecordService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.WriteOffRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 核销记录Service实现类
 * @author system
 * @since 2025-01-13
 */
@Slf4j
@Service
public class WriteOffRecordServiceImpl implements WriteOffRecordService {

    @Resource
    private WriteOffRecordMapper writeOffRecordMapper;

    @Resource
    private PaymentOrderMapper paymentOrderMapper;

    @Resource
    private PayableDocumentMapper payableDocumentMapper;

    @Override
    public PageResult<WriteOffRecordVO> queryPage(WriteOffRecordQueryParam param) {
        log.info("分页查询核销记录，参数: {}", param);

        // 计算偏移量
        int offset = (param.getPageNo() - 1) * param.getPageSize();

        // 查询核销记录列表
        List<WriteOffRecordVO> list = writeOffRecordMapper.selectWriteOffRecordPage(
            param, offset, param.getPageSize()
        );

        // 查询总数
        Long total = writeOffRecordMapper.selectWriteOffRecordCount(param);

        // 构建分页结果
        PageResult<WriteOffRecordVO> pageResult = new PageResult<>();
        pageResult.setTlist(list);
        pageResult.setTotalRecord(total.intValue());
        pageResult.setCurrentPage(param.getPageNo());
        pageResult.setPageSize(param.getPageSize());
        pageResult.setTotalPage((int) Math.ceil((double) total / param.getPageSize()));

        return pageResult;
    }

    @Override
    public WriteOffRecordVO getDetail(String writeOffId) {
        log.info("查询核销记录详情，writeOffId: {}", writeOffId);

        return writeOffRecordMapper.selectWriteOffById(writeOffId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reverseWriteOff(String writeOffId) {
        log.info("撤销核销，writeOffId: {}", writeOffId);

        // 1. 查询核销记录
        TblWriteOffRecord writeOffRecord = writeOffRecordMapper.selectById(writeOffId);
        if (writeOffRecord == null) {
            throw new RuntimeException("核销记录不存在");
        }

        // 检查核销状态
        if (writeOffRecord.getWriteOffStatus() == 2) {
            throw new RuntimeException("该核销记录已撤销，无法重复操作");
        }
        if (writeOffRecord.getWriteOffStatus() == 0) {
            throw new RuntimeException("该核销记录尚未核销，无法撤销");
        }

        // 2. 更新核销记录状态为已撤销(2)
        writeOffRecord.setWriteOffStatus(2);
        writeOffRecordMapper.updateById(writeOffRecord);

        // 3. 回退付款单的已核销金额
        if (writeOffRecord.getPaymentId() != null) {
            TblPaymentOrder paymentOrder = paymentOrderMapper.selectById(writeOffRecord.getPaymentId());
            if (paymentOrder != null) {
                BigDecimal newWriteOffAmount = paymentOrder.getWriteOffAmount()
                    .subtract(writeOffRecord.getWriteOffAmount());
                BigDecimal newRemainingAmount = paymentOrder.getRemainingAmount()
                    .add(writeOffRecord.getWriteOffAmount());

                // 校验金额
                if (newWriteOffAmount.compareTo(BigDecimal.ZERO) < 0) {
                    throw new RuntimeException("付款单已核销金额不足，无法撤销");
                }
                paymentOrder.setWriteOffAmount(newWriteOffAmount);
                paymentOrder.setRemainingAmount(newRemainingAmount);
                paymentOrderMapper.updateById(paymentOrder);

                log.info("回退付款单核销金额成功，paymentId: {}, 核销金额: {}",
                    paymentOrder.getPaymentId(), writeOffRecord.getWriteOffAmount());
            } else {
                log.warn("付款单不存在，paymentId: {}", writeOffRecord.getPaymentId());
            }
        }

        // 4. 回退应付单的已付金额
        if (writeOffRecord.getDocumentId() != null) {
            TblPayableDocument payableDocument = payableDocumentMapper.selectById(writeOffRecord.getDocumentId());
            if (payableDocument != null) {
                BigDecimal newPaidAmount = payableDocument.getPaidAmount()
                    .subtract(writeOffRecord.getWriteOffAmount());
                BigDecimal newRemainingAmount = payableDocument.getPayableAmount()
                    .subtract(newPaidAmount);

                // 校验金额
                if (newPaidAmount.compareTo(BigDecimal.ZERO) < 0) {
                    throw new RuntimeException("应付单已付金额不足，无法撤销");
                }
                payableDocument.setPaidAmount(newPaidAmount);
                payableDocument.setRemainingAmount(newRemainingAmount);
                payableDocumentMapper.updateById(payableDocument);

                log.info("回退应付单已付金额成功，documentId: {}, 核销金额: {}",
                    payableDocument.getDocumentId(), writeOffRecord.getWriteOffAmount());
            } else {
                log.warn("应付单不存在，documentId: {}", writeOffRecord.getDocumentId());
            }
        }

        log.info("撤销核销成功，writeOffId: {}", writeOffId);
    }
}
