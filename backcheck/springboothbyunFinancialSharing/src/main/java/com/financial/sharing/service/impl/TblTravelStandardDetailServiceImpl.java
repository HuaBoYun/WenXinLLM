package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.entity.TblTravelStandardDetail;
import com.financial.sharing.mapper.TblTravelStandardDetailMapper;
import com.financial.sharing.service.TblTravelStandardDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 差旅标准明细Service实现
 */
@Slf4j
@Service
public class TblTravelStandardDetailServiceImpl extends ServiceImpl<TblTravelStandardDetailMapper, TblTravelStandardDetail> 
        implements TblTravelStandardDetailService {

    @Override
    public List<TblTravelStandardDetail> getByStandardId(String standardId) {
        return this.baseMapper.selectByStandardId(standardId);
    }

    @Override
    public TblTravelStandardDetail getByStandardAndExpenseType(String standardId, String expenseType) {
        return this.baseMapper.selectByStandardAndExpenseType(standardId, expenseType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatchDetails(List<TblTravelStandardDetail> details) {
        try {
            return this.saveBatch(details);
        } catch (Exception e) {
            log.error("批量保存差旅标准明细失败", e);
            throw new RuntimeException("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByStandardId(String standardId) {
        try {
            return this.baseMapper.deleteByStandardId(standardId) > 0;
        } catch (Exception e) {
            log.error("删除差旅标准明细失败", e);
            throw new RuntimeException("删除失败: " + e.getMessage());
        }
    }
}

