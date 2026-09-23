package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.ArCustomerEntity;
import com.financial.sharing.oracle.mapper.ArCustomerMapper;
import com.financial.sharing.service.ArCustomerService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.ArCustomerQueryParam;
import com.financial.sharing.vo.param.ArCustomerSaveParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 客户档案服务实现类
 * @author system
 * @since 2026-01-04
 */
@Slf4j
@Service
public class ArCustomerServiceImpl implements ArCustomerService {

    @Resource
    private ArCustomerMapper arCustomerMapper;

    @Override
    public MyJsonBean<PageResult> getCustomerList(ArCustomerQueryParam param) {
        try {
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            List<ArCustomerEntity> list = arCustomerMapper.selectCustomerList(param);
            PageInfo<ArCustomerEntity> pageInfo = new PageInfo<>(list);

            PageResult result = new PageResult();
            result.setTlist(pageInfo.getList());
            result.setTotalRecord((int) pageInfo.getTotal());
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询客户档案列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getCustomerById(String customerId) {
        try {
            ArCustomerEntity entity = arCustomerMapper.selectById(customerId);
            if (entity == null || entity.getIsDeleted() == 1) {
                return MyJsonBean.errorData("客户档案不存在");
            }
            return MyJsonBean.successData(entity);
        } catch (Exception e) {
            log.error("查询客户档案详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getCustomerByCode(String customerCode, Long tenantId) {
        try {
            ArCustomerEntity entity = arCustomerMapper.selectByCustomerCode(customerCode, tenantId);
            return MyJsonBean.successData(entity);
        } catch (Exception e) {
            log.error("根据编码查询客户失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdateCustomer(ArCustomerSaveParam param) {
        try {
            // 检查客户编码是否重复
            int count = arCustomerMapper.checkCustomerCodeExists(param.getCustomerCode(),
                    param.getCustomerId(), param.getTenantId());
            if (count > 0) {
                return MyJsonBean.errorData("客户编码已存在");
            }

            // 截断超长字段，防止数据库字符串截断错误
            truncateFields(param);

            ArCustomerEntity entity = new ArCustomerEntity();
            BeanUtils.copyProperties(param, entity);

            if (StringUtils.hasText(param.getCustomerId())) {
                // 更新
                entity.setUpdateTime(LocalDateTime.now());
                entity.setUpdateBy(param.getOperatorId());
                arCustomerMapper.updateById(entity);
            } else {
                // 新增
                entity.setCustomerId(UUID.randomUUID().toString().replace("-", ""));
                entity.setCreateTime(LocalDateTime.now());
                entity.setCreateBy(param.getOperatorId());
                entity.setIsDeleted(0);
                arCustomerMapper.insert(entity);
            }
            return MyJsonBean.successData(entity.getCustomerId());
        } catch (Exception e) {
            log.error("保存客户档案失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteCustomer(String customerId) {
        try {
            ArCustomerEntity entity = arCustomerMapper.selectById(customerId);
            if (entity == null) {
                return MyJsonBean.errorData("客户档案不存在");
            }
            entity.setIsDeleted(1);
            entity.setUpdateTime(LocalDateTime.now());
            arCustomerMapper.updateById(entity);
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除客户档案失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchDeleteCustomer(List<String> customerIds) {
        try {
            for (String customerId : customerIds) {
                deleteCustomer(customerId);
            }
            return MyJsonBean.successData("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除客户档案失败", e);
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchUpdateStatus(List<String> customerIds, Integer status) {
        try {
            arCustomerMapper.batchUpdateStatus(customerIds, status);
            return MyJsonBean.successData("状态更新成功");
        } catch (Exception e) {
            log.error("批量更新客户状态失败", e);
            return MyJsonBean.errorData("更新失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getCustomerReceivableStats(String customerId) {
        try {
            Map<String, Object> stats = arCustomerMapper.selectCustomerReceivableStats(customerId);
            return MyJsonBean.successData(stats);
        } catch (Exception e) {
            log.error("查询客户应收统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean checkCustomerCodeExists(String customerCode, String customerId, Long tenantId) {
        try {
            int count = arCustomerMapper.checkCustomerCodeExists(customerCode, customerId, tenantId);
            return MyJsonBean.successData(count > 0);
        } catch (Exception e) {
            log.error("检查客户编码失败", e);
            return MyJsonBean.errorData("检查失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean exportCustomers(ArCustomerQueryParam param) {
        // TODO: 实现导出逻辑
        return MyJsonBean.successData("导出功能待实现");
    }

    /**
     * 截断超长字段，防止数据库字符串截断错误
     * 根据 TBL_AR_CUSTOMER 表结构定义的字段长度进行截断
     */
    private void truncateFields(ArCustomerSaveParam param) {
        if (param.getCustomerCode() != null && param.getCustomerCode().length() > 64) {
            param.setCustomerCode(param.getCustomerCode().substring(0, 64));
        }
        if (param.getCustomerName() != null && param.getCustomerName().length() > 200) {
            param.setCustomerName(param.getCustomerName().substring(0, 200));
        }
        if (param.getCreditCode() != null && param.getCreditCode().length() > 64) {
            param.setCreditCode(param.getCreditCode().substring(0, 64));
        }
        if (param.getLegalRepresentative() != null && param.getLegalRepresentative().length() > 100) {
            param.setLegalRepresentative(param.getLegalRepresentative().substring(0, 100));
        }
        if (param.getRegisteredAddress() != null && param.getRegisteredAddress().length() > 500) {
            param.setRegisteredAddress(param.getRegisteredAddress().substring(0, 500));
        }
        if (param.getContactPerson() != null && param.getContactPerson().length() > 100) {
            param.setContactPerson(param.getContactPerson().substring(0, 100));
        }
        if (param.getContactPhone() != null && param.getContactPhone().length() > 32) {
            param.setContactPhone(param.getContactPhone().substring(0, 32));
        }
        if (param.getContactEmail() != null && param.getContactEmail().length() > 100) {
            param.setContactEmail(param.getContactEmail().substring(0, 100));
        }
        if (param.getFaxNumber() != null && param.getFaxNumber().length() > 32) {
            param.setFaxNumber(param.getFaxNumber().substring(0, 32));
        }
        if (param.getContactAddress() != null && param.getContactAddress().length() > 500) {
            param.setContactAddress(param.getContactAddress().substring(0, 500));
        }
        if (param.getBankName() != null && param.getBankName().length() > 200) {
            param.setBankName(param.getBankName().substring(0, 200));
        }
        if (param.getBankAccount() != null && param.getBankAccount().length() > 64) {
            param.setBankAccount(param.getBankAccount().substring(0, 64));
        }
        if (param.getCreditLevel() != null && param.getCreditLevel().length() > 10) {
            param.setCreditLevel(param.getCreditLevel().substring(0, 10));
        }
        // REMARKS 字段长度为 1000
        if (param.getRemarks() != null && param.getRemarks().length() > 1000) {
            param.setRemarks(param.getRemarks().substring(0, 1000));
        }
    }
}

