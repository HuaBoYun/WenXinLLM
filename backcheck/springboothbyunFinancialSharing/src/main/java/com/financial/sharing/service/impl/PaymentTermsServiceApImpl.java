package com.financial.sharing.service.impl;

import com.financial.sharing.dto.param.PaymentTermsQueryParam;
import com.financial.sharing.dto.param.PaymentTermsSaveParam;
import com.financial.sharing.exception.ServiceException;
import com.financial.sharing.oracle.entity.PaymentTermsEntity;
import com.financial.sharing.oracle.mapper.PaymentTermsMapper;
import com.financial.sharing.service.PaymentTermsServiceAp;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.PaymentTermsVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 账期Service实现类
 * @author system
 * @since 2025-01-05
 */
@Slf4j
@Service
public class PaymentTermsServiceApImpl implements PaymentTermsServiceAp {

    @Resource
    private PaymentTermsMapper paymentTermsMapper;

    @Resource
    private UserProvider userProvider;

    @Override
    public PageResult<PaymentTermsVO> queryPage(PaymentTermsQueryParam param) {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        List<PaymentTermsVO> list = paymentTermsMapper.selectPageList(param);
        PageInfo<PaymentTermsVO> pageInfo = new PageInfo<>(list);
        return new PageResult<PaymentTermsVO>().build(pageInfo);
    }

    @Override
    public PaymentTermsVO getDetail(String termsId) {
        PaymentTermsVO vo = paymentTermsMapper.selectDetailById(Long.parseLong(termsId));
        if (vo == null) {
            throw new ServiceException("账期不存在");
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(PaymentTermsSaveParam param) {
        PaymentTermsEntity existing = paymentTermsMapper.selectByCode(param.getTermsCode());
        if (existing != null && !String.valueOf(existing.getTermsId()).equals(param.getTermsId())) {
            throw new ServiceException("账期编码已存在");
        }

        PaymentTermsEntity entity = new PaymentTermsEntity();
        BeanUtils.copyProperties(param, entity);

        // 获取当前用户信息
        Long currentUserId = getCurrentUserId();
        Long tenantId = getCurrentTenantId();

        if (StringUtils.hasText(param.getTermsId())) {
            entity.setTermsId(Long.parseLong(param.getTermsId()));
            entity.setUpdateTime(LocalDateTime.now());
            entity.setUpdater(currentUserId);
            paymentTermsMapper.updateById(entity);
            return param.getTermsId();
        } else {
            entity.setCreateTime(LocalDateTime.now());
            entity.setIsDeleted(0);
            entity.setCreator(currentUserId);
            entity.setTenantId(tenantId);
            paymentTermsMapper.insert(entity);
            return String.valueOf(entity.getTermsId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String termsId) {
        PaymentTermsEntity entity = paymentTermsMapper.selectById(Long.parseLong(termsId));
        if (entity == null) {
            throw new ServiceException("账期不存在");
        }
        entity.setIsDeleted(1);
        entity.setUpdateTime(LocalDateTime.now());
        paymentTermsMapper.updateById(entity);
    }

    @Override
    public List<PaymentTermsVO> queryBySupplier(String supplierId) {
        return paymentTermsMapper.selectBySupplier(supplierId);
    }

    @Override
    public List<PaymentTermsVO> queryAllEnabled() {
        return paymentTermsMapper.selectAllEnabled();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String termsId, Integer status) {
        PaymentTermsEntity entity = paymentTermsMapper.selectById(Long.parseLong(termsId));
        if (entity == null) {
            throw new ServiceException("账期不存在");
        }
        entity.setStatus(status);
        entity.setUpdateTime(LocalDateTime.now());
        paymentTermsMapper.updateById(entity);
    }

    @Override
    public String calculateDueDate(String termsId, String baseDate) {
        PaymentTermsEntity terms = paymentTermsMapper.selectById(Long.parseLong(termsId));
        if (terms == null) {
            throw new ServiceException("账期不存在");
        }
        LocalDate base = LocalDate.parse(baseDate, DateTimeFormatter.ISO_DATE);
        LocalDate dueDate = base.plusDays(terms.getPaymentDays());
        return dueDate.format(DateTimeFormatter.ISO_DATE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateStatus(List<String> termsIds, Integer status) {
        if (termsIds == null || termsIds.isEmpty()) {
            throw new ServiceException("账期ID列表不能为空");
        }
        for (String termsId : termsIds) {
            PaymentTermsEntity entity = paymentTermsMapper.selectById(Long.parseLong(termsId));
            if (entity != null) {
                entity.setStatus(status);
                entity.setUpdateTime(LocalDateTime.now());
                paymentTermsMapper.updateById(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> termsIds) {
        if (termsIds == null || termsIds.isEmpty()) {
            throw new ServiceException("账期ID列表不能为空");
        }
        for (String termsId : termsIds) {
            PaymentTermsEntity entity = paymentTermsMapper.selectById(Long.parseLong(termsId));
            if (entity != null) {
                entity.setIsDeleted(1);
                entity.setUpdateTime(LocalDateTime.now());
                paymentTermsMapper.updateById(entity);
            }
        }
    }

    // ==================== 用户上下文方法 ====================

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff != null && staff.getStaffid() != null) {
                return staff.getStaffid().longValue();
            }
        } catch (Exception e) {
            log.error("获取当前用户ID失败", e);
        }
        return 1L; // 默认用户ID
    }

    /**
     * 获取当前租户ID
     */
    private Long getCurrentTenantId() {
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff != null && staff.getOrgid() != null) {
                return staff.getOrgid().longValue();
            }
        } catch (Exception e) {
            log.error("获取当前租户ID失败", e);
        }
        return 1000L; // 默认租户ID
    }
}

