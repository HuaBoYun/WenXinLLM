package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.CreditContractDTO;
import com.global.treasurer.dto.CreditContractQueryDTO;
import com.global.treasurer.entity.TblCreditContract;
import com.global.treasurer.mapper.CreditContractMapper;
import com.global.treasurer.service.CreditContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 授信合同服务实现类（简化版）
 *
 * @author 华博云开发团队
 * @since 2026-02-04
 */
@Service
public class CreditContractServiceImpl implements CreditContractService {
    private static final Logger log = LoggerFactory.getLogger(CreditContractServiceImpl.class);

    @Autowired(required = false)
    private CreditContractMapper creditContractMapper;

    @Override
    public PageInfo<TblCreditContract> getContractList(CreditContractQueryDTO queryDTO) {
        log.info("查询授信合同列表, queryDTO: {}", queryDTO);

        try {
            // 构建查询条件
            QueryWrapper<TblCreditContract> wrapper = new QueryWrapper<>();
            wrapper.eq("DELETE_FLAG", 0);

            // 合同编号
            if (queryDTO.getContractNo() != null && !queryDTO.getContractNo().isEmpty()) {
                wrapper.like("CONTRACT_NO", queryDTO.getContractNo());
            }

            // 银行代码
            if (queryDTO.getBankCode() != null && !queryDTO.getBankCode().isEmpty()) {
                wrapper.eq("BANK_CODE", queryDTO.getBankCode());
            }

            // 银行名称
            if (queryDTO.getBankName() != null && !queryDTO.getBankName().isEmpty()) {
                wrapper.like("BANK_NAME", queryDTO.getBankName());
            }

            // 合同状态
            if (queryDTO.getContractStatus() != null && !queryDTO.getContractStatus().isEmpty()) {
                wrapper.eq("CONTRACT_STATUS", queryDTO.getContractStatus());
            }

            // 公司ID
            if (queryDTO.getCompanyId() != null) {
                wrapper.eq("COMPANY_ID", queryDTO.getCompanyId());
            }

            // 公司名称
            if (queryDTO.getCompanyName() != null && !queryDTO.getCompanyName().isEmpty()) {
                wrapper.like("COMPANY_NAME", queryDTO.getCompanyName());
            }

            // 按创建时间倒序
            wrapper.orderByDesc("CREATED_TIME");

            // 分页查询
            int pageNum = queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1;
            int pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;

            Page<TblCreditContract> page = new Page<>(pageNum, pageSize);
            IPage<TblCreditContract> result = creditContractMapper.selectPage(page, wrapper);

            // 转换为PageInfo
            PageInfo<TblCreditContract> pageInfo = new PageInfo<>();
            pageInfo.setList(result.getRecords());
            pageInfo.setTotal(result.getTotal());
            pageInfo.setPageNum((int) result.getCurrent());
            pageInfo.setPageSize((int) result.getSize());
            pageInfo.setPages((int) result.getPages());

            return pageInfo;
        } catch (Exception e) {
            log.error("查询授信合同列表失败", e);
            return new PageInfo<>(new java.util.ArrayList<>());
        }
    }

    @Override
    public TblCreditContract getContractById(Long contractId) {
        log.info("查询授信合同详情, contractId: {}", contractId);
        try {
            return creditContractMapper.selectById(contractId);
        } catch (Exception e) {
            log.error("查询授信合同详情失败, contractId: {}", contractId, e);
            return null;
        }
    }

    @Override
    public TblCreditContract saveContract(CreditContractDTO dto) {
        log.info("保存授信合同, dto: {}", dto);
        try {
            TblCreditContract contract = new TblCreditContract();

            // 判断是新增还是更新
            boolean isUpdate = (dto.getContractId() != null && dto.getContractId() > 0);

            // 复制DTO属性到实体
            if (isUpdate) {
                contract.setContractId(dto.getContractId());
            }
            // 注意：新增时不设置contractId，让数据库自动生成

            contract.setContractNo(dto.getContractNo());
            contract.setCreditApplicationId(dto.getCreditApplicationId());
            contract.setBankCode(dto.getBankCode());
            contract.setBankName(dto.getBankName());
            contract.setCreditLimit(dto.getCreditLimit());
            contract.setCurrencyCode(dto.getCurrencyCode());
            contract.setCreditPeriod(dto.getCreditPeriod());
            contract.setStartDate(dto.getStartDate());
            contract.setEndDate(dto.getEndDate());
            contract.setInterestRate(dto.getInterestRate());
            contract.setGuaranteeMethod(dto.getGuaranteeMethod());
            contract.setCompanyId(dto.getCompanyId());
            contract.setCompanyName(dto.getCompanyName());
            contract.setRemark(dto.getRemark());

            // 设置默认值
            if (!isUpdate) {
                // 新增
                contract.setContractStatus("DRAFT"); // 默认草稿状态
                contract.setDeleteFlag(0);
                contract.setCreatedTime(new Date());
                contract.setUpdatedTime(new Date());
                contract.setUsedAmount(java.math.BigDecimal.ZERO);
                contract.setAvailableAmount(dto.getCreditLimit());

                log.info("执行新增操作, contract: {}", contract);
                int result = creditContractMapper.insert(contract);
                log.info("新增结果: {}, 生成的ID: {}", result, contract.getContractId());
            } else {
                // 更新
                contract.setUpdatedTime(new Date());
                log.info("执行更新操作, contractId: {}", contract.getContractId());
                creditContractMapper.updateById(contract);
            }

            return contract;
        } catch (Exception e) {
            log.error("保存授信合同失败", e);
            throw new RuntimeException("保存授信合同失败: " + e.getMessage());
        }
    }

