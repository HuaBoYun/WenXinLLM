package com.financial.sharing.service.impl;

import com.financial.sharing.dto.param.ArAdvanceReceiptQueryParam;
import com.financial.sharing.oracle.entity.ArAdvanceReceiptEntity;
import com.financial.sharing.oracle.mapper.ArAdvanceReceiptMapper;
import com.financial.sharing.service.ArAdvanceReceiptService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
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
 * 预收款服务实现类
 * @author system
 * @since 2026-01-04
 */
@Slf4j
@Service
public class ArAdvanceReceiptServiceImpl implements ArAdvanceReceiptService {

    @Resource
    private ArAdvanceReceiptMapper arAdvanceReceiptMapper;

    @Override
    public MyJsonBean<PageResult> getAdvanceReceiptList(ArAdvanceReceiptQueryParam param) {
        try {
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            List<ArAdvanceReceiptEntity> list = arAdvanceReceiptMapper.selectAdvanceReceiptList(param);
            PageInfo<ArAdvanceReceiptEntity> pageInfo = new PageInfo<>(list);
            
            PageResult result = new PageResult();
            result.setTlist(pageInfo.getList());
            result.setTotalRecord((int) pageInfo.getTotal());
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询预收款列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getAdvanceReceiptById(String advanceId) {
        try {
            ArAdvanceReceiptEntity entity = arAdvanceReceiptMapper.selectById(advanceId);
            if (entity == null) {
                return MyJsonBean.errorData("预收款记录不存在");
            }
            return MyJsonBean.successData(entity);
        } catch (Exception e) {
            log.error("查询预收款详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdateAdvanceReceipt(ArAdvanceReceiptEntity entity) {
        try {
            if (StringUtils.hasText(entity.getAdvanceId())) {
                entity.setUpdateTime(LocalDateTime.now());
                arAdvanceReceiptMapper.updateById(entity);
            } else {
                entity.setAdvanceId(UUID.randomUUID().toString().replace("-", ""));
                entity.setAdvanceNo(generateAdvanceNo(entity.getTenantId()));
                entity.setOffsetAmount(BigDecimal.ZERO);
                entity.setRemainingAmount(entity.getAdvanceAmount());
                entity.setAdvanceStatus(0); // 未冲销
                entity.setCreateTime(LocalDateTime.now());
                entity.setIsDeleted(0);
                arAdvanceReceiptMapper.insert(entity);
            }
            return MyJsonBean.successData(entity.getAdvanceId());
        } catch (Exception e) {
            log.error("保存预收款失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteAdvanceReceipt(String advanceId) {
        try {
            ArAdvanceReceiptEntity entity = arAdvanceReceiptMapper.selectById(advanceId);
            if (entity == null) {
                return MyJsonBean.errorData("预收款记录不存在");
            }
            if (entity.getOffsetAmount() != null && entity.getOffsetAmount().compareTo(BigDecimal.ZERO) > 0) {
                return MyJsonBean.errorData("该预收款已有冲销记录，无法删除");
            }
            arAdvanceReceiptMapper.deleteById(advanceId);
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除预收款失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getAvailableAdvanceReceipts(String customerId, Long tenantId) {
        try {
            List<ArAdvanceReceiptEntity> list = arAdvanceReceiptMapper.selectAvailableForOffset(customerId, tenantId);
            return MyJsonBean.successData(list);
        } catch (Exception e) {
            log.error("查询可用预收款失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean offsetAdvanceReceipt(String advanceId, BigDecimal offsetAmount, String operatorId) {
        try {
            ArAdvanceReceiptEntity entity = arAdvanceReceiptMapper.selectById(advanceId);
            if (entity == null) {
                return MyJsonBean.errorData("预收款记录不存在");
            }
            if (entity.getRemainingAmount().compareTo(offsetAmount) < 0) {
                return MyJsonBean.errorData("冲销金额不能大于剩余金额");
            }
            // 更新冲销金额
            BigDecimal newOffsetAmount = entity.getOffsetAmount().add(offsetAmount);
            BigDecimal newRemainingAmount = entity.getRemainingAmount().subtract(offsetAmount);
            entity.setOffsetAmount(newOffsetAmount);
            entity.setRemainingAmount(newRemainingAmount);
            // 更新状态
            if (newRemainingAmount.compareTo(BigDecimal.ZERO) == 0) {
                entity.setAdvanceStatus(2); // 全部冲销
            } else {
                entity.setAdvanceStatus(1); // 部分冲销
            }
            entity.setUpdateTime(LocalDateTime.now());
            entity.setUpdateBy(operatorId);
            arAdvanceReceiptMapper.updateById(entity);
            return MyJsonBean.successData("冲销成功");
        } catch (Exception e) {
            log.error("预收款冲销失败", e);
            return MyJsonBean.errorData("冲销失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getAdvanceReceiptStatistics(ArAdvanceReceiptQueryParam param) {
        try {
            return MyJsonBean.successData(arAdvanceReceiptMapper.selectAdvanceReceiptStatistics(param));
        } catch (Exception e) {
            log.error("查询预收款统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    private String generateAdvanceNo(Long tenantId) {
        String dateStr = LocalDate.now().toString().replace("-", "");
        String random = String.format("%04d", (int)(Math.random() * 10000));
        return "ADV" + dateStr + random;
    }
}

