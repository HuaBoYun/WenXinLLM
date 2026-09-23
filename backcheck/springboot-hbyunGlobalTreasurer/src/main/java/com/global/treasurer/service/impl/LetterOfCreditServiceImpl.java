package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.LetterOfCreditDTO;
import com.global.treasurer.dto.LetterOfCreditQueryDTO;
import com.global.treasurer.entity.TblLetterOfCredit;
import com.global.treasurer.mapper.LetterOfCreditMapper;
import com.global.treasurer.service.ILetterOfCreditService;
import com.global.treasurer.vo.LetterOfCreditVO;
import com.hbfk.util.BizException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 信用证Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Service
public class LetterOfCreditServiceImpl extends ServiceImpl<LetterOfCreditMapper, TblLetterOfCredit>
        implements ILetterOfCreditService {
    @Override
    public PageInfo<LetterOfCreditVO> selectLetterOfCreditList(LetterOfCreditQueryDTO queryDTO) {
        if (queryDTO == null) {
            throw new BizException("查询参数不能为空");
        }
        PageHelper.startPage(queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1,
                            queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10);
        List<LetterOfCreditVO> list = baseMapper.selectLetterOfCreditList(queryDTO);
        return new PageInfo<>(list);
    }

    @Override
    public LetterOfCreditVO selectLetterOfCreditById(Long lcId) {
        if (lcId == null) {
            throw new BizException("信用证ID不能为空");
        }
        return baseMapper.selectLetterOfCreditById(lcId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblLetterOfCredit insertLetterOfCredit(LetterOfCreditDTO dto) {
        TblLetterOfCredit lc = new TblLetterOfCredit();
        BeanUtils.copyProperties(dto, lc);
        
        lc.setLcNumber(generateLcNumber());
        lc.setLcStatus("DRAFT");
        lc.setDeleteFlag(0);
        lc.setCreateTime(new Date());
        lc.setUpdateTime(new Date());
        lc.setLcId(System.currentTimeMillis());
        
        baseMapper.insert(lc);
        return lc;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblLetterOfCredit updateLetterOfCredit(LetterOfCreditDTO dto) {
        if (dto.getLcId() == null) {
            throw new BizException("信用证ID不能为空");
        }
        TblLetterOfCredit exist = baseMapper.selectById(dto.getLcId());
        if (exist == null || exist.getDeleteFlag() == 1) {
            throw new BizException("信用证不存在或已删除");
        }
        
        TblLetterOfCredit lc = new TblLetterOfCredit();
        BeanUtils.copyProperties(dto, lc);
        lc.setUpdateTime(new Date());
        baseMapper.updateById(lc);
        return lc;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLetterOfCreditByIds(Long[] lcIds) {
        if (lcIds == null || lcIds.length == 0) {
            throw new BizException("请选择要删除的信用证");
        }
        return baseMapper.deleteLetterOfCreditByIds(lcIds) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitLCApplication(Long lcId) {
        TblLetterOfCredit lc = baseMapper.selectById(lcId);
        if (lc == null) {
            throw new BizException("信用证不存在");
        }
        lc.setLcStatus("SUBMITTED");
        lc.setSubmitTime(new Date());
        lc.setUpdateTime(new Date());
        return baseMapper.updateById(lc) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveLCApplication(Long lcId, Map<String, Object> approvalData) {
        TblLetterOfCredit lc = baseMapper.selectById(lcId);
        if (lc == null) {
            throw new BizException("信用证不存在");
        }
        String approved = (String) approvalData.get("approved");
        lc.setLcStatus("true".equals(approved) ? "APPROVED" : "REJECTED");
        lc.setApprovalComment((String) approvalData.get("comment"));
        lc.setApprovalTime(new Date());
        lc.setUpdateTime(new Date());
        return baseMapper.updateById(lc) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean issueLetterOfCredit(Long lcId) {
        TblLetterOfCredit lc = baseMapper.selectById(lcId);
        if (lc == null) {
            throw new BizException("信用证不存在");
        }
        if (!"APPROVED".equals(lc.getLcStatus())) {
            throw new BizException("只能开立已审批通过的信用证");
        }
        lc.setLcStatus("ISSUED");
        lc.setIssueDate(new Date());
        lc.setUpdateTime(new Date());
        return baseMapper.updateById(lc) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean amendLetterOfCredit(Long lcId, Map<String, Object> amendData) {
        TblLetterOfCredit lc = baseMapper.selectById(lcId);
        if (lc == null) {
            throw new BizException("信用证不存在");
        }
        // TODO: 实现信用证修改逻辑
        lc.setUpdateTime(new Date());
        return baseMapper.updateById(lc) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processDocuments(Long lcId, Map<String, Object> documentData) {
        TblLetterOfCredit lc = baseMapper.selectById(lcId);
        if (lc == null) {
            throw new BizException("信用证不存在");
        }
        // TODO: 实现单据处理逻辑
        return true;
    }

    @Override
    public List<Map<String, Object>> getDocumentList(Long lcId, Map<String, Object> params) {
        // TODO: 实现单据列表查询
        return new ArrayList<>();
    }

    private String generateLcNumber() {
        return "LC" + System.currentTimeMillis();
    }
}