    @Override
    public void deleteContract(Long contractId) {
        log.info("删除授信合同, contractId: {}", contractId);
        try {
            // 逻辑删除
            TblCreditContract contract = new TblCreditContract();
            contract.setContractId(contractId);
            contract.setDeleteFlag(1);
            contract.setUpdatedTime(new Date());
            creditContractMapper.updateById(contract);
        } catch (Exception e) {
            log.error("删除授信合同失败, contractId: {}", contractId, e);
            throw new RuntimeException("删除授信合同失败: " + e.getMessage());
        }
    }

    @Override
    public void batchDeleteContracts(List<Long> contractIds) {
        log.info("批量删除授信合同, contractIds: {}", contractIds);
        try {
            for (Long contractId : contractIds) {
                deleteContract(contractId);
            }
        } catch (Exception e) {
            log.error("批量删除授信合同失败, contractIds: {}", contractIds, e);
            throw new RuntimeException("批量删除授信合同失败: " + e.getMessage());
        }
    }

    @Override
    public void signContract(Long contractId) {
        log.info("签署授信合同, contractId: {}", contractId);
        try {
            TblCreditContract contract = new TblCreditContract();
            contract.setContractId(contractId);
            contract.setContractStatus("SIGNED");
            contract.setSigningDate(new Date());
            contract.setUpdatedTime(new Date());
            creditContractMapper.updateById(contract);
        } catch (Exception e) {
            log.error("签署授信合同失败, contractId: {}", contractId, e);
            throw new RuntimeException("签署授信合同失败: " + e.getMessage());
        }
    }

    @Override
    public void activateContract(Long contractId) {
        log.info("生效授信合同, contractId: {}", contractId);
        try {
            TblCreditContract contract = new TblCreditContract();
            contract.setContractId(contractId);
            contract.setContractStatus("EFFECTIVE");
            contract.setEffectiveDate(new Date());
            contract.setUpdatedTime(new Date());
            creditContractMapper.updateById(contract);
        } catch (Exception e) {
            log.error("生效授信合同失败, contractId: {}", contractId, e);
            throw new RuntimeException("生效授信合同失败: " + e.getMessage());
        }
    }

    @Override
    public void terminateContract(Long contractId, String reason) {
        log.info("终止授信合同, contractId: {}, reason: {}", contractId, reason);
        try {
            TblCreditContract contract = new TblCreditContract();
            contract.setContractId(contractId);
            contract.setContractStatus("TERMINATED");
            contract.setTerminationDate(new Date());
            contract.setRemark(reason); // 将终止原因记录到备注中
            contract.setUpdatedTime(new Date());
            creditContractMapper.updateById(contract);
        } catch (Exception e) {
            log.error("终止授信合同失败, contractId: {}", contractId, e);
            throw new RuntimeException("终止授信合同失败: " + e.getMessage());
        }
    }

    @Override
    public List<TblCreditContract> getExpiringContracts(Integer days) {
        log.info("查询即将到期的合同, days: {}", days);
        // TODO: 实现查询逻辑
        return new java.util.ArrayList<>();
    }

    @Override
    public Map<String, Object> getContractSummary(Long companyId) {
        log.info("查询授信合同汇总, companyId: {}", companyId);
        // TODO: 实现汇总逻辑
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalContracts", 0);
        summary.put("activeContracts", 0);
        summary.put("totalAmount", 0);
        return summary;
    }

    @Override
    public void exportContract(CreditContractQueryDTO queryDTO, javax.servlet.http.HttpServletResponse response) {
        log.info("导出授信合同, queryDTO: {}", queryDTO);
        try {
            // 查询数据（不分页，获取全部数据）
            QueryWrapper<TblCreditContract> wrapper = new QueryWrapper<>();
            wrapper.eq("DELETE_FLAG", 0);

            // 合同编号
            if (queryDTO.getContractNo() != null && !queryDTO.getContractNo().isEmpty()) {
                wrapper.like("CONTRACT_NO", queryDTO.getContractNo());
            }

            // 银行代码
            if (queryDTO.getBankCode() != null && !queryDTO.getBankCode().isEmpty()) {
                wrapper.eq("BANK_CODE", queryDTO.getBankCode());
            }

            // 银行名称
            if (queryDTO.getBankName() != null && !queryDTO.getBankName().isEmpty()) {
                wrapper.like("BANK_NAME", queryDTO.getBankName());
            }

            // 合同状态
            if (queryDTO.getContractStatus() != null && !queryDTO.getContractStatus().isEmpty()) {
                wrapper.eq("CONTRACT_STATUS", queryDTO.getContractStatus());
            }

            // 公司ID
            if (queryDTO.getCompanyId() != null) {
                wrapper.eq("COMPANY_ID", queryDTO.getCompanyId());
            }

            // 公司名称
            if (queryDTO.getCompanyName() != null && !queryDTO.getCompanyName().isEmpty()) {
                wrapper.like("COMPANY_NAME", queryDTO.getCompanyName());
            }

            wrapper.orderByDesc("CREATED_TIME");

            List<TblCreditContract> dataList = creditContractMapper.selectList(wrapper);

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = java.net.URLEncoder.encode("授信合同列表", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 使用EasyExcel导出
            com.alibaba.excel.EasyExcel.write(response.getOutputStream(), TblCreditContract.class)
                    .sheet("授信合同")
                    .doWrite(dataList);

        } catch (Exception e) {
            log.error("导出授信合同失败", e);
            throw new RuntimeException("导出授信合同失败: " + e.getMessage());
        }
    }
}
