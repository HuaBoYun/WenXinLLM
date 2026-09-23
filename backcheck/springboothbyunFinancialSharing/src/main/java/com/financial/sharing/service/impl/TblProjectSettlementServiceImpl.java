package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.entity.TblProjectSettlement;
import com.financial.sharing.mapper.TblProjectSettlementMapper;
import com.financial.sharing.service.TblProjectSettlementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 项目结算Service实现
 */
@Slf4j
@Service
public class TblProjectSettlementServiceImpl extends ServiceImpl<TblProjectSettlementMapper, TblProjectSettlement> 
        implements TblProjectSettlementService {

    @Override
    public List<TblProjectSettlement> getByProjectId(String projectId) {
        return this.baseMapper.selectByProjectId(projectId);
    }

    @Override
    public List<TblProjectSettlement> getBySettlementStatus(String settlementStatus) {
        return this.baseMapper.selectBySettlementStatus(settlementStatus);
    }

    @Override
    public TblProjectSettlement getLatestSettlement(String projectId) {
        return this.baseMapper.selectLatestSettlement(projectId);
    }
}

