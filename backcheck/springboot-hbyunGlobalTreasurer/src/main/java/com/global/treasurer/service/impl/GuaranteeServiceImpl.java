package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.GuaranteeDTO;
import com.global.treasurer.dto.GuaranteeQueryDTO;
import com.global.treasurer.entity.TblGuarantee;
import com.global.treasurer.mapper.GuaranteeMapper;
import com.global.treasurer.service.IGuaranteeService;
import com.global.treasurer.vo.GuaranteeVO;
import com.hbfk.util.BizException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 保函Service实现类
 *
 * 数据库表 TBL_GUARANTEE 实际字段：
 * - COMPANY_ID VARCHAR2(10) NOT NULL
 * - GUARANTEE_ID VARCHAR2(10) NOT NULL (主键)
 * - GUARANTEE_TARGET VARCHAR2(100) (受益人/保函目标)
 * - GUARANTEE_AMOUNT NUMBER (保函金额)
 * - GUARANTEE_DATE DATE (保函日期)
 * - GUARANTEE_STATUS VARCHAR2(20) (保函状态)
 * - GUARANTEE_TYPE VARCHAR2(20) (保函类型)
 * - CREATE_TIME DATE (创建时间)
 * - GUARANTEED_PARTY_ID VARCHAR(50) (被担保方ID/申请人)
 *
 * @author 华博云开发团队
 * @since 2026-02-02
 */
@Service
public class GuaranteeServiceImpl extends ServiceImpl<GuaranteeMapper, TblGuarantee>
        implements IGuaranteeService {
    @Override
    public PageInfo<GuaranteeVO> selectGuaranteeList(GuaranteeQueryDTO queryDTO) {
        if (queryDTO == null) {
            throw new BizException("查询参数不能为空");
        }
        PageHelper.startPage(queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1,
                            queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10);
        List<GuaranteeVO> list = baseMapper.selectGuaranteeList(queryDTO);
        return new PageInfo<>(list);
    }

    @Override
    public GuaranteeVO selectGuaranteeById(String guaranteeId) {
        if (guaranteeId == null || guaranteeId.trim().isEmpty()) {
            throw new BizException("保函ID不能为空");
        }
        return baseMapper.selectGuaranteeById(guaranteeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblGuarantee insertGuarantee(GuaranteeDTO dto) {
        TblGuarantee guarantee = new TblGuarantee();

        // 只设置数据库中实际存在的字段
        // 生成保函ID (VARCHAR2(10)，最多10位)
        String guaranteeId = "G" + String.valueOf(System.currentTimeMillis()).substring(5);
        guarantee.setGuaranteeId(guaranteeId);

        // 公司ID (必填字段)
        guarantee.setCompanyId(dto.getCompanyId() != null ? dto.getCompanyId() : "C001");

        // 保函目标/受益人
        guarantee.setGuaranteeTarget(dto.getBeneficiary());

        // 保函金额
        guarantee.setGuaranteeAmount(dto.getGuaranteeAmount());

        // 保函日期
        guarantee.setGuaranteeDate(dto.getExpiryDate() != null ? dto.getExpiryDate() : new Date());

        // 保函状态
        guarantee.setGuaranteeStatus("DRAFT");

        // 保函类型
        guarantee.setGuaranteeType(dto.getGuaranteeType());

        // 创建时间
        guarantee.setCreateTime(new Date());

        // 被担保方ID/申请人
        guarantee.setGuaranteedPartyId(dto.getApplicant());

        baseMapper.insert(guarantee);
        return guarantee;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblGuarantee updateGuarantee(GuaranteeDTO dto) {
        if (dto.getGuaranteeId() == null || dto.getGuaranteeId().trim().isEmpty()) {
            throw new BizException("保函ID不能为空");
        }
        String guaranteeId = dto.getGuaranteeId();
        TblGuarantee exist = baseMapper.selectById(guaranteeId);
        if (exist == null) {
            throw new BizException("保函不存在或已删除");
        }

        // 只更新数据库中实际存在的字段
        if (dto.getBeneficiary() != null) {
            exist.setGuaranteeTarget(dto.getBeneficiary());
        }
        if (dto.getGuaranteeAmount() != null) {
            exist.setGuaranteeAmount(dto.getGuaranteeAmount());
        }
        if (dto.getExpiryDate() != null) {
            exist.setGuaranteeDate(dto.getExpiryDate());
        }
        if (dto.getGuaranteeType() != null) {
            exist.setGuaranteeType(dto.getGuaranteeType());
        }
        if (dto.getApplicant() != null) {
            exist.setGuaranteedPartyId(dto.getApplicant());
        }

        baseMapper.updateById(exist);
        return exist;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteGuaranteeByIds(String[] guaranteeIds) {
        if (guaranteeIds == null || guaranteeIds.length == 0) {
            throw new BizException("请选择要删除的保函");
        }
        return baseMapper.deleteGuaranteeByIds(guaranteeIds) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitGuaranteeApplication(String guaranteeId) {
        TblGuarantee guarantee = baseMapper.selectById(guaranteeId);
        if (guarantee == null) {
            throw new BizException("保函不存在");
        }
        guarantee.setGuaranteeStatus("SUBMITTED");
        return baseMapper.updateById(guarantee) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveGuaranteeApplication(String guaranteeId, Map<String, Object> approvalData) {
        TblGuarantee guarantee = baseMapper.selectById(guaranteeId);
        if (guarantee == null) {
            throw new BizException("保函不存在");
        }
        String approved = (String) approvalData.get("approved");
        guarantee.setGuaranteeStatus("true".equals(approved) ? "APPROVED" : "REJECTED");
        return baseMapper.updateById(guarantee) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean issueGuarantee(String guaranteeId) {
        TblGuarantee guarantee = baseMapper.selectById(guaranteeId);
        if (guarantee == null) {
            throw new BizException("保函不存在");
        }
        if (!"APPROVED".equals(guarantee.getGuaranteeStatus())) {
            throw new BizException("只能开立已审批通过的保函");
        }
        guarantee.setGuaranteeStatus("ISSUED");
        guarantee.setGuaranteeDate(new Date());
        return baseMapper.updateById(guarantee) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean claimGuarantee(String guaranteeId, Map<String, Object> claimData) {
        TblGuarantee guarantee = baseMapper.selectById(guaranteeId);
        if (guarantee == null) {
            throw new BizException("保函不存在");
        }
        if (!"ISSUED".equals(guarantee.getGuaranteeStatus()) && !"EFFECTIVE".equals(guarantee.getGuaranteeStatus())) {
            throw new BizException("只能对已开立或生效的保函进行索赔");
        }
        guarantee.setGuaranteeStatus("CLAIMED");
        return baseMapper.updateById(guarantee) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean releaseGuarantee(String guaranteeId, String releaseReason) {
        TblGuarantee guarantee = baseMapper.selectById(guaranteeId);
        if (guarantee == null) {
            throw new BizException("保函不存在");
        }
        guarantee.setGuaranteeStatus("RELEASED");
        return baseMapper.updateById(guarantee) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelGuarantee(String guaranteeId, String cancelReason) {
        TblGuarantee guarantee = baseMapper.selectById(guaranteeId);
        if (guarantee == null) {
            throw new BizException("保函不存在");
        }
        guarantee.setGuaranteeStatus("CANCELLED");
        return baseMapper.updateById(guarantee) > 0;
    }
}

