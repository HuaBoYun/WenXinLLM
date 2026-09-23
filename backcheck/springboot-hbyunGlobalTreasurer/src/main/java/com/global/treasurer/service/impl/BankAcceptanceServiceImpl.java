package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BankAcceptanceDTO;
import com.global.treasurer.dto.BankAcceptanceQueryDTO;
import com.global.treasurer.entity.TblBankAcceptance;
import com.global.treasurer.mapper.BankAcceptanceMapper;
import com.global.treasurer.service.IBankAcceptanceService;
import com.global.treasurer.vo.BankAcceptanceVO;
import com.hbfk.util.BizException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 银行承兑汇票Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Service
public class BankAcceptanceServiceImpl extends ServiceImpl<BankAcceptanceMapper, TblBankAcceptance>
        implements IBankAcceptanceService {
    @Override
    public PageInfo<BankAcceptanceVO> selectBankAcceptanceList(BankAcceptanceQueryDTO queryDTO) {
        if (queryDTO == null) {
            throw new BizException("查询参数不能为空");
        }
        PageHelper.startPage(queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1,
                            queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10);
        List<BankAcceptanceVO> list = baseMapper.selectBankAcceptanceList(queryDTO);
        return new PageInfo<>(list);
    }

    @Override
    public BankAcceptanceVO selectBankAcceptanceById(Long acceptanceId) {
        if (acceptanceId == null) {
            throw new BizException("承兑汇票ID不能为空");
        }
        return baseMapper.selectBankAcceptanceById(acceptanceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblBankAcceptance insertBankAcceptance(BankAcceptanceDTO dto) {
        TblBankAcceptance acceptance = new TblBankAcceptance();
        BeanUtils.copyProperties(dto, acceptance);
        
        // 如果用户输入了票据编号则使用用户输入的，否则自动生成
        if (acceptance.getAcceptanceNumber() == null || acceptance.getAcceptanceNumber().trim().isEmpty()) {
            acceptance.setAcceptanceNumber(generateAcceptanceNumber());
        }
        acceptance.setAcceptanceStatus("DRAFT");
        acceptance.setDeleteFlag(0);
        acceptance.setCreateTime(new Date());
        acceptance.setUpdateTime(new Date());
        acceptance.setAcceptanceId(System.currentTimeMillis());
        
        baseMapper.insert(acceptance);
        return acceptance;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblBankAcceptance updateBankAcceptance(BankAcceptanceDTO dto) {
        if (dto.getAcceptanceId() == null) {
            throw new BizException("承兑汇票ID不能为空");
        }
        TblBankAcceptance exist = baseMapper.selectById(dto.getAcceptanceId());
        if (exist == null || exist.getDeleteFlag() == 1) {
            throw new BizException("承兑汇票不存在或已删除");
        }
        
        TblBankAcceptance acceptance = new TblBankAcceptance();
        BeanUtils.copyProperties(dto, acceptance);
        acceptance.setUpdateTime(new Date());
        baseMapper.updateById(acceptance);
        return acceptance;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBankAcceptanceByIds(Long[] acceptanceIds) {
        if (acceptanceIds == null || acceptanceIds.length == 0) {
            throw new BizException("请选择要删除的承兑汇票");
        }
        return baseMapper.deleteBankAcceptanceByIds(acceptanceIds) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitAcceptanceApplication(Long acceptanceId) {
        TblBankAcceptance acceptance = baseMapper.selectById(acceptanceId);
        if (acceptance == null) {
            throw new BizException("承兑汇票不存在");
        }
        acceptance.setAcceptanceStatus("SUBMITTED");
        acceptance.setSubmitTime(new Date());
        acceptance.setUpdateTime(new Date());
        return baseMapper.updateById(acceptance) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveAcceptanceApplication(Long acceptanceId, Map<String, Object> approvalData) {
        TblBankAcceptance acceptance = baseMapper.selectById(acceptanceId);
        if (acceptance == null) {
            throw new BizException("承兑汇票不存在");
        }
        String approved = (String) approvalData.get("approved");
        acceptance.setAcceptanceStatus("true".equals(approved) ? "APPROVED" : "REJECTED");
        acceptance.setApprovalComment((String) approvalData.get("comment"));
        acceptance.setApprovalTime(new Date());
        acceptance.setUpdateTime(new Date());
        return baseMapper.updateById(acceptance) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean issueAcceptance(Long acceptanceId) {
        TblBankAcceptance acceptance = baseMapper.selectById(acceptanceId);
        if (acceptance == null) {
            throw new BizException("承兑汇票不存在");
        }
        if (!"APPROVED".equals(acceptance.getAcceptanceStatus())) {
            throw new BizException("只能开立已审批通过的承兑汇票");
        }
        acceptance.setAcceptanceStatus("ISSUED");
        acceptance.setIssueDate(new Date());
        acceptance.setUpdateTime(new Date());
        return baseMapper.updateById(acceptance) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean manageMargin(Long acceptanceId, Map<String, Object> marginData) {
        TblBankAcceptance acceptance = baseMapper.selectById(acceptanceId);
        if (acceptance == null) {
            throw new BizException("承兑汇票不存在");
        }
        // TODO: 实现保证金管理逻辑
        acceptance.setUpdateTime(new Date());
        return baseMapper.updateById(acceptance) > 0;
    }

    @Override
    public Map<String, Object> getMarginInfo(Long acceptanceId) {
        TblBankAcceptance acceptance = baseMapper.selectById(acceptanceId);
        if (acceptance == null) {
            throw new BizException("承兑汇票不存在");
        }
        Map<String, Object> marginInfo = new HashMap<>();
        marginInfo.put("acceptanceId", acceptanceId);
        marginInfo.put("applicationNo", acceptance.getAcceptanceNumber());  // 申请编号
        marginInfo.put("faceAmount", acceptance.getAcceptanceAmount());     // 票面金额
        marginInfo.put("marginRatio", acceptance.getMarginRate());          // 保证金比例
        marginInfo.put("marginAmount", acceptance.getMarginAmount());       // 保证金金额
        marginInfo.put("marginAccount", "");                                // 保证金账户（如果有的话）
        return marginInfo;
    }

    private String generateAcceptanceNumber() {
        return "BA" + System.currentTimeMillis();
    }
}

