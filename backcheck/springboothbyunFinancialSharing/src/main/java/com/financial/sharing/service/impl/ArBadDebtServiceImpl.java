package com.financial.sharing.service.impl;

import com.financial.sharing.oracle.entity.ArBadDebtProvisionEntity;
import com.financial.sharing.oracle.entity.ArBadDebtWriteOffEntity;
import com.financial.sharing.oracle.entity.ArBadDebtRecoveryEntity;
import com.financial.sharing.oracle.mapper.ArBadDebtProvisionMapper;
import com.financial.sharing.oracle.mapper.ArBadDebtWriteOffMapper;
import com.financial.sharing.oracle.mapper.ArBadDebtRecoveryMapper;
import com.financial.sharing.service.ArBadDebtService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 坏账管理服务实现类
 * @author system
 * @since 2026-01-04
 */
@Slf4j
@Service
public class ArBadDebtServiceImpl implements ArBadDebtService {

    @Resource
    private ArBadDebtProvisionMapper provisionMapper;

    @Resource
    private ArBadDebtWriteOffMapper badDebtWriteOffMapper;

    @Resource
    private ArBadDebtRecoveryMapper recoveryMapper;

    // ==================== 坏账准备 ====================

    @Override
    public MyJsonBean<PageResult> getProvisionList(ArBadDebtProvisionQueryParam param) {
        try {
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            List<ArBadDebtProvisionEntity> list = provisionMapper.selectProvisionList(param);
            PageInfo<ArBadDebtProvisionEntity> pageInfo = new PageInfo<>(list);

            PageResult result = new PageResult();
            result.setTlist(pageInfo.getList());
            result.setTotalRecord((int) pageInfo.getTotal());
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询坏账准备列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getProvisionById(String provisionId) {
        try {
            ArBadDebtProvisionEntity entity = provisionMapper.selectById(provisionId);
            if (entity == null || entity.getIsDeleted() == 1) {
                return MyJsonBean.errorData("坏账准备记录不存在");
            }
            return MyJsonBean.successData(entity);
        } catch (Exception e) {
            log.error("查询坏账准备详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean createProvision(ArBadDebtProvisionSaveParam param) {
        try {
            ArBadDebtProvisionEntity entity = new ArBadDebtProvisionEntity();
            BeanUtils.copyProperties(param, entity);

            entity.setProvisionId(UUID.randomUUID().toString().replace("-", ""));
            entity.setProvisionDate(LocalDate.now());
            entity.setProvisionStatus(0); // 待审核
            entity.setCreateTime(LocalDateTime.now());
            entity.setCreateBy(param.getOperatorId());
            entity.setIsDeleted(0);

            // 计算计提金额
            if (param.getProvisionRate() != null && param.getReceivableAmount() != null) {
                BigDecimal provisionAmount = param.getReceivableAmount()
                        .multiply(param.getProvisionRate())
                        .divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
                entity.setProvisionAmount(provisionAmount);
            }

            provisionMapper.insert(entity);
            return MyJsonBean.successData(entity.getProvisionId());
        } catch (Exception e) {
            log.error("计提坏账准备失败", e);
            return MyJsonBean.errorData("计提失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchCreateProvision(List<ArBadDebtProvisionSaveParam> params) {
        try {
            for (ArBadDebtProvisionSaveParam param : params) {
                createProvision(param);
            }
            return MyJsonBean.successData("批量计提成功");
        } catch (Exception e) {
            log.error("批量计提坏账准备失败", e);
            return MyJsonBean.errorData("批量计提失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean reverseProvision(String provisionId, String operatorId) {
        try {
            provisionMapper.updateProvisionStatus(provisionId, 2); // 已冲回
            return MyJsonBean.successData("冲回成功");
        } catch (Exception e) {
            log.error("冲回坏账准备失败", e);
            return MyJsonBean.errorData("冲回失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getProvisionBalance(Long tenantId, LocalDate asOfDate) {
        try {
            BigDecimal balance = provisionMapper.selectProvisionBalance(tenantId, asOfDate);
            return MyJsonBean.successData(balance);
        } catch (Exception e) {
            log.error("查询坏账准备余额失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getProvisionStatistics(ArBadDebtProvisionQueryParam param) {
        try {
            Map<String, Object> stats = provisionMapper.selectProvisionStatistics(param);
            return MyJsonBean.successData(stats);
        } catch (Exception e) {
            log.error("查询坏账准备统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 坏账核销 ====================

    @Override
    public MyJsonBean<PageResult> getBadDebtWriteOffList(ArBadDebtWriteOffQueryParam param) {
        try {
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            List<ArBadDebtWriteOffEntity> list = badDebtWriteOffMapper.selectBadDebtWriteOffList(param);
            PageInfo<ArBadDebtWriteOffEntity> pageInfo = new PageInfo<>(list);

            PageResult result = new PageResult();
            result.setTlist(pageInfo.getList());
            result.setTotalRecord((int) pageInfo.getTotal());
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询坏账核销列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getBadDebtWriteOffById(String writeOffId) {
        try {
            ArBadDebtWriteOffEntity entity = badDebtWriteOffMapper.selectById(writeOffId);
            if (entity == null || entity.getIsDeleted() == 1) {
                return MyJsonBean.errorData("坏账核销记录不存在");
            }
            return MyJsonBean.successData(entity);
        } catch (Exception e) {
            log.error("查询坏账核销详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean applyBadDebtWriteOff(ArBadDebtWriteOffSaveParam param) {
        try {
            ArBadDebtWriteOffEntity entity = new ArBadDebtWriteOffEntity();
            BeanUtils.copyProperties(param, entity);

            entity.setWriteOffId(UUID.randomUUID().toString().replace("-", ""));
            entity.setWriteOffNo(badDebtWriteOffMapper.generateWriteOffNo("BW", param.getTenantId()));
            entity.setWriteOffDate(LocalDate.now());
            entity.setWriteOffStatus(0); // 待审核
            entity.setCreateTime(LocalDateTime.now());
            entity.setCreateBy(param.getOperatorId());
            entity.setIsDeleted(0);

            badDebtWriteOffMapper.insert(entity);
            return MyJsonBean.successData(entity.getWriteOffId());
        } catch (Exception e) {
            log.error("申请坏账核销失败", e);
            return MyJsonBean.errorData("申请失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean auditBadDebtWriteOff(String writeOffId, Integer status, String auditorId, String auditComments) {
        try {
            badDebtWriteOffMapper.updateWriteOffStatus(writeOffId, status, auditorId, auditComments);
            return MyJsonBean.successData("审核完成");
        } catch (Exception e) {
            log.error("审核坏账核销失败", e);
            return MyJsonBean.errorData("审核失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getBadDebtWriteOffStatistics(ArBadDebtWriteOffQueryParam param) {
        try {
            Map<String, Object> stats = badDebtWriteOffMapper.selectBadDebtWriteOffStatistics(param);
            return MyJsonBean.successData(stats);
        } catch (Exception e) {
            log.error("查询坏账核销统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 坏账回收 ====================

    @Override
    public MyJsonBean<PageResult> getRecoveryList(ArBadDebtRecoveryQueryParam param) {
        try {
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            List<ArBadDebtRecoveryEntity> list = recoveryMapper.selectRecoveryList(param);
            PageInfo<ArBadDebtRecoveryEntity> pageInfo = new PageInfo<>(list);

            PageResult result = new PageResult();
            result.setTlist(pageInfo.getList());
            result.setTotalRecord((int) pageInfo.getTotal());
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询坏账回收列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getRecoveryById(String recoveryId) {
        try {
            ArBadDebtRecoveryEntity entity = recoveryMapper.selectById(recoveryId);
            if (entity == null || entity.getIsDeleted() == 1) {
                return MyJsonBean.errorData("坏账回收记录不存在");
            }
            return MyJsonBean.successData(entity);
        } catch (Exception e) {
            log.error("查询坏账回收详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean createRecovery(ArBadDebtRecoverySaveParam param) {
        try {
            ArBadDebtRecoveryEntity entity = new ArBadDebtRecoveryEntity();
            BeanUtils.copyProperties(param, entity);

            entity.setRecoveryId(UUID.randomUUID().toString().replace("-", ""));
            entity.setRecoveryNo(recoveryMapper.generateRecoveryNo("BR", param.getTenantId()));
            entity.setRecoveryDate(LocalDate.now());
            entity.setCreateTime(LocalDateTime.now());
            entity.setCreateBy(param.getOperatorId());
            entity.setIsDeleted(0);

            recoveryMapper.insert(entity);
            return MyJsonBean.successData(entity.getRecoveryId());
        } catch (Exception e) {
            log.error("登记坏账回收失败", e);
            return MyJsonBean.errorData("登记失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getRecoveryStatistics(ArBadDebtRecoveryQueryParam param) {
        try {
            Map<String, Object> stats = recoveryMapper.selectRecoveryStatistics(param);
            return MyJsonBean.successData(stats);
        } catch (Exception e) {
            log.error("查询坏账回收统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 导出 ====================

    @Override
    public MyJsonBean exportProvisions(ArBadDebtProvisionQueryParam param) {
        return MyJsonBean.successData("导出功能待实现");
    }

    @Override
    public MyJsonBean exportBadDebtWriteOffs(ArBadDebtWriteOffQueryParam param) {
        return MyJsonBean.successData("导出功能待实现");
    }

    @Override
    public MyJsonBean exportRecoveries(ArBadDebtRecoveryQueryParam param) {
        return MyJsonBean.successData("导出功能待实现");
    }
}
