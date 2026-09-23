package com.financial.sharing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.entity.TblTravelStandardDetail;
import java.util.List;

/**
 * 差旅标准明细Service接口
 */
public interface TblTravelStandardDetailService extends IService<TblTravelStandardDetail> {
    
    /**
     * 根据标准ID查询明细
     */
    List<TblTravelStandardDetail> getByStandardId(String standardId);
    
    /**
     * 根据标准ID和费用类型查询
     */
    TblTravelStandardDetail getByStandardAndExpenseType(String standardId, String expenseType);
    
    /**
     * 批量保存明细
     */
    boolean saveBatchDetails(List<TblTravelStandardDetail> details);
    
    /**
     * 删除标准的所有明细
     */
    boolean deleteByStandardId(String standardId);
}

