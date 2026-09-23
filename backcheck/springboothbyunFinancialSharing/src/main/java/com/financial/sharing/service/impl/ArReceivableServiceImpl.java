package com.financial.sharing.service.impl;

import com.financial.sharing.oracle.entity.ArReceivableEntity;
import com.financial.sharing.oracle.mapper.ArReceivableMapper;
import com.financial.sharing.service.ArReceivableService;
import com.financial.sharing.service.ArTodoService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.ArReceivableSaveParam;
import com.financial.sharing.vo.param.ArReceivableQueryParam;
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
 * 应收单据服务实现类
 * @author system
 * @since 2026-01-04
 */
@Slf4j
@Service
public class ArReceivableServiceImpl implements ArReceivableService {

    @Resource
    private ArReceivableMapper arReceivableMapper;

    @Resource
    private ArTodoService arTodoService;

    @Override
    public MyJsonBean<PageResult> getReceivableList(ArReceivableQueryParam param) {
        try {
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            List<ArReceivableEntity> list = arReceivableMapper.selectReceivableList(param);
            PageInfo<ArReceivableEntity> pageInfo = new PageInfo<>(list);

            PageResult result = new PageResult();
            result.setTlist(pageInfo.getList());
            result.setTotalRecord((int) pageInfo.getTotal());
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询应收单据列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getReceivableById(String receivableId) {
        try {
            ArReceivableEntity entity = arReceivableMapper.selectById(receivableId);
            if (entity == null || entity.getIsDeleted() == 1) {
                return MyJsonBean.errorData("应收单据不存在");
            }
            return MyJsonBean.successData(entity);
        } catch (Exception e) {
            log.error("查询应收单据详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getReceivableByDocumentNo(String documentNo, Long tenantId) {
        try {
            ArReceivableEntity entity = arReceivableMapper.selectByDocumentNo(documentNo, tenantId);
            return MyJsonBean.successData(entity);
        } catch (Exception e) {
            log.error("根据单据编号查询失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdateReceivable(ArReceivableSaveParam param) {
        try {
            ArReceivableEntity entity = new ArReceivableEntity();
            BeanUtils.copyProperties(param, entity);

            if (StringUtils.hasText(param.getReceivableId())) {
                // 更新操作
                entity.setUpdateTime(LocalDateTime.now());
                entity.setUpdateBy(param.getOperatorId());
                // 如果传入了单据状态则使用传入的值
                if (param.getDocumentStatus() != null) {
                    entity.setDocumentStatus(param.getDocumentStatus());
                }
                arReceivableMapper.updateReceivableById(entity);
            } else {
                // 新增操作
                entity.setReceivableId(UUID.randomUUID().toString().replace("-", ""));
                // 如果用户输入了单据编号则使用用户输入的，否则自动生成
                if (StringUtils.hasText(param.getDocumentNo())) {
                    entity.setDocumentNo(param.getDocumentNo());
                } else {
                    entity.setDocumentNo(generateDocumentNo(param.getTenantId()));
                }
                entity.setReceivedAmount(BigDecimal.ZERO);
                entity.setRemainingAmount(param.getReceivableAmount());
                // 如果传入了单据状态则使用传入的值，否则默认草稿
                entity.setDocumentStatus(param.getDocumentStatus() != null ? param.getDocumentStatus() : 0);
                entity.setCreateTime(LocalDateTime.now());
                entity.setCreateBy(param.getOperatorId());
                entity.setIsDeleted(0);
                arReceivableMapper.insert(entity);
            }
            return MyJsonBean.successData(entity.getReceivableId());
        } catch (Exception e) {
            log.error("保存应收单据失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteReceivable(String receivableId) {
        try {
            ArReceivableEntity entity = arReceivableMapper.selectById(receivableId);
            if (entity == null) {
                return MyJsonBean.errorData("应收单据不存在");
            }
            if (entity.getDocumentStatus() != 0) {
                return MyJsonBean.errorData("只能删除草稿状态的单据");
            }
            entity.setIsDeleted(1);
            entity.setUpdateTime(LocalDateTime.now());
            arReceivableMapper.updateById(entity);
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除应收单据失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchDeleteReceivable(List<String> receivableIds) {
        try {
            for (String id : receivableIds) {
                deleteReceivable(id);
            }
            return MyJsonBean.successData("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除应收单据失败", e);
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean submitForAudit(String receivableId) {
        try {
            ArReceivableEntity entity = arReceivableMapper.selectById(receivableId);
            if (entity == null) {
                return MyJsonBean.errorData("应收单据不存在");
            }
            if (entity.getDocumentStatus() != 0) {
                return MyJsonBean.errorData("只能提交草稿状态的单据");
            }
            entity.setDocumentStatus(1); // 待审核
            entity.setUpdateTime(LocalDateTime.now());
            arReceivableMapper.updateById(entity);
            return MyJsonBean.successData("提交成功");
        } catch (Exception e) {
            log.error("提交审核失败", e);
            return MyJsonBean.errorData("提交失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchSubmitForAudit(List<String> receivableIds) {
        try {
            for (String id : receivableIds) {
                submitForAudit(id);
            }
            return MyJsonBean.successData("批量提交成功");
        } catch (Exception e) {
            log.error("批量提交审核失败", e);
            return MyJsonBean.errorData("批量提交失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean approveReceivable(String receivableId, String auditorId, String auditComments) {
        try {
            ArReceivableEntity entity = arReceivableMapper.selectById(receivableId);
            if (entity == null) {
                return MyJsonBean.errorData("应收单据不存在");
            }
            entity.setDocumentStatus(2); // 已审核
            entity.setAuditTime(LocalDateTime.now());
            entity.setAuditorId(auditorId);
            entity.setAuditComments(auditComments);
            entity.setUpdateTime(LocalDateTime.now());
            arReceivableMapper.updateById(entity);
            return MyJsonBean.successData("审核通过");
        } catch (Exception e) {
            log.error("审核通过失败", e);
            return MyJsonBean.errorData("审核失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean rejectReceivable(String receivableId, String auditorId, String auditComments) {
        try {
            ArReceivableEntity entity = arReceivableMapper.selectById(receivableId);
            if (entity == null) {
                return MyJsonBean.errorData("应收单据不存在");
            }
            entity.setDocumentStatus(3); // 已驳回
            entity.setAuditTime(LocalDateTime.now());
            entity.setAuditorId(auditorId);
            entity.setAuditComments(auditComments);
            entity.setUpdateTime(LocalDateTime.now());
            arReceivableMapper.updateById(entity);
            return MyJsonBean.successData("审核驳回");
        } catch (Exception e) {
            log.error("审核驳回失败", e);
            return MyJsonBean.errorData("驳回失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchAudit(List<String> receivableIds, Integer status, String auditorId, String auditComments) {
        try {
            arReceivableMapper.batchUpdateStatus(receivableIds, status, auditorId, auditComments);
            return MyJsonBean.successData("批量审核成功");
        } catch (Exception e) {
            log.error("批量审核失败", e);
            return MyJsonBean.errorData("批量审核失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getReceivablesByCustomer(String customerId, Long tenantId) {
        try {
            List<ArReceivableEntity> list = arReceivableMapper.selectByCustomerId(customerId, tenantId);
            return MyJsonBean.successData(list);
        } catch (Exception e) {
            log.error("查询客户应收单据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getPendingWriteOffReceivables(String customerId, Long tenantId) {
        try {
            List<ArReceivableEntity> list = arReceivableMapper.selectPendingWriteOff(customerId, tenantId);
            return MyJsonBean.successData(list);
        } catch (Exception e) {
            log.error("查询待核销应收单据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getReceivableStatistics(ArReceivableQueryParam param) {
        try {
            // TODO: 实现统计逻辑
            return MyJsonBean.successData(null);
        } catch (Exception e) {
            log.error("查询应收统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean exportReceivables(ArReceivableQueryParam param) {
        return MyJsonBean.successData("导出功能待实现");
    }

    @Override
    public String generateDocumentNo(Long tenantId) {
        String prefix = "AR";
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = String.format("%04d", (int)(Math.random() * 10000));
        return prefix + dateStr + random;
    }
}
