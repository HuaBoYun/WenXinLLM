package com.financial.sharing.service.impl;

import com.financial.sharing.dto.param.PaymentOrderQueryParam;
import com.financial.sharing.dto.param.PaymentOrderSaveParam;
import com.financial.sharing.dto.param.WriteOffParam;
import com.financial.sharing.exception.ServiceException;
import com.financial.sharing.oracle.entity.TblPaymentOrder;
import com.financial.sharing.oracle.entity.TblWriteOffRecord;
import com.financial.sharing.oracle.mapper.PayableDocumentMapper;
import com.financial.sharing.oracle.mapper.PaymentOrderMapper;
import com.financial.sharing.oracle.mapper.WriteOffRecordMapper;
import com.financial.sharing.service.PaymentOrderService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.PaymentOrderVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 付款单Service实现类
 * @author system
 * @since 2025-01-05
 */
@Service
public class PaymentOrderServiceImpl implements PaymentOrderService {

    @Resource
    private PaymentOrderMapper paymentOrderMapper;
    @Resource
    private PayableDocumentMapper payableDocumentMapper;
    @Resource
    private WriteOffRecordMapper writeOffRecordMapper;

    @Override
    public PageResult<PaymentOrderVO> queryPage(PaymentOrderQueryParam param) {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        List<PaymentOrderVO> list = paymentOrderMapper.selectPageList(param);
        PageInfo<PaymentOrderVO> pageInfo = new PageInfo<>(list);
        return new PageResult<PaymentOrderVO>().build(pageInfo);
    }

    @Override
    public PaymentOrderVO getDetail(String paymentId) {
        PaymentOrderVO vo = paymentOrderMapper.selectDetailById(paymentId);
        if (vo == null) {
            throw new ServiceException("付款单不存在");
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(PaymentOrderSaveParam param) {
        TblPaymentOrder entity = new TblPaymentOrder();
        BeanUtils.copyProperties(param, entity);

        if (StringUtils.hasText(param.getPaymentId())) {
            // 更新操作
            if (!StringUtils.hasText(entity.getSummary())) {
                entity.setSummary("付款单-" + entity.getPaymentNo());
            }
            entity.setUpdateTime(LocalDateTime.now());
            paymentOrderMapper.updateById(entity);
            return param.getPaymentId();
        } else {
            // 新增操作
            entity.setPaymentId(UUID.randomUUID().toString().replace("-", ""));
            entity.setPaymentNo(generatePaymentNo());
            entity.setWriteOffAmount(BigDecimal.ZERO);
            entity.setRemainingAmount(param.getPaymentAmount());
            entity.setPaymentStatus(0);
            entity.setCreateTime(LocalDateTime.now());
            entity.setIsDeleted(0);

            // 自动生成摘要（如果前端没有传递）
            if (!StringUtils.hasText(entity.getSummary())) {
                String paymentMethodText = getPaymentMethodText(param.getPaymentMethod());
                entity.setSummary(paymentMethodText + "付款");
            }
            // 映射 remark 字段到 remarks
            if (StringUtils.hasText(param.getRemark()) && !StringUtils.hasText(entity.getRemarks())) {
                entity.setRemarks(param.getRemark());
            }

            paymentOrderMapper.insert(entity);
            return entity.getPaymentId();
        }
    }

    /**
     * 获取付款方式文本
     */
    private String getPaymentMethodText(Integer paymentMethod) {
        if (paymentMethod == null) {
            return "银行转账";
        }
        switch (paymentMethod) {
            case 1:
                return "银行转账";
            case 2:
                return "现金";
            case 3:
                return "支票";
            case 4:
                return "承兑汇票";
            default:
                return "银行转账";
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String paymentId) {
        TblPaymentOrder entity = paymentOrderMapper.selectById(paymentId);
        if (entity == null) {
            throw new ServiceException("付款单不存在");
        }
        if (entity.getPaymentStatus() == 1) {
            throw new ServiceException("已付款的单据不能删除");
        }
        entity.setIsDeleted(1);
        entity.setUpdateTime(LocalDateTime.now());
        paymentOrderMapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmPayment(String paymentId) {
        TblPaymentOrder entity = paymentOrderMapper.selectById(paymentId);
        if (entity == null) {
            throw new ServiceException("付款单不存在");
        }
        entity.setPaymentStatus(1);
        entity.setPaymentDate(LocalDate.now());
        entity.setUpdateTime(LocalDateTime.now());
        paymentOrderMapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void writeOff(WriteOffParam param) {
        TblPaymentOrder payment = paymentOrderMapper.selectById(param.getPaymentId());
        if (payment == null) {
            throw new ServiceException("付款单不存在");
        }
        BigDecimal totalWriteOff = BigDecimal.ZERO;
        for (WriteOffParam.WriteOffDetailParam detail : param.getDetails()) {
            TblWriteOffRecord record = new TblWriteOffRecord();
            record.setWriteOffId(UUID.randomUUID().toString().replace("-", ""));
            record.setPaymentId(param.getPaymentId());
            record.setDocumentId(detail.getDocumentId());
            record.setWriteOffAmount(detail.getWriteOffAmount());
            record.setWriteOffDate(param.getWriteOffDate() != null ? param.getWriteOffDate() : LocalDate.now());
            record.setWriteOffStatus(1);
            record.setCreateTime(LocalDateTime.now());
            record.setIsDeleted(0);
            writeOffRecordMapper.insert(record);
            totalWriteOff = totalWriteOff.add(detail.getWriteOffAmount());
        }
        paymentOrderMapper.updateWriteOffAmount(param.getPaymentId(), 
            payment.getWriteOffAmount().add(totalWriteOff));
    }

    @Override
    public List<PaymentOrderVO> queryBySupplier(String supplierId) {
        return paymentOrderMapper.selectBySupplier(supplierId);
    }

    private String generatePaymentNo() {
        return "PAY" + System.currentTimeMillis();
    }
}

