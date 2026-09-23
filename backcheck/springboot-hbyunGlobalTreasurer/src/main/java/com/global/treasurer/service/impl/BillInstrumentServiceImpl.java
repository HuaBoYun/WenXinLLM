package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BillInstrumentDTO;
import com.global.treasurer.dto.BillInstrumentQueryDTO;
import com.global.treasurer.entity.TblBillInstrument;
import com.global.treasurer.mapper.BillInstrumentMapper;
import com.global.treasurer.service.IBillInstrumentService;
import com.global.treasurer.vo.BillInstrumentVO;
import com.hbfk.util.BizException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 票据管理通用Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Service
public class BillInstrumentServiceImpl extends ServiceImpl<BillInstrumentMapper, TblBillInstrument>
        implements IBillInstrumentService {
    @Override
    public PageInfo<BillInstrumentVO> selectBillInstrumentList(BillInstrumentQueryDTO queryDTO) {
        if (queryDTO == null) {
            throw new BizException("查询参数不能为空");
        }
        PageHelper.startPage(queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1,
                            queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10);
        List<BillInstrumentVO> list = baseMapper.selectBillInstrumentList(queryDTO);
        return new PageInfo<>(list);
    }

    @Override
    public BillInstrumentVO selectBillInstrumentById(Long instrumentId) {
        if (instrumentId == null) {
            throw new BizException("票据ID不能为空");
        }
        return baseMapper.selectBillInstrumentById(instrumentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblBillInstrument insertBillInstrument(BillInstrumentDTO dto) {
        TblBillInstrument instrument = new TblBillInstrument();
        BeanUtils.copyProperties(dto, instrument);

        // 生成票据编号
        instrument.setInstrumentNumber(generateInstrumentNumber());
        instrument.setInstrumentStatus("DRAFT");
        instrument.setDeleteFlag(0);
        instrument.setCreateTime(new Date());
        instrument.setUpdateTime(new Date());
        instrument.setInstrumentId(System.currentTimeMillis());

        baseMapper.insert(instrument);
        return instrument;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblBillInstrument updateBillInstrument(BillInstrumentDTO dto) {
        if (dto.getInstrumentId() == null) {
            throw new BizException("票据ID不能为空");
        }
        TblBillInstrument exist = baseMapper.selectById(dto.getInstrumentId());
        if (exist == null || exist.getDeleteFlag() == 1) {
            throw new BizException("票据不存在或已删除");
        }

        TblBillInstrument instrument = new TblBillInstrument();
        BeanUtils.copyProperties(dto, instrument);
        instrument.setUpdateTime(new Date());
        baseMapper.updateById(instrument);
        return instrument;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBillInstrumentByIds(Long[] instrumentIds) {
        if (instrumentIds == null || instrumentIds.length == 0) {
            throw new BizException("请选择要删除的票据");
        }
        return baseMapper.deleteBillInstrumentByIds(instrumentIds) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean endorseBill(Long instrumentId, Map<String, Object> endorseData) {
        if (instrumentId == null) {
            throw new BizException("票据ID不能为空");
        }
        TblBillInstrument instrument = baseMapper.selectById(instrumentId);
        if (instrument == null || instrument.getDeleteFlag() == 1) {
            throw new BizException("票据不存在或已删除");
        }

        // 更新票据状态为已背书
        instrument.setInstrumentStatus("ENDORSED");
        instrument.setUpdateTime(new Date());

        // 如果有被背书人信息，更新收款人
        if (endorseData != null && endorseData.get("endorsee") != null) {
            instrument.setPayee(endorseData.get("endorsee").toString());
        }

        return baseMapper.updateById(instrument) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean discountBill(Long instrumentId, Map<String, Object> discountData) {
        if (instrumentId == null) {
            throw new BizException("票据ID不能为空");
        }
        TblBillInstrument instrument = baseMapper.selectById(instrumentId);
        if (instrument == null || instrument.getDeleteFlag() == 1) {
            throw new BizException("票据不存在或已删除");
        }

        // 更新票据状态为已贴现
        instrument.setInstrumentStatus("DISCOUNTED");
        instrument.setUpdateTime(new Date());

        return baseMapper.updateById(instrument) > 0;
    }

    private String generateInstrumentNumber() {
        return "INS" + System.currentTimeMillis();
    }
}

