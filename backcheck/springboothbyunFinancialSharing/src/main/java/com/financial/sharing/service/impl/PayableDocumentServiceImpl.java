package com.financial.sharing.service.impl;

import com.financial.sharing.dto.param.PayableDocumentQueryParam;
import com.financial.sharing.dto.param.PayableDocumentSaveParam;
import com.financial.sharing.exception.ServiceException;
import com.financial.sharing.oracle.entity.TblPayableDocument;
import com.financial.sharing.oracle.mapper.PayableDocumentMapper;
import com.financial.sharing.service.PayableDocumentService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.PayableDocumentVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 应付单据Service实现类 (使用Oracle/达梦数据库)
 * @author system
 * @since 2025-01-05
 */
@Service
public class PayableDocumentServiceImpl implements PayableDocumentService {

    @Resource(name = "oraclePayableDocumentMapper")
    private PayableDocumentMapper payableDocumentMapper;

    @Override
    public PageResult<PayableDocumentVO> queryPage(PayableDocumentQueryParam param) {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        List<PayableDocumentVO> list = payableDocumentMapper.selectPageList(param);
        PageInfo<PayableDocumentVO> pageInfo = new PageInfo<>(list);
        return new PageResult<PayableDocumentVO>().build(pageInfo);
    }

    @Override
    public PayableDocumentVO getDetail(String documentId) {
        PayableDocumentVO vo = payableDocumentMapper.selectDetailById(documentId);
        if (vo == null) {
            throw new ServiceException("应付单据不存在");
        }

        // 处理状态名称
        if (vo.getDocumentStatus() != null) {
            String[] statusNames = {"草稿", "待审核", "已审核", "已拒绝"};
            vo.setDocumentStatusName(statusNames[vo.getDocumentStatus()]);
        }

        // 处理业务类型名称
        if (vo.getBusinessType() != null) {
            String[] businessTypeNames = {"采购应付", "费用应付", "其他应付"};
            vo.setBusinessTypeName(businessTypeNames[vo.getBusinessType() - 1]);
        }

        // 处理逾期状态
        if (vo.getDueDate() != null && vo.getDocumentStatus() != 2) {
            // 只有未审核完成的单据才判断逾期
            java.time.LocalDate today = java.time.LocalDate.now();
            if (vo.getDueDate().isBefore(today)) {
                vo.setOverdue(true);
                vo.setOverdueDays((int) java.time.temporal.ChronoUnit.DAYS.between(vo.getDueDate(), today));
            } else {
                vo.setOverdue(false);
                vo.setOverdueDays(0);
            }
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(PayableDocumentSaveParam param) {
        TblPayableDocument entity = new TblPayableDocument();
        BeanUtils.copyProperties(param, entity);
        
        if (StringUtils.hasText(param.getDocumentId())) {
            // 更新
            entity.setUpdateTime(LocalDateTime.now());
            payableDocumentMapper.updateById(entity);
            return param.getDocumentId();
        } else {
            // 新增
            entity.setDocumentId(UUID.randomUUID().toString().replace("-", ""));
            entity.setDocumentNo(generateDocumentNo());
            entity.setPaidAmount(BigDecimal.ZERO);
            entity.setRemainingAmount(param.getPayableAmount());
            entity.setDocumentStatus(0); // 草稿
            entity.setCreateTime(LocalDateTime.now());
            entity.setIsDeleted(0);
            payableDocumentMapper.insert(entity);
            return entity.getDocumentId();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String documentId) {
        TblPayableDocument entity = payableDocumentMapper.selectById(documentId);
        if (entity == null) {
            throw new ServiceException("应付单据不存在");
        }
        if (entity.getDocumentStatus() == 2) {
            throw new ServiceException("已审核的单据不能删除");
        }
        entity.setIsDeleted(1);
        entity.setUpdateTime(LocalDateTime.now());
        payableDocumentMapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(String documentId, boolean approved, String comments) {
        TblPayableDocument entity = payableDocumentMapper.selectById(documentId);
        if (entity == null) {
            throw new ServiceException("应付单据不存在");
        }
        if (entity.getDocumentStatus() != 1) {
            throw new ServiceException("只有待审核的单据才能审核");
        }
        entity.setDocumentStatus(approved ? 2 : 3);
        entity.setAuditTime(LocalDateTime.now());
        entity.setAuditComments(comments);
        entity.setUpdateTime(LocalDateTime.now());
        payableDocumentMapper.updateById(entity);
    }

    @Override
    public List<PayableDocumentVO> queryOverdueList() {
        return payableDocumentMapper.selectOverdueList();
    }

    @Override
    public List<PayableDocumentVO> queryBySupplier(String supplierId) {
        return payableDocumentMapper.selectBySupplier(supplierId);
    }

    private String generateDocumentNo() {
        return "AP" + System.currentTimeMillis();
    }
}

