package com.financial.sharing.service.impl;

import com.financial.sharing.oracle.entity.ArReceiptEntity;
import com.financial.sharing.oracle.mapper.ArReceiptMapper;
import com.financial.sharing.service.ArReceiptService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.ArReceiptSaveParam;
import com.financial.sharing.vo.param.ArReceiptQueryParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 收款单服务实现类
 * @author system
 * @since 2026-01-04
 */
@Slf4j
@Service
public class ArReceiptServiceImpl implements ArReceiptService {

    @Resource
    private ArReceiptMapper arReceiptMapper;

    @Override
    public MyJsonBean<PageResult> getReceiptList(ArReceiptQueryParam param) {
        try {
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            List<ArReceiptEntity> list = arReceiptMapper.selectReceiptList(param);
            PageInfo<ArReceiptEntity> pageInfo = new PageInfo<>(list);
            
            PageResult result = new PageResult();
            result.setTlist(pageInfo.getList());
            result.setTotalRecord((int) pageInfo.getTotal());
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询收款单列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getReceiptById(String receiptId) {
        try {
            ArReceiptEntity entity = arReceiptMapper.selectById(receiptId);
            if (entity == null || entity.getIsDeleted() == 1) {
                return MyJsonBean.errorData("收款单不存在");
            }
            return MyJsonBean.successData(entity);
        } catch (Exception e) {
            log.error("查询收款单详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getReceiptByNo(String receiptNo, Long tenantId) {
        try {
            ArReceiptEntity entity = arReceiptMapper.selectByReceiptNo(receiptNo, tenantId);
            return MyJsonBean.successData(entity);
        } catch (Exception e) {
            log.error("根据收款单号查询失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdateReceipt(ArReceiptSaveParam param) {
        try {
            ArReceiptEntity entity = new ArReceiptEntity();
            BeanUtils.copyProperties(param, entity);

            if (StringUtils.hasText(param.getReceiptId())) {
                entity.setUpdateTime(LocalDateTime.now());
                entity.setUpdateBy(param.getOperatorId());
                arReceiptMapper.updateById(entity);
            } else {
                entity.setReceiptId(UUID.randomUUID().toString().replace("-", ""));
                entity.setReceiptNo(generateReceiptNo(param.getTenantId()));
                entity.setWriteOffAmount(BigDecimal.ZERO);
                entity.setRemainingAmount(param.getReceiptAmount());
                entity.setReceiptStatus(0); // 待确认
                entity.setCreateTime(LocalDateTime.now());
                entity.setCreateBy(param.getOperatorId());
                entity.setIsDeleted(0);
                arReceiptMapper.insert(entity);
            }
            return MyJsonBean.successData(entity.getReceiptId());
        } catch (Exception e) {
            log.error("保存收款单失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteReceipt(String receiptId) {
        try {
            ArReceiptEntity entity = arReceiptMapper.selectById(receiptId);
            if (entity == null) {
                return MyJsonBean.errorData("收款单不存在");
            }
            if (entity.getReceiptStatus() != 0) {
                return MyJsonBean.errorData("只能删除待确认状态的收款单");
            }
            entity.setIsDeleted(1);
            entity.setUpdateTime(LocalDateTime.now());
            arReceiptMapper.updateById(entity);
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除收款单失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchDeleteReceipt(List<String> receiptIds) {
        try {
            for (String id : receiptIds) {
                deleteReceipt(id);
            }
            return MyJsonBean.successData("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除收款单失败", e);
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean confirmReceipt(String receiptId, BigDecimal confirmAmount, String confirmBy) {
        try {
            ArReceiptEntity entity = arReceiptMapper.selectById(receiptId);
            if (entity == null) {
                return MyJsonBean.errorData("收款单不存在");
            }
            if (entity.getReceiptStatus() != 0) {
                return MyJsonBean.errorData("只能确认待确认状态的收款单");
            }
            arReceiptMapper.confirmReceipt(receiptId, confirmAmount, confirmBy);
            return MyJsonBean.successData("确认成功");
        } catch (Exception e) {
            log.error("确认收款失败", e);
            return MyJsonBean.errorData("确认失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean cancelReceipt(String receiptId, String cancelReason, String cancelBy) {
        try {
            ArReceiptEntity entity = arReceiptMapper.selectById(receiptId);
            if (entity == null) {
                return MyJsonBean.errorData("收款单不存在");
            }
            if (entity.getWriteOffAmount().compareTo(BigDecimal.ZERO) > 0) {
                return MyJsonBean.errorData("已核销的收款单不能取消");
            }
            arReceiptMapper.cancelReceipt(receiptId, cancelReason, cancelBy);
            return MyJsonBean.successData("取消成功");
        } catch (Exception e) {
            log.error("取消收款单失败", e);
            return MyJsonBean.errorData("取消失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getReceiptsByCustomer(String customerId, Long tenantId) {
        try {
            List<ArReceiptEntity> list = arReceiptMapper.selectByCustomerId(customerId, tenantId);
            return MyJsonBean.successData(list);
        } catch (Exception e) {
            log.error("查询客户收款单失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getPendingWriteOffReceipts(String customerId, Long tenantId) {
        try {
            List<ArReceiptEntity> list = arReceiptMapper.selectPendingWriteOff(customerId, tenantId);
            return MyJsonBean.successData(list);
        } catch (Exception e) {
            log.error("查询待核销收款单失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getReceiptStatistics(ArReceiptQueryParam param) {
        try {
            // TODO: 实现统计逻辑
            return MyJsonBean.successData(null);
        } catch (Exception e) {
            log.error("查询收款统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean exportReceipts(ArReceiptQueryParam param) {
        return MyJsonBean.successData("导出功能待实现");
    }

    @Override
    public String generateReceiptNo(Long tenantId) {
        String prefix = "RC";
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = String.format("%04d", (int)(Math.random() * 10000));
        return prefix + dateStr + random;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchWriteOff(List<String> receiptIds, String operatorId, String remark) {
        try {
            if (receiptIds == null || receiptIds.isEmpty()) {
                return MyJsonBean.errorData("请选择要核销的收款单");
            }

            int successCount = 0;
            int failCount = 0;
            StringBuilder failMsg = new StringBuilder();

            for (String receiptId : receiptIds) {
                ArReceiptEntity entity = arReceiptMapper.selectById(receiptId);
                if (entity == null) {
                    failCount++;
                    failMsg.append("收款单不存在: ").append(receiptId).append("; ");
                    continue;
                }
                // 只有已收款状态(1)的收款单才能核销
                if (entity.getReceiptStatus() != 1) {
                    failCount++;
                    failMsg.append("收款单[").append(entity.getReceiptNo()).append("]状态不允许核销; ");
                    continue;
                }
                // 检查是否有剩余金额可核销
                if (entity.getRemainingAmount() == null || entity.getRemainingAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    failCount++;
                    failMsg.append("收款单[").append(entity.getReceiptNo()).append("]无可核销金额; ");
                    continue;
                }

                // 执行核销：将剩余金额全部核销
                BigDecimal writeOffAmount = entity.getRemainingAmount();
                entity.setWriteOffAmount(entity.getReceiptAmount()); // 已核销金额 = 收款金额
                entity.setRemainingAmount(BigDecimal.ZERO); // 剩余金额 = 0
                entity.setReceiptStatus(2); // 已核销
                entity.setUpdateTime(LocalDateTime.now());
                entity.setUpdateBy(operatorId);
                entity.setRemarks(remark);
                arReceiptMapper.updateById(entity);
                successCount++;
            }
            if (failCount == 0) {
                return MyJsonBean.successData("批量核销成功，共核销 " + successCount + " 笔收款单");
            } else if (successCount == 0) {
                return MyJsonBean.errorData("批量核销失败: " + failMsg.toString());
            } else {
                return MyJsonBean.successData("部分核销成功，成功 " + successCount + " 笔，失败 " + failCount + " 笔。失败原因: " + failMsg.toString());
            }
        } catch (Exception e) {
            log.error("批量核销收款单失败", e);
            return MyJsonBean.errorData("批量核销失败: " + e.getMessage());
        }
    }
}
