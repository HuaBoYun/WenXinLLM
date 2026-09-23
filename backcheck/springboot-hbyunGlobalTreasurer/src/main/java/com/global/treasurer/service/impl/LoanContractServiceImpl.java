package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.LoanContractDTO;
import com.global.treasurer.entity.TblLoanContract;
import com.global.treasurer.mapper.LoanContractMapper;
import com.global.treasurer.service.LoanContractService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 贷款合同Service实现类
 * 对应数据库表: TBL_LOAN_CONTRACT
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
@Service
public class LoanContractServiceImpl implements LoanContractService {

    @Autowired
    private LoanContractMapper loanContractMapper;

    @Override
    public PageInfo<TblLoanContract> getContractList(LoanContractDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        Map<String, Object> params = new HashMap<>();
        params.put("loanId", dto.getLoanId());
        params.put("contractNo", dto.getContractNo());
        params.put("contractStatus", dto.getContractStatus());
        List<TblLoanContract> list = loanContractMapper.selectContractList(params);
        return new PageInfo<>(list);
    }

    @Override
    public List<TblLoanContract> getContractsByLoanId(String loanId) {
        return loanContractMapper.selectByLoanId(loanId);
    }

    @Override
    public TblLoanContract getContractById(String contractId) {
        return loanContractMapper.selectById(contractId);
    }

    @Override
    @Transactional
    public TblLoanContract saveContract(LoanContractDTO dto) {
        TblLoanContract contract = new TblLoanContract();
        BeanUtils.copyProperties(dto, contract);

        // 处理日期转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (StringUtils.hasText(dto.getSigningDate())) {
                contract.setSigningDate(sdf.parse(dto.getSigningDate()));
            }
            if (StringUtils.hasText(dto.getEffectiveDate())) {
                contract.setEffectiveDate(sdf.parse(dto.getEffectiveDate()));
            }
            if (StringUtils.hasText(dto.getExpiryDate())) {
                contract.setExpiryDate(sdf.parse(dto.getExpiryDate()));
            }
        } catch (Exception e) {
            throw new RuntimeException("日期格式错误", e);
        }

        if (StringUtils.hasText(dto.getContractId())) {
            // 更新
            contract.setUpdateTime(new Date());
            loanContractMapper.updateById(contract);
        } else {
            // 新增
            if (!StringUtils.hasText(contract.getContractNo())) {
                contract.setContractNo(generateContractNo());
            }
            if (!StringUtils.hasText(contract.getContractStatus())) {
                contract.setContractStatus("DRAFT");
            }
            if (!StringUtils.hasText(contract.getContractCompleteness())) {
                contract.setContractCompleteness("COMPLETE");
            }
            contract.setCreateTime(new Date());
            contract.setUpdateTime(new Date());
            loanContractMapper.insert(contract);
        }
        return contract;
    }

    @Override
    @Transactional
    public void deleteContract(String contractId) {
        // 现有表没有DELETE_FLAG字段，直接物理删除
        loanContractMapper.deleteById(contractId);
    }

    @Override
    @Transactional
    public void updateContractStatus(String contractId, String status) {
        TblLoanContract contract = loanContractMapper.selectById(contractId);
        if (contract != null) {
            contract.setContractStatus(status);
            contract.setUpdateTime(new Date());
            loanContractMapper.updateById(contract);
        }
    }

    @Override
    public String generateContractNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return "HT" + sdf.format(new Date()) + String.format("%04d", new Random().nextInt(10000));
    }
}

