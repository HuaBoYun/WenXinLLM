package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BillEndorsementDTO;
import com.global.treasurer.dto.BillEndorsementQueryDTO;
import com.global.treasurer.entity.TblBillEndorsement;
import com.global.treasurer.mapper.BillEndorsementMapper;
import com.global.treasurer.service.IBillEndorsementService;
import com.global.treasurer.vo.BillEndorsementVO;
import com.hbfk.util.BizException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 票据背书Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@Service
public class BillEndorsementServiceImpl extends ServiceImpl<BillEndorsementMapper, TblBillEndorsement>
        implements IBillEndorsementService {
    @Override
    public PageInfo<BillEndorsementVO> selectBillEndorsementList(BillEndorsementQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<BillEndorsementVO> list = baseMapper.selectBillEndorsementList(queryDTO);
        return new PageInfo<>(list);
    }

    @Override
    public BillEndorsementVO selectBillEndorsementById(Long endorsementId) {
        if (endorsementId == null) {
            throw new BizException("背书ID不能为空");
        }
        return baseMapper.selectBillEndorsementById(endorsementId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblBillEndorsement insertBillEndorsement(BillEndorsementDTO dto) {
        TblBillEndorsement endorsement = new TblBillEndorsement();
        BeanUtils.copyProperties(dto, endorsement);
        
        // 生成背书编号
        endorsement.setEndorsementNumber(generateEndorsementNumber());
        endorsement.setEndorsementStatus("PENDING");
        endorsement.setDeleteFlag(0);
        endorsement.setCreateTime(new Date());
        endorsement.setUpdateTime(new Date());
        endorsement.setApplicationDate(new Date());
        endorsement.setEndorsementId(System.currentTimeMillis());
        
        baseMapper.insert(endorsement);
        return endorsement;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblBillEndorsement updateBillEndorsement(BillEndorsementDTO dto) {
        if (dto.getEndorsementId() == null) {
            throw new BizException("背书ID不能为空");
        }
        TblBillEndorsement exist = baseMapper.selectById(dto.getEndorsementId());
        if (exist == null || exist.getDeleteFlag() == 1) {
            throw new BizException("背书记录不存在或已删除");
        }
        if (!"PENDING".equals(exist.getEndorsementStatus())) {
            throw new BizException("只能修改待审批状态的背书记录");
        }
        TblBillEndorsement endorsement = new TblBillEndorsement();
        BeanUtils.copyProperties(dto, endorsement);
        endorsement.setUpdateTime(new Date());
        baseMapper.updateById(endorsement);
        return endorsement;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBillEndorsementByIds(Long[] endorsementIds) {
        if (endorsementIds == null || endorsementIds.length == 0) {
            throw new BizException("请选择要删除的背书记录");
        }
        return baseMapper.deleteBillEndorsementByIds(endorsementIds) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveBillEndorsement(Long endorsementId, Map<String, Object> approvalData) {
        TblBillEndorsement endorsement = baseMapper.selectById(endorsementId);
        if (endorsement == null) {
            throw new BizException("背书记录不存在");
        }
        String approved = (String) approvalData.get("approved");
        endorsement.setEndorsementStatus("true".equals(approved) ? "APPROVED" : "REJECTED");
        endorsement.setApprovalComment((String) approvalData.get("comment"));
        endorsement.setApprovalDate(new Date());
        endorsement.setUpdateTime(new Date());
        return baseMapper.updateById(endorsement) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean executeBillEndorsement(Long endorsementId) {
        TblBillEndorsement endorsement = baseMapper.selectById(endorsementId);
        if (endorsement == null) {
            throw new BizException("背书记录不存在");
        }
        if (!"APPROVED".equals(endorsement.getEndorsementStatus())) {
            throw new BizException("只能执行已审批通过的背书");
        }
        endorsement.setEndorsementStatus("COMPLETED");
        endorsement.setActualExecutionDate(new Date());
        endorsement.setUpdateTime(new Date());
        return baseMapper.updateById(endorsement) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelBillEndorsement(Long endorsementId, String reason) {
        TblBillEndorsement endorsement = baseMapper.selectById(endorsementId);
        if (endorsement == null) {
            throw new BizException("背书记录不存在");
        }
        endorsement.setEndorsementStatus("CANCELLED");
        endorsement.setRemark(reason);
        endorsement.setUpdateTime(new Date());
        return baseMapper.updateById(endorsement) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchApproveBillEndorsement(List<Long> endorsementIds, Map<String, Object> approvalData) {
        for (Long id : endorsementIds) {
            approveBillEndorsement(id, approvalData);
        }
        return true;
    }

    @Override
    public List<Map<String, Object>> getAvailableBillsForEndorsement(Map<String, Object> params) {
        return new ArrayList<>();
    }

    private String generateEndorsementNumber() {
        return "BS" + System.currentTimeMillis();
    }
}

